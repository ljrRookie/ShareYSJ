package com.gxysj.shareysj.delegate;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.allen.library.SuperButton;
import com.gxysj.shareysj.R;
import com.gxysj.shareysj.bean.EventBusPayType;
import com.gxysj.shareysj.bean.event.EventEmpty;
import com.gxysj.shareysj.bean.event.InitData;
import com.gxysj.shareysj.config.ShareYSJ;
import com.gxysj.shareysj.delegate.base.YSJDelegate;
import com.gxysj.shareysj.delegate.main.mine.JoinVipDelegate_ViewBinding;
import com.gxysj.shareysj.delegate.main.mine.MyADDelegate;
import com.gxysj.shareysj.delegate.main.mine.ad.adapter.MyADAdapter;
import com.gxysj.shareysj.network.RequestCenter;
import com.gxysj.shareysj.utils.user_manager.Utils;
import com.ljr.delegate_sdk.okhttp.exception.OkHttpException;
import com.ljr.delegate_sdk.okhttp.listener.DisposeDataListener;
import com.ljr.delegate_sdk.okhttp.request.RequestParams;
import com.vondear.rxtools.view.RxToast;
import com.zyao89.view.zloading.ZLoadingDialog;
import com.zyao89.view.zloading.Z_TYPE;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import me.yokeyword.fragmentation.ISupportFragment;

/**
 * Created by 林佳荣 on 2018/2/28.
 * Github：https://github.com/ljrRookie
 * Function ：支付成功
 */

public class PayForSuccessDelegate extends YSJDelegate {
    @BindView(R.id.iv_pic)
    ImageView mIvPic;
    @BindView(R.id.tv_flag)
    TextView mTvFlag;
    @BindView(R.id.tv_price)
    TextView mTvPrice;
    @BindView(R.id.tv_tip)
    TextView mTvTip;
    @BindView(R.id.btn_ok)
    SuperButton mBtnOk;
    Unbinder unbinder;
    private String mMoney;
    private String mUuid = "";
    private int mPayType;
    private int PayShopping = 1001;
    private int PayAd = 1000;
    private int PayWater1 = 10021;
    private int PayRed = 1003;
    private int PayInvest = 1004;
    private ZLoadingDialog mCheckDialog;

    /**
     * mPayType = 1000 : 广告付款
     * mPayType = 1001 : 点点物
     * mPayType = 10021 : 饮水充值
     * mPayType = 1003 : 红包次数
     * mPayType = 1004 : 投资
     */
    @Override
    public Object setLayout() {
        return R.layout.delegate_pay_success;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        mTvPrice.setText("¥" + mMoney);
        checkShopResult();
        if(mPayType == PayDelegate.PayJoinVip){
           //充值会员
            EventBus.getDefault().post(new InitData(PayDelegate.PayJoinVip));
        }
    }

    private void checkShopResult() {
        if(!mUuid.isEmpty()){
            mCheckDialog = new ZLoadingDialog(ShareYSJ.getActivity());
            mCheckDialog.setLoadingBuilder(Z_TYPE.ROTATE_CIRCLE)//设置类型
                    .setLoadingColor(Color.GRAY)//颜色
                    .setHintText("出货中....")
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
                        Thread.sleep(10000);//休眠3秒
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            checkResult(mUuid);


                        }
                    });

                }
            }.start();
        }
    }

    private void checkResult(String uuid) {
        final RequestParams requestParams = Utils.getRequestParams();
        requestParams.put("uuid",uuid);
        RequestCenter.VerificationCommodity(requestParams, new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                RxToast.normal("出货成功");
                mCheckDialog.cancel();
            }

            @Override
            public void onFailure(Object responseObj) {
                OkHttpException error = (OkHttpException) responseObj;
                RxToast.normal("出货情况："+error.getEmsg());
                mCheckDialog.cancel();
            }
        });

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        mMoney = arguments.getString("money", "");
        mPayType = arguments.getInt("pay_type");
        mUuid = arguments.getString("uuid","");
    }

    @OnClick(R.id.btn_ok)
    public void onViewClicked() {
        if(mPayType == PayAd){
            getSupportDelegate().pop();
            getSupportDelegate().start(new MyADDelegate());
        }else{
            getSupportDelegate().pop();
        }

    }

    public static PayForSuccessDelegate create(double money, int payType) {
        PayForSuccessDelegate payForSuccessDelegate = new PayForSuccessDelegate();
        Bundle bundle = new Bundle();
        bundle.putString("money", String.valueOf(money));
        bundle.putInt("pay_type",payType);
        payForSuccessDelegate.setArguments(bundle);
        return payForSuccessDelegate;
    }
    public static PayForSuccessDelegate create(double money, int payType,String uuid) {
        PayForSuccessDelegate payForSuccessDelegate = new PayForSuccessDelegate();
        Bundle bundle = new Bundle();
        bundle.putString("money", String.valueOf(money));
        bundle.putInt("pay_type",payType);
        bundle.putString("uuid",uuid);
        payForSuccessDelegate.setArguments(bundle);
        return payForSuccessDelegate;
    }
}
