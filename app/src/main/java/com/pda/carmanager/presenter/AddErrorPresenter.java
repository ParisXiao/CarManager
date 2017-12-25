package com.pda.carmanager.presenter;

import android.content.Context;

import com.pda.carmanager.model.AddErrorModel;
import com.pda.carmanager.model.inter.IAddErrorInter;
import com.pda.carmanager.presenter.inter.IAddErrorPreInter;
import com.pda.carmanager.view.inter.IAddErrorViewInter;

import java.util.List;

/**
 * Created by Admin on 2017/12/20.
 */

public class AddErrorPresenter implements IAddErrorPreInter {
    private Context context;
    private IAddErrorViewInter iAddErrorViewInter;
    private IAddErrorInter iAddErrorInter;

    public AddErrorPresenter(Context context, IAddErrorViewInter iAddErrorViewInter) {
        this.context = context;
        this.iAddErrorViewInter = iAddErrorViewInter;
        iAddErrorInter=new AddErrorModel(context,this);
    }

    @Override
    public void getSuccess() {
        iAddErrorViewInter.getSuccess();
    }

    @Override
    public void getParkNum(List<String> parks) {
        iAddErrorInter.getParkNum(parks);
    }

    @Override
    public void addError(String mycarno,String imgurl,String addr) {
        iAddErrorInter.addError(mycarno,imgurl,addr);

    }

    @Override
    public void addSuccess() {
        iAddErrorViewInter.addSuccess();
    }

    @Override
    public void addFail(String msg) {
        iAddErrorViewInter.addFail(msg);
    }
}
