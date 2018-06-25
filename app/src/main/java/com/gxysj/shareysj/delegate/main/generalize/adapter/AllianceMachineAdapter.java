package com.gxysj.shareysj.delegate.main.generalize.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amap.api.col.sln3.da;
import com.gxysj.shareysj.R;
import com.gxysj.shareysj.bean.UserInvestmentMachineBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 林佳荣 on 2018/4/9.
 * Github：https://github.com/ljrRookie
 * Function ：
 */

public class AllianceMachineAdapter extends RecyclerView.Adapter<AllianceMachineAdapter.ViewHolder> {
    private final Context mContext;
    private final List<UserInvestmentMachineBean.DataBean> mData;


    public AllianceMachineAdapter(Context context, List<UserInvestmentMachineBean.DataBean> data) {
        this.mContext = context;
        this.mData = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_machine_info, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        UserInvestmentMachineBean.DataBean dataBean = mData.get(position);
        holder.mTvCity.setText("位置："+dataBean.getCity());
        holder.mTvMoney.setText("投资："+ dataBean.getMoney()+"元");
        holder.mTvTime.setText("时间："+dataBean.getTime());
        holder.mTvTitile.setText("机箱："+dataBean.getName());
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
        @BindView(R.id.tv_city)
        TextView mTvCity;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
