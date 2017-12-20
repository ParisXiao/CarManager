package com.pda.carmanager.presenter.inter;

import com.pda.carmanager.bean.ErrorBean;

import java.util.List;

/**
 * Created by Admin on 2017/12/20.
 */

public interface IErrorNotesPreInter {
    void getError(String pageIndex, String pagesortfield, List<ErrorBean> errorBeanList);
    void getSuccess(String pages);
    void getFail(String msg) ;
}
