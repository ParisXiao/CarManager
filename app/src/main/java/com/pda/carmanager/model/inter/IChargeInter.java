package com.pda.carmanager.model.inter;

import com.pda.carmanager.bean.ChargeBean;

import java.util.List;

/**
 * Created by Admin on 2017/12/18.
 */

public interface IChargeInter {
    void getCharge(String jddid,String page, List<ChargeBean> chargeBeanList);
}
