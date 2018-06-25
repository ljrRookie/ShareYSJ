package com.gxysj.shareysj.bean;

import java.util.List;

/**
 * Created by 林佳荣 on 2018/4/4.
 * Github：https://github.com/ljrRookie
 * Function ：
 */

public class WaterMoneyListBean {

    /**
     * code : 0
     * data : [{"id":"1","image":"file/581981_2018_04_02_06_44_18_150.jpg","number":"1","money":"1.0","type":"0"},{"id":"2","image":"file/581981_2018_04_02_06_44_18_150.jpg","number":"5","money":"5.0","type":"0"},{"id":"3","image":"file/581981_2018_04_02_06_44_18_150.jpg","number":"10","money":"10.0","type":"0"},{"id":"4","image":"file/581981_2018_04_02_06_44_18_150.jpg","number":"20","money":"15.0","type":"0"},{"id":"5","image":"file/581981_2018_04_02_06_44_18_150.jpg","number":"50","money":"25.0","type":"0"}]
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
         * image : file/581981_2018_04_02_06_44_18_150.jpg
         * number : 1
         * money : 1.0
         * type : 0
         */

        private String id;
        private String image;
        private String number;
        private String money;
        private String type;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
