package com.gxysj.shareysj.delegate.home;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.allen.library.SuperButton;
import com.allen.library.SuperTextView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gxysj.shareysj.R;
import com.gxysj.shareysj.bean.ShareInfoBean;
import com.gxysj.shareysj.bean.ShopOrderBean;
import com.gxysj.shareysj.bean.UserInfoBean;
import com.gxysj.shareysj.bean.WaterMoneyListBean;
import com.gxysj.shareysj.config.Constant;
import com.gxysj.shareysj.config.ShareYSJ;
import com.gxysj.shareysj.config_enum.ConfigKeys;
import com.gxysj.shareysj.delegate.PayDelegate;
import com.gxysj.shareysj.delegate.ShopInfoDelegate;
import com.gxysj.shareysj.delegate.SignInDelegate;
import com.gxysj.shareysj.delegate.base.YSJDelegate;
import com.gxysj.shareysj.delegate.main.home.ShoppingDelegate;
import com.gxysj.shareysj.delegate.main.home.adapter.WaterPriceAdapter;
import com.gxysj.shareysj.network.HttpConstants;
import com.gxysj.shareysj.network.RequestCenter;
import com.gxysj.shareysj.utils.camear.RequestCodes;
import com.gxysj.shareysj.utils.user_manager.AccountManager;
import com.gxysj.shareysj.utils.user_manager.Utils;
import com.gxysj.shareysj.zxing.app.CaptureActivity;
import com.ljr.delegate_sdk.okhttp.exception.OkHttpException;
import com.ljr.delegate_sdk.okhttp.listener.DisposeDataListener;
import com.ljr.delegate_sdk.okhttp.request.RequestParams;
import com.vondear.rxtools.RxImageTool;
import com.vondear.rxtools.module.wechat.share.WechatShareModel;
import com.vondear.rxtools.module.wechat.share.WechatShareTools;
import com.vondear.rxtools.view.RxToast;
import com.vondear.rxtools.view.dialog.RxDialog;
import com.zyao89.view.zloading.ZLoadingDialog;
import com.zyao89.view.zloading.Z_TYPE;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2018/2/6.
 */

public class WaterForGetDelegate extends YSJDelegate {

