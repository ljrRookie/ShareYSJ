package com.gxysj.shareysj.delegate.main.generalize;

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

/**
 * Created by 林佳荣 on 2018/3/21.
 * Github：https://github.com/ljrRookie
 * Function ：加盟申请
 */

public class RegisterAllianceDelegate extends YSJDelegate {
    @BindView(R.id.btn_back)
    ImageView mBtnBack;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.btn_right)
    ImageView mBtnRight;
    @BindView(R.id.edit_name)
    EditText mEditName;
    @BindView(R.id.edit_num)
    EditText mEditNum;

    @BindView(R.id.btn_commit)
    SuperButton mBtnCommit;
    @BindView(R.id.iv_photo_a)
    ImageView mIvPhotoA;
    @BindView(R.id.iv_pic)
    ImageView mIvPic;
    private WeakHashMap<String, String> mTextWeakHashMap;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String json = data.getString("data");
            RxLogTool.e("申请加盟商", json);
            if (json.equals("10000")) {
                RxToast.normal("操作失败，请稍后重试");
            } else {
                int code = JSON.parseObject(json).getIntValue("code");
                if (code == 0) {
                    RxToast.normal("提交申请成功");
                    getSupportDelegate().pop();
                } else {
                    RxToast.normal("提交失败，请稍后重试" + code);
                }
            }

        }
    };
    private WeakHashMap<String, String> mImageWeakHashMap;
    private boolean image = false;
    private boolean business_license = false;

    @Override
    public Object setLayout() {
        return R.layout.delegate_generalize_alliance_register;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        mTvTitle.setText("加盟商申请");
        mTextWeakHashMap = new WeakHashMap<>();
        mImageWeakHashMap = new WeakHashMap<>();
    }


    @OnClick({R.id.btn_back, R.id.iv_photo_a, R.id.iv_pic, R.id.btn_commit})
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
                        business_license = true;
                        Glide.with(getActivity()).load(args).into(mIvPhotoA);
                        mImageWeakHashMap.put("business_license", args.getPath());
                    }
                });
                break;
            case R.id.iv_pic:
                startCameraWithCheck();
                CallbackManager.getIntance().addCallback(CallbackType.ON_CROP, new IGlobalCallback<Uri>() {
                    @Override
                    public void excuteCallback(@Nullable Uri args) {
                        image = true;
                        Glide.with(getActivity()).load(args).into(mIvPic);
                        mImageWeakHashMap.put("image", args.getPath());
                    }
                });
                break;
            case R.id.btn_commit:
                String name = mEditName.getText().toString().trim();
                String number = mEditNum.getText().toString().trim();
                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(number)) {
                    RxToast.normal("输入的信息不能为空");
                    return;
                }

                if(!business_license){
                    RxToast.normal("营业执照不能为空");
                    return;
                }
                long time = RxTimeTool.getCurTimeMills();
                String timeStr = String.valueOf(time);
                String secretKey = TimeUtils.getSecretKey(timeStr);

                mTextWeakHashMap.put("userid", (String) SPUtil.get(ShareYSJ.getApplicationContext(), "userId", ""));
                mTextWeakHashMap.put("token", (String) SPUtil.get(ShareYSJ.getApplicationContext(), "token", ""));
                mTextWeakHashMap.put("secret_key", secretKey);
                mTextWeakHashMap.put("time", timeStr);
                mTextWeakHashMap.put("name", name);
                mTextWeakHashMap.put("business_number", number);
                new Labi_HttpPostUploadUtil(getContext(), mHandler, HttpConstants.UserApplyFranchiser, mTextWeakHashMap, mImageWeakHashMap).start();
                break;
        }
    }


}
