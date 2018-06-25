package com.gxysj.shareysj.bean;

import java.util.List;

/**
 * Created by 林佳荣 on 2018/4/11.
 * Github：https://github.com/ljrRookie
 * Function ：
 */

public class RedEnvelopesTypeBean {

    /**
     * code : 0
     * data : [{"id":1,"money":0.01,"nub":1},{"id":2,"money":0.02,"nub":10},{"id":3,"money":0.03,"nub":20},{"id":4,"money":0.04,"nub":50}]
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
         * id : 1
         * money : 0.01
         * nub : 1
         */

        private String id;
        private String money;
        private int nub;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getMoney() {
            return money == null ? "" : money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public int getNub() {
            return nub;
        }

        public void setNub(int nub) {
            this.nub = nub;
        }
    }
}
