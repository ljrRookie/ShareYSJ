package com.gxysj.shareysj.delegate.main.home;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.allen.library.SuperButton;
import com.allen.library.SuperTextView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gxysj.shareysj.R;
import com.gxysj.shareysj.bean.ADPriceBean;
import com.gxysj.shareysj.bean.ChargingInfo;
import com.gxysj.shareysj.bean.GetAppHomeInfoBean;
import com.gxysj.shareysj.bean.RedEnvelopeBean;
import com.gxysj.shareysj.bean.RedEnvelopesTypeBean;
import com.gxysj.shareysj.bean.UserRedEnvelopeBean;
import com.gxysj.shareysj.config.ShareYSJ;
import com.gxysj.shareysj.delegate.PayDelegate;
import com.gxysj.shareysj.delegate.base.YSJDelegate;
import com.gxysj.shareysj.delegate.main.home.ad.AdForPayDelegate;
import com.gxysj.shareysj.delegate.main.home.adapter.AdPriceAdapter;
import com.gxysj.shareysj.delegate.main.home.adapter.RedPriceAdapter;
import com.gxysj.shareysj.network.RequestCenter;
import com.gxysj.shareysj.utils.SPUtil;
import com.gxysj.shareysj.utils.TimeUtils;
import com.gxysj.shareysj.utils.user_manager.Utils;
import com.ljr.delegate_sdk.okhttp.exception.OkHttpException;
import com.ljr.delegate_sdk.okhttp.listener.DisposeDataListener;
import com.ljr.delegate_sdk.okhttp.request.RequestParams;
import com.mobike.library.MobikeView;
import com.vondear.rxtools.RxTimeTool;
import com.vondear.rxtools.view.RxToast;
import com.vondear.rxtools.view.dialog.RxDialog;
import com.zyao89.view.zloading.ZLoadingDialog;
import com.zyao89.view.zloading.Z_TYPE;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.R.attr.data;

/**
 * Created by 林佳荣 on 2018/3/1.
 * Github：https://github.com/ljrRookie
 * Function ：
 */

public class RedBagGameDelegate extends YSJDelegate {
    @BindView(R.id.btn_back)
    ImageView mBtnBack;
    @BindView(R.id.btn_buy)
    SuperButton mBtnBuy;
    @BindView(R.id.btn_start)
    ImageView mBtnStart;
    @BindView(R.id.mobike_view)
    MobikeView mMobikeView;
    Unbinder unbinder;
    @BindView(R.id.tv_cumulative_count)
    TextView mTvCumulativeCount;
    @BindView(R.id.tv_number)
    TextView mTvNumber;
    @BindView(R.id.tv_bouns_max)
    TextView mTvBounsMax;
    Unbinder unbinder1;
    @BindView(R.id.btn_info)
    ImageView mBtnInfo;
    Unbinder unbinder2;
    private RxDialog mDialog;
    private String mTitle;
    private int mRedEnvelopesCount = 10;
    private AlertDialog DIALOG;

    @Override
    public Object setLayout() {
        return R.layout.delegate_home_redbag_game;
    }

