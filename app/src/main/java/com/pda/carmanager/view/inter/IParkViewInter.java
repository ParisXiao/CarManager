package com.pda.carmanager.view.inter;

import com.pda.carmanager.bean.MyParkBean;

/**
 * Created by Admin on 2017/12/15.
 */

public interface IParkViewInter  {
    void parkSuccess(MyParkBean parkBean);
    void parkFail(String msg);
}
