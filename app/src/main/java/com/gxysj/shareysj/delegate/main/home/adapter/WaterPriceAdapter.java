package com.gxysj.shareysj.delegate.main.home.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gxysj.shareysj.R;
import com.gxysj.shareysj.bean.WaterMoneyListBean;

import java.util.List;

/**
 * Created by 林佳荣 on 2018/4/4.
 * Github：https://github.com/ljrRookie
 * Function ：
 */

public class WaterPriceAdapter extends BaseQuickAdapter<WaterMoneyListBean.DataBean,BaseViewHolder>{
    public WaterPriceAdapter(int layoutId, List<WaterMoneyListBean.DataBean> data) {
        super(layoutId,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, WaterMoneyListBean.DataBean item) {
        helper.setText(R.id.tv_price,item.getMoney()+"元\n+"+item.getNumber()+"次");
    }
}
