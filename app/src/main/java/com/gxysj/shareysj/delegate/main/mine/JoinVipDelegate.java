package com.gxysj.shareysj.delegate.main.mine;

import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.allen.library.SuperButton;
import com.gxysj.shareysj.R;
import com.gxysj.shareysj.delegate.PayDelegate;
import com.gxysj.shareysj.delegate.base.YSJDelegate;
import com.vondear.rxtools.view.RxToast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by 林佳荣 on 2018/6/13.
 * Github：https://github.com/ljrRookie
 * Function ：
 */

public class JoinVipDelegate extends YSJDelegate {
    @BindView(R.id.tv_money)
    TextView mTvMoney;
    @BindView(R.id.btn_commit)
    SuperButton mBtnCommit;
    @BindView(R.id.btn_back)
    SuperButton mBtnBack;
    private String mMoney = "";

    public static JoinVipDelegate create(String money) {
        JoinVipDelegate joinVipDelegate = new JoinVipDelegate();
        Bundle bundle = new Bundle();
        bundle.putString("money", money);
        joinVipDelegate.setArguments(bundle);
        return joinVipDelegate;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            mMoney = arguments.getString("money");
        }
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_mine_join_vip;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        if (mMoney.isEmpty()) {
            RxToast.normal("获取数据失败，请稍后重试");
        } else {
            mTvMoney.setText(mMoney + "元(永久有效)");
        }
    }

    @OnClick({R.id.btn_commit, R.id.btn_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_commit:
                getSupportDelegate().start(PayDelegate.create(Double.parseDouble(mMoney),"",PayDelegate.PayJoinVip));
                break;
            case R.id.btn_back:
                getSupportDelegate().pop();
                break;
        }
    }
}
