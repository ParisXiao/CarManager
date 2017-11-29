package com.pda.carmanager.view.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.pda.carmanager.R;
import com.pda.carmanager.base.BaseActivity;
import com.pda.carmanager.view.fragment.ManagementFragment;
import com.pda.carmanager.view.fragment.MessageFragment;
import com.pda.carmanager.view.fragment.MineFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    //GitHub测试
    private FragmentManager fragmentManager;
    private ImageButton[] imageButtons;
    List<Fragment> fragmentList;

    private int currentPosition;
    private TextView toolbar_mid;
    private ImageButton toolbar_left_btn;
    private TextView toolbar_left;
    private ImageButton toolbar_right_btn;
    private TextView toolbar_right;
    private Toolbar toolbar;
    private FrameLayout main_frame;
    private ImageButton imb_management;
    private ImageButton imb_message;
    private ImageButton imb_mine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //去除title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //去掉Activity上面的状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initFragment() {
        fragmentList = new ArrayList<>();
        fragmentList.add(new ManagementFragment());
        fragmentList.add(new MessageFragment());
        fragmentList.add(new MineFragment());
        fragmentManager = getSupportFragmentManager();
        for (Fragment fragment : fragmentList) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.main_frame, fragment);
            fragmentTransaction.hide(fragment);
            fragmentTransaction.commitAllowingStateLoss();

        }
    }

    private void initView() {
        toolbar_mid = (TextView) findViewById(R.id.toolbar_mid);
        toolbar_left_btn = (ImageButton) findViewById(R.id.toolbar_left_btn);
        toolbar_left = (TextView) findViewById(R.id.toolbar_left);
        toolbar_right_btn = (ImageButton) findViewById(R.id.toolbar_right_btn);
        toolbar_right = (TextView) findViewById(R.id.toolbar_right);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        main_frame = (FrameLayout) findViewById(R.id.main_frame);

        toolbar_left_btn.setOnClickListener(this);
        toolbar_left.setOnClickListener(this);
        toolbar_right_btn.setOnClickListener(this);
        toolbar_right.setOnClickListener(this);
        imb_management = (ImageButton) findViewById(R.id.imb_management);
        imb_management.setOnClickListener(this);
        imb_message = (ImageButton) findViewById(R.id.imb_message);
        imb_message.setOnClickListener(this);
        imb_mine = (ImageButton) findViewById(R.id.imb_mine);
        imb_mine.setOnClickListener(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        initFragment();
    }

    private void showFragment(int position) {
        if (position == currentPosition) return;
        if (position < 0 || position >= fragmentList.size()) return;

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.hide(fragmentList.get(currentPosition));
        fragmentTransaction.commitAllowingStateLoss();

        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.show(fragmentList.get(position));
        fragmentTransaction.commitAllowingStateLoss();

        currentPosition = position;
    }


    /**
     * 刷新底部控件
     */
    private void refreshImageButtons(int position) {
        for (int i = 0; i < imageButtons.length; i++) {
            if (i == position) {
//                imageButtons[i].setImageResource(resSelectIds[i]);
            } else {
//                imageButtons[i].setImageResource(resIds[i]);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_left_btn:

                break;
            case R.id.toolbar_left:

                break;
            case R.id.toolbar_right_btn:

                break;
            case R.id.toolbar_right:

                break;
            case R.id.imb_management:
                toolbar_mid.setText("车位管理");
                refreshImageButtons(0);
                showFragment(0);
                break;
            case R.id.imb_message:
                toolbar_mid.setText("我的信息");
                refreshImageButtons(1);
                showFragment(1);
                break;
            case R.id.imb_mine:
                toolbar_mid.setText("个人中心");
                refreshImageButtons(2);
                showFragment(2);
                break;
        }
    }
}