    @BindView(R.id.btn_back)
    ImageView mBtnBack;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.tv_flag)
    SuperTextView mTvFlag;
    @BindView(R.id.tv_free_time)
    TextView mTvFreeTime;
    @BindView(R.id.btn_get)
    SuperButton mBtnGet;
    @BindView(R.id.tv_buy_time)
    TextView mTvBuyTime;
    @BindView(R.id.btn_buy)
    SuperButton mBtnBuy;
    @BindView(R.id.btn_share)
    SuperButton mBtnShare;
    @BindView(R.id.rv_price)
    RecyclerView mRvPrice;
    @BindView(R.id.tv_empty)
    TextView mTvEmpty;
    private String mMachineId;
    private String mTitle;
    private AlertDialog mAlertDialog;
    private UserInfoBean mUserInfoBean;
    private Bitmap mBitmap;
    private WechatShareModel mWechatShareModel;
    private ZLoadingDialog mCheckDialog;
    private RxDialog mDialog;

    @Override
    public Object setLayout() {
        return R.layout.delegate_home_water_get;
    }

    public static WaterForGetDelegate create(String mMachineId, String title) {
        WaterForGetDelegate waterForGetDelegate = new WaterForGetDelegate();
        Bundle bundle = new Bundle();
        bundle.putString("machineId", mMachineId);
        bundle.putString("Title", title);
        waterForGetDelegate.setArguments(bundle);
        return waterForGetDelegate;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arg = getArguments();
        if (arg != null) {
            mMachineId = arg.getString("machineId","");
            mTitle = arg.getString("Title","");
        }
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        mTvTitle.setText("饮水充值");
        mBtnGet.setClickable(false);
        initData();
        initRecyclerViewPrice();
        mAlertDialog = new AlertDialog.Builder(getActivity()).create();
        mDialog = new RxDialog(getActivity());
    }

    private void initRecyclerViewPrice() {
        mRvPrice.setLayoutManager(new GridLayoutManager(getContext(), 3));
        RequestParams params = new RequestParams("type", 0);
        RequestCenter.GetWaterMoneyList(params, new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                WaterMoneyListBean waterMoneyListBean = (WaterMoneyListBean) responseObj;
                mTvEmpty.setVisibility(View.GONE);
                mRvPrice.setVisibility(View.VISIBLE);
                final List<WaterMoneyListBean.DataBean> data = waterMoneyListBean.getData();
                WaterPriceAdapter waterPriceAdapter = new WaterPriceAdapter(R.layout.item_home_ad_price, data);
                waterPriceAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                        WaterMoneyListBean.DataBean dataBean = data.get(position);
                        String id = dataBean.getId();
                        double money = Double.valueOf(dataBean.getMoney());
                        getSupportDelegate().start(PayDelegate.create(money, id, 10021));
                    }
                });
                waterPriceAdapter.openLoadAnimation();
                mRvPrice.setAdapter(waterPriceAdapter);

            }

            @Override
            public void onFailure(Object responseObj) {
                OkHttpException error = (OkHttpException) responseObj;
                mTvEmpty.setVisibility(View.VISIBLE);
                mRvPrice.setVisibility(View.GONE);
                RxToast.normal("获取充值金额数据失败。(" + error.getEmsg() + ")");

            }
        });

    }

    private void initData() {
        RequestParams requestParams = Utils.getRequestParams();
        RequestCenter.GetUserInfo(requestParams, new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                mUserInfoBean = (UserInfoBean) responseObj;
                boolean freeWater_state = mUserInfoBean.isFreeWater_state();
                String freeWater = mUserInfoBean.getFreeWater();
                mTvFreeTime.setText("新注册会员\n获得" + freeWater + "次免费喝水机会");
                if (freeWater_state) {
                    //已领取
                    mBtnGet.setClickable(false);
                    mBtnGet.setShapeGradientEndColor(getResources().getColor(R.color.gray_button)).setUseShape();
                    mBtnGet.setShapeGradientStartColor(getResources().getColor(R.color.gray_button)).setUseShape();
                    mBtnGet.setText("已领取");
                } else {
                    //未领取
                    mBtnGet.setClickable(true);
                    mBtnGet.setShapeGradientEndColor(getResources().getColor(R.color.orange_low)).setUseShape();
                    mBtnGet.setShapeGradientStartColor(getResources().getColor(R.color.orange_high)).setUseShape();
                    mBtnGet.setText("立刻领取");
                }

            }

            @Override
            public void onFailure(Object responseObj) {
                OkHttpException error = (OkHttpException) responseObj;
                int errorCode = error.getCode();
                if (errorCode == 10000) {
                    RxToast.normal("账号异常，请重新登录");
                    AccountManager.setSignState(false);
                    getParentDelegate().getSupportDelegate().start(new SignInDelegate());
                } else {
                    RxToast.normal("获取用户数据失败(" + error.getEmsg() + ")");
                }
            }
        });
    }


    @OnClick({R.id.btn_back, R.id.btn_get, R.id.btn_buy, R.id.btn_share})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                getSupportDelegate().pop();
                break;
            case R.id.btn_get:
                RequestParams requestParams = Utils.getRequestParams();
                RequestCenter.UserGetFreeWater(requestParams, new DisposeDataListener() {
                    @Override
                    public void onSuccess(Object responseObj) {
                        RxToast.normal("领取成功");
                        mBtnGet.setClickable(false);
                        mBtnGet.setShapeGradientEndColor(getResources().getColor(R.color.gray_button)).setUseShape();
                        mBtnGet.setShapeGradientStartColor(getResources().getColor(R.color.gray_button)).setUseShape();
                        mBtnGet.setText("已领取");
                    }

                    @Override
                    public void onFailure(Object responseObj) {
                        OkHttpException error = (OkHttpException) responseObj;
                        RxToast.normal("领取失败 - ("+error.getEmsg()+")");
                    }
                });
                break;
            case R.id.btn_buy:
                if(mMachineId.isEmpty()){
                    if (hasPermission(Constant.HARDWEAR_CAMERA_PERMISSION)) {
                        doOpenCamera();
                    } else {
                        requestPermission(Constant.HARDWEAR_CAMERA_CODE, Constant.HARDWEAR_CAMERA_PERMISSION);
                    }
                }else{
                    getSupportDelegate().start(ShoppingDelegate.create(mMachineId));
                }

                break;
            case R.id.btn_share:
                RxToast.normal("初始化分享内容中...,请稍等");
                RequestParams Params = Utils.getRequestParams();
                RequestCenter.GetShareInfo(Params, new DisposeDataListener() {
                    @Override
                    public void onSuccess(Object responseObj) {
                        ShareInfoBean shareInfoBean = (ShareInfoBean) responseObj;
                        showDialog(shareInfoBean);
                    }

                    @Override
                    public void onFailure(Object responseObj) {
                        OkHttpException error = (OkHttpException) responseObj;
                        RxToast.normal("获取分享数据失败 - (" + error.getEmsg() + ")");
                    }
                });
                break;
        }
    }
    @Override
    public void doOpenCamera() {
        Intent intent = new Intent(getActivity(), CaptureActivity.class);
        startActivityForResult(intent, RequestCodes.SCAN);
    }

    private void showDialog(final ShareInfoBean shareInfoBean) {
        final String image = shareInfoBean.getImage();
        new Thread(new Runnable() {
            @Override
            public void run() {
                mBitmap = RxImageTool.GetLocalOrNetBitmap(HttpConstants.ROOT_URL + image);
                final String appId = ShareYSJ.getConfiguration(ConfigKeys.WE_CHAT_APP_ID);
                WechatShareTools.init(getContext(), appId);//初始化
                final String url = HttpConstants.ROOT_URL+shareInfoBean.getUrl();//网页链接
                getProxyActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAlertDialog.show();
                        final Window window = mAlertDialog.getWindow();
                        if (window != null) {
                            window.setContentView(R.layout.dialog_share_panel);
                            window.setGravity(Gravity.BOTTOM);
                            window.setWindowAnimations(R.style.anim_panel_up_from_bottom);
                            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            //设置属性
                            final WindowManager.LayoutParams params = window.getAttributes();
                            params.width = WindowManager.LayoutParams.MATCH_PARENT;
                            params.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
                            params.dimAmount = 0.5f;
                            window.setAttributes(params);

                            window.findViewById(R.id.btn_wx_friend).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //分享至微信朋友
                                    //Friend 分享微信好友,Zone 分享微信朋友圈,Favorites 分享微信收藏
                                    if (mBitmap == null) {
                                        RxToast.normal("mBitmap空");
                                        mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_launch);//获取Bitmap
                                    }
                                    byte[] bitmapByte = Utils.bitmap2Bytes(mBitmap, 32);//将 Bitmap 转换成 byte[]
                                    mWechatShareModel = new WechatShareModel(url, shareInfoBean.getTitle(), shareInfoBean.getContent(), bitmapByte);
                                    WechatShareTools.shareURL(mWechatShareModel, WechatShareTools.SharePlace.Friend);//分享操作
                                    mAlertDialog.cancel();
                                }
                            });
                            window.findViewById(R.id.btn_wx_circle).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (mBitmap == null) {
                                        RxToast.normal("mBitmap空");
                                        mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_launch);//获取Bitmap
                                    }
                                    byte[] bitmapByte = Utils.bitmap2Bytes(mBitmap, 32);//将 Bitmap 转换成 byte[]

                                    mWechatShareModel = new WechatShareModel(url, shareInfoBean.getTitle(), shareInfoBean.getContent(), bitmapByte);
                                    WechatShareTools.shareURL(mWechatShareModel, WechatShareTools.SharePlace.Zone);//分享操作
                                    mAlertDialog.cancel();
                                }
                            });

                        }
                    }
                });
            }
        }).start();
    }

    @Override
    public void onActivityResult(final int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case RequestCodes.SCAN:
                if (resultCode == Activity.RESULT_OK) {
                    final String code = data.getStringExtra("SCAN_RESULT");
                    RequestParams requestParams = Utils.getRequestParams();
                    requestParams.put("machineid", code+"");
                    RequestCenter.GetMachineInfo(requestParams, new DisposeDataListener() {
                        @Override
                        public void onSuccess(Object responseObj) {
                            ShopOrderBean machineInfoBean = (ShopOrderBean) responseObj;
                            int resultCode = Integer.parseInt(machineInfoBean.getCode());
                            if(resultCode == 0){
                                //绑定机器
                                String id = machineInfoBean.getId();
                                mMachineId = id;
                                getSupportDelegate().start(ShoppingDelegate.create(mMachineId));
                            }else if(resultCode == -1){
                                //点点物
                                getParentDelegate().getSupportDelegate().start(ShopInfoDelegate.create(machineInfoBean));
                            }else if(resultCode == -2){
                                //饮水
                                final String uuid = machineInfoBean.getUuid();
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
                            }else{
                                RxToast.normal("操作失败");
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

                }
                break;
        }
    }
    private void checkResult(String uuid) {
        final RequestParams requestParams = Utils.getRequestParams();
        requestParams.put("uuid",uuid);
        RequestCenter.VerificationWater(requestParams, new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                RxToast.normal("饮水成功");
                mCheckDialog.cancel();
            }

            @Override
            public void onFailure(Object responseObj) {
                OkHttpException error = (OkHttpException) responseObj;
                RxToast.normal("出水情况："+error.getEmsg());
                mCheckDialog.cancel();
            }
        });
    }

}
