package com.gxysj.shareysj.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.gxysj.shareysj.R;
import com.gxysj.shareysj.network.HttpConstants;
import com.gxysj.shareysj.service.update.UpdateDownloadListener;
import com.gxysj.shareysj.service.update.UpdateManager;
import com.vondear.rxtools.RxLogTool;
import com.vondear.rxtools.view.RxToast;

import java.io.File;

/**
 * Created by 林佳荣 on 2018/4/10.
 * Github：https://github.com/ljrRookie
 * Function ：
 */

public class UpdateService extends Service {

    private NotificationManager mNotificationManager;
    /**
     * 文件存放路经
     */
    private String filePath;
    /**
     * 文件下载地址
     */
    private String apkUrl;
    private Notification mNotification;
    private PendingIntent mContentIntent;
    private Intent mInstallApkIntent;
    private String mApkurl;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        filePath = Environment.getExternalStorageDirectory() + "/update/update.apk";
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mApkurl = intent.getStringExtra("apk");
        apkUrl = HttpConstants.ROOT_URL+mApkurl;
        RxLogTool.e("update","apkurl:"+apkUrl);
        notifyNotification("开始下载", "开始下载", 0);
        startDownload();
        return super.onStartCommand(intent, flags, startId);
    }

    private void startDownload() {
        UpdateManager.getInstance().startDownload(apkUrl, filePath, new UpdateDownloadListener() {
            @Override
            public void onStarted() {

            }

            @Override
            public void onPrepared(long contentLength, String downloadUrl) {

            }
            @Override
            public void onProgressChanged(int progress, String downloadUrl) {
                    notifyNotification("正在下载",
                            "正在下载", progress);
                RxLogTool.e("service","onProgressChanged  progress="+progress);
                }


            @Override
            public void onPaused(int progress, int completeSize, String downloadUrl) {
                notifyNotification("下载暂停","下载失败,请检查网络连接",0);
                deleteApkFile();
                stopSelf();// 停掉服务自身
            }

            @Override
            public void onFinished(int completeSize, String downloadUrl) {
                RxLogTool.e("service","onProgressChanged  completeSize="+completeSize);
                notifyNotification("下载完成","下载完成",100);
                stopSelf();// 停掉服务自身
                startActivity(getInstallApkIntent());
            }

            @Override
            public void onFailure() {
                notifyNotification("下载失败","下载失败,请检查网络连接",0);
                deleteApkFile();
                stopSelf();// 停掉服务自身
            }
        });
    }

    private void notifyNotification(String tickerMsg, String message, int progress) {
        notifyThatExceedLv21(tickerMsg, message, progress);
    }

    private void notifyThatExceedLv21(String tickerMsg, String message, int progress) {
        NotificationCompat.Builder notification = new NotificationCompat.Builder(this);
        notification.setSmallIcon(R.mipmap.icon_launch);
        notification.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.icon_launch));
        notification.setContentTitle(getString(R.string.app_name));

        if (progress > 0 && progress < 100) {
            RxLogTool.e("service","progress > 0 && progress < 100"+progress);
            notification.setProgress(100, progress, false);
           // notification.setProgress(0, 0, false);
            notification.setContentText("下载进度："+progress+"%");
        } else {
            /**
             * 0,0,false,可以将进度条影藏
             */
            RxLogTool.e("service","可以将进度条影藏"+progress);
            notification.setProgress(0, 0, false);
            notification.setContentText(message);
        }
            notification.setAutoCancel(true);
            notification.setWhen(System.currentTimeMillis());
            notification.setTicker(tickerMsg);
        RxLogTool.e("service","progress >= 100"+progress);
            notification.setContentIntent(progress >= 100 ? getContentIntent() : PendingIntent.getActivity(this, 0,
                    new Intent(), PendingIntent.FLAG_UPDATE_CURRENT));
            mNotification = notification.build();
            mNotificationManager.notify(0, mNotification);
    }

    public PendingIntent getContentIntent() {
        PendingIntent mContentIntent = PendingIntent.getActivity(this, 0, getInstallApkIntent(),
                PendingIntent.FLAG_UPDATE_CURRENT);
        return mContentIntent;
    }

    /**
     * 下载完成，安装
     */
    public Intent getInstallApkIntent() {
        File apkfile = new File(filePath);
        Intent mInstallApkIntent = new Intent();
        mInstallApkIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mInstallApkIntent.setAction(Intent.ACTION_VIEW);
        mInstallApkIntent.setDataAndType(Uri.fromFile(apkfile), "application/vnd.android.package-archive");
        return mInstallApkIntent;
    }

    /**
     * 删除无用apk文件
     */
    private boolean deleteApkFile() {
        File apkFile = new File(filePath);
        if (apkFile.exists() && apkFile.isFile()) {
            return apkFile.delete();
        }
        return false;
    }
}
