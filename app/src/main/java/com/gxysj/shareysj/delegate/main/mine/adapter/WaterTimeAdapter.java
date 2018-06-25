package com.gxysj.shareysj.delegate.main.mine.adapter;

import android.support.design.widget.TextInputEditText;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.services.route.TaxiItem;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gxysj.shareysj.R;
import com.gxysj.shareysj.bean.UseWaterTime;
import com.gxysj.shareysj.network.RequestCenter;
import com.gxysj.shareysj.utils.user_manager.Utils;
import com.ljr.delegate_sdk.okhttp.exception.OkHttpException;
import com.ljr.delegate_sdk.okhttp.listener.DisposeDataListener;
import com.ljr.delegate_sdk.okhttp.request.RequestParams;
import com.vondear.rxtools.view.RxToast;
import com.vondear.rxtools.view.dialog.RxDialog;

import java.util.List;

/**
 * Created by 林佳荣 on 2018/4/16.
 * Github：https://github.com/ljrRookie
 * Function ：
 */

public class WaterTimeAdapter extends BaseQuickAdapter<UseWaterTime, BaseViewHolder> {
    private final RxDialog mDialog;

    public WaterTimeAdapter(int layoutId, List<UseWaterTime> title, RxDialog dialog) {
        super(layoutId, title);
        this.mDialog= dialog;
    }

    @Override
    protected void convert(BaseViewHolder helper, final UseWaterTime item) {
        int position = helper.getLayoutPosition();
        String name = item.getName();
        RelativeLayout rlBg = helper.getView(R.id.rl_bg);
        helper.setText(R.id.tv_title,name);
        helper.setText(R.id.btn_hot, "热");
        if (position == 0) {
            rlBg.setBackgroundResource(R.mipmap.bg_water_time_c);
            helper.setText(R.id.btn_hot, "温");
        } else if (position == 1) {
            rlBg.setBackgroundResource(R.mipmap.bg_water_time_b);

        } else if (position == 2) {
            rlBg.setBackgroundResource(R.drawable.bg_water_time_a);
        }
        helper.addOnClickListener(R.id.btn_cool);
        helper.addOnClickListener(R.id.btn_hot);

    }


}
