package com.gxysj.shareysj.delegate.main.home.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gxysj.shareysj.R;
import com.gxysj.shareysj.bean.ADPriceBean;

import java.util.List;

/**
 * Created by Administrator on 2018/2/3.
 */

public class AdPriceAdapter extends BaseQuickAdapter<ADPriceBean.DataBean,BaseViewHolder>{
    public AdPriceAdapter(int layoutId, List<ADPriceBean.DataBean> adPrice) {
        super(layoutId,adPrice);

    }

    @Override
    protected void convert(BaseViewHolder helper, ADPriceBean.DataBean item) {

        helper.setText(R.id.tv_price,item.getMoney()+"\n"+item.getDay_time());
    }
}
