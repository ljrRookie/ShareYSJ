package com.gxysj.shareysj.delegate.main.mine;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.allen.library.SuperTextView;
import com.gxysj.shareysj.R;
import com.gxysj.shareysj.delegate.base.YSJDelegate;
import com.gxysj.shareysj.delegate.main.mine.safety.CheckPersonIdDelegate;
import com.gxysj.shareysj.delegate.main.mine.safety.LoginPwdDelegate;
import com.gxysj.shareysj.delegate.main.mine.safety.PayPwdDelegate;
import com.vondear.rxtools.view.RxToast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import me.yokeyword.fragmentation.ISupportFragment;

/**
 * Created by 林佳荣 on 2018/3/9.
 * Github：https://github.com/ljrRookie
 * Function ：
 */

public class SafetySettingDelegate extends YSJDelegate {
    @BindView(R.id.btn_back)
    ImageView mBtnBack;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.btn_right)
    ImageView mBtnRight;
    @BindView(R.id.btn_login_pwd)
    SuperTextView mBtnLoginPwd;
    @BindView(R.id.btn_pay_pwd)
    SuperTextView mBtnPayPwd;
    @BindView(R.id.btn_person_id)
    SuperTextView mBtnPersonId;
    Unbinder unbinder;
    private int mIds_state;

    @Override
    public Object setLayout() {
        return R.layout.delegate_mine_setting_safety;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        mIds_state = arguments.getInt("ids_state");
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        mTvTitle.setText("安全中心");
        if(mIds_state == 0){
            //审核中
            mBtnPersonId.setLeftString("实名认证（审核中）");
        }else if(mIds_state == 1){
            //审核通过
            mBtnPersonId.setLeftString("实名认证（已认证）");
        }else if(mIds_state == 2){
            //审核未通过
            mBtnPersonId.setLeftString("实名认证（认证失败,前往个人消息查看原因）");

        }
    }


    @OnClick({R.id.btn_back, R.id.btn_login_pwd, R.id.btn_pay_pwd, R.id.btn_person_id})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                getSupportDelegate().pop();
                break;
            case R.id.btn_login_pwd:
                getSupportDelegate().start(new LoginPwdDelegate());
                break;
            case R.id.btn_pay_pwd:
                getSupportDelegate().start(new PayPwdDelegate());
                break;
            case R.id.btn_person_id:
                if(mIds_state == 0){
                    //审核中
                    RxToast.normal("审核中....");
                }else if(mIds_state == 1){
                    //审核通过
                    RxToast.normal("审核已通过.");
                }else if(mIds_state == 2){
                    //审核未通过
                    getSupportDelegate().start( CheckPersonIdDelegate.create(mIds_state));
                }else if(mIds_state == 3){
                    //未提交
                    getSupportDelegate().start( CheckPersonIdDelegate.create(mIds_state));
                }

                break;
        }
    }

    public static SafetySettingDelegate create(int ids_state) {
        SafetySettingDelegate safetySettingDelegate = new SafetySettingDelegate();
        Bundle bundle = new Bundle();
        bundle.putInt("ids_state", ids_state);
        safetySettingDelegate.setArguments(bundle);
        return safetySettingDelegate;
    }
}
