package com.gxysj.shareysj.delegate.main.mine;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.allen.library.SuperButton;
import com.allen.library.SuperTextView;
import com.bumptech.glide.Glide;
import com.gxysj.shareysj.R;
import com.gxysj.shareysj.bean.GetCodeBean;
import com.gxysj.shareysj.bean.UserInfoBean;
import com.gxysj.shareysj.bean.event.EventEmpty;
import com.gxysj.shareysj.config.ShareYSJ;
import com.gxysj.shareysj.delegate.SignInDelegate;
import com.gxysj.shareysj.delegate.base.YSJDelegate;
import com.gxysj.shareysj.listener.ITimerListener;
import com.gxysj.shareysj.network.HttpConstants;
import com.gxysj.shareysj.network.RequestCenter;
import com.gxysj.shareysj.utils.BaseTimerTask;
import com.gxysj.shareysj.utils.Labi_HttpPostUploadUtil;
import com.gxysj.shareysj.utils.SPUtil;
import com.gxysj.shareysj.utils.TimeUtils;
import com.gxysj.shareysj.utils.callback.CallbackManager;
import com.gxysj.shareysj.utils.callback.CallbackType;
import com.gxysj.shareysj.utils.callback.IGlobalCallback;
import com.gxysj.shareysj.utils.camear.CameraUtils;
import com.gxysj.shareysj.utils.user_manager.AccountManager;
import com.gxysj.shareysj.utils.user_manager.Utils;
import com.ljr.delegate_sdk.okhttp.exception.OkHttpException;
import com.ljr.delegate_sdk.okhttp.listener.DisposeDataListener;
import com.ljr.delegate_sdk.okhttp.request.RequestParams;
import com.orhanobut.logger.Logger;
import com.vondear.rxtools.RxConstTool;
import com.vondear.rxtools.RxConstants;
import com.vondear.rxtools.RxDataTool;
import com.vondear.rxtools.RxLogTool;
import com.vondear.rxtools.RxTimeTool;
import com.vondear.rxtools.RxTool;
import com.vondear.rxtools.view.RxToast;
import com.vondear.rxtools.view.dialog.RxDialog;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Timer;
import java.util.WeakHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;
import me.yokeyword.fragmentation.ISupportFragment;

/**
 * Created by Administrator on 2018/2/1.
 */

