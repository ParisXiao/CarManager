package com.pda.carmanager.presenter.inter;

import com.pda.carmanager.bean.PayInfoBean;

/**
 * Created by Administrator on 2017/12/17 0017.
 */

public interface IPayInfoPreInter {
    void getPayInfo(String id);
    void getSuccess(PayInfoBean payInfoBean);
    void getFail(String msg);
}
