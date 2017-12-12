package com.pda.carmanager.view.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.androidkun.PullToRefreshRecyclerView;
import com.androidkun.callback.PullToRefreshListener;
import com.pda.carmanager.R;
import com.pda.carmanager.adapter.ChargeAdapter;
import com.pda.carmanager.base.BaseActivity;
import com.pda.carmanager.bean.ChargeBean;
import com.pda.carmanager.bean.MsgBean;
import com.pda.carmanager.pullrefresh.SpacesItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/12/8 0008.
 */

public class ChargeRecordActivity extends BaseActivity implements View.OnClickListener,PullToRefreshListener {

    private PullToRefreshRecyclerView pullRefresh_Charge;
    private ChargeAdapter chargeAdapter;
    private List<ChargeBean> chargeBeanList = null;
    private TextView toolbar_mid;
    private ImageButton toolbar_left_btn;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charger);
        initView();
        initData();
    }


    private void initView() {

        pullRefresh_Charge = (PullToRefreshRecyclerView) findViewById(R.id.pullRefresh_Charge);
        View emptyView = View.inflate(this, R.layout.layout_empty_view, null);
        emptyView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        pullRefresh_Charge.setEmptyView(emptyView);
        pullRefresh_Charge.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        pullRefresh_Charge.addItemDecoration(new SpacesItemDecoration(getResources().getDimensionPixelSize(R.dimen.padding_middle)));
        pullRefresh_Charge.setHasFixedSize(true);
//设置是否开启上拉加载
        pullRefresh_Charge.setLoadingMoreEnabled(true);
        //设置是否开启下拉刷新
        pullRefresh_Charge.setPullRefreshEnabled(true);
        //设置是否显示上次刷新的时间
        pullRefresh_Charge.displayLastRefreshTime(true);
        //设置刷新回调
        pullRefresh_Charge.setPullToRefreshListener(this);
        //主动触发下拉刷新操作
        //pullRefresh_msg.onRefresh();
        toolbar_mid = (TextView) findViewById(R.id.toolbar_mid);
        toolbar_mid.setOnClickListener(this);
        toolbar_left_btn = (ImageButton) findViewById(R.id.toolbar_left_btn);
        toolbar_left_btn.setOnClickListener(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setOnClickListener(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar_left_btn.setVisibility(View.VISIBLE);
    }

    private void initData() {
        toolbar_mid.setText(R.string.text_sfjl);
        chargeBeanList = new ArrayList<>();
        chargeBeanList.add(new ChargeBean("贵A78487", "2017-12-8 12:22:33至2017-12-9 12:22:33", "68.00元"));
        chargeBeanList.add(new ChargeBean("贵A78487", "2017-12-8 12:22:33至2017-12-9 12:22:33", "68.00元"));
        chargeBeanList.add(new ChargeBean("贵A78487", "2017-12-8 12:22:33至2017-12-9 12:22:33", "68.00元"));
        chargeBeanList.add(new ChargeBean("贵A78487", "2017-12-8 12:22:33至2017-12-9 12:22:33", "68.00元"));
        chargeBeanList.add(new ChargeBean("贵A78487", "2017-12-8 12:22:33至2017-12-9 12:22:33", "68.00元"));
        chargeBeanList.add(new ChargeBean("贵A78487", "2017-12-8 12:22:33至2017-12-9 12:22:33", "68.00元"));
        chargeBeanList.add(new ChargeBean("贵A78487", "2017-12-8 12:22:33至2017-12-9 12:22:33", "68.00元"));
        chargeBeanList.add(new ChargeBean("贵A78487", "2017-12-8 12:22:33至2017-12-9 12:22:33", "68.00元"));
        chargeAdapter=new ChargeAdapter(this, chargeBeanList);
        pullRefresh_Charge.setAdapter(chargeAdapter);

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
    public void onRefresh() {
        pullRefresh_Charge.postDelayed(new Runnable() {
            @Override
            public void run() {
                pullRefresh_Charge.setRefreshComplete();
                chargeAdapter.notifyDataSetChanged();
            }
        }, 2000);
    }

    @Override
    public void onLoadMore() {
        pullRefresh_Charge.postDelayed(new Runnable() {
            @Override
            public void run() {
                pullRefresh_Charge.setLoadMoreComplete(); //加载数据完成
                //模拟加载数据的情况
                for (int i = 0; i < 10; i++) {
                    chargeBeanList.add(new ChargeBean("贵A78487", "2017-12-8 12:22:33至2017-12-9 12:22:33", "68.00元"));
                }
                chargeAdapter.notifyDataSetChanged();
            }
        }, 2000);
    }
}
