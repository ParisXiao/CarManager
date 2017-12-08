package com.pda.carmanager.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pda.carmanager.R;
import com.pda.carmanager.adapter.DakaListAdapter;
import com.pda.carmanager.base.BaseActivity;
import com.pda.carmanager.bean.DakaBean;

import java.util.ArrayList;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sbdk);
        initView();
        initData();
    }

    private void initData() {
        DakaType=getIntent().getStringExtra("DakaType");
        if (DakaType.equals("XB")){
            toolbar_mid.setText(R.string.text_xbdk);
            daka_text.setText(R.string.text_xbdk);
        }else {
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
        daka_text.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.linear_sbdk_btn:
                break;

            case R.id.toolbar_left_btn:
                finish();
                break;
        }
    }
}
