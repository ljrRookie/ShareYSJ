package com.gxysj.shareysj.delegate.main.generalize;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gxysj.shareysj.R;
import com.gxysj.shareysj.bean.GetCityBean;
import com.gxysj.shareysj.bean.InvestMachineBean;
import com.gxysj.shareysj.delegate.base.YSJDelegate;
import com.gxysj.shareysj.delegate.main.generalize.adapter.InvestCityAdapter;
import com.gxysj.shareysj.delegate.main.generalize.adapter.InvestMachineAdapter;
import com.gxysj.shareysj.network.RequestCenter;
import com.gxysj.shareysj.utils.SPUtil;
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
 * Created by 林佳荣 on 2018/4/16.
 * Github：https://github.com/ljrRookie
 * Function ：
 */

public class InvestMachineDelegate extends YSJDelegate {
    @BindView(R.id.btn_back)
    ImageView mBtnBack;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.rv_info)
    XRecyclerView mRvInfo;
    @BindView(R.id.tv_empty)
    TextView mTvEmpty;
    @BindView(R.id.tv_more)
    TextView mTvMore;
    Unbinder unbinder;
    private int mNumber = 15;
    private int mPage = 1;
    private List<InvestMachineBean.DataBean> mData;
    private InvestMachineAdapter mInvestMachineAdapter;
    private View popView;
    private PopupWindow popupWindow;
    private String mCity = "";
    @Override
    public Object setLayout() {
        return R.layout.delegate_generalize_alliance_invest_machine;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        mTvTitle.setText("投资列表");
        String investCity = (String) SPUtil.get(getActivity(), "investCity", "");
        if(!investCity.isEmpty()){
            mTvMore.setText(investCity+" ▼ ");
            mCity = investCity;
        }
        initRecyclerView();
        initData(mCity);
        initInvestAddress();
    }

    private void initInvestAddress() {
        RequestCenter.InvestmentCityList(new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                GetCityBean cityList = (GetCityBean) responseObj;
                final List<GetCityBean.DataBean> data = cityList.getData();
                if(data!=null && data.size()>0){
                    LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    popView = inflater.from(getActivity()).inflate(R.layout.popwindow_invest_address, null);
                    RecyclerView cityRecycler = (RecyclerView) popView.findViewById(R.id.rv_city);
                    cityRecycler.setLayoutManager(new GridLayoutManager(getActivity(),4));
                    InvestCityAdapter investCityAdapter = new InvestCityAdapter(R.layout.item_invest_machine_city, data);
                    investCityAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                            GetCityBean.DataBean dataBean = data.get(position);
                            String city = dataBean.getCity();
                            mTvMore.setText(city+" ▼ ");
                            mCity = city;
                            SPUtil.put(getActivity(),"investCity",city);
                            popupWindow.dismiss();
                            initData(city);
                            mPage = 1;
                            mRvInfo.setLoadingMoreEnabled(true);
                        }
                    });
                    investCityAdapter.openLoadAnimation();
                    cityRecycler.setAdapter(investCityAdapter);
                }

            }

            @Override
            public void onFailure(Object responseObj) {
                OkHttpException error = (OkHttpException) responseObj;
                RxToast.normal("获取城市列表失败-(" + error.getEmsg() + ")");
            }
        });


    }


    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        initData(mCity);
    }

    private void initRecyclerView() {
        mRvInfo.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRvInfo.setRefreshProgressStyle(ProgressStyle.BallClipRotateMultiple);
        mRvInfo.setLoadingMoreProgressStyle(ProgressStyle.BallScaleRippleMultiple);
        mRvInfo.setArrowImageView(R.drawable.icon_downgrey);
        mRvInfo.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                initData(mCity);
                mPage = 1;
                mRvInfo.setLoadingMoreEnabled(true);
                mRvInfo.refreshComplete();
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
        requestParams.put("value", mCity);
        RequestCenter.GetMachineList(requestParams, new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                InvestMachineBean investMachineBean = (InvestMachineBean) responseObj;
                List<InvestMachineBean.DataBean> moreData = investMachineBean.getData();
                if (moreData != null && moreData.size() > 0) {
                    for (int i = 0; i < moreData.size(); i++) {
                        mData.add(moreData.get(i));
                    }
                    mInvestMachineAdapter.notifyItemChanged(moreData.size());
                    mPage++;
                    mRvInfo.loadMoreComplete();

                } else {
                    mRvInfo.setLoadingMoreEnabled(false);
                    RxToast.normal("加载完成");
                }

            }

            @Override
            public void onFailure(Object responseObj) {
                RxToast.normal("加载失败！");
            }
        });
    }

    private void initData(String city) {
        RequestParams requestParams = Utils.getRequestParams();
        requestParams.put("count", 0);
        requestParams.put("number", mNumber);
        requestParams.put("value", city);
        RequestCenter.GetMachineList(requestParams, new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                InvestMachineBean investMachineBean = (InvestMachineBean) responseObj;
                mData = investMachineBean.getData();
                if (mData != null & mData.size() > 0) {
                    if (mData.size() < mNumber) {
                        mRvInfo.setLoadingMoreEnabled(false);
                    }
                    mRvInfo.setVisibility(View.VISIBLE);
                    mTvEmpty.setVisibility(View.GONE);
                    mInvestMachineAdapter = new InvestMachineAdapter(getActivity(), mData, InvestMachineDelegate.this);
                    mRvInfo.setAdapter(mInvestMachineAdapter);
                   /* if(mData.size()<mNumber){
                      //  mRvInfo.setPullRefreshEnabled(false);
                    }*/
                } else {
                    mRvInfo.setVisibility(View.GONE);
                    mTvEmpty.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onFailure(Object responseObj) {
                OkHttpException error = (OkHttpException) responseObj;
                RxToast.normal("获取机箱数据失败-(" + error.getEmsg() + ")");
                mRvInfo.setVisibility(View.GONE);
                mTvEmpty.setVisibility(View.VISIBLE);
            }
        });
    }

    @OnClick(R.id.btn_back)
    public void onViewClicked() {
        getSupportDelegate().pop();
    }


    @OnClick(R.id.tv_more)
    public void onMoreClicked() {
        //getSupportDelegate().startForResult();
        popupWindow = new PopupWindow(popView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAsDropDown(mTvMore);
    }
}
