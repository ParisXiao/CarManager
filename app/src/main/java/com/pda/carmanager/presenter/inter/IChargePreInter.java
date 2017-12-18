package com.pda.carmanager.presenter.inter;

import com.pda.carmanager.bean.ChargeBean;

import java.util.List;

/**
 * Created by Admin on 2017/12/18.
 */

public interface IChargePreInter {
    void getCharge(String jddid, List<ChargeBean> chargeBeanList);
    void getSuccess();
    void getFail(String msg);
}
