package com.pda.carmanager.presenter.inter;

/**
 * Created by Administrator on 2017/12/15 0015.
 */

public interface ILogoutPreInter {
    void logout();
    void getTodayPrice();
    void getSuccess(String moneny);
    void logoutSuccess();
    void logoutFails(String msg);
}
