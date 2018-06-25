package com.gxysj.shareysj.delegate.main.mine.ad;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gxysj.shareysj.R;
import com.gxysj.shareysj.bean.GetAdvertisementInfoBean;
import com.gxysj.shareysj.delegate.base.YSJDelegate;
import com.gxysj.shareysj.network.HttpConstants;
import com.gxysj.shareysj.network.RequestCenter;
import com.ljr.delegate_sdk.okhttp.exception.OkHttpException;
import com.ljr.delegate_sdk.okhttp.listener.DisposeDataListener;
import com.ljr.delegate_sdk.okhttp.request.RequestParams;
import com.vondear.rxtools.view.RxToast;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by 林佳荣 on 2018/4/3.
 * Github：https://github.com/ljrRookie
 * Function ：
 */

public class AdInfoDelegate extends YSJDelegate {

    @BindView(R.id.btn_back)
    ImageView mBtnBack;
    @BindView(R.id.tv_title)
    TextView mTvTitle;

    @BindView(R.id.tv_content)
    TextView mTvContent;
    @BindView(R.id.iv_a)
    ImageView mIvA;
    @BindView(R.id.iv_b)
    ImageView mIvB;
    @BindView(R.id.iv_c)
    ImageView mIvC;
    @BindView(R.id.tv_start_time)
    TextView mTvStartTime;
    @BindView(R.id.tv_end_time)
    TextView mTvEndTime;
    Unbinder unbinder;
    @BindView(R.id.tv_tit)
    TextView mTvTit;
    Unbinder unbinder1;
    private String mId;

    @Override
    public Object setLayout() {
        return R.layout.delegate_ad_info;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        mTvTitle.setText("广告详情");
        initData();
    }

    private void initData() {
        RequestParams params = new RequestParams();
        params.put("id", mId);

        RequestCenter.GetAdvertisementInfo(params, new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                GetAdvertisementInfoBean advertisementInfoBean = (GetAdvertisementInfoBean) responseObj;

                mTvTit.setText(advertisementInfoBean.getTitle());
                mTvContent.setText(advertisementInfoBean.getContent());
                String image = advertisementInfoBean.getImage();
                Log.e("广告", image);
                if (!image.contains(",")) {
                    mIvA.setVisibility(View.VISIBLE);
                    Glide.with(getActivity()).load(HttpConstants.ROOT_URL + image).placeholder(R.mipmap.bg_banner).error(R.mipmap.bg_banner).into(mIvA);
                   mIvB.setVisibility(View.GONE);
                    mIvC.setVisibility(View.GONE);
                } else {
                    String str2=image.replace(" ", "");//去掉所用空格
                    List<String> list = Arrays.asList(str2.split(","));
                    int size = list.size();
                    if(size==2){
                        mIvA.setVisibility(View.VISIBLE);
                        Glide.with(getActivity()).load(HttpConstants.ROOT_URL + list.get(0)).placeholder(R.mipmap.bg_banner).error(R.mipmap.bg_banner).into(mIvA);
                        mIvB.setVisibility(View.VISIBLE);
                        Glide.with(getActivity()).load(HttpConstants.ROOT_URL + list.get(1)).placeholder(R.mipmap.bg_banner).error(R.mipmap.bg_banner).into(mIvB);
                        mIvC.setVisibility(View.GONE);
                    }else if(size == 3){
                        mIvA.setVisibility(View.VISIBLE);
                        Glide.with(getActivity()).load(HttpConstants.ROOT_URL + list.get(0)).placeholder(R.mipmap.bg_banner).error(R.mipmap.bg_banner).into(mIvA);
                        mIvB.setVisibility(View.VISIBLE);
                        Glide.with(getActivity()).load(HttpConstants.ROOT_URL + list.get(1)).placeholder(R.mipmap.bg_banner).error(R.mipmap.bg_banner).into(mIvB);
                        mIvC.setVisibility(View.VISIBLE);
                        Glide.with(getActivity()).load(HttpConstants.ROOT_URL + list.get(2)).placeholder(R.mipmap.bg_banner).error(R.mipmap.bg_banner).into(mIvC);
                    }
                }
                mTvStartTime.setText("开始时间："+advertisementInfoBean.getState_time());
                mTvEndTime.setText("结束时间："+advertisementInfoBean.getEnd_time());
            }

            @Override
            public void onFailure(Object responseObj) {
                OkHttpException error = (OkHttpException) responseObj;
                RxToast.normal("获取广告详情失败 - (" + error.getEmsg() + ")");
            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        mId = arguments.getString("id", "");
    }

    @OnClick(R.id.btn_back)
    public void onViewClicked() {
        getSupportDelegate().pop();
    }

    public static AdInfoDelegate create(String id) {
        AdInfoDelegate adInfoDelegate = new AdInfoDelegate();
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        adInfoDelegate.setArguments(bundle);
        return adInfoDelegate;
    }

}
