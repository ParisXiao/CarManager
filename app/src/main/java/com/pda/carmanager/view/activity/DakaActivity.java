package com.pda.carmanager.view.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.pda.carmanager.R;
import com.pda.carmanager.adapter.DakaListAdapter;
import com.pda.carmanager.base.BaseActivity;
import com.pda.carmanager.bean.DakaBean;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/12/8 0008.
 */

public class DakaActivity extends BaseActivity implements View.OnClickListener {
    private TextView text_sbdk_time;
    private LinearLayout linear_sbdk_btn;
    private RecyclerView rvTrace;
    private List<DakaBean> dakaBeanList = null;
    private DakaListAdapter dakaListAdapter;
    private TextView toolbar_mid;
    private ImageButton toolbar_left_btn;
    private Toolbar toolbar;
    private TextView daka_text;
    private String DakaType;
    private static final int msgKey1 = 0x11;
    public LocationClient mLocationClient = null;
    private LocationListener myListener = new LocationListener();
    private String city = "";     //获取城市
    private String district = "";    //获取区县
    private String street = "";   //获取街道信息
    private String address = "";   //获取详细信息

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sbdk);
        initView();
        initData();
    }

    public class TimeThread extends Thread {
        @Override
        public void run() {
            super.run();
            do {
                try {
                    Thread.sleep(1000);
                    Message msg = new Message();
                    msg.what = msgKey1;
                    mHandler.sendMessage(msg);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (true);
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case msgKey1:
                    long time = System.currentTimeMillis();
                    Date date = new Date(time);
                    SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
                    text_sbdk_time.setText(format.format(date));
                    break;
                default:
                    break;
            }
        }
    };

    private void initData() {
        DakaType = getIntent().getStringExtra("DakaType");
        if (DakaType.equals("XB")) {
            toolbar_mid.setText(R.string.text_xbdk);
            daka_text.setText(R.string.text_xbdk);
        } else {
            toolbar_mid.setText(R.string.text_sbdk);
            daka_text.setText(R.string.text_sbdk);
        }
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

// 模拟一些假的数据
        dakaBeanList = new ArrayList<>();
        dakaBeanList.add(new DakaBean("上班签到时间 2011-05-25 17:48:00", "贵阳市xx路xxx街道17-3"));
        dakaBeanList.add(new DakaBean("下班签到时间 2011-05-25 17:48:00", "贵阳市xx路xxx街道17-3"));
        dakaBeanList.add(new DakaBean("上班签到时间 2011-05-25 17:48:00", "贵阳市xx路xxx街道17-4"));
        dakaBeanList.add(new DakaBean("下班签到时间 2011-05-25 17:48:00", "贵阳市xx路xxx街道17-5"));
        dakaBeanList.add(new DakaBean("上班签到时间 2011-05-25 17:48:00", "贵阳市xx路xxx街道17-6"));
        dakaListAdapter = new DakaListAdapter(this, dakaBeanList);
        rvTrace.setLayoutManager(new LinearLayoutManager(this));
        rvTrace.setAdapter(dakaListAdapter);

        mLocationClient = new LocationClient(getApplicationContext());
        //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);
        //注册监听函数
        LocationClientOption option = new LocationClientOption();
        option.setIsNeedAddress(true);
//可选，是否需要地址信息，默认为不需要，即参数为false
//如果开发者需要获得当前点的地址信息，此处必须为true
        mLocationClient.setLocOption(option);
//mLocationClient为第二步初始化过的LocationClient对象

    }

    private void initView() {
        text_sbdk_time = (TextView) findViewById(R.id.text_sbdk_time);
        linear_sbdk_btn = (LinearLayout) findViewById(R.id.linear_sbdk_btn);
        rvTrace = (RecyclerView) findViewById(R.id.rvTrace);
        linear_sbdk_btn.setOnClickListener(this);
        toolbar_mid = (TextView) findViewById(R.id.toolbar_mid);
        toolbar_mid.setOnClickListener(this);
        toolbar_left_btn = (ImageButton) findViewById(R.id.toolbar_left_btn);
        toolbar_left_btn.setVisibility(View.VISIBLE);
        toolbar_left_btn.setOnClickListener(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setOnClickListener(this);
        toolbar_left_btn.setOnClickListener(this);
        daka_text = (TextView) findViewById(R.id.daka_text);
        new TimeThread().start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.linear_sbdk_btn:
                mLocationClient.start();

                Toast.makeText(DakaActivity.this,"打卡成功,地点："+city+district+street,Toast.LENGTH_LONG).show();
//                Toast.makeText(DakaActivity.this,"打卡成功,地点："+address,Toast.LENGTH_LONG).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mLocationClient.stop();
                    }
                },5000);
                break;

            case R.id.toolbar_left_btn:
                finish();
                break;
        }

    }

    class LocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            city = bdLocation.getCity();
            district = bdLocation.getDistrict();
            street = bdLocation.getStreet();

            address=bdLocation.getAddrStr();
        }
    }
}
