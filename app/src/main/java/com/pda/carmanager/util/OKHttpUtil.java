package com.pda.carmanager.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Base64;
import android.util.Log;

import com.google.gson.Gson;
import com.pda.carmanager.bean.Test;
import com.pda.carmanager.config.UrlConfig;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONObject;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
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
    public static void GetMessage(Context context, String[] key, Map<String, String> vally) {
        try {
            JSONObject mJson = new JSONObject();
            String json="{";
            for (String s : key) {
                mJson.put(s, vally.get(s));
//                json+="'"+s+"':'"+vally.get(s)+"',";
            }
//            json+="}";

            String Data = mJson.toString();
            Log.d(TAG, "Data : " + Data);
            OkHttpClient client = new OkHttpClient();
            client.setConnectTimeout(10, TimeUnit.SECONDS);
            client.setWriteTimeout(10, TimeUnit.SECONDS);
            client.setReadTimeout(30, TimeUnit.SECONDS);
            try {
//                RequestBody StringBody = RequestBody.create(MediaType.parse("application/json"), "PDA");
                String var="id";
                String val="2334";
                String var2="name";
                String val2="xl";
                String s="{'"+var+"':'"+val+"','"+var2+"':'"+val2+"'}";
                Test test=new Test("123","xiaoli");
//                RequestBody body = new FormEncodingBuilder()
////                        .add("useid", " ")
////                        .add("token", " ")
////                        .add("platform", "pda")
//                        .add("data","{'id':123,'name':'zhq'}"
//
//                        )
//                        .build();
                RequestBody body = RequestBody.create(JSON, new String("{'data':'"+getBase64(mJson.toString())+"'}"));
                Request request = new Request.Builder()

                        .url(UrlConfig.HttpUrl)
                        .post(body)
                        .build();
                Response response = client.newCall(request).execute();
                Log.d(TAG, "body:" + body.toString());
                Log.d(TAG, "response:" + response);

                if (response.isSuccessful()) {
                    Log.d(TAG, "R" + response.body().string());
                }
            } catch (Exception e) {
                Log.d(TAG, e.toString());
            }
        } catch (Exception e) {

        }
    }
    public static String getBase64(String str) {
        String result = "";
        if( str != null) {
            try {
                result = new String(Base64.encode(str.getBytes("utf-8"), Base64.NO_WRAP),"utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
    public static  String SendDataByPost(String urlStr){
        URL url = null;
        String result="";//要返回的结果
        try {
            url=new URL(urlStr);
            HttpURLConnection httpURLConnection= (HttpURLConnection) url.openConnection();

            httpURLConnection.setConnectTimeout(2000);//设置连接超时时间，单位ms
            httpURLConnection.setReadTimeout(2000);//设置读取超时时间，单位ms

            //设置是否向httpURLConnection输出，因为post请求参数要放在http正文内，所以要设置为true
            httpURLConnection.setDoOutput(true);

            //设置是否从httpURLConnection读入，默认是false
            httpURLConnection.setDoInput(true);

            //POST请求不能用缓存，设置为false
            httpURLConnection.setUseCaches(false);

            //传送的内容是可序列化的
            //如果不设置此项，传送序列化对象时，当WEB服务默认的不是这种类型时，会抛出java.io.EOFException错误
            httpURLConnection.setRequestProperty("Content-type","application/json");

            //设置请求方法是POST
            httpURLConnection.setRequestMethod("POST");

            //连接服务器
            httpURLConnection.connect();

            //getOutputStream会隐含调用connect()，所以不用写上述的httpURLConnection.connect()也行。
            //得到httpURLConnection的输出流
            OutputStream os= httpURLConnection.getOutputStream();

            //构建输出流对象，以实现输出序列化的对象
            ObjectOutputStream objOut=new ObjectOutputStream(os);

            //dataPost类是自定义的数据交互对象，只有两个成员变量
            Test data= new Test("123","Xiaoli");

            //向对象输出流写出数据，这些数据将存到内存缓冲区中
            objOut.writeObject(data);

            //刷新对象输出流，将字节全部写入输出流中
            objOut.flush();

            //关闭流对象
            objOut.close();
            os.close();

            //将内存缓冲区中封装好的完整的HTTP请求电文发送到服务端，并获取访问状态
            if(HttpURLConnection.HTTP_OK==httpURLConnection.getResponseCode()){

                //得到httpURLConnection的输入流，这里面包含服务器返回来的java对象
                InputStream in=httpURLConnection.getInputStream();

                //构建对象输入流，使用readObject()方法取出输入流中的java对象
                ObjectInputStream inObj=new ObjectInputStream(in);
                data= (Test) inObj.readObject();

                //取出对象里面的数据
                result=data.getName();

                //输出日志，在控制台可以看到接收到的数据
                Log.w("HTTP",result+"  :by post");

                //关闭创建的流
                in.close();
                inObj.close();
            }else{
                Log.w("HTTP","Connction failed"+httpURLConnection.getResponseCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
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
