package com.pda.carmanager.presenter.inter;

import com.pda.carmanager.bean.MyParkBean;

/**
 * Created by Admin on 2017/12/15.
 */

public interface IParkPreInter {
    void postParkList(String pageIndex,String pagesortfield);
    void parkSuccess(MyParkBean parkBean);
    void parkFail(String msg);
}
