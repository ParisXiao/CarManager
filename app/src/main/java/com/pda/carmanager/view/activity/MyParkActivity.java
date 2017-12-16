package com.pda.carmanager.view.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.posapi.PosApi;
import android.posapi.PrintQueue;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.androidkun.PullToRefreshRecyclerView;
import com.androidkun.callback.PullToRefreshListener;
import com.pda.carmanager.R;
import com.pda.carmanager.adapter.MyParkAdapter;
import com.pda.carmanager.base.BaseActivity;
import com.pda.carmanager.bean.ChargeBean;
import com.pda.carmanager.bean.MyParkBean;
import com.pda.carmanager.inter.ParkItemOnInter;
import com.pda.carmanager.presenter.ParkPresenter;
import com.pda.carmanager.pullrefresh.GridSpacingItemDecoration;
import com.pda.carmanager.service.ScanService;
import com.pda.carmanager.util.AMUtil;
import com.pda.carmanager.util.BarcodeCreater;
import com.pda.carmanager.util.BitmapTools;
import com.pda.carmanager.util.DialogUtil;
import com.pda.carmanager.util.OKHttpUtil;
import com.pda.carmanager.util.PhotoUtils;
import com.pda.carmanager.util.UserInfoClearUtil;
import com.pda.carmanager.view.inter.IParkViewInter;
import com.pda.carmanager.view.test.PDAPrintActivity;
import com.pda.carmanager.view.widght.CustomerCarDialog;
import com.pda.carmanager.view.widght.IdentifyingCodeView;
import com.suke.widget.SwitchButton;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/12/9 0009.
 */

public class MyParkActivity extends BaseActivity implements View.OnClickListener, ParkItemOnInter, PullToRefreshListener, IParkViewInter {
    private TextView toolbar_mid;
    private ImageButton toolbar_left_btn;
    private Toolbar toolbar;
    private PullToRefreshRecyclerView pullRefresh_myPark;
    private MyParkAdapter myParkAdapter;
    private List<MyParkBean> parkBeanList = null;
    private List<MyParkBean> parkBeanListshow = new ArrayList<>();
    private PopupWindow popupWindow1;
    private ParkPresenter parkPresenter;
    private int page = 0;
    private boolean reFreshNext;
    private boolean hasNext;
    private boolean isRefreah;
    private int list = 10;
    private static final int RequsetPark=0x12;
    public int level_battry=50;

    /**
     * 打印小票
     *
     * @param savedInstanceState
     */
    private PrintQueue mPrintQueue = null;
    private boolean isCanPrint = true;
    private Bitmap mBitmap = null;

