package com.gxysj.shareysj.delegate.main.mine.order.adapter;

import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gxysj.shareysj.R;
import com.gxysj.shareysj.bean.UserOrderListBean;
import com.gxysj.shareysj.network.HttpConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 林佳荣 on 2018/3/22.
 * Github：https://github.com/ljrRookie
 * Function ：
 */

public class OrderItemAdapter extends BaseQuickAdapter<UserOrderListBean.DataBean.ListBean,BaseViewHolder>{
    public OrderItemAdapter(int layoutId, List<UserOrderListBean.DataBean.ListBean> items) {
        super(layoutId,items);

    }

    @Override
    protected void convert(BaseViewHolder helper, UserOrderListBean.DataBean.ListBean item) {
        String commodity_image = item.getCommodity_image();
        ImageView image = helper.getView(R.id.iv_pic);
        Glide.with(mContext).load(HttpConstants.ROOT_URL + commodity_image).placeholder(R.mipmap.bg_shop).error(R.mipmap.bg_shop).dontAnimate().into(image);
        String commodity_name = item.getCommodity_name();
        helper.setText(R.id.tv_name, commodity_name);
        helper.setText(R.id.tv_count, "数量：" + item.getNubs());
        helper.setText(R.id.tv_price,"总价：¥"+item.getMoney());
    }
}
