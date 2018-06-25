package com.gxysj.shareysj.delegate.home;

import android.graphics.Color;

import com.gxysj.shareysj.R;
import com.gxysj.shareysj.bean.BottomTabBean;
import com.gxysj.shareysj.delegate.main.generalize.MyGeneralizeDelegate;
import com.gxysj.shareysj.delegate.main.home.HomeDelegate;
import com.gxysj.shareysj.delegate.main.mine.MineDelegate;

import java.util.LinkedHashMap;

/**
 * Created by Administrator on 2018/1/29.
 */

public class BottomDelegate extends BaseBottomDelegate {
    @Override
    public LinkedHashMap<BottomTabBean, BottomItemDelegate> setItems(ItemBuilder builder) {
        final LinkedHashMap<BottomTabBean,BottomItemDelegate> items = new LinkedHashMap<>();
        items.put(new BottomTabBean(R.mipmap.icon_home,"首页"),new HomeDelegate());
        items.put(new BottomTabBean(R.mipmap.icon_generalize,"我的推广"),new MyGeneralizeDelegate());
        items.put(new BottomTabBean(R.mipmap.icon_mine,"个人中心"),new MineDelegate());
        return builder.addItems(items).build();
    }

    @Override
    public int setIndexDelegate() {
        return 0;
    }

    @Override
    public int setClickedColor() {
        return Color.parseColor("#0096ff");
    }


}
