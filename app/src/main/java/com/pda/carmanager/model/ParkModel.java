package com.pda.carmanager.model;

import android.content.Context;
import android.widget.Toast;

import com.pda.carmanager.R;
import com.pda.carmanager.bean.MyParkBean;
import com.pda.carmanager.bean.PrintBean;
import com.pda.carmanager.config.AccountConfig;
import com.pda.carmanager.config.UrlConfig;
import com.pda.carmanager.model.inter.IParkInter;
import com.pda.carmanager.presenter.inter.IParkPreInter;
import com.pda.carmanager.util.AMUtil;
import com.pda.carmanager.util.DialogUtil;
import com.pda.carmanager.util.OKHttpUtil;
import com.pda.carmanager.util.PreferenceUtils;
import com.pda.carmanager.util.StringEqualUtil;
import com.pda.carmanager.view.activity.LoginActivity;

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
 * Created by Admin on 2017/12/15.
 */

public class ParkModel implements IParkInter {
    private Context context;
    private IParkPreInter iParkPreInter;
    private String desc;
    private String pages;
    private PrintBean  printBean;

    public ParkModel(Context context, IParkPreInter iParkPreInter) {
        this.context = context;
        this.iParkPreInter = iParkPreInter;
    }

    @Override
    public void postParkList(final String pageIndex, String pagesortfield, final List<MyParkBean> parkBeans) {

        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> e) throws Exception {
                if (OKHttpUtil.isConllection(context)) {
                    parkBeans.clear();
                    String[] key = new String[]{"departmentid", "pageindex", "pagerows"};
                    Map map = new HashMap();
                    map.put("pageindex", pageIndex);
                    map.put("pagerows", "10");
                    map.put("departmentid", PreferenceUtils.getInstance(context).getString(AccountConfig.Departmentid));
                    String Http = OKHttpUtil.GetMessage(context, UrlConfig.ParkPost, key, map);
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
                                    MyParkBean myParkBean = new MyParkBean();
                                    JSONObject temp = (JSONObject) jsonArray.get(i);
                                    myParkBean.setParkNum(temp.getString("cwbh"));
                                    if (!temp.getString("starttime").equals("null")) {
                                        myParkBean.setStartTime(temp.getString("starttime"));
                                        if (!temp.getString("parkingrecordid").equals("null")) {
                                            myParkBean.setParkingrecordid(temp.getString("parkingrecordid"));
                                        }
                                        if (!temp.getString("carnum").equals("null")) {
                                            myParkBean.setCarNum(temp.getString("carnum"));
                                            if (!temp.getString("memberno").equals("null")) {
                                                myParkBean.setParkType("4");
                                            } else {
                                                myParkBean.setParkType("3");
                                            }
                                            if (!temp.getString("cartype").equals("null")) {
                                                if (temp.getString("cartype").equals("1")) {
                                                    myParkBean.setCarType("小车");
                                                } else if (temp.getString("cartype").equals("2")) {
                                                    myParkBean.setCarType("货车");
                                                }
                                            }
                                        } else {
                                            myParkBean.setParkType("2");
                                        }
                                    } else {
                                        myParkBean.setParkType("1");
                                    }
                                    parkBeans.add(myParkBean);

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
                                iParkPreInter.parkSuccess(pages);
                                break;
                            case 1:
                                DialogUtil.dismise();
                                iParkPreInter.parkFail(context.getResources().getString(R.string.httpOut));
                                Toast.makeText(context, context.getResources().getString(R.string.httpOut), Toast.LENGTH_SHORT).show();
                                break;
                            case 2:
                                DialogUtil.dismise();
                                iParkPreInter.parkFail(desc);
                                Toast.makeText(context, desc, Toast.LENGTH_SHORT).show();
                                break;
                            case 3:
                                DialogUtil.dismise();
                                iParkPreInter.parkFail(context.getResources().getString(R.string.httpError));
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
    public void getPrintInfo(final String id) {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> e) throws Exception {
                if (OKHttpUtil.isConllection(context)) {
                    String[] key = new String[]{"id"};
                    Map map = new HashMap();
                    map.put("id", id);
                    String Http = OKHttpUtil.GetMessage(context, UrlConfig.PrintPost, key, map);
                    if (Http != null) {
                        JSONObject jsonObject;
                        try {
                            jsonObject = new JSONObject(Http);

                            String code = jsonObject.getString("code");
                            desc = jsonObject.getString("desc");

                            if (code.equals("0")) {
                                printBean = new PrintBean();
                                JSONObject jsonObject1 = new JSONObject(jsonObject.getString("result"));
                                printBean.setCarNo(jsonObject1.getString("MyCarNo"));
                                printBean.setStartTime(jsonObject1.getString("StartTime"));
                                if (StringEqualUtil.stringNull(jsonObject1.getString("MemberNo"))) {
                                    printBean.setMemberNo(jsonObject1.getString("MemberNo"));
                                }
                                printBean.setUrl(jsonObject1.getString("Url"));
                                List<PrintBean.IsQFModel> isQFModels = new ArrayList<PrintBean.IsQFModel>();
                                if (StringEqualUtil.stringNull(jsonObject1.getString("IsQFModel"))) {
                                    JSONArray jsonArray = new JSONArray(jsonObject1.getString("IsQFModel"));
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        PrintBean.IsQFModel isQFModel = new PrintBean.IsQFModel();
                                        JSONObject temp = (JSONObject) jsonArray.get(i);
                                        isQFModel.setJD(temp.getString("JD"));
                                        isQFModel.setStartTime(temp.getString("StartTime"));
                                        isQFModel.setStopTime(temp.getString("StopTime"));
                                        isQFModel.setCarNO(temp.getString("MyCarNo"));
                                        isQFModel.setMoney(temp.getString("TotalMoney"));
                                        isQFModels.add(isQFModel);
                                    }
                                }
                                if (isQFModels.size() > 0) {
                                    printBean.setIsQFModels(isQFModels);
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
                        iParkPreInter.getPrintSuccess(printBean);
                        break;
                    case 1:
                        DialogUtil.dismise();
                        iParkPreInter.parkFail(context.getResources().getString(R.string.httpOut));
                        Toast.makeText(context, context.getResources().getString(R.string.httpOut), Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        DialogUtil.dismise();
                        iParkPreInter.parkFail(desc);

                        Toast.makeText(context, desc, Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        DialogUtil.dismise();
                        iParkPreInter.parkFail(context.getResources().getString(R.string.httpError));
                        Toast.makeText(context, context.getResources().getString(R.string.httpError), Toast.LENGTH_SHORT).show();
                        break;
                    case 4:
                        DialogUtil.dismise();
                        iParkPreInter.parkFail(context.getResources().getString(R.string.httpNo));
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
