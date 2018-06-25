package com.gxysj.shareysj.delegate.main.home.adapter;

import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gxysj.shareysj.R;
import com.gxysj.shareysj.bean.GetAppHomeInfoBean;
import com.gxysj.shareysj.network.HttpConstants;

import java.util.List;

/**
 * Created by 林佳荣 on 2018/4/2.
 * Github：https://github.com/ljrRookie
 * Function ：
 */

public class HomeItemAdapter extends BaseQuickAdapter<GetAppHomeInfoBean.Data2Bean ,BaseViewHolder>{
    public HomeItemAdapter(int layoutId, List<GetAppHomeInfoBean.Data2Bean> data2) {
        super(layoutId,data2);
    }

    @Override
    protected void convert(BaseViewHolder helper, GetAppHomeInfoBean.Data2Bean item) {
        ImageView image = helper.getView(R.id.iv_icon);
        Glide.with(mContext).load(HttpConstants.ROOT_URL + item.getImage()).placeholder(R.mipmap.bg_icon).error(R.mipmap.bg_icon).dontAnimate().into(image);
        helper.setText(R.id.tv_title, item.getName());
    }
}
