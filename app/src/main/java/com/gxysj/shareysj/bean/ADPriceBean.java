package com.gxysj.shareysj.bean;

import java.util.List;

/**
 * Created by 林佳荣 on 2018/3/31.
 * Github：https://github.com/ljrRookie
 * Function ：
 */

public class ADPriceBean {

    /**
     * code : 0
     * data : [{"money":"2.0元","id":7,"day_time":"1小时"},{"money":"3.0元","id":8,"day_time":"2小时"},{"money":"5.0元","id":9,"day_time":"3小时"},{"money":"10.0元","id":6,"day_time":"6小时"},{"money":"20.0元","id":1,"day_time":"1天"},{"money":"100.0元","id":2,"day_time":"7天"},{"money":"500.0元","id":5,"day_time":"1月"},{"money":"1500.0元","id":3,"day_time":"6月"},{"money":"2000.0元","id":4,"day_time":"12月"}]
     */

    private String code;
    private List<DataBean> data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * money : 2.0元
         * id : 7
         * day_time : 1小时
         */

        private String money;
        private int id;
        private String day_time;

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getDay_time() {
            return day_time;
        }

        public void setDay_time(String day_time) {
            this.day_time = day_time;
        }
    }
}
