package com.gxysj.shareysj.delegate.main.mine.safety;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.allen.library.SuperButton;
import com.bumptech.glide.Glide;
import com.gxysj.shareysj.R;
import com.gxysj.shareysj.bean.UserInfoBean;
import com.gxysj.shareysj.bean.event.EventEmpty;
import com.gxysj.shareysj.config.ShareYSJ;
import com.gxysj.shareysj.delegate.SignInDelegate;
import com.gxysj.shareysj.delegate.base.YSJDelegate;
import com.gxysj.shareysj.network.HttpConstants;
import com.gxysj.shareysj.network.RequestCenter;
import com.gxysj.shareysj.utils.Labi_HttpPostUploadUtil;
import com.gxysj.shareysj.utils.SPUtil;
import com.gxysj.shareysj.utils.TimeUtils;
import com.gxysj.shareysj.utils.callback.CallbackManager;
import com.gxysj.shareysj.utils.callback.CallbackType;
import com.gxysj.shareysj.utils.callback.IGlobalCallback;
import com.gxysj.shareysj.utils.user_manager.AccountManager;
import com.gxysj.shareysj.utils.user_manager.Utils;
import com.ljr.delegate_sdk.okhttp.exception.OkHttpException;
import com.ljr.delegate_sdk.okhttp.listener.DisposeDataListener;
import com.ljr.delegate_sdk.okhttp.request.RequestParams;
import com.vondear.rxtools.RxLogTool;
import com.vondear.rxtools.RxTimeTool;
import com.vondear.rxtools.view.RxToast;

import org.greenrobot.eventbus.EventBus;

import java.util.WeakHashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import me.yokeyword.fragmentation.ISupportFragment;

/**
 * Created by 林佳荣 on 2018/3/26.
 * Github：https://github.com/ljrRookie
 * Function ：
 */

