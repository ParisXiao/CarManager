package com.pda.carmanager.presenter.inter;

import com.pda.carmanager.bean.MyParkBean;

import java.util.List;

/**
 * Created by Admin on 2017/12/15.
 */

public interface IParkPreInter {
    void postParkList(String pageIndex,String pagesortfield);
    void parkSuccess(List<MyParkBean> parkBeans);
    void parkFail(String msg);
}
