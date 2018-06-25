package com.gxysj.shareysj.generators;


import com.example.annotation.EntryGenerator;
import com.gxysj.shareysj.wechat.temp.WXEntryTemplate;

/**
 * Created by 傅令杰 on 2017/4/22
 */

@SuppressWarnings("unused")
@EntryGenerator(
        packageName = "com.gxysj.shareysj",
        entryTemplate = WXEntryTemplate.class
)
public interface WeChatEntry {
}
