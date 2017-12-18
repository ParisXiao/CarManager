package com.pda.carmanager.model.inter;

import com.pda.carmanager.bean.MyParkBean;

import java.util.List;

/**
 * Created by Admin on 2017/12/15.
 */

public interface IParkInter {
    void postParkList(final String pageIndex, String pagesortfield,final List<MyParkBean> parkBeans);
    void getPrintInfo(String id);
}
