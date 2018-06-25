package com.gxysj.shareysj.delegate;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.AppCompatTextView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.allen.library.SuperButton;
import com.bumptech.glide.Glide;
import com.gxysj.shareysj.R;
import com.gxysj.shareysj.bean.AppImageBean;
import com.gxysj.shareysj.bean.AppUpdateBean;
import com.gxysj.shareysj.config.ShareYSJ;
import com.gxysj.shareysj.config_enum.OnLauncherFinishTag;
import com.gxysj.shareysj.delegate.base.YSJDelegate;
import com.gxysj.shareysj.delegate.launch.LauncherScrollDelegate;
import com.gxysj.shareysj.delegate.launch.ScrollLauncherTag;
import com.gxysj.shareysj.listener.ILauncherListener;
import com.gxysj.shareysj.listener.ITimerListener;
import com.gxysj.shareysj.listener.IUserChecker;
import com.gxysj.shareysj.network.HttpConstants;
import com.gxysj.shareysj.network.RequestCenter;
import com.gxysj.shareysj.service.UpdateService;
import com.gxysj.shareysj.utils.BaseTimerTask;
import com.gxysj.shareysj.utils.SPUtil;
import com.gxysj.shareysj.utils.user_manager.AccountManager;
import com.gxysj.shareysj.utils.user_manager.Utils;
import com.ljr.delegate_sdk.okhttp.listener.DisposeDataListener;
import com.ljr.delegate_sdk.okhttp.request.RequestParams;

import com.vondear.rxtools.view.RxToast;
import com.vondear.rxtools.view.dialog.RxDialog;

import java.text.MessageFormat;
import java.util.List;
import java.util.Timer;

import butterknife.BindView;
import butterknife.OnClick;
import qiu.niorgai.StatusBarCompat;

import static com.gxysj.shareysj.application.MyApplication.mContext;

/**
 * Created by Administrator on 2018/1/29.
 */

public class LauncherDelegate extends YSJDelegate implements ITimerListener {
    @BindView(R.id.iv_bg)
    ImageView mIvBg;
    @BindView(R.id.tv_launcher_timer)
    AppCompatTextView mTvLauncherTimer;

    @BindView(R.id.btn_commit)
    SuperButton mBtnCommit;

    private ILauncherListener mILauncherListener = null;

    private Timer mTimer = null;
    private int mCount = 5;
    private RxDialog mDialog;

    @Override
    public Object setLayout() {
        return R.layout.delegate_launcher;
    }

    @Override
    public void onAttach(Context context) {
        if (context instanceof ILauncherListener) {
            mILauncherListener = (ILauncherListener) context;
        }
        super.onAttach(context);
    }

