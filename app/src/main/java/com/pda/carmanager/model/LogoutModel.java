package com.pda.carmanager.model;

import android.content.Context;
import android.widget.Switch;
import android.widget.Toast;

import com.pda.carmanager.R;
import com.pda.carmanager.config.AccountConfig;
import com.pda.carmanager.config.UrlConfig;
import com.pda.carmanager.model.inter.ILogoutInter;
import com.pda.carmanager.presenter.inter.ILogoutPreInter;
import com.pda.carmanager.util.DialogUtil;
import com.pda.carmanager.util.OKHttpUtil;
import com.pda.carmanager.util.PreferenceUtils;
import com.pda.carmanager.util.UserInfoClearUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/12/15 0015.
 */

public class LogoutModel implements ILogoutInter {
    private Context context;
    private ILogoutPreInter iLogoutPreInter;
    private String desc;

    public LogoutModel(Context context, ILogoutPreInter iLogoutPreInter) {
        this.context = context;
        this.iLogoutPreInter = iLogoutPreInter;
    }

    @Override
    public void logout() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> e) throws Exception {
                if (OKHttpUtil.isConllection(context)) {
                    String[] key = new String[]{};
                    Map map = new HashMap();
                    String Http = OKHttpUtil.GetMessage(context, UrlConfig.LogoutPost, key, map);
                    if (Http != null) {
                        JSONObject jsonObject;
                        try {
                            jsonObject = new JSONObject(Http);
                            String code = jsonObject.getString("code");
                            desc = jsonObject.getString("desc");
                            if (code.equals("0")) {
                                UserInfoClearUtil.ClearUserInfo(context);
                                e.onNext(0);
                            } else if (code.equals("1")) {
                                e.onNext(1);
                            } else {
                                e.onNext(2);
                            }
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                    } else {
                        e.onNext(3);
                    }
                }else {
                    e.onNext(4);
                }
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull Integer integer) {
                switch (integer) {
                    case 0:
                        DialogUtil.dismise();
                        iLogoutPreInter.logoutSuccess();
                        Toast.makeText(context, R.string.success_logout, Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        break;
                    case 2:
                        DialogUtil.dismise();
                        iLogoutPreInter.logoutFails(desc);
                        Toast.makeText(context, desc, Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        DialogUtil.dismise();
                        iLogoutPreInter.logoutFails(context.getResources().getString(R.string.httpError));
                        Toast.makeText(context, context.getResources().getString(R.string.httpError), Toast.LENGTH_SHORT).show();
                        break;
                    case 4:
                        DialogUtil.dismise();
                        DialogUtil.showSetMessage(context);
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
