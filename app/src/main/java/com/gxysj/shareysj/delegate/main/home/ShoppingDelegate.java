package com.gxysj.shareysj.delegate.main.home;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AccelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.allen.library.SuperButton;
import com.allen.library.SuperTextView;
import com.gxysj.shareysj.R;
import com.gxysj.shareysj.bean.MachineGoodBean;
import com.gxysj.shareysj.bean.ShopOrderBean;
import com.gxysj.shareysj.bean.event.EventEmpty;
import com.gxysj.shareysj.config.Constant;
import com.gxysj.shareysj.config.ShareYSJ;
import com.gxysj.shareysj.config.loading.YSJLoading;
import com.gxysj.shareysj.delegate.PayDelegate;
import com.gxysj.shareysj.delegate.ShopInfoDelegate;
import com.gxysj.shareysj.delegate.base.YSJDelegate;
import com.gxysj.shareysj.delegate.main.home.adapter.ShopAdapter;
import com.gxysj.shareysj.delegate.main.home.adapter.ShopCartAdapter;
import com.gxysj.shareysj.listener.ShopCartInterface;
import com.gxysj.shareysj.listener.ShopCartItemInterface;
import com.gxysj.shareysj.network.RequestCenter;
import com.gxysj.shareysj.ui.RxFakeAddImageView;
import com.gxysj.shareysj.ui.RxPointFTypeEvaluator;
import com.gxysj.shareysj.utils.CartStorage;
import com.gxysj.shareysj.utils.SPUtil;
import com.gxysj.shareysj.utils.TimeUtils;
import com.gxysj.shareysj.utils.camear.RequestCodes;
import com.gxysj.shareysj.utils.user_manager.Utils;
import com.gxysj.shareysj.zxing.app.CaptureActivity;
import com.gxysj.shareysj.zxing.util.Util;
import com.ljr.delegate_sdk.okhttp.exception.OkHttpException;
import com.ljr.delegate_sdk.okhttp.listener.DisposeDataListener;
import com.ljr.delegate_sdk.okhttp.request.RequestParams;
import com.vondear.rxtools.RxDataTool;
import com.vondear.rxtools.RxLogTool;
import com.vondear.rxtools.RxTimeTool;
import com.vondear.rxtools.view.RxToast;
import com.vondear.rxtools.view.dialog.RxDialog;
import com.zyao89.view.zloading.ZLoadingDialog;
import com.zyao89.view.zloading.Z_TYPE;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import me.yokeyword.fragmentation.ISupportFragment;

/**
 * Created by Administrator on 2018/2/2.
 */

public class ShoppingDelegate extends YSJDelegate implements ShopCartInterface, ShopCartItemInterface {


