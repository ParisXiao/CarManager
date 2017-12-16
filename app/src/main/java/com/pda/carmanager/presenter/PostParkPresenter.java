package com.pda.carmanager.presenter;

import android.content.Context;

import com.pda.carmanager.model.PostParkModel;
import com.pda.carmanager.model.inter.IPostParkInter;
import com.pda.carmanager.presenter.inter.IPostParkPreInter;
import com.pda.carmanager.view.inter.IPostParkViewInter;

/**
 * Created by Administrator on 2017/12/17 0017.
 */

public class PostParkPresenter implements IPostParkPreInter {
    private Context context;
    private IPostParkViewInter iPostParkViewInter;
    private IPostParkInter iPostParkInter;

    public PostParkPresenter(Context context, IPostParkViewInter iPostParkViewInter) {
        this.context = context;
        this.iPostParkViewInter = iPostParkViewInter;
        iPostParkInter=new PostParkModel(context,this);
    }

    @Override
    public void postPark(String id, String carnum, String carType,String img1,String img2) {
        iPostParkInter.postPark(id,carnum,carType,img1,img2);
    }

    @Override
    public void postSuccess() {
        iPostParkViewInter.postSuccess();
    }

    @Override
    public void postFail(String msg) {
        iPostParkViewInter.postFail(msg);
    }
}
