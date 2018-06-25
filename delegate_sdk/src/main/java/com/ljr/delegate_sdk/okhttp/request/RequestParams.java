package com.ljr.delegate_sdk.okhttp.request;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by 林佳荣 on 2018/2/8.
 * Github：https://github.com/ljrRookie
 * Function ：请求参数
 */

public class RequestParams {
    public ConcurrentHashMap<String, Object> urlParams = new ConcurrentHashMap<>();
    public ConcurrentHashMap<String, Object> fileParams = new ConcurrentHashMap<>();

    /**
     * 创建一个空的构造方法
     */
    public RequestParams() {
        this((Map<String,Object>) null);
    }

    /**
     *  Constructs a new RequestParams instance containing the key/value string
     * @param source the source key/value string map to add.
     */
    public RequestParams(Map<String, Object> source) {
        if (source != null) {
            for (Map.Entry<String, Object> entry : source.entrySet()) {
                put(entry.getKey(), entry.getValue());
            }
        }
    }

    /**
     *
     * @param key 参数名
     * @param value 参数值
     */
    public RequestParams(final String key, final Object value) {
        this(new HashMap<String, Object>() {
            {
                put(key, value);
            }
        });
    }

    public void put(String key, Object value) {
        if (key != null && value != null) {
            urlParams.put(key, value);
        }
    }
    public boolean hasParams() {
        if(urlParams.size() > 0 || fileParams.size() > 0){

            return true;
        }
        return false;
    }

}
