package com.pda.carmanager.util;

import android.content.Context;

import com.pda.carmanager.config.AccountConfig;

/**
 * Created by Administrator on 2017/12/16 0016.
 */

public class UserInfoClearUtil {
    public static void ClearUserInfo(Context context){
        PreferenceUtils.getInstance(context).saveString(AccountConfig.UserId, "");
//        PreferenceUtils.getInstance(context).saveString(AccountConfig.AccountId, "");
        PreferenceUtils.getInstance(context).saveString(AccountConfig.AccountPassword, "");
        PreferenceUtils.getInstance(context).saveString(AccountConfig.Platform, "PDA");
        PreferenceUtils.getInstance(context).saveString(AccountConfig.Token, "");
        PreferenceUtils.getInstance(context).saveString(AccountConfig.Realname, "");
//        PreferenceUtils.getInstance(context).saveString(AccountConfig.CommenyCode, "");
        PreferenceUtils.getInstance(context).saveString(AccountConfig.Organizeid, "");
        PreferenceUtils.getInstance(context).saveString(AccountConfig.Organizename, "");
        PreferenceUtils.getInstance(context).saveString(AccountConfig.Departmentid, "");
        PreferenceUtils.getInstance(context).saveString(AccountConfig.Departmentname, "");
        PreferenceUtils.getInstance(context).saveString(AccountConfig.Jdname, "");
        PreferenceUtils.getInstance(context).saveString(AccountConfig.Jdid, "");
        PreferenceUtils.getInstance(context).saveBoolean(AccountConfig.IsLogin, false);
    }

}
