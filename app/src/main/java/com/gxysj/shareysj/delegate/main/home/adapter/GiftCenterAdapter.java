package com.gxysj.shareysj.delegate.main.home.adapter;

import android.view.View;

import com.allen.library.SuperButton;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gxysj.shareysj.R;
import com.gxysj.shareysj.bean.GiftBagBean;
import com.vondear.rxtools.view.RxTextAutoZoom;

import java.util.List;

/**
 * Created by 林佳荣 on 2018/6/13.
 * Github：https://github.com/ljrRookie
 * Function ：
 */

public class GiftCenterAdapter extends BaseQuickAdapter<GiftBagBean.DateBean, BaseViewHolder> {
    public GiftCenterAdapter(int layoutId, List<GiftBagBean.DateBean> date) {
        super(layoutId, date);
    }

    @Override
    protected void convert(BaseViewHolder helper, GiftBagBean.DateBean item) {
        RxTextAutoZoom tvName = helper.getView(R.id.tv_name);
        tvName.setText(item.getName());
        SuperButton mBtnGet = helper.getView(R.id.btn_get);
        String state = item.getState();
        if (state.equals("0")) {
            //未领取
            mBtnGet.setShapeGradientEndColor(mContext.getResources().getColor(R.color.orange_low)).setUseShape();
            mBtnGet.setShapeGradientStartColor(mContext.getResources().getColor(R.color.orange_high)).setUseShape();
            mBtnGet.setText("立刻领取");
        }else{
            //已领取
            mBtnGet.setShapeGradientEndColor(mContext.getResources().getColor(R.color.gray_button)).setUseShape();
            mBtnGet.setShapeGradientStartColor(mContext.getResources().getColor(R.color.gray_button)).setUseShape();
            mBtnGet.setText("已领取");
        }
        helper.addOnClickListener(R.id.btn_get);
    }
}
