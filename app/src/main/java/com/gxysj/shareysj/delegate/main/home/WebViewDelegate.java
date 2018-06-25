package com.gxysj.shareysj.delegate.main.home;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.navi.view.LoadingView;
import com.gxysj.shareysj.R;
import com.gxysj.shareysj.bean.BannerWebBean;
import com.gxysj.shareysj.config.ShareYSJ;
import com.gxysj.shareysj.config.loading.LoaderCreator;
import com.gxysj.shareysj.config.loading.YSJLoading;
import com.gxysj.shareysj.delegate.base.YSJDelegate;
import com.gxysj.shareysj.network.HttpConstants;
import com.gxysj.shareysj.network.RequestCenter;
import com.ljr.delegate_sdk.okhttp.exception.OkHttpException;
import com.ljr.delegate_sdk.okhttp.listener.DisposeDataListener;
import com.ljr.delegate_sdk.okhttp.request.RequestParams;
import com.vondear.rxtools.view.RxToast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import me.yokeyword.fragmentation.ISupportFragment;

/**
 * Created by 林佳荣 on 2018/4/12.
 * Github：https://github.com/ljrRookie
 * Function ：
 */

public class WebViewDelegate extends YSJDelegate {
    @BindView(R.id.btn_back)
    ImageView mBtnBack;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.webView)
    WebView mWebview;
    Unbinder unbinder;
    private String mId;
    private String mUrl;

    @Override
    public Object setLayout() {
        return R.layout.delegate_home_banner_webview;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        initWebView();
        initData();
    }

    private void initWebView() {
        WebSettings settings = mWebview.getSettings();
        settings.setJavaScriptEnabled(true);
        mWebview.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                YSJLoading.showLoading(getActivity());
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                YSJLoading.stopLoading();
            }
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return false;

            }
        });
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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if(arguments!=null){
            mId = arguments.getString("id", "");
        }
    }

    private void initData() {
        RequestParams params = new RequestParams("id", mId);
        RequestCenter.GetArticleInfo(params, new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                BannerWebBean bannerWebBean = (BannerWebBean) responseObj;
                mTvTitle.setText(bannerWebBean.getTitle());
                mUrl = bannerWebBean.getUrl();
                mWebview.loadUrl(HttpConstants.ROOT_URL+mUrl);
            }

            @Override
            public void onFailure(Object responseObj) {
                OkHttpException error = (OkHttpException) responseObj;
                RxToast.normal("获取数据失败 - (" + error.getEmsg() + ")");
            }
        });
    }


    @OnClick(R.id.btn_back)
    public void onViewClicked() {
        getSupportDelegate().pop();
    }

    public static WebViewDelegate create(String id) {
        WebViewDelegate webViewDelegate = new WebViewDelegate();
        Bundle bundle = new Bundle();
        bundle.putString("id",id);
        webViewDelegate.setArguments(bundle);
        return webViewDelegate;
    }
}
