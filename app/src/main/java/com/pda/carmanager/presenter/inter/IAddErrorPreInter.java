package com.pda.carmanager.presenter.inter;

import java.util.List;

/**
 * Created by Admin on 2017/12/20.
 */

public interface IAddErrorPreInter {
    void getSuccess();
    void getParkNum( List<String> parks);
    void addError(String mycarno,String imgurl,String addr);
    void addSuccess();
    void addFail(String msg);
}
