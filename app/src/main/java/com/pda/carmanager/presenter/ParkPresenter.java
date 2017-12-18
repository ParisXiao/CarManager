package com.pda.carmanager.presenter;

import android.content.Context;

import com.pda.carmanager.bean.MyParkBean;
import com.pda.carmanager.bean.PrintBean;
import com.pda.carmanager.model.ParkModel;
import com.pda.carmanager.model.inter.IParkInter;
import com.pda.carmanager.presenter.inter.IParkPreInter;
import com.pda.carmanager.view.inter.IParkViewInter;

import java.util.List;

/**
 * Created by Admin on 2017/12/15.
 */

public class ParkPresenter implements IParkPreInter {
    private Context context;
    private IParkViewInter iParkViewInter;
    private IParkInter iParkInter;

    public ParkPresenter(Context context, IParkViewInter iParkViewInter) {
        this.context = context;
        this.iParkViewInter = iParkViewInter;
        iParkInter=new ParkModel(context,this);
    }

    @Override
    public void postParkList(final String pageIndex, String pagesortfield,final List<MyParkBean> parkBeans) {
        iParkInter.postParkList(pageIndex,pagesortfield,parkBeans);

    }

    @Override
    public void getPrintInfo(String id) {
        iParkInter.getPrintInfo(id);
    }

    @Override
    public void parkSuccess(String pages) {
        iParkViewInter.parkSuccess(pages);
    }

    @Override
    public void getPrintSuccess(PrintBean printBean) {
        iParkViewInter.getPrintSuccess(printBean);
    }

    @Override
    public void parkFail(String msg) {
        iParkViewInter.parkFail(msg);
    }
}
