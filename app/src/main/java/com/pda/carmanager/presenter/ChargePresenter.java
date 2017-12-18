package com.pda.carmanager.presenter;

import android.content.Context;

import com.pda.carmanager.bean.ChargeBean;
import com.pda.carmanager.model.ChargeModel;
import com.pda.carmanager.model.inter.IChargeInter;
import com.pda.carmanager.presenter.inter.IChargePreInter;
import com.pda.carmanager.view.inter.IChargeViewInter;

import java.util.List;

/**
 * Created by Admin on 2017/12/18.
 */

public class ChargePresenter implements IChargePreInter {
    private Context context;
    private IChargeViewInter iChargeViewInter;
    private IChargeInter iChargeInter;

    public ChargePresenter(Context context, IChargeViewInter iChargeViewInter) {
        this.context = context;
        this.iChargeViewInter = iChargeViewInter;
        iChargeInter = new ChargeModel(context, this);
    }

    @Override
    public void getCharge(String jddid, String page, List<ChargeBean> chargeBeanList) {
        iChargeInter.getCharge(jddid, page, chargeBeanList);
    }

    @Override
    public void getSuccess(String pages) {
        iChargeViewInter.getSuccess(pages);

    }

    @Override
    public void getFail(String msg) {
        iChargeViewInter.getFail(msg);
    }
}
