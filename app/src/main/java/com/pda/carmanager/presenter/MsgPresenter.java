package com.pda.carmanager.presenter;

import android.content.Context;

import com.pda.carmanager.bean.MsgBean;
import com.pda.carmanager.model.MsgModel;
import com.pda.carmanager.model.inter.IMsgInter;
import com.pda.carmanager.presenter.inter.IMsgPreInter;
import com.pda.carmanager.view.inter.IMsgViewInter;

import java.util.List;

/**
 * Created by Administrator on 2017/12/21 0021.
 */

public class MsgPresenter implements IMsgPreInter {
    private Context context;
    private IMsgViewInter iMsgViewInter;
    private IMsgInter iMsgInter;

    public MsgPresenter(Context context, IMsgViewInter iMsgViewInter) {
        this.context = context;
        this.iMsgViewInter = iMsgViewInter;
        iMsgInter=new MsgModel(context,this);
    }

    @Override
    public void getMsg(String page, List<MsgBean> msgBeenList) {
        iMsgInter.getMsg(page,msgBeenList);
    }

    @Override
    public void getSuccess(String pages) {
        iMsgViewInter.getSuccess(pages);
    }

    @Override
    public void getFail(String decs) {
    iMsgViewInter.getFail(decs);
    }
}
