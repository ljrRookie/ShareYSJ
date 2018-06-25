package com.gxysj.shareysj.delegate.main.home;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.allen.library.SuperTextView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gxysj.shareysj.R;
import com.gxysj.shareysj.bean.GetAppHomeInfoBean;
import com.gxysj.shareysj.bean.GetCodeBean;
import com.gxysj.shareysj.bean.MachineInfoBean;
import com.gxysj.shareysj.bean.ShareInfoBean;
import com.gxysj.shareysj.bean.ShopOrderBean;
import com.gxysj.shareysj.codes.WaterType;
import com.gxysj.shareysj.config.Constant;
import com.gxysj.shareysj.config.ShareYSJ;
import com.gxysj.shareysj.config_enum.ConfigKeys;
import com.gxysj.shareysj.delegate.PayDelegate;
import com.gxysj.shareysj.delegate.ShopInfoDelegate;
import com.gxysj.shareysj.delegate.SignInDelegate;
import com.gxysj.shareysj.delegate.home.BottomItemDelegate;
import com.gxysj.shareysj.delegate.home.ColdWaterOneDelegate;
import com.gxysj.shareysj.delegate.home.WaterForGetDelegate;
import com.gxysj.shareysj.delegate.main.home.adapter.HomeItemAdapter;
import com.gxysj.shareysj.delegate.main.mine.adapter.ReChargeAdapter;
import com.gxysj.shareysj.listener.IUserChecker;
import com.gxysj.shareysj.network.HttpConstants;
import com.gxysj.shareysj.network.RequestCenter;
import com.gxysj.shareysj.utils.GlideImageLoader;
import com.gxysj.shareysj.utils.SPUtil;
import com.gxysj.shareysj.utils.camear.RequestCodes;
import com.gxysj.shareysj.utils.user_manager.AccountManager;
import com.gxysj.shareysj.utils.user_manager.Utils;
import com.gxysj.shareysj.zxing.app.CaptureActivity;
import com.ljr.delegate_sdk.okhttp.exception.OkHttpException;
import com.ljr.delegate_sdk.okhttp.listener.DisposeDataListener;
import com.ljr.delegate_sdk.okhttp.request.RequestParams;
import com.vondear.rxtools.RxActivityTool;
import com.vondear.rxtools.RxImageTool;
import com.vondear.rxtools.module.wechat.share.WechatShareModel;
import com.vondear.rxtools.module.wechat.share.WechatShareTools;
import com.vondear.rxtools.view.RxToast;
import com.vondear.rxtools.view.dialog.RxDialog;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;
import com.zyao89.view.zloading.ZLoadingDialog;
import com.zyao89.view.zloading.Z_TYPE;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;


/**
 * Created by Administrator on 2018/1/29.
 */

public class HomeDelegate extends BottomItemDelegate {

    @BindView(R.id.banner)
    Banner mBanner;
    @BindView(R.id.scroll)
    NestedScrollView mScroll;
    @BindView(R.id.refresh)
    SwipeRefreshLayout mRefresh;
    @BindView(R.id.rl_search)
    RelativeLayout mRlSearch;
    @BindView(R.id.btn_scaner)
    ImageView mBtnScaner;
    @BindView(R.id.btn_share)
    ImageView mBtnShare;
    @BindView(R.id.rv_item)
    RecyclerView mRvItem;
    @BindView(R.id.rv_recharge)
    RecyclerView mRvRecharge;
    Unbinder unbinder;
    @BindView(R.id.tv_flag)
    SuperTextView mTvFlag;
    Unbinder unbinder1;
    private RxDialog mDialog;
    private AlertDialog mAlertDialog;
    private Bitmap mBitmap;
    private WechatShareModel mWechatShareModel;
    private String mMachineId = "";
    //是否已经登录
    private boolean isSign = false;

    //是否绑定机器
    private boolean isBindMachine = false;
    private ZLoadingDialog mCheckDialog;

