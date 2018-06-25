package com.gxysj.shareysj.bean;

import java.util.List;

/**
 * Created by 林佳荣 on 2018/4/11.
 * Github：https://github.com/ljrRookie
 * Function ：
 */

public class UserNewsLogBean {

    /**
     * code : 0
     * data : [{"id":"101","mess":"【饮水充值】您充值的饮水次数已经到账，请注意查看。","image":"","time":"2018-04-11 14:21:58"},{"id":"100","mess":"【抢红包】您充值的抢红包次数已经到账。","image":"","time":"2018-04-11 14:19:16"},{"id":"99","mess":"【抢红包】您充值的抢红包次数已经到账。","image":"","time":"2018-04-11 14:11:47"},{"id":"81","mess":"【商品购买】您购买商品获赠免费饮水1次。","image":"","time":"2018-04-10 18:31:42"},{"id":"61","mess":"【饮水充值】您充值的饮水次数已经到账，请注意查看。","image":"","time":"2018-04-09 18:19:32"},{"id":"60","mess":"【饮水充值】您充值的饮水次数已经到账，请注意查看。","image":"","time":"2018-04-09 18:18:07"},{"id":"59","mess":"【饮水充值】您充值的饮水次数已经到账，请注意查看。","image":"","time":"2018-04-09 18:16:20"},{"id":"58","mess":"【饮水充值】您充值的饮水次数已经到账，请注意查看。","image":"","time":"2018-04-09 17:24:16"},{"id":"57","mess":"【饮水充值】您充值的饮水次数已经到账，请注意查看。","image":"","time":"2018-04-09 17:23:49"},{"id":"56","mess":"【饮水充值】您充值的饮水次数已经到账，请注意查看。","image":"","time":"2018-04-09 17:23:27"}]
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
         * id : 101
         * mess : 【饮水充值】您充值的饮水次数已经到账，请注意查看。
         * image :
         * time : 2018-04-11 14:21:58
         */

        private String id;
        private String mess;
        private String image;
        private String time;

        public String getId() {
            return id == null ? "" : id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getMess() {
            return mess == null ? "" : mess;
        }

        public void setMess(String mess) {
            this.mess = mess;
        }

        public String getImage() {
            return image == null ? "" : image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getTime() {
            return time == null ? "" : time;
        }

        public void setTime(String time) {
            this.time = time;
        }
    }
}
