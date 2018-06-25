package com.gxysj.shareysj.delegate.main.mine.detail.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gxysj.shareysj.R;
import com.gxysj.shareysj.bean.UserTransactionDetailedBean;
import com.gxysj.shareysj.bean.event.EventStartFragment;
import com.gxysj.shareysj.delegate.main.mine.detail.AllDetailDelegate;
import com.gxysj.shareysj.delegate.main.mine.order.OrderInfoDelegate;
import com.vondear.rxtools.RxLogTool;
import com.vondear.rxtools.view.RxToast;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 林佳荣 on 2018/3/6.
 * Github：https://github.com/ljrRookie
 * Function ：
 */

public class DetailAdapter extends RecyclerView.Adapter<DetailAdapter.ViewHolder> {

    private final Context mContext;
    private final List<UserTransactionDetailedBean.DataBean> mData;
    private final AllDetailDelegate mDelegate;



    public DetailAdapter(Context context, List<UserTransactionDetailedBean.DataBean> datas, AllDetailDelegate delegate) {
        this.mContext = context;
        this.mData = datas;
        this.mDelegate = delegate;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_detail_all, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        UserTransactionDetailedBean.DataBean dataBean = mData.get(position);
            String money = dataBean.getMoney();
            holder.mTvTitle.setText(dataBean.getName());
            holder.mTvTime.setText(dataBean.getTime());
            int type = dataBean.getType();
            if(type==1){
                holder.mIvPic.setImageResource(R.mipmap.icon_pay);
                holder.mTvPrice.setText("-"+money);
            }else{
                holder.mIvPic.setImageResource(R.mipmap.icon_recharge);
                holder.mTvPrice.setText("+"+money);
            }




      /* holder.mLlMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new EventStartFragment("跳转了"));
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_pic)
        ImageView mIvPic;
        @BindView(R.id.tv_title)
        TextView mTvTitle;
        @BindView(R.id.tv_time)
        TextView mTvTime;
        @BindView(R.id.tv_price)
        TextView mTvPrice;
        @BindView(R.id.ll_main)
        RelativeLayout mLlMain;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
