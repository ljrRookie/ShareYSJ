package com.gxysj.shareysj.delegate.main.generalize;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.allen.library.SuperTextView;
import com.bumptech.glide.Glide;
import com.gxysj.shareysj.R;
import com.gxysj.shareysj.bean.GetUserDistributionInfoBean;
import com.gxysj.shareysj.bean.ShareInfoBean;
import com.gxysj.shareysj.bean.event.EventEmpty;
import com.gxysj.shareysj.config.ShareYSJ;
import com.gxysj.shareysj.config_enum.ConfigKeys;
import com.gxysj.shareysj.delegate.SignInDelegate;
import com.gxysj.shareysj.delegate.home.BottomItemDelegate;
import com.gxysj.shareysj.network.HttpConstants;
import com.gxysj.shareysj.network.RequestCenter;
import com.gxysj.shareysj.utils.SPUtil;
import com.gxysj.shareysj.utils.TimeUtils;
import com.gxysj.shareysj.utils.user_manager.AccountManager;
import com.gxysj.shareysj.utils.user_manager.Utils;
import com.ljr.delegate_sdk.okhttp.exception.OkHttpException;
import com.ljr.delegate_sdk.okhttp.listener.DisposeDataListener;
import com.ljr.delegate_sdk.okhttp.request.RequestParams;
import com.vondear.rxtools.RxImageTool;
import com.vondear.rxtools.RxTimeTool;
import com.vondear.rxtools.module.wechat.share.WechatShareModel;
import com.vondear.rxtools.module.wechat.share.WechatShareTools;
import com.vondear.rxtools.view.RxToast;
import com.vondear.rxtools.view.dialog.RxDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;
import qiu.niorgai.StatusBarCompat;


/**
 * Created by Administrator on 2018/1/29.
 */

public class MyGeneralizeDelegate extends BottomItemDelegate {

