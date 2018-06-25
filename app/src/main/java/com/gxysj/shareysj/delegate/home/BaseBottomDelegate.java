package com.gxysj.shareysj.delegate.home;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.ContentFrameLayout;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.gxysj.shareysj.R;
import com.gxysj.shareysj.bean.BottomTabBean;
import com.gxysj.shareysj.delegate.SignInDelegate;
import com.gxysj.shareysj.delegate.base.YSJDelegate;
import com.gxysj.shareysj.listener.IUserChecker;
import com.gxysj.shareysj.utils.user_manager.AccountManager;
import com.joanzapata.iconify.widget.IconTextView;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.yokeyword.fragmentation.ISupportFragment;
import qiu.niorgai.StatusBarCompat;

/**
 * Created by Administrator on 2018/1/29.
 */

public abstract class BaseBottomDelegate extends YSJDelegate implements View.OnClickListener {
    private final ArrayList<BottomTabBean> TAB_BEANS = new ArrayList<>();
    private final ArrayList<BottomItemDelegate> ITEM_DELEGATES = new ArrayList<>();
    private final LinkedHashMap<BottomTabBean, BottomItemDelegate> ITEMS = new LinkedHashMap<>();
    @BindView(R.id.bottom_bar_delegate_container)
    ContentFrameLayout mBottomBarDelegateContainer;
    @BindView(R.id.bottom_bar)
    LinearLayoutCompat mBottomBar;

    private int mCurrentDelegate = 0;
    private int mIndexDelegate = 0;
    private int mClickedColor = Color.parseColor("#0096ff");
    private boolean isSign = false;

    public abstract LinkedHashMap<BottomTabBean, BottomItemDelegate> setItems(ItemBuilder builder);

    public abstract int setIndexDelegate();

    @ColorInt
    public abstract int setClickedColor();

    @Override
    public void onClick(View v) {
        final int tag = (int) v.getTag();
        resetColor();
        final RelativeLayout item = (RelativeLayout) v;
        final ImageView itemIcon = (ImageView) item.getChildAt(0);
        final AppCompatTextView itemTitle = (AppCompatTextView) item.getChildAt(1);

        if (tag == 0) {

            itemTitle.setTextColor(mClickedColor);
            itemIcon.setImageResource(R.mipmap.icon_home_press);
            getSupportDelegate().showHideFragment(ITEM_DELEGATES.get(tag), ITEM_DELEGATES.get(mCurrentDelegate));
        } else if (tag == 1) {

            if (!checkIsSign()) {
                getSupportDelegate().start(new SignInDelegate());
                final RelativeLayout itemLogin = (RelativeLayout) mBottomBar.getChildAt(0);
                final ImageView itemIconLogin = (ImageView) itemLogin.getChildAt(0);
                final AppCompatTextView itemTitleLogin = (AppCompatTextView) itemLogin.getChildAt(1);
                itemTitleLogin.setTextColor(mClickedColor);
                itemIconLogin.setImageResource(R.mipmap.icon_home_press);
            } else {

                itemTitle.setTextColor(mClickedColor);
                itemIcon.setImageResource(R.mipmap.icon_generalize_press);
                getSupportDelegate().showHideFragment(ITEM_DELEGATES.get(tag), ITEM_DELEGATES.get(mCurrentDelegate));
            }
        } else if (tag == 2) {
            if (!checkIsSign()) {
                getSupportDelegate().start(new SignInDelegate());
                final RelativeLayout itemLogin = (RelativeLayout) mBottomBar.getChildAt(0);
                final ImageView itemIconLogin = (ImageView) itemLogin.getChildAt(0);
                final AppCompatTextView itemTitleLogin = (AppCompatTextView) itemLogin.getChildAt(1);
                itemTitleLogin.setTextColor(mClickedColor);
                itemIconLogin.setImageResource(R.mipmap.icon_home_press);
            } else {

                itemTitle.setTextColor(mClickedColor);
                itemIcon.setImageResource(R.mipmap.icon_mine_press);
                getSupportDelegate().showHideFragment(ITEM_DELEGATES.get(tag), ITEM_DELEGATES.get(mCurrentDelegate));

            }

        }

        //注意先后顺序
        mCurrentDelegate = tag;
    }

    private boolean checkIsSign() {
        AccountManager.checkAccount(new IUserChecker() {
            @Override
            public void onSignIn() {
                isSign = true;
            }

            @Override
            public void onNotSignIn() {
                isSign = false;
                AccountManager.setSignState(false);
            }
        });
        return isSign;
    }

    private void resetColor() {
        final int count = mBottomBar.getChildCount();
        for (int i = 0; i < count; i++) {
            final RelativeLayout item = (RelativeLayout) mBottomBar.getChildAt(i);
            final ImageView itemIcon = (ImageView) item.getChildAt(0);
            final AppCompatTextView itemTitle = (AppCompatTextView) item.getChildAt(1);
            itemTitle.setTextColor(Color.GRAY);
            if (i == 0) {
                itemIcon.setImageResource(R.mipmap.icon_home);
            } else if (i == 1) {
                itemIcon.setImageResource(R.mipmap.icon_generalize);
            } else if (i == 2) {
                itemIcon.setImageResource(R.mipmap.icon_mine);
            }
        }
    }

    @Override
    public Object setLayout() {
        return R.layout.delegate_bottom;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, @NonNull View rootView) {
        final int size = ITEMS.size();
        for (int i = 0; i < size; i++) {
            LayoutInflater.from(getContext()).inflate(R.layout.bottom_item_icon_text_layout, mBottomBar);
            final RelativeLayout item = (RelativeLayout) mBottomBar.getChildAt(i);
            //设置每个item的点击事件
            item.setTag(i);
            item.setOnClickListener(this);
            final ImageView itemIcon = (ImageView) item.getChildAt(0);
            final AppCompatTextView itemTitle = (AppCompatTextView) item.getChildAt(1);
            BottomTabBean bottomTabBean = TAB_BEANS.get(i);
            itemIcon.setImageResource(bottomTabBean.getIcon());
            itemTitle.setText(bottomTabBean.getTitle());
            //默认首页
            if (i == mIndexDelegate) {
                itemIcon.setImageResource(R.mipmap.icon_home_press);
                itemTitle.setTextColor(mClickedColor);
            }
        }
        ISupportFragment[] delegateArray = ITEM_DELEGATES.toArray(new ISupportFragment[size]);
        getSupportDelegate().loadMultipleRootFragment(R.id.bottom_bar_delegate_container, mIndexDelegate, delegateArray);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mIndexDelegate = setIndexDelegate();
        if (setClickedColor() != 0) {
            mClickedColor = setClickedColor();
        }
        final ItemBuilder builder = ItemBuilder.builder();
        LinkedHashMap<BottomTabBean, BottomItemDelegate> items = setItems(builder);
        ITEMS.putAll(items);
        for (Map.Entry<BottomTabBean, BottomItemDelegate> item : ITEMS.entrySet()) {
            final BottomTabBean key = item.getKey();
            final BottomItemDelegate value = item.getValue();
            TAB_BEANS.add(key);
            ITEM_DELEGATES.add(value);
        }

    }
}