    @Override
    public Object setLayout() {
        return R.layout.delegate_home;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        isBindMachine = false;
        mAlertDialog = new AlertDialog.Builder(getActivity()).create();
        initData();
        initScanDialog();
        initRefreshLayout();
        mDialog = new RxDialog(getActivity());

        mTvFlag.setRightTvClickListener(new SuperTextView.OnRightTvClickListener() {
            @Override
            public void onClickListener() {
                if (checkIsSign()) {
                    getParentDelegate().getSupportDelegate().start(WaterForGetDelegate.create(mMachineId, ""));
                } else {
                    getParentDelegate().getSupportDelegate().start(new SignInDelegate());
                }
            }
        });
    }

    private void initScanDialog() {
        mCheckDialog = new ZLoadingDialog(ShareYSJ.getActivity());
        mCheckDialog.setLoadingBuilder(Z_TYPE.ROTATE_CIRCLE)//设置类型
                .setLoadingColor(Color.GRAY)//颜色
                .setHintText("出水中....")
                .setHintTextSize(14) // 设置字体大小 dp
                .setCancelable(false)
                .setCanceledOnTouchOutside(false)
                .setHintTextColor(Color.BLACK)  // 设置字体颜色
                .setDurationTime(0.5) // 设置动画时间百分比 - 0.5倍
                .setDialogBackgroundColor(Color.TRANSPARENT);
    }

