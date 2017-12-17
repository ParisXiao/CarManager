package com.pda.carmanager.view.inter;

import com.pda.carmanager.bean.PrintBean;

/**
 * Created by Administrator on 2017/12/17 0017.
 */

public interface IPostParkViewInter {
    void postSuccess(PrintBean printBean);
    void postFail(String msg);
}
