package com.gxysj.shareysj.delegate.main.home.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gxysj.shareysj.R;
import com.gxysj.shareysj.bean.ADPriceBean;
import com.gxysj.shareysj.bean.RedEnvelopesTypeBean;

import java.util.List;

/**
 * Created by Administrator on 2018/2/3.
 */

public class RedPriceAdapter extends BaseQuickAdapter<RedEnvelopesTypeBean.DataBean,BaseViewHolder>{
    public RedPriceAdapter(int layoutId, List<RedEnvelopesTypeBean.DataBean> adPrice) {
        super(layoutId,adPrice);

    }

    @Override
    protected void convert(BaseViewHolder helper, RedEnvelopesTypeBean.DataBean item) {

        helper.setText(R.id.tv_price,item.getMoney()+"元\n+"+item.getNub()+"次");
    }
}