    private void initData() {
        mRvRecharge.setLayoutManager(new GridLayoutManager(getContext(), 3));
        RequestCenter.GetAppHomeInfo(getActivity(), new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                GetAppHomeInfoBean appHomeInfoBean = (GetAppHomeInfoBean) responseObj;
                List<GetAppHomeInfoBean.DataBean> data = appHomeInfoBean.getData();
                initBanner(data);
                final List<GetAppHomeInfoBean.Data1Bean> data1 = appHomeInfoBean.getData1();
                if (data1 != null && data1.size() > 0) {
                    ReChargeAdapter reChargeAdapter = new ReChargeAdapter(R.layout.item_home_recharge, data1);
                    reChargeAdapter.openLoadAnimation();
                    reChargeAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                            if (checkIsSign()) {
                                GetAppHomeInfoBean.Data1Bean data1Bean = data1.get(position);
                                double money = Double.valueOf(data1Bean.getMoney());
                                String id = data1Bean.getId();
                                getParentDelegate().getSupportDelegate().start(PayDelegate.create(money, id, 10021));
                            } else {
                                getParentDelegate().getSupportDelegate().start(new SignInDelegate());
                            }


                        }
                    });
                    mRvRecharge.setAdapter(reChargeAdapter);
                }

                List<GetAppHomeInfoBean.Data2Bean> data2 = appHomeInfoBean.getData2();
                initItem(data2);
            }

            @Override
            public void onFailure(Object responseObj) {
                OkHttpException error = (OkHttpException) responseObj;
                RxToast.normal("获取首页数据失败-(" + error.getEmsg() + ")");
            }
        });
    }

    private void initItem(final List<GetAppHomeInfoBean.Data2Bean> data2) {
        if (data2 != null && data2.size() > 0) {
            for (int i = 0; i < data2.size(); i++) {
                GetAppHomeInfoBean.Data2Bean data2Bean = data2.get(i);
                int index = Integer.valueOf(data2Bean.getId());
                if (index == 1) {
                    SPUtil.put(ShareYSJ.getApplicationContext(), WaterType.Water_Default, data2.get(i).getName());
                }
                if (index == 4) {
                    SPUtil.put(ShareYSJ.getApplicationContext(), WaterType.Water_Hot, data2.get(i).getName());
                }
                if (index == 5) {
                    SPUtil.put(ShareYSJ.getApplicationContext(), WaterType.Water_Brand, data2.get(i).getName());
                }
            }
            HomeItemAdapter homeItemAdapter = new HomeItemAdapter(R.layout.item_home, data2);
            homeItemAdapter.openLoadAnimation();
            homeItemAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    GetAppHomeInfoBean.Data2Bean data2Bean = data2.get(position);
                    int index = Integer.valueOf(data2Bean.getId());
                    if (index == 1) {
                        if (checkIsSign()) {
                            if (isBindMachine) {
                                getParentDelegate().getSupportDelegate().start(ColdWaterOneDelegate.create(Integer.parseInt(WaterType.Water_Default), data2.get(position).getName(), mMachineId));
                            } else {
                                showDialogBindMachine();
                            }

                        } else {
                            getParentDelegate().getSupportDelegate().start(new SignInDelegate());
                        }
                    } else if (index == 2) {
                        if (checkIsSign()) {
                            if (isBindMachine) {
                                getParentDelegate().getSupportDelegate().start(ShoppingDelegate.create(mMachineId));
                            } else {
                                showDialogBindMachine();
                            }

                        } else {
                            getParentDelegate().getSupportDelegate().start(new SignInDelegate());
                        }
                    } else if (index == 3) {
                        if (checkIsSign()) {
                            getParentDelegate().getSupportDelegate().start(new AdDelegate());
                        } else {
                            getParentDelegate().getSupportDelegate().start(new SignInDelegate());
                        }

                    } else if (index == 4) {
                        if (checkIsSign()) {
                            if (isBindMachine) {
                                getParentDelegate().getSupportDelegate().start(ColdWaterOneDelegate.create(Integer.parseInt(WaterType.Water_Hot), data2.get(position).getName(), mMachineId));
                            } else {
                                showDialogBindMachine();
                            }
                        } else {
                            getParentDelegate().getSupportDelegate().start(new SignInDelegate());
                        }
                    } else if (index == 5) {
                        if (checkIsSign()) {
                            if (isBindMachine) {
                                getParentDelegate().getSupportDelegate().start(ColdWaterOneDelegate.create(Integer.parseInt(WaterType.Water_Brand), data2.get(position).getName(), mMachineId));
                            } else {
                                showDialogBindMachine();
                            }
                        } else {
                            getParentDelegate().getSupportDelegate().start(new SignInDelegate());
                        }
                    } else if (index == 6) {
                        RxActivityTool.skipActivity(getActivity(), MapActivity.class);
                    } else if (index == 7) {
                        if (checkIsSign()) {
                            getParentDelegate().getSupportDelegate().start(RedBagGameDelegate.create(data2.get(position).getName()));
                        } else {
                            getParentDelegate().getSupportDelegate().start(new SignInDelegate());
                        }
                    } else if (index == 8) {
                        if (checkIsSign()) {
                            getParentDelegate().getSupportDelegate().start(GiftDelegate.create(data2.get(position).getName()));
                        } else {
                            getParentDelegate().getSupportDelegate().start(new SignInDelegate());
                        }


                    } else if (index == 9) {
                        if (checkIsSign()) {
                            if (isBindMachine) {
                                getParentDelegate().getSupportDelegate().start(ChargeDelegate.create(data2.get(position).getName(), mMachineId));
                            } else {
                                showDialogBindMachine();
                            }
                        } else {
                            getParentDelegate().getSupportDelegate().start(new SignInDelegate());
                        }
                    }
                }
            });
            mRvItem.setAdapter(homeItemAdapter);
            mRvItem.setHasFixedSize(true);
            mRvItem.setNestedScrollingEnabled(false);
        } else {

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

    private void initBanner(final List<GetAppHomeInfoBean.DataBean> data) {
        mBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        mBanner.setIndicatorGravity(BannerConfig.CENTER);
        List<String> images = new ArrayList<>();
        mBanner.setImageLoader(new GlideImageLoader());
        if (data != null && data.size() > 0) {
            int bannerSize = data.size();
            for (int i = 0; i < bannerSize; i++) {
                images.add(HttpConstants.ROOT_URL + data.get(i).getImage());
            }
            mBanner.setImages(images);
            mBanner.start();
            mBanner.setOnBannerListener(new OnBannerListener() {
                @Override
                public void OnBannerClick(int position) {
                    GetAppHomeInfoBean.DataBean dataBean = data.get(position);
                    String id = dataBean.getId();
                    getParentDelegate().getSupportDelegate().start(WebViewDelegate.create(id));
                }
            });
        }


    }

    private void initRefreshLayout() {
        mRvItem.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        mScroll.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == 0) {
                    // 顶部
                    mRefresh.setEnabled(true);
                }
            }
        });
        mRefresh.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light
        );
        mRefresh.setProgressViewOffset(true, 120, 300);
        mRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
                mRefresh.setRefreshing(false);

            }
        });
    }


    @OnClick(R.id.rl_search)
    public void onViewClicked() {
        getParentDelegate().getSupportDelegate().start(new SearchDelegate());
    }


    @OnClick({R.id.btn_scaner, R.id.btn_share})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_scaner:
                if (checkIsSign()) {
                    if (hasPermission(Constant.HARDWEAR_CAMERA_PERMISSION)) {
                        doOpenCamera();
                    } else {
                        requestPermission(Constant.HARDWEAR_CAMERA_CODE, Constant.HARDWEAR_CAMERA_PERMISSION);
                    }
                } else {
                    getParentDelegate().getSupportDelegate().start(SignInDelegate.create(0, ""));
                }

                break;
            case R.id.btn_share:
                if (checkIsSign()) {
                    RequestParams requestParams = Utils.getRequestParams();
                    RequestCenter.GetShareInfo(requestParams, new DisposeDataListener() {
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
                } else {
                    getParentDelegate().getSupportDelegate().start(SignInDelegate.create(0, ""));
                }
                break;
            default:
                break;
        }
    }


    private void showDialog(final ShareInfoBean shareInfoBean) {
        final String image = shareInfoBean.getImage();
        new Thread(new Runnable() {
            @Override
            public void run() {
                mBitmap = RxImageTool.GetLocalOrNetBitmap(HttpConstants.ROOT_URL + image);
                final String appId = ShareYSJ.getConfiguration(ConfigKeys.WE_CHAT_APP_ID);
                WechatShareTools.init(getContext(), appId);//初始化
                final String url = HttpConstants.ROOT_URL + shareInfoBean.getUrl();//网页链接
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

    private boolean checkIsSign() {
        //地图绑定
        String mapMachineId = (String) SPUtil.get(ShareYSJ.getApplicationContext(), "MACHINEID", "");
        if (mapMachineId.isEmpty()) {
            isBindMachine = false;
        } else {
            isBindMachine = true;
            mMachineId = mapMachineId;
        }
        Log.e("clear", "machineid :"+mapMachineId+"  boolean :"+isBindMachine);

        AccountManager.checkAccount(new IUserChecker() {
            @Override
            public void onSignIn() {
                isSign = true;
            }

            @Override
            public void onNotSignIn() {
                isSign = false;
            }
        });
        return isSign;
    }


    @Override
    public void doOpenCamera() {
        Intent intent = new Intent(getActivity(), CaptureActivity.class);
        startActivityForResult(intent, RequestCodes.SCAN);
    }

    @Override
    public void onActivityResult(final int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case RequestCodes.SCAN:
                if (resultCode == Activity.RESULT_OK) {
                    final String code = data.getStringExtra("SCAN_RESULT");
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
                                SPUtil.put(ShareYSJ.getApplicationContext(), "MACHINEID", mMachineId);
                                isBindMachine = true;
                            } else if (resultCode == -1) {
                                //点点物
                                getParentDelegate().getSupportDelegate().start(ShopInfoDelegate.create(machineInfoBean));
                            } else if (resultCode == -2) {
                                //饮水
                                final String uuid = machineInfoBean.getUuid();
                              // 设置背景色，默认白色
                                mCheckDialog.setHintText("出水中....").show();
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
                            } else if (resultCode == -3){
                               //充电
                                mCheckDialog.setHintText("准备充电中....").show();
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
                            }else{
                                RxToast.normal("操作失败");
                            }


                        }

                        @Override
                        public void onFailure(Object responseObj) {
                            OkHttpException error = (OkHttpException) responseObj;
                            int errorCode = error.getCode();
                            if (errorCode == 10000) {
                                RxToast.normal("登录异常，请重新登录");
                                AccountManager.setSignState(false);
                                getParentDelegate().getSupportDelegate().start(SignInDelegate.create(0, ""));
                            } else {
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

    @Override
    public void onStop() {

        Log.e("clear", "home onStop");
        String machineid = (String) SPUtil.get(getActivity(), "MACHINEID", "");
        if(!machineid.isEmpty()){
            SPUtil.remove(getActivity(), "MACHINEID", "");
            isBindMachine = false;
            Log.e("clear", "machineid 不为空"+machineid+"boolean :"+isBindMachine);

      }else{
            Log.e("clear", "machineid 为空");
        }
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.e("clear", "home onDestroyView");

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("clear", "home onDestroy");
    }
}
