package com.gxysj.shareysj.activity;

import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.util.Log;

import com.gxysj.shareysj.R;
import com.gxysj.shareysj.config.ShareYSJ;
import com.gxysj.shareysj.config_enum.OnLauncherFinishTag;
import com.gxysj.shareysj.delegate.LauncherDelegate;
import com.gxysj.shareysj.delegate.SignInDelegate;
import com.gxysj.shareysj.delegate.base.YSJDelegate;
import com.gxysj.shareysj.delegate.home.BottomDelegate;
import com.gxysj.shareysj.listener.ILauncherListener;
import com.gxysj.shareysj.listener.ISignListener;
import com.gxysj.shareysj.utils.SPUtil;
import com.vondear.rxtools.view.RxToast;

import me.yokeyword.fragmentation.Fragmentation;
import me.yokeyword.fragmentation.anim.DefaultHorizontalAnimator;
import me.yokeyword.fragmentation.anim.FragmentAnimator;
import qiu.niorgai.StatusBarCompat;

import static com.gxysj.shareysj.config.ShareYSJ.getActivity;

public class MainActivity extends ProxyActivity implements ILauncherListener,ISignListener {

    @Override
    public YSJDelegate setRootDelegate() {
        return new LauncherDelegate();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        StatusBarCompat.translucentStatusBar(this);
        ShareYSJ.getConfigurator().withActivity(this);
    }

    @Override
    public void onLauncherFinish(OnLauncherFinishTag tag) {
        switch (tag){
            case SIGNED:
                extraTransaction()
                        .setCustomAnimations(R.anim.v_fragment_enter, R.anim.v_fragment_pop_exit,
                                R.anim.v_fragment_pop_enter, R.anim.v_fragment_exit)
                        .start(new BottomDelegate());

                break;
            case NOT_SIGNED:
            //    getSupportDelegate().startWithPop(new BottomDelegate());
                extraTransaction()
                        .setCustomAnimations(R.anim.v_fragment_enter, R.anim.v_fragment_pop_exit,
                                R.anim.v_fragment_pop_enter, R.anim.v_fragment_exit)
                        .start(new BottomDelegate());
                break;
            default:
                break;
        }
    }

    @Override
    public void onSignInSuccess() {

    }

    @Override
    public void onSignUpSuccess() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("clear", "activity : onDestroy");
    }
}
