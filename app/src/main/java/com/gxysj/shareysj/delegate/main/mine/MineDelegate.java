package com.gxysj.shareysj.delegate.main.mine;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.allen.library.SuperButton;
import com.allen.library.SuperTextView;
import com.bumptech.glide.Glide;
import com.gxysj.shareysj.R;
import com.gxysj.shareysj.bean.UserAlipayRechargeBalanceBean;
import com.gxysj.shareysj.bean.UserInfoBean;
import com.gxysj.shareysj.bean.event.EventEmpty;
import com.gxysj.shareysj.bean.event.InitData;
import com.gxysj.shareysj.config.ShareYSJ;
import com.gxysj.shareysj.config_enum.ConfigKeys;
import com.gxysj.shareysj.delegate.SignInDelegate;
import com.gxysj.shareysj.delegate.home.BottomItemDelegate;
import com.gxysj.shareysj.listener.IAliPayResultListener;
import com.gxysj.shareysj.network.HttpConstants;
import com.gxysj.shareysj.network.RequestCenter;
import com.gxysj.shareysj.utils.SPUtil;
import com.gxysj.shareysj.utils.TimeUtils;
import com.gxysj.shareysj.utils.pay.PayAsyncTask;
import com.gxysj.shareysj.utils.user_manager.AccountManager;
import com.gxysj.shareysj.utils.user_manager.Utils;
import com.gxysj.shareysj.wechat.WeChatConfig;
import com.ljr.delegate_sdk.okhttp.exception.OkHttpException;
import com.ljr.delegate_sdk.okhttp.listener.DisposeDataListener;
import com.ljr.delegate_sdk.okhttp.request.RequestParams;
import com.louisgeek.dropdownviewlib.DropDownView;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.vondear.rxtools.RxTimeTool;
import com.vondear.rxtools.module.wechat.share.WechatShareModel;
import com.vondear.rxtools.view.RxToast;
import com.vondear.rxtools.view.dialog.RxDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashSet;
import java.util.Set;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.jpush.android.api.JPushInterface;
import de.hdodenhof.circleimageview.CircleImageView;
import q.rorbin.badgeview.Badge;
import q.rorbin.badgeview.QBadgeView;

/**
 * Created by Administrator on 2018/1/29.
 */

public class MineDelegate extends BottomItemDelegate implements IAliPayResultListener {
    @BindView(R.id.iv_pic)
    CircleImageView mIvPic;
    @BindView(R.id.tv_name)
    TextView mTvName;
    @BindView(R.id.tv_vip)
    TextView mTvVip;
    @BindView(R.id.tv_time)
    TextView mTvTime;
    @BindView(R.id.tv_flag)
    TextView mTvFlag;
    @BindView(R.id.tv_money)
    TextView mTvMoney;
    @BindView(R.id.rl_mine)
    RelativeLayout mRlMine;
    @BindView(R.id.btn_rechange)
    Button mBtnRechange;
    @BindView(R.id.btn_take)
    Button mBtnTake;
    @BindView(R.id.btn_order)
    SuperTextView mBtnOrder;
    @BindView(R.id.btn_number)
    SuperTextView mBtnNumber;
    /*   @BindView(R.id.btn_bag)
       SuperTextView mBtnBag;*/
    @BindView(R.id.btn_ad)
    SuperTextView mBtnAd;
    @BindView(R.id.btn_suggest)
    SuperTextView mBtnSuggest;
    @BindView(R.id.btn_setting)
    SuperTextView mBtnSetting;
    @BindView(R.id.scroll)
    NestedScrollView mScroll;
    @BindView(R.id.refresh)
    SwipeRefreshLayout mRefresh;
    @BindView(R.id.btn_detail)
    SuperTextView mBtnDetail;
    /* @BindView(R.id.btn_present)
     SuperTextView mBtnPresent;*/

    Unbinder unbinder;
    @BindView(R.id.btn_msg)
    ImageView mBtnMsg;
    @BindView(R.id.btn_exit)
    SuperButton mBtnExit;


