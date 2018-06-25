package com.gxysj.shareysj.delegate.main.generalize;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gxysj.shareysj.R;
import com.gxysj.shareysj.bean.BounsDetail;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 林佳荣 on 2018/4/3.
 * Github：https://github.com/ljrRookie
 * Function ：
 */

class BounsDetailAdapter extends RecyclerView.Adapter<BounsDetailAdapter.ViewHolder> {

    private final Context mContext;
    private final List<BounsDetail.DataBean> mData;


    public BounsDetailAdapter(Context context, List<BounsDetail.DataBean> data) {
        this.mContext = context;
        this.mData = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bouns_detail, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        BounsDetail.DataBean dataBean = mData.get(position);
        holder.mTvTitile.setText(dataBean.getName());
        holder.mTvMoney.setText("+"+dataBean.getMoney());
        holder.mTvTime.setText("时间："+dataBean.getTime());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_titile)
        TextView mTvTitile;
        @BindView(R.id.tv_time)
        TextView mTvTime;
        @BindView(R.id.tv_money)
        TextView mTvMoney;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
