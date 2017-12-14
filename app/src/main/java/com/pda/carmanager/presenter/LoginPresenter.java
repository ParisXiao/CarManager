package com.pda.carmanager.presenter;

import android.content.Context;

import com.pda.carmanager.model.LoginModel;
import com.pda.carmanager.model.inter.LoginModelInter;
import com.pda.carmanager.presenter.inter.LoginPreInter;
import com.pda.carmanager.view.inter.LoginViewInter;

/**
 * Created by Admin on 2017/12/13.
 */

public class LoginPresenter implements LoginPreInter {
    private Context context;
    private LoginViewInter loginViewInter;
    private LoginModelInter loginModelInter;

    public LoginPresenter(Context context, LoginViewInter loginViewInter) {
        this.context = context;
        this.loginViewInter = loginViewInter;
        loginModelInter = new LoginModel(context,this);
    }

    @Override
    public void login(String username, String password, String companycode) {
        loginModelInter.getLoginInfo(username,password,companycode);
    }

    @Override
    public void loginSuccess() {
        loginViewInter.loginSuccess();
    }

    @Override
    public void loginFail(String loginMsg) {
        loginViewInter.loginFail(loginMsg);
    }

    @Override
    public void loginError(String errorMsg) {
        loginViewInter.loginError(errorMsg);
    }
}
