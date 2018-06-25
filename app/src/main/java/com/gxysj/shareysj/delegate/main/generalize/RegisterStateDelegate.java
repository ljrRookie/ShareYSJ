package com.gxysj.shareysj.delegate.main.generalize;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gxysj.shareysj.R;
import com.gxysj.shareysj.delegate.base.YSJDelegate;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by 林佳荣 on 2018/3/21.
 * Github：https://github.com/ljrRookie
 * Function ：申请加盟商审核
 */

public class RegisterStateDelegate extends YSJDelegate {
    @BindView(R.id.btn_cancel)
    ImageView mBtnCancel;
    @BindView(R.id.tv_flag)
    TextView mTvFlag;
    Unbinder unbinder;

    @Override
    public Object setLayout() {
        return R.layout.delegate_generalize_alliance_check;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {

    }



    @OnClick(R.id.btn_cancel)
    public void onViewClicked() {
        getSupportDelegate().pop();
    }
}
