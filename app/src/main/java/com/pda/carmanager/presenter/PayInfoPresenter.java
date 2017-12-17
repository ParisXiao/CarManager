package com.pda.carmanager.presenter;

import android.content.Context;

import com.pda.carmanager.bean.PayInfoBean;
import com.pda.carmanager.model.PayInfoModel;
import com.pda.carmanager.model.inter.IPayInfoInter;
import com.pda.carmanager.presenter.inter.IPayInfoPreInter;
import com.pda.carmanager.view.inter.IPayInfoViewInter;

/**
 * Created by Administrator on 2017/12/17 0017.
 */

public class PayInfoPresenter implements IPayInfoPreInter {
    private Context context;
    private IPayInfoViewInter iPayInfoViewInter;
    private IPayInfoInter iPayInfoInter;

    public PayInfoPresenter(Context context, IPayInfoViewInter iPayInfoViewInter) {
        this.context = context;
        this.iPayInfoViewInter = iPayInfoViewInter;
        iPayInfoInter=new PayInfoModel(context,this);
    }

    @Override
    public void getPayInfo(String id) {

        iPayInfoInter.getPayInfo(id);
    }

    @Override
    public void Pay(String id,String type,String auth_code) {
        iPayInfoInter.Pay(id,type,auth_code);
    }

    @Override
    public void getSuccess(PayInfoBean payInfoBean) {
        iPayInfoViewInter.getSuccess(payInfoBean);
    }

    @Override
    public void paySuccess() {
        iPayInfoViewInter.paySuccess();
    }

    @Override
    public void getFail(String msg) {
        iPayInfoViewInter.getFail(msg);
    }
}
