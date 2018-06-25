package com.gxysj.shareysj.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 林佳荣 on 2018/6/1.
 * Github：https://github.com/ljrRookie
 * Function ：
 */

public class MachineInfoBean implements Serializable{

    /**
     * code : -1
     * data : [{"name":"果粒橙","image":"assets/image/a1.jpg","nub":2,"id":41,"money":"0.02"}]
     * money : 0.04
     * orderid : zss201806010228446998521
     * time : 2018-06-01 14:28:44
     */

    private int code;
    private double money;
    private String orderid;
    private String time;
    private List<DataBean> data;
    /**
     * uuid : zss201806010241191530115
     */

    private String uuid;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public static class DataBean implements Serializable{
        /**
         * name : 果粒橙
         * image : assets/image/a1.jpg
         * nub : 2
         * id : 41
         * money : 0.02
         */

        private String name;
        private String image;
        private int nub;
        private int id;
        private String money;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImage() {
            return image;
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

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }
    }
}
