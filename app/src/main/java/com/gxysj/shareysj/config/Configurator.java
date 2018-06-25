package com.gxysj.shareysj.config;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.widget.TableRow;

import com.gxysj.shareysj.config_enum.ConfigKeys;
import com.gxysj.shareysj.utils.CrashHandle;
import com.joanzapata.iconify.IconFontDescriptor;
import com.joanzapata.iconify.Iconify;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.vondear.rxtools.RxTool;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2018/1/29.
 */

public class Configurator {
    private static final HashMap<Object, Object> YSJ_CONFIGS = new HashMap<>();
    private static final Handler HANDLER = new Handler();
    private static final ArrayList<IconFontDescriptor> ICONS = new ArrayList<>();

    private Configurator() {
        YSJ_CONFIGS.put(ConfigKeys.CONFIG_READY.name(),false);
        YSJ_CONFIGS.put(ConfigKeys.HANDLER, HANDLER);
    }
    public static Configurator getInstance(){
        return Holder.INSTANCE;
    }
    final HashMap<Object,Object> getYsjConfigs(){
        return YSJ_CONFIGS;
    }
    private static class Holder {
        private static final Configurator INSTANCE = new Configurator();
    }
    @SuppressWarnings("unchecked")
    final <T> T getConfiguration(Object key){
        checkConfiguration();
        return (T) YSJ_CONFIGS.get(key);
    }

    private void checkConfiguration() {
        final boolean isReady = (boolean) YSJ_CONFIGS.get(ConfigKeys.CONFIG_READY);
        if(!isReady){
            throw new RuntimeException("Configuration is not ready,call,configure");
        }
    }
    public final void configure(){
        initIcons();
        YSJ_CONFIGS.put(ConfigKeys.CONFIG_READY, true);
    }
    public final Configurator withApiHost(String host){
        YSJ_CONFIGS.put(ConfigKeys.API_HOST, host);
        return this;
    }
    public final Configurator withRxTool(Context context){
        RxTool.init(context);
        return this;
    }
    public final Configurator withCrashHandle(Context context){
        CrashHandle.getInstance().init(context);
        return this;
    }
    public final Configurator withLoaderDelayed(long delayed){
        YSJ_CONFIGS.put(ConfigKeys.LOADER_DELAYED, delayed);
        return this;
    }

    private void initIcons() {
        if (ICONS.size() > 0) {
            final Iconify.IconifyInitializer initializer = Iconify.with(ICONS.get(0));
            for (int i = 1; i < ICONS.size(); i++) {
                initializer.with(ICONS.get(i));
            }
        }
    }
    public final Configurator withIcon(IconFontDescriptor descriptor){
        ICONS.add(descriptor);
        return this;
    }
    public final Configurator withLogger(){
        Logger.addLogAdapter(new AndroidLogAdapter());
        return this;
    }
    public final Configurator withWeChatAppId(String appId) {
        YSJ_CONFIGS.put(ConfigKeys.WE_CHAT_APP_ID, appId);
        return this;
    }

    public final Configurator withWeChatAppSecret(String appSecret) {
        YSJ_CONFIGS.put(ConfigKeys.WE_CHAT_APP_SECRET, appSecret);
        return this;
    }
    public final Configurator withActivity(Activity activity) {
        YSJ_CONFIGS.put(ConfigKeys.ACTIVITY, activity);
        return this;
    }
}
