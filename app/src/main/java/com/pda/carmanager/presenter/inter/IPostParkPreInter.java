package com.pda.carmanager.presenter.inter;

import com.pda.carmanager.bean.PrintBean;

/**
 * Created by Administrator on 2017/12/17 0017.
 */

public interface IPostParkPreInter {
    void postPark(String id,String carnum,String carType,String img1,String img2);
    void postSuccess(PrintBean printBean);
    void postFail(String msg);
}
