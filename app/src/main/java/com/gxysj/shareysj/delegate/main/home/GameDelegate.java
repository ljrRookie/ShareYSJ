package com.gxysj.shareysj.delegate.main.home;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.gxysj.shareysj.R;
import com.gxysj.shareysj.delegate.base.YSJDelegate;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by 林佳荣 on 2018/2/8.
 * Github：https://github.com/ljrRookie
 * Function ：
 */

public class GameDelegate extends YSJDelegate {
    @BindView(R.id.web)
    WebView mWebview;
    Unbinder unbinder;

    @Override
    public Object setLayout() {
        return R.layout.delegate_home_game;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        initWebView();
        mWebview.loadUrl("file:///android_asset/index.html");
    }
    @SuppressLint("AddJavascriptInterface")
    private void initWebView() {
        WebSettings settings = mWebview.getSettings();
        settings.setJavaScriptEnabled(true);
        mWebview.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);

            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

            }
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return false;

            }
        });
        mWebview.addJavascriptInterface(new AndroidAndJsInterface(),"Android");
        mWebview.setWebChromeClient(new WebChromeClient());
        //不能横向滚动
        mWebview.setHorizontalScrollBarEnabled(false);
        //不能纵向滚动
        mWebview.setVerticalScrollBarEnabled(false);
        //允许截图
        mWebview.setDrawingCacheEnabled(true);
        //屏蔽长按事件
        mWebview.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });
        //隐藏缩放控件
        settings.setBuiltInZoomControls(false);
        settings.setDisplayZoomControls(false);
        //禁止缩放
        settings.setSupportZoom(false);
        //文件权限
        settings.setAllowFileAccess(true);
        settings.setAllowFileAccessFromFileURLs(true);
        settings.setAllowUniversalAccessFromFileURLs(true);
        settings.setAllowContentAccess(true);
        //缓存相关
        settings.setAppCacheEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
    }
    private class AndroidAndJsInterface {
        @JavascriptInterface
        public void pop(){
            getSupportDelegate().pop();
        }
    }
}
