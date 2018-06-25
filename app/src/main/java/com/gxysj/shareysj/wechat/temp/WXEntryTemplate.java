package com.gxysj.shareysj.wechat.temp;


import com.gxysj.shareysj.wechat.BaseWXEntryActivity;
import com.gxysj.shareysj.wechat.WeChatConfig;
import com.tencent.mm.opensdk.modelbase.BaseReq;

public class WXEntryTemplate extends BaseWXEntryActivity {

    @Override
    protected void onResume() {
        super.onResume();
        finish();
        overridePendingTransition(0,0);
    }

    @Override
    protected void onSignInSuccess(String userInfo) {
        WeChatConfig.getInstance().getSignInCallback().onSignInSuccess(userInfo);
    }


}
