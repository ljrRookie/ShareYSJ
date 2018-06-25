package com.gxysj.shareysj.delegate.main.home.ad;

import android.content.ComponentName;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.allen.library.SuperButton;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gxysj.shareysj.R;
import com.gxysj.shareysj.bean.ADPriceBean;
import com.gxysj.shareysj.bean.event.EventEmpty;
import com.gxysj.shareysj.config.ShareYSJ;
import com.gxysj.shareysj.delegate.PayDelegate;
import com.gxysj.shareysj.delegate.SignInDelegate;
import com.gxysj.shareysj.delegate.base.YSJDelegate;
import com.gxysj.shareysj.delegate.main.home.adapter.AdPriceAdapter;
import com.gxysj.shareysj.network.HttpConstants;
import com.gxysj.shareysj.network.RequestCenter;
import com.gxysj.shareysj.ui.AutoPhotoLayout;
import com.gxysj.shareysj.utils.Labi_HttpPostUploadUtil;
import com.gxysj.shareysj.utils.SPUtil;
import com.gxysj.shareysj.utils.TimeUtils;
import com.gxysj.shareysj.utils.callback.CallbackManager;
import com.gxysj.shareysj.utils.callback.CallbackType;
import com.gxysj.shareysj.utils.callback.IGlobalCallback;
import com.gxysj.shareysj.utils.user_manager.AccountManager;
import com.ljr.delegate_sdk.okhttp.exception.OkHttpException;
import com.ljr.delegate_sdk.okhttp.https.HttpsUtils;
import com.ljr.delegate_sdk.okhttp.listener.DisposeDataListener;
import com.vondear.rxtools.RxLogTool;
import com.vondear.rxtools.RxTimeTool;
import com.vondear.rxtools.model.ActionItem;
import com.vondear.rxtools.view.RxToast;
import com.vondear.rxtools.view.popupwindows.RxPopupSingleView;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import me.yokeyword.fragmentation.ISupportFragment;
import me.yokeyword.fragmentation.SupportFragmentDelegate;

/**
 * Created by 林佳荣 on 2018/3/27.
 * Github：https://github.com/ljrRookie
 * Function ：
 */

public class AdForPayDelegate extends YSJDelegate {


    @BindView(R.id.btn_back)
    ImageView mBtnBack;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.edit_title)
    EditText mEditTitle;
    @BindView(R.id.edit_content)
    EditText mEditContent;
    @BindView(R.id.auto_photo_layout)
    AutoPhotoLayout mAutoPhotoLayout;
    @BindView(R.id.tv_time)
    TextView mTvTime;
    @BindView(R.id.tv_price)
    TextView mTvPrice;
    @BindView(R.id.btn_pay)
    SuperButton mBtnPay;
    @BindView(R.id.shopping_cart_bottom)
    RelativeLayout mShoppingCartBottom;
    Unbinder unbinder;
    private RxPopupSingleView mRxPopupSingleView;
    private WeakHashMap<String, String> mImageFileWeakHashMap;
    private int mTimeId;
    private String mTimeStr;
    private String mMoney;
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
                    double money = JSON.parseObject(json).getDouble("money");
                    String orderid = JSON.parseObject(json).getString("uuid");
                    RxLogTool.e("广告","发布："+orderid);
                    getSupportDelegate().popQuiet();

                    getSupportDelegate().start(PayDelegate.create(money,orderid,1000));

                } else if (code == 10000) {
                    RxToast.normal("另一设备登录此账号，请重新登录");
                    AccountManager.setSignState(false);
                    getSupportDelegate().pop();
                    getSupportDelegate().start(new SignInDelegate());
                }else if (code == 10074 ) {
                    RxToast.normal("至少上传一张图片");
                } else {
                    RxToast.normal("操作失败" + code);
                }
            }

        }
    };
    private String mUserId;
    private String mToken;

    @Override
    public Object setLayout() {
        return R.layout.delegate_home_ad_pay;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        mTvTitle.setText("发布广告");
        mUserId = (String) SPUtil.get(ShareYSJ.getApplicationContext(), "userId", "");
        mToken = (String) SPUtil.get(ShareYSJ.getApplicationContext(), "token", "");
        mAutoPhotoLayout.setDelegate(this);
        CallbackManager.getIntance()
                .addCallback(CallbackType.ON_CROP, new IGlobalCallback<Uri>() {
                    @Override
                    public void excuteCallback(@Nullable Uri args) {
                        mAutoPhotoLayout.onCropTarget(args);
                    }
                });
        initData();
        mTvTime.setText(mTimeStr);
        mTvPrice.setText(mMoney);
        mImageFileWeakHashMap = new WeakHashMap<>();

    }

    private void initData() {
        RequestCenter.GetAdvertisementPriceList(new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                ADPriceBean adPriceBean = (ADPriceBean) responseObj;
                final List<ADPriceBean.DataBean> data = adPriceBean.getData();
                mRxPopupSingleView = new RxPopupSingleView(getActivity(), ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT, R.layout.item_ad_price);
                mRxPopupSingleView.setColorItemText(getActivity().getResources().getColor(R.color.black));

                if(data!=null&& data.size()>0){
                    for (int i = 0; i < data.size(); i++) {
                        mRxPopupSingleView.addAction(new ActionItem(data.get(i).getDay_time()));
                    }
                    mRxPopupSingleView.setItemOnClickListener(new RxPopupSingleView.OnItemOnClickListener() {
                        @Override
                        public void onItemClick(ActionItem actionItem, int i) {
                            ActionItem action = mRxPopupSingleView.getAction(i);
                            mTvTime.setText(action.mTitle);
                            String money = data.get(i).getMoney();
                            mTvPrice.setText(money);
                            mTimeId = data.get(i).getId();
                        }
                    });
                }

            }

            @Override
            public void onFailure(Object responseObj) {
                OkHttpException error = (OkHttpException) responseObj;
                RxToast.normal("获取广告价格失败-(" + error.getEmsg() + ")");
            }
        });
    }


    @OnClick({R.id.btn_back, R.id.tv_time, R.id.btn_pay})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                getSupportDelegate().pop();
                break;
            case R.id.tv_time:
                mRxPopupSingleView.show(mTvTime, 0);
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
                paramsMap.put("timeid", String.valueOf(mTimeId));
                for (Map.Entry<String, String> entry : paramsMap.entrySet()) {
                    RxLogTool.e("广告", " Key = " + entry.getKey() + ", Value = " + entry.getValue());
                }
                new Labi_HttpPostUploadUtil(getContext(), mHandler,HttpConstants.UserAddAdvertisement, paramsMap, mImageFileWeakHashMap).start();
                break;
        }
    }

    public static AdForPayDelegate create(int id, String dayTime, String money) {
        AdForPayDelegate adForPayDelegate = new AdForPayDelegate();
        Bundle bundle = new Bundle();
        bundle.putInt("timeId",id);
        bundle.putString("timeStr",dayTime);
        bundle.putString("money",money);
        adForPayDelegate.setArguments(bundle);
        return adForPayDelegate;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        mTimeId = arguments.getInt("timeId");
        mTimeStr = arguments.getString("timeStr");
        mMoney = arguments.getString("money");
    }
}