    @BindView(R.id.btn_back)
    ImageView mBtnBack;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.btn_right)
    ImageView mBtnRight;
    @BindView(R.id.iv_pic)
    CircleImageView mIvPic;

    @BindView(R.id.tv_name)
    TextView mTvName;
    @BindView(R.id.tv_code)
    TextView mTvCode;
    @BindView(R.id.tv_flag)
    TextView mTvFlag;
    @BindView(R.id.btn_withdraw)
    SuperTextView mBtnWithdraw;
    @BindView(R.id.iv_q)
    ImageView mIvQ;
    @BindView(R.id.money)
    TextView mMoney;
    @BindView(R.id.tv_money)
    TextView mTvMoney;
    @BindView(R.id.btn_bonus)
    RelativeLayout mBtnBonus;
    @BindView(R.id.iv_w)
    ImageView mIvW;
    @BindView(R.id.detail)
    TextView mDetail;
    @BindView(R.id.btn_bonus_detail)
    RelativeLayout mBtnBonusDetail;
    @BindView(R.id.iv_e)
    ImageView mIvE;
    @BindView(R.id.user)
    TextView mUser;
    @BindView(R.id.tv_low)
    TextView mTvLow;
    @BindView(R.id.btn_downline)
    RelativeLayout mBtnDownline;
    @BindView(R.id.iv_p)
    ImageView mIvP;
    @BindView(R.id.join)
    TextView mJoin;
    @BindView(R.id.btn_alliance)
    RelativeLayout mBtnAlliance;
    @BindView(R.id.tv_apply_money)
    TextView mTvApplyMoney;
    @BindView(R.id.tv_money_detail)
    TextView mTvMoneyDetail;
    @BindView(R.id.refresh)
    SwipeRefreshLayout mRefresh;
    Unbinder unbinder;
    private AlertDialog mAlertDialog;

    private RxDialog mDialog;
    private Bitmap mBitmap;
    private WechatShareModel mWechatShareModel;

    @Override
    public Object setLayout() {
        return R.layout.delegate_generalize;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {

        mBtnBack.setVisibility(View.GONE);
        EventBus.getDefault().register(this);
        mBtnRight.setVisibility(View.VISIBLE);
        mTvTitle.setText("我的推广");
        mAlertDialog = new AlertDialog.Builder(getActivity()).create();
        mDialog = new RxDialog(getActivity());
        initRefreshLayout();

    }

    private void initRefreshLayout() {
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

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        initData();
    }

    /**
     * 个人中心修改资料的回调
     * @param messageEvent
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void readInfo(EventEmpty messageEvent) {
        Uri image = messageEvent.getImage();
        String str = messageEvent.getStr();
        if (!TextUtils.isEmpty(str)) {
            mTvName.setText(str);
        }
        if (image != null) {
            Glide.with(getActivity()).load(image).into(mIvPic);

        }

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void initData() {
        RequestParams requestParams = Utils.getRequestParams();
        RequestCenter.GetUserDistributionInfo(requestParams, new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                GetUserDistributionInfoBean userDistributionInfoBean = (GetUserDistributionInfoBean) responseObj;
                String image = userDistributionInfoBean.getImage();
                Glide.with(getActivity()).load(HttpConstants.ROOT_URL + image).into(mIvPic);
                mTvName.setText(userDistributionInfoBean.getName());
                mTvCode.setText("邀请码：" + userDistributionInfoBean.getCode());
                mTvFlag.setText("累积奖金：" + userDistributionInfoBean.getCommissionCount() + "元");
                mTvApplyMoney.setText(userDistributionInfoBean.getCommission());
                mTvLow.setText(userDistributionInfoBean.getOffline() + "人");
                mTvMoney.setText(userDistributionInfoBean.getCommission() + "元");
                mTvMoneyDetail.setText(userDistributionInfoBean.getCommissionCount() + "元");

            }

            @Override
            public void onFailure(Object responseObj) {
                OkHttpException error = (OkHttpException) responseObj;

                int errorCode = error.getCode();
                if(errorCode == 10000){
                    RxToast.normal("登录异常，请重新登录 " );
                    AccountManager.setSignState(false);
                    getParentDelegate().getSupportDelegate().start(new SignInDelegate());
                }else{
                    RxToast.normal("获取数据失败 - (" + error.getEmsg() + ")");
                }
            }
        });
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


    @OnClick({R.id.btn_right, R.id.btn_withdraw, R.id.btn_bonus, R.id.btn_bonus_detail, R.id.btn_downline, R.id.btn_alliance})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_right:
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
                break;
            case R.id.btn_withdraw:
                showTake();
                break;
            case R.id.btn_bonus:
                break;
            case R.id.btn_bonus_detail:
                getParentDelegate().getSupportDelegate().start(new MyGeneralizeBounsDetailDelegate());
                break;
            case R.id.btn_downline:
                getParentDelegate().getSupportDelegate().start(new MyDownLineDelegate());
                break;
            case R.id.btn_alliance:
                toAlliance();
                break;
        }
    }

    private void toAlliance() {
        RequestParams requestParams = Utils.getRequestParams();
        RequestCenter.GetFranchiserInfo(requestParams, new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                getParentDelegate().getSupportDelegate().start(new AllianceDelegate());
            }

            @Override
            public void onFailure(Object responseObj) {
                OkHttpException error = (OkHttpException) responseObj;
                int errorCode = error.getCode();
                if (errorCode == 10097) {
                    getParentDelegate().getSupportDelegate().start(new RegisterAllianceDelegate());
                } else if (errorCode == 10098) {
                    RxToast.normal("加盟商审核中...");
                }else if(errorCode == 10099){
                    RxToast.normal("加盟商审核不通过...");
                    String reason = (String) error.getEmsg();
                    RxToast.normal("审核失败 :" + reason+"（请重新提交审核）" ,8);
                    getParentDelegate().getSupportDelegate().start(new RegisterAllianceDelegate());
                }else if(errorCode == 10093){
                    RxToast.normal("未通过实名认证,请通过后再申请加盟商..,",5);
                }else{
                    RxToast.normal("操作失败 - （" + error.getEmsg() + ")");
                }
            }
        });
    }

    private void showTake() {
        mDialog.getLayoutParams().gravity = Gravity.CENTER;
        View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_take_panel, null);
        final TextView dropDownView = (TextView) dialogView.findViewById(R.id.ddv);
        final EditText editMoney = (EditText) dialogView.findViewById(R.id.edit_money);
        final EditText editAccount = (EditText) dialogView.findViewById(R.id.edit_account);
        final EditText editUserName = (EditText) dialogView.findViewById(R.id.edit_username);
        dialogView.findViewById(R.id.btn_commit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String moneyStr = editMoney.getText().toString().trim();
                String accountStr = editAccount.getText().toString().trim();
                String usernameStr = editUserName.getText().toString().trim();
                if (TextUtils.isEmpty(moneyStr) || TextUtils.isEmpty(accountStr) || TextUtils.isEmpty(usernameStr)) {
                    RxToast.normal("请输入完整的提现信息");
                } else {
                    RequestParams requestParams = Utils.getRequestParams();
                    requestParams.put("username", accountStr);
                    requestParams.put("name", usernameStr);
                    requestParams.put("money", moneyStr);
                    requestParams.put("type", 1);
                    RequestCenter.UserWithdrawalsAlipay(requestParams, new DisposeDataListener() {
                        @Override
                        public void onSuccess(Object responseObj) {
                            RxToast.normal("申请提现成功，审核通过后到账");
                            mDialog.cancel();
                        }

                        @Override
                        public void onFailure(Object responseObj) {
                            OkHttpException error = (OkHttpException) responseObj;
                            RxToast.normal("操作失败 - (" + error.getEmsg() + ")");
                        }
                    });
                }
            }
        });
        mDialog.setContentView(dialogView);
        mDialog.show();
    }





}