    private RxDialog mDialog;
    private int MineCode = 10000;
    private AlertDialog mAlertDialog;
    private boolean isSign = false;
    private UserInfoBean mUserInfoBean = null;
    private int mIds_state = 3;
    private Bitmap mBitmap;
    private WechatShareModel mWechatShareModel;
    private IAliPayResultListener mIAlPayResultListener = null;
    private Activity mActivity = null;
    /**
     * 充值金额
     */
    private String mMoneyStr;
    /**
     * 用户余额
     */
    private String mUserMoney;
    private String mUserId;

    private boolean isRechange = false;
    private int mCount = 0;
    private Badge mBadge;

    @Override
    public Object setLayout() {
        return R.layout.delegate_mine;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        EventBus.getDefault().register(this);
        mBadge = new QBadgeView(getActivity()).bindTarget(mBtnMsg).setBadgeBackgroundColor(getResources().getColor(R.color.red)).setGravityOffset(5, 5, true)
                .setBadgePadding(2, true)
                .setBadgeTextSize(9, true)
                .setBadgeGravity(Gravity.END | Gravity.TOP);
        initRefreshLayout();
        mDialog = new RxDialog(getActivity());

        mAlertDialog = new AlertDialog.Builder(getActivity()).create();
        mActivity = getProxyActivity();
        mIAlPayResultListener = (IAliPayResultListener) MineDelegate.this;
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        mUserId = (String) SPUtil.get(ShareYSJ.getApplicationContext(), "userId", "");
        initData();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isRechange) {
            initData();
            isRechange = false;
        }
    }

    private void initData() {
        RequestParams requestParams = Utils.getRequestParams();
        RequestCenter.GetUserInfo(requestParams, new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                mUserInfoBean = (UserInfoBean) responseObj;
                mCount = mUserInfoBean.getCount();
                mBadge.setBadgeNumber(mCount);
                if (mUserInfoBean.getVip() == 0) {
                    //未开通会员
                    mTvVip.setClickable(true);
                    mTvVip.setTextColor(getResources().getColor(R.color.colorAccent));
                    mTvVip.setBackgroundResource(R.drawable.edit_white_bg);
                    mTvVip.setText("加入会员");
                } else {
                    //已开通会员
                    mTvVip.setClickable(false);
                    mTvVip.setTextColor(getResources().getColor(R.color.white));
                    mTvVip.setBackgroundResource(R.drawable.red_bg);
                    mTvVip.setText("中沙会员");

                }
                Glide.with(getActivity()).load(HttpConstants.ROOT_URL + mUserInfoBean.getImage()).into(mIvPic);
                mTvName.setText(mUserInfoBean.getName());
                mTvTime.setText("加入时间：" + mUserInfoBean.getTime());
                mUserMoney = mUserInfoBean.getMoney();
                mTvMoney.setText(mUserMoney);
                mIds_state = mUserInfoBean.getIds_state();
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


    private void initRefreshLayout() {
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


    @OnClick({R.id.rl_mine, R.id.btn_rechange, R.id.btn_take, R.id.btn_order, R.id.btn_number,
            R.id.btn_ad, R.id.btn_suggest, R.id.btn_setting, R.id.btn_detail, R.id.btn_msg, R.id.btn_exit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_mine:
                if (mUserInfoBean != null) {
                    getParentDelegate().getSupportDelegate().startForResult(PersonalDelegate.create(mUserInfoBean), MineCode);
                } else {
                    RxToast.normal("正在获取用户数据，请稍后重试");
                }
                break;
            case R.id.btn_rechange:
                showRechange();
                break;
            case R.id.btn_take:
                showTake();
                break;
            case R.id.btn_msg:
                mBadge.setBadgeNumber(0);
                getParentDelegate().getSupportDelegate().start(new MsgDelegate());
                break;
            case R.id.btn_order:
                getParentDelegate().getSupportDelegate().start(new OrderDelegate());
                break;
            case R.id.btn_detail:
                getParentDelegate().getSupportDelegate().start(new TransactionDetailDelegate());
                break;
            case R.id.btn_number:
                getParentDelegate().getSupportDelegate().start(new WaterTimeDelegate());
                break;
            case R.id.btn_ad:
                getParentDelegate().getSupportDelegate().start(new MyADDelegate());
                break;
            case R.id.btn_suggest:
                getParentDelegate().getSupportDelegate().start(new SuggestDelegate());
                break;
            case R.id.btn_setting:
                getParentDelegate().getSupportDelegate().start(SafetySettingDelegate.create(mIds_state));
                break;
            case R.id.btn_exit:
                showExitDialog();
                break;

        }
    }

    /**
     * 退出登录
     */
    private void showExitDialog() {
        mDialog.getLayoutParams().gravity = Gravity.CENTER;
        View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_commit, null);
        TextView title = (TextView) dialogView.findViewById(R.id.tv_title);
        title.setText("是否确认退出登录？");
        SuperTextView btnCancel = (SuperTextView) dialogView.findViewById(R.id.btn_cancel);
        btnCancel.setCenterString("取消");
        btnCancel.setOnSuperTextViewClickListener(new SuperTextView.OnSuperTextViewClickListener() {
            @Override
            public void onClickListener(SuperTextView superTextView) {
                mDialog.cancel();
            }
        });
        SuperTextView btnCommit = (SuperTextView) dialogView.findViewById(R.id.btn_commit);
        btnCommit.setCenterString("退出");
        btnCommit.setOnSuperTextViewClickListener(new SuperTextView.OnSuperTextViewClickListener() {
            @Override
            public void onClickListener(SuperTextView superTextView) {
                AccountManager.setSignState(false);
                Set<String> tags = new HashSet<String>();
                tags.add(mUserId);
                JPushInterface.deleteTags(ShareYSJ.getApplicationContext(), Integer.parseInt(mUserId), tags);
                SPUtil.clear(ShareYSJ.getApplicationContext(), "userId");
                SPUtil.clear(ShareYSJ.getApplicationContext(), "token");
                mDialog.cancel();
                getParentDelegate().getSupportDelegate().extraTransaction()
                        .setCustomAnimations(R.anim.v_fragment_enter, R.anim.v_fragment_pop_exit,
                                R.anim.v_fragment_pop_enter, R.anim.v_fragment_exit)
                        .startWithPop(new SignInDelegate());

            }
        });
        mDialog.setContentView(dialogView);
        mDialog.show();
    }

    /**
     * 提现
     */
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
                    long time = RxTimeTool.getCurTimeMills();
                    String timeStr = String.valueOf(time);
                    String secretKey = TimeUtils.getSecretKey(timeStr);
                    RequestParams requestParams = Utils.getRequestParams();
                    requestParams.put("username", accountStr);
                    requestParams.put("name", usernameStr);
                    requestParams.put("money", moneyStr);
                    requestParams.put("type", 0);
                    RequestCenter.UserWithdrawalsAlipay(requestParams, new DisposeDataListener() {
                        @Override
                        public void onSuccess(Object responseObj) {
                            double douUserMoney = Double.parseDouble(mUserMoney);
                            double rechargeMoney = Double.parseDouble(moneyStr);
                            mTvMoney.setText(String.valueOf(douUserMoney - rechargeMoney));
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

    /**
     * 充值
     */
    private void showRechange() {
        mDialog.getLayoutParams().gravity = Gravity.CENTER;
        View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_recharge, null);
        final DropDownView dropDownView = (DropDownView) dialogView.findViewById(R.id.ddv);
        final EditText editMoney = (EditText) dialogView.findViewById(R.id.edit_money);
        dialogView.findViewById(R.id.btn_commit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMoneyStr = editMoney.getText().toString().trim();
                String selectName = dropDownView.getSelectName();
                if (TextUtils.isEmpty(mMoneyStr)) {
                    RxToast.normal("请输入充值金额");
                } else {
                    RequestParams requestParams = Utils.getRequestParams();
                    requestParams.put("money", mMoneyStr);
                    if (selectName.equals("支付宝")) {
                        requestParams.put("type", 0);
                        RequestCenter.UserAlipayRechargeBalance(requestParams, new DisposeDataListener() {
                            @Override
                            public void onSuccess(Object responseObj) {
                                UserAlipayRechargeBalanceBean userAlipayRechargeBalanceBean = (UserAlipayRechargeBalanceBean) responseObj;
                                String data = userAlipayRechargeBalanceBean.getData();
                                if (TextUtils.isEmpty(data)) {
                                    RxToast.normal("充值失败");
                                } else {
                                    PayAsyncTask payAsyncTask = new PayAsyncTask(mActivity, mIAlPayResultListener);
                                    payAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, data);
                                }
                            }

                            @Override
                            public void onFailure(Object responseObj) {
                                OkHttpException error = (OkHttpException) responseObj;
                                RxToast.normal("充值失败 - (" + error.getEmsg() + ")");
                            }
                        });
                        mDialog.cancel();
                    } else if (selectName.equals("微信")) {
                        final IWXAPI wxapi = WeChatConfig.getInstance().getWXAPI();
                        final String appid = ShareYSJ.getConfiguration(ConfigKeys.WE_CHAT_APP_ID);
                        requestParams.put("type", 1);
                        RequestCenter.UserAlipayRechargeBalance(requestParams, new DisposeDataListener() {
                            @Override
                            public void onSuccess(Object responseObj) {
                                UserAlipayRechargeBalanceBean userAlipayRechargeBalanceBean = (UserAlipayRechargeBalanceBean) responseObj;
                                PayReq payReq = new PayReq();
                                payReq.appId = appid;
                                payReq.prepayId = userAlipayRechargeBalanceBean.getPrepayid();
                                payReq.partnerId = userAlipayRechargeBalanceBean.getPartnerid();
                                payReq.nonceStr = userAlipayRechargeBalanceBean.getNoncestr();
                                payReq.sign = userAlipayRechargeBalanceBean.getSign();
                                payReq.packageValue = "Sign=WXPay";
                                payReq.timeStamp = userAlipayRechargeBalanceBean.getTimestamp();
                                SPUtil.put(ShareYSJ.getApplicationContext(), "isReChargeBalance", true);
                                wxapi.sendReq(payReq);
                                isRechange = true;
                            }

                            @Override
                            public void onFailure(Object responseObj) {
                                OkHttpException error = (OkHttpException) responseObj;
                                RxToast.normal("充值失败 - (" + error.getEmsg() + ")");
                            }
                        });
                        mDialog.cancel();
                    } else {
                        RxToast.normal("请选择充值方式");
                    }
                }
            }
        });
        mDialog.setContentView(dialogView);
        mDialog.show();
    }

    // 再点一次退出程序时间设置
    private static final long WAIT_TIME = 2000L;
    private long TOUCH_TIME = 0;

    @Override
    public boolean onBackPressedSupport() {
        if (System.currentTimeMillis() - TOUCH_TIME < WAIT_TIME) {
            _mActivity.finish();
        } else {
            TOUCH_TIME = System.currentTimeMillis();
            RxToast.normal("双击退出" + ShareYSJ.getApplicationContext().getString(R.string.app_name));
        }
        return true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void readInfo(EventEmpty messageEvent) {
        Uri image = messageEvent.getImage();
        String str = messageEvent.getStr();
        if (image != null) {
            Glide.with(getActivity()).load(image).into(mIvPic);
        }
        if (!TextUtils.isEmpty(str)) {
            mTvName.setText(str);
            mUserInfoBean.setName(str);
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void initData(InitData initType) {
        initData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onPaySuccess() {
        RxToast.normal("充值成功!");
        double douUserMoney = Double.parseDouble(mUserMoney);
        double rechargeMoney = Double.parseDouble(mMoneyStr);
        mTvMoney.setText(String.valueOf(douUserMoney + rechargeMoney));
    }

    @Override
    public void onPaying() {

    }

    @Override
    public void onPayFail() {
        RxToast.normal("充值失败!");
    }

    @Override
    public void onPayCancel() {
        RxToast.normal("取消充值!");
    }

    @Override
    public void onPayConnectError() {
        RxToast.normal("充值失败!连接错误");
    }

    @OnClick(R.id.tv_vip)
    public void joinVipClicked() {
        getParentDelegate().getSupportDelegate().start(JoinVipDelegate.create(mUserInfoBean.getVip_money()));
    }
}
