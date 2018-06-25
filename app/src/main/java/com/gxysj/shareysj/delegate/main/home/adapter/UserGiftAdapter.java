package com.gxysj.shareysj.delegate.main.home.adapter;

import com.allen.library.SuperButton;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gxysj.shareysj.R;
import com.gxysj.shareysj.bean.GiftBagBean;
import com.gxysj.shareysj.bean.UserGiftBean;
import com.vondear.rxtools.view.RxTextAutoZoom;

import java.util.List;

/**
 * Created by 林佳荣 on 2018/6/13.
 * Github：https://github.com/ljrRookie
 * Function ：
 */

public class UserGiftAdapter extends BaseQuickAdapter<UserGiftBean.DateBean, BaseViewHolder> {
    public UserGiftAdapter(int layoutId, List<UserGiftBean.DateBean> date) {
        super(layoutId, date);
    }

    @Override
    protected void convert(BaseViewHolder helper, UserGiftBean.DateBean item) {
        RxTextAutoZoom tvName = helper.getView(R.id.tv_name);
        tvName.setText(item.getName());
        SuperButton mBtnGet = helper.getView(R.id.btn_get);
        mBtnGet.setText("立刻使用");
        helper.addOnClickListener(R.id.btn_get);
    }
}
