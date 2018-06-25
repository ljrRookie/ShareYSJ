package com.gxysj.shareysj.bean;

import java.util.List;

/**
 * Created by 林佳荣 on 2018/4/9.
 * Github：https://github.com/ljrRookie
 * Function ：
 */

public class ChargingInfo {

    /**
     * code : 0
     * data : [{"id":"1","name":"充电期间不能退出此页面."},{"id":"2","name":"请勿离开手机，以免手机被盗。"},{"id":"3","name":"充电一次 扣除一次次数。"},{"id":"4","name":"购买商品、充值、发布广告都可以获取充电次数。"}]
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
         * name : 充电期间不能退出此页面.
         */

        private String id;
        private String name;

        public String getId() {
            return id == null ? "" : id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name == null ? "" : name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
