package com.pda.carmanager.model;

import android.content.Context;

import com.pda.carmanager.model.inter.AddParkInter;
import com.pda.carmanager.presenter.inter.IAddParkPreInter;
import com.pda.carmanager.view.inter.IAddParkViewInter;

/**
 * Created by Administrator on 2017/12/15 0015.
 */

public class AddParkModel implements AddParkInter {
    private Context context;
    private IAddParkPreInter iAddParkPreInter;
    private String decs;

    public AddParkModel(Context context,IAddParkPreInter iAddParkPreInter) {
        this.context = context;
        this.iAddParkPreInter = iAddParkPreInter;
    }

    @Override
    public void getSweetData() {

    }

    @Override
    public void addPark() {

    }
}
