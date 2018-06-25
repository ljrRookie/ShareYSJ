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
import com.gxysj.shareysj.bean.UserNewsLogBean;
import com.gxysj.shareysj.bean.UserRedLogBean;
import com.gxysj.shareysj.delegate.base.YSJDelegate;
import com.gxysj.shareysj.delegate.main.home.adapter.MsgAdapter;
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

public class MsgDelegate extends YSJDelegate {
    @BindView(R.id.btn_back)
    ImageView mBtnBack;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.rv_msg)
    XRecyclerView mRvMsg;
    @BindView(R.id.tv_empty)
    TextView mTvEmpty;
    private int mNumber = 15;
    private int mPage = 1;
    private List<UserNewsLogBean.DataBean> mData;
    private MsgAdapter mMsgAdapter;

    @Override
    public Object setLayout() {
        return R.layout.delegate_mine_msg;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        mTvTitle.setText("我的消息");
        initRecyclerView();
        initData();
    }
    private void initRecyclerView() {
        mRvMsg.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRvMsg.setRefreshProgressStyle(ProgressStyle.BallClipRotateMultiple);
        mRvMsg.setLoadingMoreProgressStyle(ProgressStyle.BallScaleRippleMultiple);
        mRvMsg.setArrowImageView(R.drawable.icon_downgrey);
        mRvMsg.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                initData();
                mPage=1;
                mRvMsg.setLoadingMoreEnabled(true);
                mRvMsg.refreshComplete();
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
        RequestCenter.GetUserNewsLog(requestParams, new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                UserNewsLogBean userNewsLogBean = (UserNewsLogBean) responseObj;
                List<UserNewsLogBean.DataBean> moreData = userNewsLogBean.getData();
                if(moreData !=null&& moreData.size()>0){
                    for (int i = 0; i < moreData.size(); i++) {
                        mData.add(moreData.get(i));
                    }
                    mMsgAdapter.notifyItemChanged(moreData.size());
                    mPage++;
                    mRvMsg.loadMoreComplete();

                }else{
                    mRvMsg.setLoadingMoreEnabled(false);
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
        RequestCenter.GetUserNewsLog(requestParams, new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                UserNewsLogBean userNewsLogBean = (UserNewsLogBean) responseObj;
                mData = userNewsLogBean.getData();
                if (mData !=null & mData.size()>0) {
                    if(mData.size()<mNumber){
                        mRvMsg.setLoadingMoreEnabled(false);
                    }
                    mRvMsg.setVisibility(View.VISIBLE);
                    mTvEmpty.setVisibility(View.GONE);
                    mMsgAdapter = new MsgAdapter(getActivity(), mData);
                     mRvMsg.setAdapter(mMsgAdapter);
                    if(mData.size()<mNumber){
                        mRvMsg.setPullRefreshEnabled(false);
                    }
                }else{
                    mRvMsg.setVisibility(View.GONE);
                    mTvEmpty.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onFailure(Object responseObj) {
                OkHttpException error = (OkHttpException) responseObj;
                RxToast.normal("获取消息数据失败-(" + error.getEmsg() + ")");
                mRvMsg.setVisibility(View.GONE);
                mTvEmpty.setVisibility(View.VISIBLE);
            }
        });
    }
    @OnClick(R.id.btn_back)
    public void onViewClicked() {
        getSupportDelegate().pop();
    }
}
