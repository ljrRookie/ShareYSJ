package com.gxysj.shareysj.application;

import android.app.Application;
import android.content.Context;

import com.gxysj.shareysj.config.ShareYSJ;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

import cn.jpush.android.api.JPushInterface;


/**
 * Created by Administrator on 2018/1/29.
 */

public class MyApplication extends Application {
    public static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this.getApplicationContext();
        initConfig();
          		// 初始化 JPush
    }

    private void initConfig() {
        ShareYSJ.init(this)
                .withIcon(new FontAwesomeModule())
                .withApiHost("")
                .withWeChatAppId("wx4e454178e430a37a")
                .withWeChatAppSecret("")
                .withLogger()
                .withRxTool(mContext)
             //  .withCrashHandle(mContext)
                .configure();
        //开启极光推送
        JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);
    }
}
