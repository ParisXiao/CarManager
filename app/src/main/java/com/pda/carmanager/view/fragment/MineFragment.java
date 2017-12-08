package com.pda.carmanager.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pda.carmanager.R;
import com.pda.carmanager.util.AMUtil;
import com.pda.carmanager.view.activity.ChargeRecordActivity;
import com.pda.carmanager.view.activity.DakaActivity;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;


/**
 * Created by Admin on 2017/11/29.
 */

public class MineFragment extends Fragment implements View.OnClickListener {
    private TextView text_usrName;
    private TextView text_todayMoney;
    private RelativeLayout rel_sbdk;
    private RelativeLayout rel_xbdk;
    private RelativeLayout rel_sfjl;
    private RelativeLayout rel_ycss;
    private Button button_logout;
    private Activity context;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_mine, null);
        initView(layout);
        return layout;
    }

    private void initView(View layout) {
        context=getActivity();
        text_usrName = (TextView) layout.findViewById(R.id.text_usrName);
        text_todayMoney = (TextView) layout.findViewById(R.id.text_todayMoney);
        rel_sbdk = (RelativeLayout) layout.findViewById(R.id.rel_sbdk);
        rel_xbdk = (RelativeLayout) layout.findViewById(R.id.rel_xbdk);
        rel_sfjl = (RelativeLayout) layout.findViewById(R.id.rel_sfjl);
        rel_ycss = (RelativeLayout) layout.findViewById(R.id.rel_ycss);
        button_logout = (Button) layout.findViewById(R.id.button_logout);
        rel_sbdk.setOnClickListener(this);
        rel_xbdk.setOnClickListener(this);
        rel_sfjl.setOnClickListener(this);
        rel_ycss.setOnClickListener(this);
        button_logout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent dakaType=new Intent(context,DakaActivity.class);
        dakaType.addFlags(FLAG_ACTIVITY_NEW_TASK);
        switch (v.getId()) {
            case R.id.rel_sbdk:
                dakaType.putExtra("DakaType","SB");
                startActivity(dakaType);
                break;
            case R.id.rel_xbdk:
                dakaType.putExtra("DakaType","XB");
                startActivity(dakaType);
                break;
            case R.id.rel_sfjl:
                AMUtil.actionStart(context, ChargeRecordActivity.class);
                break;
            case R.id.rel_ycss:
                break;
            case R.id.button_logout:

                break;
        }
    }
}
