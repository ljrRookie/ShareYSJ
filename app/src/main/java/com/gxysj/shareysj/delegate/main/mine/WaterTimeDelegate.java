package com.gxysj.shareysj.delegate.main.mine;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.allen.library.SuperButton;
import com.allen.library.SuperTextView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gxysj.shareysj.R;
import com.gxysj.shareysj.bean.GetUuid;
import com.gxysj.shareysj.bean.ShopOrderBean;
import com.gxysj.shareysj.bean.UseWaterTime;
import com.gxysj.shareysj.bean.UserInfoBean;
import com.gxysj.shareysj.codes.WaterType;
import com.gxysj.shareysj.config.Constant;
import com.gxysj.shareysj.config.ShareYSJ;
import com.gxysj.shareysj.delegate.SignInDelegate;
import com.gxysj.shareysj.delegate.base.YSJDelegate;
import com.gxysj.shareysj.delegate.main.mine.adapter.WaterTimeAdapter;
import com.gxysj.shareysj.network.RequestCenter;
import com.gxysj.shareysj.utils.SPUtil;
import com.gxysj.shareysj.utils.camear.RequestCodes;
import com.gxysj.shareysj.utils.user_manager.AccountManager;
import com.gxysj.shareysj.utils.user_manager.Utils;
import com.gxysj.shareysj.zxing.app.CaptureActivity;
import com.ljr.delegate_sdk.okhttp.exception.OkHttpException;
import com.ljr.delegate_sdk.okhttp.listener.DisposeDataListener;
import com.ljr.delegate_sdk.okhttp.request.RequestParams;
import com.vondear.rxtools.view.RxToast;
import com.vondear.rxtools.view.dialog.RxDialog;
import com.zyao89.view.zloading.ZLoadingDialog;
import com.zyao89.view.zloading.Z_TYPE;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by 林佳荣 on 2018/3/21.
 * Github：https://github.com/ljrRookie
 * Function ：饮水次数
 */

