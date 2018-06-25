package com.gxysj.shareysj.listener;

import android.view.View;

import com.gxysj.shareysj.bean.MachineGoodBean;

/**
 * Created by 林佳荣 on 2018/3/29.
 * Github：https://github.com/ljrRookie
 * Function ：
 */

public interface ShopCartItemInterface {
    void ItemAdd(View view, int postion, MachineGoodBean.DataBean dataBean);
    void ItemRemove(View view, int postion,MachineGoodBean.DataBean dataBean);
}
