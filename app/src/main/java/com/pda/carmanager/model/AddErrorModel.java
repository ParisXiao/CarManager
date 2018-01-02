package com.pda.carmanager.model;

import android.content.Context;
import android.widget.Toast;

import com.pda.carmanager.R;
import com.pda.carmanager.config.AccountConfig;
import com.pda.carmanager.config.UrlConfig;
import com.pda.carmanager.model.inter.IAddErrorInter;
import com.pda.carmanager.presenter.inter.IAddErrorPreInter;
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
 * Created by Admin on 2017/12/20.
 */

public class AddErrorModel implements IAddErrorInter {
    private Context context;
    private IAddErrorPreInter iAddErrorPreInter;
    private String decs;

    public AddErrorModel(Context context, IAddErrorPreInter iAddErrorPreInter) {
        this.context = context;
        this.iAddErrorPreInter = iAddErrorPreInter;
    }

    /**
     * 获取部门相关车位编号进行模糊查询
     * @param parks
     */
    @Override
    public void getParkNum(final List<String> parks) {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> e) throws Exception {
                if (OKHttpUtil.isConllection(context)) {
                    String[] key = new String[]{"departmentid"};
                    Map map = new HashMap();
                    map.put("departmentid", PreferenceUtils.getInstance(context).getString(AccountConfig.Departmentid));
                    String Http = OKHttpUtil.GetMessage(context, UrlConfig.ParkNumPost, key, map);
                    if (StringEqualUtil.stringNull(Http)) {
                        JSONObject jsonObject;
                        try {
                            jsonObject = new JSONObject(Http);

                            String code = jsonObject.getString("code");
                            decs = jsonObject.getString("desc");
                            JSONArray jsonArray1=new JSONArray(jsonObject.getString("result"));
                            for (int i = 0; i < jsonArray1.length(); i++) {
                                JSONObject temp= (JSONObject) jsonArray1.get(i);
                                parks.add(temp.getString("CWBH"));
                            }
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
                        iAddErrorPreInter.getSuccess();
                        break;
                    case 1:
                        DialogUtil.dismise();
                        iAddErrorPreInter.addFail(context.getResources().getString(R.string.httpOut));
                        Toast.makeText(context, context.getResources().getString(R.string.httpOut), Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        DialogUtil.dismise();
                        iAddErrorPreInter.addFail(decs);

                        Toast.makeText(context, decs, Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        DialogUtil.dismise();
                        iAddErrorPreInter.addFail(context.getResources().getString(R.string.httpError));
                        Toast.makeText(context, context.getResources().getString(R.string.httpError), Toast.LENGTH_SHORT).show();
                        break;
                    case 4:
                        DialogUtil.dismise();
                        DialogUtil.showSetMessage(context);
                        iAddErrorPreInter.addFail(context.getResources().getString(R.string.httpNo));
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
     * 新增申诉
     * @param mycarno
     * @param imgurl
     * @param addr
     */

    @Override
    public void addError(final String mycarno, final String imgurl, final String addr) {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> e) throws Exception {
                if (OKHttpUtil.isConllection(context)) {
                    String[] key = new String[]{"id", "mycarno", "imgurl","addr","jddid"};
                    Map map = new HashMap();
                    map.put("id", "");
                    map.put("mycarno", mycarno);
                    map.put("imgurl", imgurl);
                    map.put("addr", addr);
                    map.put("jddid", PreferenceUtils.getInstance(context).getString(AccountConfig.Departmentid));

                    String Http = OKHttpUtil.GetMessage(context, UrlConfig.DualCatchPost, key, map);
                    if (StringEqualUtil.stringNull(Http)) {
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
                        iAddErrorPreInter.addSuccess();
                        Toast.makeText(context, "申诉提交成功", Toast.LENGTH_SHORT).show();

                        break;
                    case 1:
                        DialogUtil.dismise();
                        iAddErrorPreInter.addFail(context.getResources().getString(R.string.httpOut));
                        Toast.makeText(context, context.getResources().getString(R.string.httpOut), Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        DialogUtil.dismise();
                        iAddErrorPreInter.addFail(decs);

                        Toast.makeText(context, decs, Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        DialogUtil.dismise();
                        iAddErrorPreInter.addFail(context.getResources().getString(R.string.httpError));
                        Toast.makeText(context, context.getResources().getString(R.string.httpError), Toast.LENGTH_SHORT).show();
                        break;
                    case 4:
                        DialogUtil.dismise();
                        DialogUtil.showSetMessage(context);
                        iAddErrorPreInter.addFail(context.getResources().getString(R.string.httpNo));
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
