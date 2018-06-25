package com.gxysj.shareysj.utils.callback;

import android.support.annotation.Nullable;

/**
 * Created by Administrator on 2018/2/1.
 */

public interface IGlobalCallback<T> {
    void excuteCallback(@Nullable T args);
}
