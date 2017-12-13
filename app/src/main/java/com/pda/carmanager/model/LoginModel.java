package com.pda.carmanager.model;

import android.content.Context;

import com.pda.carmanager.model.inter.LoginModelInter;
import com.pda.carmanager.presenter.inter.LoginPreInter;

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
    public void getLoginInfo() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> e) throws Exception {

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
