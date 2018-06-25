package com.gxysj.shareysj.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 林佳荣 on 2018/3/29.
 * Github：https://github.com/ljrRookie
 * Function ：
 */

public class UserAdBean implements Serializable{


    /**
     * code : 0
     * data : [{"id":"16","title":"哈哈红红火火恍恍惚惚","content":"哈哈红红火火恍恍惚惚\n哥红红火火恍恍惚惚","image":"file/543530_2018_04_02_05_24_08_483.jpg","time":"2018-04-02","state":"0","feedback":"","money_state":"0","uuid":"zss201804020524081227365"},{"id":"17","title":"哈哈红红火火恍恍惚惚","content":"哈哈红红火火恍恍惚惚\n哥红红火火恍恍惚惚","image":"file/384011_2018_04_02_05_25_46_22.jpg","time":"2018-04-02","state":"0","feedback":"","money_state":"0","uuid":"zss201804020525475666347"}]
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

    public static class DataBean implements Serializable {
        /**
         * id : 16
         * title : 哈哈红红火火恍恍惚惚
         * content : 哈哈红红火火恍恍惚惚
         哥红红火火恍恍惚惚
         * image : file/543530_2018_04_02_05_24_08_483.jpg
         * time : 2018-04-02
         * state : 0
         * feedback :
         * money_state : 0
         * uuid : zss201804020524081227365
         */

        private String id;
        private String title;
        private String content;
        private String image;
        private String time;
        private String state;
        private String feedback;
        private String money_state;
        private String uuid;
        private double money;
        private String DayTime;

        public String getDayTime() {
            return DayTime == null ? "" : DayTime;
        }

        public void setDayTime(String dayTime) {
            DayTime = dayTime;
        }

        public double getMoney() {
            return money;
        }

        public void setMoney(double money) {
            this.money = money;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getFeedback() {
            return feedback;
        }

        public void setFeedback(String feedback) {
            this.feedback = feedback;
        }

        public String getMoney_state() {
            return money_state;
        }

        public void setMoney_state(String money_state) {
            this.money_state = money_state;
        }

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }
    }
}
