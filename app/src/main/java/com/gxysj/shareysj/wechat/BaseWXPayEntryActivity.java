package com.gxysj.shareysj.wechat;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.vondear.rxtools.RxLogTool;

/**
 * Created by 林佳荣 on 2018/2/28.
 * Github：https://github.com/ljrRookie
 * Function ：
 */

public abstract class BaseWXPayEntryActivity extends BaseWXActivity {
    private static final int WX_PAY_SUCCESS = 0;
    private static final int WX_PAY_FAIL = -1;
    private static final int WX_PAY_CANCEL = -2;

    protected abstract void onPaySuccess();

    protected abstract void onPayFail();

    protected abstract void onPayCancel();

    @Override
    public void onResp(BaseResp baseResp) {
        RxLogTool.e("微信","错误码："+baseResp.errCode+"错误信息："+baseResp.errStr +"微信类型："+baseResp.getType());
        if (baseResp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            RxLogTool.e("微信","错误码："+baseResp.errCode+"错误信息："+baseResp.errStr);
            switch (baseResp.errCode) {
                case WX_PAY_SUCCESS:
                    onPaySuccess();
                    break;
                case WX_PAY_FAIL:
                    onPayFail();
                    break;
                case WX_PAY_CANCEL:
                    onPayCancel();
                    break;
                default:
                    break;
            }
        }
    }
}
