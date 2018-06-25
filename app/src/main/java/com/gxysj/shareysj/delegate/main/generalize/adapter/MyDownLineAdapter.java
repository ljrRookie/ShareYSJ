package com.gxysj.shareysj.delegate.main.generalize.adapter;

import android.content.Context;
import android.media.Image;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gxysj.shareysj.R;
import com.gxysj.shareysj.bean.MyOfflineBean;
import com.gxysj.shareysj.delegate.main.mine.detail.adapter.DetailAdapter;
import com.gxysj.shareysj.network.HttpConstants;
import com.gxysj.shareysj.ui.GlideCircleTransform;
import com.gxysj.shareysj.ui.GlideRoundTransform;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by 林佳荣 on 2018/3/21.
 * Github：https://github.com/ljrRookie
 * Function ：
 */

public class MyDownLineAdapter extends RecyclerView.Adapter<MyDownLineAdapter.ViewHolder> {
    private final Context mContext;
    private final List<MyOfflineBean.DataBean> mData;


    public MyDownLineAdapter(Context context, List<MyOfflineBean.DataBean> datas) {
        this.mContext = context;
        this.mData = datas;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_generalize_my_downline, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MyOfflineBean.DataBean dataBean = mData.get(position);
        holder.mTvName.setText(dataBean.getName());
        holder.mTvTime.setText("注册使用时间："+dataBean.getTime());
        Glide.with(mContext).load(HttpConstants.ROOT_URL + dataBean.getImage()).transform(new GlideCircleTransform(mContext)).dontAnimate().into(holder.mIvPic);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_pic)
        ImageView mIvPic;
        @BindView(R.id.tv_name)
        TextView mTvName;
        @BindView(R.id.tv_time)
        TextView mTvTime;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }
}
