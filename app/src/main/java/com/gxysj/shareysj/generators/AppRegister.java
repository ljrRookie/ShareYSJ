package com.gxysj.shareysj.generators;


import com.example.annotation.AppRegisterGenerator;
import com.gxysj.shareysj.wechat.temp.AppRegisterTemplate;

/**
 * Created by 傅令杰 on 2017/4/22
 */
@SuppressWarnings("unused")
@AppRegisterGenerator(
        packageName = "com.gxysj.shareysj",
        registerTemplate = AppRegisterTemplate.class
)
public interface AppRegister {
}
