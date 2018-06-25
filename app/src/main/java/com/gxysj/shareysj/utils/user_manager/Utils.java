package com.gxysj.shareysj.utils.user_manager;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;

import com.gxysj.shareysj.config.ShareYSJ;
import com.gxysj.shareysj.utils.SPUtil;
import com.gxysj.shareysj.utils.TimeUtils;
import com.ljr.delegate_sdk.okhttp.request.RequestParams;
import com.vondear.rxtools.RxLogTool;
import com.vondear.rxtools.RxTimeTool;

import java.io.ByteArrayOutputStream;

/**
 * Created by 林佳荣 on 2018/4/9.
 * Github：https://github.com/ljrRookie
 * Function ：
 */

public class Utils {
    /**
     * Bitmap转换成byte[]并且进行压缩,压缩到不大于maxkb
     *
     * @param bitmap
     * @param maxkb
     * @return
     */
    public static byte[] bitmap2Bytes(Bitmap bitmap, int maxkb) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, output);
        int options = 100;
        while (output.toByteArray().length > maxkb && options != 10) {
            output.reset(); //清空output
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, output);//这里压缩options%，把压缩后的数据存放到output中
            options -= 10;
        }
        return output.toByteArray();
    }
    public static int getVersionCode(Context context) {
        int versionCode = 1;
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionCode = pi.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return versionCode;
    }
    public static RequestParams getRequestParams() {
        long time = RxTimeTool.getCurTimeMills();
        String timeStr = String.valueOf(time);
        String secretKey = TimeUtils.getSecretKey(timeStr);
        RequestParams requestParams = new RequestParams();
        requestParams.put("userid", SPUtil.get(ShareYSJ.getApplicationContext(), "userId", ""));
        requestParams.put("token", SPUtil.get(ShareYSJ.getApplicationContext(), "token", ""));
        requestParams.put("secret_key", secretKey);
        requestParams.put("time", timeStr);
        RxLogTool.e("params", "userID: " + SPUtil.get(ShareYSJ.getApplicationContext(), "userId", ""));
        RxLogTool.e("params", "token: " + SPUtil.get(ShareYSJ.getApplicationContext(), "token", ""));
        RxLogTool.e("params", "secretKey: " + secretKey);
        RxLogTool.e("params", "time: " + timeStr);
        return requestParams;
    }
}
