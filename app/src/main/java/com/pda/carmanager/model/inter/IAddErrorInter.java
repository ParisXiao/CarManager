package com.pda.carmanager.model.inter;

import java.util.List;

/**
 * Created by Admin on 2017/12/20.
 */

public interface IAddErrorInter {
    void getParkNum( List<String> parks);
    void addError(String mycarno,String imgurl,String addr);
}
