package com.gxysj.shareysj.bean;

import java.util.List;

/**
 * Created by 林佳荣 on 2018/5/26.
 * Github：https://github.com/ljrRookie
 * Function ：
 */

public class GetCityBean {

    /**
     * code : 0
     * data : [{"city":"深圳市","name":"测试AAAA","id":"14"},{"city":"广州","name":"南浦","id":"2"},{"city":"东莞","name":"东莞","id":"4"},{"city":"深圳","name":"深圳","id":"5"},{"city":"广州市","name":"南海怡丰城","id":"1"},{"city":"武汉","name":"武汉","id":"6"},{"city":"长沙","name":"长沙","id":"7"},{"city":"北京","name":"北京","id":"8"},{"city":"西安","name":"西安","id":"9"},{"city":"杭州","name":"杭州","id":"10"}]
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
         * city : 深圳市
         * name : 测试AAAA
         * id : 14
         */

        private String city;
        private String name;
        private String id;

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

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
    }
}
