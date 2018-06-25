package com.gxysj.shareysj.delegate.main.mine.safety;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
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
import butterknife.OnClick;
import butterknife.Unbinder;


/**
 * Created by 林佳荣 on 2018/3/12.
 * Github：https://github.com/ljrRookie
 * Function ：
 */

public class LoginPwdDelegate extends YSJDelegate {


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
    @BindView(R.id.btn_commit)
    Button mBtnCommit;
    Unbinder unbinder;

    @Override
    public Object setLayout() {
        return R.layout.delegate_change_pwd;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        mTvTitle.setText("修改密码");
        mBtnCommit.setText("提交");
    }




    @OnClick({R.id.btn_back, R.id.btn_commit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                getSupportDelegate().pop();
                break;
            case R.id.btn_commit:
                String oldPwd = mEditOldPwd.getText().toString().trim();
                String pwd = mEditPwd.getText().toString().trim();
                String pwdAgain = mEditPwdAgain.getText().toString().trim();
                if(TextUtils.isEmpty(oldPwd) || TextUtils.isEmpty(pwd) || TextUtils.isEmpty(pwdAgain)){
                    RxToast.normal("输入的密码不能为空");
                    return;
                }
                if(!pwd.equals(pwdAgain)){
                    RxToast.normal("输入的密码不一致");
                    return;
                }
                long time = RxTimeTool.getCurTimeMills();
                String timeStr = String.valueOf(time);
                String secretKey = TimeUtils.getSecretKey(timeStr);

                final RequestParams requestParams = new RequestParams();
                requestParams.put("userid", SPUtil.get(ShareYSJ.getApplicationContext(), "userId", ""));
                requestParams.put("token", SPUtil.get(ShareYSJ.getApplicationContext(), "token", ""));
                requestParams.put("secret_key", secretKey);
                requestParams.put("time", timeStr);
                requestParams.put("pssword", oldPwd);
                requestParams.put("newpssword", pwd);

                RequestCenter.UserUpdatePassword(requestParams, new DisposeDataListener() {
                    @Override
                    public void onSuccess(Object responseObj) {
                        RxToast.normal("修改成功");
                        getSupportDelegate().pop();
                    }

                    @Override
                    public void onFailure(Object responseObj) {
                        OkHttpException error = (OkHttpException) responseObj;
                        int errorCode = error.getCode();
                        RxToast.normal("修改失败-("+error.getEmsg()+")");


                    }
                });

                break;
        }
    }
}
