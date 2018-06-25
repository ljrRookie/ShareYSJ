package com.gxysj.shareysj.delegate;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.allen.library.SuperTextView;
import com.gxysj.shareysj.R;
import com.gxysj.shareysj.bean.GetCodeBean;
import com.gxysj.shareysj.bean.LoginUserBean;
import com.gxysj.shareysj.bean.event.EventEmpty;
import com.gxysj.shareysj.config.ShareYSJ;
import com.gxysj.shareysj.delegate.base.YSJDelegate;
import com.gxysj.shareysj.delegate.home.BottomDelegate;
import com.gxysj.shareysj.listener.ITimerListener;
import com.gxysj.shareysj.network.HttpConstants;
import com.gxysj.shareysj.network.RequestCenter;
import com.gxysj.shareysj.utils.BaseTimerTask;
import com.gxysj.shareysj.utils.Labi_HttpPostUploadUtil;
import com.gxysj.shareysj.utils.SPUtil;
import com.gxysj.shareysj.utils.TimeUtils;
import com.gxysj.shareysj.utils.user_manager.AccountManager;
import com.gxysj.shareysj.wechat.WeChatConfig;
import com.gxysj.shareysj.wechat.callback.IWeChatSignInCallback;
import com.gxysj.shareysj.zxing.decode.Intents;
import com.ljr.delegate_sdk.okhttp.exception.OkHttpException;
import com.ljr.delegate_sdk.okhttp.listener.DisposeDataListener;
import com.ljr.delegate_sdk.okhttp.request.RequestParams;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.vondear.rxtools.RxConstTool;
import com.vondear.rxtools.RxLogTool;
import com.vondear.rxtools.RxTimeTool;
import com.vondear.rxtools.view.RxToast;
import com.vondear.rxtools.view.dialog.RxDialog;

import org.greenrobot.eventbus.EventBus;

import java.text.MessageFormat;
import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.WeakHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import qiu.niorgai.StatusBarCompat;

import static android.icu.lang.UScript.getCode;

/**
 * Created by Administrator on 2018/1/29.
 */

public class SignInDelegate extends YSJDelegate implements ITimerListener {


