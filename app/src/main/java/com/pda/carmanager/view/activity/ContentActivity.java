package com.pda.carmanager.view.activity;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.pda.carmanager.R;
import com.pda.carmanager.base.BaseActivity;
import com.pda.carmanager.config.UrlConfig;
import com.pda.carmanager.util.StringEqualUtil;
import com.pda.carmanager.view.widght.JustifyTextView;

import java.net.URL;

/**
 * Created by Admin on 2017/12/14.
 */

public class ContentActivity extends BaseActivity implements View.OnClickListener {
    private TextView toolbar_mid;
    private ImageButton toolbar_left_btn;
    private WebView webView;
    private ProgressBar mProgressBar;
    private Toolbar toolbar;
    private TextView content_title;
    private JustifyTextView text_content;
    private String url="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        initView();
        initData();
        initWeb();
    }

    private void initData() {
        Bundle bundle = getIntent().getExtras();
        content_title.setText(bundle.getString("Title"));
        url= UrlConfig.NEWHTML_POST+bundle.getString("Id");
        String color = bundle.getString("TitleColor");
        if (StringEqualUtil.stringNull(color)) {
            content_title.setTextColor(Color.parseColor(color));
        }
    }
    private void initWeb() {
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        //			WebSettings webSettings = webView .getSettings();
        //			webSettings.setJavaScriptEnabled(true);  //支持js
        //			webSettings.setUseWideViewPort(false);  //将图片调整到适合webview的大小
        //			webSettings.setSupportZoom(true);  //支持缩放
        //			webSettings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN); //支持内容重新布局
        //			webSettings.supportMultipleWindows();  //多窗口
        //			webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);  //关闭webview中缓存
        //			webSettings.setAllowFileAccess(true);  //设置可以访问文件
        //			webSettings.setNeedInitialFocus(true); //当webview调用requestFocus时为webview设置节点
        //			webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        //			webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        //			webSettings.setLoadsImagesAutomatically(true);  //支持自动加载图片
        //			webSettings.setUseWideViewPort(true);//设置此属性，可任意比例缩放
        //			webSettings.setBuiltInZoomControls(true);
        WebSettings s = webView.getSettings();
        s.setBuiltInZoomControls(false);
        s.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        s.setUseWideViewPort(true);
        s.setCacheMode(WebSettings.LOAD_DEFAULT);
        s.setLoadWithOverviewMode(true);
        s.setPluginState(WebSettings.PluginState.ON);
        s.setSavePassword(true);
        s.setSaveFormData(true);
        s.setJavaScriptEnabled(true);
        s.setGeolocationEnabled(true);
        s.setGeolocationDatabasePath("/data/data/org.itri.html5webview/databases/");
        s.setDomStorageEnabled(true);
        webView.requestFocus();
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_INSET);
//
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    mProgressBar.setVisibility(View.INVISIBLE);
                } else {
                    if (View.INVISIBLE == mProgressBar.getVisibility()) {
                        mProgressBar.setVisibility(View.VISIBLE);
                    }
                    mProgressBar.setProgress(newProgress);
                }
                super.onProgressChanged(view, newProgress);
            }

        });

//        webView.addJavascriptInterface(new Object() {
//            public void clickOnAndroid() {
//                mHandler.post(new Runnable() {
//                    public void run() {
//                        webView.loadUrl("javascript:wave()");
//                    }
//                });
//            }
//        }, "web");
        webView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_UP:
                        if (!v.hasFocus()) {
                            v.requestFocus();
                        }
                        break;
                }
                return false;
            }
        });
        webView.loadUrl(url);
    }

    private void initView() {
        toolbar_mid = (TextView) findViewById(R.id.toolbar_mid);
        toolbar_left_btn = (ImageButton) findViewById(R.id.toolbar_left_btn);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        content_title = (TextView) findViewById(R.id.content_title);
//        text_content = (JustifyTextView) findViewById(R.id.text_content);
        webView= (WebView) findViewById(R.id.webView);
        mProgressBar= (ProgressBar) findViewById(R.id.myProgressBar);
        toolbar_left_btn.setOnClickListener(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar_left_btn.setVisibility(View.VISIBLE);
        toolbar_mid.setText(R.string.content_view);
    }

    //这里面的resource就是fromhtml函数的第一个参数里面的含有的url
    Html.ImageGetter imgGetter = new Html.ImageGetter() {
        public Drawable getDrawable(String source) {
            Log.i("RG", "source---?>>>" + source);
            Drawable drawable = null;
            URL url;
            try {
                url = new URL(source);
                Log.i("RG", "url---?>>>" + url);
                drawable = Drawable.createFromStream(url.openStream(), ""); // 获取网路图片
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight());
            Log.i("RG", "url---?>>>" + url);
            return drawable;
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_left_btn:
                finish();
                break;
        }
    }
}
