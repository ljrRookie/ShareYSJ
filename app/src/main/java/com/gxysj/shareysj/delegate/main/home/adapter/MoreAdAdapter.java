package com.gxysj.shareysj.delegate.main.home.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gxysj.shareysj.R;
import com.gxysj.shareysj.bean.GetAdvertisementBean;
import com.gxysj.shareysj.delegate.base.YSJDelegate;
import com.gxysj.shareysj.delegate.main.home.ad.MoreAdDelegate;
import com.gxysj.shareysj.delegate.main.mine.ad.AdInfoDelegate;
import com.gxysj.shareysj.network.HttpConstants;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 林佳荣 on 2018/4/2.
 * Github：https://github.com/ljrRookie
 * Function ：
 */

public class MoreAdAdapter extends RecyclerView.Adapter<MoreAdAdapter.ViewHolder> {

    private final Context mContext;
    private final List<GetAdvertisementBean.DataBean> mData;
    private final YSJDelegate mDelegate;



    public MoreAdAdapter(Context context, List<GetAdvertisementBean.DataBean> data, YSJDelegate delegate) {
        this.mContext = context;
        this.mData = data;
        this.mDelegate = delegate;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_ad_list, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        GetAdvertisementBean.DataBean dataBean = mData.get(position);
        holder.mTvTime.setText(dataBean.getTime());
        holder.mTvTitle.setText(dataBean.getTitle());
        holder.mTvContent.setText(dataBean.getContent());
        String image = dataBean.getImage();
        if (!image.contains(",")) {
            Glide.with(mContext).load(HttpConstants.ROOT_URL + image).placeholder(R.mipmap.bg_empty).error(R.mipmap.bg_empty).into(holder.mIvPic);
        } else {
            String imageResult = image.substring(0, image.indexOf(","));
            Glide.with(mContext).load(HttpConstants.ROOT_URL + imageResult).placeholder(R.mipmap.bg_empty).error(R.mipmap.bg_empty).into(holder.mIvPic);
        }

    holder.mRlItem.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            GetAdvertisementBean.DataBean dataBean1 = mData.get(position);
            String id = dataBean1.getId();
            mDelegate.getSupportDelegate().start(AdInfoDelegate.create(id));
        }
    });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_time)
        TextView mTvTime;
        @BindView(R.id.iv_pic)
        ImageView mIvPic;
        @BindView(R.id.tv_title)
        TextView mTvTitle;
        @BindView(R.id.tv_content)
        TextView mTvContent;
        @BindView(R.id.rl_item)
        RelativeLayout mRlItem;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
