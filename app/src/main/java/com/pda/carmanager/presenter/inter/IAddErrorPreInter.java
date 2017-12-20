package com.pda.carmanager.presenter.inter;

/**
 * Created by Admin on 2017/12/20.
 */

public interface IAddErrorPreInter {
    void addError(String mycarno,String imgurl,String addr);
    void addSuccess();
    void addFail(String msg);
}
