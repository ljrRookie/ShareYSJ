package com.gxysj.shareysj.delegate;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.allen.library.SuperButton;
import com.allen.library.SuperTextView;
import com.gxysj.shareysj.R;
import com.gxysj.shareysj.bean.MachineInfoBean;
import com.gxysj.shareysj.bean.ShopOrderBean;
import com.gxysj.shareysj.delegate.base.YSJDelegate;
import com.gxysj.shareysj.delegate.main.home.adapter.ShopInfoAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by 林佳荣 on 2018/4/8.
 * Github：https://github.com/ljrRookie
 * Function ：
 */

public class ShopInfoDelegate extends YSJDelegate {

    @BindView(R.id.btn_back)
    ImageView mBtnBack;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.rv_shop)
    RecyclerView mRvShop;
    @BindView(R.id.tv_order)
    SuperTextView mTvOrder;
    @BindView(R.id.shopping_cart_total_tv)
    TextView mShoppingCartTotalTv;
    @BindView(R.id.btn_pay)
    SuperButton mBtnPay;
    @BindView(R.id.shopping_cart_bottom)
    RelativeLayout mShoppingCartBottom;
    Unbinder unbinder;
    private ShopOrderBean mData = null;

    @Override
    public Object setLayout() {
        return R.layout.delegate_shop_info;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        mTvTitle.setText("商品详情");
        initData();
        mTvOrder.setCenterString(mData.getOrderid());
        mShoppingCartTotalTv.setText("¥"+mData.getMoney());
    }

    private void initData() {
        mRvShop.setLayoutManager(new LinearLayoutManager(getActivity()));
        List<ShopOrderBean.DataBean> data = mData.getData();
        ShopInfoAdapter shopInfoAdapter = new ShopInfoAdapter(R.layout.item_order_info, data);
        shopInfoAdapter.openLoadAnimation();
        mRvShop.setAdapter(shopInfoAdapter);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            mData = (ShopOrderBean) arguments.getSerializable("data");
        }
    }

    /**
     * 手机点点物入口
     * @param shopOrder
     * @return
     */
    public static ShopInfoDelegate create(ShopOrderBean shopOrder) {
        ShopInfoDelegate shopInfoDelegate = new ShopInfoDelegate();
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", shopOrder);
        shopInfoDelegate.setArguments(bundle);
        return shopInfoDelegate;
    }
/*
    public static ShopInfoDelegate create(MachineInfoBean machineInfoBean) {
        ShopInfoDelegate shopInfoDelegate = new ShopInfoDelegate();
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", machineInfoBean);
        shopInfoDelegate.setArguments(bundle);
        return shopInfoDelegate;
    }*/
    @OnClick({R.id.btn_back, R.id.btn_pay})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                getSupportDelegate().pop();
                break;
            case R.id.btn_pay:
                double money = mData.getMoney();
                String orderidNum = mData.getOrderid();
                getSupportDelegate().pop();
                getSupportDelegate().start(PayDelegate.create(money, orderidNum, 1001));

                break;
        }
    }
}
