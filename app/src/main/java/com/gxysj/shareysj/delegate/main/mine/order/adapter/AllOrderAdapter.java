package com.gxysj.shareysj.delegate.main.mine.order.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.allen.library.SuperTextView;
import com.gxysj.shareysj.R;
import com.gxysj.shareysj.bean.UserOrderListBean;
import com.gxysj.shareysj.delegate.main.mine.OrderDelegate;
import com.gxysj.shareysj.delegate.main.mine.order.OrderInfoDelegate;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 林佳荣 on 2018/3/6.
 * Github：https://github.com/ljrRookie
 * Function ：
 */

public class AllOrderAdapter extends RecyclerView.Adapter<AllOrderAdapter.ViewHolder> {
    private final Context mContext;
    private final List<UserOrderListBean.DataBean> mData;
    private final OrderDelegate mDelegate;



    public AllOrderAdapter(Context context, List<UserOrderListBean.DataBean> datas, OrderDelegate delegate) {
        this.mContext = context;
        this.mData = datas;
        this.mDelegate = delegate;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_all, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        UserOrderListBean.DataBean dataBean = mData.get(position);
        String time = dataBean.getTime();
        holder.mTvInfo.setLeftString(time);
        List<UserOrderListBean.DataBean.ListBean> list = dataBean.getList();
        holder.mItemorder.setLayoutManager(new LinearLayoutManager(mContext));
        holder.mItemorder.setAdapter(new OrderItemAdapter(R.layout.item_order_all_item,list));

        holder.mItemorder.setNestedScrollingEnabled(false);
        holder.mBtnInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDelegate.getSupportDelegate().start(OrderInfoDelegate.create(mData.get(position).getId()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_info)
        SuperTextView mTvInfo;
        @BindView(R.id.itemorder)
        RecyclerView mItemorder;
        @BindView(R.id.btn_info)
        TextView mBtnInfo;
        @BindView(R.id.ll_item)
        LinearLayout mLlItem;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
