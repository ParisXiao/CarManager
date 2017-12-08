package com.pda.carmanager.view.fragment;

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
    private Button button_login;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_mine, null);
        initView(layout);
        return layout;
    }

    private void initView(View layout) {
        text_usrName = (TextView) layout.findViewById(R.id.text_usrName);
        text_todayMoney = (TextView) layout.findViewById(R.id.text_todayMoney);
        rel_sbdk = (RelativeLayout) layout.findViewById(R.id.rel_sbdk);
        rel_xbdk = (RelativeLayout) layout.findViewById(R.id.rel_xbdk);
        rel_sfjl = (RelativeLayout) layout.findViewById(R.id.rel_sfjl);
        rel_ycss = (RelativeLayout) layout.findViewById(R.id.rel_ycss);
        button_login = (Button) layout.findViewById(R.id.button_login);

        button_login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_login:

                break;
        }
    }
}
