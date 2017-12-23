package com.pda.carmanager.view.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pda.carmanager.R;
import com.pda.carmanager.config.AccountConfig;
import com.pda.carmanager.presenter.LogoutPresenter;
import com.pda.carmanager.service.SignalAService;
import com.pda.carmanager.util.AMUtil;
import com.pda.carmanager.util.DialogUtil;
import com.pda.carmanager.util.OKHttpUtil;
import com.pda.carmanager.util.PreferenceUtils;
import com.pda.carmanager.view.activity.ChargeRecordActivity;
import com.pda.carmanager.view.activity.DakaActivity;
import com.pda.carmanager.view.activity.ErrorNoteActivity;
import com.pda.carmanager.view.activity.LoginActivity;
import com.pda.carmanager.view.inter.ILogoutViewInter;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;


/**
 * Created by Admin on 2017/11/29.
 */

public class MineFragment extends Fragment implements View.OnClickListener,ILogoutViewInter {
    private TextView text_usrName;
    private TextView text_todayMoney;
    private RelativeLayout rel_sbdk;
    private RelativeLayout rel_xbdk;
    private RelativeLayout rel_sfjl;
    private RelativeLayout rel_ycss;
    private Button button_logout;
    private Activity context;
    private LogoutPresenter logoutPresenter;
    public static boolean flag=false;
    public static boolean flag1=false;

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
        logoutPresenter=new LogoutPresenter(context,this);
        text_usrName.setText(PreferenceUtils.getInstance(context).getString(AccountConfig.Realname));
    }

    @Override
    public void onResume() {
        super.onResume();
        logoutPresenter.getTodayPrice();
    }

    @Override
    public void onClick(View v) {
        Intent dakaType=new Intent(context,DakaActivity.class);
        dakaType.addFlags(FLAG_ACTIVITY_NEW_TASK);
        switch (v.getId()) {
            case R.id.rel_sbdk:
                dakaType.putExtra("DakaType","1");
                startActivity(dakaType);
                break;
            case R.id.rel_xbdk:
                dakaType.putExtra("DakaType","2");
                startActivity(dakaType);
                break;
            case R.id.rel_sfjl:
                AMUtil.actionStart(context, ChargeRecordActivity.class);
                break;
            case R.id.rel_ycss:
                AMUtil.actionStart(context, ErrorNoteActivity.class);
                break;
            case R.id.button_logout:
                if (!flag1)
                    flag1=true;
                Intent intent=new Intent(context, SignalAService.class);
                context.stopService(intent);
                showChooseMessage(context,"注销确认","是否确认注销并清空个人信息？");
                break;
        }
    }
    private void showChooseMessage(final Context context, String text, String textContent) {
        final AlertDialog progressDialog = new AlertDialog.Builder(context).create();
        if (!(progressDialog != null && progressDialog.isShowing())) {
            try {
                progressDialog.show();
//            WindowManager.LayoutParams params =
//                    dialog.getWindow().getAttributes();
//            params.width = 250;
//            params.height = 250;
//            dialog.getWindow().setAttributes(params);
                Window window = progressDialog.getWindow();
                WindowManager.LayoutParams lp = window.getAttributes();
                window.setGravity(Gravity.CENTER);
                lp.alpha = 1f;
                window.setAttributes(lp);
                window.setContentView(R.layout.layout_choose_dialog);
                TextView text1 = (TextView) window.findViewById(R.id.note_text);
                TextView text2 = (TextView) window.findViewById(R.id.note_text_content);
                Button button1 = (Button) window.findViewById(R.id.note_exit);
                Button button2 = (Button) window.findViewById(R.id.note_sure);
                text1.setText(text);
                text2.setText(textContent);
                button1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        progressDialog.dismiss();
                    }
                });
                button2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!flag)
                            flag=true;
                        if (OKHttpUtil.isConllection(context))
                            DialogUtil.showMessage(context,"正在注销...");
                        logoutPresenter.logout();
                        progressDialog.dismiss();

                    }
                });
            } catch (Exception e) {

            }
        }
    }

    @Override
    public void getSuccess(String moneny) {
        text_todayMoney.setText(moneny+"元");
    }

    @Override
    public void logoutSuccess() {
        flag=false;
        flag1=false;
        AMUtil.actionStart(context, LoginActivity.class);
    }

    @Override
    public void logoutFail(String Msg) {
        flag=false;
        flag1=false;

    }
}
