package com.gxysj.shareysj.wechat.temp;


import com.gxysj.shareysj.R;
import com.gxysj.shareysj.bean.EventBusPayType;
import com.gxysj.shareysj.bean.event.EventEmpty;
import com.gxysj.shareysj.config.ShareYSJ;
import com.gxysj.shareysj.delegate.PayForSuccessDelegate;
import com.gxysj.shareysj.delegate.base.YSJDelegate;
import com.gxysj.shareysj.delegate.home.BottomDelegate;
import com.gxysj.shareysj.utils.SPUtil;
import com.gxysj.shareysj.wechat.BaseWXPayEntryActivity;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.vondear.rxtools.RxLogTool;
import com.vondear.rxtools.RxSPTool;
import com.vondear.rxtools.view.RxToast;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by 傅令杰 on 2017/1/2
 */

public class WXPayEntryTemplate extends BaseWXPayEntryActivity {


    @Override
    protected void onPaySuccess() {
        RxLogTool.e("微信","支付结果：成功");
        boolean isReChargeBalance = (boolean) SPUtil.get(ShareYSJ.getApplicationContext(), "isReChargeBalance", false);
        if(isReChargeBalance){
            finish();
            overridePendingTransition(0, 0);
            SPUtil.put(ShareYSJ.getApplicationContext(),"isReChargeBalance",false);
        }else{
            finish();
            overridePendingTransition(0, 0);
            EventBus.getDefault().post(new EventBusPayType(1));
        }


    }

    @Override
    protected void onPayFail() {
        RxLogTool.e("微信","支付结果：失败");
        RxToast.normal("微信支付失败");
        finish();
        overridePendingTransition(0, 0);
        SPUtil.put(ShareYSJ.getApplicationContext(),"isReChargeBalance",false);
    }

    @Override
    protected void onPayCancel() {
        RxLogTool.e("微信","支付结果：取消");
        finish();
        RxToast.normal("取消了微信支付");
        overridePendingTransition(0, 0);
        SPUtil.put(ShareYSJ.getApplicationContext(),"isReChargeBalance",false);
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }


}