    private String CarNum;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    if (!reFreshNext) {
                        parkBeanListshow.clear();
                    }
                    if (parkBeanList != null && parkBeanList.size() > 0) {
                        Log.e("list", list + "");
                        int s = parkBeanList.size() < list ? parkBeanList.size() : list;
                        for (int i = 0; i < s; i++) {
                            parkBeanListshow.add(parkBeanList.get(i));
                        }
                        myParkAdapter.notifyDataSetChanged();
                    } else {
                        parkBeanListshow.clear();
                        myParkAdapter.notifyDataSetChanged();
                    }
                    isRefreah = false;
                    reFreshNext = false;
                    pullRefresh_myPark.setLoadMoreComplete();
                    break;
                case 1:
                    pullRefresh_myPark.setLoadMoreComplete();
                    break;
                case 2:
//                    getList();
                    break;
            }
        }
    };

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
        pullRefresh_myPark = (PullToRefreshRecyclerView) findViewById(R.id.pullRefresh_myPark);
        View emptyView = View.inflate(this, R.layout.layout_empty_view, null);
        emptyView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        pullRefresh_myPark.setEmptyView(emptyView);
        pullRefresh_myPark.setLayoutManager(new GridLayoutManager(this, 2));
        pullRefresh_myPark.addItemDecoration(new GridSpacingItemDecoration(getResources().getDimensionPixelSize(R.dimen.padding_middle), 2, true));
        pullRefresh_myPark.setHasFixedSize(true);
        toolbar_left_btn.setOnClickListener(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar_left_btn.setVisibility(View.VISIBLE);
//       设置是否开启上拉加载
        pullRefresh_myPark.setLoadingMoreEnabled(true);
        //设置是否开启下拉刷新
        pullRefresh_myPark.setPullRefreshEnabled(true);
        //设置是否显示上次刷新的时间
        pullRefresh_myPark.displayLastRefreshTime(true);
        //设置刷新回调
        pullRefresh_myPark.setPullToRefreshListener(this);
        //主动触发下拉刷新操作
        //pullRefresh_msg.onRefresh();

    }

    private void initData() {
        toolbar_mid.setText(R.string.myPark);
        DialogUtil.showMessage(this, getResources().getString(R.string.text_loading));
        parkPresenter = new ParkPresenter(this, this);
        parkBeanList = new ArrayList<>();
        myParkAdapter = new MyParkAdapter(this, parkBeanListshow, this);
        pullRefresh_myPark.setAdapter(myParkAdapter);
        parkPresenter.postParkList("0", "");

    }

    @Override
    protected void onResume() {
        super.onResume();

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
    public void writeCarNum() {
        Intent intent=new Intent(MyParkActivity.this,DialogCarWriteActivity.class);
        startActivityForResult(intent, RequsetPark);
    }

    @Override
    public void payCar() {
        AMUtil.actionStart(MyParkActivity.this, PayMessageActivity.class);
    }

    @Override
    public void AutoPayCar(String carNum) {
        DialogUtil.showBoXunVIP(MyParkActivity.this, carNum);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_CANCELED)
            return;

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case RequsetPark:
                    Bundle bundle = data.getExtras();
                    CarNum=bundle.getString("carNum");
                    showChooseMessage(this,CarNum,"是否打印小票");
                    break;
            }
        }

    }
    @Override
    public void onRefresh() {
        if (!isRefreah) {
            isRefreah = true;
            if (!reFreshNext) {
                page = 1;
            }
            parkPresenter.postParkList(page+"","");
        }
    }

    @Override
    public void onLoadMore() {
        if (hasNext) {
            page += 1;
            reFreshNext = true;
            if (!isRefreah) {
                isRefreah = true;
                if (!reFreshNext) {
                    page = 1;
                }
                parkPresenter.postParkList(page+"","");
            }
        } else {
            handler.sendEmptyMessageDelayed(1, 1000);
        }
    }

    @Override
    public void parkSuccess(List<MyParkBean> parkBeans) {
        parkBeans=parkBeanList;
        handler.sendEmptyMessage(0);
    }

    @Override
    public void parkFail(String msg) {

        if (msg.equals(getResources().getString(R.string.httpOut))) {
            UserInfoClearUtil.ClearUserInfo(MyParkActivity.this);
            AMUtil.actionStart(MyParkActivity.this, LoginActivity.class);
            finish();
        }else {
            handler.sendEmptyMessage(0);
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

    private void printMix() {

        try {

            int concentration = 25;

            StringBuilder sb = new StringBuilder();
            sb.append("        收 银 凭 据                 ");
            sb.append("\n");
            sb.append("时间   : ");
            sb.append("2016-11-15     16:00");
            sb.append("\n");
            sb.append("操作员:admin");
            sb.append("\n");
            sb.append("收据单号：1234567890");
            sb.append("\n");
            sb.append("编号  数量  单价  折扣  小计");
            sb.append("\n");
            sb.append("-----------------------------");
            sb.append("\n");
            sb.append("AM126   1  1200  0   1200");
            sb.append("\n");
            sb.append("AM127   1  1300  0   1300");
            sb.append("\n");
            sb.append("AM128   1  1400  0   1400");
            sb.append("\n");
            sb.append("-----------------------------");
            sb.append("\n");
            sb.append("共销售数量: 3 ");
            sb.append("\n");
            sb.append("售价合计(RMB): 3900");
            sb.append("\n");
            sb.append("实收金额(RMB): 3900");
            sb.append("\n");
            sb.append("找零金额(RMB): 0");
            sb.append("\n");
            sb.append("-----------------------------");
            sb.append("\n");
            sb.append("支付方式: 微信支付 ");
            sb.append("\n");
            sb.append("欢迎下次光临    请保留好小票！");
            sb.append("\n");

            sb.append("-----------------------------");
            sb.append("\n");
            byte[] text = null;
            text = sb.toString().getBytes("GBK");

            addPrintTextWithSize(1, concentration, text);

            sb = new StringBuilder();
            sb.append("   谢谢惠顾");
            sb.append("\n");

            text = sb.toString().getBytes("GBK");
            addPrintTextWithSize(2, concentration, text);

            sb = new StringBuilder();
            sb.append("\n");
            text = sb.toString().getBytes("GBK");
            addPrintTextWithSize(1, concentration, text);

            int mWidth = 300;
            int mHeight = 60;
            mBitmap = BarcodeCreater.creatBarcode(getApplicationContext(),
                    "1234567890", mWidth, mHeight, true, 1);
            byte[] printData = BitmapTools.bitmap2PrinterBytes(mBitmap);


            mPrintQueue.addBmp(concentration, 30, mBitmap.getWidth(),
                    mBitmap.getHeight(), printData);

            sb = new StringBuilder();
            sb.append("\n");
            sb.append("     扫一扫下载APP更多优惠");
            sb.append("\n");
            sb.append("\n");
            text = sb.toString().getBytes("GBK");
            addPrintTextWithSize(1, concentration, text);

            mWidth = 150;
            mHeight = 150;

            mBitmap = BarcodeCreater.encode2dAsBitmap("1234567890", mWidth,
                    mHeight, 2);
            printData = BitmapTools.bitmap2PrinterBytes(mBitmap);
            mPrintQueue.addBmp(concentration, 100, mBitmap.getWidth(),
                    mBitmap.getHeight(), printData);

            sb = new StringBuilder();
            sb.append("1个月之内可凭票至服务台开具发票!");
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
        byte[] _2x = new byte[] { 0x1b, 0x57, 0x02 };
        // 1倍字体大小
        byte[] _1x = new byte[] { 0x1b, 0x57, 0x01 };
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
    public void getButerryNum(){
        registerReceiver(new BatteryReceiver(), new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
    }



    private void showChooseMessage(final Context context, String text, String textContent) {
        final AlertDialog progressDialog = new AlertDialog.Builder(context).create();
        if (!(progressDialog != null && progressDialog.isShowing())) {
            try {
                progressDialog.show();
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
                        if(!isCanPrint) return;

                        if(level_battry<=12){
                            Toast.makeText(MyParkActivity.this, "低电量不能打印", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        byte[] test = new byte[] { 0x33, 0x34, 0x31, 0x33, 0x34, 0x31,
                                0x33, 0x34, 0x31, 0x33, 0x34, 0x31, 0x00 };
                        String string = "微信支付 微信支付\n";

                        try {
                            test = string.getBytes("GBK");
                        } catch (UnsupportedEncodingException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                        printMix();
                        progressDialog.dismiss();

                    }
                });
            } catch (Exception e) {

            }
        }
    }

    /**接受电量改变广播*/
    class BatteryReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            if(intent.getAction().equals(Intent.ACTION_BATTERY_CHANGED)){

                level_battry = intent.getIntExtra("level", 0);
            }
        }
    }
}
