package com.pda.carmanager.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.mingle.headsUp.HeadsUp;
import com.mingle.headsUp.HeadsUpManager;
import com.pda.carmanager.R;
import com.pda.carmanager.bean.MsgBean;
import com.pda.carmanager.bean.MyParkBean;
import com.pda.carmanager.bean.PayCallBackBean;
import com.pda.carmanager.config.AccountConfig;
import com.pda.carmanager.config.UrlConfig;
import com.pda.carmanager.inter.ISignalAInter;
import com.pda.carmanager.util.PreferenceUtils;
import com.pda.carmanager.util.StringEqualUtil;
import com.pda.carmanager.view.activity.ContentActivity;
import com.pda.carmanager.view.activity.MainActivity;
import com.pda.carmanager.view.activity.MyParkActivity;
import com.pda.carmanager.view.activity.PaySuccessActivity;
import com.zsoft.signala.hubs.HubConnection;
import com.zsoft.signala.hubs.HubInvokeCallback;
import com.zsoft.signala.hubs.HubOnDataCallback;
import com.zsoft.signala.hubs.IHubProxy;
import com.zsoft.signala.transport.StateBase;
import com.zsoft.signala.transport.longpolling.LongPollingTransport;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP;
import static com.zsoft.signala.ConnectionState.Connected;

/**
 * Created by Admin on 2017/12/21.
 */

public class SignalAService extends Service {
    private IHubProxy hub = null;
    private boolean isHub = true;
    private int code = 1;
    // 通知栏消息 数量
    private int messageNotificationID = 0;
    private ISignalAInter iSignalAInter;
    private CarObservable carObservable;
    private NewsObservable newsObservable;
    private PayObservable payObservable;
    private SoundPool sp;//声明一个SoundPool
    private boolean loaded = false;
    private boolean isSpeaking = false;
    private Map<Integer, Integer> sounddata;//

    public final class LocalBinder extends Binder {
        public SignalAService getService() {
            return SignalAService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new LocalBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        isHub = true;
        carObservable = new CarObservable();
        newsObservable = new NewsObservable();
        payObservable = new PayObservable();

        InitSound();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getAction() != null) {
            if (intent.getAction().equals("Connect")) {
                if (conn != null) {
                    isHub=true;
                    beginConnect();
                }
            }
            if (intent.getAction().equals("Disconnect")) {
                if (conn != null) {
                    conn.Stop();
                    isHub = false;
                }
            }
        }
        return super.onStartCommand(intent, flags, startId);

    }

