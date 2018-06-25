package com.gxysj.shareysj.utils.pay;

import android.app.Activity;
import android.os.AsyncTask;

import com.alipay.sdk.app.PayTask;
import com.gxysj.shareysj.listener.IAliPayResultListener;
import com.vondear.rxtools.RxLogTool;


/**
 * Created by 林佳荣 on 2018/2/28.
 * Github：https://github.com/ljrRookie
 * Function ：
 */

public class PayAsyncTask extends AsyncTask<String,Void,String>{
    private final Activity ACTIVITY;
    private final IAliPayResultListener LISTENER;
    //订单支付成功
    private static final String AL_PAY_STATUS_SUCCESS = "9000";
    //订单处理中
    private static final String AL_PAY_STATUS_PAYING = "8000";
    //订单支付失败
    private static final String AL_PAY_STATUS_FAIL = "4000";
    //用户取消
    private static final String AL_PAY_STATUS_CANCEL = "6001";
    //支付网络错误
    private static final String AL_PAY_STATUS_CONNECT_ERROR = "6002";

    public PayAsyncTask(Activity activity, IAliPayResultListener listener) {
        this.ACTIVITY = activity;
        this.LISTENER = listener;
    }

    @Override
    protected String doInBackground(String... params) {
        final String aliPaySign = params[0];
        final PayTask payTask = new PayTask(ACTIVITY);
        return payTask.pay(aliPaySign,true);
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        PayResult payResult = new PayResult(result);
        final String resultInfo = payResult.getResult();
        final String resultStatus = payResult.getResultStatus();
        RxLogTool.e("支付宝",resultInfo);
        RxLogTool.e("支付宝",resultStatus);
        switch (resultStatus) {
            case AL_PAY_STATUS_SUCCESS:
                if (LISTENER != null) {
                    LISTENER.onPaySuccess();
                }
                break;
            case AL_PAY_STATUS_FAIL:
                if (LISTENER != null) {
                    LISTENER.onPayFail();
                }
                break;
            case AL_PAY_STATUS_PAYING:
                if (LISTENER != null) {
                    LISTENER.onPaying();
                }
                break;
            case AL_PAY_STATUS_CANCEL:
                if (LISTENER != null) {
                    LISTENER.onPayCancel();
                }
                break;
            case AL_PAY_STATUS_CONNECT_ERROR:
                if (LISTENER != null) {
                    LISTENER.onPayConnectError();
                }
                break;
            default:
                break;
        }
    }
}
