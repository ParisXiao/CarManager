package com.pda.carmanager.model;

import android.content.Context;
import android.widget.Toast;

import com.pda.carmanager.R;
import com.pda.carmanager.config.UrlConfig;
import com.pda.carmanager.model.inter.IPostParkInter;
import com.pda.carmanager.presenter.inter.IPostParkPreInter;
import com.pda.carmanager.util.DialogUtil;
import com.pda.carmanager.util.OKHttpUtil;

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

public class PostParkModel implements IPostParkInter {
    private Context context;
    private IPostParkPreInter iPostParkPreInter;
    String decs;

    public PostParkModel(Context context, IPostParkPreInter iPostParkPreInter) {
        this.context = context;
        this.iPostParkPreInter = iPostParkPreInter;
    }

    @Override
    public void postPark(final String id, final String carnum, final String carType, final String img1, final String img2) {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> e) throws Exception {
                String[] key = new String[]{"id", "carnum","cartype","img1","img2"};

                Map map = new HashMap();
                map.put("id",id);
                map.put("carnum",carnum);
                map.put("cartype",carType);
                map.put("img1",img1);
                map.put("img2",img2);

                String Http = OKHttpUtil.GetMessage(context, UrlConfig.PostParkPost, key, map);
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
                        iPostParkPreInter.postSuccess();
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
