package com.pda.carmanager.model.inter;

import com.pda.carmanager.bean.SweetBean;
import com.pda.carmanager.bean.SweetDuanBean;

import java.util.List;

/**
 * Created by Administrator on 2017/12/15 0015.
 */

public interface AddParkInter {
    void getSweetData(List<SweetBean> sweetBeens);
    void getSweetDuanData(String jdid,List<SweetDuanBean> duanBeanList);
    void addPark(String jd, String jdd,  String dcbh, String cwbh);
}
