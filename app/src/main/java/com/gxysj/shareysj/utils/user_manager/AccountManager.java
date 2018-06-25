package com.gxysj.shareysj.utils.user_manager;

import com.gxysj.shareysj.config.Configurator;
import com.gxysj.shareysj.config.ShareYSJ;
import com.gxysj.shareysj.listener.IUserChecker;
import com.vondear.rxtools.RxSPTool;

/**
 * Created by Administrator on 2018/1/29.
 */

public class AccountManager {
    private enum SignTag{
        SIGN_TAG
    }
    //保存用户登录状态
    public static void setSignState(boolean state){
        RxSPTool.putBoolean(ShareYSJ.getApplicationContext(),SignTag.SIGN_TAG.name(),state);
    }
    private static boolean isSignIn(){
        return RxSPTool.getBoolean(ShareYSJ.getApplicationContext(), SignTag.SIGN_TAG.name());
    }
    public static void checkAccount(IUserChecker iUserCheck){
        if(isSignIn()){
            iUserCheck.onSignIn();
        }else{
            iUserCheck.onNotSignIn();
        }

    }
}
