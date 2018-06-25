package com.gxysj.shareysj.wechat;

import android.app.Activity;

import com.gxysj.shareysj.config.ShareYSJ;
import com.gxysj.shareysj.config_enum.ConfigKeys;
import com.gxysj.shareysj.wechat.callback.IWeChatSignInCallback;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * Created by 林佳荣 on 2018/2/28.
 * Github：https://github.com/ljrRookie
 * Function ：微信实例
 */

public class WeChatConfig {
    public static final String APP_ID = ShareYSJ.getConfiguration(ConfigKeys.WE_CHAT_APP_ID);
    public static final String APP_SECRET = ShareYSJ.getConfiguration(ConfigKeys.WE_CHAT_APP_SECRET);
    private final IWXAPI mIWXAPI;
    private IWeChatSignInCallback mSignInCallback = null;
    private static final class Holder{
        private static final WeChatConfig INSTANCE = new WeChatConfig();
    }
    public static WeChatConfig getInstance(){
        return Holder.INSTANCE;
    }
    private WeChatConfig(){
        final Activity activity = ShareYSJ.getConfiguration(ConfigKeys.ACTIVITY);
        mIWXAPI = WXAPIFactory.createWXAPI(activity, APP_ID, true);
        mIWXAPI.registerApp(APP_ID);
    }
    public WeChatConfig onSignSuccess(IWeChatSignInCallback callback) {
        this.mSignInCallback = callback;
        return this;
    }
    public IWeChatSignInCallback getSignInCallback() {
        return mSignInCallback;
    }
    public final IWXAPI getWXAPI(){
        return mIWXAPI;
    }
    public final void signIn(){
        final SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "random_state";
        mIWXAPI.sendReq(req);
    }
}
