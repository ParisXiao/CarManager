package com.pda.carmanager.model;

import android.content.Context;
import android.widget.Toast;

import com.pda.carmanager.R;
import com.pda.carmanager.bean.PayInfoBean;
import com.pda.carmanager.config.UrlConfig;
import com.pda.carmanager.model.inter.IPayInfoInter;
import com.pda.carmanager.presenter.inter.IPayInfoPreInter;
import com.pda.carmanager.util.DialogUtil;
import com.pda.carmanager.util.IPUtil;
import com.pda.carmanager.util.OKHttpUtil;
import com.pda.carmanager.util.StringEqualUtil;

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
 * Created by Administrator on 2017/12/17 0017.
 */

public class PayInfoModel implements IPayInfoInter {
    private Context context;
    private IPayInfoPreInter iPayInfoPreInter;
    private String decs;
    private PayInfoBean payInfoBean;
    private String url;

    public PayInfoModel(Context context, IPayInfoPreInter iPayInfoPreInter) {
        this.context = context;
        this.iPayInfoPreInter = iPayInfoPreInter;
    }

    /**
     * 根据停车id查询收费信息
     * @param id
     */
    @Override
    public void getPayInfo(final String id) {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> e) throws Exception {
                if (OKHttpUtil.isConllection(context)) {
                    String[] key = new String[]{"parkingrecordid"};
                    Map map = new HashMap();
                    map.put("parkingrecordid", id);
                    String Http = OKHttpUtil.GetMessage(context, UrlConfig.PayInfoPost, key, map);
                    if (StringEqualUtil.stringNull(Http)) {
                        JSONObject jsonObject;
                        try {
                            jsonObject = new JSONObject(Http);

                            String code = jsonObject.getString("code");
                            decs = jsonObject.getString("desc");

                            if (code.equals("0")) {
                                payInfoBean = new PayInfoBean();
                                JSONObject jsonObject1 = new JSONObject(jsonObject.getString("result"));
                                payInfoBean.setTotalMoney(jsonObject1.getString("TotalMoney"));
                                payInfoBean.setCurMoney(jsonObject1.getString("CurMoney"));
                                payInfoBean.setQFMoney(jsonObject1.getString("QFMoney"));
                                payInfoBean.setYHMoney(jsonObject1.getString("YHMoney"));
                                payInfoBean.setCarNum(jsonObject1.getString("CarNum"));
                                payInfoBean.setStartTime(jsonObject1.getString("StartTime"));
                                payInfoBean.setStopTime(jsonObject1.getString("StopTime"));
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
                        iPayInfoPreInter.getSuccess(payInfoBean);
                        DialogUtil.dismise();
                        break;
                    case 1:
                        DialogUtil.dismise();
                        iPayInfoPreInter.getFail(context.getResources().getString(R.string.httpOut));
                        Toast.makeText(context, context.getResources().getString(R.string.httpOut), Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        DialogUtil.dismise();
                        iPayInfoPreInter.getFail(decs);

                        Toast.makeText(context, decs, Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        DialogUtil.dismise();
                        iPayInfoPreInter.getFail(context.getResources().getString(R.string.httpError));
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

    /**
     * 支付接口
     * @param id
     * @param type
     * @param auth_code
     */

    @Override
    public void Pay(final String id, final String type, final String auth_code) {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> e) throws Exception {
                if (OKHttpUtil.isConllection(context)) {
                    String[] key = new String[]{"parkingrecordid","paytype","ip","auth_code"};
                    Map map = new HashMap();
                    map.put("parkingrecordid", id);
                    map.put("paytype", type);
                    map.put("ip", IPUtil.getIPAddress(context));
                    map.put("auth_code", auth_code);
                    String Http = OKHttpUtil.GetMessage(context, UrlConfig.PayPost, key, map);
                    if (StringEqualUtil.stringNull(Http)) {
                        JSONObject jsonObject;
                        try {
                            jsonObject = new JSONObject(Http);

                            String code = jsonObject.getString("code");
                            decs = jsonObject.getString("desc");
                            if (code.equals("0")) {
                                JSONObject jsonObject1 = new JSONObject(jsonObject.getString("result"));
                                url=jsonObject1.getString("Url");
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
                        iPayInfoPreInter.paySuccess(url);
                        break;
                    case 1:
                        DialogUtil.dismise();
                        iPayInfoPreInter.getFail(context.getResources().getString(R.string.httpOut));
                        Toast.makeText(context, context.getResources().getString(R.string.httpOut), Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        DialogUtil.dismise();
                        iPayInfoPreInter.getFail(decs);

                        Toast.makeText(context, decs, Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        DialogUtil.dismise();
                        iPayInfoPreInter.getFail(context.getResources().getString(R.string.httpError));
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
