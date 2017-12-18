package com.pda.carmanager.presenter.inter;

import com.pda.carmanager.bean.DakaBean;

import java.util.List;

/**
 * Created by Admin on 2017/12/15.
 */

public interface IDakaPreInter {
    void postDaka(String address,String dakaType);
    void getDaka(String kqtime,String page,List<DakaBean> dakaBeanList);
    void dakaSuccess();
    void getdakaSuccess( String pages);
    void dakaFail(String msg);
}
