package com.pda.carmanager.presenter;

import android.content.Context;

import com.pda.carmanager.model.DakaModel;
import com.pda.carmanager.model.inter.IDakaInter;
import com.pda.carmanager.presenter.inter.IDakaPreInter;
import com.pda.carmanager.view.inter.IDakaViewInter;

/**
 * Created by Admin on 2017/12/15.
 */

public class DakaPresenter implements IDakaPreInter {
    private Context context;
    private IDakaViewInter iDakaViewInter;
    private IDakaInter iDakaInter;

    public DakaPresenter(Context context, IDakaViewInter iDakaViewInter) {
        this.context = context;
        this.iDakaViewInter = iDakaViewInter;
        iDakaInter=new DakaModel(context,this);
    }

    @Override
    public void postDaka(String address, String dakaType) {
        iDakaInter.postDaka(address,dakaType);
    }

    @Override
    public void dakaSuccess() {
        iDakaViewInter.dakaSuccess();
    }

    @Override
    public void dakaFail(String msg) {
        iDakaViewInter.dakaFail(msg);
    }
}
