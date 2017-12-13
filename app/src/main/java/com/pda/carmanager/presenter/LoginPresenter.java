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

    public LoginPresenter(Context context, LoginViewInter loginViewInter, LoginModelInter loginModelInter) {
        this.context = context;
        this.loginViewInter = loginViewInter;
        this.loginModelInter = new LoginModel(context,this);
    }

    @Override
    public void login() {

    }

    @Override
    public void loginSuccess() {

    }

    @Override
    public void loginFail() {

    }

    @Override
    public void loginError() {

    }
}
