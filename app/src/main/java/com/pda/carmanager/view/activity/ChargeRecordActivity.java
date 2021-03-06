package com.pda.carmanager.view.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.pda.carmanager.R;
import com.pda.carmanager.adapter.ChargeAdapter;
import com.pda.carmanager.base.BaseActivity;
import com.pda.carmanager.bean.ChargeBean;
import com.pda.carmanager.config.AccountConfig;
import com.pda.carmanager.presenter.ChargePresenter;
import com.pda.carmanager.pulltorefresh.PullToRefreshBase;
import com.pda.carmanager.pulltorefresh.PullToRefreshListView;
import com.pda.carmanager.util.AMUtil;
import com.pda.carmanager.util.PreferenceUtils;
import com.pda.carmanager.util.UserInfoClearUtil;
import com.pda.carmanager.view.inter.IChargeViewInter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/12/8 0008.
 */

public class ChargeRecordActivity extends BaseActivity implements View.OnClickListener,IChargeViewInter {

    private PullToRefreshListView pullRefresh_Charge;
    private ChargeAdapter chargeAdapter=null;
    private List<ChargeBean> chargeBeanList = null;
    private List<ChargeBean> chargeBeanListShow = new ArrayList<>();
    private TextView toolbar_mid;
    private ImageButton toolbar_left_btn;
    private Toolbar toolbar;
    private ImageView empty_img;
    private TextView empty_text;
    private View emptyView;
    private int page = 1;
    private int Pages = 0;
    private boolean reFreshNext;
    private boolean hasNext;
    private boolean isRefreah;
    private int list = 10;
    private int load = 0;
    private ChargePresenter chargePresenter;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    if (!reFreshNext) {
                        chargeBeanListShow.clear();
                    }
                    if (chargeBeanList != null && chargeBeanList.size() > 0) {
                        Log.e("list", list + "");
                        int s = chargeBeanList.size() < list ? chargeBeanList.size() : list;
                        for (int i = 0; i < s; i++) {
                            chargeBeanListShow.add(chargeBeanList.get(i));
                        }
                        chargeAdapter.notifyDataSetChanged();
                    } else {
                        chargeBeanListShow.clear();
                        chargeAdapter.notifyDataSetChanged();
                    }
                    isRefreah = false;
                    reFreshNext = false;
                    pullRefresh_Charge.setEmptyView(emptyView);
                    pullRefresh_Charge.onRefreshComplete();
                    break;
                case 1:
                    pullRefresh_Charge.onRefreshComplete();
                    break;

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charger);
        initView();
        initData();
    }


    private void initView() {

        pullRefresh_Charge = (PullToRefreshListView) findViewById(R.id.pullRefresh_Charge);
        emptyView = View.inflate(this, R.layout.layout_empty_view, null);
        emptyView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        empty_img = (ImageView) emptyView.findViewById(R.id.empty_img);
        empty_text = (TextView) emptyView.findViewById(R.id.empty_text);
        empty_img.setImageDrawable(getResources().getDrawable(R.drawable.shoufeijilu_no));
        empty_text.setText(R.string.empty_charge_view);

        toolbar_mid = (TextView) findViewById(R.id.toolbar_mid);
        toolbar_mid.setOnClickListener(this);
        toolbar_left_btn = (ImageButton) findViewById(R.id.toolbar_left_btn);
        toolbar_left_btn.setOnClickListener(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setOnClickListener(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar_left_btn.setVisibility(View.VISIBLE);
        pullRefresh_Charge.setMode(PullToRefreshBase.Mode.BOTH);
        PullToRefreshBase.OnRefreshListener2<ListView> pullListener = new PullToRefreshBase.OnRefreshListener2<ListView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                load = 1;
                if (!isRefreah) {
                    isRefreah = true;
                    if (!reFreshNext) {
                        page = 1;
                    }
                    chargePresenter .getCharge(PreferenceUtils.getInstance(ChargeRecordActivity.this).getString(AccountConfig.Departmentid),page+"",chargeBeanList);

                }
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                load = 2;
                if (hasNext) {
                    page += 1;
                    reFreshNext = true;
                    if (!isRefreah) {
                        isRefreah = true;
                        if (!reFreshNext) {
                            page = 1;
                        }
                        chargePresenter .getCharge(PreferenceUtils.getInstance(ChargeRecordActivity.this).getString(AccountConfig.Departmentid),page+"",chargeBeanList);
                    }
                } else {
                    handler.sendEmptyMessageDelayed(1, 1000);
                }
            }
        };
        pullRefresh_Charge.setOnRefreshListener(pullListener);
    }

    private void initData() {
        toolbar_mid.setText(R.string.text_sfjl);
        chargeBeanList = new ArrayList<>();
        chargePresenter=new ChargePresenter(this,this);
        chargePresenter .getCharge(PreferenceUtils.getInstance(ChargeRecordActivity.this).getString(AccountConfig.Departmentid),page+"",chargeBeanList);
        chargeAdapter = new ChargeAdapter(this, chargeBeanListShow);
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
            UserInfoClearUtil.ClearUserInfo(ChargeRecordActivity.this);
            AMUtil.actionStart(ChargeRecordActivity.this, LoginActivity.class);
            finish();
        }else {
            finish();
        }
    }
}
