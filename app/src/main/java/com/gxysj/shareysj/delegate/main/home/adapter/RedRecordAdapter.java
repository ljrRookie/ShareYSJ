package com.gxysj.shareysj.delegate.main.home.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gxysj.shareysj.R;
import com.gxysj.shareysj.bean.UserRedLogBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 林佳荣 on 2018/4/11.
 * Github：https://github.com/ljrRookie
 * Function ：
 */

public class RedRecordAdapter extends RecyclerView.Adapter<RedRecordAdapter.ViewHolder> {
    private final Context mContext;
    private final List<UserRedLogBean.DataBean> mData;

    public RedRecordAdapter(Context context, List<UserRedLogBean.DataBean> data) {
        this.mContext = context;
        this.mData = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_red_log, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        UserRedLogBean.DataBean dataBean = mData.get(position);
        String money = dataBean.getMoney();
        if(money.equals("0")){
            holder.mTvMoney.setText("未中奖");
        }else{
            holder.mTvMoney.setText("+"+money);
        }

        holder.mTvTime.setText(dataBean.getTime());
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
        @BindView(R.id.tv_money)
        TextView mTvMoney;
        @BindView(R.id.rl_item)
        RelativeLayout mRlItem;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
