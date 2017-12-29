package com.pda.carmanager.model;

import android.content.Context;
import android.widget.Toast;

import com.pda.carmanager.R;
import com.pda.carmanager.bean.ErrorBean;
import com.pda.carmanager.config.AccountConfig;
import com.pda.carmanager.config.UrlConfig;
import com.pda.carmanager.model.inter.IErrorNotesInter;
import com.pda.carmanager.presenter.inter.IErrorNotesPreInter;
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

public class ErrorNotesModel implements IErrorNotesInter {
    private Context context;
    private IErrorNotesPreInter iErrorNotesPreInter;
    private String decs;
    private String pages;

    public ErrorNotesModel(Context context, IErrorNotesPreInter iErrorNotesPreInter) {
        this.context = context;
        this.iErrorNotesPreInter = iErrorNotesPreInter;
    }

    @Override
    public void getError(final String pageIndex, String pagesortfield, final List<ErrorBean> errorBeanList) {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> e) throws Exception {
                if (OKHttpUtil.isConllection(context)) {
                    errorBeanList.clear();
                    String[] key = new String[]{"jddid", "pageindex", "pagerows"};
                    Map map = new HashMap();
                    map.put("jddid", PreferenceUtils.getInstance(context).getString(AccountConfig.Departmentid));
                    map.put("pageindex", pageIndex);
                    map.put("pagerows", "10");
                    String Http = OKHttpUtil.GetMessage(context, UrlConfig.SelCatchPost, key, map);
                    if (StringEqualUtil.stringNull(Http)) {
                        JSONObject jsonObject;
                        try {
                            jsonObject = new JSONObject(Http);

                            String code = jsonObject.getString("code");
                            decs = jsonObject.getString("desc");
                            if (code.equals("0")) {
                                JSONObject jsonObject1 = new JSONObject(jsonObject.getString("result"));
                                pages = jsonObject1.getString("pages");
                                JSONArray jsonArray = new JSONArray(jsonObject1.getString("items"));
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    ErrorBean errorBean=new ErrorBean();
                                    JSONObject temp=(JSONObject) jsonArray.get(i);
                                    errorBean.setErrorAddress(temp.getString("addr"));
                                    errorBean.setErrorStatus(temp.getString("status"));
                                    errorBean.setErrorTime(temp.getString("statetime"));
                                    errorBeanList.add(errorBean);
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
                                iErrorNotesPreInter.getSuccess(pages);
                                break;
                            case 1:
                                DialogUtil.dismise();
                                iErrorNotesPreInter.getFail(context.getResources().getString(R.string.httpOut));
                                Toast.makeText(context, context.getResources().getString(R.string.httpOut), Toast.LENGTH_SHORT).show();
                                break;
                            case 2:
                                DialogUtil.dismise();
                                iErrorNotesPreInter.getFail(decs);
                                Toast.makeText(context, decs, Toast.LENGTH_SHORT).show();
                                break;
                            case 3:
                                DialogUtil.dismise();
                                iErrorNotesPreInter.getFail(context.getResources().getString(R.string.httpError));
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
