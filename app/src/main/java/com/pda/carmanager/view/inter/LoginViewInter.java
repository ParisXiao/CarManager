package com.pda.carmanager.view.inter;

/**
 * Created by Admin on 2017/12/13.
 */

public interface LoginViewInter  {
    void loginSuccess();
    void loginFail(String failMsg);
    void loginError(String errorMsgs);
}
