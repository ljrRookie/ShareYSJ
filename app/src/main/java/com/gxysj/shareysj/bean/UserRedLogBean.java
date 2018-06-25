package com.gxysj.shareysj.bean;

import java.util.List;

/**
 * Created by 林佳荣 on 2018/4/11.
 * Github：https://github.com/ljrRookie
 * Function ：
 */

public class UserRedLogBean {

    /**
     * code : 0
     * data : [{"id":"94","money":1.2171,"time":"2018-04-10 18:28:58"},{"id":"93","money":1.3175,"time":"2018-04-10 18:28:50"},{"id":"92","money":0.11,"time":"2018-04-10 18:28:24"},{"id":"91","money":0.7225,"time":"2018-04-10 18:28:16"},{"id":"90","money":0.8713,"time":"2018-04-10 18:28:13"},{"id":"89","money":8.5062,"time":"2018-04-10 18:27:39"},{"id":"88","money":2.0936,"time":"2018-04-10 18:27:35"},{"id":"87","money":3.081,"time":"2018-04-10 18:27:32"},{"id":"86","money":4.5519,"time":"2018-04-10 18:27:28"},{"id":"85","money":1.9743,"time":"2018-04-10 18:27:25"}]
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
         * id : 94
         * money : 1.2171
         * time : 2018-04-10 18:28:58
         */

        private String id;
        private String money;
        private String time;

        public String getId() {
            return id == null ? "" : id;
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

        public String getTime() {
            return time == null ? "" : time;
        }

        public void setTime(String time) {
            this.time = time;
        }
    }
}
