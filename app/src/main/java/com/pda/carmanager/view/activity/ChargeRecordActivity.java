package com.pda.carmanager.view.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.pda.carmanager.R;
import com.pda.carmanager.adapter.ChargeAdapter;
import com.pda.carmanager.base.BaseActivity;
import com.pda.carmanager.bean.ChargeBean;
import com.pda.carmanager.pullrefresh.MessageRelativeLayout;
import com.pda.carmanager.pullrefresh.PullRefreshRecyclerView;
import com.pda.carmanager.pullrefresh.SpacesItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/12/8 0008.
 */

public class ChargeRecordActivity extends BaseActivity implements View.OnClickListener {

    private PullRefreshRecyclerView pullRefresh_Charge;
    private MessageRelativeLayout refreshLayout_ChargeMsg;
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

        pullRefresh_Charge = (PullRefreshRecyclerView) findViewById(R.id.pullRefresh_Charge);
        refreshLayout_ChargeMsg = (MessageRelativeLayout) findViewById(R.id.refreshLayout_ChargeMsg);
        pullRefresh_Charge.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        pullRefresh_Charge.addItemDecoration(new SpacesItemDecoration(getResources().getDimensionPixelSize(R.dimen.padding_middle)));
        pullRefresh_Charge.setHasFixedSize(true);

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
        pullRefresh_Charge.setOnRefreshListener(new PullRefreshRecyclerView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //刷新数据

                //结束刷新
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pullRefresh_Charge.stopRefresh();
                        refreshLayout_ChargeMsg.setMessage("更新了18条数据");
                    }
                }, 2000);

            }

            @Override
            public void onLoadMore() {
                //加载更多

                //结束加载
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pullRefresh_Charge.stopLoadMore();
                    }
                }, 2000);
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_left_btn:
                finish();
                break;
        }
    }
}
