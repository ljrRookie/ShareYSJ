package com.gxysj.shareysj.jpush;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.gxysj.shareysj.activity.MainActivity;
import com.vondear.rxtools.RxLogTool;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.List;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by 林佳荣 on 2018/4/18.
 * Github：https://github.com/ljrRookie
 * Function ：
 */

public class JPushReceiver extends BroadcastReceiver{
    private static final String TAG = JPushReceiver.class.getSimpleName();
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        Log.d(TAG, "[JPushReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));
        RxLogTool.e("push","===================onReceive===============");
        if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            Log.d(TAG, "[JPushReceiver] 接收到推送下来的通知的ID: " + notifactionId);
            RxLogTool.e("push","===================ACTION_NOTIFICATION_RECEIVED===============");

        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            RxLogTool.e("push","===================ACTION_NOTIFICATION_OPENED===============");
            /**
             * 此处可以通过写一个方法，决定出要跳转到那些页面，一些细节的处理，可以通过是不是从推送过来的，去多一个分支去处理。
             * 1.应用未启动,------>启动主页----->不需要登陆信息类型，直接跳转到消息展示页面
             *                         ----->需要登陆信息类型，由于应用都未启动，肯定不存在已经登陆这种情况------>跳转到登陆页面
             *                                                                                                 ----->登陆完毕，跳转到信息展示页面。
             *                                                                                                 ----->取消登陆，返回首页。
             * 2.如果应用已经启动，------>不需要登陆的信息类型，直接跳转到信息展示页面。
             *                 ------>需要登陆的信息类型------>已经登陆----->直接跳转到信息展示页面。
             *                                      ------>未登陆------->则跳转到登陆页面
             *                                                                      ----->登陆完毕，跳转到信息展示页面。
             *                                                                      ----->取消登陆，回到首页。
             *
             * 3.startActivities(Intent[]);在推送中的妙用,注意startActivities在生命周期上的一个细节,
             * 前面的Activity是不会真正创建的，直到要到对应的页面
             * 4.如果为了复用，可以将极光推送封装到一个Manager类中,为外部提供init, setTag, setAlias,
             * setNotificationCustom等一系列常用的方法。
             */

            /**
             * 如果应用已经启动(无论前台还是后台)
             */
            if (getCurrentTask(context)) {
                RxLogTool.e("push","===================应用已经启动===============");
                if(isBackground(context)){
                    Intent pushIntent = new Intent();
                    pushIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    pushIntent.setClass(context, MainActivity.class);
                    context.startActivity(pushIntent);
                }

            } else {
                RxLogTool.e("push","===================应用没有启动===============");
                /**
                 * 这里只分了两种类型，如果消息类型很多的话，用switch--case去匹配
                 */

                Intent mainIntent = new Intent(context, MainActivity.class);
                mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
               // mainIntent.putExtra("fromPush", true);
                mainIntent.setClass(context, MainActivity.class);
                context.startActivity(mainIntent);
               /* if (pushMessage.messageType != null
                        && pushMessage.messageType.equals("2")) {
                    Intent loginIntent = new Intent();
                    loginIntent.setClass(context, LoginActivity.class);
                    loginIntent.putExtra("fromPush", true);
                    loginIntent.putExtra("pushMessage", pushMessage);
                    context.startActivities(new Intent[]{mainIntent, loginIntent});
                } else {
                    Intent pushIntent = new Intent(context, PushMessageActivity.class);
                    pushIntent.putExtra("pushMessage", pushMessage);
                    context.startActivities(new Intent[]{mainIntent, pushIntent});
                }*/
            }
        }
    }

    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            } else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                if (bundle.getString(JPushInterface.EXTRA_EXTRA).isEmpty()) {
                    Log.i(TAG, "This message has no Extra data");
                    continue;
                }

                try {

                    /**
                     * 先将JSON字符串转化为对象，再取其中的字段
                     */
                    JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                    Iterator<String> it = json.keys();

                    while (it.hasNext()) {
                        String myKey = it.next().toString();
                        sb.append("\nkey:" + key + ", value: [" + myKey + " - " + json.optString(myKey) + "]");
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "Get message extra JSON error!");
                }

            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
            }
        }
        return sb.toString();
    }

    /**
     * 这个是真正的获取指定包名的应用程序是否在运行(无论前台还是后台)
     *
     * @return
     */
    private boolean getCurrentTask(Context context) {

        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> appProcessInfos = activityManager.getRunningTasks(50);
        for (ActivityManager.RunningTaskInfo process : appProcessInfos) {

            if (process.baseActivity.getPackageName().equals(context.getPackageName())
                    || process.topActivity.getPackageName().equals(context.getPackageName())) {

                return true;
            }
        }
        return false;
    }
    public static boolean isBackground(Context context) {
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
                /*
                BACKGROUND=400 EMPTY=500 FOREGROUND=100
                GONE=1000 PERCEPTIBLE=130 SERVICE=300 ISIBLE=200
                 */
                Log.i(context.getPackageName(), "此appimportace ="
                        + appProcess.importance
                        + ",context.getClass().getName()="
                        + context.getClass().getName());
                if (appProcess.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    Log.i(context.getPackageName(), "处于后台"
                            + appProcess.processName);
                    return true;
                } else {
                    Log.i(context.getPackageName(), "处于前台"
                            + appProcess.processName);
                    return false;
                }
            }
        }
        return false;
    }


}