public class PersonalDelegate extends YSJDelegate implements ITimerListener {
    @BindView(R.id.iv_pic)
    CircleImageView mIvPic;
    @BindView(R.id.btn_pic)
    RelativeLayout mBtnPic;
    @BindView(R.id.edit_name)
    EditText mEditName;
    @BindView(R.id.stv_phone)
    SuperTextView mStvPhone;
    @BindView(R.id.edit_email)
    EditText mEditEmail;
    @BindView(R.id.cb_male)
    RadioButton mCbMale;
    @BindView(R.id.cb_female)
    RadioButton mCbFemale;
    @BindView(R.id.rg_sex)
    RadioGroup mRgSex;
    @BindView(R.id.edit_remark)
    EditText mEditRemark;
    @BindView(R.id.btn_commit)
    SuperButton mBtnCommit;
    Unbinder unbinder;
    @BindView(R.id.btn_back)
    ImageView mBtnBack;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.btn_right)
    ImageView mBtnRight;
    private RxDialog mDialog;
    private boolean isExChange = false;
    private String mImageUrl;
    private UserInfoBean mUserInfoBean = null;
    private WeakHashMap<String, String> mTextWeakHashMap;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String json = data.getString("data");
            RxLogTool.e("修改个人信息", json);
            if (json.equals("10000")) {
                RxToast.normal("操作失败");
            } else {
                int code = JSON.parseObject(json).getIntValue("code");
                if (code == 0) {
                    if (isFinish) {
                        RxToast.normal("修改成功");
                        EventBus.getDefault().post(new EventEmpty(mName, image));
                        getSupportDelegate().pop();
                    } else {
                        EventBus.getDefault().post(new EventEmpty(mName, image));
                        RxToast.normal("修改成功");

                    }
                } else if (code == 10000) {
                    RxToast.normal("登录异常，请重新登录");
                    AccountManager.setSignState(false);
                    getSupportDelegate().pop();
                    getSupportDelegate().start(new SignInDelegate());
                } else {
                    RxToast.normal("修改失败" + code);
                }
            }

        }
    };
    private String mUserId;
    private String mToken;
    private Uri image = null;
    private Timer mTimer = null;
    private int mCount = 60;
    private Button mSendCode;
    private int mGender = 0;
    private boolean isFinish = false;
    private String mName = "";
    private UserInfoBean mUserInfoBeanBean;

    @Override
    public Object setLayout() {
        return R.layout.delegate_mine_personal;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        mTvTitle.setText("个人信息");
        mDialog = new RxDialog(getActivity());
        mUserId = (String) SPUtil.get(ShareYSJ.getApplicationContext(), "userId", "");
        mToken = (String) SPUtil.get(ShareYSJ.getApplicationContext(), "token", "");
        initData();
        mTextWeakHashMap = new WeakHashMap<>();
    }

    private void initData() {
        RequestParams requestParams = Utils.getRequestParams();
        RequestCenter.GetUserInfo(requestParams, new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                mUserInfoBeanBean = (UserInfoBean) responseObj;
                Glide.with(getActivity()).load(HttpConstants.ROOT_URL + mUserInfoBean.getImage()).into(mIvPic);
                mName = mUserInfoBean.getName();
                mEditName.setText(mName);
                mStvPhone.setRightString(mUserInfoBean.getPhone());
                String email = mUserInfoBean.getEmail();
                if (!TextUtils.isEmpty(email)) {
                    mEditEmail.setText(email);
                }
                String introduce = mUserInfoBean.getIntroduce();
                if (!TextUtils.isEmpty(introduce)) {
                    mEditRemark.setText(introduce);
                }
                mGender = mUserInfoBean.getGender();
                if (mGender == 0) {
                    mCbMale.setChecked(true);
                } else {
                    mCbFemale.setChecked(true);
                }
            }

            @Override
            public void onFailure(Object responseObj) {
                OkHttpException error = (OkHttpException) responseObj;

                int errorCode = error.getCode();
                if (errorCode == 10000) {
                    RxToast.normal("另一设备登录此账号，请重新登录");
                    AccountManager.setSignState(false);
                    getSupportDelegate().start(new SignInDelegate());
                } else {
                    RxToast.normal("获取用户数据失败" + error.getCode());
                }
            }
        });


    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        mUserInfoBean = (UserInfoBean) arguments.getSerializable("UserInfo");

    }

    @OnClick({R.id.btn_pic, R.id.btn_commit, R.id.btn_back, R.id.stv_phone})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                getSupportDelegate().pop();
                break;
            case R.id.btn_pic:
                startCameraWithCheck();
                CallbackManager.getIntance().addCallback(CallbackType.ON_CROP, new IGlobalCallback<Uri>() {
                    @Override
                    public void excuteCallback(@Nullable Uri args) {
                        image = args;
                        Glide.with(getActivity()).load(args).into(mIvPic);
                        mTextWeakHashMap.put("userid", mUserId);
                        mTextWeakHashMap.put("token", mToken);
                        long time = RxTimeTool.getCurTimeMills();
                        String timeStr = String.valueOf(time);
                        String secretKey = TimeUtils.getSecretKey(timeStr);
                        mTextWeakHashMap.put("secret_key", secretKey);
                        mTextWeakHashMap.put("time", timeStr);
                        WeakHashMap<String, String> ImageWeakHashMap = new WeakHashMap<>();
                        ImageWeakHashMap.put("image", args.getPath());
                        isFinish = false;
                        new Labi_HttpPostUploadUtil(getContext(), mHandler, HttpConstants.UpdateUserInfo, mTextWeakHashMap, ImageWeakHashMap).start();

                    }
                });
                break;
            case R.id.btn_commit:
                mName = mEditName.getText().toString().trim();
                String email = mEditEmail.getText().toString().trim();
                String remark = mEditRemark.getText().toString().trim();
                if (mCbMale.isChecked()) {
                    mGender = 0;
                } else {
                    mGender = 1;
                }
                long time = RxTimeTool.getCurTimeMills();
                String timeStr = String.valueOf(time);
                String secretKey = TimeUtils.getSecretKey(timeStr);

                WeakHashMap<String, String> paramsMap = new WeakHashMap<>();
                paramsMap.put("userid", mUserId);
                paramsMap.put("token", mToken);
                paramsMap.put("secret_key", secretKey);
                paramsMap.put("time", timeStr);
                paramsMap.put("name", mName);
                paramsMap.put("email", email);
                paramsMap.put("gender", String.valueOf(mGender));
                paramsMap.put("introduce", remark);
                isFinish = true;
                new Labi_HttpPostUploadUtil(getContext(), mHandler, HttpConstants.UpdateUserInfo, paramsMap, null).start();
               /* RequestParams params = new RequestParams(paramsMap);
                RequestCenter.UpdateUserInfo(params, new DisposeDataListener() {
                    @Override
                    public void onSuccess(Object responseObj) {
                        RxToast.normal("修改成功");
                        EventBus.getDefault().post(new EventEmpty(name, image));
                        getSupportDelegate().pop();
                    }

                    @Override
                    public void onFailure(Object responseObj) {
                    OkHttpException error = (OkHttpException) responseObj;
                        int errorCode = error.getCode();
                        if (errorCode == 10000) {
                            RxToast.normal("另一设备登录此账号，请重新登录");
                            AccountManager.setSignState(false);
                            getSupportDelegate().start(new SignInDelegate());
                        }else{
                            RxToast.normal("修改失败"+errorCode);
                        }

                    }
                });*/

                break;
            case R.id.stv_phone:
                initPhoneDialog();
                break;
        }
    }

    private void initPhoneDialog() {
        mDialog.getLayoutParams().gravity = Gravity.CENTER;
        View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_phone, null);
        final EditText phoneNum = (EditText) dialogView.findViewById(R.id.edit_phone_num);
        final EditText code = (EditText) dialogView.findViewById(R.id.edit_code);
        mSendCode = (Button) dialogView.findViewById(R.id.btn_code);
        mSendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mPhoneNumCode = phoneNum.getText().toString().trim();
                getCode(mPhoneNumCode);
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
                } else {
                    RequestParams params = Utils.getRequestParams();
                    params.put("newphone", mPhoneNumCode);
                    params.put("code", code);
                    RequestCenter.UserUpdatePhone(params, new DisposeDataListener() {
                        @Override
                        public void onSuccess(Object responseObj) {
                            RxToast.normal("修改成功");
                            mDialog.cancel();
                        }

                        @Override
                        public void onFailure(Object responseObj) {
                            OkHttpException error = (OkHttpException) responseObj;
                            int errorCode = error.getCode();
                            if (errorCode == 10035) {
                                RxToast.normal("手机号码不能为空");
                            } else if (errorCode == 10036) {
                                RxToast.normal("手机号码错误");
                            } else if (errorCode == 10037) {
                                RxToast.normal("手机号码已存在");
                            } else if (errorCode == 10039) {
                                RxToast.normal("两次手机号码不能相同");
                            } else if (errorCode == 10000) {
                                RxToast.normal("另一设备登录此账号，请重新登录");
                                AccountManager.setSignState(false);
                                getSupportDelegate().start(new SignInDelegate());
                            } else {
                                RxToast.normal("修改手机号码失败");
                            }
                            mDialog.cancel();
                        }
                    });
                }
            }
        });
        mDialog.setContentView(dialogView);
        mDialog.show();
    }

    private void initTime() {
        mTimer = new Timer();
        final BaseTimerTask task = new BaseTimerTask(this);
        mTimer.schedule(task, 0, 1000);
    }

    public static PersonalDelegate create(UserInfoBean userInfoBean) {
        PersonalDelegate personalDelegate = new PersonalDelegate();
        Bundle bundle = new Bundle();
        bundle.putSerializable("UserInfo", userInfoBean);
        personalDelegate.setArguments(bundle);
        return personalDelegate;
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

    public void getCode(String mPhoneNumCode) {
        String type = "0";
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
                if (errorCode == 10018) {
                    RxToast.normal("发送验证码次数过多，请稍后重试");
                } else {
                    RxToast.normal("获取验证码失败，请稍后重试");
                }
            }
        });
    }
}