    //初始化声音
    public void InitSound() {
        sp = new SoundPool(3, AudioManager.STREAM_MUSIC, 0);
        sounddata = new HashMap<Integer, Integer>();
        sounddata.put(1, sp.load(this, R.raw.car_come, 1));
        sounddata.put(2, sp.load(this, R.raw.car_go, 1));
        sounddata.put(3, sp.load(this, R.raw.message_come, 1));
        sp.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool sound, int sampleId, int status) {
                loaded = true;
            }
        });
    }


    public void playSound(int sound, int number) {
        AudioManager am = (AudioManager) this
                .getSystemService(Context.AUDIO_SERVICE);
        float audioMaxVolumn = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        float volumnCurrent = am.getStreamVolume(AudioManager.STREAM_MUSIC);
        float volumnRatio = volumnCurrent / audioMaxVolumn;

        sp.play(sounddata.get(sound),
                1,// 左声道音量
                1,// 右声道音量
                1, // 优先级
                0,// 循环播放次数
                1);// 回放速度，该值在0.5-2.0之间 1为正常速度
    }

    /**
     * 开启推送服务 panderman 2013-10-25
     */
    private void beginConnect() {
        if (!conn.getCurrentState().getState().equals(Connected)) {
            try {
                hub = conn.CreateHubProxy("ChatHub");
            } catch (OperationApplicationException e) {
                e.printStackTrace();
            }
            hub.On("callBack_CarIn", new HubOnDataCallback() {
                @Override
                public void OnReceived(JSONArray args) {
                    MyParkBean myParkBeen = new MyParkBean();
                    myParkBeen.setParkingrecordid(args.opt(0).toString());
                    myParkBeen.setParkNum(args.opt(1).toString());
                    myParkBeen.setIn(true);
                    carObservable.notifyChanged(myParkBeen);
                    CarNotifiContent("有车停靠，请前往录入！", args.opt(1).toString());
                    playSound(1, 1);
                }
            });
            hub.On("callBack_Login", new HubOnDataCallback() {
                @Override
                public void OnReceived(JSONArray args) {
                    Log.d("Hub", "Login=" + args.opt(0).toString());
                }
            });
            hub.On("callBack_CarOut", new HubOnDataCallback() {
                @Override
                public void OnReceived(JSONArray args) {
                    MyParkBean myParkBeen = new MyParkBean();
                    myParkBeen.setParkingrecordid(args.opt(0).toString());
                    myParkBeen.setParkNum(args.opt(1).toString());
                    myParkBeen.setOut(true);
                    carObservable.notifyChanged(myParkBeen);
                    CarNotifiContent("有车离开，请前往收费！", args.opt(1).toString());
                    playSound(2, 1);
                }
            });
            hub.On("callBack_PublishNews", new HubOnDataCallback() {
                @Override
                public void OnReceived(JSONArray args) {
                    MsgBean msgBean = new MsgBean();
                    msgBean.setId(args.opt(0).toString());
                    msgBean.setMsg_title(args.opt(2).toString());
                    if (StringEqualUtil.stringNull(args.opt(3).toString())) {
                        msgBean.setMsg_titleColor(args.opt(3).toString());
                    } else {
                        msgBean.setMsg_titleColor("#000000");
                    }
                    msgBean.setMsg_content(args.opt(4).toString());
                    msgBean.setMsg_time(args.opt(5).toString());
                    newsObservable.notifyChanged(msgBean);
                    MsgNotifiContent(args.opt(0).toString(), args.opt(2).toString(), args.opt(2).toString(), msgBean.getMsg_titleColor());
                    playSound(3, 1);
                }
            });
            hub.On("callBack_Pay", new HubOnDataCallback() {
                @Override
                public void OnReceived(JSONArray args) {
                    PayCallBackBean payCallBackBean = new PayCallBackBean();
                    payCallBackBean.setStutas(args.opt(1).toString());
                    payCallBackBean.setMoney(args.opt(2).toString());
                    payObservable.notifyChanged(payCallBackBean);
                    Intent intent = new Intent(SignalAService.this, PaySuccessActivity.class);
                    intent.putExtra("payStatus", payCallBackBean.getStutas());
                    intent.putExtra("payMoney", payCallBackBean.getMoney());
                    intent.setFlags(FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                }
            });
            conn.Start();
        }
    }

    private void login() {
        HubInvokeCallback callback = new HubInvokeCallback() {
            @Override
            public void OnResult(boolean succeeded, String response) {
                Log.d("Hub", succeeded + "");
            }

            @Override
            public void OnError(Exception ex) {
                Log.d("Hub", ex.getMessage());
            }
        };
        List<String> args = new ArrayList<String>();
        args.add(PreferenceUtils.getInstance(SignalAService.this).getString(AccountConfig.AccountId));
        args.add(PreferenceUtils.getInstance(SignalAService.this).getString(AccountConfig.Realname));
        args.add(PreferenceUtils.getInstance(SignalAService.this).getString(AccountConfig.Departmentid));
        args.add(PreferenceUtils.getInstance(SignalAService.this).getString(AccountConfig.Organizeid));
        args.add("2");
        hub.Invoke("login", args, callback);
    }

    /**
     * hub链接
     */
    private HubConnection conn = new HubConnection(UrlConfig.HUB_POST, this, new LongPollingTransport()) {
        @Override
        public void OnError(Exception exception) {
            Log.d("Hub", "OnError=" + exception.getMessage());
        }

        @Override
        public void OnMessage(String message) {

            Log.d("Hub", "message=" + message);
        }

        @Override
        public void OnStateChanged(StateBase oldState, StateBase newState) {
            Log.d("Hub", "OnStateChanged=" + oldState.getState() + " -> " + newState.getState());
            if (newState.getState().toString().equals("Connected")) {
                login();
            } else if (isHub && newState.getState().toString().equals("Disconnected")) {
                beginConnect();
            }
        }
    };

    private void CarNotifiContent(String title, String s) {
        //定义一个notification
        Intent notificationIntent = new Intent(this, MyParkActivity.class);
        notificationIntent.addFlags(FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        HeadsUpManager manage = HeadsUpManager.getInstant(getApplication());
        HeadsUp.Builder builder = new HeadsUp.Builder(this);
        builder.setContentTitle(title).setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS)
                //要显示通知栏通知,这个一定要设置
                .setSmallIcon(R.mipmap.logo)
                //2.3 一定要设置这个参数,负责会报错
                .setContentIntent(pendingIntent)
                .setFullScreenIntent(pendingIntent, false)
                .setAutoCancel(true)
                .setContentText(s);
        HeadsUp headsUp = builder.buildHeadUp();
        headsUp.setSticky(true);
        manage.notify(1, headsUp);
    }

    private void MsgNotifiContent(final String id, String context, final String title, final String titleColer) {

        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.addFlags(FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        final HeadsUpManager manage1 = HeadsUpManager.getInstant(getApplication());
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.custom_notification, null);
        TextView notiTitle = (TextView) view.findViewById(R.id.notifi_title);
        TextView notiContext = (TextView) view.findViewById(R.id.notifi_context);
        notiTitle.setText("通知公告");
        notiContext.setText(context + "");
        view.findViewById(R.id.openSource).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignalAService.this, ContentActivity.class);
                intent.setFlags(FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("Id", id);
                intent.putExtra("Title", title);
                intent.putExtra("TitleColor", titleColer);
                startActivity(intent);
                manage1.cancel();
            }
        });

        HeadsUp headsUp1 = new HeadsUp.Builder(this)
                .setContentTitle(title).setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS)
                .setContentTitle("通知公告")
                //要显示通知栏通知,这个一定要设置
                .setSmallIcon(R.mipmap.logo)
                //2.3 一定要设置这个参数,负责会报错
                .setContentIntent(pendingIntent)
                .setContentText("您有新信息")
                .setAutoCancel(true)
                .buildHeadUp();
        headsUp1.setCustomView(view);
        manage1.notify(code++, headsUp1);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("Hub", "service=" + "onDestroy()");
        sp.release();
        if (conn != null) {
            isHub = false;
            conn.Stop();
        }
    }

    /**
     * 添加车位状态观察者
     *
     * @param observer
     */
    public void addCarObservable(Observer observer) {
        carObservable.addObserver(observer);
    }

    /**
     * 添加新闻观察者
     *
     * @param observer
     */
    public void addNewsObservable(Observer observer) {
        newsObservable.addObserver(observer);
    }

    /**
     * 添加支付观察者
     *
     * @param observer
     */
    public void addPayObservable(Observer observer) {
        payObservable.addObserver(observer);
    }

    /**
     * 车位状态改变
     */
    private class CarObservable extends Observable {
        public void notifyChanged(MyParkBean myParkBean) {
            this.setChanged();
            this.notifyObservers(myParkBean);
        }
    }

    /**
     * 新闻刷新改变
     */
    private class NewsObservable extends Observable {
        public void notifyChanged(MsgBean msgBean) {
            this.setChanged();
            this.notifyObservers(msgBean);
        }
    }

    /**
     * 支付回调
     */
    private class PayObservable extends Observable {
        public void notifyChanged(PayCallBackBean payCallBackBean) {
            this.setChanged();
            this.notifyObservers(payCallBackBean);
        }
    }
}
