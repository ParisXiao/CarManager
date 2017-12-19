package com.pda.carmanager.presenter.inter;

import java.io.Serializable;

/**
 * Created by Admin on 2017/12/19.
 */

public interface ISplashPreInter {
    void updata();
    void splash();
    void needUpdata(Serializable object);
    void notUpdata();
    void updataFail(String msg);
    void splashLoginSuccess();
    void splashLoginFail(String msg);
}
