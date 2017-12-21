package com.pda.carmanager.model.inter;

import com.pda.carmanager.bean.MsgBean;

import java.util.List;

/**
 * Created by Administrator on 2017/12/21 0021.
 */

public interface IMsgInter {
    void getMsg(String page, List<MsgBean> msgBeenList);
}
