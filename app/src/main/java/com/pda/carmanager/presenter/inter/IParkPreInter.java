package com.pda.carmanager.presenter.inter;

import com.pda.carmanager.bean.MyParkBean;

import java.util.List;

/**
 * Created by Admin on 2017/12/15.
 */

public interface IParkPreInter {
    void postParkList(final String pageIndex, String pagesortfield,final List<MyParkBean> parkBeans);
    void parkSuccess(String pages);
    void parkFail(String msg);
}
