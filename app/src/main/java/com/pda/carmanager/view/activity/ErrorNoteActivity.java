package com.pda.carmanager.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidkun.PullToRefreshRecyclerView;
import com.androidkun.callback.PullToRefreshListener;
import com.pda.carmanager.R;
import com.pda.carmanager.adapter.ErrorAdapter;
import com.pda.carmanager.base.BaseActivity;
import com.pda.carmanager.bean.ErrorBean;
import com.pda.carmanager.config.AccountConfig;
import com.pda.carmanager.presenter.ErrorNotesPresenter;
import com.pda.carmanager.pulltorefresh.SpacesItemDecoration;
import com.pda.carmanager.util.AMUtil;
import com.pda.carmanager.util.PreferenceUtils;
import com.pda.carmanager.util.UserInfoClearUtil;
import com.pda.carmanager.view.inter.IErrorNotesViewInter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/12/12 0012.
 */

public class ErrorNoteActivity extends BaseActivity implements View.OnClickListener  ,IErrorNotesViewInter,PullToRefreshListener{
    private TextView toolbar_mid;
    private ImageButton toolbar_left_btn;
    private ImageButton toolbar_right_btn;
    private Toolbar toolbar;
    private LinearLayout linear_sbdk_btn;
    private PullToRefreshRecyclerView pullRefresh_myError;
    private List<ErrorBean> errorBeanList = null;
    private List<ErrorBean> errorBeanListShow = new ArrayList<>() ;
    private ErrorAdapter errorAdapter;
    private ImageView empty_img;
    private TextView empty_text;
    private  View emptyView;
    private ErrorNotesPresenter errorNotesPresenter;
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
                        errorBeanListShow.clear();
                    }
                    if (errorBeanList != null && errorBeanList.size() > 0) {
                        Log.e("list", list + "");
                        int s = errorBeanList.size() < list ? errorBeanList.size() : list;
                        for (int i = 0; i < s; i++) {
                            errorBeanListShow.add(errorBeanList.get(i));
                        }
                        errorAdapter.notifyDataSetChanged();
                    } else {
                        errorBeanListShow.clear();
                        errorAdapter.notifyDataSetChanged();
                    }
                    isRefreah = false;
                    reFreshNext = false;
                    if (load == 1) {
                        pullRefresh_myError.setRefreshComplete();
                    } else if (load == 2) {
                        pullRefresh_myError.setLoadMoreComplete();
                    }
                    pullRefresh_myError.setEmptyView(emptyView);

                    break;
                case 1:
                    if (load == 1) {
                        pullRefresh_myError.setRefreshComplete();
                    } else if (load == 2) {
                        pullRefresh_myError.setLoadMoreComplete();
                    }
                    break;
            }
        }
    };
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
        emptyView = View.inflate(this, R.layout.layout_empty_view, null);
        emptyView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        empty_img = (ImageView) emptyView.findViewById(R.id.empty_img);
        empty_text = (TextView) emptyView.findViewById(R.id.empty_text);
        empty_img.setImageDrawable(getResources().getDrawable(R.drawable.shensujilu));
        empty_text.setText(R.string.empty_shensu_view);

        pullRefresh_myError.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        pullRefresh_myError.addItemDecoration(new SpacesItemDecoration(getResources().getDimensionPixelSize(R.dimen.padding_middle1)));
        pullRefresh_myError.setHasFixedSize(true);
        //设置是否开启上拉加载
        pullRefresh_myError.setLoadingMoreEnabled(true);
        //设置是否开启下拉刷新
        pullRefresh_myError.setPullRefreshEnabled(true);
        //设置是否显示上次刷新的时间
        pullRefresh_myError.displayLastRefreshTime(false);
        //设置刷新回调
        pullRefresh_myError.setPullToRefreshListener(this);
        //主动触发下拉刷新操作
        //pullRefresh_myError.onRefresh();
        errorBeanList = new ArrayList<>();
        errorAdapter = new ErrorAdapter(ErrorNoteActivity.this, errorBeanList);
        pullRefresh_myError.setAdapter(errorAdapter);
        errorNotesPresenter=new ErrorNotesPresenter(this,this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        page=1;
        errorNotesPresenter.getError(page+"", PreferenceUtils.getInstance(ErrorNoteActivity.this).getString(AccountConfig.Departmentid),errorBeanList);

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

    @Override
    public void getSuccess(String pages) {
        this.Pages = Integer.valueOf(pages);
        if (Pages <= page) {
            hasNext = false;
        } else {
            hasNext = true;
        }
        handler.sendEmptyMessage(0);
    }

    @Override
    public void getFail(String msg) {
        if (msg.equals(getResources().getString(R.string.httpOut))) {
            UserInfoClearUtil.ClearUserInfo(ErrorNoteActivity.this);
            AMUtil.actionStart(ErrorNoteActivity.this, LoginActivity.class);
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
            errorNotesPresenter.getError(page+"", PreferenceUtils.getInstance(ErrorNoteActivity.this).getString(AccountConfig.Departmentid),errorBeanList);
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
                errorNotesPresenter.getError(page+"", PreferenceUtils.getInstance(ErrorNoteActivity.this).getString(AccountConfig.Departmentid),errorBeanList);
            }
        } else {
            handler.sendEmptyMessageDelayed(1, 1000);
        }
    }
}
