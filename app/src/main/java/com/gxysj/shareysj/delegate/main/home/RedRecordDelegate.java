package com.gxysj.shareysj.delegate.main.home;

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
import com.gxysj.shareysj.bean.UserRedLogBean;
import com.gxysj.shareysj.delegate.base.YSJDelegate;
import com.gxysj.shareysj.delegate.main.home.ad.MoreAdDelegate;
import com.gxysj.shareysj.delegate.main.home.adapter.MoreAdAdapter;
import com.gxysj.shareysj.delegate.main.home.adapter.RedRecordAdapter;
import com.gxysj.shareysj.network.RequestCenter;
import com.gxysj.shareysj.utils.user_manager.Utils;
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
 * Created by 林佳荣 on 2018/4/11.
 * Github：https://github.com/ljrRookie
 * Function ：
 */

public class RedRecordDelegate extends YSJDelegate {
    @BindView(R.id.btn_back)
    ImageView mBtnBack;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.rv_red)
    XRecyclerView mRvRed;
    @BindView(R.id.tv_empty)
    TextView mTvEmpty;
    Unbinder unbinder;
    private int mNumber = 10;
    private int mPage = 1;
    private List<UserRedLogBean.DataBean> mData;
    private RedRecordAdapter mRedRecordAdapter;

    @Override
    public Object setLayout() {
        return R.layout.delegate_home_redbag_game_info;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        mTvTitle.setText("红包记录");
        initRecyclerView();
        initData();
    }
    private void initRecyclerView() {
        mRvRed.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRvRed.setRefreshProgressStyle(ProgressStyle.BallClipRotateMultiple);
        mRvRed.setLoadingMoreProgressStyle(ProgressStyle.BallScaleRippleMultiple);
        mRvRed.setArrowImageView(R.drawable.icon_downgrey);
        mRvRed.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                initData();
                mPage=1;
                mRvRed.setLoadingMoreEnabled(true);
                mRvRed.refreshComplete();
            }

            @Override
            public void onLoadMore() {
                MoreData();
            }
        });
    }

    private void MoreData() {
        RequestParams requestParams = Utils.getRequestParams();
        requestParams.put("count",mPage);
        requestParams.put("number",mNumber);
        RequestCenter.UserRedEnvelopesLog(requestParams, new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                UserRedLogBean userRedLogBean = (UserRedLogBean) responseObj;
                List<UserRedLogBean.DataBean> moreData = userRedLogBean.getData();
                if(moreData !=null&& moreData.size()>0){
                    for (int i = 0; i < moreData.size(); i++) {
                        mData.add(moreData.get(i));
                    }
                    mRedRecordAdapter.notifyItemChanged(moreData.size());
                    mPage++;
                    mRvRed.loadMoreComplete();

                }else{
                    mRvRed.setLoadingMoreEnabled(false);
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
        RequestParams requestParams = Utils.getRequestParams();
        requestParams.put("count",0);
        requestParams.put("number",mNumber);
        RequestCenter.UserRedEnvelopesLog(requestParams, new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                UserRedLogBean userRedLogBean = (UserRedLogBean) responseObj;
                mData = userRedLogBean.getData();
                if (mData !=null & mData.size()>0) {
                    if(mData.size()<mNumber){
                        mRvRed.setLoadingMoreEnabled(false);
                    }
                    mRvRed.setVisibility(View.VISIBLE);
                    mTvEmpty.setVisibility(View.GONE);
                    mRedRecordAdapter = new RedRecordAdapter(getActivity(), mData);
                    mRvRed.setAdapter(mRedRecordAdapter);
                    if(mData.size()<mNumber){
                        mRvRed.setPullRefreshEnabled(false);
                    }
                }else{
                    mRvRed.setVisibility(View.GONE);
                    mTvEmpty.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onFailure(Object responseObj) {
                OkHttpException error = (OkHttpException) responseObj;
                RxToast.normal("获取记录数据失败-(" + error.getEmsg() + ")");
                mRvRed.setVisibility(View.GONE);
                mTvEmpty.setVisibility(View.VISIBLE);
            }
        });

    }

    @OnClick(R.id.btn_back)
    public void onViewClicked() {
        getSupportDelegate().pop();
    }
}
