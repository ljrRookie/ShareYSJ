package com.gxysj.shareysj.delegate.main.home.ad;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gxysj.shareysj.R;
import com.gxysj.shareysj.bean.GetAdvertisementBean;
import com.gxysj.shareysj.delegate.base.YSJDelegate;
import com.gxysj.shareysj.delegate.main.generalize.adapter.MyDownLineAdapter;
import com.gxysj.shareysj.delegate.main.home.adapter.MoreAdAdapter;
import com.gxysj.shareysj.network.RequestCenter;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
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
 * Created by 林佳荣 on 2018/3/24.
 * Github：https://github.com/ljrRookie
 * Function ：
 */

public class MoreAdDelegate extends YSJDelegate {
    @BindView(R.id.btn_back)
    ImageView mBtnBack;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.btn_right)
    ImageView mBtnRight;
    @BindView(R.id.rv_ad_more)
    XRecyclerView mRvAdMore;
    @BindView(R.id.tv_empty)
    TextView mTvEmpty;

    private int mCount = 1;
    private int mNumber = 10;
    private MoreAdAdapter mMoreAdapter;
    private List<GetAdvertisementBean.DataBean> mData;

    @Override
    public Object setLayout() {
        return R.layout.delegate_home_ad_more;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        mTvTitle.setText("更多广告");
        initRecyclerView();
        initData();
    }

    private void initRecyclerView() {
        mRvAdMore.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRvAdMore.setRefreshProgressStyle(ProgressStyle.BallClipRotateMultiple);
        mRvAdMore.setLoadingMoreProgressStyle(ProgressStyle.BallScaleRippleMultiple);
        mRvAdMore.setArrowImageView(R.drawable.icon_downgrey);
        mRvAdMore.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                initData();
                mRvAdMore.setLoadingMoreEnabled(true);
                mRvAdMore.refreshComplete();
            }

            @Override
            public void onLoadMore() {
                MoreData();
            }
        });
    }

    private void MoreData() {
        RequestParams params = new RequestParams();
        params.put("count", mCount);
        params.put("number", mNumber);
        params.put("type", 1);
        params.put("value", "");
        params.put("key", 0);
        RequestCenter.GetAdvertisementList(params, new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                GetAdvertisementBean getAdvertisementBean = (GetAdvertisementBean) responseObj;
                List<GetAdvertisementBean.DataBean> moreData = getAdvertisementBean.getData();
                if(moreData !=null&& moreData.size()>0){
                    for (int i = 0; i < moreData.size(); i++) {
                        mData.add(moreData.get(i));
                    }
                    mMoreAdapter.notifyItemChanged(moreData.size());
                    mCount++;
                    mRvAdMore.loadMoreComplete();

                }else{
                    mRvAdMore.setLoadingMoreEnabled(false);
                    RxToast.normal("加载完成");
                }

            }

            @Override
            public void onFailure(Object responseObj) {
                RxToast.normal("加载失败！");
            }
        });
    }

    private void initData() {
        RequestParams params = new RequestParams();
        params.put("count", 0);
        params.put("number", mNumber);
        params.put("type", 1);
        params.put("value", "");
        params.put("key", 0);
        RequestCenter.GetAdvertisementList(params, new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                GetAdvertisementBean getAdvertisementBean = (GetAdvertisementBean) responseObj;
                mData = getAdvertisementBean.getData();
                if (mData !=null & mData.size()>0) {
                    if(mData.size()<mNumber){
                        mRvAdMore.setLoadingMoreEnabled(false);
                    }
                    mRvAdMore.setVisibility(View.VISIBLE);
                    mTvEmpty.setVisibility(View.GONE);
                    mMoreAdapter = new MoreAdAdapter(getActivity(), mData, MoreAdDelegate.this);
                    mRvAdMore.setAdapter(mMoreAdapter);
                    if(mData.size()<mNumber){
                        mRvAdMore.setPullRefreshEnabled(false);
                    }
                }else{
                    mRvAdMore.setVisibility(View.GONE);
                    mTvEmpty.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onFailure(Object responseObj) {
                OkHttpException error = (OkHttpException) responseObj;
                RxToast.normal("获取广告数据失败-(" + error.getEmsg() + ")");
                mRvAdMore.setVisibility(View.GONE);
                mTvEmpty.setVisibility(View.VISIBLE);
            }
        });

    }

    @OnClick(R.id.btn_back)
    public void onViewClicked() {
        getSupportDelegate().pop();

    }

}
