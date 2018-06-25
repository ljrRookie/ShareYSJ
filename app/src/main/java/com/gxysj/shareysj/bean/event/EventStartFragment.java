package com.gxysj.shareysj.bean.event;

/**
 * Created by 林佳荣 on 2018/3/6.
 * Github：https://github.com/ljrRookie
 * Function ：
 */

public class EventStartFragment {
    private String message;

    public EventStartFragment(String s) {
        this.message = s;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
