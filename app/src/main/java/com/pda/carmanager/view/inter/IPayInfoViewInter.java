package com.pda.carmanager.view.inter;

import com.pda.carmanager.bean.PayInfoBean;

/**
 * Created by Administrator on 2017/12/17 0017.
 */

public interface IPayInfoViewInter {
    void getSuccess(PayInfoBean payInfoBean);
    void getFail(String msg);
}
