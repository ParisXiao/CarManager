package com.pda.carmanager.view.inter;

import java.io.Serializable;

/**
 * Created by Admin on 2017/12/19.
 */

public interface ISplashViewInter {
    void needUpdata(Serializable object);
    void notUpdata();
    void updataFail(String msg);
    void splashLoginSuccess();
    void splashLoginFail(String msg);
}
