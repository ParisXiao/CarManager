package com.pda.carmanager.model.inter;

import com.pda.carmanager.bean.ErrorBean;

import java.util.List;

/**
 * Created by Admin on 2017/12/20.
 */

public interface IErrorNotesInter {
    void getError(String pageIndex, String pagesortfield, List<ErrorBean> errorBeanList);
}
