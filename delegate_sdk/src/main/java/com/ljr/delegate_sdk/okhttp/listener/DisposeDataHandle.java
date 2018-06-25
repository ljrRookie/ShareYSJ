package com.ljr.delegate_sdk.okhttp.listener;

/**
 * Created by 林佳荣 on 2018/2/8.
 * Github：https://github.com/ljrRookie
 * Function ：处理jsonbean实体
 */

public class DisposeDataHandle {
    public DisposeDataListener mDisposeDataListener = null;
    public Class<?> mClass = null;
    public String mSource = null;

    public DisposeDataHandle(DisposeDataListener disposeDataListener) {
        this.mDisposeDataListener = disposeDataListener;
    }

    public DisposeDataHandle(DisposeDataListener disposeDataListener, Class<?> aClass) {
        this.mDisposeDataListener = disposeDataListener;
        this.mClass = aClass;
    }

    public DisposeDataHandle(DisposeDataListener disposeDataListener, String source) {
        this.mDisposeDataListener = disposeDataListener;
        this.mSource = source;
    }
}
