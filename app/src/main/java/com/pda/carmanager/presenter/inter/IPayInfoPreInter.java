package com.pda.carmanager.presenter.inter;

import com.pda.carmanager.bean.PayInfoBean;

/**
 * Created by Administrator on 2017/12/17 0017.
 */

public interface IPayInfoPreInter {
    void getPayInfo(String id);
    void Pay(String id,String type,String auth_code);
    void getSuccess(PayInfoBean payInfoBean);
    void paySuccess();
    void getFail(String msg);
}
