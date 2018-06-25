package com.gxysj.shareysj.delegate;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gxysj.shareysj.R;
import com.gxysj.shareysj.bean.GetCodeBean;
import com.gxysj.shareysj.bean.LoginUserBean;
import com.gxysj.shareysj.bean.event.EventEmpty;
import com.gxysj.shareysj.config.ShareYSJ;
import com.gxysj.shareysj.delegate.base.YSJDelegate;
import com.gxysj.shareysj.delegate.home.BottomDelegate;
import com.gxysj.shareysj.listener.ITimerListener;
import com.gxysj.shareysj.network.RequestCenter;
import com.gxysj.shareysj.utils.BaseTimerTask;
import com.gxysj.shareysj.utils.SPUtil;
import com.gxysj.shareysj.utils.user_manager.AccountManager;
import com.ljr.delegate_sdk.okhttp.exception.OkHttpException;
import com.ljr.delegate_sdk.okhttp.listener.DisposeDataListener;
import com.ljr.delegate_sdk.okhttp.request.RequestParams;
import com.vondear.rxtools.view.RxToast;

import org.greenrobot.eventbus.EventBus;

import java.text.MessageFormat;
import java.util.HashSet;
import java.util.Set;
import java.util.Timer;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * Created by 林佳荣 on 2018/3/27.
 * Github：https://github.com/ljrRookie
 * Function ：短信登录页面
 */

public class SignInOtherDelegate extends YSJDelegate implements ITimerListener {
    @BindView(R.id.btn_back)
    ImageView mBtnBack;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.iv_phone)
    ImageView mIvPhone;
    @BindView(R.id.edit_username)
    EditText mEditUsername;
    @BindView(R.id.rl_username)
    RelativeLayout mRlUsername;
    @BindView(R.id.edit_code)
    EditText mEditCode;
    @BindView(R.id.btn_code)
    Button mBtnCode;
    @BindView(R.id.rl_pwd)
    LinearLayout mRlPwd;
    @BindView(R.id.btn_other_login)
    TextView mBtnOtherLogin;
    @BindView(R.id.btn_login)
    Button mBtnLogin;
    private Timer mTimer = null;
    private int mCount = 60;
    private final TagAliasCallback mAliasCallback = new TagAliasCallback() {
        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs;
            switch (code) {
                case 0:
                    //这里可以往 SharePreference 里写一个成功设置的状态。成功设置一次后，以后不必再次设置了。
                    //UserUtils.saveTagAlias(getHoldingActivity(), true);
                    logs = "Set tag and alias success极光推送别名设置成功";
                    Log.e("TAG", logs);
                    break;
                case 6002:
                    //极低的可能设置失败 我设置过几百回 出现3次失败 不放心的话可以失败后继续调用上面那个方面 重连3次即可 记得return 不要进入死循环了...
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.极光推送别名设置失败，60秒后重试";
                    Log.e("TAG", logs);
                    break;
                default:
                    logs = "极光推送设置失败，Failed with errorCode = " + code;
                    Log.e("TAG", logs);
                    break;
            }
        }
    };
    @Override
    public Object setLayout() {
        return R.layout.delegate_sign_in_code;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        String phoneNum = (String) SPUtil.get(ShareYSJ.getApplicationContext(), "phone", "");
        if(!TextUtils.isEmpty(phoneNum)){
            mEditUsername.setText(phoneNum);
        }
    }

    private void initTime() {
        mTimer = new Timer();
        final BaseTimerTask task = new BaseTimerTask(this);
        mTimer.schedule(task, 0, 1000);
    }

    @OnClick({R.id.btn_back, R.id.btn_code, R.id.btn_other_login, R.id.btn_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                getSupportDelegate().pop();
                break;
            case R.id.btn_code:
                 String mPhoneNumCode = mEditUsername.getText().toString().trim();
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
                            RxToast.normal("发送验证码失败，请稍后重试");
                        }
                    }
                });
                break;
            case R.id.btn_other_login:
                getSupportDelegate().pop();
                break;
            case R.id.btn_login:
                String phoneNum = mEditUsername.getText().toString().trim();
                SPUtil.put(ShareYSJ.getApplicationContext(),"phone",phoneNum);
                String code = mEditCode.getText().toString().trim();
                RequestParams requestParams = new RequestParams();
                requestParams.put("username",phoneNum);
                requestParams.put("code",code);
                requestParams.put("type","1");
                RequestCenter.LoginUser(requestParams, new DisposeDataListener() {
                    @Override
                    public void onSuccess(Object responseObj) {
                        LoginUserBean loginUserBean = (LoginUserBean) responseObj;
                        String userid = loginUserBean.getUserid();
                        String token = loginUserBean.getToken();
                        SPUtil.put(ShareYSJ.getApplicationContext(),"userId",userid);
                        SPUtil.put(ShareYSJ.getApplicationContext(),"token",token);
                        Set<String> tags = new HashSet<String>();
                        tags.add(userid);
                        JPushInterface.setAliasAndTags(ShareYSJ.getApplicationContext(),userid,tags,mAliasCallback);
                        AccountManager.setSignState(true);
                        extraTransaction()
                                .setCustomAnimations(R.anim.v_fragment_enter, R.anim.v_fragment_pop_exit,
                                        R.anim.v_fragment_pop_enter, R.anim.v_fragment_exit)
                                .start(new BottomDelegate());
                        RxToast.normal("登录成功");
                    }

                    @Override
                    public void onFailure(Object responseObj) {
                        OkHttpException error = (OkHttpException) responseObj;
                        int errorCode = error.getCode();
                        if (errorCode == 10001) {
                            RxToast.normal("账号不能为空");
                        } else if (errorCode == 10002) {
                            RxToast.normal("密码不能为空");
                        } else if (errorCode == 10005) {
                            RxToast.normal("账号不存在");
                        } else if (errorCode == 10006) {
                            RxToast.normal("账号冻结中");
                        } else if (errorCode == 10009) {
                            RxToast.normal("登录失败");
                        }else if (errorCode == 10008) {
                            RxToast.normal("验证码不能为空");
                        } else if (errorCode == 10010) {
                            RxToast.normal("验证码错误");
                        } else if (errorCode == 10011) {
                            RxToast.normal("验证码超时");
                        } else if (errorCode == 10018) {
                            RxToast.normal("发送验证码失败，请稍后重试");
                        } else if (errorCode == 10019) {
                            RxToast.normal("发送验证码失败，请稍后重试");
                        }else {
                            RxToast.normal("登录失败");
                        }
                    }
                });



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
