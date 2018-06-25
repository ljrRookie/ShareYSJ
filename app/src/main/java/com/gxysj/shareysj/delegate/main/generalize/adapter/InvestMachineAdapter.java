package com.gxysj.shareysj.delegate.main.generalize.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.TextView;

import com.allen.library.SuperButton;
import com.allen.library.SuperTextView;
import com.gxysj.shareysj.R;
import com.gxysj.shareysj.bean.FranchiserInfoBean;
import com.gxysj.shareysj.bean.InvestMachineBean;
import com.gxysj.shareysj.delegate.PayDelegate;
import com.gxysj.shareysj.delegate.main.generalize.InvestMachineDelegate;
import com.vondear.rxtools.view.RxToast;
import com.vondear.rxtools.view.dialog.RxDialog;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 林佳荣 on 2018/4/16.
 * Github：https://github.com/ljrRookie
 * Function ：
 */

public class InvestMachineAdapter extends RecyclerView.Adapter<InvestMachineAdapter.ViewHolder> {

    private final Context mContext;
    private final List<InvestMachineBean.DataBean> mData;
    private final RxDialog mDialog;
    private final InvestMachineDelegate mDelegate;


    public InvestMachineAdapter(Context context, List<InvestMachineBean.DataBean> data, InvestMachineDelegate investMachineDelegate) {
        this.mContext = context;
        this.mData = data;
        this.mDelegate = investMachineDelegate;
        mDialog = new RxDialog(mContext);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_invest_machine, parent, false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        InvestMachineBean.DataBean dataBean = mData.get(position);
        holder.mTvName.setText(dataBean.getName());
        holder.mTvNum.setText("已投资人数：" + dataBean.getCount());
        holder.mTvCity.setText("城市："+dataBean.getCity());
        final String money = dataBean.getMoney();
        holder.mTvMoney.setText("剩余可投资金额："+money);
        holder.mTvTime.setText("日期："+dataBean.getTime());
        if(money.equals("0")){
            holder.mBtnInvest.setClickable(false);
            holder.mBtnInvest.setShapeGradientEndColor(mContext.getResources().getColor(R.color.gray_button)).setUseShape();
            holder.mBtnInvest.setShapeGradientStartColor(mContext.getResources().getColor(R.color.gray_button)).setUseShape();
            } else {
            holder.mBtnInvest.setClickable(true);
            holder.mBtnInvest.setShapeGradientEndColor(mContext.getResources().getColor(R.color.blue_low)).setUseShape();
            holder.mBtnInvest.setShapeGradientStartColor(mContext.getResources().getColor(R.color.blue_high)).setUseShape();
            }
            holder.mBtnInvest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    InvestMachineBean.DataBean dataBean = mData.get(position);
                    final String money1 = dataBean.getMoney();
                    final String id = dataBean.getId();
                    if(!money1.equals("0")){
                        mDialog.getLayoutParams().gravity = Gravity.CENTER;
                        View dialogView = LayoutInflater.from(mContext).inflate(R.layout.dialog_money, null);
                        TextView title = (TextView) dialogView.findViewById(R.id.tv_title);
                        title.setText("投资金额");
                        SuperTextView btn_commit = (SuperTextView) dialogView.findViewById(R.id.btn_commit);
                        final EditText editMoney = (EditText) dialogView.findViewById(R.id.edit_money);
                        editMoney.setHint("可投资金额："+money1);
                        btn_commit.setOnSuperTextViewClickListener(new SuperTextView.OnSuperTextViewClickListener() {
                            @Override
                            public void onClickListener(SuperTextView superTextView) {
                                String money = editMoney.getText().toString().trim();

                                if (TextUtils.isEmpty(money)) {
                                    RxToast.normal("投资的金额不能为空");
                                    return;
                                }
                                if(money.equals("0")){
                                    RxToast.normal("投资的金额不能零");
                                    return;
                                }
                                double douMoney = Double.parseDouble(money);
                                if(douMoney > Double.parseDouble(money1)){
                                    RxToast.normal("投资金额大于可投资金额");
                                }else{
                                    mDelegate.getSupportDelegate().start(PayDelegate.create(douMoney,id,1004));
                                    mDialog.cancel();
                                }
                                }
                        });
                        mDialog.setContentView(dialogView);
                        mDialog.show();
                    }

                }
            });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_name)
        TextView mTvName;
        @BindView(R.id.tv_num)
        TextView mTvNum;
        @BindView(R.id.tv_city)
        TextView mTvCity;
        @BindView(R.id.tv_money)
        TextView mTvMoney;
        @BindView(R.id.tv_time)
        TextView mTvTime;
        @BindView(R.id.btn_invest)
        SuperButton mBtnInvest;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
