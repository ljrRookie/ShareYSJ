package com.gxysj.shareysj.listener;

import android.view.View;

import com.gxysj.shareysj.bean.UserAdBean;

/**
 * Created by 林佳荣 on 2018/3/29.
 * Github：https://github.com/ljrRookie
 * Function ：
 */

public interface MyAdInterface {
    void itemOnClick(UserAdBean.DataBean dataBean);
    void otherOnClick(UserAdBean.DataBean dataBean);
}