    private static final int REGIST_CODE = 1000;
    private static final int FIND_CODE = 1001;
    @BindView(R.id.btn_back)
    ImageView mBtnBack;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.edit_username)
    EditText mEditUsername;
    @BindView(R.id.edit_pwd)
    EditText mEditPwd;
    @BindView(R.id.btn_login)
    Button mBtnLogin;
    @BindView(R.id.btn_regist)
    TextView mBtnRegist;
    @BindView(R.id.btn_find_pwd)
    TextView mBtnFindPwd;
    Unbinder unbinder;
    @BindView(R.id.btn_wechat)
    ImageView mBtnWechat;
    @BindView(R.id.btn_other_login)
    TextView mBtnOtherLogin;
    private int mId;
    private String mFlag;
    private RxDialog mDialog;
    private Button mSendCode;
    private int mCount = 60;
    private Timer mTimer;
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
        return R.layout.delegate_sign_in;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        mDialog = new RxDialog(getActivity());
        mTvTitle.setText(getResources().getString(R.string.app_name));

        if (mId != 001) {
            RxToast.normal("请登录后操作");
        }
        String phoneNum = (String) SPUtil.get(ShareYSJ.getApplicationContext(), "phone", "");
        if (!TextUtils.isEmpty(phoneNum)) {
            mEditUsername.setText(phoneNum);
        }

    }

    public static SignInDelegate create(int Id, String flag) {
        SignInDelegate signInDelegate = new SignInDelegate();
        Bundle bundle = new Bundle();
        bundle.putInt("Id", Id);
        bundle.putString("Flag", flag);
        signInDelegate.setArguments(bundle);
        return signInDelegate;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arg = getArguments();
        if (arg != null) {
            mId = arg.getInt("Id", 001);
            mFlag = arg.getString("Flag", "");
        }
    }

    @OnClick({R.id.btn_back, R.id.btn_login, R.id.btn_regist, R.id.btn_find_pwd, R.id.btn_wechat, R.id.btn_other_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                extraTransaction()
                        .setCustomAnimations(R.anim.v_fragment_enter, R.anim.v_fragment_pop_exit,
                                R.anim.v_fragment_pop_enter, R.anim.v_fragment_exit)
                        .start(new BottomDelegate());
                break;
            case R.id.btn_login:
                String phoneNum = mEditUsername.getText().toString().trim();
                SPUtil.put(ShareYSJ.getApplicationContext(), "phone", phoneNum);
                String pwd = mEditPwd.getText().toString().trim();
                RequestParams requestParams = new RequestParams();
                requestParams.put("username", phoneNum);
                requestParams.put("password", pwd);
                requestParams.put("type", "0");
                RequestCenter.LoginUser(requestParams, new DisposeDataListener() {
                    @Override
                    public void onSuccess(Object responseObj) {
                        LoginUserBean loginUserBean = (LoginUserBean) responseObj;
                        String userid = loginUserBean.getUserid();
                        String token = loginUserBean.getToken();
                        SPUtil.put(ShareYSJ.getApplicationContext(), "userId", userid);
                        SPUtil.put(ShareYSJ.getApplicationContext(), "token", token);
                        Set<String> tags = new HashSet<String>();
                        tags.add(userid);
                        JPushInterface.setAliasAndTags(ShareYSJ.getApplicationContext(), userid, tags, mAliasCallback);
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

                            RxToast.normal("登录失败-("+error.getEmsg()+")");

                    }
                });

                break;
            case R.id.btn_regist:
                getSupportDelegate().startForResult(new SignUpDelegate(), REGIST_CODE);
                break;
            case R.id.btn_find_pwd:
                getSupportDelegate().startForResult(new FindPwdDelegate(), FIND_CODE);
                break;
            case R.id.btn_wechat:
                SignInByWechat();
                break;
            case R.id.btn_other_login:
                getSupportDelegate().start(new SignInOtherDelegate());
                break;
        }
    }
    @Override
    public boolean onBackPressedSupport() {
        getSupportDelegate().extraTransaction()
                .setCustomAnimations(R.anim.v_fragment_enter, R.anim.v_fragment_pop_exit,
                        R.anim.v_fragment_pop_enter, R.anim.v_fragment_exit)
                .start(new BottomDelegate());
        return true;
    }

    private void SignInByWechat() {
        RxToast.normal("微信登录中...,",3);
        WeChatConfig.getInstance().onSignSuccess(new IWeChatSignInCallback() {
            @Override
            public void onSignInSuccess(String userInfo) {
                if (TextUtils.isEmpty(userInfo)) {
                    RxToast.normal("微信登录失败");
                } else {
                    WeakHashMap<String, String> param = new WeakHashMap<>();
                    param.put("code", userInfo);
                    OkGo.post(HttpConstants.WeixinLogin)
                            .params("code", userInfo)
                            .execute(new StringCallback() {
                                @Override
                                public void onSuccess(String s, Call call, Response response) {
                                    RxLogTool.e("微信", "调用微信登录结果：" + s);
                                    JSONObject jsonObject = JSON.parseObject(s);
                                    int code = jsonObject.getIntValue("code");
                                    if (code == 0) {
                                        String userId = jsonObject.getString("userid");
                                        String token = jsonObject.getString("token");
                                        SPUtil.put(ShareYSJ.getApplicationContext(), "userId", userId);
                                        SPUtil.put(ShareYSJ.getApplicationContext(), "token", token);
                                        Set<String> tags = new HashSet<String>();
                                        tags.add(userId);
                                        JPushInterface.setAliasAndTags(ShareYSJ.getApplicationContext(), userId, tags, mAliasCallback);
                                        AccountManager.setSignState(true);
                                        getSupportDelegate().startWithPop(new BottomDelegate());

                                        RxToast.normal("登录成功");
                                    } else if (code == 10101) {
                                        String openid2 = JSON.parseObject(s).getString("openid2");
                                        RxToast.normal("未绑定微信");
                                        showBindPhone(openid2);
                                    } else {
                                        RxToast.normal("微信登录失败 - （code出错)");
                                    }
                                }

                                @Override
                                public void onError(Call call, Response response, Exception e) {
                                    super.onError(call, response, e);
                                    RxToast.normal("微信登录失败 - （code出错)");

                                }
                            });
                    //  new Labi_HttpPostUploadUtil(getContext(), mHandler, HttpConstants.WeixinLogin, param, null).start();
                }
            }
        }).signIn();
    }

    private void showBindPhone(final String openid2) {
        mDialog.getLayoutParams().gravity = Gravity.CENTER;
        View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_phone, null);
        final EditText phoneNum = (EditText) dialogView.findViewById(R.id.edit_phone_num);
        final TextView sp = (TextView) dialogView.findViewById(R.id.tv_sp);
        final TextView title = (TextView) dialogView.findViewById(R.id.tv_title);
        title.setText("微信绑定手机号码");
        sp.setVisibility(View.VISIBLE);
        final EditText code = (EditText) dialogView.findViewById(R.id.edit_code);
        mSendCode = (Button) dialogView.findViewById(R.id.btn_code);
        mSendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mPhoneNumCode = phoneNum.getText().toString().trim();
                getPhoneCode(mPhoneNumCode);
            }
        });
        SuperTextView commit = (SuperTextView) dialogView.findViewById(R.id.btn_commit);
        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mPhoneNumCode = phoneNum.getText().toString().trim();
                String codeStr = code.getText().toString().trim();
                if (TextUtils.isEmpty(codeStr)) {
                    RxToast.normal("验证码不能为空");
                    return;
                }
                if (TextUtils.isEmpty(mPhoneNumCode)) {
                    RxToast.normal("手机号码不能为空");
                    return;
                }
                RequestParams params = new RequestParams();
                params.put("phone", mPhoneNumCode);
                params.put("code", codeStr);
                params.put("openid", openid2);
                RequestCenter.BindingUserName(params, new DisposeDataListener() {
                    @Override
                    public void onSuccess(Object responseObj) {
                        LoginUserBean loginUserBean = (LoginUserBean) responseObj;
                        String userid = loginUserBean.getUserid();
                        String token = loginUserBean.getToken();
                        SPUtil.put(ShareYSJ.getApplicationContext(), "userId", userid);
                        SPUtil.put(ShareYSJ.getApplicationContext(), "token", token);
                        Set<String> tags = new HashSet<String>();
                        tags.add(userid);
                        JPushInterface.setAliasAndTags(ShareYSJ.getApplicationContext(), userid, tags, mAliasCallback);
                        AccountManager.setSignState(true);
                        extraTransaction()
                                .setCustomAnimations(R.anim.v_fragment_enter, R.anim.v_fragment_pop_exit,
                                        R.anim.v_fragment_pop_enter, R.anim.v_fragment_exit)
                                .start(new BottomDelegate());
                        mDialog.cancel();
                        RxToast.normal("登录成功");
                    }

                    @Override
                    public void onFailure(Object responseObj) {
                        OkHttpException error = (OkHttpException) responseObj;
                        mDialog.cancel();
                        RxToast.normal("登录失败 - （" + error.getEmsg() + ")");
                    }
                });

            }
        });
        mDialog.setContentView(dialogView);
        mDialog.show();
    }

    public void getPhoneCode(String mPhoneNumCode) {
        String type = "2";
        if (TextUtils.isEmpty(mPhoneNumCode)) {
            RxToast.normal("请输入手机号码");
            return;
        }
        Pattern compile = Pattern.compile(RxConstTool.REGEX_MOBILE_SIMPLE);
        Matcher matcher = compile.matcher(mPhoneNumCode);

        if (!matcher.matches()) {
            RxToast.normal("请输入正确的手机号码");
            return;
        }
        RequestCenter.GetCodeData(mPhoneNumCode, type, new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                GetCodeBean getCode = (GetCodeBean) responseObj;
                RxToast.normal("发送成功");
                mCount = 60;
                mSendCode.setText("60");
                initTime();
                mSendCode.setClickable(false);
            }

            @Override
            public void onFailure(Object responseObj) {
                OkHttpException error = (OkHttpException) responseObj;
                int errorCode = error.getCode();
                RxToast.normal("操作失败："+error.getEmsg());
            }
        });
    }

    private void initTime() {
        mTimer = new Timer();
        final BaseTimerTask task = new BaseTimerTask(this);
        mTimer.schedule(task, 0, 1000);
    }

    @Override
    public void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);
        if (requestCode == REGIST_CODE && resultCode == RESULT_OK) {
            // 在此通过Bundle data 获取返回的数据
            String phone = data.getString("phone");
            String pwd = data.getString("pwd");
            mEditPwd.setText(pwd);
            mEditUsername.setText(phone);
        }
        if (requestCode == FIND_CODE && resultCode == RESULT_OK) {
            // 在此通过Bundle data 获取返回的数据
            String phone = data.getString("phone");
            String pwd = data.getString("pwd");
            mEditPwd.setText(pwd);
            mEditUsername.setText(phone);
        }
    }


    @Override
    public void onTimer() {
        getProxyActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mSendCode != null) {
                    mSendCode.setText(MessageFormat.format("{0} s", mCount));
                    mCount--;
                    if (mCount < 0) {
                        if (mTimer != null) {
                            mTimer.cancel();
                            mTimer = null;
                        }
                        mSendCode.setText("发送验证码");
                        mSendCode.setClickable(true);
                    }
                }
            }
        });
    }
}
