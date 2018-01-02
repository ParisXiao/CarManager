package com.pda.carmanager.model;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.pda.carmanager.R;
import com.pda.carmanager.config.AccountConfig;
import com.pda.carmanager.config.UrlConfig;
import com.pda.carmanager.model.inter.LoginModelInter;
import com.pda.carmanager.presenter.inter.LoginPreInter;
import com.pda.carmanager.util.DialogUtil;
import com.pda.carmanager.util.MD5util;
import com.pda.carmanager.util.OKHttpUtil;
import com.pda.carmanager.util.PreferenceUtils;
import com.pda.carmanager.util.StringEqualUtil;
import com.pda.carmanager.view.activity.LoginActivity;

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
 * Created by Admin on 2017/12/13.
 */

public class LoginModel implements LoginModelInter {
    private Context context;
    private LoginPreInter loginPreInter;
    private String desc;

    public LoginModel(Context context, LoginPreInter loginPreInter) {
        this.context = context;
        this.loginPreInter = loginPreInter;
    }

    /**
     * 提交登录信息
     * @param username
     * @param password
     * @param companycode
     */
    @Override
    public void getLoginInfo(final String username, final String password, final String companycode) {

        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> e) throws Exception {
                if (OKHttpUtil.isConllection(context)) {
                    String[] key = new String[]{"username", "password", "companycode", "platform"};
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("username", username);
                    String p = MD5util.getMD5Str(password);
                    map.put("password", p);
                    map.put("companycode", companycode);
                    map.put("platform", "PDA");
                    String Http = OKHttpUtil.GetLoginMessage(context, UrlConfig.LoginPost, key, map);
                    if (StringEqualUtil.stringNull(Http)) {
                        JSONObject jsonObject;
                        try {
                            jsonObject = new JSONObject(Http);
                            String code = jsonObject.getString("code");
                            desc = jsonObject.getString("desc");
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
                                PreferenceUtils.getInstance(context).saveString(AccountConfig.AccountPassword, password);
                                PreferenceUtils.getInstance(context).saveString(AccountConfig.Platform, "PDA");
                                PreferenceUtils.getInstance(context).saveString(AccountConfig.Token, token);
                                PreferenceUtils.getInstance(context).saveString(AccountConfig.Realname, realname);
                                PreferenceUtils.getInstance(context).saveString(AccountConfig.CommenyCode, companycode);
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
                        loginPreInter.loginSuccess();

                        Toast.makeText(context, R.string.success_login, Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        DialogUtil.dismise();
                        Toast.makeText(context, desc, Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        DialogUtil.dismise();
                        loginPreInter.loginFail(desc);
                        Toast.makeText(context, desc, Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        DialogUtil.dismise();
                        loginPreInter.loginFail(context.getResources().getString(R.string.httpError));
                        Toast.makeText(context, context.getResources().getString(R.string.httpError), Toast.LENGTH_SHORT).show();
                        break;
                    case 4:
                        DialogUtil.dismise();
                        DialogUtil.showSetMessage(context);
                        LoginActivity.flag=false;
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
