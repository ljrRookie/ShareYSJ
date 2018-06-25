package com.gxysj.shareysj.delegate.main.mine.ad.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.allen.library.SuperTextView;
import com.bumptech.glide.Glide;
import com.gxysj.shareysj.R;
import com.gxysj.shareysj.bean.ADPayBean;
import com.gxysj.shareysj.bean.UserAdBean;
import com.gxysj.shareysj.bean.event.EventEmpty;
import com.gxysj.shareysj.delegate.main.mine.MyADDelegate;
import com.gxysj.shareysj.network.HttpConstants;
import com.vondear.rxtools.view.dialog.RxDialog;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 林佳荣 on 2018/3/29.
 * Github：https://github.com/ljrRookie
 * Function ：
 */

public class MyADAdapter extends RecyclerView.Adapter<MyADAdapter.ViewHolder> {
    private final Context mContext;
    private final List<UserAdBean.DataBean> mData;
    private final MyADDelegate mDelegate;
    private final RxDialog mDialog;


    public MyADAdapter(Context context, List<UserAdBean.DataBean> data, MyADDelegate delegate) {
        this.mContext = context;
        this.mData = data;
        this.mDelegate = delegate;
        mDialog = new RxDialog(mContext);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_ad, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.mBtnCommit.setVisibility(View.GONE);
        holder.mBtnCommit.setText("立即支付");
        final UserAdBean.DataBean dataBean = mData.get(position);
        holder.mTvTitle.setText(dataBean.getTitle());
        holder.mTvContent.setText(dataBean.getContent());
        holder.mTvTime.setText(dataBean.getTime());
        String stateStr = dataBean.getState();
        final int state = Integer.valueOf(stateStr);
        final String money_state = dataBean.getMoney_state();
        String image = dataBean.getImage();
        Log.e("广告", image);
        if (!image.contains(",")) {
            Glide.with(mContext).load(HttpConstants.ROOT_URL + image).into(holder.mIvPic);
        } else {
            String imageResult = image.substring(0, image.indexOf(","));
            Glide.with(holder.mIvPic.getContext()).load(HttpConstants.ROOT_URL + imageResult).into(holder.mIvPic);
            Log.e("广告", "广告列表图片：" + HttpConstants.ROOT_URL + imageResult);
        }
        if (state == 0) {
            if (money_state.equals("0")) {
                holder.mBtnCommit.setVisibility(View.VISIBLE);
                holder.mBtnAgain.setVisibility(View.GONE);
                holder.mBtnCommit.setText("立即支付");
                holder.mTvState.setText("待付款");
            } else {
                holder.mBtnCommit.setVisibility(View.GONE);
                holder.mBtnAgain.setVisibility(View.GONE);
                holder.mTvState.setText("待审核");
            }
        } else if (state == 1) {
            holder.mTvState.setText("已发布");
        } else if (state == 2) {
            holder.mTvState.setText("驳回");
            holder.mBtnCommit.setVisibility(View.VISIBLE);
            holder.mBtnAgain.setVisibility(View.VISIBLE);

            holder.mBtnCommit.setText("查看原因");
        } else if (state == 3) {
            holder.mTvState.setText("已过期");
        }
        holder.mBtnAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserAdBean.DataBean dataOnClick = mData.get(position);
                EventBus.getDefault().post(dataOnClick);
            }
        });
        holder.mBtnCommit.setOnClickListener(new BtnOnClick(position));
        holder.mRlItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserAdBean.DataBean dataOnClick = mData.get(position);
                EventBus.getDefault().post(new EventEmpty(dataOnClick.getId(), null));

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
        @BindView(R.id.tv_state)
        TextView mTvState;
        @BindView(R.id.iv_pic)
        ImageView mIvPic;
        @BindView(R.id.tv_title)
        TextView mTvTitle;
        @BindView(R.id.tv_content)
        TextView mTvContent;
        @BindView(R.id.rl_item)
        LinearLayout mRlItem;
        @BindView(R.id.btn_commit)
        TextView mBtnCommit;
        @BindView(R.id.btn_again)
        TextView mBtnAgain;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private class BtnOnClick implements View.OnClickListener {
        private final int mPosition;

        public BtnOnClick(int position) {
            mPosition = position;
        }

        @Override
        public void onClick(View v) {
            UserAdBean.DataBean dataOnClick = mData.get(mPosition);
            String stateStr = dataOnClick.getState();
            final int state = Integer.valueOf(stateStr);
            final String money_state = dataOnClick.getMoney_state();

            if (state == 0) {
                if (money_state.equals("0")) {
                    //付款
                    double money = dataOnClick.getMoney();
                    String uuid = dataOnClick.getUuid();
                    EventBus.getDefault().post(new ADPayBean(money, uuid, 1000));

                }
            } else if (state == 2) {
                //审核不通过
                mDialog.getLayoutParams().gravity = Gravity.CENTER;
                View dialogView = LayoutInflater.from(mContext).inflate(R.layout.dialog_ad_reason, null);
                TextView tvTitle = (TextView) dialogView.findViewById(R.id.tv_reason);
                tvTitle.setText(dataOnClick.getFeedback());
                SuperTextView btnCommit = (SuperTextView) dialogView.findViewById(R.id.btn_commit);
                btnCommit.setCenterString("确认");
                btnCommit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDialog.dismiss();
                    }
                });
                mDialog.setContentView(dialogView);
                mDialog.show();
            }
        }
    }
}
