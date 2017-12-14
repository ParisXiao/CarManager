package com.pda.carmanager.model;

import android.content.Context;

import com.pda.carmanager.config.UrlConfig;
import com.pda.carmanager.model.inter.LoginModelInter;
import com.pda.carmanager.presenter.inter.LoginPreInter;
import com.pda.carmanager.util.MD5util;
import com.pda.carmanager.util.OKHttpUtil;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Admin on 2017/12/13.
 */

public class LoginModel  implements LoginModelInter{
    private Context context;
    private LoginPreInter loginPreInter;

    public LoginModel(Context context, LoginPreInter loginPreInter) {
        this.context = context;
        this.loginPreInter = loginPreInter;
    }

    @Override
    public void getLoginInfo(final String username,final String password, final String companycode ) {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> e) throws Exception {
                String[] key=new String[]{"username","password","companycode","platform"};
                Map<String,String> map=new HashMap<String, String>();
                map.put("username",username);
                String p= MD5util.getMD5Str(password);
                map.put("password",p);
                map.put("platform","PDA");
                OKHttpUtil.GetLoginMessage(context, UrlConfig.LoginPost,key,map);

            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {


            }

            @Override
            public void onNext(@NonNull Integer integer) {
                switch (integer) {
                    case 0:
                        break;
                    case 1:
                        break;
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }
}
