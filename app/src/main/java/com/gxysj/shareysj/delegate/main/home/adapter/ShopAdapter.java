package com.gxysj.shareysj.delegate.main.home.adapter;

import android.graphics.Paint;
import android.media.Image;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gxysj.shareysj.R;
import com.gxysj.shareysj.bean.MachineGoodBean;
import com.gxysj.shareysj.listener.ShopCartInterface;
import com.gxysj.shareysj.network.HttpConstants;
import com.vondear.rxtools.RxLogTool;
import com.vondear.rxtools.view.RxToast;

import java.util.List;

/**
 * Created by 林佳荣 on 2018/3/5.
 * Github：https://github.com/ljrRookie
 * Function ：
 */

public class ShopAdapter extends BaseQuickAdapter<MachineGoodBean.DataBean, BaseViewHolder> {
    private ShopCartInterface shopCartImp;

    public ShopAdapter(List<MachineGoodBean.DataBean> datas, int layoutId) {
        super(layoutId, datas);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final MachineGoodBean.DataBean item) {
        final int position = helper.getLayoutPosition();
        final ImageView iv_remove = helper.getView(R.id.right_dish_remove);
        ImageView iv_add = helper.getView(R.id.right_dish_add);
        final TextView tv_account = helper.getView(R.id.right_dish_account);
        helper.setText(R.id.tv_total, "库存:" + item.getStock());
        int count = item.getCount();
        if (count > 0) {
            iv_remove.setVisibility(View.VISIBLE);
            iv_add.setVisibility(View.VISIBLE);
            tv_account.setVisibility(View.VISIBLE);
            tv_account.setText(count + "");
        } else {
            iv_remove.setVisibility(View.GONE);
            iv_add.setVisibility(View.VISIBLE);
            tv_account.setVisibility(View.GONE);
        }
        helper.setText(R.id.right_dish_name, item.getName());
        ImageView image = helper.getView(R.id.iv_pic);
        Glide.with(mContext).load(HttpConstants.ROOT_URL + item.getImage()).placeholder(R.mipmap.bg_shop).error(R.mipmap.bg_shop).dontAnimate().into(image);
        helper.setText(R.id.tv_price_old, "编号：" + item.getUuid());

        helper.setText(R.id.tv_price, "¥" + item.getPrice1());


        iv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String stockStr = item.getStock();
                int stock = Integer.valueOf(stockStr);
                final int count = Integer.valueOf(tv_account.getText().toString());
                if (count + 1 <= stock) {
                    iv_remove.setVisibility(View.VISIBLE);
                    tv_account.setVisibility(View.VISIBLE);
                    tv_account.setText(String.valueOf(count + 1));
                    if (shopCartImp != null) {
                        shopCartImp.add(v, position);
                    }
                } else {
                    RxToast.normal("库存不足");
                }
            }
        });
        iv_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int count = Integer.valueOf(tv_account.getText().toString());

                if (count >= 1) {
                    tv_account.setText(String.valueOf(count - 1));
                    if (count - 1 == 0) {
                        iv_remove.setVisibility(View.GONE);
                        tv_account.setVisibility(View.GONE);
                    }
                    if (shopCartImp != null) {
                        shopCartImp.remove(v, position);
                    }
                }

            }
        });

    }

    public ShopCartInterface getShopCartInterface() {
        return shopCartImp;
    }

    public void setShopCartInterface(ShopCartInterface shopCartImp) {
        this.shopCartImp = shopCartImp;
    }
}
