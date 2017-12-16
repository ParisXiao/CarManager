package com.pda.carmanager.view.inter;

/**
 * Created by Administrator on 2017/12/15 0015.
 */

public interface IAddParkViewInter {
    void getSuccesss();
    void getFail(String msg);
    void getDuanSuccesss();
    void getDuanFail(String msg);
    void addSuccess();
    void addFail(String msg);
}
