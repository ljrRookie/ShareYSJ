package com.gxysj.shareysj.delegate.main.mine.ad;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.gxysj.shareysj.R;
import com.gxysj.shareysj.bean.UserAdBean;
import com.gxysj.shareysj.bean.event.EventEmpty;
import com.gxysj.shareysj.config.ShareYSJ;
import com.gxysj.shareysj.delegate.base.YSJDelegate;
import com.gxysj.shareysj.delegate.main.mine.MyADDelegate;
import com.gxysj.shareysj.delegate.main.mine.ad.adapter.MyADAdapter;
import com.gxysj.shareysj.listener.MyAdInterface;
import com.gxysj.shareysj.network.RequestCenter;
import com.gxysj.shareysj.utils.SPUtil;
import com.gxysj.shareysj.utils.TimeUtils;
import com.gxysj.shareysj.utils.user_manager.Utils;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.ljr.delegate_sdk.okhttp.exception.OkHttpException;
import com.ljr.delegate_sdk.okhttp.listener.DisposeDataListener;
import com.ljr.delegate_sdk.okhttp.request.RequestParams;
import com.vondear.rxtools.RxLogTool;
import com.vondear.rxtools.RxTimeTool;
import com.vondear.rxtools.view.RxToast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;

import static android.R.attr.matchOrder;
import static android.R.attr.type;

/**
 * Created by 林佳荣 on 2018/3/9.
 * Github：https://github.com/ljrRookie
 * Function ：
 */

public class AllADDelegate extends YSJDelegate {
    @BindView(R.id.rv_ad)
    XRecyclerView mRvAd;
    @BindView(R.id.tv_empty)
    TextView mTvEmpty;
    //页数
    private int mPage = 1;
    //每页数量
    private int mNumber = 10;

    private int mType = -1;
    private List<UserAdBean.DataBean> mData;
    private MyADAdapter mMyADAdapter;
    private MyADDelegate mMyADDelegate;
    private MyADDelegate mParentDelegate;

    @Override
    public Object setLayout() {
        return R.layout.delegate_mine_my_ad_all;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        Log.e("fragment", "fragment:" + mType);
        initRecyclerView();
        initData();
        mParentDelegate = getParentDelegate();
    }



    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        mType = arguments.getInt("Type", -1);
    }

    private void initRecyclerView() {
        mRvAd.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRvAd.setRefreshProgressStyle(ProgressStyle.BallClipRotateMultiple);
        mRvAd.setLoadingMoreProgressStyle(ProgressStyle.BallScaleRippleMultiple);
        mRvAd.setArrowImageView(R.drawable.icon_downgrey);
        mRvAd.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                mPage = 1;
                initData();
                mRvAd.setLoadingMoreEnabled(true);
                mRvAd.refreshComplete();

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
        requestParams.put("number", mNumber);
        requestParams.put("type", mType);
        RequestCenter.GetUserAdvertisement(requestParams, new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                UserAdBean userAdBean = (UserAdBean) responseObj;
                List<UserAdBean.DataBean> moreData = userAdBean.getData();
                if (moreData != null && moreData.size() > 0) {
                    for (int i = 0; i < moreData.size(); i++) {
                        mData.add(moreData.get(i));
                    }
                    mMyADAdapter.notifyItemChanged(moreData.size());
                    mPage++;
                    mRvAd.loadMoreComplete();
                } else {
                    mRvAd.setLoadingMoreEnabled(false);
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
        requestParams.put("count", 0);
        requestParams.put("number", mNumber);
        requestParams.put("type", mType);
        RequestCenter.GetUserAdvertisement(requestParams, new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                UserAdBean userAdBean = (UserAdBean) responseObj;
                mData = userAdBean.getData();
                if (mData != null && mData.size() > 0) {
                    mTvEmpty.setVisibility(View.GONE);
                    mRvAd.setVisibility(View.VISIBLE);
                    mMyADAdapter = new MyADAdapter(getActivity(), mData, mParentDelegate);

                    mRvAd.setAdapter(mMyADAdapter);
                } else {
                    mTvEmpty.setVisibility(View.VISIBLE);
                    mRvAd.setVisibility(View.GONE);

                }
            }

            @Override
            public void onFailure(Object responseObj) {
                OkHttpException error = (OkHttpException) responseObj;
                mTvEmpty.setVisibility(View.VISIBLE);
                mRvAd.setVisibility(View.GONE);
                mTvEmpty.setText("获取数据失败 - （" + error.getEmsg() + ")");

            }
        });
    }

    public static AllADDelegate create(int i) {
        AllADDelegate allADDelegate = new AllADDelegate();
        Bundle bundle = new Bundle();
        bundle.putInt("Type", i);
        allADDelegate.setArguments(bundle);
        return allADDelegate;
    }



}
