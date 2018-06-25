package com.gxysj.shareysj.bean;

import java.util.List;

/**
 * Created by 林佳荣 on 2018/4/2.
 * Github：https://github.com/ljrRookie
 * Function ：
 */

public class GetAppHomeInfoBean {

    /**
     * code : 0
     * data : [{"id":"1","image":"file/859525_2018_04_02_04_40_51_161.png"},{"id":"2","image":"file/859525_2018_04_02_04_40_51_161.png"},{"id":"3","image":"file/859525_2018_04_02_04_40_51_161.png"},{"id":"4","image":"file/859525_2018_04_02_04_40_51_161.png"}]
     * data1 : [{"id":"1","image":"file/859525_2018_04_02_04_40_51_161.png","number":"1","money":"1.0","type":"0"},{"id":"2","image":"file/859525_2018_04_02_04_40_51_161.png","number":"5","money":"5.0","type":"0"},{"id":"3","image":"file/859525_2018_04_02_04_40_51_161.png","number":"10","money":"10.0","type":"0"}]
     * data2 : [{"id":"1","image":"file/859525_2018_04_02_04_40_51_161.png","name":"清清泉直饮水"},{"id":"2","image":"file/859525_2018_04_02_04_40_51_161.png","name":"点点物"},{"id":"3","image":"file/859525_2018_04_02_04_40_51_161.png","name":"我点广告"},{"id":"4","image":"file/859525_2018_04_02_04_40_51_161.png","name":"方便熟水"},{"id":"5","image":"file/859525_2018_04_02_04_40_51_161.png","name":"品牌水"},{"id":"6","image":"file/859525_2018_04_02_04_40_51_161.png","name":"实景导航"},{"id":"7","image":"file/859525_2018_04_02_04_40_51_161.png","name":"抢红包"},{"id":"8","image":"file/859525_2018_04_02_04_40_51_161.png","name":"送礼包"},{"id":"9","image":"file/859525_2018_04_02_04_40_51_161.png","name":"速充宝"}]
     */

    private String code;
    private List<DataBean> data;
    private List<Data1Bean> data1;
    private List<Data2Bean> data2;

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

    public List<Data1Bean> getData1() {
        return data1;
    }

    public void setData1(List<Data1Bean> data1) {
        this.data1 = data1;
    }

    public List<Data2Bean> getData2() {
        return data2;
    }

    public void setData2(List<Data2Bean> data2) {
        this.data2 = data2;
    }

    public static class DataBean {
        /**
         * id : 1
         * image : file/859525_2018_04_02_04_40_51_161.png
         */

        private String id;
        private String image;

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
    }

    public static class Data1Bean {
        /**
         * id : 1
         * image : file/859525_2018_04_02_04_40_51_161.png
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

    public static class Data2Bean {
        /**
         * id : 1
         * image : file/859525_2018_04_02_04_40_51_161.png
         * name : 清清泉直饮水
         */

        private String id;
        private String image;
        private String name;

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

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
