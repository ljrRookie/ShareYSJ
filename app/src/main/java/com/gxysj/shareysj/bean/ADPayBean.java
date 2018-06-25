package com.gxysj.shareysj.bean;

/**
 * Created by 林佳荣 on 2018/4/3.
 * Github：https://github.com/ljrRookie
 * Function ：
 */

public class ADPayBean {
    private double money;
    private String orderid;
    private int typeId;

    public ADPayBean(double money, String orderid, int typeId) {
        this.money = money;
        this.orderid = orderid;
        this.typeId = typeId;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }
}
