package com.gxysj.shareysj.wechat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

/**
 * Created by 林佳荣 on 2018/2/28.
 * Github：https://github.com/ljrRookie
 * Function ：微信基类
 */

public abstract class BaseWXActivity extends AppCompatActivity implements IWXAPIEventHandler{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WeChatConfig.getInstance().getWXAPI().handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        WeChatConfig.getInstance().getWXAPI().handleIntent(getIntent(), this);
    }
}
