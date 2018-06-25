package com.gxysj.shareysj.bean;

import java.util.List;

/**
 * Created by 林佳荣 on 2018/6/13.
 * Github：https://github.com/ljrRookie
 * Function ：
 */

public class UserGiftBean {

    /**
     * date : [{"name":"会员福利  x1","id":"3","time":"2018-06-13"}]
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
         * name : 会员福利  x1
         * id : 3
         * time : 2018-06-13
         */

        private String name;
        private String id;
        private String time;

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

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }
    }
}
