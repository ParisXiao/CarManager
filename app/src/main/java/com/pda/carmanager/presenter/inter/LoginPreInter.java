package com.pda.carmanager.presenter.inter;

/**
 * Created by Admin on 2017/12/13.
 */

public interface LoginPreInter {
    void login(final String username,final String password,final String companycode);
    void loginSuccess();

    void loginFail(String loginMsg);

    void loginError(String errorMsg);
}
