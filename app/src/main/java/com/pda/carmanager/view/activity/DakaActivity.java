package com.pda.carmanager.view.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidkun.PullToRefreshRecyclerView;
import com.androidkun.callback.PullToRefreshListener;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.pda.carmanager.R;
import com.pda.carmanager.adapter.DakaListAdapter;
import com.pda.carmanager.base.BaseActivity;
import com.pda.carmanager.bean.DakaBean;
import com.pda.carmanager.presenter.DakaPresenter;
import com.pda.carmanager.pullrefresh.GridSpacingItemDecoration;
import com.pda.carmanager.pullrefresh.SpacesItemDecoration;
import com.pda.carmanager.util.AMUtil;
import com.pda.carmanager.util.DialogUtil;
import com.pda.carmanager.util.UserInfoClearUtil;
import com.pda.carmanager.view.inter.IDakaViewInter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.pda.carmanager.R.id.pullRefresh_myPark;

/**
 * Created by Administrator on 2017/12/8 0008.
 */

public class DakaActivity extends BaseActivity implements View.OnClickListener, IDakaViewInter, PullToRefreshListener {
    private TextView text_sbdk_time;
    private LinearLayout linear_sbdk_btn;
    private List<DakaBean> dakaBeanList = null;
    private List<DakaBean> dakaBeanListShow = new ArrayList<>();
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
    private DakaPresenter dakaPresenter;
    private boolean flag = false;
    private PullToRefreshRecyclerView pullRefresh_Daka;
    private View emptyView;
    private int page = 1;
    private int Pages = 0;
    private boolean reFreshNext;
    private boolean hasNext;
    private boolean isRefreah;
    private int list = 10;
    private int load = 0;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    if (!reFreshNext) {
                        dakaBeanListShow.clear();
                    }
                    if (dakaBeanList != null && dakaBeanList.size() > 0) {
                        Log.e("list", list + "");
                        int s = dakaBeanList.size() < list ? dakaBeanList.size() : list;
                        for (int i = 0; i < s; i++) {
                            dakaBeanListShow.add(dakaBeanList.get(i));
                        }
                        dakaListAdapter.notifyDataSetChanged();
                    } else {
                        dakaBeanListShow.clear();
                        dakaListAdapter.notifyDataSetChanged();
                    }
                    isRefreah = false;
                    reFreshNext = false;
                    if (load == 1) {
                        pullRefresh_Daka.setRefreshComplete();
                    } else if (load == 2) {
                        pullRefresh_Daka.setLoadMoreComplete();
                    }

                    break;
                case 1:
                    if (load == 1) {
                        pullRefresh_Daka.setRefreshComplete();
                    } else if (load == 2) {
                        pullRefresh_Daka.setLoadMoreComplete();
                    }
                    break;
                case msgKey1:
                    long time = System.currentTimeMillis();
                    Date date = new Date(time);
                    SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
                    text_sbdk_time.setText(format.format(date));
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sbdk);
        initView();
        initData();
    }

    @Override
    public void dakaSuccess() {
        mLocationClient.stop();
    }

    @Override
    public void getdakaSuccess(String pages) {
        this.Pages = Integer.valueOf(pages);
        if (Pages <= page) {
            hasNext = false;
        } else {
            hasNext = true;
        }
        handler.sendEmptyMessage(0);
        pullRefresh_Daka.setEmptyView(emptyView);

    }

    @Override
    public void dakaFail(String msg) {
        if (msg.equals(getResources().getString(R.string.httpOut))) {
            UserInfoClearUtil.ClearUserInfo(DakaActivity.this);
            AMUtil.actionStart(DakaActivity.this, LoginActivity.class);
            finish();
        }
    }

    @Override
    public void onRefresh() {
        load = 1;
        if (!isRefreah) {
            isRefreah = true;
            if (!reFreshNext) {
                page = 1;
            }
            dakaPresenter.getDaka("",page+"",dakaBeanList);
        }
    }

    @Override
    public void onLoadMore() {
        load = 2;
        if (hasNext) {
            page += 1;
            reFreshNext = true;
            if (!isRefreah) {
                isRefreah = true;
                if (!reFreshNext) {
                    page = 1;
                }
                dakaPresenter.getDaka("",page+"",dakaBeanList);
            }
        } else {
            handler.sendEmptyMessageDelayed(1, 1000);
        }
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
                    handler.sendMessage(msg);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (true);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }


    private void initData() {
        DakaType = getIntent().getStringExtra("DakaType");
        dakaPresenter = new DakaPresenter(this, this);
        if (DakaType.equals("2")) {
            toolbar_mid.setText(R.string.text_xbdk);
            daka_text.setText(R.string.text_xbdk);
        } else if (DakaType.equals("1")) {
            toolbar_mid.setText(R.string.text_sbdk);
            daka_text.setText(R.string.text_sbdk);
        }
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);



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
        DialogUtil.showMessage(DakaActivity.this,getResources().getString(R.string.text_loading));
        dakaPresenter.getDaka("",page+"",dakaBeanList);
        dakaBeanList=new ArrayList<>();
        dakaListAdapter = new DakaListAdapter(this, dakaBeanList);
        pullRefresh_Daka.setAdapter(dakaListAdapter);
    }

    private void initView() {

        text_sbdk_time = (TextView) findViewById(R.id.text_sbdk_time);
        linear_sbdk_btn = (LinearLayout) findViewById(R.id.linear_sbdk_btn);
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
        pullRefresh_Daka = (PullToRefreshRecyclerView) findViewById(R.id.pullRefresh_Daka);
         emptyView = View.inflate(this, R.layout.layout_empty_view, null);
        emptyView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        ImageView empty_img = (ImageView) emptyView.findViewById(R.id.empty_img);
        TextView empty_text = (TextView) emptyView.findViewById(R.id.empty_text);
        empty_img.setImageDrawable(getResources().getDrawable(R.drawable.shoufeijilu_no));
        empty_text.setText("你还没有打卡记录！");
        pullRefresh_Daka.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        pullRefresh_Daka.addItemDecoration(new SpacesItemDecoration(getResources().getDimensionPixelSize(R.dimen.padding_middle)));
        pullRefresh_Daka.setHasFixedSize(true);

        //设置是否开启上拉加载
        pullRefresh_Daka.setLoadingMoreEnabled(true);
        //设置是否开启下拉刷新
        pullRefresh_Daka.setPullRefreshEnabled(true);
        //设置是否显示上次刷新的时间
        pullRefresh_Daka.displayLastRefreshTime(true);
        //设置刷新回调
        pullRefresh_Daka.setPullToRefreshListener(this);
        //主动触发下拉刷新操作
        //pullRefresh_msg.onRefresh();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.linear_sbdk_btn:
                mLocationClient.start();
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
            String address = city + district + street;
            address = bdLocation.getAddrStr();
            if (address!=null) {
                DialogUtil.showMessage(DakaActivity.this,getResources().getString(R.string.text_uping));
                dakaPresenter.postDaka(address, DakaType);
            }
        }
    }
}
