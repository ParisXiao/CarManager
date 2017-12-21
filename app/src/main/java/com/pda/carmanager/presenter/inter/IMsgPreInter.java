package com.pda.carmanager.presenter.inter;

import com.pda.carmanager.bean.MsgBean;

import java.util.List;

/**
 * Created by Administrator on 2017/12/21 0021.
 */

public interface IMsgPreInter {
    void getMsg(String page, List<MsgBean> msgBeenList);
    void getSuccess(String pages);
    void getFail(String decs);
}
