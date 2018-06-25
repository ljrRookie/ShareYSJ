package com.gxysj.shareysj.bean;

import java.util.List;

/**
 * Created by 林佳荣 on 2018/4/2.
 * Github：https://github.com/ljrRookie
 * Function ：
 */

public class GetAdvertisementBean {

    /**
     * code : 0
     * data : [{"feedback":"","image":"file/82756_2018_03_31_03_27_44_64.png,file/997290_2018_03_31_03_27_44_486.png,file/286077_2018_03_31_03_27_44_408.png","id":"7","time":"2018-03-31","state":"1","title":"测试广告","money_state":"1","content":"测试内容阿萨德卡死理发师"}]
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
         * feedback :
         * image : file/82756_2018_03_31_03_27_44_64.png,file/997290_2018_03_31_03_27_44_486.png,file/286077_2018_03_31_03_27_44_408.png
         * id : 7
         * time : 2018-03-31
         * state : 1
         * title : 测试广告
         * money_state : 1
         * content : 测试内容阿萨德卡死理发师
         */

        private String feedback;
        private String image;
        private String id;
        private String time;
        private String state;
        private String title;
        private String money_state;
        private String content;

        public String getFeedback() {
            return feedback;
        }

        public void setFeedback(String feedback) {
            this.feedback = feedback;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
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

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getMoney_state() {
            return money_state;
        }

        public void setMoney_state(String money_state) {
            this.money_state = money_state;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
