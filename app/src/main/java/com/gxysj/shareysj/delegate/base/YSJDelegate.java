package com.gxysj.shareysj.delegate.base;

/**
 * Created by Administrator on 2018/1/29.
 */

public abstract class YSJDelegate extends PermissionCheckerDelegate {
    @SuppressWarnings("unchecked")
    public <T extends YSJDelegate> T getParentDelegate() {
        return (T) getParentFragment();
    }
}
