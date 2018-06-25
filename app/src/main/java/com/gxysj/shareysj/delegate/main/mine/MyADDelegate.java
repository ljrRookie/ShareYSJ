package com.gxysj.shareysj.delegate.main.mine;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gxysj.shareysj.R;
import com.gxysj.shareysj.bean.ADPayBean;
import com.gxysj.shareysj.bean.UserAdBean;
import com.gxysj.shareysj.bean.event.EventEmpty;
import com.gxysj.shareysj.delegate.PayDelegate;
import com.gxysj.shareysj.delegate.base.YSJDelegate;
import com.gxysj.shareysj.delegate.main.mine.ad.AdAgainDelegate;
import com.gxysj.shareysj.delegate.main.mine.ad.AdInfoDelegate;
import com.gxysj.shareysj.delegate.main.mine.ad.AllADDelegate;
import com.gxysj.shareysj.delegate.main.mine.adapter.TabAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by 林佳荣 on 2018/3/9.
 * Github：https://github.com/ljrRookie
 * Function ：
 */

public class MyADDelegate extends YSJDelegate {
    @BindView(R.id.btn_back)
    ImageView mBtnBack;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.btn_right)
    ImageView mBtnRight;
    @BindView(R.id.tab_ad)
    TabLayout mTabAd;
    @BindView(R.id.vp_order)
    ViewPager mVpOrder;
    private List<YSJDelegate> mDelegate;
    private int PayResult = 1000;

    @Override
    public Object setLayout() {
        return R.layout.delegate_mine_my_ad;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        mTvTitle.setText("我的广告");
        initViewPager();
        EventBus.getDefault().register(this);
    }

    private void initViewPager() {
        ArrayList<String> titles = new ArrayList<>();
        titles.add("全部");
        titles.add("待付款");
        titles.add("待审核");
        titles.add("已发布");
        titles.add("驳回");
        titles.add("过期");
        for (int i = 0; i < titles.size(); i++) {
            mTabAd.addTab(mTabAd.newTab().setText(titles.get(i)));
        }
        mDelegate = new ArrayList<>();
        mDelegate.add(AllADDelegate.create(-1));
        mDelegate.add(AllADDelegate.create(-2));
        mDelegate.add(AllADDelegate.create(0));
        mDelegate.add(AllADDelegate.create(1));
        mDelegate.add(AllADDelegate.create(2));
        mDelegate.add(AllADDelegate.create(3));
        TabAdapter tabAdapter = new TabAdapter(getFragmentManager(), mDelegate, titles);
        mVpOrder.setAdapter(tabAdapter);
        mVpOrder.setOffscreenPageLimit(2);
        mTabAd.setTabMode(TabLayout.MODE_SCROLLABLE);
        mTabAd.setTabsFromPagerAdapter(tabAdapter);
        mTabAd.setupWithViewPager(mVpOrder);


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void toAdInfo(EventEmpty messageEvent) {
        getSupportDelegate().start(AdInfoDelegate.create(messageEvent.getStr()));
        Log.e("fragment", "toAdInfo");

    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void toAgainAd(UserAdBean.DataBean dataBean) {
        getSupportDelegate().pop();
        getSupportDelegate().start(AdAgainDelegate.create(dataBean));
        Log.e("fragment", "toAdInfo");

    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void toAdPay(ADPayBean bean) {
       // getSupportDelegate().startForResult(PayDelegate.create(bean.getMoney(),bean.getOrderid(),bean.getTypeId()),PayResult);
        getSupportDelegate().pop();
        getSupportDelegate().start(PayDelegate.create(bean.getMoney(),bean.getOrderid(),bean.getTypeId()));
        Log.e("fragment", "toAdPay");

    }
    @OnClick(R.id.btn_back)
    public void onViewClicked() {
        getSupportDelegate().pop();
    }


 /*   @Override
    public void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);
        if (requestCode == PayResult && resultCode == RESULT_OK) {
            // 在此通过Bundle data 获取返回的数据
          initViewPager();
            Log.e("fragment", "payresult回调");
        }

    }*/
}
