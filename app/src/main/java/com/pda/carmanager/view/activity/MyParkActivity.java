package com.pda.carmanager.view.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.IBinder;
import android.posapi.PosApi;
import android.posapi.PrintQueue;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pda.carmanager.R;
import com.pda.carmanager.adapter.MyParkAdapter;
import com.pda.carmanager.base.BaseActivity;
import com.pda.carmanager.base.BaseApplication;
import com.pda.carmanager.bean.MyParkBean;
import com.pda.carmanager.bean.PrintBean;
import com.pda.carmanager.config.AccountConfig;
import com.pda.carmanager.presenter.ParkPresenter;
import com.pda.carmanager.service.ScanService;
import com.pda.carmanager.service.SignalAService;
import com.pda.carmanager.util.AMUtil;
import com.pda.carmanager.util.BitmapTools;
import com.pda.carmanager.util.DialogUtil;
import com.pda.carmanager.util.PreferenceUtils;
import com.pda.carmanager.util.StringEqualUtil;
import com.pda.carmanager.util.UserInfoClearUtil;
import com.pda.carmanager.view.inter.IParkViewInter;
import com.xys.libzxing.zxing.encoding.EncodingUtils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Administrator on 2017/12/9 0009.
 */

public class MyParkActivity extends BaseActivity implements Observer,View.OnClickListener, IParkViewInter {
    private TextView toolbar_mid;
    private ImageButton toolbar_left_btn;
    private Toolbar toolbar;
    private GridView pullRefresh_myPark;
    private MyParkAdapter myParkAdapter;
    private List<MyParkBean> parkBeanList = null;
    private ParkPresenter parkPresenter;
    private static final int RequsetPark = 0x12;
    public int level_battry = 50;
    private View emptyView;

    /**
     * 打印小票
     *
     * @param savedInstanceState
     */
    private PrintQueue mPrintQueue = null;
    private boolean isCanPrint = true;
    private Bitmap mBitmap = null;
    PrintBean printBean;
    private String CarNum;

