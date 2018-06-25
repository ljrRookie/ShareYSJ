package com.gxysj.shareysj.ui;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;

import me.dm7.barcodescanner.core.IViewFinder;
import me.dm7.barcodescanner.core.ViewFinderView;

/**
 * Created by Administrator on 2018/2/2.
 */

public class ShareYSJViewFinderView extends ViewFinderView {
    public ShareYSJViewFinderView(Context context) {
        this(context,null);
    }

    public ShareYSJViewFinderView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        mSquareViewFinder = true;
        mBorderPaint.setColor(Color.parseColor("#0096ff"));
        mLaserPaint.setColor(Color.parseColor("#0096ff"));
    }
}
