package com.pda.carmanager.model;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.pda.carmanager.R;
import com.pda.carmanager.config.AccountConfig;
import com.pda.carmanager.config.UrlConfig;
import com.pda.carmanager.model.inter.ISplashInter;
import com.pda.carmanager.presenter.inter.ISplashPreInter;
import com.pda.carmanager.util.DialogUtil;
import com.pda.carmanager.util.MD5util;
import com.pda.carmanager.util.OKHttpUtil;
import com.pda.carmanager.util.PreferenceUtils;

import org.json.JSONException;
import org.json.JSONObject;

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
 * Created by Admin on 2017/12/19.
 */

public class SplashModel implements ISplashInter {
    private Context context;
    private ISplashPreInter iSplashPreInter;
    private String decs;

    public SplashModel(Context context, ISplashPreInter iSplashPreInter) {
        this.context = context;
        this.iSplashPreInter = iSplashPreInter;
    }

    @Override
    public void updata() {

    }

    @Override
    public void splash() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> e) throws Exception {
                if (OKHttpUtil.isConllection(context)) {

                    if (!TextUtils.isEmpty(PreferenceUtils.getInstance(context).getString(AccountConfig.AccountId)) && PreferenceUtils.getInstance(context).getBoolean(AccountConfig.IsLogin, false)) {

                        String[] key = new String[]{"username", "password", "companycode", "platform"};
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("username", PreferenceUtils.getInstance(context).getString(AccountConfig.AccountId));
                        String p = MD5util.getMD5Str(PreferenceUtils.getInstance(context).getString(AccountConfig.AccountPassword));
                        map.put("password", p);
                        map.put("companycode", PreferenceUtils.getInstance(context).getString(AccountConfig.CommenyCode));
                        map.put("platform", "PDA");
                        String Http = OKHttpUtil.GetLoginMessage(context, UrlConfig.LoginPost, key, map);
                        if (Http != null) {
                            JSONObject jsonObject;
                            try {
                                jsonObject = new JSONObject(Http);
                                String code = jsonObject.getString("code");
                                decs = jsonObject.getString("desc");
                                if (code.equals("0")) {
                                    JSONObject jsonObject2 = new JSONObject(jsonObject.getString("result"));
                                    String token = jsonObject2.getString("token");
                                    String userid = jsonObject2.getString("userid");
                                    String account = jsonObject2.getString("account");
                                    String realname = jsonObject2.getString("realname");
                                    String departmentid = jsonObject2.getString("departmentid");
                                    String departmentname = jsonObject2.getString("departmentname");
                                    String organizeid = jsonObject2.getString("organizeid");
                                    String organizename = jsonObject2.getString("organizename");
                                    String jdid = jsonObject2.getString("jdid");
                                    String jdname = jsonObject2.getString("jdname");
                                    Log.d("LoginJson", "userid:" + userid);
                                    Log.d("LoginJson", "account:" + account);
                                    Log.d("LoginJson", "realname:" + realname);
                                    Log.d("LoginJson", "departmentid:" + departmentid);
                                    Log.d("LoginJson", "departmentname:" + departmentname);
                                    Log.d("LoginJson", "organizeid:" + organizeid);
                                    Log.d("LoginJson", "organizename:" + organizename);
                                    PreferenceUtils.getInstance(context).saveString(AccountConfig.UserId, userid);
                                    PreferenceUtils.getInstance(context).saveString(AccountConfig.AccountId, account);
                                    PreferenceUtils.getInstance(context).saveString(AccountConfig.Platform, "PDA");
                                    PreferenceUtils.getInstance(context).saveString(AccountConfig.Token, token);
                                    PreferenceUtils.getInstance(context).saveString(AccountConfig.Realname, realname);
                                    PreferenceUtils.getInstance(context).saveString(AccountConfig.Organizeid, organizeid);
                                    PreferenceUtils.getInstance(context).saveString(AccountConfig.Organizename, organizename);
                                    PreferenceUtils.getInstance(context).saveString(AccountConfig.Departmentid, departmentid);
                                    PreferenceUtils.getInstance(context).saveString(AccountConfig.Departmentname, departmentname);
                                    PreferenceUtils.getInstance(context).saveString(AccountConfig.Jdid, jdid);
                                    PreferenceUtils.getInstance(context).saveString(AccountConfig.Jdname, jdname);
                                    PreferenceUtils.getInstance(context).saveBoolean(AccountConfig.IsLogin, true);
                                    e.onNext(0);
                                } else if (code.equals("1")) {
//离线
                                } else {
                                    e.onNext(2);

                                }

                            } catch (JSONException e1) {
                                // TODO Auto-generated catch block
                                e1.printStackTrace();
                            }
                        } else {
                            e.onNext(3);
                        }
                    }else {
                        e.onNext(5);
                    }
                } else {
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
                        iSplashPreInter.splashLoginSuccess();
                        break;
                    case 1:
                        DialogUtil.dismise();
                        break;
                    case 2:
                        DialogUtil.dismise();
                        iSplashPreInter.splashLoginFail(decs);
                        break;
                    case 3:
                        DialogUtil.dismise();
                        iSplashPreInter.splashLoginFail(context.getResources().getString(R.string.httpError));
                        Toast.makeText(context, context.getResources().getString(R.string.httpError), Toast.LENGTH_SHORT).show();
                        break;
                    case 4:
                        DialogUtil.dismise();
                        DialogUtil.showSetMessage(context);
                        break;
                    case 5:
                        iSplashPreInter.splashLoginFail("");
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
