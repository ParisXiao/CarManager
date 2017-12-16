package com.pda.carmanager.view.activity;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

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
import com.pda.carmanager.util.AMUtil;
import com.pda.carmanager.util.DialogUtil;
import com.pda.carmanager.util.UserInfoClearUtil;
import com.pda.carmanager.view.inter.IParkViewInter;
import com.pda.carmanager.view.widght.CustomerCarDialog;
import com.pda.carmanager.view.widght.IdentifyingCodeView;
import com.suke.widget.SwitchButton;

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
        AMUtil.actionStartForResult(MyParkActivity.this, DialogCarWriteActivity.class);
    }

    @Override
    public void payCar() {
        AMUtil.actionStartForResult(MyParkActivity.this, PayMessageActivity.class);
    }

    @Override
    public void AutoPayCar(String carNum) {
        DialogUtil.showBoXunVIP(MyParkActivity.this, carNum);
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
}
