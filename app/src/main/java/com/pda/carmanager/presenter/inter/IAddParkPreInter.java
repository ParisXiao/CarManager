package com.pda.carmanager.presenter.inter;

/**
 * Created by Administrator on 2017/12/15 0015.
 */

public interface IAddParkPreInter {
    void getSweetData();
    void addPark();
    void getSuccesss();
    void getFail(String msg);
    void addSuccess();
    void addFail(String msg);
}
