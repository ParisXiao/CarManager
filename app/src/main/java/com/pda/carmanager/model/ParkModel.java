package com.pda.carmanager.model;

import android.content.Context;
import android.widget.Toast;

import com.pda.carmanager.R;
import com.pda.carmanager.bean.MyParkBean;
import com.pda.carmanager.config.AccountConfig;
import com.pda.carmanager.config.UrlConfig;
import com.pda.carmanager.model.inter.IParkInter;
import com.pda.carmanager.presenter.inter.IParkPreInter;
import com.pda.carmanager.util.AMUtil;
import com.pda.carmanager.util.DialogUtil;
import com.pda.carmanager.util.OKHttpUtil;
import com.pda.carmanager.util.PreferenceUtils;
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
    private List<MyParkBean> parkBeans;

    public ParkModel(Context context, IParkPreInter iParkPreInter) {
        this.context = context;
        this.iParkPreInter = iParkPreInter;
    }

    @Override
    public void postParkList(final String pageIndex, String pagesortfield) {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> e) throws Exception {
                String[] key = new String[]{"departmentid", "pageindex", "pagerows"};
                Map map = new HashMap();
                map.put("pageindex", pageIndex);
                map.put("pagerows", "10");
//                map.put("departmentid", PreferenceUtils.getInstance(context).getString(AccountConfig.Departmentid));
                map.put("departmentid", "acc5cdb3-e298-4941-9749-e251db8e94fa");
                String Http = OKHttpUtil.GetMessage(context, UrlConfig.ParkPost, key, map);
                if (Http != null) {
                    JSONObject jsonObject;
                    try {
                        jsonObject = new JSONObject(Http);

                        String code = jsonObject.getString("code");
                        desc = jsonObject.getString("desc");
                        if (code.equals("0")) {
                            JSONArray arr = new JSONArray(jsonObject.getString("items"));
                            parkBeans = new ArrayList<MyParkBean>();
                            for (int i = 0; i < arr.length(); i++) {
                                MyParkBean myParkBean = new MyParkBean();

                            }

                    e.onNext(0);
                } else if (code.equals("1")) {
                    e.onNext(1);
                } else {
                    e.onNext(2);
                }
            } catch(
            JSONException e1)

            {
                e1.printStackTrace();
            }
        } else

        {
            e.onNext(3);
        }
        e.onComplete();
    }
}).

        subscribeOn(Schedulers.io()).

        observeOn(AndroidSchedulers.mainThread()).

        subscribe(new Observer<Integer>(){
@Override
public void onSubscribe(@NonNull Disposable d){

        }

@Override
public void onNext(@NonNull Integer integer){
        switch(integer){
        case 0:
        DialogUtil.dismise();
        iParkPreInter.parkSuccess(parkBeans);
        break;
        case 1:
        DialogUtil.dismise();
        iParkPreInter.parkFail(context.getResources().getString(R.string.httpOut));
        Toast.makeText(context,context.getResources().getString(R.string.httpOut),Toast.LENGTH_SHORT).show();
        break;
        case 2:
        DialogUtil.dismise();
        iParkPreInter.parkFail(desc);

        Toast.makeText(context,desc,Toast.LENGTH_SHORT).show();
        break;
        case 3:
        DialogUtil.dismise();
        iParkPreInter.parkFail(context.getResources().getString(R.string.httpError));
        Toast.makeText(context,context.getResources().getString(R.string.httpError),Toast.LENGTH_SHORT).show();
        break;
        }
        }

@Override
public void onError(@NonNull Throwable e){

        }

@Override
public void onComplete(){

        }
        });
        }
        }
