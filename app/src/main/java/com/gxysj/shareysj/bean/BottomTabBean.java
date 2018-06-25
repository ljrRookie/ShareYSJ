package com.gxysj.shareysj.bean;

/**
 * Created by 傅令杰
 */

public final class BottomTabBean {

    private final int ICON;
    private final CharSequence TITLE;

    public BottomTabBean(int icon, CharSequence title) {
        this.ICON = icon;
        this.TITLE = title;
    }

    public int getIcon() {
        return ICON;
    }

    public CharSequence getTitle() {
        return TITLE;
    }
}
