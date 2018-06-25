package com.gxysj.shareysj.delegate;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.allen.library.SuperButton;
import com.gxysj.shareysj.R;
import com.gxysj.shareysj.bean.EventBusPayType;
import com.gxysj.shareysj.bean.UserAlipayRechargeBalanceBean;
import com.gxysj.shareysj.bean.UserInfoBean;
import com.gxysj.shareysj.config.ShareYSJ;
import com.gxysj.shareysj.config_enum.ConfigKeys;
import com.gxysj.shareysj.delegate.base.YSJDelegate;
import com.gxysj.shareysj.delegate.main.home.ad.AdForPayDelegate;
import com.gxysj.shareysj.delegate.main.mine.safety.PayPwdDelegate;
import com.gxysj.shareysj.listener.IAliPayResultListener;
import com.gxysj.shareysj.network.RequestCenter;
import com.gxysj.shareysj.utils.SPUtil;
import com.gxysj.shareysj.utils.pay.PayAsyncTask;
import com.gxysj.shareysj.utils.user_manager.Utils;
import com.gxysj.shareysj.wechat.WeChatConfig;
import com.joanzapata.iconify.widget.IconTextView;
import com.ljr.delegate_sdk.okhttp.exception.OkHttpException;
import com.ljr.delegate_sdk.okhttp.listener.DisposeDataListener;
import com.ljr.delegate_sdk.okhttp.request.RequestParams;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.vondear.rxtools.view.RxToast;
import com.vondear.rxtools.view.dialog.RxDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;
import lsp.com.lib.PasswordInputEdt;
import me.yokeyword.fragmentation.ISupportFragment;

/**
 * Created by Administrator on 2018/1/10.
 */

public class PayDelegate extends YSJDelegate implements IAliPayResultListener {


