package com.pda.carmanager.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.mingle.headsUp.HeadsUp;
import com.mingle.headsUp.HeadsUpManager;
import com.pda.carmanager.R;
import com.pda.carmanager.bean.MyParkBean;
import com.pda.carmanager.config.AccountConfig;
import com.pda.carmanager.config.UrlConfig;
import com.pda.carmanager.inter.ISignalAInter;
import com.pda.carmanager.util.PreferenceUtils;
import com.pda.carmanager.view.activity.MainActivity;
import com.pda.carmanager.view.activity.MyParkActivity;
import com.zsoft.signala.hubs.HubConnection;
import com.zsoft.signala.hubs.HubInvokeCallback;
import com.zsoft.signala.hubs.HubOnDataCallback;
import com.zsoft.signala.hubs.IHubProxy;
import com.zsoft.signala.transport.StateBase;
import com.zsoft.signala.transport.longpolling.LongPollingTransport;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP;

/**
 * Created by Admin on 2017/12/21.
 */

public class SignalAService extends Service {
    private IHubProxy hub = null;
    private int code=1;
    // 通知栏消息 数量
    private int messageNotificationID = 0;
    private ISignalAInter iSignalAInter;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new SignalABinder(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        beginConnect();
    }
    /**
     * 开启推送服务 panderman 2013-10-25
     */
    private void beginConnect() {
        try {
            hub = conn.CreateHubProxy("ChatHub");
        } catch (OperationApplicationException e) {
            e.printStackTrace();
        }
//        hub.On("callBack_CarIn", new HubOnDataCallback() {
//            @Override
//            public void OnReceived(JSONArray args) {
//            }
//        });
//        hub.On("callBack_Login", new HubOnDataCallback() {
//            @Override
//            public void OnReceived(JSONArray args) {
//
//            }
//        });
        hub.On("callBack_CarOut", new HubOnDataCallback() {
            @Override
            public void OnReceived(JSONArray args) {
            }
        });
//        hub.On("callBack_PublishNews", new HubOnDataCallback() {
//            @Override
//            public void OnReceived(JSONArray args) {
//            }
//        });

        conn.Start();
    }
    private void login(){
        HubInvokeCallback callback = new HubInvokeCallback() {
            @Override
            public void OnResult(boolean succeeded, String response) {
                Log.d("Hub",succeeded+"");
            }

            @Override
            public void OnError(Exception ex) {
                Log.d("Hub",ex.getMessage());
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
            if (newState.getState().toString().equals("Connected")){
                login();
            }
        }
    };
    private void CarNotifiContent(String title ,String s) {
        //定义一个notification
        Intent notificationIntent = new Intent(this, MyParkActivity.class);
        notificationIntent.addFlags(FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 11, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        HeadsUpManager manage = HeadsUpManager.getInstant(getApplication());
        HeadsUp.Builder builder = new HeadsUp.Builder(this);
        builder.setContentTitle(title).setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS)
                //要显示通知栏通知,这个一定要设置
                .setSmallIcon(R.mipmap.logo)
                //2.3 一定要设置这个参数,负责会报错
                .setContentIntent(pendingIntent)
                .setFullScreenIntent(pendingIntent,false)
                .setAutoCancel(true)
                .setContentText(s);
        HeadsUp headsUp = builder.buildHeadUp();
        headsUp.setSticky(true);
        manage.notify(code++, headsUp);
    }
    private void MsgNotifiContent(final String title, String context){

        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.addFlags(FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 11, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        final HeadsUpManager manage1 = HeadsUpManager.getInstant(getApplication());
        LayoutInflater inflater = LayoutInflater.from(this);
        View view= inflater.inflate(R.layout.custom_notification, null);
       TextView notiTitle= (TextView) view.findViewById(R.id.noti_title);
       TextView notiContext= (TextView) view.findViewById(R.id.noti_context);
        notiTitle.setText(title);
        notiContext.setText(context);
        view.findViewById(R.id.openSource).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SignalAService.this,MyParkActivity.class);
                intent.setFlags(FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                manage1.cancel();
            }
        });

        HeadsUp headsUp1 = new HeadsUp.Builder(this)
                .setContentTitle("text")
                //要显示通知栏通知,这个一定要设置
                .setSmallIcon(R.mipmap.logo)
                //2.3 一定要设置这个参数,负责会报错
                .setContentIntent(pendingIntent)
                .setContentText("text")
                .setAutoCancel(true)
                .buildHeadUp();
        headsUp1.setCustomView(view);
        manage1.notify(code++, headsUp1);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(conn!=null)
        {
            conn.Stop();
        }
    }
    /**
     * 车位状态改变
     */
    private class MyObservable extends Observable {
        public void notifyChanged(List<MyParkBean> parkBeanList){
            for (MyParkBean myParkBean : parkBeanList) {
                this.setChanged();
                this.notifyObservers(myParkBean);
            }

        }
    }
}
