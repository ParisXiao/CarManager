package com.pda.carmanager.model.inter;

import com.pda.carmanager.bean.DakaBean;

import java.util.List;

/**
 * Created by Admin on 2017/12/15.
 */

public interface IDakaInter {
    void postDaka(String address,String DakaType);
    void getDaka(String kqdate,String page, List<DakaBean> dakaBeanList);
}