    @BindView(R.id.btn_back)
    ImageView mBtnBack;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.btn_right)
    ImageView mBtnRight;
    @BindView(R.id.shopping_cart_total_tv)
    TextView mShoppingCartTotalTv;
    @BindView(R.id.btn_pay)
    SuperButton mBtnPay;
    @BindView(R.id.shopping_cart_bottom)
    RelativeLayout mShoppingCartBottom;
    @BindView(R.id.rv_goods)
    RecyclerView mRvGoods;
    @BindView(R.id.shopping_cart_layout)
    FrameLayout mShoppingCartLayout;
    @BindView(R.id.shopping_cart_total_num)
    TextView mShoppingCartTotalNum;
    @BindView(R.id.main_layout)
    RelativeLayout mMainLayout;

    @BindView(R.id.tv_empty)
    TextView mTvEmpty;

    private String mMachineId = "";
    private RxDialog mDialog;
    private List<MachineGoodBean.DataBean> mMachineGoods = null;
    private CartStorage mCartStorage;
    private List<MachineGoodBean.DataBean> mCartData = null;
    private ShopAdapter mShopAdapter;
    private TextView mTvTotal;
    private TextView mTv_count;


    @Override
    public Object setLayout() {
        return R.layout.delegate_home_shop;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        EventBus.getDefault().register(this);
        mCartStorage = CartStorage.getInstance();
        mDialog = new RxDialog(getActivity());
        mTvTitle.setText("点点物");
        mTvEmpty.setVisibility(View.GONE);
        mRvGoods.setVisibility(View.VISIBLE);
        initData();


    }


    private void initData() {
        //RequestParams requestParams = Utils.getRequestParams();
        RequestParams requestParams = new RequestParams();
        requestParams.put("machineid", mMachineId);
        RequestCenter.GetMachineCommodityList(requestParams, new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                MachineGoodBean data = (MachineGoodBean) responseObj;
                mMachineGoods = data.getData();
                if (mMachineGoods != null && mMachineGoods.size() > 0) {
                    for (int i = 0; i < mMachineGoods.size(); i++) {
                        mMachineGoods.get(i).setCount(0);
                    }
                    mShopAdapter = new ShopAdapter(mMachineGoods, R.layout.item_shop_right);
                    mShopAdapter.openLoadAnimation();
                    mShopAdapter.setShopCartInterface(ShoppingDelegate.this);
                    mRvGoods.setLayoutManager(new LinearLayoutManager(getActivity()));
                    mRvGoods.setAdapter(mShopAdapter);
                } else {
                    mTvEmpty.setVisibility(View.VISIBLE);
                    mTvEmpty.setText("此售货机暂无商品");
                    mRvGoods.setVisibility(View.GONE);
                }
              /*  dialog.cancel();*/
            }

            @Override
            public void onFailure(Object responseObj) {
                OkHttpException error = (OkHttpException) responseObj;
                mTvEmpty.setVisibility(View.VISIBLE);
                mTvEmpty.setText("获取本机商品失败，请稍后重试.\n(" + error.getEmsg() + ")");
                mRvGoods.setVisibility(View.GONE);
              /*  dialog.cancel();*/
            }
        });

    }


    @OnClick({R.id.btn_back, R.id.btn_pay, R.id.shopping_cart_layout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                getSupportDelegate().pop();
                break;
            case R.id.btn_pay:
                mCartData = mCartStorage.cartSparseToList();
                if (mCartData != null && mCartData.size() > 0) {
                    createOrder(mCartData);
                } else {
                    RxToast.normal("还没有选择商品！");
                }
                break;
            case R.id.shopping_cart_layout:
                mCartData = mCartStorage.cartSparseToList();
                if (mCartData != null && mCartData.size() > 0) {
                    showCart(mCartData);
                } else {
                    RxToast.normal("购物车还没有商品！");
                }

                break;
        }
    }

    private void createOrder(List<MachineGoodBean.DataBean> cartData) {
        RequestParams requestParams = Utils.getRequestParams();
        requestParams.put("machineid", mMachineId);
        String orderid = "";
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < cartData.size(); i++) {
            MachineGoodBean.DataBean dataBean = cartData.get(i);
            String id = dataBean.getId();
            int count = dataBean.getCount();
            stringBuffer.append(id + "_" + count + ",");
        }
        orderid = stringBuffer.toString().substring(0, stringBuffer.toString().length() - 1);
        requestParams.put("orderid", orderid);
        RxLogTool.e("orderid", "orderid:==== " + orderid);
        RequestCenter.SubmitCommodityOrder(requestParams, new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                ShopOrderBean shopOrder = (ShopOrderBean) responseObj;
                getSupportDelegate().start(ShopInfoDelegate.create(shopOrder));
            }

            @Override
            public void onFailure(Object responseObj) {
                OkHttpException error = (OkHttpException) responseObj;
                RxToast.normal("结算失败 - （" + error.getEmsg() + ")");

            }
        });

    }

    private void showCart(List<MachineGoodBean.DataBean> cartData) {
        // mDialog.getLayoutParams().gravity = Gravity.BOTTOM;
        mDialog.setFullScreenWidth();
        Window window = mDialog.getWindow();
        window.setWindowAnimations(R.style.anim_panel_up_from_bottom_cart);
        View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_cart, null);
        SuperTextView btnDel = (SuperTextView) dialogView.findViewById(R.id.btn_del);
        RecyclerView rvCart = (RecyclerView) dialogView.findViewById(R.id.rv_cart);
        mTvTotal = (TextView) dialogView.findViewById(R.id.shopping_cart_total_tv);
        SuperButton btnPay = (SuperButton) dialogView.findViewById(R.id.btn_pay);
        FrameLayout ivCart = (FrameLayout) dialogView.findViewById(R.id.shopping_cart_layout);
        mTv_count = (TextView) dialogView.findViewById(R.id.shopping_cart_total_num);
        int totalCount = 0;
        double totalPrice = 0.00;
        int size = cartData.size();
        for (int i = 0; i < size; i++) {
            MachineGoodBean.DataBean dataBean = cartData.get(i);
            int count = Integer.valueOf(dataBean.getCount());
            double price = Double.parseDouble(dataBean.getPrice1());
            double itemPrice = price * count;
            totalPrice = itemPrice + totalPrice;
            totalCount = count + totalCount;
        }
        mTvTotal.setText("¥" + RxDataTool.getAmountValue(totalPrice));
        mTv_count.setVisibility(View.VISIBLE);
        mTv_count.setText(String.valueOf(totalCount));
        ShopCartAdapter shopCartAdapter = new ShopCartAdapter(cartData, R.layout.item_shop_cart);
        shopCartAdapter.setShopCartItemInterface(this);
        shopCartAdapter.openLoadAnimation();
        rvCart.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvCart.setAdapter(shopCartAdapter);
        ivCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
        btnDel.setRightTvClickListener(new SuperTextView.OnRightTvClickListener() {
            @Override
            public void onClickListener() {
                RxToast.normal("清空购物车");
                mCartStorage.clearCartData();
                mTvTotal.setText("¥" + 0.00);
                mTv_count.setVisibility(View.GONE);
                showTotalPrice();
                EventBus.getDefault().post(new EventEmpty("clear", null));
                mDialog.dismiss();
            }
        });
        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCartData = mCartStorage.cartSparseToList();
                createOrder(mCartData);
            }
        });
        mDialog.setCanceledOnTouchOutside(true);
        mDialog.setCancelable(true);
        mDialog.setContentView(dialogView);
        mDialog.show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void EventBus(EventEmpty eventEmpty) {
        String str = eventEmpty.getStr();
        if (str.equals("clear")) {
            initData();
        } else {
            mRvGoods.setAdapter(mShopAdapter);
        }

    }

    @Override
    public void add(View view, int postion) {
        //点击添加的动画
        addAnimator(view);
        if (mMachineGoods != null && mMachineGoods.size() > 0) {
            mCartStorage.addData(mMachineGoods.get(postion));
            showTotalPrice();
        }
    }

    private void addAnimator(View view) {
        int[] addLocation = new int[2];
        int[] cartLocation = new int[2];
        int[] recycleLocation = new int[2];
        view.getLocationInWindow(addLocation);
        mShoppingCartLayout.getLocationInWindow(cartLocation);
        mRvGoods.getLocationInWindow(recycleLocation);

        PointF startP = new PointF();
        PointF endP = new PointF();
        PointF controlP = new PointF();

        startP.x = addLocation[0];
        startP.y = addLocation[1] - recycleLocation[1];
        endP.x = cartLocation[0];
        endP.y = cartLocation[1] - recycleLocation[1];
        controlP.x = endP.x;
        controlP.y = startP.y;
        final RxFakeAddImageView rxFakeAddImageView = new RxFakeAddImageView(getActivity());
        mMainLayout.addView(rxFakeAddImageView);
        rxFakeAddImageView.setImageResource(R.drawable.ic_add_circle_blue_700_36dp);
        rxFakeAddImageView.getLayoutParams().width = getResources().getDimensionPixelSize(R.dimen.item_dish_circle_size);
        rxFakeAddImageView.getLayoutParams().height = getResources().getDimensionPixelSize(R.dimen.item_dish_circle_size);
        rxFakeAddImageView.setVisibility(View.VISIBLE);
        ObjectAnimator addAnimator = ObjectAnimator.ofObject(rxFakeAddImageView, "mPointF",
                new RxPointFTypeEvaluator(controlP), startP, endP);
        addAnimator.setInterpolator(new AccelerateInterpolator());
        addAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                rxFakeAddImageView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                rxFakeAddImageView.setVisibility(View.GONE);
                mMainLayout.removeView(rxFakeAddImageView);
            }

            @Override
            public void onAnimationCancel(Animator animator) {
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });
        ObjectAnimator scaleAnimatorX = new ObjectAnimator().ofFloat(mShoppingCartLayout, "scaleX", 0.6f, 1.0f);
        ObjectAnimator scaleAnimatorY = new ObjectAnimator().ofFloat(mShoppingCartLayout, "scaleY", 0.6f, 1.0f);
        scaleAnimatorX.setInterpolator(new AccelerateInterpolator());
        scaleAnimatorY.setInterpolator(new AccelerateInterpolator());
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(scaleAnimatorX).with(scaleAnimatorY).after(addAnimator);
        animatorSet.setDuration(300);
        animatorSet.start();
    }

    @Override
    public void remove(View view, int postion) {
        if (mMachineGoods != null && mMachineGoods.size() > 0) {
            mCartStorage.deleteData(mMachineGoods.get(postion));
            showTotalPrice();
        }
    }

    private void showTotalPrice() {
        mCartData = mCartStorage.cartSparseToList();
        if (mCartData != null && mCartData.size() > 0) {
            int totalCount = 0;
            double totalPrice = 0.00;
            int size = mCartData.size();
            for (int i = 0; i < size; i++) {
                MachineGoodBean.DataBean dataBean = mCartData.get(i);
                int count = Integer.valueOf(dataBean.getCount());
                double price = Double.parseDouble(dataBean.getPrice1());
                double itemPrice = price * count;
                totalPrice = itemPrice + totalPrice;
                totalCount = count + totalCount;
            }

            mShoppingCartTotalTv.setText("¥" + RxDataTool.getAmountValue(totalPrice));
            mShoppingCartTotalNum.setVisibility(View.VISIBLE);
            mShoppingCartTotalNum.setText(String.valueOf(totalCount));
        } else {
            mShoppingCartTotalTv.setText("¥" + 0.00);
            mShoppingCartTotalNum.setVisibility(View.GONE);

        }


    }

    @Override
    public void ItemAdd(View view, int postion, MachineGoodBean.DataBean data) {
        if (mMachineGoods != null && mMachineGoods.size() > 0) {
            mCartStorage.addData(data);
            mCartData = mCartStorage.cartSparseToList();
            if (mCartData != null && mCartData.size() > 0) {
                int totalCount = 0;
                double totalPrice = 0.00;
                int size = mCartData.size();
                for (int i = 0; i < size; i++) {
                    MachineGoodBean.DataBean dataBean = mCartData.get(i);
                    int count = Integer.valueOf(dataBean.getCount());
                    double price = Double.parseDouble(dataBean.getPrice1());
                    double itemPrice = price * count;
                    totalPrice = itemPrice + totalPrice;
                    totalCount = count + totalCount;
                }
                mTvTotal.setText("¥" + RxDataTool.getAmountValue(totalPrice));
                mTv_count.setVisibility(View.VISIBLE);
                mTv_count.setText(String.valueOf(totalCount));
                mShoppingCartTotalTv.setText("¥" + RxDataTool.getAmountValue(totalPrice));
                mShoppingCartTotalNum.setVisibility(View.VISIBLE);
                mShoppingCartTotalNum.setText(String.valueOf(totalCount));
            } else {
                mShoppingCartTotalTv.setText("¥" + 0.00);
                mShoppingCartTotalNum.setVisibility(View.GONE);
                mTvTotal.setText("¥" + 0.00);
                mTv_count.setVisibility(View.GONE);
            }
            EventBus.getDefault().post(new EventEmpty("count", null));
        }

    }

    @Override
    public void ItemRemove(View view, int postion, MachineGoodBean.DataBean data) {
        if (mMachineGoods != null && mMachineGoods.size() > 0) {
            mCartStorage.deleteData(data);
            mCartData = mCartStorage.cartSparseToList();
            if (mCartData != null && mCartData.size() > 0) {
                int totalCount = 0;
                double totalPrice = 0.00;
                int size = mCartData.size();
                for (int i = 0; i < size; i++) {
                    MachineGoodBean.DataBean dataBean = mCartData.get(i);
                    int count = Integer.valueOf(dataBean.getCount());
                    double price = Double.parseDouble(dataBean.getPrice1());
                    double itemPrice = price * count;
                    totalPrice = itemPrice + totalPrice;
                    totalCount = count + totalCount;
                }
                mTvTotal.setText("¥" + RxDataTool.getAmountValue(totalPrice));
                mTv_count.setVisibility(View.VISIBLE);
                mTv_count.setText(String.valueOf(totalCount));
                mShoppingCartTotalTv.setText("¥" + RxDataTool.getAmountValue(totalPrice));
                mShoppingCartTotalNum.setVisibility(View.VISIBLE);
                mShoppingCartTotalNum.setText(String.valueOf(totalCount));
            } else {
                mShoppingCartTotalTv.setText("¥" + 0.00);
                mShoppingCartTotalNum.setVisibility(View.GONE);
                mTvTotal.setText("¥" + 0.00);
                mTv_count.setVisibility(View.GONE);
            }
            EventBus.getDefault().post(new EventEmpty("count", null));

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        mCartStorage.clearCartData();
    }


    public static ShoppingDelegate create(String code) {
        ShoppingDelegate shoppingDelegate = new ShoppingDelegate();
        Bundle bundle = new Bundle();
        bundle.putString("mMachineId",code);
        shoppingDelegate.setArguments(bundle);

        return shoppingDelegate;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        mMachineId = arguments.getString("mMachineId", "");
    }
}
