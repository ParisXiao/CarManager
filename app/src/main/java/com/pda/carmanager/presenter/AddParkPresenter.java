package com.pda.carmanager.presenter;

import android.content.Context;

import com.pda.carmanager.model.AddParkModel;
import com.pda.carmanager.model.inter.AddParkInter;
import com.pda.carmanager.presenter.inter.IAddParkPreInter;
import com.pda.carmanager.view.inter.IAddParkViewInter;

/**
 * Created by Administrator on 2017/12/15 0015.
 */

public class AddParkPresenter implements IAddParkPreInter {
    private Context context;
    private IAddParkViewInter iAddParkViewInter;
    private AddParkInter addParkInter;

    public AddParkPresenter(Context context, IAddParkViewInter iAddParkViewInter) {
        this.context = context;
        this.iAddParkViewInter = iAddParkViewInter;
        addParkInter=new AddParkModel(context,this);
    }

    @Override
    public void getSweetData() {

    }

    @Override
    public void addPark() {

    }

    @Override
    public void getSuccesss() {

    }

    @Override
    public void getFail(String msg) {

    }

    @Override
    public void addSuccess() {

    }

    @Override
    public void addFail(String msg) {

    }
}
