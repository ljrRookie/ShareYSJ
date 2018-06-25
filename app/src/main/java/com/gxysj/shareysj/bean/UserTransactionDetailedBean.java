package com.gxysj.shareysj.bean;

import org.jbox2d.collision.TimeOfImpact;

import java.util.List;

/**
 * Created by 林佳荣 on 2018/3/31.
 * Github：https://github.com/ljrRookie
 * Function ：
 */

public class UserTransactionDetailedBean {

    /**
     * code : 0
     * data : [{"id":10,"name":"购买百岁山 ( 已完成)","money":"5.8","type":"1","time":"2018-03-30"},{"id":11,"name":"购买电池 ( 已完成)","money":"9.9","type":"1","time":"2018-03-30"},{"id":12,"name":"购买百岁山 ( 已完成)","money":"2.9","type":"1","time":"2018-03-30"},{"id":13,"name":"购买电池 ( 已完成)","money":"9.9","type":"1","time":"2018-03-30"},{"id":14,"name":"购买百岁山 ( 已完成)","money":"2.9","type":"1","time":"2018-03-30"},{"id":15,"name":"购买电池 ( 已完成)","money":"9.9","type":"1","time":"2018-03-30"},{"id":16,"name":"购买百岁山 ( 已完成)","money":"2.9","type":"1","time":"2018-03-30"},{"id":17,"name":"购买电池 ( 已完成)","money":"9.9","type":"1","time":"2018-03-30"},{"id":18,"name":"购买百岁山 ( 已完成)","money":"5.8","type":"1","time":"2018-03-30"},{"id":19,"name":"购买电池 ( 已完成)","money":"29.7","type":"1","time":"2018-03-30"},{"id":29,"name":"购买果粒橙 ( 已取消)","money":"3.6","type":"1","time":"2018-03-31"},{"id":30,"name":"购买辣子鸡 ( 已取消)","money":"3.1","type":"1","time":"2018-03-31"},{"id":31,"name":"购买果粒橙 ( 已取消)","money":"3.6","type":"1","time":"2018-03-31"},{"id":32,"name":"购买辣子鸡 ( 已取消)","money":"3.1","type":"1","time":"2018-03-31"},{"id":33,"name":"购买果粒橙 ( 已取消)","money":"3.6","type":"1","time":"2018-03-31"}]
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
         * id : 10
         * name : 购买百岁山 ( 已完成)
         * money : 5.8
         * type : 1
         * time : 2018-03-30
         */

        private int id;
        private String name;
        private String money;
        private int type;
        private String time;

        @Override
        public String toString() {
            return "DataBean{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", money='" + money + '\'' +
                    ", type='" + type + '\'' +
                    ", time='" + time + '\'' +
                    '}';
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
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

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }
    }
}
