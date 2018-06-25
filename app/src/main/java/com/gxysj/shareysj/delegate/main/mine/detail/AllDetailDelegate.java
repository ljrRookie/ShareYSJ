package com.gxysj.shareysj.delegate.main.mine.detail;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.gxysj.shareysj.R;
import com.gxysj.shareysj.bean.UserTransactionDetailedBean;
import com.gxysj.shareysj.bean.event.EventStartFragment;
import com.gxysj.shareysj.config.ShareYSJ;
import com.gxysj.shareysj.delegate.base.YSJDelegate;
import com.gxysj.shareysj.delegate.main.mine.TransactionDetailDelegate;
import com.gxysj.shareysj.delegate.main.mine.detail.adapter.DetailAdapter;
import com.gxysj.shareysj.delegate.main.mine.order.OrderInfoDelegate;
import com.gxysj.shareysj.network.RequestCenter;
import com.gxysj.shareysj.utils.SPUtil;
import com.gxysj.shareysj.utils.TimeUtils;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.ljr.delegate_sdk.okhttp.listener.DisposeDataListener;
import com.ljr.delegate_sdk.okhttp.request.RequestParams;
import com.vondear.rxtools.RxLogTool;
import com.vondear.rxtools.RxTimeTool;
import com.vondear.rxtools.view.RxToast;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by 林佳荣 on 2018/3/6.
 * Github：https://github.com/ljrRookie
 * Function ：
 */

public class AllDetailDelegate extends YSJDelegate {
    @BindView(R.id.rv_detail)
    XRecyclerView mRvDetail;
    @BindView(R.id.tv_empty)
    TextView mTvEmpty;
    private int mTypeId;
    private int mPage = 1;
    private int mCount = 10;
    private List<UserTransactionDetailedBean.DataBean> mData;
    private DetailAdapter mDetailAdapter;

    @Override
    public Object setLayout() {
        return R.layout.delegate_mine_detail_all;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        initData();
        initRecyclerView();
    }

    @Override
    public void onResume() {
        super.onResume();
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
                mPage = 1;
                initData();
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
        requestParams.put("type", mTypeId);
        RequestCenter.UserTransactionDetailed(requestParams, new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                UserTransactionDetailedBean userTransactionDetailedBean= (UserTransactionDetailedBean) responseObj;
                List<UserTransactionDetailedBean.DataBean> moreData = userTransactionDetailedBean.getData();
                if(moreData !=null&& moreData.size()>0){
                    for (int i = 0; i < moreData.size(); i++) {
                        mData.add(moreData.get(i));
                    }
                    mDetailAdapter.notifyItemChanged(moreData.size());
                    mPage++;
                    mRvDetail.loadMoreComplete();

                }else{
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
        requestParams.put("type", mTypeId);
        RequestCenter.UserTransactionDetailed(requestParams, new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                 UserTransactionDetailedBean userTransactionDetailedBean= (UserTransactionDetailedBean) responseObj;
                mData = userTransactionDetailedBean.getData();
                if(mData !=null&& mData.size()>0){
                    mTvEmpty.setVisibility(View.GONE);
                    mRvDetail.setVisibility(View.VISIBLE);
                    mDetailAdapter = new DetailAdapter(getActivity(), mData, AllDetailDelegate.this);
                    mRvDetail.setAdapter(mDetailAdapter);
                }else{

                    mTvEmpty.setVisibility(View.VISIBLE);
                    mRvDetail.setVisibility(View.GONE);
                }

            }

            @Override
            public void onFailure(Object responseObj) {
                mTvEmpty.setText("获取数据失败!");
                mTvEmpty.setVisibility(View.VISIBLE);
                mRvDetail.setVisibility(View.GONE);
            }
        });



    }


    public static AllDetailDelegate create(int typeId) {
        AllDetailDelegate allDetailDelegate = new AllDetailDelegate();
        Bundle bundle = new Bundle();
        bundle.putInt("typeId",typeId);
        allDetailDelegate.setArguments(bundle);
        return allDetailDelegate;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        mTypeId = arguments.getInt("typeId", 0);
    }
}
