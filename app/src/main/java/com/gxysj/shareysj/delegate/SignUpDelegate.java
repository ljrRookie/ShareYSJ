package com.gxysj.shareysj.delegate;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.gxysj.shareysj.R;
import com.gxysj.shareysj.bean.GetCodeBean;
import com.gxysj.shareysj.config.ShareYSJ;
import com.gxysj.shareysj.delegate.base.YSJDelegate;
import com.gxysj.shareysj.listener.ITimerListener;
import com.gxysj.shareysj.network.RequestCenter;

import com.gxysj.shareysj.utils.BaseTimerTask;
import com.gxysj.shareysj.utils.SPUtil;
import com.ljr.delegate_sdk.okhttp.exception.OkHttpException;
import com.ljr.delegate_sdk.okhttp.listener.DisposeDataListener;
import com.ljr.delegate_sdk.okhttp.request.RequestParams;
import com.vondear.rxtools.RxLogTool;
import com.vondear.rxtools.view.RxToast;

import java.text.MessageFormat;
import java.util.Timer;

import butterknife.BindView;

import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2018/1/30.
 * 注册页面
 */

public class SignUpDelegate extends YSJDelegate implements ITimerListener {
    @BindView(R.id.btn_login)
    TextView mBtnLogin;
    @BindView(R.id.edit_phone)
    EditText mEditPhone;
    @BindView(R.id.edit_code)
    EditText mEditCode;
    @BindView(R.id.btn_code)
    Button mBtnCode;
    @BindView(R.id.edit_pwd)
    EditText mEditPwd;
    @BindView(R.id.edit_pwd_again)
    EditText mEditPwdAgain;
    @BindView(R.id.edit_invite_code)
    EditText mEditInviteCode;
    @BindView(R.id.btn_regist)
    Button mBtnRegist;
    Unbinder unbinder;
    private Timer mTimer = null;
    private int mCount = 60;
    private String mPhoneNumCode;


    @Override
    public Object setLayout() {
        return R.layout.delegate_sign_up;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {

    }

    private void initTime() {
        mTimer = new Timer();
        final BaseTimerTask task = new BaseTimerTask(this);
        mTimer.schedule(task, 0, 1000);
    }


    @OnClick({R.id.btn_login, R.id.btn_code, R.id.btn_regist})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                getSupportDelegate().pop();
                break;
            case R.id.btn_code:
                mPhoneNumCode = mEditPhone.getText().toString().trim();
                String type = "0";
                if (TextUtils.isEmpty(mPhoneNumCode)) {
                    RxToast.normal("请输入手机号码");
                    return;
                }

                RequestCenter.GetCodeData(mPhoneNumCode, type, new DisposeDataListener() {
                    @Override
                    public void onSuccess(Object responseObj) {
                        GetCodeBean getCode = (GetCodeBean) responseObj;
                        RxToast.normal("发送成功");
                        mCount = 60;
                        mBtnCode.setText("60");
                        initTime();
                        mBtnCode.setClickable(false);
                    }

                    @Override
                    public void onFailure(Object responseObj) {
                        OkHttpException error = (OkHttpException) responseObj;
                        int errorCode = error.getCode();
                        RxToast.normal("操作失败："+error.getEmsg());
                    }
                });
                break;
            case R.id.btn_regist:
                String code = mEditCode.getText().toString().trim();
                final String pwd = mEditPwd.getText().toString().trim();
                String pwdAgain = mEditPwdAgain.getText().toString().trim();
                String inviteCode = "";
                inviteCode = mEditInviteCode.getText().toString().trim();
                final String phoneNum = mEditPhone.getText().toString().trim();
                if (TextUtils.isEmpty(phoneNum)) {
                    RxToast.normal("请输入手机号码");
                    return;
                }
                if (!mPhoneNumCode.equals(phoneNum)) {
                    RxToast.normal("获取验证码的手机号码与注册手机号码不一致");
                    return;
                }
                if (TextUtils.isEmpty(code)) {
                    RxToast.normal("请输入验证码");
                    return;
                }
                if (TextUtils.isEmpty(pwd)) {
                    RxToast.normal("请输入密码");
                    return;
                }
                if (TextUtils.isEmpty(pwdAgain)) {
                    RxToast.normal("请再次输入密码");
                    return;
                }
                if (!pwd.equals(pwdAgain)) {
                    RxToast.normal("输入的密码不一致");
                    return;
                }
                final RequestParams requestParams = new RequestParams();
                requestParams.put("username", phoneNum);
                requestParams.put("password", pwd);
                requestParams.put("code", code);
                requestParams.put("invitationcode", inviteCode);
                RequestCenter.RegUserData(requestParams, new DisposeDataListener() {
                    @Override
                    public void onSuccess(Object responseObj) {
                        RxToast.normal("注册成功");
                        Bundle bundle = new Bundle();
                        bundle.putString("phone", phoneNum);
                        bundle.putString("pwd", pwd);
                        setFragmentResult(RESULT_OK, bundle);
                        getSupportDelegate().pop();
                    }

                    @Override
                    public void onFailure(Object responseObj) {
                        OkHttpException error = (OkHttpException) responseObj;
                        int errorCode = error.getCode();
                        RxLogTool.e("注册用户", "失败码：" + errorCode);
                        if (errorCode == 10010) {
                            RxToast.normal("验证码错误");
                        } else if (errorCode == 10011) {
                            RxToast.normal("验证码超时");
                        } else if (errorCode == 10012) {
                            RxToast.normal("邀请码不存在");
                        } else if (errorCode == 10013) {
                            RxToast.normal("密码设置太简单");
                        } else if (errorCode == 10014) {
                            RxToast.normal("密码少于6位");
                        } else if (errorCode == 10015) {
                            RxToast.normal("账号已存在");
                        } else if (errorCode == 10016) {
                            RxToast.normal("注册失败");
                        } else if (errorCode == 10017) {
                            RxToast.normal("注册账号失败");
                        } else if (errorCode == 10018) {
                            RxToast.normal("手机号码频率限制");
                        } else {
                            RxToast.normal("注册失败");
                        }

                    }
                });

                break;
        }
    }

    @Override
    public void onTimer() {
        getProxyActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mBtnCode != null) {
                    mBtnCode.setText(MessageFormat.format("{0} s", mCount));
                    mCount--;
                    if (mCount < 0) {
                        if (mTimer != null) {
                            mTimer.cancel();
                            mTimer = null;
                        }
                        mBtnCode.setText("发送验证码");
                        mBtnCode.setClickable(true);
                    }
                }
            }
        });
    }
}
