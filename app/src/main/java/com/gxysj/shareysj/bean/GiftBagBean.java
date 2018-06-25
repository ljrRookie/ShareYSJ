package com.gxysj.shareysj.bean;

import java.util.List;

/**
 * Created by 林佳荣 on 2018/6/13.
 * Github：https://github.com/ljrRookie
 * Function ：
 */

public class GiftBagBean {

    /**
     * date : [{"name":"会员福利,抢红包  x1","id":"11","state":"0","time":"2018-06-13","type":"0"},{"name":"测试免费礼品,点点物礼包  x1","id":"10","state":"0","time":"2018-06-13","type":"1"},{"name":"免费礼品,点点物礼包  x1","id":"9","state":"0","time":"2018-06-13","type":"1"},{"name":"会员福利,充电  x5","id":"8","state":"0","time":"2018-06-13","type":"0"},{"name":"会员福利,饮水  x5","id":"7","state":"0","time":"2018-06-13","type":"0"},{"name":"下载app奖励,充电  x1","id":"6","state":"0","time":"2018-06-13","type":"0"},{"name":"下载app奖励,饮水  x1","id":"5","state":"0","time":"2018-06-13","type":"0"}]
     * code : 0
     */

    private String code;
    private List<DateBean> date;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<DateBean> getDate() {
        return date;
    }

    public void setDate(List<DateBean> date) {
        this.date = date;
    }

    public static class DateBean {
        /**
         * name : 会员福利,抢红包  x1
         * id : 11
         * state : 0
         * time : 2018-06-13
         * type : 0
         */

        private String name;
        private String id;
        private String state;
        private String time;
        private String type;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
