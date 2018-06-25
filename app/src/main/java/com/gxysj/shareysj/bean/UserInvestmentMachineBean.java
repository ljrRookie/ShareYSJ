package com.gxysj.shareysj.bean;

import java.util.List;

/**
 * Created by 林佳荣 on 2018/4/9.
 * Github：https://github.com/ljrRookie
 * Function ：
 */

public class UserInvestmentMachineBean {

    /**
     * code : 0
     * data : [{"money":33330,"city":"广州","name":"悦凯中心","x":"23.01469","y":"113.32284","id":"3","time":"1970-01-01 08:00:00"}]
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
         * money : 33330
         * city : 广州
         * name : 悦凯中心
         * x : 23.01469
         * y : 113.32284
         * id : 3
         * time : 1970-01-01 08:00:00
         */

        private String money;
        private String city;
        private String name;
        private String x;
        private String y;
        private String id;
        private String time;

        public String getMoney() {
            return money == null ? "" : money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getCity() {
            return city == null ? "" : city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getName() {
            return name == null ? "" : name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getX() {
            return x == null ? "" : x;
        }

        public void setX(String x) {
            this.x = x;
        }

        public String getY() {
            return y == null ? "" : y;
        }

        public void setY(String y) {
            this.y = y;
        }

        public String getId() {
            return id == null ? "" : id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTime() {
            return time == null ? "" : time;
        }

        public void setTime(String time) {
            this.time = time;
        }
    }
}
