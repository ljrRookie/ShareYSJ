package com.gxysj.shareysj.delegate.main.home;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.allen.library.SuperTextView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gxysj.shareysj.R;
import com.gxysj.shareysj.bean.ADPriceBean;
import com.gxysj.shareysj.bean.GetAdvertisementBean;
import com.gxysj.shareysj.delegate.base.YSJDelegate;
import com.gxysj.shareysj.delegate.main.home.ad.AdForPayDelegate;
import com.gxysj.shareysj.delegate.main.home.ad.MoreAdDelegate;
import com.gxysj.shareysj.delegate.main.home.adapter.AdListAdapter;
import com.gxysj.shareysj.delegate.main.home.adapter.AdPriceAdapter;
import com.gxysj.shareysj.delegate.main.mine.ad.AdInfoDelegate;
import com.gxysj.shareysj.network.RequestCenter;
import com.ljr.delegate_sdk.okhttp.exception.OkHttpException;
import com.ljr.delegate_sdk.okhttp.listener.DisposeDataListener;
import com.ljr.delegate_sdk.okhttp.request.RequestParams;
import com.vondear.rxtools.view.RxToast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


/**
 * Created by Administrator on 2018/2/3.
 */

public class AdDelegate extends YSJDelegate {
    @BindView(R.id.btn_back)
    ImageView mBtnBack;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.rc_price)
    RecyclerView mRcPrice;
    @BindView(R.id.btn_more)
    SuperTextView mBtnMore;
    @BindView(R.id.rc_ad)
    RecyclerView mRcAd;
    Unbinder unbinder;
    @BindView(R.id.btn_right)
    ImageView mBtnRight;
    @BindView(R.id.tv_flag)
    SuperTextView mTvFlag;
    @BindView(R.id.tv_empty)
    TextView mTvEmpty;
    Unbinder unbinder1;
    private List<String> mAdPrice;
    private List<String> mAdList;

    @Override
    public Object setLayout() {
        return R.layout.delegate_home_ad;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        mTvTitle.setText("发布广告");
        initView();
        initData();
    }

    private void initView() {
        mRcPrice.setNestedScrollingEnabled(false);
        mRcAd.setNestedScrollingEnabled(false);
        mRcPrice.setLayoutManager(new GridLayoutManager(getContext(), 3));
        mRcAd.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void initData() {
        RequestCenter.GetAdvertisementPriceList(new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                ADPriceBean adPriceBean = (ADPriceBean) responseObj;
                final List<ADPriceBean.DataBean> data = adPriceBean.getData();
                AdPriceAdapter adPriceAdapter = new AdPriceAdapter(R.layout.item_home_ad_price, data);
                adPriceAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                        ADPriceBean.DataBean dataBean = data.get(position);
                        int id = dataBean.getId();
                        String day_time = dataBean.getDay_time();
                        String money = dataBean.getMoney();
                        getSupportDelegate().start(AdForPayDelegate.create(id,day_time,money));
                    }
                });
                adPriceAdapter.openLoadAnimation();
                mRcPrice.setAdapter(adPriceAdapter);
            }

            @Override
            public void onFailure(Object responseObj) {
                OkHttpException error = (OkHttpException) responseObj;
                RxToast.normal("获取广告价格失败-(" + error.getEmsg() + ")");
            }
        });
        RequestParams params = new RequestParams();
        params.put("count", 0);
        params.put("number", 10);
        params.put("type", 0);
        params.put("value", "");
        params.put("key", 0);
        RequestCenter.GetAdvertisementList(params, new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                mRcAd.setVisibility(View.VISIBLE);
                mTvEmpty.setVisibility(View.GONE);
                GetAdvertisementBean getAdvertisementBean = (GetAdvertisementBean) responseObj;
                final List<GetAdvertisementBean.DataBean> data = getAdvertisementBean.getData();
                if (data != null && data.size() > 0) {
                    final AdListAdapter adListAdapter = new AdListAdapter(R.layout.item_home_ad_list, data);
                    adListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                                    GetAdvertisementBean.DataBean dataBean1 = data.get(position);
                                    String id = dataBean1.getId();
                                    getSupportDelegate().start(AdInfoDelegate.create(id));

                        }
                    });
                    adListAdapter.openLoadAnimation();
                    mRcAd.setAdapter(adListAdapter);
                } else {
                    mRcAd.setVisibility(View.GONE);
                    mTvEmpty.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onFailure(Object responseObj) {
                OkHttpException error = (OkHttpException) responseObj;
                RxToast.normal("获取广告数据失败-(" + error.getEmsg() + ")");
                mRcAd.setVisibility(View.GONE);
                mTvEmpty.setVisibility(View.VISIBLE);
            }
        });
    }

    @OnClick({R.id.btn_back, R.id.btn_more})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                getSupportDelegate().pop();
                break;
            case R.id.btn_more:
                getSupportDelegate().start(new MoreAdDelegate());
                break;
        }
    }


}
