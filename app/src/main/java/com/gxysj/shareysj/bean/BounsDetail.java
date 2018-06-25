package com.gxysj.shareysj.bean;

import java.util.List;

/**
 * Created by 林佳荣 on 2018/4/3.
 * Github：https://github.com/ljrRookie
 * Function ：
 */

public class BounsDetail {

    /**
     * code : 0
     * data : [{"id":"3","name":"购买商品返利0.01","money":0.01,"time":"1970-01-01 08:02:03"}]
     * money : 0.01
     */

    private String code;
    private String money;
    private List<DataBean> data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 3
         * name : 购买商品返利0.01
         * money : 0.01
         * time : 1970-01-01 08:02:03
         */

        private String id;
        private String name;
        private String money;
        private String time;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }
    }
}
