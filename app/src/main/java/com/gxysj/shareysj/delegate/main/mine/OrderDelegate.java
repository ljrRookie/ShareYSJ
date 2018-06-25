package com.gxysj.shareysj.delegate.main.mine;

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
import com.gxysj.shareysj.bean.UserOrderListBean;
import com.gxysj.shareysj.config.ShareYSJ;
import com.gxysj.shareysj.delegate.base.YSJDelegate;
import com.gxysj.shareysj.delegate.main.mine.order.adapter.AllOrderAdapter;
import com.gxysj.shareysj.network.RequestCenter;
import com.gxysj.shareysj.utils.SPUtil;
import com.gxysj.shareysj.utils.TimeUtils;
import com.gxysj.shareysj.utils.user_manager.Utils;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.ljr.delegate_sdk.okhttp.exception.OkHttpException;
import com.ljr.delegate_sdk.okhttp.listener.DisposeDataListener;
import com.ljr.delegate_sdk.okhttp.request.RequestParams;
import com.vondear.rxtools.RxTimeTool;
import com.vondear.rxtools.view.RxToast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by 林佳荣 on 2018/3/5.
 * Github：https://github.com/ljrRookie
 * Function ：订单页面
 */

public class OrderDelegate extends YSJDelegate {
    @BindView(R.id.btn_back)
    ImageView mBtnBack;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.rv_order)
    XRecyclerView mRvOrder;
    @BindView(R.id.tv_empty)
    TextView mTvEmpty;

    //页数
    private int mPage = 1;
    //每页数据量
    private int mCount = 10;
    private AllOrderAdapter mAllOrderAdapter;
    private List<UserOrderListBean.DataBean> mData;

    @Override
    public Object setLayout() {
        return R.layout.delegate_mine_order;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        mTvTitle.setText("商品订单");
        initRecyclerView();
        initLoadingListener();
        initData();
    }

    private void initRecyclerView() {
        mRvOrder.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRvOrder.setRefreshProgressStyle(ProgressStyle.BallClipRotateMultiple);
        mRvOrder.setLoadingMoreProgressStyle(ProgressStyle.BallScaleRippleMultiple);
        mRvOrder.setArrowImageView(R.drawable.icon_downgrey);
    }

    private void initLoadingListener() {
        mRvOrder.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                mPage = 1;
                mRvOrder.setLoadingMoreEnabled(true);
                initData();
                mRvOrder.refreshComplete();
            }

            @Override
            public void onLoadMore() {
                MoreData();
            }
        });
    }

    private void MoreData() {
        RequestParams requestParams = Utils.getRequestParams();
        requestParams.put("count", mPage);
        requestParams.put("number", mCount);
        RequestCenter.GetUserOrderList(requestParams, new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                UserOrderListBean userOrderBean = (UserOrderListBean) responseObj;
                List<UserOrderListBean.DataBean> moreData = userOrderBean.getData();
                if(moreData != null && moreData.size()>0){
                    for (int i = 0; i < moreData.size(); i++) {
                        mData.add(moreData.get(i));
                    }
                    mAllOrderAdapter.notifyItemChanged(moreData.size());
                    mPage++;
                    mRvOrder.loadMoreComplete();
                }else{
                    mRvOrder.setLoadingMoreEnabled(false);
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
        RequestCenter.GetUserOrderList(requestParams, new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                UserOrderListBean userOrderBean = (UserOrderListBean) responseObj;
                mData = userOrderBean.getData();
                if(mData != null && mData.size()>0){
                    mRvOrder.setVisibility(View.VISIBLE);
                    mTvEmpty.setVisibility(View.GONE);
                    mRvOrder.setLayoutManager(new LinearLayoutManager(getActivity()));
                    mAllOrderAdapter = new AllOrderAdapter(getActivity(), mData, OrderDelegate.this);
                    mRvOrder.setAdapter(mAllOrderAdapter);
                }else{
                    mRvOrder.setVisibility(View.GONE);
                    mTvEmpty.setVisibility(View.VISIBLE);
                    mTvEmpty.setText("哎呀，还没有任何订单啊！");
                }
            }

            @Override
            public void onFailure(Object responseObj) {
                OkHttpException error = (OkHttpException) responseObj;
                RxToast.normal("获取数据错误（"+error.getEmsg()+"）");
                mRvOrder.setVisibility(View.GONE);
                mTvEmpty.setVisibility(View.VISIBLE);
                mTvEmpty.setText("获取数据失败，请稍后重试");
            }
        });


    }

    @OnClick(R.id.btn_back)
    public void onViewClicked() {
        getSupportDelegate().pop();
    }


}
