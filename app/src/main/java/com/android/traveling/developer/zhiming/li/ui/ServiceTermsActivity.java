package com.android.traveling.developer.zhiming.li.ui;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.android.traveling.R;
import com.android.traveling.ui.BackableActivity;
import com.android.traveling.util.StaticClass;

/**
 * 项目名：Traveling
 * 包名：  com.android.traveling.developer.zhiming.li.ui
 * 文件名：ServiceTermsActivity
 * 创建者：HY
 * 创建时间：2018/10/4 11:06
 * 描述：  服务条款
 */

public class ServiceTermsActivity extends BackableActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_terms);
        initView();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initView() {
        WebView webView = findViewById(R.id.terms_view);
        ProgressBar progressBar = findViewById(R.id.progress);
        webView.loadUrl(StaticClass.SERVICE_TERMS_URL);

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressBar.setVisibility(View.VISIBLE);
            }
        });
        WebSettings webSettings =webView.getSettings();
        webSettings.setJavaScriptEnabled(true); //支持js
        webSettings.setLoadsImagesAutomatically(true);  //自动加载图片
        webSettings.setSupportZoom(true);   //支持屏幕缩放

    }
}