    @BindView(R.id.tv_order_price)
    TextView mTvOrderPrice;
    @BindView(R.id.balance_pay)
    ImageView mBalancePay;
    @BindView(R.id.pay_a)
    TextView mPayA;
    @BindView(R.id.tv_balance_flag)
    TextView mTvBalanceFlag;
    @BindView(R.id.tv_balance)
    TextView mTvBalance;
    @BindView(R.id.rb_balance_pay)
    RadioButton mRbBalancePay;
    @BindView(R.id.rl_balance)
    RelativeLayout mRlBalance;
    @BindView(R.id.ali_pay)
    ImageView mAliPay;
    @BindView(R.id.pay_b)
    TextView mPayB;
    @BindView(R.id.rb_ali_pay)
    RadioButton mRbAliPay;
    @BindView(R.id.rl_ali)
    RelativeLayout mRlAli;
    @BindView(R.id.wechat_pay)
    ImageView mWechatPay;
    @BindView(R.id.pay_c)
    TextView mPayC;
    @BindView(R.id.rb_wechat_pay)
    RadioButton mRbWechatPay;
    @BindView(R.id.rl_wechat)
    RelativeLayout mRlWechat;
    @BindView(R.id.btn_pay)
    SuperButton mBtnPay;
    @BindView(R.id.btn_back)
    ImageView mBtnBack;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.btn_right)
    ImageView mBtnRight;
    private Activity mActivity = null;
    private RxDialog mDialog;
    private IAliPayResultListener mIAliPayResultListener = null;
    private String mOrderNum;
    private double mMoney;
    private int mPayType;
    private int PayShopping = 1001;
    private int PayAd = 1000;
    private int PayWater1 = 10021;
    private int PayRed = 1003;
    private int PayInvest = 1004;
    public static final int PayJoinVip = 1005;
    /**
     * mPayType = 1000 : 广告付款
     * mPayType = 1001 : 点点物
     * mPayType = 10021 : 饮水充值
     * mPayType = 1003 : 红包次数
     * mPayType = 1004 : 投资
     */

    @Override
    public Object setLayout() {
        return R.layout.delegate_pay;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        mTvTitle.setText("支付中心");
        EventBus.getDefault().register(this);
        mDialog = new RxDialog(getActivity());
        mActivity = getProxyActivity();
        mIAliPayResultListener = (IAliPayResultListener) PayDelegate.this;
        mTvOrderPrice.setText("¥" + mMoney);
        getUserInfo();
    }

    private void getUserInfo() {
        RequestParams requestParams = Utils.getRequestParams();
        RequestCenter.GetUserInfo(requestParams, new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                UserInfoBean mUserInfoBean = (UserInfoBean) responseObj;
                mTvBalance.setText(mUserInfoBean.getMoney());
                SPUtil.put(ShareYSJ.getApplicationContext(), "money", mUserInfoBean.getMoney());
            }

            @Override
            public void onFailure(Object responseObj) {
                OkHttpException error = (OkHttpException) responseObj;
                RxToast.normal("获取用户余额失败(" + error.getEmsg() + ")");
                mTvBalance.setText("获取失败");
            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        mOrderNum = arguments.getString("orderNum", "");
        mMoney = arguments.getDouble("money", 0.00);
        mPayType = arguments.getInt("payType", 0);

    }

    public static PayDelegate create(double money, String orderidNum, int payType) {
        PayDelegate payDelegate = new PayDelegate();
        Bundle bundle = new Bundle();
        bundle.putString("orderNum", orderidNum);
        bundle.putDouble("money", money);
        bundle.putInt("payType", payType);
        payDelegate.setArguments(bundle);
        return payDelegate;
    }

    @OnClick({R.id.rl_balance, R.id.rl_ali, R.id.rl_wechat, R.id.btn_pay})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_balance:
                mRbBalancePay.setChecked(true);
                mRbAliPay.setChecked(false);
                mRbWechatPay.setChecked(false);
                break;
            case R.id.rl_ali:
                mRbBalancePay.setChecked(false);
                mRbAliPay.setChecked(true);
                mRbWechatPay.setChecked(false);
                break;
            case R.id.rl_wechat:
                mRbBalancePay.setChecked(false);
                mRbAliPay.setChecked(false);
                mRbWechatPay.setChecked(true);
                break;
            case R.id.btn_pay:
                if (mRbAliPay.isChecked()) {
                    aliPay();
                } else if (mRbBalancePay.isChecked()) {
                    showDialog();
                } else if (mRbWechatPay.isChecked()) {
                    weChatPay();
                }
                break;
            default:
                break;
        }
    }

    /**
     * 微信支付
     */
    private void weChatPay() {
        RequestParams requestParams = Utils.getRequestParams();
        requestParams.put("type", 1);
        final IWXAPI wxapi = WeChatConfig.getInstance().getWXAPI();
        final String appid = ShareYSJ.getConfiguration(ConfigKeys.WE_CHAT_APP_ID);

        if (mPayType == PayShopping) {
            //购买商品
            requestParams.put("orderid", mOrderNum);
            RequestCenter.UserAlipayCommodity(requestParams, new DisposeDataListener() {
                @Override
                public void onSuccess(Object responseObj) {
                    PayByWeChat((UserAlipayRechargeBalanceBean) responseObj, appid, wxapi);
                }

                @Override
                public void onFailure(Object responseObj) {
                    OkHttpException error = (OkHttpException) responseObj;
                    RxToast.normal("支付失败 - (" + error.getEmsg() + ")");
                }
            });
        } else if (mPayType == PayAd) {
            //购买广告
            requestParams.put("orderid", mOrderNum);
            RequestCenter.UserAlipayAdvertisement(requestParams, new DisposeDataListener() {
                @Override
                public void onSuccess(Object responseObj) {
                    PayByWeChat((UserAlipayRechargeBalanceBean) responseObj, appid, wxapi);
                }

                @Override
                public void onFailure(Object responseObj) {
                    OkHttpException error = (OkHttpException) responseObj;
                    RxToast.normal("支付失败 - (" + error.getEmsg() + ")");
                }
            });
        } else if (mPayType == PayWater1) {
            //购买饮水次数
            requestParams.put("id", mOrderNum);
            RequestCenter.UserAlipayWater(requestParams, new DisposeDataListener() {
                @Override
                public void onSuccess(Object responseObj) {
                    PayByWeChat((UserAlipayRechargeBalanceBean) responseObj, appid, wxapi);
                }

                @Override
                public void onFailure(Object responseObj) {
                    OkHttpException error = (OkHttpException) responseObj;
                    RxToast.normal("支付失败 - (" + error.getEmsg() + ")");
                }
            });
        } else if (mPayType == PayRed) {
            //购买红包次数
            requestParams.put("id", mOrderNum);
            RequestCenter.UserAlipayRedEnvelopesNub(requestParams, new DisposeDataListener() {
                @Override
                public void onSuccess(Object responseObj) {
                    PayByWeChat((UserAlipayRechargeBalanceBean) responseObj, appid, wxapi);
                }

                @Override
                public void onFailure(Object responseObj) {
                    OkHttpException error = (OkHttpException) responseObj;
                    RxToast.normal("支付失败 - (" + error.getEmsg() + ")");
                }
            });
        } else if (mPayType == PayInvest) {
            //投资机箱
            requestParams.put("machineid", mOrderNum);
            requestParams.put("money", mMoney);
            RequestCenter.UserAlipayInvestmentMachine(requestParams, new DisposeDataListener() {
                @Override
                public void onSuccess(Object responseObj) {
                    PayByWeChat((UserAlipayRechargeBalanceBean) responseObj, appid, wxapi);
                }

                @Override
                public void onFailure(Object responseObj) {
                    OkHttpException error = (OkHttpException) responseObj;
                    RxToast.normal("支付失败 - (" + error.getEmsg() + ")");
                }
            });
        }else if(mPayType == PayJoinVip){
            RequestCenter.UserAlipayVip(requestParams, new DisposeDataListener() {
                @Override
                public void onSuccess(Object responseObj) {
                    PayByWeChat((UserAlipayRechargeBalanceBean) responseObj, appid, wxapi);
                }

                @Override
                public void onFailure(Object responseObj) {
                    OkHttpException error = (OkHttpException) responseObj;
                    RxToast.normal("支付失败 - (" + error.getEmsg() + ")");
                }
            });
        }

    }

    private void PayByWeChat(UserAlipayRechargeBalanceBean responseObj, String appid, IWXAPI wxapi) {
        UserAlipayRechargeBalanceBean userAlipayRechargeBalanceBean = responseObj;
        PayReq payReq = new PayReq();
        payReq.appId = appid;
        payReq.prepayId = userAlipayRechargeBalanceBean.getPrepayid();
        payReq.partnerId = userAlipayRechargeBalanceBean.getPartnerid();
        payReq.nonceStr = userAlipayRechargeBalanceBean.getNoncestr();
        payReq.sign = userAlipayRechargeBalanceBean.getSign();
        payReq.packageValue = "Sign=WXPay";
        payReq.timeStamp = userAlipayRechargeBalanceBean.getTimestamp();
        wxapi.sendReq(payReq);
    }

    /**
     * 支付宝支付
     */
    private void aliPay() {
        RequestParams requestParams = Utils.getRequestParams();
        requestParams.put("type", 0);
        if (mPayType == PayShopping) {
            requestParams.put("orderid", mOrderNum);
            RequestCenter.UserAlipayCommodity(requestParams, new DisposeDataListener() {
                @Override
                public void onSuccess(Object responseObj) {
                    PayByAliPay((UserAlipayRechargeBalanceBean) responseObj);
                }

                @Override
                public void onFailure(Object responseObj) {
                    OkHttpException error = (OkHttpException) responseObj;
                    RxToast.normal("支付失败 - (" + error.getEmsg() + ")");
                }
            });
        } else if (mPayType == PayAd) {
            requestParams.put("orderid", mOrderNum);
            RequestCenter.UserAlipayAdvertisement(requestParams, new DisposeDataListener() {
                @Override
                public void onSuccess(Object responseObj) {
                    PayByAliPay((UserAlipayRechargeBalanceBean) responseObj);
                }

                @Override
                public void onFailure(Object responseObj) {
                    OkHttpException error = (OkHttpException) responseObj;
                    RxToast.normal("支付失败 - (" + error.getEmsg() + ")");
                }
            });
        } else if (mPayType == PayWater1) {
            requestParams.put("id", mOrderNum);
            RequestCenter.UserAlipayWater(requestParams, new DisposeDataListener() {
                @Override
                public void onSuccess(Object responseObj) {
                    PayByAliPay((UserAlipayRechargeBalanceBean) responseObj);
                }

                @Override
                public void onFailure(Object responseObj) {
                    OkHttpException error = (OkHttpException) responseObj;
                    RxToast.normal("支付失败 - (" + error.getEmsg() + ")");
                }
            });
        } else if (mPayType == PayRed) {
            requestParams.put("id", mOrderNum);
            RequestCenter.UserAlipayRedEnvelopesNub(requestParams, new DisposeDataListener() {
                @Override
                public void onSuccess(Object responseObj) {
                    PayByAliPay((UserAlipayRechargeBalanceBean) responseObj);
                }

                @Override
                public void onFailure(Object responseObj) {
                    OkHttpException error = (OkHttpException) responseObj;
                    RxToast.normal("支付失败 - (" + error.getEmsg() + ")");
                }
            });
        } else if (mPayType == PayInvest) {
            requestParams.put("machineid", mOrderNum);
            requestParams.put("money", mMoney);
            RequestCenter.UserAlipayInvestmentMachine(requestParams, new DisposeDataListener() {
                @Override
                public void onSuccess(Object responseObj) {
                    PayByAliPay((UserAlipayRechargeBalanceBean) responseObj);
                }

                @Override
                public void onFailure(Object responseObj) {
                    OkHttpException error = (OkHttpException) responseObj;
                    RxToast.normal("支付失败 - (" + error.getEmsg() + ")");
                }
            });
        }else if(mPayType == PayJoinVip){
            RequestCenter.UserAlipayVip(requestParams, new DisposeDataListener() {
                @Override
                public void onSuccess(Object responseObj) {
                    PayByAliPay((UserAlipayRechargeBalanceBean) responseObj);
                }

                @Override
                public void onFailure(Object responseObj) {
                    OkHttpException error = (OkHttpException) responseObj;
                    RxToast.normal("支付失败 - (" + error.getEmsg() + ")");
                }
            });
        }


    }

    private void PayByAliPay(UserAlipayRechargeBalanceBean responseObj) {
        UserAlipayRechargeBalanceBean userAlipayRechargeBalanceBean = responseObj;
        String data = userAlipayRechargeBalanceBean.getData();
        if (TextUtils.isEmpty(data)) {
            RxToast.normal("支付失败");
        } else {
            PayAsyncTask payAsyncTask = new PayAsyncTask(mActivity, mIAliPayResultListener);
            payAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, data);
        }
    }

    /**
     * 余额支付
     */
    private void showDialog() {
        mDialog.getLayoutParams().gravity = Gravity.CENTER;
        View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_password, null);
        TextView dialog_price = (TextView) dialogView.findViewById(R.id.tv_price);
        dialog_price.setText("¥" + mMoney);
        IconTextView btnCancel = (IconTextView) dialogView.findViewById(R.id.btn_cancel);
        TextView btnSetPayPwd = (TextView) dialogView.findViewById(R.id.btn_setting_pay_pwd);
        btnSetPayPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportDelegate().start(new PayPwdDelegate());
                mDialog.cancel();
            }
        });
        PasswordInputEdt edtPassword = (PasswordInputEdt) dialogView.findViewById(R.id.edt_password);
        edtPassword.setFocus(true);
        showSoftInput(edtPassword);
        edtPassword.setOnInputOverListener(new PasswordInputEdt.onInputOverListener() {
            @Override
            public void onInputOver(String s) {
                RequestParams requestParams = Utils.getRequestParams();
                requestParams.put("password", s);
                if (mPayType == PayShopping) {
                    /**
                     * 点点物余额付款
                     */
                    requestParams.put("orderid", mOrderNum);
                    RequestCenter.BalancePurchaseCommodity(requestParams, new DisposeDataListener() {
                        @Override
                        public void onSuccess(Object responseObj) {
                            getSupportDelegate().pop();
                            getSupportDelegate().start(PayForSuccessDelegate.create(mMoney, mPayType, mOrderNum));
                        }

                        @Override
                        public void onFailure(Object responseObj) {
                            OkHttpException error = (OkHttpException) responseObj;
                            RxToast.normal("操作失败 - (" + error.getEmsg() + ")");
                            if (error.getCode() == 10063) {
                                getSupportDelegate().start(new PayPwdDelegate());
                            }

                        }
                    });
                } else if (mPayType == PayAd) {
                    /**
                     * 广告余额付款
                     */
                    requestParams.put("orderid", mOrderNum);
                    RequestCenter.BalancePaymentAdvertisement(requestParams, new DisposeDataListener() {
                        @Override
                        public void onSuccess(Object responseObj) {
                            getSupportDelegate().pop();
                            getSupportDelegate().start(PayForSuccessDelegate.create(mMoney, mPayType));
                            // EventBus.getDefault().post(new EventEmpty("",null));
                        }

                        @Override
                        public void onFailure(Object responseObj) {
                            OkHttpException error = (OkHttpException) responseObj;
                            RxToast.normal("操作失败 - (" + error.getEmsg() + ")");
                            int errorCode = error.getCode();
                            if (errorCode == 10063) {
                                getSupportDelegate().start(new PayPwdDelegate());
                            }

                        }
                    });
                } else if (mPayType == PayWater1) {
                    /**
                     * 充值饮水次数余额付款
                     */
                    requestParams.put("id", mOrderNum);
                    RequestCenter.UserBalancePaymentWater(requestParams, new DisposeDataListener() {
                        @Override
                        public void onSuccess(Object responseObj) {
                            getSupportDelegate().pop();
                            getSupportDelegate().start(PayForSuccessDelegate.create(mMoney, mPayType));
                        }

                        @Override
                        public void onFailure(Object responseObj) {
                            OkHttpException error = (OkHttpException) responseObj;
                            RxToast.normal("操作失败 - (" + error.getEmsg() + ")");
                            if (error.getCode() == 10063) {
                                getSupportDelegate().start(new PayPwdDelegate());
                            }

                        }
                    });
                } else if (mPayType == PayRed) {
                    /**
                     * 购买红包余额付款
                     */
                    requestParams.put("id", mOrderNum);
                    RequestCenter.BalanceRedEnvelopes(requestParams, new DisposeDataListener() {
                        @Override
                        public void onSuccess(Object responseObj) {
                            getSupportDelegate().pop();
                            getSupportDelegate().start(PayForSuccessDelegate.create(mMoney, mPayType));
                        }

                        @Override
                        public void onFailure(Object responseObj) {
                            OkHttpException error = (OkHttpException) responseObj;
                            RxToast.normal("操作失败 - (" + error.getEmsg() + ")");
                            if (error.getCode() == 10063) {
                                getSupportDelegate().start(new PayPwdDelegate());
                            }

                        }
                    });
                } else if (mPayType == PayInvest) {
                    /**
                     * 投资机箱余额付款
                     */
                    requestParams.put("machineid", mOrderNum);
                    requestParams.put("money", mMoney);
                    RequestCenter.UserInvestmentMachine(requestParams, new DisposeDataListener() {
                        @Override
                        public void onSuccess(Object responseObj) {
                            getSupportDelegate().pop();
                            getSupportDelegate().start(PayForSuccessDelegate.create(mMoney, mPayType));
                        }

                        @Override
                        public void onFailure(Object responseObj) {
                            OkHttpException error = (OkHttpException) responseObj;
                            RxToast.normal("操作失败 - (" + error.getEmsg() + ")");
                            if (error.getCode() == 10063) {
                                getSupportDelegate().start(new PayPwdDelegate());
                            }

                        }
                    });
                }else if(mPayType == PayJoinVip){
                    RequestCenter.BalancePaymentVip(requestParams, new DisposeDataListener() {
                        @Override
                        public void onSuccess(Object responseObj) {
                            getSupportDelegate().popQuiet();
                            getSupportDelegate().start(PayForSuccessDelegate.create(mMoney, mPayType));
                        }

                        @Override
                        public void onFailure(Object responseObj) {
                            OkHttpException error = (OkHttpException) responseObj;
                            RxToast.normal("操作失败 - (" + error.getEmsg() + ")");
                            if (error.getCode() == 10063) {
                                getSupportDelegate().start(new PayPwdDelegate());
                            }

                        }
                    });
                }

                mDialog.cancel();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.cancel();
            }
        });
        mDialog.setContentView(dialogView);
        mDialog.show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 微信支付成功回调
     *
     * @param payType
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void payResult(EventBusPayType payType) {
        getSupportDelegate().pop();
        if (mPayType == PayShopping) {
            getSupportDelegate().start(PayForSuccessDelegate.create(mMoney, mPayType, mOrderNum));
        } else {
            getSupportDelegate().start(PayForSuccessDelegate.create(mMoney, mPayType));
        }


    }

    @OnClick(R.id.btn_back)
    public void onViewClicked() {
        getSupportDelegate().pop();
    }

    @Override
    public void onPaySuccess() {
        getSupportDelegate().pop();
        if (mPayType == PayShopping) {
            getSupportDelegate().start(PayForSuccessDelegate.create(mMoney, mPayType, mOrderNum));
        } else {
            getSupportDelegate().start(PayForSuccessDelegate.create(mMoney, mPayType));
        }
    }

    @Override
    public void onPaying() {

    }

    @Override
    public void onPayFail() {
        RxToast.normal("支付失败!");
    }

    @Override
    public void onPayCancel() {
        RxToast.normal("取消支付!");
    }

    @Override
    public void onPayConnectError() {
        RxToast.normal("支付失败!连接错误");
    }


}
