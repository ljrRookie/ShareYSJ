package com.gxysj.shareysj.delegate.home;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.allen.library.SuperButton;
import com.allen.library.SuperTextView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gxysj.shareysj.R;
import com.gxysj.shareysj.bean.GetUuid;
import com.gxysj.shareysj.bean.UseWaterTime;
import com.gxysj.shareysj.bean.UserInfoBean;
import com.gxysj.shareysj.codes.WaterType;
import com.gxysj.shareysj.config.ShareYSJ;
import com.gxysj.shareysj.delegate.SignInDelegate;
import com.gxysj.shareysj.delegate.base.YSJDelegate;
import com.gxysj.shareysj.delegate.main.mine.adapter.WaterTimeAdapter;
import com.gxysj.shareysj.network.RequestCenter;
import com.gxysj.shareysj.utils.SPUtil;
import com.gxysj.shareysj.utils.TimeUtils;
import com.gxysj.shareysj.utils.user_manager.AccountManager;
import com.gxysj.shareysj.utils.user_manager.Utils;
import com.gxysj.shareysj.wechat.WeChatConfig;
import com.ljr.delegate_sdk.okhttp.exception.OkHttpException;
import com.ljr.delegate_sdk.okhttp.listener.DisposeDataListener;
import com.ljr.delegate_sdk.okhttp.request.RequestParams;
import com.vondear.rxtools.RxLogTool;
import com.vondear.rxtools.RxTimeTool;
import com.vondear.rxtools.view.RxToast;
import com.vondear.rxtools.view.dialog.RxDialog;
import com.zyao89.view.zloading.ZLoadingDialog;
import com.zyao89.view.zloading.Z_TYPE;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/2/6.
 */

