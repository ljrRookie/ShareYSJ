package com.gxysj.shareysj.delegate.main.generalize;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.allen.library.SuperButton;
import com.allen.library.SuperTextView;
import com.bumptech.glide.Glide;
import com.gxysj.shareysj.R;
import com.gxysj.shareysj.bean.FranchiserInfoBean;
import com.gxysj.shareysj.config.ShareYSJ;
import com.gxysj.shareysj.delegate.base.YSJDelegate;
import com.gxysj.shareysj.network.HttpConstants;
import com.gxysj.shareysj.network.RequestCenter;
import com.gxysj.shareysj.utils.SPUtil;
import com.gxysj.shareysj.utils.TimeUtils;
import com.gxysj.shareysj.utils.user_manager.Utils;
import com.ljr.delegate_sdk.okhttp.exception.OkHttpException;
import com.ljr.delegate_sdk.okhttp.listener.DisposeDataListener;
import com.ljr.delegate_sdk.okhttp.request.RequestParams;
import com.vondear.rxtools.RxTimeTool;
import com.vondear.rxtools.view.RxToast;
import com.vondear.rxtools.view.dialog.RxDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by 林佳荣 on 2018/3/21.
 * Github：https://github.com/ljrRookie
 * Function ：
 */

public class AllianceDelegate extends YSJDelegate {
    @BindView(R.id.btn_back)
    ImageView mBtnBack;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.iv_pic)
    CircleImageView mIvPic;
    @BindView(R.id.tv_name)
    TextView mTvName;
    @BindView(R.id.btn_rechange)
    TextView mBtnRechange;
    @BindView(R.id.tv_invest)
    TextView mTvInvest;
    @BindView(R.id.tv_flag)
    TextView mTvFlag;
    @BindView(R.id.tv_money_month)
    TextView mTvMoneyMonth;
    @BindView(R.id.iv_q)
    ImageView mIvQ;
    @BindView(R.id.money)
    TextView mMoney;
    @BindView(R.id.tv_money)
    TextView mTvMoney;
    @BindView(R.id.btn_money_total)
    RelativeLayout mBtnMoneyTotal;
    @BindView(R.id.iv_w)
    ImageView mIvW;
    @BindView(R.id.detail)
    TextView mDetail;
    @BindView(R.id.btn_money_wait)
    RelativeLayout mBtnMoneyWait;
    @BindView(R.id.iv_e)
    ImageView mIvE;
    @BindView(R.id.user)
    TextView mUser;
    @BindView(R.id.btn_money_already)
    RelativeLayout mBtnMoneyAlready;
    @BindView(R.id.btn_withdraw)
    SuperTextView mBtnWithdraw;
    @BindView(R.id.bg_money)
    RelativeLayout mBgMoney;

    @BindView(R.id.tv_machine_num)
    TextView mTvMachineNum;
    @BindView(R.id.tv_invest_bottom)
    TextView mTvInvestBottom;
    Unbinder unbinder;
    private RxDialog mDialog;


    @Override
    public Object setLayout() {
        return R.layout.delegate_generalize_alliance;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        mTvTitle.setText("加盟商");
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
        RequestCenter.GetFranchiserInfo(requestParams, new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                FranchiserInfoBean franchiserInfoBean = (FranchiserInfoBean) responseObj;
                mTvMoneyMonth.setText(franchiserInfoBean.getMoney());
                mTvInvest.setText("投资金额：" + franchiserInfoBean.getInvestment_money());
                mTvInvestBottom.setText(franchiserInfoBean.getInvestment_money()+"元");
                Glide.with(getActivity()).load(HttpConstants.ROOT_URL + franchiserInfoBean.getImage()).into(mIvPic);
                mTvMachineNum.setText(franchiserInfoBean.getMachine() + "台");
                mTvName.setText(franchiserInfoBean.getName());
                mTvMoney.setText(franchiserInfoBean.getCumulative_money() + "元");
            }

            @Override
            public void onFailure(Object responseObj) {
                OkHttpException error = (OkHttpException) responseObj;
                int errorCode = error.getCode();
                if (errorCode == 10097) {
                    getParentDelegate().getSupportDelegate().start(new RegisterAllianceDelegate());
                } else if (errorCode == 10098) {
                    RxToast.normal("加盟商审核中...");
                } else if (errorCode == 10099) {
                    RxToast.normal("加盟商审核不通过...");
                } else if (errorCode == 10093) {
                    RxToast.normal("未通过实名认证,请通过后再申请加盟商..,", 5);
                } else {
                    RxToast.normal("操作失败 - （" + error.getEmsg() + ")");
                }
            }
        });
    }


    @OnClick({R.id.btn_back, R.id.btn_rechange, R.id.btn_money_total, R.id.btn_money_wait, R.id.btn_money_already, R.id.btn_withdraw})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                getSupportDelegate().pop();
                break;
            case R.id.btn_rechange:
                getSupportDelegate().start(new InvestMachineDelegate());
                break;
            case R.id.btn_money_total:
                getSupportDelegate().start(new AllianceProfitDelegate());
                break;
            case R.id.btn_money_wait:
                getSupportDelegate().start(new AllianceInvestDelegate());
                break;
            case R.id.btn_money_already:
                getSupportDelegate().start(new AllianceMachineDelegate());
                break;
            case R.id.btn_withdraw:
                showTake();
                break;
        }
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
                    requestParams.put("type", 2);
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
