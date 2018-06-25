package com.gxysj.shareysj.delegate.main.mine;

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
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by 林佳荣 on 2018/3/9.
 * Github：https://github.com/ljrRookie
 * Function ：
 */

public class SuggestDelegate extends YSJDelegate {
    @BindView(R.id.btn_back)
    ImageView mBtnBack;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.btn_right)
    ImageView mBtnRight;
    @BindView(R.id.tv)
    TextView mTv;
    @BindView(R.id.edit_title)
    EditText mEditTitle;
    @BindView(R.id.content)
    TextView mContent;
    @BindView(R.id.edit_content)
    EditText mEditContent;
    @BindView(R.id.btn_commit)
    Button mBtnCommit;
    Unbinder unbinder;

    @Override
    public Object setLayout() {
        return R.layout.delegate_mine_suggest;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        mTvTitle.setText("意见反馈");
    }


    @OnClick({R.id.btn_back, R.id.btn_commit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                getSupportDelegate().pop();
                break;
            case R.id.btn_commit:
                String title = mEditTitle.getText().toString().trim();
                String content = mEditContent.getText().toString().trim();
                if(TextUtils.isEmpty(title)&&TextUtils.isEmpty(content)){
                    RxToast.normal("输入的内容不能为空");
                    return;
                }
                if(title.length()>20){
                    RxToast.normal("标题超过字数限制");
                    return;
                }
                if(content.length()>1000){
                    RxToast.normal("内容超过字数限制");
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
                requestParams.put("title", title);
                requestParams.put("content", content);
                RequestCenter.UserFeedback(requestParams, new DisposeDataListener() {
                    @Override
                    public void onSuccess(Object responseObj) {
                        RxToast.normal("提交成功");
                        getSupportDelegate().pop();
                    }

                    @Override
                    public void onFailure(Object responseObj) {
                        OkHttpException error = (OkHttpException) responseObj;
                        RxToast.normal("提交失败 - （"+error.getEmsg()+")");
                    }
                });
                break;
        }
    }
}
