package com.pda.carmanager.presenter.inter;

import com.pda.carmanager.bean.SweetBean;
import com.pda.carmanager.bean.SweetDuanBean;

import java.util.List;

/**
 * Created by Administrator on 2017/12/15 0015.
 */

public interface IAddParkPreInter {
    void getSweetData(List<SweetBean> sweetBeen);
    void getSweetDuanData(String jdid,List<SweetDuanBean> duanBeanList);
    void addPark(String jd, String jdd,  String dcbh, String cwbh);
    void getSuccesss();
    void getFail(String msg);
    void getDuanSuccesss();
    void getDuanFail(String msg);
    void addSuccess();
    void addFail(String msg);
}
