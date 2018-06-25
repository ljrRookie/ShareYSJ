package com.gxysj.shareysj.delegate.main.home.adapter;

import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gxysj.shareysj.R;
import com.gxysj.shareysj.bean.GetAdvertisementBean;
import com.gxysj.shareysj.delegate.main.mine.ad.AdInfoDelegate;
import com.gxysj.shareysj.network.HttpConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/2/3.
 */

public class AdListAdapter extends BaseQuickAdapter<GetAdvertisementBean.DataBean,BaseViewHolder>{
    public AdListAdapter(int layoutId, List<GetAdvertisementBean.DataBean> adList) {
        super(layoutId,adList);
    }

    @Override
    protected void convert(BaseViewHolder helper, GetAdvertisementBean.DataBean item) {
        helper.setText(R.id.tv_time, item.getTime());
        String image = item.getImage();
        ImageView imageView = helper.getView(R.id.iv_pic);
        if(!image.contains(",")){
            Glide.with(mContext).load(HttpConstants.ROOT_URL + image).placeholder(R.mipmap.bg_empty).error(R.mipmap.bg_empty).into(imageView);
        }else{
            String imageResult = image.substring(0, image.indexOf(","));
            Glide.with(mContext).load(HttpConstants.ROOT_URL + imageResult).placeholder(R.mipmap.bg_empty).error(R.mipmap.bg_empty).into(imageView);
        }
        helper.setText(R.id.tv_title, item.getTitle());
        helper.setText(R.id.tv_content, item.getContent());

    }
}
