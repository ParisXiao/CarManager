package com.pda.carmanager.model.inter;

/**
 * Created by Administrator on 2017/12/17 0017.
 */

public interface IPayInfoInter {
    void getPayInfo(String id);
    void Pay(String id,String type,String auth_code);
}
