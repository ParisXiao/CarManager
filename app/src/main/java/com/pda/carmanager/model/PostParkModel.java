package com.pda.carmanager.model;

import android.content.Context;
import android.widget.Toast;

import com.pda.carmanager.R;
import com.pda.carmanager.bean.PrintBean;
import com.pda.carmanager.config.UrlConfig;
import com.pda.carmanager.model.inter.IPostParkInter;
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

public class PostParkModel implements IPostParkInter {
    private Context context;
    private IPostParkPreInter iPostParkPreInter;
    String decs;
    PrintBean printBean;

    public PostParkModel(Context context, IPostParkPreInter iPostParkPreInter) {
        this.context = context;
        this.iPostParkPreInter = iPostParkPreInter;
    }

    @Override
    public void postPark(final String id, final String carnum, final String carType, final String img1, final String img2) {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> e) throws Exception {
                if (OKHttpUtil.isConllection(context)) {
                    String[] key = new String[]{"id", "carnum", "cartype", "img1", "img2"};
                    Map map = new HashMap();
                    map.put("id", id);
                    map.put("carnum", carnum);
                    map.put("cartype", carType);
                    map.put("img1", img1);
                    map.put("img2", img2);

                    String Http = OKHttpUtil.GetMessage(context, UrlConfig.PostParkPost, key, map);
                    if (StringEqualUtil.stringNull(Http)) {
                        JSONObject jsonObject;
                        try {
                            jsonObject = new JSONObject(Http);

                            String code = jsonObject.getString("code");
                            decs = jsonObject.getString("desc");

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
                        iPostParkPreInter.postSuccess(printBean);
                        break;
                    case 1:
                        DialogUtil.dismise();
                        iPostParkPreInter.postFail(context.getResources().getString(R.string.httpOut));
                        Toast.makeText(context, context.getResources().getString(R.string.httpOut), Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        DialogUtil.dismise();
                        iPostParkPreInter.postFail(decs);

                        Toast.makeText(context, decs, Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        DialogUtil.dismise();
                        iPostParkPreInter.postFail(context.getResources().getString(R.string.httpError));
                        Toast.makeText(context, context.getResources().getString(R.string.httpError), Toast.LENGTH_SHORT).show();
                        break;
                    case 4:
                        DialogUtil.dismise();
                        iPostParkPreInter.postFail(context.getResources().getString(R.string.httpNo));
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
