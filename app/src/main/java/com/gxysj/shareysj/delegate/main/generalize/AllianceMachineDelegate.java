package com.gxysj.shareysj.delegate.main.generalize;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gxysj.shareysj.R;
import com.gxysj.shareysj.bean.UserInvestmentMachineBean;
import com.gxysj.shareysj.config.ShareYSJ;
import com.gxysj.shareysj.delegate.base.YSJDelegate;
import com.gxysj.shareysj.delegate.main.generalize.adapter.AllianceMachineAdapter;
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

/**
 * Created by 林佳荣 on 2018/4/9.
 * Github：https://github.com/ljrRookie
 * Function ：
 */

public class AllianceMachineDelegate extends YSJDelegate {
    @BindView(R.id.btn_back)
    ImageView mBtnBack;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.btn_right)
    ImageView mBtnRight;
    @BindView(R.id.rv_detail)
    XRecyclerView mRvDetail;
    @BindView(R.id.tv_empty)
    TextView mTvEmpty;

    private int mPage = 1;
    private int mCount = 15;
    private List<UserInvestmentMachineBean.DataBean> mData;
    private AllianceMachineAdapter mAllianceMachineAdapter;

    @Override
    public Object setLayout() {
        return R.layout.delegate_generalize_alliance_machine;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        mTvTitle.setText("机箱信息");
        initRecyclerView();
        initData();
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

        RequestCenter.GetUserInvestmentMachineList(requestParams,new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                UserInvestmentMachineBean userInvestmentMachineBean = (UserInvestmentMachineBean) responseObj;
                List<UserInvestmentMachineBean.DataBean> moreData = userInvestmentMachineBean.getData();
                if (moreData != null && moreData.size() > 0) {
                    for (int i = 0; i < moreData.size(); i++) {
                        mData.add(moreData.get(i));
                    }
                    mAllianceMachineAdapter.notifyItemChanged(moreData.size());
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
        RequestCenter.GetUserInvestmentMachineList(requestParams, new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                UserInvestmentMachineBean userInvestmentMachineBean = (UserInvestmentMachineBean) responseObj;
                mData = userInvestmentMachineBean.getData();
                if (mData != null && mData.size() > 0) {
                    if(mData.size()<mCount){
                        mRvDetail.setLoadingMoreEnabled(false);
                    }
                    mTvEmpty.setVisibility(View.GONE);
                    mRvDetail.setVisibility(View.VISIBLE);
                    mAllianceMachineAdapter = new AllianceMachineAdapter(getActivity(), mData);
                    mRvDetail.setAdapter(mAllianceMachineAdapter);
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

    @OnClick(R.id.btn_back)
    public void onViewClicked() {
        getSupportDelegate().pop();
    }
}
