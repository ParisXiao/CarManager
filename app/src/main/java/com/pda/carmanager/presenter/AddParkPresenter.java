package com.pda.carmanager.presenter;

import android.content.Context;

import com.pda.carmanager.bean.SweetBean;
import com.pda.carmanager.bean.SweetDuanBean;
import com.pda.carmanager.model.AddParkModel;
import com.pda.carmanager.model.inter.AddParkInter;
import com.pda.carmanager.presenter.inter.IAddParkPreInter;
import com.pda.carmanager.view.inter.IAddParkViewInter;

import java.util.List;

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
    public void getSweetData(List<SweetBean> sweetBeens) {
        addParkInter.getSweetData(sweetBeens);
    }

    @Override
    public void getSweetDuanData(String jdid,List<SweetDuanBean> duanBeanList) {
        addParkInter.getSweetDuanData(jdid,duanBeanList);
    }

    @Override
    public void addPark(String jd, String jdd,  String dcbh, String cwbh) {
        addParkInter.addPark(jd,jdd,dcbh,cwbh);
    }

    @Override
    public void getSuccesss() {
        iAddParkViewInter.getSuccesss();
    }

    @Override
    public void getFail(String msg) {
        iAddParkViewInter.getFail(msg);
    }

    @Override
    public void getDuanSuccesss() {
        iAddParkViewInter.getDuanSuccesss();
    }

    @Override
    public void getDuanFail(String msg) {
        iAddParkViewInter.getDuanFail(msg);
    }

    @Override
    public void addSuccess() {
        iAddParkViewInter.addSuccess();
    }

    @Override
    public void addFail(String msg) {
        iAddParkViewInter.addFail(msg);
    }
}
