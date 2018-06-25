package com.gxysj.shareysj.delegate.main.home;

import android.graphics.Color;
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

import com.allen.library.SuperButton;
import com.allen.library.SuperTextView;
import com.gxysj.shareysj.R;
import com.gxysj.shareysj.bean.ChargingInfo;
import com.gxysj.shareysj.bean.GetCodeBean;
import com.gxysj.shareysj.config.ShareYSJ;
import com.gxysj.shareysj.delegate.base.YSJDelegate;
import com.gxysj.shareysj.delegate.main.mine.adapter.ChargeAdapter;
import com.gxysj.shareysj.delegate.main.mine.adapter.ReChargeAdapter;
import com.gxysj.shareysj.network.RequestCenter;
import com.gxysj.shareysj.utils.user_manager.Utils;
import com.ljr.delegate_sdk.okhttp.exception.OkHttpException;
import com.ljr.delegate_sdk.okhttp.listener.DisposeDataListener;
import com.ljr.delegate_sdk.okhttp.request.RequestParams;
import com.vondear.rxtools.RxLogTool;
import com.vondear.rxtools.view.RxToast;
import com.zyao89.view.zloading.ZLoadingDialog;
import com.zyao89.view.zloading.Z_TYPE;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import me.yokeyword.fragmentation.ISupportFragment;


/**
 * Created by Administrator on 2018/2/3.
 */

public class ChargeDelegate extends YSJDelegate {
    @BindView(R.id.btn_back)
    ImageView mBtnBack;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.btn_right)
    ImageView mBtnRight;
    @BindView(R.id.tv_flag)
    SuperTextView mTvFlag;
    @BindView(R.id.btn_commit)
    SuperButton mBtnCommit;
    @BindView(R.id.rv_info)
    RecyclerView mRvInfo;
    private String mTitle;
    private String mMachineId;
    private ZLoadingDialog mCheckDialog;

    @Override
    public Object setLayout() {
        return R.layout.delegate_home_charge;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        mTvTitle.setText(mTitle);
        initData();
    }

    private void initData() {
        RequestCenter.GetChargingInfo(new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                ChargingInfo chargingInfo = (ChargingInfo) responseObj;
                List<ChargingInfo.DataBean> data = chargingInfo.getData();
                ChargeAdapter chargeAdapter = new ChargeAdapter(R.layout.item_charge, data);
                chargeAdapter.openLoadAnimation();
                mRvInfo.setLayoutManager(new LinearLayoutManager(getActivity()));
                mRvInfo.setAdapter(chargeAdapter);
            }

            @Override
            public void onFailure(Object responseObj) {
                RxToast.normal("获取充电说明失败");
            }
        });
    }


    @OnClick({R.id.btn_back, R.id.btn_commit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                getSupportDelegate().pop();
                break;
            case R.id.btn_commit:
                final RequestParams requestParams = Utils.getRequestParams();
                requestParams.put("machineid",mMachineId);
                RequestCenter.UserCharging(requestParams, new DisposeDataListener() {
                    @Override
                    public void onSuccess(Object responseObj) {
                        startCharging();
                    }
                    @Override
                    public void onFailure(Object responseObj) {
                        OkHttpException error = (OkHttpException) responseObj;
                        RxToast.normal("操作失败-(" + error.getEmsg() + ")");
                    }
                });
                break;
        }
    }


    public static ChargeDelegate create(String name, String machineId) {
        Bundle bundle = new Bundle();
        ChargeDelegate chargeDelegate = new ChargeDelegate();
        bundle.putString("Title", name);
        bundle.putString("machineId", machineId);
        chargeDelegate.setArguments(bundle);
        return chargeDelegate;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arg = getArguments();
        if (arg != null) {
            mTitle = arg.getString("Title");
            mMachineId = arg.getString("machineId");
        }
    }
    private void startCharging() {
        mCheckDialog = new ZLoadingDialog(ShareYSJ.getActivity());
        mCheckDialog.setLoadingBuilder(Z_TYPE.ROTATE_CIRCLE)//设置类型
                .setLoadingColor(Color.GRAY)//颜色
                .setHintText("充电准备中....")
                .setHintTextSize(14) // 设置字体大小 dp
                .setCancelable(false)
                .setCanceledOnTouchOutside(false)
                .setHintTextColor(Color.BLACK)  // 设置字体颜色
                .setDurationTime(0.5) // 设置动画时间百分比 - 0.5倍
                .setDialogBackgroundColor(Color.TRANSPARENT) // 设置背景色，默认白色
                .show();
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(9000);//休眠3秒
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mCheckDialog.cancel();


                    }
                });
            }
        }.start();
    }
}
