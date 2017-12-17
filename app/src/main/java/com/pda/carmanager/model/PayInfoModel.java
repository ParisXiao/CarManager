package com.pda.carmanager.model;

import android.content.Context;
import android.widget.Toast;

import com.pda.carmanager.R;
import com.pda.carmanager.bean.PayInfoBean;
import com.pda.carmanager.bean.PrintBean;
import com.pda.carmanager.config.UrlConfig;
import com.pda.carmanager.model.inter.IPayInfoInter;
import com.pda.carmanager.presenter.inter.IPayInfoPreInter;
import com.pda.carmanager.presenter.inter.IPostParkPreInter;
import com.pda.carmanager.util.DialogUtil;
import com.pda.carmanager.util.OKHttpUtil;
import com.pda.carmanager.util.StringEqualUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    public PayInfoModel(Context context, IPayInfoPreInter iPayInfoPreInter) {
        this.context = context;
        this.iPayInfoPreInter = iPayInfoPreInter;
    }

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
                    if (Http != null) {
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
                        DialogUtil.dismise();
                        iPayInfoPreInter.getSuccess(payInfoBean);
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
