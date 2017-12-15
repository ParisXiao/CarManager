package com.pda.carmanager.view.activity;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.Toolbar;
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
import com.pda.carmanager.view.inter.IParkViewInter;
import com.pda.carmanager.view.widght.CustomerCarDialog;
import com.pda.carmanager.view.widght.IdentifyingCodeView;
import com.suke.widget.SwitchButton;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/12/9 0009.
 */

public class MyParkActivity extends BaseActivity implements View.OnClickListener, ParkItemOnInter, PullToRefreshListener ,IParkViewInter{
    private TextView toolbar_mid;
    private ImageButton toolbar_left_btn;
    private Toolbar toolbar;
    private PullToRefreshRecyclerView pullRefresh_myPark;
    private MyParkAdapter myParkAdapter;
    private List<MyParkBean> parkBeanList = null;
    private PopupWindow popupWindow1;
    private ParkPresenter parkPresenter;

    /**
     * popupWindow1
     *
     * @param savedInstanceState
     */
    private Button chooseSmall, chooseBig, AreaBtn, Next, Exit;
    private SwitchButton chooseNew, chooseStu;
    private IdentifyingCodeView identifyingCodeView;
    private ImageView camera1, camera2;
    private TextView text_stucar;
    private CustomerCarDialog areaDialog;
    private MyParkBean parkBean;

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
        DialogUtil.showMessage(this,getResources().getString(R.string.text_loading));
        parkPresenter=new ParkPresenter(this,this);
        parkPresenter.postParkList();
        parkBeanList = new ArrayList<>();
        parkBeanList.add(new MyParkBean("货车", "1", "贵A132198", "No.24418"));
        parkBeanList.add(new MyParkBean("小车", "2", "贵A132198", "No.24418"));
        parkBeanList.add(new MyParkBean("小车", "3", "贵A132198", "No.24418"));
        parkBeanList.add(new MyParkBean("货车", "4", "贵A132198", "No.24418"));
        parkBeanList.add(new MyParkBean("货车", "2", "贵A132198", "No.24418"));
        myParkAdapter = new MyParkAdapter(this, parkBeanList, this);
        pullRefresh_myPark.setAdapter(myParkAdapter);


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
        DialogUtil.showBoXunVIP(MyParkActivity.this,carNum);
    }

    @Override
    public void onRefresh() {
        pullRefresh_myPark.postDelayed(new Runnable() {
            @Override
            public void run() {
                pullRefresh_myPark.setRefreshComplete();
                myParkAdapter.notifyDataSetChanged();
            }
        }, 2000);
    }

    @Override
    public void onLoadMore() {
        pullRefresh_myPark.postDelayed(new Runnable() {
            @Override
            public void run() {
                pullRefresh_myPark.setLoadMoreComplete(); //加载数据完成
                //模拟加载数据的情况
                for (int i = 0; i < 10; i++) {
                    parkBeanList.add(new MyParkBean("小车", "3", "贵A132198", "No.24418"));
                }
                myParkAdapter.notifyDataSetChanged();
            }
        }, 2000);
    }

    @Override
    public void parkSuccess(MyParkBean parkBean) {

    }

    @Override
    public void parkFail(String msg) {
        if (msg.equals(getResources().getString(R.string.httpOut))){
            AMUtil.actionStart(MyParkActivity.this, LoginActivity.class);
            finish();
        }
    }
}
