package com.gxysj.shareysj.delegate.main.mine.order;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.allen.library.SuperTextView;
import com.gxysj.shareysj.R;
import com.gxysj.shareysj.bean.GetOrderInfoBean;
import com.gxysj.shareysj.config.ShareYSJ;
import com.gxysj.shareysj.delegate.base.YSJDelegate;
import com.gxysj.shareysj.delegate.main.mine.order.adapter.OrderInfoAdapter;
import com.gxysj.shareysj.network.RequestCenter;
import com.gxysj.shareysj.utils.SPUtil;
import com.gxysj.shareysj.utils.TimeUtils;
import com.gxysj.shareysj.utils.user_manager.Utils;
import com.gxysj.shareysj.zxing.util.Util;
import com.ljr.delegate_sdk.okhttp.exception.OkHttpException;
import com.ljr.delegate_sdk.okhttp.listener.DisposeDataListener;
import com.ljr.delegate_sdk.okhttp.request.RequestParams;
import com.tencent.mm.opensdk.modelbiz.AddCardToWXCardPackage;
import com.vondear.rxtools.RxLogTool;
import com.vondear.rxtools.RxTimeTool;
import com.vondear.rxtools.view.RxToast;

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

public class OrderInfoDelegate extends YSJDelegate {

    @BindView(R.id.btn_back)
    ImageView mBtnBack;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.btn_right)
    ImageView mBtnRight;
    @BindView(R.id.rv_goods)
    RecyclerView mRvGoods;
    @BindView(R.id.tv_machine_id)
    SuperTextView mTvMachineId;
    @BindView(R.id.tv_order_num)
    SuperTextView mTvOrderNum;
    @BindView(R.id.tv_time)
    SuperTextView mTvTime;

    private String mOrderId;


    @Override
    public Object setLayout() {
        return R.layout.delegate_mine_order_info;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        mOrderId = arguments.getString("orderId", "");

    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        mTvTitle.setText("订单详情");
        initData();
    }

    private void initData() {
        RequestParams requestParams = Utils.getRequestParams();
        requestParams.put("id", mOrderId);
        RequestCenter.GetOrderInfo(requestParams, new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                GetOrderInfoBean getOrderInfoBean = (GetOrderInfoBean) responseObj;
                mTvMachineId.setCenterString(getOrderInfoBean.getMachineid());
                mTvOrderNum.setCenterString(getOrderInfoBean.getOrder_number());
                mTvTime.setCenterString(getOrderInfoBean.getTime());
                List<GetOrderInfoBean.ListBean> list = getOrderInfoBean.getList();
                OrderInfoAdapter orderInfoAdapter = new OrderInfoAdapter(R.layout.item_order_info, list);
                mRvGoods.setLayoutManager(new LinearLayoutManager(getActivity()));
                orderInfoAdapter.openLoadAnimation();
                mRvGoods.setAdapter(orderInfoAdapter);

            }

            @Override
            public void onFailure(Object responseObj) {
                OkHttpException error = (OkHttpException) responseObj;
                RxToast.normal("获取数据失败 - （" + error.getEmsg() + ")");

            }
        });
    }


    @OnClick(R.id.btn_back)
    public void onViewClicked() {
        getSupportDelegate().pop();
    }

    public static OrderInfoDelegate create(String id) {
        OrderInfoDelegate orderInfoDelegate = new OrderInfoDelegate();
        Bundle bundle = new Bundle();
        bundle.putString("orderId", id);
        orderInfoDelegate.setArguments(bundle);
        return orderInfoDelegate;
    }


}
