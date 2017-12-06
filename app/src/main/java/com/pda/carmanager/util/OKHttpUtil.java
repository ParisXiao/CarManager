package com.pda.carmanager.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.google.gson.Gson;
import com.pda.carmanager.config.UrlConfig;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONObject;

import java.util.Map;
import java.util.concurrent.TimeUnit;



/**
 * Created by Admin on 2017/12/6.
 */

public class OKHttpUtil {
    private static final String TAG = OKHttpUtil.class.getSimpleName();

    /**
     * 查看返回数据结构
     *
     * @param context 上下文
     * @param key     参数 key集合
     * @param vally   参数key对应数据
     */
    public static void GetMessage(Context context,String[] key, Map<String, String> vally) {
        try {
            JSONObject mJson = new JSONObject();
            for (String s : key) {
                mJson.put(s, vally.get(s));
            }
            String Data = mJson.toString();
            Log.d(TAG, "Data : " + Data);
            OkHttpClient client = new OkHttpClient();
            client.setConnectTimeout(10, TimeUnit.SECONDS);
            client.setWriteTimeout(10, TimeUnit.SECONDS);
            client.setReadTimeout(30, TimeUnit.SECONDS);
            try {
                RequestBody body = new FormEncodingBuilder()
                        .add("useid","")
                        .add("token","")
                        .add("platform", "pda")
                        .add("Data", Data)
                        .build();
                Request request = new Request.Builder()
                        .addHeader("Access-Control-Allow-Origin","*")
                        .url(UrlConfig.HttpUrl)
                        .post(body)
                        .build();
                Response response = client.newCall(request).execute();
                Log.d(TAG, "response:" + response);
                if (response.isSuccessful()) {
                    Log.d(TAG, response.body().string());
                }
            } catch (Exception e) {
                Log.d(TAG, e.toString());
            }
        } catch (Exception e) {

        }
    }
    /**
     * 短信验证接口
     *
     */
    public static void GetCodeMessage() {
        try {
            JSONObject mJson = new JSONObject();
            String Data = mJson.toString();
            Log.d(TAG, "Data : " + Data);
            OkHttpClient client = new OkHttpClient();
            client.setConnectTimeout(10, TimeUnit.SECONDS);
            client.setWriteTimeout(10, TimeUnit.SECONDS);
            client.setReadTimeout(30, TimeUnit.SECONDS);
            try {
                RequestBody body = new FormEncodingBuilder()
                        .add("codeType","找回密码")
                        .add("phone","17723166546")
                        .add("message1", "张三")
                        .add("message2", "1234")
                        .build();
                Request request = new Request.Builder()
                        .addHeader("Content-Type","Content-Type")
                        .url(UrlConfig.SMSUrl)
                        .post(body)
                        .build();
                Response response = client.newCall(request).execute();
                Log.d(TAG, "response:" + response);
                if (response.isSuccessful()) {
                    Log.d(TAG, response.body().string());
                }
            } catch (Exception e) {
                Log.d(TAG, e.toString());
            }
        } catch (Exception e) {

        }
    }

//    /**
//     * 解析数据返回实体类
//     *
//     * @param context 上下文
//     * @param clase   实体class
//     */
//    public static <T> T HttpPostEntity(Context context, String[] key, Map<String, String> vally, Class clase) {
//        try {
//            JSONObject mJsonData = new JSONObject();
//            for (String s : key) {
//                mJsonData.put(s, vally.get(s));
//            }
//            String Data = mJsonData.toString();
//            Log.i(TAG, "Data : " + Data);
//
//            OkHttpClient client = new OkHttpClient();
//            try {
//                RequestBody formBody = new FormBody.Builder()
//                        .add("Token", "Token")
//                        .add("Data", Data)
//                        .build();
//                Request request = new Request.Builder()
//                        .url("hh")
//                        .post(formBody)
//                        .build();
//
//                Response response = client.newCall(request).execute();
//                if (response.isSuccessful()) {
//                    String string = response.body().string();
//                    Log.d(TAG, string);
//                    T requset = (T) new Gson().fromJson(string, clase);
//                    return requset;
//                }
//            } catch (Exception e) {
//                Log.d(TAG, e.toString());
//            }
//        } catch (Exception e) {
//            Log.d(TAG, e.toString());
//        }
//
//        return null;
//    }

    /**
     * 判断网络状态
     * @param context
     * @return
     */
    public static boolean isConllection(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }
}
