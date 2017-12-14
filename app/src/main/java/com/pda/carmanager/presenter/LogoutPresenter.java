package com.pda.carmanager.presenter;

import android.content.Context;

import com.pda.carmanager.bean.ChargeBean;
import com.pda.carmanager.model.LogoutModel;
import com.pda.carmanager.model.inter.ILogoutInter;
import com.pda.carmanager.presenter.inter.ILogoutPreInter;
import com.pda.carmanager.view.inter.ILogoutViewInter;

/**
 * Created by Administrator on 2017/12/15 0015.
 */

public class LogoutPresenter   implements ILogoutPreInter{
    private Context context;
    private ILogoutInter iLogoutInter;
    private ILogoutViewInter iLogoutViewInter;

    public LogoutPresenter(Context context, ILogoutViewInter iLogoutViewInter) {
        this.context = context;
        this.iLogoutViewInter=iLogoutViewInter;
        iLogoutInter = new LogoutModel(context,this);
    }

    @Override
    public void logout() {
        iLogoutInter.logout();
    }

    @Override
    public void logoutSuccess() {
        iLogoutViewInter.logoutSuccess();
    }

    @Override
    public void logoutFails(String msg) {
        iLogoutViewInter.logoutFail(msg);
    }
}
