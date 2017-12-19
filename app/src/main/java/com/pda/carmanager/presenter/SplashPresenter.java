package com.pda.carmanager.presenter;

import android.content.Context;

import com.pda.carmanager.model.SplashModel;
import com.pda.carmanager.model.inter.ISplashInter;
import com.pda.carmanager.presenter.inter.ISplashPreInter;
import com.pda.carmanager.view.inter.ISplashViewInter;

import java.io.Serializable;

/**
 * Created by Admin on 2017/12/19.
 */

public class SplashPresenter implements ISplashPreInter {
    private Context   context;
    private ISplashViewInter iSplashViewInter;
    private ISplashInter iSplashInter;

    public SplashPresenter(Context context, ISplashViewInter iSplashViewInter) {
        this.context = context;
        this.iSplashViewInter = iSplashViewInter;
        iSplashInter=new SplashModel(context,this);
    }

    @Override
    public void updata() {
        iSplashInter.updata();

    }

    @Override
    public void splash() {
        iSplashInter.splash();
    }

    @Override
    public void needUpdata(Serializable object) {
        iSplashViewInter.needUpdata(object);
    }

    @Override
    public void notUpdata() {
        iSplashViewInter.notUpdata();
    }

    @Override
    public void updataFail(String msg) {
        iSplashViewInter.updataFail(msg);
    }

    @Override
    public void splashLoginSuccess() {
        iSplashViewInter.splashLoginSuccess();

    }

    @Override
    public void splashLoginFail(String msg) {
        iSplashViewInter.splashLoginFail(msg);
    }
}
