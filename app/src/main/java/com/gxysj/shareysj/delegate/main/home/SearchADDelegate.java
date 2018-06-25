package com.gxysj.shareysj.delegate.main.home;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gxysj.shareysj.R;
import com.gxysj.shareysj.bean.GetAdvertisementBean;
import com.gxysj.shareysj.delegate.base.YSJDelegate;
import com.gxysj.shareysj.delegate.main.home.ad.MoreAdDelegate;
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
 * Created by 林佳荣 on 2018/4/9.
 * Github：https://github.com/ljrRookie
 * Function ：
 */

public class SearchADDelegate extends YSJDelegate {
    @BindView(R.id.btn_back)
    ImageView mBtnBack;
    @BindView(R.id.edit_key)
    EditText mEditKey;
    @BindView(R.id.btn_search)
    Button mBtnSearch;
    @BindView(R.id.ll_toolbar)
    LinearLayout mLlToolbar;
    @BindView(R.id.rv_ad)
    XRecyclerView mRvAd;
    @BindView(R.id.tv_empty)
    TextView mTvEmpty;
    Unbinder unbinder;
    private String mKey = "";
    private int mPage = 1;
    private int mNumber = 10;
    private List<GetAdvertisementBean.DataBean> mData;
    private MoreAdAdapter mMoreAdapter;

    @Override
    public Object setLayout() {
        return R.layout.delegate_search_list;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        initRecyclerView();
        initData(mKey);
        initEditText();
    }

    private void initEditText() {
        mEditKey.setText(mKey);
        mEditKey.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String key = mEditKey.getText().toString().trim();
                    mEditKey.setText("");
                    initData(key);
                }
                return false;
            }
        });
    }

    private void initData(String key) {
        mPage = 1;
        RequestParams params = new RequestParams();
        params.put("count", 0);
        params.put("number", mNumber);
        params.put("type", 1);
        params.put("value", key);
        params.put("key", 0);
        RequestCenter.GetAdvertisementList(params, new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                GetAdvertisementBean getAdvertisementBean = (GetAdvertisementBean) responseObj;
                mData = getAdvertisementBean.getData();
                if (mData != null & mData.size() > 0) {
                    if (mData.size() < mNumber) {
                        mRvAd.setLoadingMoreEnabled(false);
                    }
                    mRvAd.setVisibility(View.VISIBLE);
                    mTvEmpty.setVisibility(View.GONE);
                    mMoreAdapter = new MoreAdAdapter(getActivity(), mData, SearchADDelegate.this);
                    mRvAd.setAdapter(mMoreAdapter);
                    if (mData.size() < mNumber) {
                        mRvAd.setPullRefreshEnabled(false);
                    }
                } else {
                    mRvAd.setVisibility(View.GONE);
                    mTvEmpty.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onFailure(Object responseObj) {
                OkHttpException error = (OkHttpException) responseObj;
                RxToast.normal("获取广告数据失败-(" + error.getEmsg() + ")");
                mRvAd.setVisibility(View.GONE);
                mTvEmpty.setVisibility(View.VISIBLE);
            }
        });

    }

    public static SearchADDelegate create(String key) {
        SearchADDelegate searchADDelegate = new SearchADDelegate();
        Bundle bundle = new Bundle();
        bundle.putString("key", key);
        searchADDelegate.setArguments(bundle);
        return searchADDelegate;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            mKey = arguments.getString("key");
        }
    }

    private void initRecyclerView() {
        mRvAd.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRvAd.setRefreshProgressStyle(ProgressStyle.BallClipRotateMultiple);
        mRvAd.setLoadingMoreProgressStyle(ProgressStyle.BallScaleRippleMultiple);
        mRvAd.setArrowImageView(R.drawable.icon_downgrey);
        mRvAd.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                RxToast.normal("下拉刷新");
                initData(mKey);
                mPage = 1;
                mRvAd.setLoadingMoreEnabled(true);
                mRvAd.refreshComplete();
            }

            @Override
            public void onLoadMore() {
                MoreData(mKey);
            }
        });
    }

    private void MoreData(String key) {
        RequestParams params = new RequestParams();
        params.put("count", mPage);
        params.put("number", mNumber);
        params.put("type", 1);
        params.put("value", key);
        params.put("key", 0);
        RequestCenter.GetAdvertisementList(params, new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                GetAdvertisementBean getAdvertisementBean = (GetAdvertisementBean) responseObj;
                List<GetAdvertisementBean.DataBean> moreData = getAdvertisementBean.getData();
                if (moreData != null && moreData.size() > 0) {
                    for (int i = 0; i < moreData.size(); i++) {
                        mData.add(moreData.get(i));
                    }
                    mMoreAdapter.notifyItemChanged(moreData.size());
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

    @OnClick({R.id.btn_back, R.id.btn_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                getSupportDelegate().pop();
                break;
            case R.id.btn_search:
                String key = mEditKey.getText().toString().trim();
                if (TextUtils.isEmpty(key)) {
                    RxToast.normal("搜索内容不能为空");
                } else {
                    initData(key);
                }
                break;
        }
    }
}
