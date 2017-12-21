package com.pda.carmanager.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.google.gson.Gson;
import com.pda.carmanager.config.AccountConfig;
import com.pda.carmanager.config.UrlConfig;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.MediaType;
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
    public static final MediaType JSON = MediaType.parse("application/json");


    /**
     * 查看返回数据结构
     *
     * @param context 上下文
     * @param key     参数 key集合
     * @param vally   参数key对应数据
     */
    public static String GetMessage(Context context,String Url, String[] key, Map<String, String> vally) {
        try {
            JSONObject mJsonData = new JSONObject();
            String json = "{";
            for (String s : key) {
                mJsonData.put(s, vally.get(s));
            }

            String Data = mJsonData.toString();
            Log.d(TAG, "Data : " + Data);
            JSONObject mJson = new JSONObject();
            mJson.put("userid", PreferenceUtils.getInstance(context).getString(AccountConfig.UserId));
            mJson.put("token", PreferenceUtils.getInstance(context).getString(AccountConfig.Token));
            mJson.put("platform", PreferenceUtils.getInstance(context).getString(AccountConfig.Platform));
            mJson.put("data", mJsonData);

            OkHttpClient client = new OkHttpClient();
            client.setConnectTimeout(10, TimeUnit.SECONDS);
            client.setWriteTimeout(10, TimeUnit.SECONDS);
            client.setReadTimeout(30, TimeUnit.SECONDS);
            try {
//                RequestBody body = new FormEncodingBuilder()
////                        .add("useid", " ")
////                        .add("token", " ")
////                        .add("platform", "pda")
//                        .add("data","{'id':123,'name':'zhq'}"
//
//                        )
//                        .build();
                RequestBody body = RequestBody.create(JSON, new String("{'data':'" + Base64Utils.getBase64(mJson.toString())+ "'}"));
                Request request = new Request.Builder()

                        .url(Url)
                        .post(body)
                        .build();
                Response response = client.newCall(request).execute();
                Log.d(TAG, "body:" + new String("{'data':'" + Base64Utils.getBase64(mJson.toString())+ "'}"));
                Log.d(TAG, "response:" + response);

                if (response.isSuccessful()) {
                    String result=Base64Utils.getFromBase64( response.body().string());
                    Log.d(TAG, "result:" + result);
                    return  result;
                }
            } catch (Exception e) {
                Log.d(TAG, e.toString());
            }
        } catch (Exception e) {

        }
        return null;
    }
    public static String GetLoginMessage(Context context,String Url, String[] key, Map<String, String> vally) {
        try {
            JSONObject mJsonData = new JSONObject();
            String json = "{";
            for (String s : key) {
                mJsonData.put(s, vally.get(s));
            }

            String Data = mJsonData.toString();
            Log.d(TAG, "Data : " + Data);
            JSONObject mJson = new JSONObject();

            OkHttpClient client = new OkHttpClient();
            client.setConnectTimeout(10, TimeUnit.SECONDS);
            client.setWriteTimeout(10, TimeUnit.SECONDS);
            client.setReadTimeout(30, TimeUnit.SECONDS);
            try {
//                RequestBody body = new FormEncodingBuilder()
////                        .add("useid", " ")
////                        .add("token", " ")
////                        .add("platform", "pda")
//                        .add("data","{'id':123,'name':'zhq'}"
//
//                        )
//                        .build();
                RequestBody body = RequestBody.create(JSON, new String("{'data':'" + Base64Utils.getBase64(mJsonData.toString()) + "'}"));
                Log.d(TAG, "body:" + Base64Utils.getBase64(mJsonData.toString()));
                Request request = new Request.Builder()

                        .url(Url)
                        .post(body)
                        .build();
                Response response = client.newCall(request).execute();

                Log.d(TAG, "response:" + response);

                if (response.isSuccessful()) {
                    String result=Base64Utils.getFromBase64( response.body().string());
                    Log.d(TAG, "result:" + result);
                    return  result;
                }
            } catch (Exception e) {
                Log.d(TAG, e.toString());
            }
        } catch (Exception e) {

        }
        return null;
    }

    public static String updataPost(String Url) {
        try {




            OkHttpClient client = new OkHttpClient();
            client.setConnectTimeout(10, TimeUnit.SECONDS);
            client.setWriteTimeout(10, TimeUnit.SECONDS);
            client.setReadTimeout(30, TimeUnit.SECONDS);
            try {
                Request request = new Request.Builder()
                        .url(Url)
                        .get()
                        .build();
                Response response = client.newCall(request).execute();

                Log.d(TAG, "response:" + response);

                if (response.isSuccessful()) {
                    String result=response.body().string();
                    Log.d(TAG, "result:" + result);
                    return  result;
                }
            } catch (Exception e) {
                Log.d(TAG, e.toString());
            }
        } catch (Exception e) {

        }
        return null;
    }


    /**
     * 短信验证接口
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
                        .add("codeType", "找回密码")
                        .add("phone", "17723166546")
                        .add("message1", "张三")
                        .add("message2", "1234")
                        .build();
                Request request = new Request.Builder()
                        .addHeader("Content-Type", "Content-Type")
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

    /**
     * 解析数据返回实体类
     *
     * @param context
     * @param Url
     * @param key
     * @param vally
     * @param clase
     * @param <T>
     * @return
     */
    public static <T> T HttpPostEntity(Context context, String Url, String[] key, Map<String, String> vally, Class clase) {
        try {
            JSONObject mJsonData = new JSONObject();
            for (String s : key) {
                mJsonData.put(s, vally.get(s));
            }
            String Data = mJsonData.toString();
            Log.i(TAG, "Data : " + Data);
            JSONObject mJson = new JSONObject();
            mJson.put("useid", PreferenceUtils.getInstance(context).getString(AccountConfig.AccountId));
            mJson.put("token", PreferenceUtils.getInstance(context).getString(AccountConfig.Token));
            mJson.put("platform", PreferenceUtils.getInstance(context).getString(AccountConfig.Platform));
            mJson.put("data", mJsonData);

            OkHttpClient client = new OkHttpClient();
            try {
                RequestBody body = RequestBody.create(JSON, new String("{'data':'" + Base64Utils.getBase64(mJsonData.toString()) + "'}"));
                Request request = new Request.Builder()
                        .url(Url)
                        .post(body)
                        .build();

                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    String string =Base64Utils.getFromBase64( response.body().string());
                    Log.d(TAG, string);
                    T requset = (T) new Gson().fromJson(string, clase);
                    return requset;
                }
            } catch (Exception e) {
                Log.d(TAG, e.toString());
            }
        } catch (Exception e) {
            Log.d(TAG, e.toString());
        }

        return null;
    }
    /**
     * 解析数据返回实体类
     *
     * @param context
     * @param Url
     * @param key
     * @param vally
     * @param clase
     * @param <T>
     * @return
     */
    public static <T> T LoginPostEntity(Context context, String Url, String[] key, Map<String, String> vally, Class clase) {
        try {
            JSONObject mJsonData = new JSONObject();
            for (String s : key) {
                mJsonData.put(s, vally.get(s));
            }
            String Data = mJsonData.toString();
            Log.i(TAG, "Data : " + Data);
            JSONObject mJson = new JSONObject();

            OkHttpClient client = new OkHttpClient();
            try {
                RequestBody body = RequestBody.create(JSON, new String("{'data':'" + Base64Utils.getBase64(mJsonData.toString()) + "'}"));
                Request request = new Request.Builder()
                        .url(Url)
                        .post(body)
                        .build();

                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {

                    String string = Base64Utils.getFromBase64( response.body().string());
                    Log.d(TAG, string);
                    T requset = (T) new Gson().fromJson(string, clase);
                    return requset;

                }
            } catch (Exception e) {
                Log.d(TAG, e.toString());
            }
        } catch (Exception e) {
            Log.d(TAG, e.toString());
        }

        return null;
    }
    /**
     * 判断网络状态
     *
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
