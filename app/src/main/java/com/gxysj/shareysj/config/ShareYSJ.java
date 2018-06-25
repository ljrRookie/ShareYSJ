package com.gxysj.shareysj.config;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;

import com.gxysj.shareysj.config_enum.ConfigKeys;

import java.util.HashMap;

/**
 * Created by Administrator on 2018/1/29.
 */

public final class ShareYSJ {
    public static Configurator init(Context context){
        Configurator.getInstance()
                .getYsjConfigs()
                .put(ConfigKeys.APPLICATION_CONTEXT, context.getApplicationContext());
        return Configurator.getInstance();
    }
    public static Configurator getConfigurator(){
        return Configurator.getInstance();
    }
    public static <T> T getConfiguration(Object key){
        return getConfigurator().getConfiguration(key);
    }
    public static Context getApplicationContext(){
        return getConfiguration(ConfigKeys.APPLICATION_CONTEXT);
    }
    public static Activity getActivity(){
        return getConfiguration(ConfigKeys.ACTIVITY);
    }
    public static Handler getHandler(){
        return getConfiguration(ConfigKeys.HANDLER);
    }
}
