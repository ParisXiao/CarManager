package com.pda.carmanager.view.inter;

import com.pda.carmanager.bean.MyParkBean;

import java.util.List;

/**
 * Created by Admin on 2017/12/15.
 */

public interface IParkViewInter  {
    void parkSuccess(String pages);
    void parkFail(String msg);
}
