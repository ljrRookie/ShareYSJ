package com.ljr.delegate_sdk.okhttp;

import com.ljr.delegate_sdk.okhttp.https.HttpsUtils;
import com.ljr.delegate_sdk.okhttp.listener.DisposeDataHandle;
import com.ljr.delegate_sdk.okhttp.response.CommonJsonCallback;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.Call;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by 林佳荣 on 2018/2/8.
 * Github：https://github.com/ljrRookie
 * Function ：发送请求工具类，包括设置一些请求的共用参数
 */

public class CommonOkHttpClient {
    private static final int TIME_OUT = 30;
    private static OkHttpClient mOkHttpClient;
    static {
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
        okHttpClientBuilder.hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });
        /**
         *  为所有请求添加请求头，看个人需求
         */
        okHttpClientBuilder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request()
                        .newBuilder()
                        .addHeader("User-Agent", "app-ZSSZ") // 标明发送本次请求的客户端
                        .build();
                return chain.proceed(request);
            }
        });
        /**
         * 支持所以https协议
         */
        okHttpClientBuilder.sslSocketFactory(HttpsUtils.initSSLSocketFactory(), HttpsUtils.initTrustManager());
        //okHttpClientBuilder.cookieJar(new SimpleCookieJar());
        okHttpClientBuilder.connectTimeout(TIME_OUT, TimeUnit.SECONDS);
        okHttpClientBuilder.readTimeout(TIME_OUT, TimeUnit.SECONDS);
        okHttpClientBuilder.writeTimeout(TIME_OUT, TimeUnit.SECONDS);
        okHttpClientBuilder.followRedirects(true);
        mOkHttpClient = okHttpClientBuilder.build();
    }
    public static OkHttpClient getOkHttpClinet(){
        return mOkHttpClient;
    }

    /**
     * 发送具体的http/https请求
     * @param request
     * @param handle
     * @return
     */

    public static Call get(Request request, DisposeDataHandle handle) {
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new CommonJsonCallback(handle));
        return call;
    }

    public static Call post(Request request, DisposeDataHandle handle) {

        Call call = getOkHttpClinet().newCall(request);
        call.enqueue(new CommonJsonCallback(handle));
        return call;
    }
}
