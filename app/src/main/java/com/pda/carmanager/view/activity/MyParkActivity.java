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
import com.pda.carmanager.pullrefresh.GridSpacingItemDecoration;
import com.pda.carmanager.util.AMUtil;
import com.pda.carmanager.view.widght.CustomerCarDialog;
import com.pda.carmanager.view.widght.IdentifyingCodeView;
import com.suke.widget.SwitchButton;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/12/9 0009.
 */

public class MyParkActivity extends BaseActivity implements View.OnClickListener, ParkItemOnInter, PullToRefreshListener {
    private TextView toolbar_mid;
    private ImageButton toolbar_left_btn;
    private Toolbar toolbar;
    private PullToRefreshRecyclerView pullRefresh_myPark;
    private MyParkAdapter myParkAdapter;
    private List<MyParkBean> parkBeanList = null;
    private PopupWindow popupWindow1;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_mypark);
        initView();
        initData();
        initPopupwindow();
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
        parkBeanList = new ArrayList<>();
        parkBeanList.add(new MyParkBean("货车", "1", "贵A132198", "No.24418"));
        parkBeanList.add(new MyParkBean("小车", "2", "贵A132198", "No.24418"));
        parkBeanList.add(new MyParkBean("小车", "3", "贵A132198", "No.24418"));
        parkBeanList.add(new MyParkBean("货车", "4", "贵A132198", "No.24418"));
        parkBeanList.add(new MyParkBean("货车", "2", "贵A132198", "No.24418"));
        myParkAdapter = new MyParkAdapter(this, parkBeanList, this);
        pullRefresh_myPark.setAdapter(myParkAdapter);


    }

    private void initPopupwindow() {
        View view = LayoutInflater.from(this).inflate(R.layout.layout_pop_car, null);
        popupWindow1 = new PopupWindow(this);
        popupWindow1.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow1.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow1.setContentView(view);
        popupWindow1.setBackgroundDrawable(new ColorDrawable(0x00000000));
        popupWindow1.setOutsideTouchable(false);
        popupWindow1.setFocusable(true);
        chooseSmall = (Button) view.findViewById(R.id.pop_choose_small);
        chooseBig = (Button) view.findViewById(R.id.pop_choose_big);
        chooseNew = (SwitchButton) view.findViewById(R.id.btn_catType_new);
        chooseStu = (SwitchButton) view.findViewById(R.id.btn_catType_stu);
        AreaBtn = (Button) view.findViewById(R.id.btn_area);
        identifyingCodeView = (IdentifyingCodeView) view.findViewById(R.id.carNum_edit1);
        camera1 = (ImageView) view.findViewById(R.id.camera_1);
        camera2 = (ImageView) view.findViewById(R.id.camera_2);
        Next = (Button) view.findViewById(R.id.pop1_next);
        Exit = (Button) view.findViewById(R.id.pop1_exit);
        text_stucar = (TextView) view.findViewById(R.id.text_stucar);
        areaDialog = new CustomerCarDialog(this, "贵", "A");
        chooseSmall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseSmall.setBackground(getResources().getDrawable(R.drawable.shape_choose_on));
                chooseBig.setBackground(getResources().getDrawable(R.drawable.shape_choose));
            }
        });
        chooseBig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseBig.setBackground(getResources().getDrawable(R.drawable.shape_choose_on));
                chooseSmall.setBackground(getResources().getDrawable(R.drawable.shape_choose));
            }
        });
        chooseNew.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if (isChecked) {
                    identifyingCodeView.setTextCount(6);
                    chooseNew.setChecked(true);
                    chooseStu.setChecked(false);
                    text_stucar.setVisibility(View.GONE);
                }

            }
        });
        chooseStu.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if (isChecked) {
                    identifyingCodeView.setTextCount(4);
                    chooseNew.setChecked(false);
                    chooseStu.setChecked(true);
                    text_stucar.setVisibility(View.VISIBLE);
                }
            }
        });
        AreaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                areaDialog.show();
                areaDialog.setOnDateDialogListener(new CustomerCarDialog.DateDialogListener() {
                    @Override
                    public void getDate() {
                        AreaBtn.setText(areaDialog.getSettingHour() + areaDialog.getSettingMinute());
                    }
                });
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

    @Override
    public void writeCarNum() {
        AMUtil.actionStartForResult(MyParkActivity.this, DialogCarWriteActivity.class);
    }

    @Override
    public void payCar() {
        AMUtil.actionStartForResult(MyParkActivity.this, PayMessageActivity.class);
    }

    @Override
    public void AutoPayCar() {

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
}
