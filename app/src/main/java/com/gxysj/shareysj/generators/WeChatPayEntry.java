package com.gxysj.shareysj.generators;


import com.example.annotation.PayEntryGenerator;
import com.gxysj.shareysj.wechat.temp.WXPayEntryTemplate;

/**
 * Created by 傅令杰 on 2017/4/22
 */
@SuppressWarnings("unused")
@PayEntryGenerator(
        packageName = "com.gxysj.shareysj",
        payEntryTemplate = WXPayEntryTemplate.class
)
public interface WeChatPayEntry {
}
