package com.pda.carmanager.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidkun.PullToRefreshRecyclerView;
import com.pda.carmanager.R;
import com.pda.carmanager.adapter.ErrorAdapter;
import com.pda.carmanager.base.BaseActivity;
import com.pda.carmanager.bean.ErrorBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/12/12 0012.
 */

public class ErrorNoteActivity extends BaseActivity implements View.OnClickListener {
    private TextView toolbar_mid;
    private ImageButton toolbar_left_btn;
    private ImageButton toolbar_right_btn;
    private Toolbar toolbar;
    private LinearLayout linear_sbdk_btn;
    private PullToRefreshRecyclerView pullRefresh_myError;
    private List<ErrorBean> errorBeanList = null;
    private ErrorAdapter errorAdapter;
    private ImageView empty_img;
    private TextView empty_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_errornote);
        initView();
    }

    private void initView() {
        toolbar_mid = (TextView) findViewById(R.id.toolbar_mid);
        toolbar_left_btn = (ImageButton) findViewById(R.id.toolbar_left_btn);
        toolbar_right_btn = (ImageButton) findViewById(R.id.toolbar_right_btn);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        linear_sbdk_btn = (LinearLayout) findViewById(R.id.linear_sbdk_btn);

        toolbar_left_btn.setOnClickListener(this);
        toolbar_right_btn.setOnClickListener(this);
        linear_sbdk_btn.setOnClickListener(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar_left_btn.setVisibility(View.VISIBLE);
        toolbar_mid.setText(R.string.text_ycss);
        pullRefresh_myError = (PullToRefreshRecyclerView) findViewById(R.id.pullRefresh_myError);
        View emptyView = View.inflate(this, R.layout.layout_empty_view, null);
        emptyView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        empty_img = (ImageView) emptyView.findViewById(R.id.empty_img);
        empty_text = (TextView) emptyView.findViewById(R.id.empty_text);
        empty_img.setImageDrawable(getResources().getDrawable(R.drawable.shensujilu));
        empty_text.setText(R.string.empty_shensu_view);
        pullRefresh_myError.setEmptyView(emptyView);
        pullRefresh_myError.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        pullRefresh_myError.setHasFixedSize(false);
        //设置是否开启上拉加载
        pullRefresh_myError.setLoadingMoreEnabled(false);
        //设置是否开启下拉刷新
        pullRefresh_myError.setPullRefreshEnabled(false);
        //设置是否显示上次刷新的时间
        pullRefresh_myError.displayLastRefreshTime(false);
        //设置刷新回调
//        pullRefresh_myError.setPullToRefreshListener(this);
        //主动触发下拉刷新操作
        //pullRefresh_myError.onRefresh();
        errorBeanList = new ArrayList<>();
//        msgBeanList.add(new MsgBean("2017年12月11日","23:20","泊讯消息","泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯"));
//        msgBeanList.add(new MsgBean("2017年12月11日","23:20","泊讯消息","泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯"));
//        msgBeanList.add(new MsgBean("2017年12月11日","23:20","泊讯消息","泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯"));
//        msgBeanList.add(new MsgBean("2017年12月11日","23:20","泊讯消息","泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯"));
//        msgBeanList.add(new MsgBean("2017年12月11日","23:20","泊讯消息","泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯"));
//        msgBeanList.add(new MsgBean("2017年12月11日","23:20","泊讯消息","泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯"));
//        msgBeanList.add(new MsgBean("2017年12月11日","23:20","泊讯消息","泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯"));
//        msgBeanList.add(new MsgBean("2017年12月11日","23:20","泊讯消息","泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯泊讯"));
        errorAdapter = new ErrorAdapter(ErrorNoteActivity.this, errorBeanList);
        pullRefresh_myError.setAdapter(errorAdapter);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_left_btn:
                finish();
                break;
            case R.id.linear_sbdk_btn:
                Intent intent = new Intent(ErrorNoteActivity.this, AddErrorActivity.class);
                startActivity(intent);
                break;

        }
    }
}
