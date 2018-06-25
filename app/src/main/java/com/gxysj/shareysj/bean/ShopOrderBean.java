package com.gxysj.shareysj.bean;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 林佳荣 on 2018/3/30.
 * Github：https://github.com/ljrRookie
 * Function ：
 */

public class ShopOrderBean implements Serializable{


    /**
     * code : 0
     * orderid : zss201804080517385812349
     * data : [{"id":1,"name":"百岁山","image":"image/market_portrait.png","nub":1,"money":"2.9"},{"id":2,"name":"电池","image":"image/batterydoctor_icon.png","nub":1,"money":"9.9"},{"id":3,"name":"果粒橙","image":"image/a1.jpg","nub":1,"money":"3.6"}]
     * money : 16.4
     * time : 1523179058515
     */

    private String code;
    private String orderid;
    private double money;
    private String time;
    private List<DataBean> data;
    private String uuid;
    /**
     * money : 100000
     * city : 广州市
     * name : 南浦
     * x : 23.04023
     * y : 113.299873
     * id : 2
     * state : 0
     */

    @JSONField(name = "money")
    private int moneyX;
    private String city;
    private String name;
    private String x;
    private String y;
    private String id;
    private String state;

    public String getUuid() {
        return uuid == null ? "" : uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getCode() {
        return code == null ? "" : code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getOrderid() {
        return orderid == null ? "" : orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public String getTime() {
        return time == null ? "" : time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<DataBean> getData() {
        if (data == null) {
            return new ArrayList<>();
        }
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public int getMoneyX() {
        return moneyX;
    }

    public void setMoneyX(int moneyX) {
        this.moneyX = moneyX;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public static class DataBean implements Serializable{
        /**
         * id : 1
         * name : 百岁山
         * image : image/market_portrait.png
         * nub : 1
         * money : 2.9
         */

        private int id;
        private String name;
        private String image;
        private int nub;
        private String money;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name == null ? "" : name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImage() {
            return image == null ? "" : image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public int getNub() {
            return nub;
        }

        public void setNub(int nub) {
            this.nub = nub;
        }

        public String getMoney() {
            return money == null ? "" : money;
        }

        public void setMoney(String money) {
            this.money = money;
        }
    }
}
