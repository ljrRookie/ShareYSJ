package com.gxysj.shareysj.delegate.main.generalize.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gxysj.shareysj.R;
import com.gxysj.shareysj.bean.GetCityBean;

import java.util.List;

/**
 * Created by 林佳荣 on 2018/5/26.
 * Github：https://github.com/ljrRookie
 * Function ：
 */

public class InvestCityAdapter extends BaseQuickAdapter<GetCityBean.DataBean,BaseViewHolder>{
    public InvestCityAdapter(int layoutId, List<GetCityBean.DataBean> data) {
        super(layoutId,data);
    }


    @Override
    protected void convert(BaseViewHolder helper, GetCityBean.DataBean item) {
        helper.setText(R.id.tv_name, item.getCity());
    }
}
