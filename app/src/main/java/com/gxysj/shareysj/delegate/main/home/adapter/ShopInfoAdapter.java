package com.gxysj.shareysj.delegate.main.home.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gxysj.shareysj.R;
import com.gxysj.shareysj.bean.ShopOrderBean;
import com.gxysj.shareysj.delegate.main.mine.detail.adapter.DetailAdapter;
import com.gxysj.shareysj.network.HttpConstants;
import com.vondear.rxtools.RxDataTool;

import java.util.List;

/**
 * Created by 林佳荣 on 2018/4/8.
 * Github：https://github.com/ljrRookie
 * Function ：
 */

public class ShopInfoAdapter extends BaseQuickAdapter<ShopOrderBean.DataBean,BaseViewHolder>{
    public ShopInfoAdapter(int layoutId, List<ShopOrderBean.DataBean> data) {
        super(layoutId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ShopOrderBean.DataBean item) {
        ImageView image = helper.getView(R.id.iv_pic);
        Glide.with(mContext).load(HttpConstants.ROOT_URL + item.getImage()).placeholder(R.mipmap.bg_shop).error(R.mipmap.bg_shop).dontAnimate().into(image);
        helper.setText(R.id.tv_name, item.getName());
        String money = item.getMoney();
        double moneyItem = Double.parseDouble(money);
        double totalMoney = moneyItem * item.getNub();
        helper.setText(R.id.tv_price, "¥" +  RxDataTool.getAmountValue(totalMoney));
        helper.setText(R.id.tv_count, "X" + item.getNub());
    }
}
