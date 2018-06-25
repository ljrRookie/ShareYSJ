package com.gxysj.shareysj.delegate.main.mine.adapter;

import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gxysj.shareysj.R;
import com.gxysj.shareysj.bean.GetAppHomeInfoBean;
import com.gxysj.shareysj.delegate.main.home.HomeDelegate;
import com.gxysj.shareysj.network.HttpConstants;
import com.gxysj.shareysj.ui.GlideRoundTransform;

import java.util.List;

/**
 * Created by 林佳荣 on 2018/4/4.
 * Github：https://github.com/ljrRookie
 * Function ：
 */

public class ReChargeAdapter extends BaseQuickAdapter<GetAppHomeInfoBean.Data1Bean,BaseViewHolder>{
    public ReChargeAdapter(int layoutId, List<GetAppHomeInfoBean.Data1Bean> data1) {
        super(layoutId,data1);
    }

    @Override
    protected void convert(BaseViewHolder helper, GetAppHomeInfoBean.Data1Bean item) {
        ImageView image = helper.getView(R.id.iv_pic);
        Glide.with(mContext).load(HttpConstants.ROOT_URL + item.getImage()).transform(new GlideRoundTransform(mContext, 8)).placeholder(R.mipmap.bg_a).error(R.mipmap.bg_a).into(image);
        helper.setText(R.id.tv_money, item.getMoney() + "元\n" + item.getNumber() + "次饮水次数");
    }


}
