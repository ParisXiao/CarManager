package com.pda.carmanager.model;

import android.content.Context;
import android.widget.Toast;

import com.pda.carmanager.R;
import com.pda.carmanager.bean.MsgBean;
import com.pda.carmanager.config.UrlConfig;
import com.pda.carmanager.model.inter.IMsgInter;
import com.pda.carmanager.presenter.inter.IMsgPreInter;
import com.pda.carmanager.util.DialogUtil;
import com.pda.carmanager.util.OKHttpUtil;
import com.pda.carmanager.util.StringEqualUtil;
import com.pda.carmanager.view.activity.PayMessageActivity;

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
 * Created by Administrator on 2017/12/21 0021.
 */

public class MsgModel implements IMsgInter {
    private Context  context;
    private IMsgPreInter iMsgPreInter;
    private String decs;
    private String pages;
    public MsgModel(Context context, IMsgPreInter iMsgPreInter) {
        this.context = context;
        this.iMsgPreInter = iMsgPreInter;
    }

    /**
     * 获取信息列表
     * @param page
     * @param msgBeenList
     */
    @Override
    public void getMsg(final String page, final List<MsgBean> msgBeenList) {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> e) throws Exception {
                if (OKHttpUtil.isConllection(context)) {
                    msgBeenList.clear();
                    String[] key = new String[]{ "pageindex", "pagerows"};
                    Map map = new HashMap();
                    map.put("pageindex", page);
                    map.put("pagerows", "10");
                    String Http = OKHttpUtil.GetMessage(context, UrlConfig.MsgPost, key, map);
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
                                    MsgBean msgBean = new MsgBean();
                                    JSONObject temp = (JSONObject) jsonArray.get(i);
                                    msgBean.setId(temp.getString("newsid"));
                                    msgBean.setMsg_time(temp.getString("releasetime"));
                                    msgBean.setMsg_title(temp.getString("fullhead"));
                                    msgBean.setMsg_content(temp.getString("newscontent"));
                                    msgBean.setMsg_titleColor(temp.getString("fullheadcolor"));
                                    msgBeenList.add(msgBean);
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
                                iMsgPreInter.getSuccess(pages);
                                break;
                            case 1:
                                DialogUtil.dismise();
                                iMsgPreInter.getFail(context.getResources().getString(R.string.httpOut));
                                Toast.makeText(context, context.getResources().getString(R.string.httpOut), Toast.LENGTH_SHORT).show();
                                break;
                            case 2:
                                DialogUtil.dismise();
                                iMsgPreInter.getFail(decs);
                                Toast.makeText(context, decs, Toast.LENGTH_SHORT).show();
                                break;
                            case 3:
                                DialogUtil.dismise();
                                iMsgPreInter.getFail(context.getResources().getString(R.string.httpError));
                                Toast.makeText(context, context.getResources().getString(R.string.httpError), Toast.LENGTH_SHORT).show();
                                break;
                            case 4:
                                DialogUtil.dismise();
                                DialogUtil.showSetMessage(context);
                                PayMessageActivity.flags=false;
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
