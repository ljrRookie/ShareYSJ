package com.gxysj.shareysj.delegate.main.generalize;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.allen.library.SuperTextView;
import com.gxysj.shareysj.R;
import com.gxysj.shareysj.bean.BounsDetail;
import com.gxysj.shareysj.config.ShareYSJ;
import com.gxysj.shareysj.delegate.base.YSJDelegate;
import com.gxysj.shareysj.network.RequestCenter;
import com.gxysj.shareysj.utils.SPUtil;
import com.gxysj.shareysj.utils.TimeUtils;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.ljr.delegate_sdk.okhttp.exception.OkHttpException;
import com.ljr.delegate_sdk.okhttp.listener.DisposeDataListener;
import com.ljr.delegate_sdk.okhttp.request.RequestParams;
import com.vondear.rxtools.RxTimeTool;
import com.vondear.rxtools.view.RxToast;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by 林佳荣 on 2018/4/9.
 * Github：https://github.com/ljrRookie
 * Function ：
 */

public class AllianceInvestDelegate extends YSJDelegate {
    @BindView(R.id.btn_back)
    ImageView mBtnBack;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.tv_bouns)
    SuperTextView mTvBouns;
    @BindView(R.id.rv_detail)
    XRecyclerView mRvDetail;
    @BindView(R.id.tv_empty)
    TextView mTvEmpty;
    Unbinder unbinder;
    private int mPage = 1;
    private int mCount = 15;
    private BounsDetailAdapter mBounsDetailAdapter;
    private List<BounsDetail.DataBean> mData;

    @Override
    public Object setLayout() {
        return R.layout.delegate_generalize_alliance_profit;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        mTvTitle.setText("投资明细");
        mTvEmpty.setText("暂无投资明细~");
        mTvBouns.setLeftString("累计投资");
        initRecyclerView();
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
        RequestCenter.GetUserInvestmentDetailed(requestParams, new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                BounsDetail bounsDetail = (BounsDetail) responseObj;
                String money = bounsDetail.getMoney();
                if(!TextUtils.isEmpty(money)){
                    mTvBouns.setRightString("+"+money + "元");
                }
                mData = bounsDetail.getData();
                if (mData != null && mData.size() > 0) {
                    if(mData.size()<mCount){
                        mRvDetail.setLoadingMoreEnabled(false);
                    }
                    mTvEmpty.setVisibility(View.GONE);
                    mRvDetail.setVisibility(View.VISIBLE);
                    mBounsDetailAdapter = new BounsDetailAdapter(getActivity(), mData);
                    mRvDetail.setAdapter(mBounsDetailAdapter);
                } else {
                    mTvEmpty.setVisibility(View.VISIBLE);
                    mRvDetail.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Object responseObj) {
                OkHttpException error = (OkHttpException) responseObj;
                mTvEmpty.setVisibility(View.VISIBLE);
                mRvDetail.setVisibility(View.GONE);
                mTvEmpty.setText("获取数据失败。\n（" + error.getEmsg() + ")");
            }
        });

    }
    private void initRecyclerView() {
        mRvDetail.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRvDetail.setRefreshProgressStyle(ProgressStyle.BallClipRotateMultiple);
        mRvDetail.setLoadingMoreProgressStyle(ProgressStyle.BallScaleRippleMultiple);
        mRvDetail.setArrowImageView(R.drawable.icon_downgrey);
        mRvDetail.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                initData();
                mPage = 1;
                mRvDetail.setLoadingMoreEnabled(true);
                mRvDetail.refreshComplete();
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

        RequestCenter.GetUserInvestmentDetailed(requestParams,new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                BounsDetail bounsDetail = (BounsDetail) responseObj;
                List<BounsDetail.DataBean> moreData = bounsDetail.getData();
                if (moreData != null && moreData.size() > 0) {
                    for (int i = 0; i < moreData.size(); i++) {
                        mData.add(moreData.get(i));
                    }
                    mBounsDetailAdapter.notifyItemChanged(moreData.size());
                    mPage++;
                    mRvDetail.loadMoreComplete();
                } else {
                    mRvDetail.setLoadingMoreEnabled(false);
                    RxToast.normal("加载完成");
                }
            }

            @Override
            public void onFailure(Object responseObj) {
                RxToast.normal("加载失败！");

            }
        });
    }
    @OnClick(R.id.btn_back)
    public void onViewClicked() {
        getSupportDelegate().pop();
    }
}
