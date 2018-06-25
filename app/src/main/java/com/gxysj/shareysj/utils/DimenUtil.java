package com.gxysj.shareysj.utils;

import android.content.res.Resources;
import android.util.DisplayMetrics;


import com.gxysj.shareysj.config.ShareYSJ;


/**
 * Created by LinJiaRong on 2017/7/26.
 * TODO：获取设备宽高
 */

public class DimenUtil {
    public static int getScreenWidth() {
        final Resources resources = ShareYSJ.getApplicationContext().getResources();
        final DisplayMetrics dm = resources.getDisplayMetrics();
        return dm.widthPixels;
    }

    public static int getScreenHeight() {
        final Resources resources = ShareYSJ.getApplicationContext().getResources();
        final DisplayMetrics dm = resources.getDisplayMetrics();
        return dm.heightPixels;
    }
}
