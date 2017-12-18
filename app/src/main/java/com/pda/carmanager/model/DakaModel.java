package com.pda.carmanager.model;

import android.content.Context;
import android.widget.Toast;

import com.pda.carmanager.R;
import com.pda.carmanager.bean.DakaBean;
import com.pda.carmanager.bean.MyParkBean;
import com.pda.carmanager.config.AccountConfig;
import com.pda.carmanager.config.UrlConfig;
import com.pda.carmanager.model.inter.IDakaInter;
import com.pda.carmanager.presenter.inter.IDakaPreInter;
import com.pda.carmanager.util.DialogUtil;
import com.pda.carmanager.util.OKHttpUtil;
import com.pda.carmanager.util.PreferenceUtils;
import com.pda.carmanager.util.StringEqualUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
 * Created by Admin on 2017/12/15.
 */

public class DakaModel implements IDakaInter {
    private Context context;
    private IDakaPreInter iDakaPreInter;
    private String desc;
    private String pages;

    public DakaModel(Context context, IDakaPreInter iDakaPreInter) {
        this.context = context;
        this.iDakaPreInter = iDakaPreInter;
    }

    @Override
    public void postDaka(final String address, final String DakaType) {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> e) throws Exception {
                if (OKHttpUtil.isConllection(context)){
                String[] key = new String[]{"addaddr", "kqtype"};
                Map map = new HashMap();
                map.put("addaddr", address);
                map.put("kqtype", DakaType);
                String Http = OKHttpUtil.GetMessage(context, UrlConfig.DakaPost, key, map);
                if (Http != null) {
                    JSONObject jsonObject;
                    try {
                        jsonObject = new JSONObject(Http);

                        String code = jsonObject.getString("code");
                        desc = jsonObject.getString("desc");

                        if (code.equals("0")) {
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
                }}else {
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
                        iDakaPreInter.dakaSuccess();
                        Toast.makeText(context, "打卡成功", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        DialogUtil.dismise();
                        iDakaPreInter.dakaFail(context.getResources().getString(R.string.httpOut));
                        Toast.makeText(context, context.getResources().getString(R.string.httpOut), Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        DialogUtil.dismise();
                        iDakaPreInter.dakaFail(desc);

                        Toast.makeText(context, desc, Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        DialogUtil.dismise();
                        iDakaPreInter.dakaFail(context.getResources().getString(R.string.httpError));
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

    @Override
    public void getDaka(final String kqdate, final String page, final List<DakaBean> dakaBeanList) {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> e) throws Exception {
                if (OKHttpUtil.isConllection(context)) {
                    String[] key = new String[]{"kqdate", "pageindex", "pagerows"};
                    Map map = new HashMap();

                    map.put("kqdate", kqdate);
                    map.put("pageindex", page);
                    map.put("pagerows", "10");
                    String Http = OKHttpUtil.GetMessage(context, UrlConfig.GetDakaPost, key, map);
                    if (Http != null) {
                        JSONObject jsonObject;
                        try {
                            jsonObject = new JSONObject(Http);

                            String code = jsonObject.getString("code");
                            desc = jsonObject.getString("desc");
                            if (code.equals("0")) {
                                JSONObject jsonObject1 = new JSONObject(jsonObject.getString("result"));
                                pages = jsonObject1.getString("pages");
                                JSONArray jsonArray = new JSONArray(jsonObject1.getString("items"));
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    DakaBean dakaBean=new DakaBean();
                                    JSONObject temp = (JSONObject) jsonArray.get(i);
                                    dakaBean.setDakaAddress(temp.getString("addaddr"));
                                    if (StringEqualUtil.stringNull(temp.getString("addbegtime"))){
                                        dakaBean.setDakaTime(temp.getString("addbegtime"));
                                        dakaBean.setDakaType(1);
                                    }else {
                                        dakaBean.setDakaTime(temp.getString("addendtime"));
                                        dakaBean.setDakaType(2);
                                    }
                                    dakaBeanList.add(dakaBean);
                                }
                                e.onNext(0);
                            } else if (code.equals("1")) {
                                e.onNext(1);
                            } else {
                                e.onNext(2);
                            }
                        } catch (
                                JSONException e1)

                        {
                            e1.printStackTrace();
                        }
                    } else

                    {
                        e.onNext(3);
                    }
                }else {
                    e.onNext(4);
                }
                e.onComplete();
            }
        }).

                subscribeOn(Schedulers.io()).

                observeOn(AndroidSchedulers.mainThread()).

                subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull Integer integer) {
                        switch (integer) {
                            case 0:
                                DialogUtil.dismise();
                                iDakaPreInter.getdakaSuccess(pages);
                                break;
                            case 1:
                                DialogUtil.dismise();
                                iDakaPreInter.dakaFail(context.getResources().getString(R.string.httpOut));
                                Toast.makeText(context, context.getResources().getString(R.string.httpOut), Toast.LENGTH_SHORT).show();
                                break;
                            case 2:
                                DialogUtil.dismise();
                                iDakaPreInter.dakaFail(desc);
                                Toast.makeText(context, desc, Toast.LENGTH_SHORT).show();
                                break;
                            case 3:
                                DialogUtil.dismise();
                                iDakaPreInter.dakaFail(context.getResources().getString(R.string.httpError));
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
