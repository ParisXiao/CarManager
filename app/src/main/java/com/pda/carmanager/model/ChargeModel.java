package com.pda.carmanager.model;

import android.content.Context;

import com.pda.carmanager.bean.ChargeBean;
import com.pda.carmanager.model.inter.IChargeInter;
import com.pda.carmanager.presenter.inter.IChargePreInter;

import java.util.List;

/**
 * Created by Admin on 2017/12/18.
 */

public class ChargeModel  implements IChargeInter{
    private Context context;
    private IChargePreInter iChargePreInter;
    private String decs;

    public ChargeModel(Context context, IChargePreInter iChargePreInter) {
        this.context = context;
        this.iChargePreInter = iChargePreInter;
    }

    @Override
    public void getCharge(String jddid, List<ChargeBean> chargeBeanList) {

    }
}
