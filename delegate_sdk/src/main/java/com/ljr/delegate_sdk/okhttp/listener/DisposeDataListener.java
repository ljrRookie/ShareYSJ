package com.ljr.delegate_sdk.okhttp.listener;

/**
 * Created by 林佳荣 on 2018/2/8.
 * Github：https://github.com/ljrRookie
 * Function ：
 */

public interface DisposeDataListener {
    /**
     * 请求成功回调事件处理
     */
    void onSuccess(Object responseObj);
    /**
     * 请求失败回调事件处理
     */
    void onFailure(Object responseObj);
}
