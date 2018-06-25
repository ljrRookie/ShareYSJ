package com.gxysj.shareysj.wechat;

import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.vondear.rxtools.RxLogTool;

/**
 * Created by 林佳荣 on 2018/2/28.
 * Github：https://github.com/ljrRookie
 * Function ：微信登录入口
 */

public abstract class BaseWXEntryActivity extends BaseWXActivity {
    //用户登录成功后的回调
    protected abstract void onSignInSuccess(String info);
    @Override
    public void onReq(BaseReq baseReq) {

    }
    //第三方应用发送请求到微信后的回调
    @Override
    public void onResp(BaseResp baseResp) {
        String code = ((SendAuth.Resp) baseResp).code;
        RxLogTool.e("微信","微信登录：code："+code);
        onSignInSuccess(code);
    }
}
