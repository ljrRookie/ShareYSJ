package com.gxysj.shareysj.delegate.main.mine.ad;

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
import com.gxysj.shareysj.R;
import com.gxysj.shareysj.bean.UserAdBean;
import com.gxysj.shareysj.config.ShareYSJ;
import com.gxysj.shareysj.delegate.PayDelegate;
import com.gxysj.shareysj.delegate.SignInDelegate;
import com.gxysj.shareysj.delegate.base.YSJDelegate;
import com.gxysj.shareysj.network.HttpConstants;
import com.gxysj.shareysj.ui.AutoPhotoLayout;
import com.gxysj.shareysj.utils.Labi_HttpPostUploadUtil;
import com.gxysj.shareysj.utils.SPUtil;
import com.gxysj.shareysj.utils.TimeUtils;
import com.gxysj.shareysj.utils.callback.CallbackManager;
import com.gxysj.shareysj.utils.callback.CallbackType;
import com.gxysj.shareysj.utils.callback.IGlobalCallback;
import com.gxysj.shareysj.utils.user_manager.AccountManager;
import com.ljr.delegate_sdk.okhttp.exception.OkHttpException;
import com.vondear.rxtools.RxLogTool;
import com.vondear.rxtools.RxTimeTool;
import com.vondear.rxtools.view.RxToast;

import java.util.Map;
import java.util.WeakHashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by 林佳荣 on 2018/5/11.
 * Github：https://github.com/ljrRookie
 * Function ：
 */

public class AdAgainDelegate extends YSJDelegate {

    @BindView(R.id.btn_back)
    ImageView mBtnBack;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.tv_reason)
    TextView mTvReason;
    @BindView(R.id.tv_time)
    TextView mTvTime;
    @BindView(R.id.edit_title)
    EditText mEditTitle;
    @BindView(R.id.edit_content)
    EditText mEditContent;
    @BindView(R.id.tv_flag_photo)
    TextView mTvFlagPhoto;
    @BindView(R.id.auto_photo_layout)
    AutoPhotoLayout mAutoPhotoLayout;
    @BindView(R.id.btn_pay)
    SuperButton mBtnPay;
    private UserAdBean.DataBean mData;
    private WeakHashMap<String, String> mImageFileWeakHashMap;
    private String mUserId;
    private String mToken;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String json = data.getString("data");
            if (json.equals("10000")) {
                RxToast.normal("操作失败");
            } else {
                int code = JSON.parseObject(json).getIntValue("code");
                if (code == 0) {
                    getSupportDelegate().pop();
                    RxToast.normal("发布成功，请等待审核");
                } else if (code == 10000) {
                    RxToast.normal("登录异常，请重新登录");
                    AccountManager.setSignState(false);
                    getSupportDelegate().pop();
                    getSupportDelegate().start(new SignInDelegate());
                }else if (code == 10074 ) {
                    RxToast.normal("至少上传一张图片");
                } else {
                    OkHttpException okHttpException = new OkHttpException(code, "");
                    String emsg = (String) okHttpException.getEmsg();
                    RxToast.normal("操作失败(" + emsg+")");
                }
            }

        }
    };
    @Override
    public Object setLayout() {
        return R.layout.delegate_ad_again;
    }

    public static AdAgainDelegate create(UserAdBean.DataBean dataBean) {
        AdAgainDelegate adAgainDelegate = new AdAgainDelegate();
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", dataBean);
        adAgainDelegate.setArguments(bundle);
        return adAgainDelegate;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        mData = (UserAdBean.DataBean) arguments.getSerializable("data");
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        mTvTitle.setText("重新发布");
        initData();
        mAutoPhotoLayout.setDelegate(this);
        mUserId = (String) SPUtil.get(ShareYSJ.getApplicationContext(), "userId", "");
        mToken = (String) SPUtil.get(ShareYSJ.getApplicationContext(), "token", "");
        CallbackManager.getIntance()
                .addCallback(CallbackType.ON_CROP, new IGlobalCallback<Uri>() {
                    @Override
                    public void excuteCallback(@Nullable Uri args) {
                        mAutoPhotoLayout.onCropTarget(args);
                    }
                });
        mImageFileWeakHashMap = new WeakHashMap<>();
    }

    private void initData() {
        String feedback = mData.getFeedback();
        mTvReason.setText(feedback);
        String title = mData.getTitle();
        mEditTitle.setText(title);
        String content = mData.getContent();
        mEditContent.setText(content);
    }


    @OnClick({R.id.btn_back, R.id.btn_pay})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                getSupportDelegate().pop();
                break;
            case R.id.btn_pay:
                String title = mEditTitle.getText().toString().trim();
                String content = mEditContent.getText().toString().trim();
                if(TextUtils.isEmpty(title)){
                    RxToast.normal("标题不能为空");
                    return;
                }
                if(TextUtils.isEmpty(content)){
                    RxToast.normal("内容不能为空");
                    return;
                }
                if(title.length()>20){
                    RxToast.normal("标题请限制在20字以内");
                    return;
                }
                if(content.length()>1000){
                    RxToast.normal("内容请限制在1000字以内");
                    return;
                }
                WeakHashMap<Integer, String> imageFiles = mAutoPhotoLayout.getImageFiles();
                String s = imageFiles.get(0);
                if(TextUtils.isEmpty(s)){
                    RxToast.normal("至少选择一张图片");
                    return;
                }
                for (int j = 0; j < imageFiles.size(); j++) {
                    int num = j + 1;
                    String image = imageFiles.get(j);
                    if (TextUtils.isEmpty(image)) {
                        image = "";
                    }else{
                        mImageFileWeakHashMap.put("image"+num,image );
                    }

                }
                for (Map.Entry<String, String> entry : mImageFileWeakHashMap.entrySet()) {
                    RxLogTool.e("广告", " Key = " + entry.getKey() + ", Value = " + entry.getValue());
                }
                long time = RxTimeTool.getCurTimeMills();
                String timeStr = String.valueOf(time);
                String secretKey = TimeUtils.getSecretKey(timeStr);

                WeakHashMap<String, String> paramsMap = new WeakHashMap<>();
                paramsMap.put("userid", mUserId);
                paramsMap.put("token", mToken);
                paramsMap.put("secret_key", secretKey);
                paramsMap.put("time", timeStr);
                paramsMap.put("title", title);
                paramsMap.put("content", content);
                paramsMap.put("id", mData.getId());
                for (Map.Entry<String, String> entry : paramsMap.entrySet()) {
                    RxLogTool.e("广告", " Key = " + entry.getKey() + ", Value = " + entry.getValue());
                }
                new Labi_HttpPostUploadUtil(getContext(), mHandler, HttpConstants.UserUpdateAdvertisement, paramsMap, mImageFileWeakHashMap).start();
                break;
        }
    }
}
