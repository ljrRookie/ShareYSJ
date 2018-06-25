package com.ljr.delegate_sdk.okhttp.request;

import java.io.File;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by 林佳荣 on 2018/2/8.
 * Github：https://github.com/ljrRookie
 * Function ：基于okhttp创建一个请求类
 */

public class CommonRequest {
    /**
     * 创建一个post请求
     * @param url
     * @param params
     * @return
     */
    public static Request createPostRequest(String url ,RequestParams params){
        return createPostRequest(url, params, null);
    }

    /**
     * 可以带请求头的Post请求
     * @param url
     * @param params
     * @param headers
     * @return
     */
    private static Request createPostRequest(String url, RequestParams params, RequestParams headers) {
        FormBody.Builder mFormBodyBuild = new FormBody.Builder();
        if(params != null){
            for (Map.Entry<String,Object> entry :params.urlParams.entrySet()){
                mFormBodyBuild.add(entry.getKey(), String.valueOf(entry.getValue()));
            }
        }
        //添加请求头
        Headers.Builder mHeaderBuild = new Headers.Builder();
        if(headers != null){
            for (Map.Entry<String,Object> entry :headers.urlParams.entrySet()){
                mHeaderBuild.add(entry.getKey(), String.valueOf(entry.getValue()));
            }
        }
        FormBody mFormBody = mFormBodyBuild.build();
        Headers mHeaders = mHeaderBuild.build();
        Request request = new Request.Builder().url(url).post(mFormBody).headers(mHeaders)
                .build();
        return request;
    }

    /**
     * 创建一个get请求
     * @param url
     * @param params
     * @return
     */
    public static Request createGetRequest(String url,RequestParams params){
        return createGetRequest(url, params, null);

    }

    /**
     * 创建一个可以带请求头的get请求
     * @param url
     * @param params
     * @param headers
     * @return
     */
    private static Request createGetRequest(String url, RequestParams params, RequestParams headers) {
        StringBuilder urlBuilder = new StringBuilder(url).append("?");
        if (params != null) {
            for (Map.Entry<String, Object> entry : params.urlParams.entrySet()) {
                urlBuilder.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }
        }
        //添加请求头
        Headers.Builder mHeaderBuild = new Headers.Builder();
        if (headers != null) {
            for (Map.Entry<String, Object> entry : headers.urlParams.entrySet()) {
                mHeaderBuild.add(entry.getKey(), String.valueOf(entry.getValue()));
            }
        }
        Headers mHeader = mHeaderBuild.build();
        return new Request.Builder().
                url(urlBuilder.substring(0, urlBuilder.length() - 1))
                .get()
                .headers(mHeader)
                .build();
    }
    /**
     * @param url
     * @param params
     * @return
     */
    public static Request createMonitorRequest(String url, RequestParams params) {
        StringBuilder urlBuilder = new StringBuilder(url).append("&");
        if (params != null && params.hasParams()) {
            for (Map.Entry<String, Object> entry : params.urlParams.entrySet()) {
                urlBuilder.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }
        }
        return new Request.Builder().url(urlBuilder.substring(0, urlBuilder.length() - 1)).get().build();
    }

    /**
     * 文件上传请求
     *
     * @return
     */
    private static final MediaType FILE_TYPE = MediaType.parse("application/octet-stream");

    public static Request createMultiPostRequest(String url, RequestParams params) {

        MultipartBody.Builder requestBody = new MultipartBody.Builder();
        requestBody.setType(MultipartBody.FORM);
        if (params != null) {

            for (Map.Entry<String, Object> entry : params.fileParams.entrySet()) {
                if (entry.getValue() instanceof File) {
                    requestBody.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + entry.getKey() + "\""),
                            RequestBody.create(FILE_TYPE, (File) entry.getValue()));
                } else if (entry.getValue() instanceof String) {

                    requestBody.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + entry.getKey() + "\""),
                            RequestBody.create(null, (String) entry.getValue()));
                }
            }
        }
        return new Request.Builder().url(url).post(requestBody.build()).build();
    }
}
