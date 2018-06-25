package com.gxysj.shareysj.delegate.main.mine.safety;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.gxysj.shareysj.R;
import com.gxysj.shareysj.config.ShareYSJ;
import com.gxysj.shareysj.delegate.base.YSJDelegate;
import com.gxysj.shareysj.network.RequestCenter;
import com.gxysj.shareysj.utils.SPUtil;
import com.gxysj.shareysj.utils.TimeUtils;
import com.ljr.delegate_sdk.okhttp.exception.OkHttpException;
import com.ljr.delegate_sdk.okhttp.listener.DisposeDataListener;
import com.ljr.delegate_sdk.okhttp.request.RequestParams;
import com.vondear.rxtools.RxTimeTool;
import com.vondear.rxtools.view.RxToast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by 林佳荣 on 2018/3/26.
 * Github：https://github.com/ljrRookie
 * Function ：
 */

public class PayPwdDelegate extends YSJDelegate {
    @BindView(R.id.btn_back)
    ImageView mBtnBack;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.btn_right)
    ImageView mBtnRight;
    @BindView(R.id.edit_old_pwd)
    EditText mEditOldPwd;
    @BindView(R.id.edit_pwd)
    EditText mEditPwd;
    @BindView(R.id.edit_pwd_again)
    EditText mEditPwdAgain;
    @BindView(R.id.btn_regist)
    Button mBtnRegist;
    Unbinder unbinder;

    @Override
    public Object setLayout() {
        return R.layout.delegate_mine_setting_safety_pay_pwd;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        mTvTitle.setText("支付密码");

    }


    @OnClick({R.id.btn_back, R.id.btn_regist})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                getSupportDelegate().pop();
                break;
            case R.id.btn_regist:
                String oldPwd = mEditOldPwd.getText().toString().trim();
                String pwd = mEditPwd.getText().toString().trim();
                String pwdAgain = mEditPwdAgain.getText().toString().trim();
                if(TextUtils.isEmpty(pwd) || TextUtils.isEmpty(pwdAgain)){
                    RxToast.normal("新的支付密码不能为空");
                    return;
                }
                if(pwd.length()!=6){
                    RxToast.normal("请输入六位数的密码");
                    return;
                }
                if(!pwd.equals(pwdAgain)){
                    RxToast.normal("输入的新密码不一致");
                    return;
                }
                long time = RxTimeTool.getCurTimeMills();
                String timeStr = String.valueOf(time);
                String secretKey = TimeUtils.getSecretKey(timeStr);
                RequestParams params = new RequestParams();
                params.put("userid", SPUtil.get(ShareYSJ.getApplicationContext(), "userId", ""));
                params.put("token", SPUtil.get(ShareYSJ.getApplicationContext(), "token", ""));
                params.put("secret_key", secretKey);
                params.put("time", timeStr);
                params.put("paymentpssword",oldPwd);
                params.put("newpaymentpssword",pwdAgain);
                RequestCenter.UpdateUserPayment(params, new DisposeDataListener() {
                    @Override
                    public void onSuccess(Object responseObj) {
                        RxToast.normal("修改成功！");
                        getSupportDelegate().pop();
                    }

                    @Override
                    public void onFailure(Object responseObj) {
                        OkHttpException errorInfo = (OkHttpException) responseObj;
                        Object emsg = errorInfo.getEmsg();
                        RxToast.normal("修改失败-（" + emsg + ")");
                    }
                });
                break;
        }
    }
}
