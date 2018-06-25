package com.gxysj.shareysj.delegate.main.mine.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.gxysj.shareysj.delegate.base.YSJDelegate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 林佳荣 on 2018/3/5.
 * Github：https://github.com/ljrRookie
 * Function ：
 */

public class TabAdapter extends FragmentStatePagerAdapter{
    private final List<String> mTitles;
    private final List<YSJDelegate> mDelegate;

    public TabAdapter(FragmentManager fm, List<YSJDelegate> delegate, ArrayList<String> titles) {
        super(fm);
        mTitles = titles;
        mDelegate = delegate;
    }

    @Override
    public Fragment getItem(int position) {
        return mDelegate.get(position);
    }

    @Override
    public int getCount() {
        return mTitles.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.get(position);
    }
}