public class CheckPersonIdDelegate extends YSJDelegate {
    @BindView(R.id.btn_back)
    ImageView mBtnBack;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.btn_right)
    ImageView mBtnRight;
    @BindView(R.id.tv_name)
    TextView mTvName;
    @BindView(R.id.edit_name)
    EditText mEditName;
    @BindView(R.id.edit_num)
    EditText mEditNum;
    @BindView(R.id.iv_photo_a)
    ImageView mIvPhotoA;
    @BindView(R.id.iv_photo_b)
    ImageView mIvPhotoB;
    @BindView(R.id.btn_commit)
    SuperButton mBtnCommit;
    private int mIds_state;
    private boolean mPhotoA = false;
    private boolean mPhotoB = false;
    private WeakHashMap<String, String> mTextWeakHashMap;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String json = data.getString("data");
            RxLogTool.e("修改个人信息", json);
            if (json.equals("10000")) {
                RxToast.normal("提交失败");
            } else {
                int code = JSON.parseObject(json).getIntValue("code");
                if (code == 0) {
                    RxToast.normal("提交成功");
                    getSupportDelegate().pop();
                } else if (code == 10000) {
                    RxToast.normal("登录异常，请重新登录");
                    AccountManager.setSignState(false);
                    getSupportDelegate().pop();
                    getSupportDelegate().start(new SignInDelegate());
                } else {
                    RxToast.normal("提交失败" + code);
                }
            }

        }
    };
    private WeakHashMap<String, String> mImageWeakHashMap;
    private String mUserId;
    private String mToken;
    private UserInfoBean mUserInfoBean;

    @Override
    public Object setLayout() {
        return R.layout.delegate_mine_setting_safety_check_id;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        mIds_state = arguments.getInt("ids_state");
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        mTvTitle.setText("实名认证");
        mTextWeakHashMap = new WeakHashMap<>();
        mImageWeakHashMap = new WeakHashMap<>();
        mUserId = (String) SPUtil.get(ShareYSJ.getApplicationContext(), "userId", "");
        mToken = (String) SPUtil.get(ShareYSJ.getApplicationContext(), "token", "");
        if (mIds_state == 1) {
            mBtnCommit.setVisibility(View.GONE);
            initData();
            mTvName.setText("个人信息");
        }
    }

    private void initData() {

        RequestParams requestParams = Utils.getRequestParams();
        RequestCenter.GetUserInfo(requestParams, new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                mUserInfoBean = (UserInfoBean) responseObj;

                mEditName.setFocusable(false);
                mEditName.setFocusableInTouchMode(false);
                mEditName.setText(mUserInfoBean.getFull_name());
                mEditNum.setFocusable(false);
                mEditNum.setFocusableInTouchMode(false);
                mEditNum.setText(mUserInfoBean.getIds());
                Glide.with(getActivity()).load(HttpConstants.ROOT_URL + mUserInfoBean.getIds_image1()).into(mIvPhotoA);
                Glide.with(getActivity()).load(HttpConstants.ROOT_URL + mUserInfoBean.getIds_image2()).into(mIvPhotoB);

                mIvPhotoA.setClickable(false);
                mIvPhotoB.setClickable(false);
            }

            @Override
            public void onFailure(Object responseObj) {
                OkHttpException error = (OkHttpException) responseObj;
                int errorCode = error.getCode();
                if (errorCode == 10000) {
                    RxToast.normal("登录异常，请重新登录");
                    AccountManager.setSignState(false);
                    getParentDelegate().getSupportDelegate().start(new SignInDelegate());
                } else {
                    RxToast.normal("获取用户数据失败(" + error.getEmsg() + ")");
                }
            }
        });
    }

    @OnClick({R.id.btn_back, R.id.iv_photo_a, R.id.iv_photo_b, R.id.btn_commit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                getSupportDelegate().pop();
                break;
            case R.id.iv_photo_a:
                startCameraWithCheck();
                CallbackManager.getIntance().addCallback(CallbackType.ON_CROP, new IGlobalCallback<Uri>() {
                    @Override
                    public void excuteCallback(@Nullable Uri args) {
                        mPhotoA = true;
                        mImageWeakHashMap.put("image1", args.getPath());
                        Glide.with(getActivity()).load(args).into(mIvPhotoA);
                    }
                });
                break;
            case R.id.iv_photo_b:
                startCameraWithCheck();
                CallbackManager.getIntance().addCallback(CallbackType.ON_CROP, new IGlobalCallback<Uri>() {
                    @Override
                    public void excuteCallback(@Nullable Uri args) {
                        mPhotoB = true;
                        mImageWeakHashMap.put("image2", args.getPath());
                        Glide.with(getActivity()).load(args).into(mIvPhotoB);
                    }
                });
                break;
            case R.id.btn_commit:
                String name = mEditName.getText().toString().trim();
                String num = mEditNum.getText().toString().trim();
                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(num)) {
                    RxToast.normal("输入的姓名和身份证号码不能为空");
                    return;
                }
                if (!mPhotoA) {
                    RxToast.normal("请上传身份证正面照片");
                    return;
                }
                if (!mPhotoB) {
                    RxToast.normal("请上传身份证反面照片");
                    return;
                }
                long time = RxTimeTool.getCurTimeMills();
                String timeStr = String.valueOf(time);
                String secretKey = TimeUtils.getSecretKey(timeStr);
                mTextWeakHashMap.put("userid", mUserId);
                mTextWeakHashMap.put("token", mToken);
                mTextWeakHashMap.put("secret_key", secretKey);
                mTextWeakHashMap.put("time", timeStr);
                mTextWeakHashMap.put("ids", num);
                mTextWeakHashMap.put("name", name);
                new Labi_HttpPostUploadUtil(getContext(), mHandler, HttpConstants.UserIDSAuthentication, mTextWeakHashMap, mImageWeakHashMap).start();

                break;
        }
    }

    public static CheckPersonIdDelegate create(int ids_state) {
        CheckPersonIdDelegate checkPersonIdDelegate = new CheckPersonIdDelegate();
        Bundle bundle = new Bundle();
        bundle.putInt("ids_state", ids_state);
        checkPersonIdDelegate.setArguments(bundle);
        return checkPersonIdDelegate;
    }
}
