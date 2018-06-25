package com.gxysj.shareysj.delegate.main.generalize;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gxysj.shareysj.R;
import com.gxysj.shareysj.bean.MyOfflineBean;
import com.gxysj.shareysj.config.ShareYSJ;
import com.gxysj.shareysj.delegate.base.YSJDelegate;
import com.gxysj.shareysj.delegate.main.generalize.adapter.MyDownLineAdapter;
import com.gxysj.shareysj.network.RequestCenter;
import com.gxysj.shareysj.utils.SPUtil;
import com.gxysj.shareysj.utils.TimeUtils;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.ljr.delegate_sdk.okhttp.exception.OkHttpException;
import com.ljr.delegate_sdk.okhttp.listener.DisposeDataListener;
import com.ljr.delegate_sdk.okhttp.request.RequestParams;
import com.vondear.rxtools.RxLogTool;
import com.vondear.rxtools.RxTimeTool;
import com.vondear.rxtools.view.RxToast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by 林佳荣 on 2018/3/21.
 * Github：https://github.com/ljrRookie
 * Function ：
 */

public class MyDownLineDelegate extends YSJDelegate {
    @BindView(R.id.btn_back)
    ImageView mBtnBack;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.btn_right)
    ImageView mBtnRight;
    @BindView(R.id.rv_downline)
    XRecyclerView mRvDownline;
    @BindView(R.id.tv_empty)
    TextView mTvEmpty;
    private int mPage = 1;
    private int mCount = 15;
    private List<MyOfflineBean.DataBean> mData;
    private MyDownLineAdapter mMyDownLineAdapter;

    @Override
    public Object setLayout() {
        return R.layout.delegate_generalize_downline;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        mTvTitle.setText("我的下线");
        initData();
    }

    private void initData() {
        long time = RxTimeTool.getCurTimeMills();
        String timeStr = String.valueOf(time);
        String secretKey = TimeUtils.getSecretKey(timeStr);
        final RequestParams requestParams = new RequestParams();
        requestParams.put("userid", SPUtil.get(ShareYSJ.getApplicationContext(), "userId", ""));
        requestParams.put("token", SPUtil.get(ShareYSJ.getApplicationContext(), "token", ""));
        requestParams.put("secret_key", secretKey);
        requestParams.put("time", timeStr);
        requestParams.put("count", 0);
        requestParams.put("number", mCount);
        RequestCenter.UserGetOfflineList(requestParams, new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                MyOfflineBean myOffline = (MyOfflineBean) responseObj;
                mData = myOffline.getData();
                if (mData != null && mData.size() > 0) {
                    mTvEmpty.setVisibility(View.GONE);
                    mRvDownline.setVisibility(View.VISIBLE);
                    mMyDownLineAdapter = new MyDownLineAdapter(getActivity(), mData);
                    mRvDownline.setAdapter(mMyDownLineAdapter);
                } else {
                    mTvEmpty.setVisibility(View.VISIBLE);
                    mRvDownline.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Object responseObj) {
                OkHttpException error = (OkHttpException) responseObj;
                mTvEmpty.setVisibility(View.VISIBLE);
                mRvDownline.setVisibility(View.GONE);
                mTvEmpty.setText("获取数据失败。\n（" + error.getEmsg() + ")");
            }
        });

    }


    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        initRecyclerView();
        initLoadingListener();
    }

    private void initLoadingListener() {
        mRvDownline.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                RxToast.normal("下拉刷新");
                initData();
                mPage = 1;
                mRvDownline.setLoadingMoreEnabled(true);
                mRvDownline.refreshComplete();
            }

            @Override
            public void onLoadMore() {
                MoreData();
            }
        });
    }

    private void MoreData() {
        long time = RxTimeTool.getCurTimeMills();
        String timeStr = String.valueOf(time);
        String secretKey = TimeUtils.getSecretKey(timeStr);
        final RequestParams requestParams = new RequestParams();
        requestParams.put("userid", SPUtil.get(ShareYSJ.getApplicationContext(), "userId", ""));
        requestParams.put("token", SPUtil.get(ShareYSJ.getApplicationContext(), "token", ""));
        requestParams.put("secret_key", secretKey);
        requestParams.put("time", timeStr);
        requestParams.put("count", mPage);
        requestParams.put("number", mCount);

        RequestCenter.UserGetOfflineList(requestParams,new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                MyOfflineBean myOffline = (MyOfflineBean) responseObj;
                List<MyOfflineBean.DataBean> moreData = myOffline.getData();
                if (moreData != null && moreData.size() > 0) {
                    for (int i = 0; i < moreData.size(); i++) {
                        mData.add(moreData.get(i));
                    }
                    mMyDownLineAdapter.notifyItemChanged(moreData.size());
                    mPage++;
                    mRvDownline.loadMoreComplete();
                } else {
                    mRvDownline.setLoadingMoreEnabled(false);
                    RxToast.normal("加载完成");
                }
            }

            @Override
            public void onFailure(Object responseObj) {
                RxToast.normal("加载失败！");

            }
        });
    }

    private void initRecyclerView() {
        mRvDownline.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRvDownline.setRefreshProgressStyle(ProgressStyle.BallClipRotateMultiple);
        mRvDownline.setLoadingMoreProgressStyle(ProgressStyle.BallScaleRippleMultiple);
        mRvDownline.setArrowImageView(R.drawable.icon_downgrey);

    }

    @OnClick(R.id.btn_back)
    public void onViewClicked() {
        getSupportDelegate().pop();
    }
}
