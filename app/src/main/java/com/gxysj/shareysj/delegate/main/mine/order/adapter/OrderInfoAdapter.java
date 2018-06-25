package com.gxysj.shareysj.delegate.main.mine.order.adapter;

import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gxysj.shareysj.R;
import com.gxysj.shareysj.bean.GetOrderInfoBean;
import com.gxysj.shareysj.listener.IUserChecker;
import com.gxysj.shareysj.network.HttpConstants;

import java.util.List;

/**
 * Created by 林佳荣 on 2018/3/30.
 * Github：https://github.com/ljrRookie
 * Function ：
 */

public class OrderInfoAdapter extends BaseQuickAdapter<GetOrderInfoBean.ListBean,BaseViewHolder>{
    public OrderInfoAdapter(int layoutId, List<GetOrderInfoBean.ListBean> list) {
        super(layoutId,list);
    }

    @Override
    protected void convert(BaseViewHolder helper, GetOrderInfoBean.ListBean item) {
        ImageView image = helper.getView(R.id.iv_pic);
        Glide.with(mContext).load(HttpConstants.ROOT_URL + item.getCommodity_image()).placeholder(R.mipmap.bg_shop).error(R.mipmap.bg_shop).dontAnimate().into(image);
        helper.setText(R.id.tv_name, item.getCommodity_name());
        helper.setText(R.id.tv_price, "¥" + item.getMoney());
        helper.setText(R.id.tv_count, "X" + item.getNubs());
    }
}
