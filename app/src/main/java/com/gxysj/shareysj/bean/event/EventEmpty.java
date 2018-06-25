package com.gxysj.shareysj.bean.event;

import android.net.Uri;

/**
 * Created by 林佳荣 on 2018/3/21.
 * Github：https://github.com/ljrRookie
 * Function ：
 */

public class EventEmpty {
    private String Str;
    private Uri image;

    public EventEmpty(String str, Uri image) {
        Str = str;
        this.image = image;
    }

    public EventEmpty() {
    }

    public String getStr() {
        return Str;
    }

    public void setStr(String str) {
        Str = str;
    }

    public Uri getImage() {
        return image;
    }

    public void setImage(Uri image) {
        this.image = image;
    }
}
