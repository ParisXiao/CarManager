package com.pda.carmanager.util;

/**
 * Created by Administrator on 2017/12/17 0017.
 */

public class StringEqualUtil {
    public static boolean stringNull(String s){
        if (s==null){
           return false;
        }else {
            if (s.equals("")||s.equals("null")){
                return false;
            }else {
                return true;
            }
        }

    }
}
