package com.gxysj.shareysj.delegate.main.home;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.gxysj.shareysj.bean.GetAppHomeInfoBean;
import com.gxysj.shareysj.bean.GiftBagBean;
import com.gxysj.shareysj.bean.ShopOrderBean;
import com.gxysj.shareysj.bean.UserGiftBean;
import com.gxysj.shareysj.config.Constant;
import com.gxysj.shareysj.config.ShareYSJ;
import com.gxysj.shareysj.delegate.PayDelegate;
import com.gxysj.shareysj.delegate.ShopInfoDelegate;
import com.gxysj.shareysj.delegate.SignInDelegate;
import com.gxysj.shareysj.delegate.base.YSJDelegate;
import com.gxysj.shareysj.delegate.main.home.adapter.GiftCenterAdapter;
import com.gxysj.shareysj.delegate.main.home.adapter.UserGiftAdapter;
import com.gxysj.shareysj.delegate.main.mine.adapter.ReChargeAdapter;
import com.gxysj.shareysj.network.RequestCenter;
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

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.R.attr.id;
import static android.R.attr.mimeType;

/**
 * Created by Administrator on 2018/2/7.
 */

public class GiftDelegate extends YSJDelegate {


    @BindView(R.id.btn_back)
    ImageView mBtnBack;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.tv_flag)
    SuperTextView mTvFlag;
    @BindView(R.id.tv_empty_gift)
    TextView mTvEmptyGift;
    @BindView(R.id.rv_gift)
    RecyclerView mRvGift;
    @BindView(R.id.tv_empty)
    TextView mTvEmpty;
    @BindView(R.id.rv_user_gift)
    RecyclerView mRvUserGift;
    Unbinder unbinder;
    private String mTitle;
    private GiftCenterAdapter mGiftCenterAdapter;
    private List<GiftBagBean.DateBean> mGiftList;
    private List<UserGiftBean.DateBean> mUserGiftList;
    private RxDialog mDialog;
    private ZLoadingDialog mCheckDialog;
    private String mMachineId;
    private int mUserGiftListIndex = 0;
    private UserGiftAdapter mUserGiftAdapter;

    @Override
    public Object setLayout() {
        return R.layout.delegate_home_gift;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        mTvTitle.setText(mTitle);
        initData();
        mDialog = new RxDialog(getActivity());
        mCheckDialog = new ZLoadingDialog(ShareYSJ.getActivity());
    }

    private void initData() {
        mRvUserGift.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRvGift.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRvUserGift.setNestedScrollingEnabled(false);
        mRvGift.setNestedScrollingEnabled(false);
        RequestParams requestParams = Utils.getRequestParams();
        RequestCenter.GiftBagList(requestParams, new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                GiftBagBean giftBagBean = (GiftBagBean) responseObj;
                mGiftList = giftBagBean.getDate();
                if (mGiftList != null && mGiftList.size() > 0) {
                    mRvGift.setVisibility(View.VISIBLE);
                    mTvEmptyGift.setVisibility(View.GONE);
                    mGiftCenterAdapter = new GiftCenterAdapter(R.layout.item_home_gift_center, mGiftList);
                    mGiftCenterAdapter.openLoadAnimation();
                    mGiftCenterAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                        @Override
                        public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                            GiftBagBean.DateBean dateBean = mGiftList.get(position);
                            if (dateBean == null) {
                                return;
                            }
                            String state = dateBean.getState();
                            if (state.equals("0")) {
                                //未领取
                                getGift(dateBean, position);
                            } else {
                                return;
                            }
                        }
                    });
                    mRvGift.setAdapter(mGiftCenterAdapter);
                } else {
                    mRvGift.setVisibility(View.GONE);
                    mTvEmptyGift.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Object responseObj) {
                OkHttpException error = (OkHttpException) responseObj;
                mRvGift.setVisibility(View.GONE);
                mTvEmptyGift.setVisibility(View.VISIBLE);
                RxToast.normal("获取数据失败-(" + error.getEmsg() + ")");
            }
        });
        initUserGift(requestParams);

    }

    private void initUserGift(RequestParams requestParams) {
        RequestCenter.GetUserGiftBagList(requestParams, new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                UserGiftBean giftBagBean = (UserGiftBean) responseObj;
                mUserGiftList = giftBagBean.getDate();
                if (mUserGiftList != null && mUserGiftList.size() > 0) {
                    mRvUserGift.setVisibility(View.VISIBLE);
                    mTvEmpty.setVisibility(View.GONE);
                    mUserGiftAdapter = new UserGiftAdapter(R.layout.item_home_gift_center, mUserGiftList);
                    mUserGiftAdapter.openLoadAnimation();
                    mUserGiftAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                        @Override
                        public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                            mUserGiftListIndex = position;
                            showDialog();
                        }
                    });
                    mRvUserGift.setAdapter(mUserGiftAdapter);
                } else {
                    mRvUserGift.setVisibility(View.GONE);
                    mTvEmpty.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Object responseObj) {
                OkHttpException error = (OkHttpException) responseObj;
                mRvUserGift.setVisibility(View.GONE);
                mTvEmpty.setVisibility(View.VISIBLE);
                RxToast.normal("获取数据失败-(" + error.getEmsg() + ")");
            }
        });
    }

    private void showDialog() {
        mDialog.getLayoutParams().gravity = Gravity.CENTER;
        View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_commit, null);
        TextView mTvTitle = (TextView) dialogView.findViewById(R.id.tv_title);
        mTvTitle.setText("提示\n请先扫码绑定售货机,并确认售货机上是否存在该商品。(使用后不可退)");
        mTvTitle.setTextSize(13);
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

    private void getGift(final GiftBagBean.DateBean dateBean, final int position) {
        String id = dateBean.getId();
        final RequestParams requestParams = Utils.getRequestParams();
        requestParams.put("bagid", id);
        RequestCenter.UserReceiveGiftBagList(requestParams, new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                RxToast.normal("领取成功");
                mGiftList.get(position).setState("1");
                mGiftCenterAdapter.notifyItemChanged(position);
                if (dateBean.getType().equals("1")) {
                    initUserGift(requestParams);
                }


            }

            @Override
            public void onFailure(Object responseObj) {
                OkHttpException error = (OkHttpException) responseObj;
                RxToast.normal("领取失败-(" + error.getEmsg() + ")");
            }
        });
    }

    private void userGift() {
        UserGiftBean.DateBean dateBean = mUserGiftList.get(mUserGiftListIndex);
        if(dateBean==null){
            RxToast.normal("使用失败");
            return;
        }
        RequestParams requestParams = Utils.getRequestParams();
        requestParams.put("bagid", dateBean.getId());
        requestParams.put("machineid", mMachineId);
        RequestCenter.UserUseReceiveGiftBag(requestParams, new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                mCheckDialog.setLoadingBuilder(Z_TYPE.ROTATE_CIRCLE)//设置类型
                        .setLoadingColor(Color.GRAY)//颜色
                        .setHintText("商品掉落中...")
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
                            Thread.sleep(15000);//休眠3秒
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mCheckDialog.cancel();
                                mUserGiftList.remove(mUserGiftListIndex);
                                mUserGiftAdapter.notifyDataSetChanged();
                                RxToast.normal("成功使用礼包");
                                if(mUserGiftList==null ){
                                    mRvUserGift.setVisibility(View.GONE);
                                    mTvEmpty.setVisibility(View.VISIBLE);
                                }

                            }
                        });

                    }
                }.start();
            }

            @Override
            public void onFailure(Object responseObj) {
                OkHttpException error = (OkHttpException) responseObj;
                RxToast.normal("使用失败-(" + error.getEmsg() + ")");
            }
        });
    }

    public static GiftDelegate create(String title) {
        GiftDelegate giftDelegate = new GiftDelegate();
        Bundle bundle = new Bundle();
        bundle.putString("Title", title);
        giftDelegate.setArguments(bundle);
        return giftDelegate;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        mTitle = arguments.getString("Title");
    }


    @OnClick(R.id.btn_back)
    public void onViewClicked() {
        getSupportDelegate().pop();
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
                                userGift();
                            }else if(resultCode == -1){
                                //点点物
                                getParentDelegate().getSupportDelegate().start(ShopInfoDelegate.create(machineInfoBean));
                            }else if(resultCode == -2){
                                //饮水
                                final String uuid = machineInfoBean.getUuid();

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
}