    public static RedBagGameDelegate create(String name) {
        RedBagGameDelegate redBagGameDelegate = new RedBagGameDelegate();
        Bundle bundle = new Bundle();
        bundle.putString("title", name);
        redBagGameDelegate.setArguments(bundle);
        return redBagGameDelegate;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        mTitle = arguments.getString("title", "");
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        mDialog = new RxDialog(getActivity());
        DIALOG = new AlertDialog.Builder(getActivity()).create();
        initData();
        initViews();

    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    private void initData() {
        RequestParams requestParams = Utils.getRequestParams();
        RequestCenter.GetRedEnvelopeList(requestParams, new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                RedEnvelopeBean redEnvelopeBean = (RedEnvelopeBean) responseObj;
                mTvNumber.setText(redEnvelopeBean.getNumber() + "");
                mTvCumulativeCount.setText(redEnvelopeBean.getCumulativeCount());
                mTvBounsMax.setText("红包池:" + redEnvelopeBean.getBonusMAX() + "元");
                mRedEnvelopesCount = redEnvelopeBean.getRedEnvelopesCount();

            }

            @Override
            public void onFailure(Object responseObj) {
                OkHttpException error = (OkHttpException) responseObj;
                RxToast.normal("获取个人信息失败 - (" + error.getEmsg() + ")");

            }
        });
    }



    private void initViews() {
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;
        for (int i = 0; i < mRedEnvelopesCount; i++) {
            ImageView imageView = new ImageView(getActivity());
            ViewGroup.LayoutParams layoutParams1 = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            imageView.setLayoutParams(layoutParams1);
            imageView.setImageResource(R.drawable.icon_red_bag);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setTag(R.id.mobike_view_id_tag, i + 1);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final ZLoadingDialog dialog = new ZLoadingDialog(ShareYSJ.getActivity());
                    dialog.setLoadingBuilder(Z_TYPE.SNAKE_CIRCLE)//设置类型
                            .setLoadingColor(Color.GRAY)//颜色
                            .setHintText("正在开奖....")
                            .setHintTextSize(14) // 设置字体大小 dp
                            .setHintTextColor(Color.BLACK)  // 设置字体颜色
                            .setDurationTime(0.5) // 设置动画时间百分比 - 0.5倍
                            .setDialogBackgroundColor(Color.TRANSPARENT) // 设置背景色，默认白色
                            .show();
                    int id = (int) v.getTag(R.id.mobike_view_id_tag);
                   // mMobikeView.getmMobike().onStop();

                    RequestParams requestParams = Utils.getRequestParams();
                    RequestCenter.UserRedEnvelope(requestParams, new DisposeDataListener() {
                        @Override
                        public void onSuccess(Object responseObj) {
                            UserRedEnvelopeBean userRedEnvelopeBean = (UserRedEnvelopeBean) responseObj;
                           /* String s = mTvNumber.getText().toString();
                            int num = Integer.valueOf(s);
                            mTvNumber.setText(num - 1 + "");*/
                            dialog.cancel();
                            showDialogForResult(userRedEnvelopeBean.getMoney());
                        }

                        @Override
                        public void onFailure(Object responseObj) {
                            OkHttpException errror = (OkHttpException) responseObj;
                            dialog.cancel();
                            RxToast.normal("开奖失败 - (" + errror.getEmsg() + ")");
                        }
                    });


                }
            });
            if (i == mRedEnvelopesCount - 1) {
                imageView.setTag(R.id.mobike_view_circle_tag, false);
            } else {
                imageView.setTag(R.id.mobike_view_circle_tag, true);
            }
            mMobikeView.addView(imageView, layoutParams);
        }

        mMobikeView.getmMobike().setDensity(15);
        mMobikeView.getmMobike().setFriction(0);
        mMobikeView.getmMobike().setRestitution(1f);
        mMobikeView.getmMobike().setRatio(100);

    }

    private int time = 0;
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            mMobikeView.getmMobike().random();
            if (time < 1) {
                handler.postDelayed(this, 500);
                time++;
            }

        }
    };


    @OnClick({R.id.btn_back, R.id.btn_buy, R.id.btn_start, R.id.btn_info})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                getSupportDelegate().pop();
                break;
            case R.id.btn_info:
                getSupportDelegate().start(new RedRecordDelegate());
                break;
            case R.id.btn_buy:
                initPriceData();

                break;
            case R.id.btn_start:
                mMobikeView.setVisibility(View.VISIBLE);
                mMobikeView.getmMobike().onStart();
                handler.postDelayed(runnable, 500);

                break;
        }
    }

    private void initPriceData() {
        RequestCenter.GetRedEnvelopesTypes(new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                RedEnvelopesTypeBean redEnvelopesTypeBean = (RedEnvelopesTypeBean) responseObj;
                final List<RedEnvelopesTypeBean.DataBean> data = redEnvelopesTypeBean.getData();
                RedPriceAdapter redPriceAdapter = new RedPriceAdapter(R.layout.item_home_red_price, data);

                redPriceAdapter.openLoadAnimation();
                DIALOG.show();
                final Window window = DIALOG.getWindow();
                if (window != null) {
                    window.setContentView(R.layout.dialog_red_panel);
                    window.setGravity(Gravity.BOTTOM);
                    window.setWindowAnimations(R.style.anim_panel_up_from_bottom);
                    window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    //设置属性
                    final WindowManager.LayoutParams params = window.getAttributes();
                    params.width = WindowManager.LayoutParams.MATCH_PARENT;
                    params.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
                    params.dimAmount = 0.5f;
                    window.setAttributes(params);
                    window.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DIALOG.cancel();
                        }
                    });
                    final RecyclerView rvRed = (RecyclerView) window.findViewById(R.id.rv_red);
                    rvRed.setLayoutManager(new GridLayoutManager(getActivity(),3));
                    rvRed.setAdapter(redPriceAdapter);
                }
                redPriceAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                        RedEnvelopesTypeBean.DataBean dataBean = data.get(position);
                        double money = Double.valueOf(dataBean.getMoney());
                        String id = dataBean.getId();
                        DIALOG.cancel();
                        getSupportDelegate().start(PayDelegate.create(money, id, 1003));
                    }
                });
            }

            @Override
            public void onFailure(Object responseObj) {
                OkHttpException error = (OkHttpException) responseObj;
                RxToast.normal("获取套餐价格失败-(" + error.getEmsg() + ")");
            }
        });
    }

    private void showDialogForResult(String money) {
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.getLayoutParams().gravity = Gravity.CENTER;
        View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_game_result, null);
        TextView tvResult = (TextView) dialogView.findViewById(R.id.tv_result);

        if (money.equals("0")) {
            tvResult.setText("没有中奖");
        } else {
            tvResult.setText("恭喜你\n获得" + money + "元红包");
        }
        initData();
        SuperButton commit = (SuperButton) dialogView.findViewById(R.id.btn_commit);
        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // mMobikeView.setVisibility(View.GONE);
                mDialog.cancel();
            }
        });
        mDialog.setContentView(dialogView);
        mDialog.show();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
        time = 0;
    }
}
