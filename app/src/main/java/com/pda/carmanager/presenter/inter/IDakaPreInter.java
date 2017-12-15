package com.pda.carmanager.presenter.inter;

/**
 * Created by Admin on 2017/12/15.
 */

public interface IDakaPreInter {
    void postDaka(String address,String dakaType);
    void dakaSuccess();
    void dakaFail(String msg);
}