public class ColdWaterOneDelegate extends YSJDelegate {
    @BindView(R.id.btn_back)
    ImageView mBtnBack;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.tv_flag)
    SuperTextView mTvFlag;
    @BindView(R.id.tv_free_time)
    TextView mTvFreeTime;
    @BindView(R.id.btn_use_free)
    SuperButton mBtnUseFree;
    @BindView(R.id.tv_buy_time)
    TextView mTvBuyTime;
    @BindView(R.id.btn_use_buy)
    SuperButton mBtnUseBuy;
    @BindView(R.id.btn_get_time)
    SuperButton mBtnGetTime;
    @BindView(R.id.tv_explain)
    TextView mTvExplain;
    private int mWaterId;
    private String mTitle;
    private RxDialog mDialog;
    private UserInfoBean mUserInfoBean;
    private int waterType = 0;
    public static final int FREE_WATER = 1;
    public static final int BUY_WATER = 0;
    private String mBuyWater;
    private String mFreeWater;
    private ZLoadingDialog mCheckDialog;
    private String mMachineId;

    @Override
    public Object setLayout() {
        return R.layout.delegate_home_cold_water_one;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        if (TextUtils.isEmpty(mTitle)) {
            mTvTitle.setText("饮水");
        } else {
            mTvTitle.setText(mTitle);
        }
        mDialog = new RxDialog(getActivity());
        initData();
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        initData();
    }


    private void initData() {
        RequestParams requestParams = Utils.getRequestParams();
        RequestCenter.GetUserInfo(requestParams, new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                mUserInfoBean = (UserInfoBean) responseObj;
                mBuyWater = mUserInfoBean.getWater();
                mFreeWater = mUserInfoBean.getGivewater();
                mTvFreeTime.setText("您有" + mFreeWater + "次免费饮水次数");
                mTvBuyTime.setText("您已购买" + mBuyWater + "次饮水次数");
                if (mBuyWater.equals("0")) {
                    mBtnUseBuy.setClickable(false);
                    mBtnUseBuy.setShapeGradientEndColor(getResources().getColor(R.color.gray_button)).setUseShape();
                    mBtnUseBuy.setShapeGradientStartColor(getResources().getColor(R.color.gray_button)).setUseShape();
                } else {
                    mBtnUseBuy.setClickable(true);
                    mBtnUseBuy.setShapeGradientEndColor(getResources().getColor(R.color.orange_low)).setUseShape();
                    mBtnUseBuy.setShapeGradientStartColor(getResources().getColor(R.color.orange_high)).setUseShape();
                }
                if (mFreeWater.equals("0")) {
                    mBtnUseFree.setClickable(false);
                    mBtnUseFree.setShapeGradientEndColor(getResources().getColor(R.color.gray_button)).setUseShape();
                    mBtnUseFree.setShapeGradientStartColor(getResources().getColor(R.color.gray_button)).setUseShape();
                } else {
                    mBtnUseFree.setClickable(true);
                    mBtnUseFree.setShapeGradientEndColor(getResources().getColor(R.color.orange_low)).setUseShape();
                    mBtnUseFree.setShapeGradientStartColor(getResources().getColor(R.color.orange_high)).setUseShape();
                }
            }

            @Override
            public void onFailure(Object responseObj) {
                OkHttpException error = (OkHttpException) responseObj;
                int errorCode = error.getCode();
                if (errorCode == 10000) {
                    RxToast.normal("登录异常，请重新登录");
                    AccountManager.setSignState(false);
                    getParentDelegate().getSupportDelegate().start(new SignInDelegate());
                } else {
                    RxToast.normal("获取用户数据失败(" + error.getEmsg() + ")");
                }
            }
        });
    }


    @OnClick({R.id.btn_back, R.id.btn_use_free, R.id.btn_use_buy, R.id.btn_get_time})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                getSupportDelegate().pop();
                break;
            case R.id.btn_use_free:
                waterType = FREE_WATER;
                if (!mFreeWater.equals("0")) {
                    showUseWaterDialog();
                }

                break;
            case R.id.btn_use_buy:
                waterType = BUY_WATER;
                if (!mBuyWater.equals("0")) {
                    showUseWaterDialog();
                }
                break;
            case R.id.btn_get_time:
                getSupportDelegate().start(WaterForGetDelegate.create(mMachineId, mTitle));

                break;
        }
    }

    private void showUseWaterDialog() {
        final RequestParams requestParams = Utils.getRequestParams();
        requestParams.put("type1", waterType);
        requestParams.put("machineid", mMachineId);
        mDialog.getLayoutParams().gravity = Gravity.CENTER;
        View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_select_water, null);

        ImageView btnHot = (ImageView) dialogView.findViewById(R.id.btn_hot);
        if (mWaterId == Integer.parseInt(WaterType.Water_Default)) {
            btnHot.setImageResource(R.mipmap.icon_warm);
        }
        btnHot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mWaterId == Integer.parseInt(WaterType.Water_Default)){
                    requestParams.put("type", 11);
                }else{
                    requestParams.put("type", 21);
                }
                loadData(requestParams);
                mDialog.cancel();
            }
        });
        dialogView.findViewById(R.id.btn_cool).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mWaterId == Integer.parseInt(WaterType.Water_Default)){
                    requestParams.put("type", 12);
                }else{
                    requestParams.put("type", 22);
                }
                loadData(requestParams);
                mDialog.cancel();
            }
        });

        mDialog.setContentView(dialogView);
        mDialog.show();
    }

    private void loadData(RequestParams requestParams) {
        RequestCenter.UserUseWater(requestParams, new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {

                GetUuid getUuid = (GetUuid) responseObj;
                final String uuid = getUuid.getUuid();
                mCheckDialog = new ZLoadingDialog(ShareYSJ.getActivity());
                mCheckDialog.setLoadingBuilder(Z_TYPE.ROTATE_CIRCLE)//设置类型
                        .setLoadingColor(Color.GRAY)//颜色
                        .setHintText("出水中....")
                        .setHintTextSize(14) // 设置字体大小 dp
                        .setCancelable(false)
                        .setCanceledOnTouchOutside(false)
                        .setHintTextColor(Color.BLACK)  // 设置字体颜色
                        .setDurationTime(0.5) // 设置动画时间百分比 - 0.5倍
                        .setDialogBackgroundColor(Color.TRANSPARENT) // 设置背景色，默认白色
                        .show();
                initData();
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        try {
                            Thread.sleep(5000);//休眠3秒
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                checkResult(uuid);
                            }
                        });
                    }
                }.start();
            }

            @Override
            public void onFailure(Object responseObj) {
                OkHttpException Error = (OkHttpException) responseObj;
                RxToast.normal("出水失败...(" + Error.getEmsg() + ")");
            }
        });
    }

    private void checkResult(String uuid) {
        final RequestParams requestParams = Utils.getRequestParams();
        requestParams.put("uuid", uuid);
        RequestCenter.VerificationWater(requestParams, new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {

                RxToast.normal("饮水成功");
                mCheckDialog.cancel();
            }

            @Override
            public void onFailure(Object responseObj) {
                OkHttpException error = (OkHttpException) responseObj;
                RxToast.normal("出水情况：" + error.getEmsg());
                mCheckDialog.cancel();
            }
        });
    }

    public static ColdWaterOneDelegate create(int waterId, String title,String machineId) {
        ColdWaterOneDelegate coldWaterOneDelegate = new ColdWaterOneDelegate();
        Bundle bundle = new Bundle();
        bundle.putInt("WaterId", waterId);
        bundle.putString("Title", title);
        bundle.putString("machineId", machineId);
        coldWaterOneDelegate.setArguments(bundle);
        return coldWaterOneDelegate;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arg = getArguments();
        if (arg != null) {
            mWaterId = arg.getInt("WaterId");
            mTitle = arg.getString("Title");
            mMachineId = arg.getString("machineId");
        }
    }
}
