package com.pda.carmanager.view.inter;

import com.pda.carmanager.bean.MyParkBean;
import com.pda.carmanager.bean.PrintBean;

import java.util.List;

/**
 * Created by Admin on 2017/12/15.
 */

public interface IParkViewInter  {
    void getPrintSuccess(PrintBean printBean);
    void parkSuccess(String pages);
    void parkFail(String msg);
}