    //SDK API<23时，onAttach(Context)不执行，需要使用onAttach(Activity)。Fragment自身的Bug，v4的没有此问题
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            if (activity instanceof ILauncherListener) {
                mILauncherListener = (ILauncherListener) activity;
            }
        }
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {

       // initImage( getProxyActivity(),mIvBg,R.mipmap.bg_launch);
        initData();
        mDialog = new RxDialog(getActivity());

    }

    private void initData() {
        RequestCenter.GetAppImage(new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                AppImageBean appImageBean = (AppImageBean) responseObj;
                List<AppImageBean.DataBean> data = appImageBean.getData();
               if(data!=null&& data.size()>0){
                   AppImageBean.DataBean dataBean = data.get(0);
                   String image = dataBean.getImage();
                   Glide.with(mContext).load(HttpConstants.ROOT_URL + image).placeholder(R.mipmap.bg_launch).error(R.mipmap.bg_launch).into(mIvBg);
               }
            }

            @Override
            public void onFailure(Object responseObj) {

            }
        });
        RequestParams params = new RequestParams();
        params.put("type",0);
        RequestCenter.AppUpdate(params, new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                AppUpdateBean appUpdateBean = (AppUpdateBean) responseObj;
                if(Utils.getVersionCode(getContext()) < appUpdateBean.getVersionCode()){
                    //有新版本
                    showUpdateDialog(appUpdateBean);
                }else{
                    initTimer();
                }
            }

            @Override
            public void onFailure(Object responseObj) {
                initTimer();
            }
        });
    }

    private void showUpdateDialog(final AppUpdateBean appUpdateBean) {
        mDialog.getLayoutParams().gravity = Gravity.CENTER;
        mDialog.setCanceledOnTouchOutside(false);
        View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_update_app, null);
        TextView title = (TextView) dialogView.findViewById(R.id.tv_title);
        title.setText(appUpdateBean.getName());
        TextView msg = (TextView) dialogView.findViewById(R.id.tv_msg);
        msg.setText(appUpdateBean.getVersion_notes());
        dialogView.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String updatekey = appUpdateBean.getUpdatekey();
                if (updatekey.equals("2")) {
                    RxToast.normal("此次为强制升级app,需升级后才能正常使用");
                } else {
                    mDialog.cancel();
                    checkIsSign();
                }
            }
        });
        dialogView.findViewById(R.id.btn_commit).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String apkurl = appUpdateBean.getApkurl();
                Intent intent = new Intent(getActivity(), UpdateService.class);

                intent.putExtra("apk", apkurl);
                getActivity().startService(intent);
                checkIsSign();
                mDialog.cancel();

            }
        });
        mDialog.setContentView(dialogView);
        mDialog.show();
    }

    private void initImage(FragmentActivity activity, final View view, int drawableResId) {
        // 获取屏幕的高宽
        Point outSize = new Point();
        activity.getWindow().getWindowManager().getDefaultDisplay().getSize(outSize);
        // 解析将要被处理的图片
        Bitmap resourceBitmap = BitmapFactory.decodeResource(activity.getResources(), drawableResId);
        if (resourceBitmap == null) {
            return;
        }
        // 开始对图片进行拉伸或者缩放
        // 使用图片的缩放比例计算将要放大的图片的高度
        int bitmapScaledHeight = Math.round(resourceBitmap.getHeight() * outSize.x * 1.0f / resourceBitmap.getWidth());
        // 以屏幕的宽度为基准，如果图片的宽度比屏幕宽，则等比缩小，如果窄，则放大
        final Bitmap scaledBitmap = Bitmap.createScaledBitmap(resourceBitmap, outSize.x, bitmapScaledHeight, false);
        view.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                //这里防止图像的重复创建，避免申请不必要的内存空间
                if (scaledBitmap.isRecycled())
                    //必须返回true
                    return true;
                // 当UI绘制完毕，我们对图片进行处理
                int viewHeight = view.getMeasuredHeight();
                // 计算将要裁剪的图片的顶部以及底部的偏移量
                int offset = (scaledBitmap.getHeight() - viewHeight) / 2;
                // 对图片以中心进行裁剪，裁剪出的图片就是非常适合做引导页的图片了
                Bitmap finallyBitmap = Bitmap.createBitmap(scaledBitmap, 0, offset, scaledBitmap.getWidth(),
                        scaledBitmap.getHeight() - offset * 2);
                if (!finallyBitmap.equals(scaledBitmap)) {//如果返回的不是原图，则对原图进行回收
                    scaledBitmap.recycle();
                    System.gc();
                }
                // 设置图片显示
                view.setBackgroundDrawable(new BitmapDrawable(getContext().getResources(), finallyBitmap));
                return true;
            }
        });
    }

    private void initTimer() {
        mTimer = new Timer();
        final BaseTimerTask task = new BaseTimerTask(this);
        mTimer.schedule(task, 0, 1000);
    }

    @Override
    public void onTimer() {
        getProxyActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mTvLauncherTimer != null) {
                    mTvLauncherTimer.setText(MessageFormat.format("跳过\n{0}s", mCount));
                    mCount--;
                    if (mCount < 0) {
                        if (mTimer != null) {
                            mTimer.cancel();
                            mTimer = null;
                        }
                        checkIsShowScroll();
                    }
                }
            }
        });
    }
    //判断是否显示滑动启动页
    private void checkIsShowScroll() {
        boolean isLaunch = (boolean) SPUtil.get(ShareYSJ.getApplicationContext(), ScrollLauncherTag.HAS_FIRST_LAUNCHER_APP.name(), false);
        if (!isLaunch) {
            getSupportDelegate().start(new LauncherScrollDelegate(), SINGLETASK);
        } else {
            //检查用户是否登录了APP
            checkIsSign();
        }
    }

    private void checkIsSign() {
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

    @OnClick({R.id.tv_launcher_timer, R.id.btn_commit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_launcher_timer:
                if (mTimer != null) {
                    mTimer.cancel();
                    mTimer = null;
                }
                checkIsShowScroll();
                break;
            case R.id.btn_commit:
                if (mTimer != null) {
                    mTimer.cancel();
                    mTimer = null;
                }
                checkIsShowScroll();
                break;
        }
    }

}
