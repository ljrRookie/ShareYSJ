package com.gxysj.shareysj.delegate.main.home.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gxysj.shareysj.R;
import com.gxysj.shareysj.bean.MachineGoodBean;
import com.gxysj.shareysj.listener.ShopCartInterface;
import com.gxysj.shareysj.listener.ShopCartItemInterface;
import com.gxysj.shareysj.network.HttpConstants;
import com.vondear.rxtools.RxDataTool;
import com.vondear.rxtools.view.RxToast;

import java.util.List;

/**
 * Created by 林佳荣 on 2018/3/5.
 * Github：https://github.com/ljrRookie
 * Function ：
 */

public class ShopCartAdapter extends BaseQuickAdapter<MachineGoodBean.DataBean, BaseViewHolder> {
    private ShopCartItemInterface shopCartItemImp;

    public ShopCartAdapter(List<MachineGoodBean.DataBean> datas, int layoutId) {
        super(layoutId, datas);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final MachineGoodBean.DataBean item) {
        final int position = helper.getLayoutPosition();
        helper.setText(R.id.tv_name, item.getName());
        double price = Double.parseDouble(item.getPrice1());
        int count = item.getCount();
        double finalPrice = price * count;
        String amountValue = RxDataTool.getAmountValue(finalPrice);
        helper.setText(R.id.tv_price, "¥" + amountValue);
        final ImageView iv_remove = helper.getView(R.id.btn_remove);
        ImageView iv_add = helper.getView(R.id.btn_add);
        final TextView tv_account = helper.getView(R.id.tv_account);
        tv_account.setText(String.valueOf(count));
        iv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String stockStr = item.getStock();
                int stock = Integer.valueOf(stockStr);
                final int count = Integer.valueOf(tv_account.getText().toString());
                if(count+1<=stock){
                    iv_remove.setVisibility(View.VISIBLE);
                    tv_account.setVisibility(View.VISIBLE);
                    tv_account.setText(String.valueOf(count+1));
                    if(shopCartItemImp!=null) {
                        shopCartItemImp.ItemAdd(v, position,mData.get(position));
                    }
                }else{
                    RxToast.normal("库存不足");
                }

            }
        });
        iv_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int count = Integer.valueOf(tv_account.getText().toString());

                if(count>=1){
                    tv_account.setText(String.valueOf(count - 1));
                    if(shopCartItemImp!=null) {
                        shopCartItemImp.ItemRemove(v, position,mData.get(position));
                    }
                    if(count-1 == 0){
                        mData.remove(position);
                        notifyDataSetChanged();
                    }

                }

            }
        });

    }
   public ShopCartItemInterface getShopCartItemInterface() {
        return shopCartItemImp;
    }

    public void setShopCartItemInterface(ShopCartItemInterface shopCartItemImp) {
        this.shopCartItemImp = shopCartItemImp;
    }
}
