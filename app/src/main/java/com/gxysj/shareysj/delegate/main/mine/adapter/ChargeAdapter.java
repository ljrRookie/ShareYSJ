package com.gxysj.shareysj.delegate.main.mine.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gxysj.shareysj.R;
import com.gxysj.shareysj.bean.ChargingInfo;

import java.util.List;

/**
 * Created by 林佳荣 on 2018/4/9.
 * Github：https://github.com/ljrRookie
 * Function ：
 */

public class ChargeAdapter extends BaseQuickAdapter<ChargingInfo.DataBean,BaseViewHolder>{
    public ChargeAdapter(int layoutId, List<ChargingInfo.DataBean> data) {
        super(layoutId,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ChargingInfo.DataBean item) {
        int layoutPosition = helper.getLayoutPosition();
        helper.setText(R.id.tv_item, layoutPosition+1 + "、" + item.getName());
    }
}
