package com.pda.carmanager.presenter;

import android.content.Context;

import com.pda.carmanager.bean.ErrorBean;
import com.pda.carmanager.model.ErrorNotesModel;
import com.pda.carmanager.model.inter.IErrorNotesInter;
import com.pda.carmanager.presenter.inter.IErrorNotesPreInter;
import com.pda.carmanager.view.inter.IErrorNotesViewInter;

import java.util.List;

/**
 * Created by Admin on 2017/12/20.
 */

public class ErrorNotesPresenter implements IErrorNotesPreInter {
    private Context context;
    private IErrorNotesViewInter iErrorNotesViewInter;
    private IErrorNotesInter iErrorNotesInter;

    public ErrorNotesPresenter(Context context, IErrorNotesViewInter iErrorNotesViewInter) {
        this.context = context;
        this.iErrorNotesViewInter = iErrorNotesViewInter;
        iErrorNotesInter=new ErrorNotesModel(context,this);
    }

    @Override
    public void getError(String pageIndex, String pagesortfield, List<ErrorBean> errorBeanList) {
        iErrorNotesInter.getError(pageIndex, pagesortfield, errorBeanList);
    }

    @Override
    public void getSuccess(String pages) {
        iErrorNotesViewInter.getSuccess(pages);
    }

    @Override
    public void getFail(String msg) {
        iErrorNotesViewInter.getFail(msg);

    }
}
