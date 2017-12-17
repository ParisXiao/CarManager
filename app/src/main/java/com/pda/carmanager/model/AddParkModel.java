package com.pda.carmanager.model;

import android.content.Context;
import android.widget.Toast;

import com.pda.carmanager.R;
import com.pda.carmanager.bean.SweetBean;
import com.pda.carmanager.bean.SweetDuanBean;
import com.pda.carmanager.config.AccountConfig;
import com.pda.carmanager.config.UrlConfig;
import com.pda.carmanager.model.inter.AddParkInter;
import com.pda.carmanager.presenter.inter.IAddParkPreInter;
import com.pda.carmanager.util.DialogUtil;
import com.pda.carmanager.util.OKHttpUtil;
import com.pda.carmanager.util.PreferenceUtils;
import com.pda.carmanager.view.inter.IAddParkViewInter;

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
 * Created by Administrator on 2017/12/15 0015.
 */

public class AddParkModel implements AddParkInter {
    private Context context;
    private IAddParkPreInter iAddParkPreInter;
    private String decs;

    public AddParkModel(Context context,IAddParkPreInter iAddParkPreInter) {
        this.context = context;
        this.iAddParkPreInter = iAddParkPreInter;
    }

    @Override
    public void getSweetData(final List<SweetBean> sweetBeens) {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> e) throws Exception {
                if (OKHttpUtil.isConllection(context)){
                String[] key = new String[]{"companyid"};
                Map map = new HashMap();
                map.put("companyid", PreferenceUtils.getInstance(context).getString(AccountConfig.Organizeid));
                String Http = OKHttpUtil.GetMessage(context, UrlConfig.SweetPost, key, map);
                if (Http != null) {
                    JSONObject jsonObject;
                    try {
                        jsonObject = new JSONObject(Http);

                        String code = jsonObject.getString("code");
                        decs = jsonObject.getString("desc");

                        if (code.equals("0")) {
                            JSONArray jsonArray=new JSONArray(jsonObject.getString("result"));
                            for (int i = 0; i < jsonArray.length(); i++) {
                                SweetBean sweetBean=new SweetBean();

                                List<SweetDuanBean> sweetDuanBeens=new ArrayList<SweetDuanBean>();
                                JSONObject temp = (JSONObject) jsonArray.get(i);
                                sweetBean.setId(temp.getString("DepartmentId"));
                                sweetBean.setName(temp.getString("FullName"));
                                JSONArray jsonArray1=new JSONArray(temp.getString("items"));
                                for (int i1 = 0; i1 < jsonArray1.length(); i1++) {
                                    SweetDuanBean sweetDuanBean=new SweetDuanBean();
                                    JSONObject temp2 = (JSONObject) jsonArray1.get(i1);
                                    sweetDuanBean.setId(temp2.getString("DepartmentId"));
                                    sweetDuanBean.setName(temp2.getString("FullName"));
                                    sweetDuanBeens.add(sweetDuanBean);
                                }
                                sweetBean.setSweetDuanBean(sweetDuanBeens);
                                sweetBeens.add(sweetBean);
                            }
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
                        iAddParkPreInter.getSuccesss();
                        break;
                    case 1:
                        DialogUtil.dismise();
                        iAddParkPreInter.getFail(context.getResources().getString(R.string.httpOut));
                        Toast.makeText(context, context.getResources().getString(R.string.httpOut), Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        DialogUtil.dismise();
                        iAddParkPreInter.getFail(decs);

                        Toast.makeText(context, decs, Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        DialogUtil.dismise();
                        iAddParkPreInter.getFail(context.getResources().getString(R.string.httpError));
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
    public void getSweetDuanData(final String jdid, final List<SweetDuanBean> duanBeanList) {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> e) throws Exception {
                String[] key = new String[]{"jdid"};
                Map map = new HashMap();
                map.put("jdid", jdid);
                String Http = OKHttpUtil.GetMessage(context, UrlConfig.SweetDuanPost, key, map);
                if (Http != null) {
                    JSONObject jsonObject;
                    try {
                        jsonObject = new JSONObject(Http);

                        String code = jsonObject.getString("code");
                        decs = jsonObject.getString("desc");

                        if (code.equals("0")) {
                            JSONArray jsonArray=new JSONArray(jsonObject.getString("result"));
                            for (int i = 0; i < jsonArray.length(); i++) {
                                SweetDuanBean sweetDuanBean=new SweetDuanBean();
                                JSONObject temp = (JSONObject) jsonArray.get(i);
                                sweetDuanBean.setId(temp.getString("DepartmentId"));
                                sweetDuanBean.setName(temp.getString("FullName"));
                                duanBeanList.add(sweetDuanBean);
                            }
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
                        iAddParkPreInter.getDuanSuccesss();
                        break;
                    case 1:
                        DialogUtil.dismise();
                        iAddParkPreInter.getDuanFail(context.getResources().getString(R.string.httpOut));
                        Toast.makeText(context, context.getResources().getString(R.string.httpOut), Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        DialogUtil.dismise();
                        iAddParkPreInter.getDuanFail(decs);

                        Toast.makeText(context, decs, Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        DialogUtil.dismise();
                        iAddParkPreInter.getDuanFail(context.getResources().getString(R.string.httpError));
                        Toast.makeText(context, context.getResources().getString(R.string.httpError), Toast.LENGTH_SHORT).show();
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
    public void addPark(final String jd, final String jdd, final String dcbh, final String cwbh) {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> e) throws Exception {
                String[] key = new String[]{"jd","jdd","dcbh","cwbh"};
                Map map = new HashMap();
                map.put("jd", jd);
                map.put("jdd", jdd);
                map.put("dcbh", dcbh);
                map.put("cwbh", cwbh);
                String Http = OKHttpUtil.GetMessage(context, UrlConfig.AddParkPost, key, map);
                if (Http != null) {
                    JSONObject jsonObject;
                    try {
                        jsonObject = new JSONObject(Http);

                        String code = jsonObject.getString("code");
                        decs = jsonObject.getString("desc");

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
                        iAddParkPreInter.addSuccess();
                        Toast.makeText(context,"新增车位成功", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        DialogUtil.dismise();
                        iAddParkPreInter.addFail(context.getResources().getString(R.string.httpOut));
                        Toast.makeText(context, context.getResources().getString(R.string.httpOut), Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        DialogUtil.dismise();
                        iAddParkPreInter.addFail(decs);

                        Toast.makeText(context, decs, Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        DialogUtil.dismise();
                        iAddParkPreInter.addFail(context.getResources().getString(R.string.httpError));
                        Toast.makeText(context, context.getResources().getString(R.string.httpError), Toast.LENGTH_SHORT).show();
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
