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
import android.widget.ImageView;
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
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2018/2/5.
 */

public class FindPwdDelegate extends YSJDelegate implements ITimerListener {
    @BindView(R.id.btn_back)
    ImageView mBtnBack;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.btn_right)
    ImageView mBtnRight;
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
    @BindView(R.id.btn_regist)
    Button mBtnRegist;
    private Timer mTimer = null;
    private int mCount = 60;

    @Override
    public Object setLayout() {
        return R.layout.delegate_find_pwd;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        mTvTitle.setText("找回密码");
        String phoneNum = (String) SPUtil.get(ShareYSJ.getApplicationContext(), "phone", "");
        if(!TextUtils.isEmpty(phoneNum)){
            mEditPhone.setText(phoneNum);
        }
    }

    private void initTime() {
        mTimer = new Timer();
        final BaseTimerTask task = new BaseTimerTask(this);
        mTimer.schedule(task, 0, 1000);
    }

    @OnClick({R.id.btn_back, R.id.btn_code, R.id.btn_regist})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                getSupportDelegate().pop();
                break;
            case R.id.btn_code:
                String mPhoneNumCode = mEditPhone.getText().toString().trim();
                String type = "1";
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
                        if (errorCode == 10018) {
                            RxToast.normal("发送验证码次数过多，请稍后重试");
                        } else {
                            RxToast.normal("获取验证码失败，请稍后重试");
                        }
                    }
                });
                break;
            case R.id.btn_regist:
                String code = mEditCode.getText().toString().trim();
                final String pwd = mEditPwd.getText().toString().trim();
                String pwdAgain = mEditPwdAgain.getText().toString().trim();
                final String phoneNum = mEditPhone.getText().toString().trim();
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
                requestParams.put("newpassword", pwd);
                requestParams.put("code", code);
                RequestCenter.UserBackPassword(requestParams, new DisposeDataListener() {
                    @Override
                    public void onSuccess(Object responseObj) {
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
                        } else if (errorCode == 10013) {
                            RxToast.normal("密码设置太简单");
                        } else if (errorCode == 10014) {
                            RxToast.normal("密码少于6位");
                        }else if (errorCode == 10020) {
                            RxToast.normal("新密码不能为空");
                        } else {
                            RxToast.normal("找回密码失败");
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
