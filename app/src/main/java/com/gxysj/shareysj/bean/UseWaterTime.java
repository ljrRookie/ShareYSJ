package com.gxysj.shareysj.bean;

/**
 * Created by 林佳荣 on 2018/4/16.
 * Github：https://github.com/ljrRookie
 * Function ：
 */

public class UseWaterTime {
    private String name;
    private String type;
    private String state;

    public UseWaterTime(String name, String type, String state) {
        this.name = name;
        this.type = type;
        this.state = state;
    }

    public String getName() {
        return name == null ? "" : name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type == null ? "" : type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getState() {
        return state == null ? "" : state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