public class WaterTimeDelegate extends YSJDelegate {
    @BindView(R.id.btn_back)
    ImageView mBtnBack;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.tv_a_time)
    TextView mTvATime;
    @BindView(R.id.btn_use_a)
    SuperButton mBtnUseA;
    @BindView(R.id.tv_b_time)
    TextView mTvBTime;
    @BindView(R.id.btn_use_b)
    SuperButton mBtnUseB;
    @BindView(R.id.tv_Chargenub)
    SuperTextView mTvChargenub;
    Unbinder unbinder;

    private UserInfoBean mUserInfoBean;
    private RxDialog mDialog;
    private String mUseWater = "0";
    private String mFreeWater = "0";
    private String machineId = "";
    //是否绑定机器
    private boolean isBindMachine = false;
    private ZLoadingDialog mCheckDialog;
    private String mMachineId;

    @Override
    public Object setLayout() {
        return R.layout.delegate_mine_water_time;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        mTvTitle.setText("饮水次数");
        mDialog = new RxDialog(getContext());
        initData();
    }

    private void initData() {
        RequestParams requestParams = Utils.getRequestParams();
        RequestCenter.GetUserInfo(requestParams, new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                mUserInfoBean = (UserInfoBean) responseObj;
                mUseWater = mUserInfoBean.getWater();
                mFreeWater = mUserInfoBean.getGivewater();
                mTvChargenub.setLeftString("免费充电次数：" + mUserInfoBean.getChargenub());
                mTvATime.setText("您有" + mUseWater + "次饮水次数");
                mTvBTime.setText("您有" + mFreeWater + "次饮水次数");

                if (mUseWater.equals("0")) {
                    mBtnUseA.setClickable(false);
                    mBtnUseA.setShapeGradientEndColor(getResources().getColor(R.color.gray_button)).setUseShape();
                    mBtnUseA.setShapeGradientStartColor(getResources().getColor(R.color.gray_button)).setUseShape();
                } else {
                    mBtnUseA.setClickable(true);
                    mBtnUseA.setShapeGradientEndColor(getResources().getColor(R.color.orange_low)).setUseShape();
                    mBtnUseA.setShapeGradientStartColor(getResources().getColor(R.color.orange_high)).setUseShape();
                }
                if (mFreeWater.equals("0")) {
                    mBtnUseB.setClickable(false);
                    mBtnUseB.setShapeGradientEndColor(getResources().getColor(R.color.gray_button)).setUseShape();
                    mBtnUseB.setShapeGradientStartColor(getResources().getColor(R.color.gray_button)).setUseShape();
                } else {
                    mBtnUseB.setClickable(true);
                    mBtnUseB.setShapeGradientEndColor(getResources().getColor(R.color.orange_low)).setUseShape();
                    mBtnUseB.setShapeGradientStartColor(getResources().getColor(R.color.orange_high)).setUseShape();
                }

            }

            @Override
            public void onFailure(Object responseObj) {
                OkHttpException error = (OkHttpException) responseObj;
                RxToast.normal("获取用户数据失败(" + error.getEmsg() + ")");
            }
        });

    }

    private int userType = 0;

    @OnClick({R.id.btn_back, R.id.btn_use_a, R.id.btn_use_b})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                getSupportDelegate().pop();
                break;
            case R.id.btn_use_a:
                //充值次数
                userType = 0;
                int UseTime = Integer.parseInt(mUseWater);
                if (UseTime >= 1) {
                    if (isBindMachine) {
                        showUseWaterDialog();
                    } else {
                        showDialogBindMachine();
                    }

                }
                break;
            case R.id.btn_use_b:
                //免费次数
                userType = 1;
                int FreeTime = Integer.parseInt(mFreeWater);
                if (FreeTime >= 1) {
                    if (isBindMachine) {
                        showUseWaterDialog();
                    } else {
                        showDialogBindMachine();
                    }
                }
                break;

        }
    }

    private void showDialogBindMachine() {
        mDialog.getLayoutParams().gravity = Gravity.CENTER;
        View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_commit, null);
        TextView mTvTitle = (TextView) dialogView.findViewById(R.id.tv_title);
        mTvTitle.setText("提示\n请先扫码绑定售货机");
        SuperTextView btnCommit = (SuperTextView) dialogView.findViewById(R.id.btn_commit);
        SuperTextView btnCancel = (SuperTextView) dialogView.findViewById(R.id.btn_cancel);
        btnCancel.setCenterString("取消");
        btnCommit.setCenterString("绑定");
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.cancel();
            }
        });
        btnCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.cancel();
                if (hasPermission(Constant.HARDWEAR_CAMERA_PERMISSION)) {
                    doOpenCamera();
                } else {
                    requestPermission(Constant.HARDWEAR_CAMERA_CODE, Constant.HARDWEAR_CAMERA_PERMISSION);
                }
            }
        });
        mDialog.setContentView(dialogView);
        mDialog.show();
    }

    @Override
    public void doOpenCamera() {
        Intent intent = new Intent(getActivity(), CaptureActivity.class);
        startActivityForResult(intent, RequestCodes.SCAN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case RequestCodes.SCAN:
                if (resultCode == Activity.RESULT_OK) {
                    String code = data.getStringExtra("SCAN_RESULT");
                    //  showDialogForUse(code);
                    RequestParams requestParams = Utils.getRequestParams();
                    requestParams.put("machineid", code + "");
                    RequestCenter.GetMachineInfo(requestParams, new DisposeDataListener() {
                        @Override
                        public void onSuccess(Object responseObj) {
                            ShopOrderBean machineInfoBean = (ShopOrderBean) responseObj;
                            int resultCode = Integer.parseInt(machineInfoBean.getCode());
                            if (resultCode == 0) {
                                //绑定机器
                                String id = machineInfoBean.getId();
                                showDialogForUse(id);
                                mMachineId = id;
                                isBindMachine = true;
                            } else {
                                RxToast.normal("扫码失败");
                            }
                        }

                        @Override
                        public void onFailure(Object responseObj) {
                            OkHttpException error = (OkHttpException) responseObj;
                            int errorCode = error.getCode();
                            if(errorCode == 10000){
                                RxToast.normal("登录异常，请重新登录");
                                AccountManager.setSignState(false);
                                getParentDelegate().getSupportDelegate().start(SignInDelegate.create(0, ""));
                            }else{
                                RxToast.normal("操作失败 - (" + error.getEmsg() + "),请稍后重试");
                            }
                        }
                    });
                    //  RxToast.normal("已绑定：" + code + "号售货机。");

                }
                break;
        }
    }

    private void showDialogForUse(String code) {
        mDialog.getLayoutParams().gravity = Gravity.CENTER;
        View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_commit, null);
        TextView mTvTitle = (TextView) dialogView.findViewById(R.id.tv_title);
        mTvTitle.setText("已绑定：" + code + "号售货机。");
        SuperTextView btnCommit = (SuperTextView) dialogView.findViewById(R.id.btn_commit);
        SuperTextView btnCancel = (SuperTextView) dialogView.findViewById(R.id.btn_cancel);
        btnCancel.setVisibility(View.GONE);
        btnCommit.setCenterString("确定");
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.cancel();
            }
        });
        btnCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.cancel();
            }
        });
        mDialog.setContentView(dialogView);
        mDialog.show();
    }

    private void showUseWaterDialog() {
        final RequestParams requestParams = Utils.getRequestParams();
        requestParams.put("type1", userType);
        requestParams.put("machineid", mMachineId);
        mDialog.getLayoutParams().gravity = Gravity.CENTER;
        View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_user_water, null);
        RecyclerView rvTime = (RecyclerView) dialogView.findViewById(R.id.rv_time);
        final List<UseWaterTime> title = new ArrayList<>();
        String Water_Default = String.valueOf(SPUtil.get(ShareYSJ.getApplicationContext(), WaterType.Water_Default, ""));
        if (!Water_Default.isEmpty()) {
            title.add(new UseWaterTime(Water_Default, WaterType.Water_Default, "0"));
        }
        String Water_Hot = String.valueOf(SPUtil.get(ShareYSJ.getApplicationContext(), WaterType.Water_Hot, ""));
        if (!Water_Hot.isEmpty()) {
            title.add(new UseWaterTime(Water_Hot, WaterType.Water_Hot, "0"));
        }
        String Water_Brand = String.valueOf(SPUtil.get(ShareYSJ.getApplicationContext(), WaterType.Water_Brand, ""));
        if (!Water_Brand.isEmpty()) {
            title.add(new UseWaterTime(Water_Brand, WaterType.Water_Brand, "0"));
        }
        rvTime.setLayoutManager(new LinearLayoutManager(getContext()));
        WaterTimeAdapter waterTimeAdapter = new WaterTimeAdapter(R.layout.item_water_time, title, mDialog);
        selectWaterType(requestParams, title, waterTimeAdapter);
        rvTime.setAdapter(waterTimeAdapter);
        mDialog.setContentView(dialogView);
        mDialog.show();
    }

    private void selectWaterType(final RequestParams requestParams, final List<UseWaterTime> title, WaterTimeAdapter waterTimeAdapter) {
        waterTimeAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                int id = view.getId();
                UseWaterTime useWaterTime = title.get(position);
                /**
                 * 11=清清泉常温水，12=清清泉冰水
                 * 21=品牌水热水，22=品牌冰水
                 * 方便熟水  和  品牌水 传一样
                 */
                if (id == R.id.btn_cool) {
                    //冷水
                    if (useWaterTime.getType().equals(WaterType.Water_Default)) {
                        //清清泉
                        requestParams.put("type", 12);
                        Log.e("UserUseWater", "清清泉冷水：type1:" + userType + "==machineid:" + mMachineId + "===type:" + 12);

                    } else {
                        //方便熟水和品牌水
                        requestParams.put("type", 22);
                        Log.e("UserUseWater", "方便熟水和品牌水冷水type1:" + userType + "==machineid:" + mMachineId + "===type:" + 22);

                    }
                    loadData(requestParams);
                    mDialog.cancel();
                } else if (id == R.id.btn_hot) {
                    //温水
                    if (useWaterTime.getType().equals(WaterType.Water_Default)) {
                        //清清泉
                        requestParams.put("type", 11);
                        Log.e("UserUseWater", "type1:" + userType + "==machineid:" + mMachineId + "===type:" + 11);

                    } else {
                        //方便熟水和品牌水
                        requestParams.put("type", 21);
                        Log.e("UserUseWater", "type1:" + userType + "==machineid:" + mMachineId + "===type:" + 21);

                    }
                }
                loadData(requestParams);
                mDialog.cancel();
            }
        });
    }

    private void loadData(final RequestParams requestParams) {
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