    private SignalAService mService;
    private CarServiceConn conn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_mypark);
        initView();
        initData();
        initPrint();
    }


    private void initView() {
        toolbar_mid = (TextView) findViewById(R.id.toolbar_mid);
        toolbar_left_btn = (ImageButton) findViewById(R.id.toolbar_left_btn);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbar_left_btn.setOnClickListener(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar_left_btn.setVisibility(View.VISIBLE);

        pullRefresh_myPark = (GridView) findViewById(R.id.pullRefresh_myPark);
        emptyView = View.inflate(this, R.layout.layout_empty_view, null);
        emptyView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        ImageView empty_img = (ImageView) emptyView.findViewById(R.id.empty_img);
        TextView empty_text = (TextView) emptyView.findViewById(R.id.empty_text);
        empty_img.setImageDrawable(getResources().getDrawable(R.drawable.chewei_null));
        empty_text.setText("您还没有可管理车位！");
    }

    private void initData() {
        toolbar_mid.setText(PreferenceUtils.getInstance(this).getString(AccountConfig.Departmentname));
        DialogUtil.showMessage(this, getResources().getString(R.string.text_loading));
        parkPresenter = new ParkPresenter(this, this);
        parkBeanList = new ArrayList<>();
        myParkAdapter = new MyParkAdapter(this, parkBeanList);
        pullRefresh_myPark.setAdapter(myParkAdapter);
        conn = new CarServiceConn();
        bindService(new Intent(this,SignalAService.class),conn,BIND_AUTO_CREATE);
        pullRefresh_myPark.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (parkBeanList.get(position).getParkType()){
                    case "2":
                        Intent intent = new Intent(MyParkActivity.this, DialogCarWriteActivity.class);
                        intent.putExtra("ParkId", parkBeanList.get(position).getParkingrecordid());
                        startActivityForResult(intent, RequsetPark);
                        break;
                    case "3":
                        Intent intent1 = new Intent(MyParkActivity.this, PayMessageActivity.class);
                        intent1.putExtra("ID", id);
                        startActivity(intent1);
                        break;
                    case "4":
                        DialogUtil.showBoXunVIP(MyParkActivity.this, getResources().getString(R.string.text_dialog_vip1), 1);
                        break;
                }
            }
        });
        pullRefresh_myPark.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (parkBeanList.get(position).getParkType().equals("3")) {
                    if (BaseApplication.isPos) {
                        DialogUtil.showMessage(MyParkActivity.this, getResources().getString(R.string.text_loading));
                        parkPresenter.getPrintInfo(parkBeanList.get(position).getParkingrecordid());
                    }else {
                        DialogUtil.showBoXunVIP(MyParkActivity.this, "该终端无法进行打印", 1);
                    }
                }

                return false;
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        parkPresenter.postParkList ("", "", parkBeanList);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(conn);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_left_btn:
                finish();
                break;
        }
    }

    @Override
    public void update(Observable o, Object arg) {

        MyParkBean myParkBean=(MyParkBean)arg;
        Log.d("HubOb",myParkBean.getParkingrecordid());
        for (int i = 0; i < parkBeanList.size(); i++) {
            if (myParkBean.getParkNum().equals(parkBeanList.get(i).getParkNum())){
                parkBeanList.remove(i);
                myParkAdapter.notifyDataSetChanged();
            }
        }
        if (myParkBean.isIn()){
            myParkBean.setParkType("2");
            parkBeanList.add(0,myParkBean);
            myParkAdapter.notifyDataSetChanged();
        }
        if (myParkBean.isOut()){
            myParkBean.setParkType("1");
            if (parkBeanList.size()>0){
                parkBeanList.add(parkBeanList.size()-1,myParkBean);

            }else {
                parkBeanList.add(0,myParkBean);

            }
            myParkAdapter.notifyDataSetChanged();
        }


    }

    public class CarServiceConn implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mService = ((SignalAService.LocalBinder) iBinder).getService();
            //将当前Activity添加为观察者
            mService.addCarObservable(MyParkActivity.this);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mService = null;
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_CANCELED)
            return;

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case RequsetPark:
                    printBean = (PrintBean) data.getSerializableExtra("Print");
                    if (StringEqualUtil.stringNull(printBean.getCarNum())) {
                        if (BaseApplication.getInstance().isPosApi()) {
                            Bitmap logoBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.logo);
                            Bitmap bitmap = EncodingUtils.createQRCode(printBean.getUrl(), 400, 400, logoBitmap);
                            showChooseMessage(this,printBean, bitmap,printBean.getCarNum(), "是否打印小票");
                        }else {
                            DialogUtil.showBoXunVIP(MyParkActivity.this, "该终端无法进行打印", 1);
                        }
                    } else {
                        DialogUtil.showBoXunVIP(MyParkActivity.this, printBean.getCarNum(), 0);
                    }

                    break;
            }
        }

    }



    @Override
    public void getPrintSuccess(PrintBean printBeanlong) {
            Bitmap logoBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.logo);
            Bitmap bitmap = EncodingUtils.createQRCode(printBeanlong.getUrl(), 400, 400, logoBitmap);
            showChooseMessage(this,printBeanlong,bitmap, printBeanlong.getCarNum(), "是否打印小票");
    }

    @Override
    public void parkSuccess(String pages) {
        myParkAdapter.notifyDataSetChanged();
    }

    @Override
    public void parkFail(String msg) {
        if (msg.equals(getResources().getString(R.string.httpOut))) {
            UserInfoClearUtil.ClearUserInfo(MyParkActivity.this);
            AMUtil.actionStart(MyParkActivity.this, LoginActivity.class);
            finish();
        } else {
            finish();
        }
    }

    private void initPrint() {
        getButerryNum();
        mPrintQueue = new PrintQueue(this, ScanService.mApi);
        mPrintQueue.init();
        mPrintQueue.setOnPrintListener(new PrintQueue.OnPrintListener() {
            @Override
            public void onFinish() {
                // TODO Auto-generated method stub
                Toast.makeText(getApplicationContext(),
                        getString(R.string.print_complete), Toast.LENGTH_SHORT)
                        .show();

                isCanPrint = true;

            }

            @Override
            public void onFailed(int state) {
                // TODO Auto-generated method stub
                isCanPrint = true;
                switch (state) {
                    case PosApi.ERR_POS_PRINT_NO_PAPER:
                        // 打印缺纸
                        showTip(getString(R.string.print_no_paper));
                        break;
                    case PosApi.ERR_POS_PRINT_FAILED:
                        // 打印失败
                        showTip(getString(R.string.print_failed));
                        break;
                    case PosApi.ERR_POS_PRINT_VOLTAGE_LOW:
                        // 电压过低
                        showTip(getString(R.string.print_voltate_low));
                        break;
                    case PosApi.ERR_POS_PRINT_VOLTAGE_HIGH:
                        // 电压过高
                        showTip(getString(R.string.print_voltate_high));
                        break;
                }
            }

            @Override
            public void onGetState(int arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onPrinterSetting(int state) {
                // TODO Auto-generated method stub
                isCanPrint = true;
                switch (state) {
                    case 0:
                        Toast.makeText(MyParkActivity.this, "持续有纸", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        Toast.makeText(MyParkActivity.this, getString(R.string.no_paper), Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        Toast.makeText(MyParkActivity.this, getString(R.string.label), Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

    private void showTip(String msg) {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.tips))
                .setMessage(msg)
                .setNegativeButton(getString(R.string.close),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // TODO Auto-generated method stub
                                dialog.dismiss();
                            }
                        }).show();
    }

    private void printMix(PrintBean printBean1,Bitmap bitmap) {

        try {
            int concentration = 35;
            StringBuilder sb = new StringBuilder();
            byte[] text = null;
            sb.append("\n");
            sb.append("\n");
            sb.append("  泊讯停车|临街");
            sb.append("   车位缴费小票");
            sb.append("\n");
            sb.append("\n");

            text = sb.toString().getBytes("GBK");
            addPrintTextWithSize(2, concentration, text);
            sb = new StringBuilder();
            sb.append("            本次停车信息");
            sb.append("\n");
            sb.append("停车街道：");
            sb.append("   " + PreferenceUtils.getInstance(MyParkActivity.this).getString(AccountConfig.Departmentname));
            sb.append("\n");
            sb.append("车位编号：");
            sb.append("   " + printBean1.getCarNo());
            sb.append("\n");
            sb.append("车牌号  ：");
            sb.append("   " + printBean1.getCarNum());
            sb.append("\n");
            sb.append("停车时刻： ");
            sb.append(printBean1.getStartTime());
            sb.append("\n");
            if (printBean1.getIsQFModels() != null) {
                if (printBean1.getIsQFModels().size() > 0) {
                    for (int i = 0; i < printBean1.getIsQFModels().size(); i++) {
                        sb.append("==============================");
                        sb.append("\n");
                        sb.append("             欠费信息");
                        sb.append("\n");
                        sb.append("停车街道：");
                        sb.append("   " + printBean1.getIsQFModels().get(i).getJD());
                        sb.append("\n");
                        sb.append("车位编号：");
                        sb.append("   " + printBean1.getIsQFModels().get(i).getCarNO());
                        sb.append("\n");
                        sb.append("停车时刻： ");
                        sb.append(printBean1.getIsQFModels().get(i).getStartTime());

                        sb.append("\n");
                        sb.append("         至" + printBean1.getIsQFModels().get(i).getStopTime());
                        sb.append("\n");
                        sb.append("欠费金额：");
                        sb.append("         " + printBean1.getIsQFModels().get(i).getMoney() + "元");
                        sb.append("\n");

                    }
                }
            }

            sb.append("==============================");
            sb.append("\n");
            sb.append("\n");
            sb.append("     您离开时可用支付宝或微信");
            sb.append("\n");
            sb.append("     扫描下方二维码自主缴费");
            sb.append("\n");
            sb.append("\n");
            text = sb.toString().getBytes("GBK");
            addPrintTextWithSize(1, concentration, text);


            int mWidth = 300;
            int mHeight = 60;
//            mBitmap = BarcodeCreater.creatBarcode(getApplicationContext(),
//                    "1234567890", mWidth, mHeight, true, 1);
//            byte[] printData = BitmapTools.bitmap2PrinterBytes(mBitmap);

//
//            mPrintQueue.addBmp(concentration, 30, mBitmap.getWidth(),
//                    mBitmap.getHeight(), printData);


            mWidth = 400;
            mHeight = 400;

//            mBitmap = BarcodeCreater.encode2dAsBitmap(printBean.getUrl(), mWidth,
//                    mHeight, 2);
//            mBitmap = EncodingUtils.createQRCode(printBean1.getUrl(), 400, 400, null);

            byte[] printData = BitmapTools.bitmap2PrinterBytes(bitmap);
            mPrintQueue.addBmp(concentration, 5, bitmap.getWidth(),
                    bitmap.getHeight(), printData);

            sb = new StringBuilder();
            sb.append("\n");
            sb.append("\n");
            sb.append("\n");
            sb.append("\n");
            sb.append("\n");
            sb.append("\n");
            text = sb.toString().getBytes("GBK");

            addPrintTextWithSize(1, concentration, text);


            mPrintQueue.printStart();

        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /*
        * 打印文字 size 1 --倍大小 2--2倍大小
        */
    private void addPrintTextWithSize(int size, int concentration, byte[] data) {
        if (data == null)
            return;
        // 2倍字体大小
        byte[] _2x = new byte[]{0x1b, 0x57, 0x02};
        // 1倍字体大小
        byte[] _1x = new byte[]{0x1b, 0x57, 0x01};
        byte[] mData = null;
        if (size == 1) {
            mData = new byte[3 + data.length];
            // 1倍字体大小 默认
            System.arraycopy(_1x, 0, mData, 0, _1x.length);
            System.arraycopy(data, 0, mData, _1x.length, data.length);

            mPrintQueue.addText(concentration, mData);

        } else if (size == 2) {
            mData = new byte[3 + data.length];
            // 1倍字体大小 默认
            System.arraycopy(_2x, 0, mData, 0, _2x.length);
            System.arraycopy(data, 0, mData, _2x.length, data.length);

            mPrintQueue.addText(concentration, mData);

        }

    }

    public void getButerryNum() {
        registerReceiver(new BatteryReceiver(), new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
    }


    private void showChooseMessage(final Context context, final PrintBean printBeanS, final Bitmap bitmap, String text, final String textContent) {
        final AlertDialog progressDialog = new AlertDialog.Builder(context).create();
        if (!(progressDialog != null && progressDialog.isShowing())) {
            try {
                progressDialog.show();
                progressDialog.setCanceledOnTouchOutside(false);
//            WindowManager.LayoutParams params =
//                    dialog.getWindow().getAttributes();
//            params.width = 250;
//            params.height = 250;
//            dialog.getWindow().setAttributes(params);
                Window window = progressDialog.getWindow();
                WindowManager.LayoutParams lp = window.getAttributes();
                window.setGravity(Gravity.CENTER);
                lp.alpha = 1f;
                window.setAttributes(lp);
                window.setContentView(R.layout.layout_choose_dialog);
                TextView text1 = (TextView) window.findViewById(R.id.note_text);
                TextView text2 = (TextView) window.findViewById(R.id.note_text_content);
                Button button1 = (Button) window.findViewById(R.id.note_exit);
                Button button2 = (Button) window.findViewById(R.id.note_sure);
                text1.setText(text);
                text2.setText(textContent);
                button1.setText("不打印");
                button2.setText("打印");
                button1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        progressDialog.dismiss();
                    }
                });
                button2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        // TODO Auto-generated method stub
                        if (!isCanPrint) return;

                        if (level_battry <= 12) {
                            Toast.makeText(MyParkActivity.this, "低电量不能打印", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        byte[] test = new byte[]{0x33, 0x34, 0x31, 0x33, 0x34, 0x31,
                                0x33, 0x34, 0x31, 0x33, 0x34, 0x31, 0x00};
                        String string = "微信支付 微信支付\n";

                        try {
                            test = string.getBytes("GBK");
                        } catch (UnsupportedEncodingException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                        printMix(printBeanS,bitmap);
                        progressDialog.dismiss();

                    }
                });
            } catch (Exception e) {

            }
        }
    }

    /**
     * 接受电量改变广播
     */
    class BatteryReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals(Intent.ACTION_BATTERY_CHANGED)) {

                level_battry = intent.getIntExtra("level", 0);
            }
        }
    }
}
