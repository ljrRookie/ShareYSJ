package com.gxysj.shareysj.delegate.main.mine;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gxysj.shareysj.R;
import com.gxysj.shareysj.bean.event.EventStartFragment;
import com.gxysj.shareysj.delegate.base.YSJDelegate;
import com.gxysj.shareysj.delegate.main.mine.adapter.TabAdapter;
import com.gxysj.shareysj.delegate.main.mine.detail.AllDetailDelegate;
import com.gxysj.shareysj.delegate.main.mine.detail.TypeADetailDelegate;
import com.gxysj.shareysj.delegate.main.mine.detail.TypeBDetailDelegate;
import com.gxysj.shareysj.delegate.main.mine.detail.TypeCDetailDelegate;
import com.gxysj.shareysj.delegate.main.mine.order.OrderInfoDelegate;
import com.vondear.rxtools.view.RxToast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * Created by 林佳荣 on 2018/3/6.
 * Github：https://github.com/ljrRookie
 * Function ：
 */

public class TransactionDetailDelegate extends YSJDelegate {
    @BindView(R.id.btn_back)
    ImageView mBtnBack;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.tab_order)
    TabLayout mTabOrder;
    @BindView(R.id.vp_order)
    ViewPager mVpOrder;
    private List<YSJDelegate> mDelegate;


    @Override
    public Object setLayout() {
        return R.layout.delegate_mine_transaction_detail;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        mTvTitle.setText("交易明细");
        EventBus.getDefault().register(this);
        initViewPager();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void startFragment(EventStartFragment eventStartFragment) {
        getSupportDelegate().start(new OrderInfoDelegate());
    }

    private void initViewPager() {
        ArrayList<String> titles = new ArrayList<>();
        titles.add("提现");
        titles.add("饮水");
        titles.add("商品");
        titles.add("充值");
        titles.add("广告");
        titles.add("其他");
        //为Tab添加标题
        for (int i = 0; i < titles.size(); i++) {
            mTabOrder.addTab(mTabOrder.newTab().setText(titles.get(i)));
        }

        mDelegate = new ArrayList<>();
        mDelegate.add(AllDetailDelegate.create(1));
        mDelegate.add(AllDetailDelegate.create(2));
        mDelegate.add(AllDetailDelegate.create(3));
        mDelegate.add(AllDetailDelegate.create(4));
        mDelegate.add(AllDetailDelegate.create(6));
        mDelegate.add(AllDetailDelegate.create(5));
        TabAdapter tagFragmentAdapter = new TabAdapter(getFragmentManager(), mDelegate, titles);
        mVpOrder.setAdapter(tagFragmentAdapter);
        mVpOrder.setOffscreenPageLimit(4);
        mTabOrder.setTabMode(TabLayout.MODE_SCROLLABLE);
        mTabOrder.setTabsFromPagerAdapter(tagFragmentAdapter);
        mTabOrder.setupWithViewPager(mVpOrder);
    }


    @OnClick(R.id.btn_back)
    public void onViewClicked() {
        getSupportDelegate().pop();
    }
}
