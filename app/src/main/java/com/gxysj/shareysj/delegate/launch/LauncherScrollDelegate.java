package com.gxysj.shareysj.delegate.launch;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.allen.library.SuperButton;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.gxysj.shareysj.R;
import com.gxysj.shareysj.config.ShareYSJ;
import com.gxysj.shareysj.config_enum.OnLauncherFinishTag;
import com.gxysj.shareysj.delegate.base.YSJDelegate;
import com.gxysj.shareysj.listener.ILauncherListener;
import com.gxysj.shareysj.listener.IUserChecker;
import com.gxysj.shareysj.utils.SPUtil;
import com.gxysj.shareysj.utils.user_manager.AccountManager;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by 林佳荣 on 2018/4/11.
 * Github：https://github.com/ljrRookie
 * Function ：
 */

public class LauncherScrollDelegate extends YSJDelegate  {
    @BindView(R.id.banner)
    ConvenientBanner<Integer> mConvenientBanner = null;
    @BindView(R.id.btn_commit)
    SuperButton mBtnCommit;
    Unbinder unbinder;
    Unbinder unbinder1;
  //  private ConvenientBanner<Integer> mConvenientBanner = null;
    private static final ArrayList<Integer> INTEGERS = new ArrayList<>();
    private ILauncherListener mILauncherListener = null;

    private void initBanner() {
        INTEGERS.add(R.mipmap.launch_a);
        INTEGERS.add(R.mipmap.launch_b);
        INTEGERS.add(R.mipmap.launch_c);
        mConvenientBanner
                .setPages(new LauncherHolderCreator(), INTEGERS)
                .setCanLoop(false);
        mConvenientBanner.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position == INTEGERS.size() - 1) {
                    SPUtil.put(ShareYSJ.getApplicationContext(), ScrollLauncherTag.HAS_FIRST_LAUNCHER_APP.name(), true);
                    //检查用户是否已经登录
                    AccountManager.checkAccount(new IUserChecker() {
                        @Override
                        public void onSignIn() {
                            if (mILauncherListener != null) {
                                mILauncherListener.onLauncherFinish(OnLauncherFinishTag.SIGNED);
                            }
                        }

                        @Override
                        public void onNotSignIn() {
                            if (mILauncherListener != null) {
                                mILauncherListener.onLauncherFinish(OnLauncherFinishTag.NOT_SIGNED);
                            }
                        }
                    });
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof ILauncherListener) {
            mILauncherListener = (ILauncherListener) activity;
        }
    }

    @Override
    public Object setLayout() {
     /*   mConvenientBanner = new ConvenientBanner<>(getContext());
        return mConvenientBanner;*/
        return R.layout.delegate_launcher_sroll;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        initBanner();
    }

 /*   @Override
    public void onItemClick(int position) {
        //如果点击的是最后一个
        if (position == INTEGERS.size() - 1) {
            SPUtil.put(ShareYSJ.getApplicationContext(), ScrollLauncherTag.HAS_FIRST_LAUNCHER_APP.name(), true);
            //检查用户是否已经登录
            AccountManager.checkAccount(new IUserChecker() {
                @Override
                public void onSignIn() {
                    if (mILauncherListener != null) {
                        mILauncherListener.onLauncherFinish(OnLauncherFinishTag.SIGNED);
                    }
                }

                @Override
                public void onNotSignIn() {
                    if (mILauncherListener != null) {
                        mILauncherListener.onLauncherFinish(OnLauncherFinishTag.NOT_SIGNED);
                    }
                }
            });
        }
    }*/


    @OnClick(R.id.btn_commit)
    public void onViewClicked() {
        SPUtil.put(ShareYSJ.getApplicationContext(), ScrollLauncherTag.HAS_FIRST_LAUNCHER_APP.name(), true);
        //检查用户是否已经登录
        AccountManager.checkAccount(new IUserChecker() {
            @Override
            public void onSignIn() {
                if (mILauncherListener != null) {
                    mILauncherListener.onLauncherFinish(OnLauncherFinishTag.SIGNED);
                }
            }

            @Override
            public void onNotSignIn() {
                if (mILauncherListener != null) {
                    mILauncherListener.onLauncherFinish(OnLauncherFinishTag.NOT_SIGNED);
                }
            }
        });
    }
}
