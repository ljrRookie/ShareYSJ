package com.ljr.delegate_sdk.okhttp.response;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.ljr.delegate_sdk.okhttp.exception.OkHttpException;
import com.ljr.delegate_sdk.okhttp.listener.DisposeDataHandle;
import com.ljr.delegate_sdk.okhttp.listener.DisposeDataListener;
import com.ljr.delegate_sdk.util.ResponseEntityToModule;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by 林佳荣 on 2018/2/8.
 * Github：https://github.com/ljrRookie
 * Function ：处理json的回调
 */

public class CommonJsonCallback implements Callback {
    //请求返回的code
    protected final String RESULT_CODE = "code";
    //请求返回的code值 - 成功
    protected final int[] RESULT_CODE_VALUE = {0,-1,-2};

    //请求失败的返回信息
    protected final String RESULT_MSG = "emsg";
    //请求没有数据
    protected final String EMPTY_MSG = "网络连接超时！";

    //请求失败
    /**
     * the network relative error
     */
    protected final int NETWORK_ERROR = -1000;
    /**
     * the JSON relative error
     */
    protected final int JSON_ERROR = -2000;
    /**
     * the unknow error
     */
    protected final int OTHER_ERROR = -3000;
    /**
     * 将其它线程的数据转发到UI线程
     */
    private Handler mDeliveryHandler;
    private DisposeDataListener mListener;
    private Class<?> mClass;
    /**
     * token 失效
     */
    protected final int TOKEN_ERROR = 10000;
    public CommonJsonCallback(DisposeDataHandle handler) {
        this.mListener = handler.mDisposeDataListener;
        this.mClass = handler.mClass;
        this.mDeliveryHandler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void onFailure(Call call, final IOException e) {
        /**
         * 此时还在非UI线程，因此要转发
         */
        mDeliveryHandler.post(new Runnable() {
            @Override
            public void run() {
                mListener.onFailure(new OkHttpException(NETWORK_ERROR,  EMPTY_MSG));
            }
        });
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        final String result = response.body().string();
        mDeliveryHandler.post(new Runnable() {
            @Override
            public void run() {
                handleResponse(result);
            }
        });
    }
    public static boolean useLoop(int[] arr, int targetValue) {
        for(int s: arr){
            if(s==targetValue)
                return true;
        }
        return false;
    }
    private void handleResponse(Object responseObj) {

        if (responseObj == null || responseObj.toString().trim().equals("")) {
            mListener.onFailure(new OkHttpException(NETWORK_ERROR, EMPTY_MSG));
            return;
        }
        try {
            /**
             * 协议确定后看这里如何修改
             */
            Log.e("okhttp", "请求结果："+responseObj.toString());
            JSONObject result = new JSONObject(responseObj.toString());

            if (result.has(RESULT_CODE)) {
                //从Json对象中取出我们的响应码，若成功，则是正确的响应
                if(useLoop(RESULT_CODE_VALUE,result.getInt(RESULT_CODE)) ){
                    Log.e("okhttp", "存在  code  == 1");
                    if (mClass == null) {
                        mListener.onSuccess(result);
                    } else {
                        Object obj = ResponseEntityToModule.parseJsonObjectToModule(result, mClass);
                        if(obj != null){
                            mListener.onSuccess(obj);
                        }else{
                            mListener.onFailure(new OkHttpException(JSON_ERROR,EMPTY_MSG));
                            Log.e("okhttp:", "obj == null");
                        }
                    }
                }else{
                    Log.e("okhttp", "存在  code  ！= 1");
                    //将服务器返回给我们的异常回调到应用处去处理   请求码错误
                    int errorCode = result.getInt(RESULT_CODE);

                    if (result.has("error")) {
                        Log.e("okhttp", "存在 error");
                        String error  = null;
                        error = result.getString("error");
                        if(!error.isEmpty()){
                            Log.e("okhttp", " error 不为空");
                            mListener.onFailure(new OkHttpException(errorCode,error));
                        }else{
                            Log.e("okhttp", " error 为空");
                            mListener.onFailure(new OkHttpException(errorCode,"网络连接超时！"));
                        }
                    }else{
                        Log.e("okhttp", "不存在 error");
                        mListener.onFailure(new OkHttpException(result.getInt(RESULT_CODE),"网络连接超时！"));
                    }

                }
            }else{
                Log.e("okhttp", "不存在 code");
                mListener.onFailure(new OkHttpException(result.getInt(RESULT_CODE),"请求错误"));

            }
        } catch (JSONException e) {
            Log.e("okhttp", "JSONException");
            mListener.onFailure(new OkHttpException(OTHER_ERROR, "异常操作"));
            e.printStackTrace();
        }
    }
}
