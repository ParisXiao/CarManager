package com.pda.carmanager.base;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.posapi.PosApi;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;

/**
 * 加载网络图片时，需要对ImageLoader进行全局配置
 *
 */

public class BaseApplication extends Application {
    private String mCurDev = "";

    static BaseApplication instance = null;
    //PosSDK mSDK = null;
    PosApi mPosApi = null;
    public boolean isPos=true;
    public BaseApplication(){
        super.onCreate();
        instance = this;
    }
    public static  BaseApplication getInstance(){
        if(instance==null){
            instance =new BaseApplication();
        }
        return instance;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        initImagloader(getApplicationContext());
        try {
            mPosApi = PosApi.getInstance(this);
            if (Build.MODEL.equalsIgnoreCase("3508")||Build.MODEL.equalsIgnoreCase("403")) {
                mPosApi.initPosDev("ima35s09");
                setCurDevice("ima35s09");
            } else if(Build.MODEL.equalsIgnoreCase("5501")){
                mPosApi.initPosDev("ima35s12");
                setCurDevice("ima35s12");
            }else{
                mPosApi.initPosDev(PosApi.PRODUCT_MODEL_IMA80M01);
                setCurDevice(PosApi.PRODUCT_MODEL_IMA80M01);
            }
        }catch (UnsatisfiedLinkError e){
            e.printStackTrace();
            isPos=false;
        }



    }

    private void initImagloader(Context context) {
        File cacheDir = StorageUtils.getOwnCacheDirectory(context,
                "photoview/Cache");// 获取到缓存的目录地址
        // 创建配置ImageLoader(所有的选项都是可选的,只使用那些你真的想定制)，这个可以设定在APPLACATION里面，设置为全局的配置参数
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                context)
                // 线程池内加载的数量
                .threadPoolSize(3).threadPriority(Thread.NORM_PRIORITY - 2)
                .memoryCache(new WeakMemoryCache())
                .denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                // 将保存的时候的URI名称用MD5 加密
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .discCache(new UnlimitedDiscCache(cacheDir))// 自定义缓存路径
                // .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                .writeDebugLogs() // Remove for release app
                .build();
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);// 全局初始化此配置
    }
    public String getCurDevice() {
        return mCurDev;
    }

    public void setCurDevice(String mCurDev) {
        this.mCurDev = mCurDev;
    }

    public PosApi getPosApi(){
        return mPosApi;
    }
    public boolean isPosApi(){
        return isPos;
    }

}
