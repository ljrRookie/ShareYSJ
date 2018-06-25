package com.gxysj.shareysj.bean;

import java.util.List;

/**
 * Created by 林佳荣 on 2018/4/16.
 * Github：https://github.com/ljrRookie
 * Function ：
 */

public class InvestMachineBean {

    /**
     * code : 0
     * data : [{"id":"1","name":"悦凯中心","coordinate_x":23.01469,"coordinate_y":113.32284,"money":0,"none":50,"city":"广州","image":"","count":1,"time":"1970-01-01 08:00:00"},{"id":"2","name":"南浦","coordinate_x":23.04023,"coordinate_y":113.299873,"money":20000,"none":10,"city":"广州","image":"","count":2,"time":"1970-01-01 08:00:00"},{"id":"3","name":"西朗","coordinate_x":23.081562,"coordinate_y":113.229553,"money":69999.97,"none":10,"city":"广州","image":"","count":1,"time":"1970-01-01 08:00:00"},{"id":"4","name":"东莞","coordinate_x":23.037419,"coordinate_y":113.762013,"money":87000.02,"none":90,"city":"东莞","image":"","count":2,"time":"1970-01-01 08:00:00"},{"id":"5","name":"深圳","coordinate_x":22.559939,"coordinate_y":114.065127,"money":90000,"none":10,"city":"深圳","image":"","count":1,"time":"1970-01-01 08:00:00"},{"id":"6","name":"武汉","coordinate_x":30.593244,"coordinate_y":114.278237,"money":90000,"none":20,"city":"武汉","image":"","count":1,"time":"1970-01-01 08:00:00"},{"id":"11","name":"有成美食","coordinate_x":66,"coordinate_y":113.3385229111,"money":98800.01,"none":70,"city":"广州","image":"","count":1,"time":"1970-01-01 08:00:00"},{"id":"12","name":"花好悦圆酒家","coordinate_x":23.01525,"coordinate_y":113.32262,"money":99999.9,"none":80,"city":"广州","image":"","count":1,"time":"1970-01-01 08:00:00"},{"id":"7","name":"长沙","coordinate_x":28.23769,"coordinate_y":112.937424,"money":90000,"none":30,"city":"长沙","image":"","count":1,"time":"1970-01-01 08:00:00"},{"id":"8","name":"北京","coordinate_x":39.915574,"coordinate_y":116.40476,"money":90000,"none":40,"city":"北京","image":"","count":1,"time":"1970-01-01 08:00:00"},{"id":"9","name":"西安","coordinate_x":34.347507,"coordinate_y":108.944741,"money":90000,"none":50,"city":"西安","image":"","count":1,"time":"1970-01-01 08:00:00"},{"id":"10","name":"杭州","coordinate_x":30.25483,"coordinate_y":120.208038,"money":90000,"none":60,"city":"杭州","image":"","count":1,"time":"1970-01-01 08:00:00"}]
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
         * name : 悦凯中心
         * coordinate_x : 23.01469
         * coordinate_y : 113.32284
         * money : 0
         * none : 50
         * city : 广州
         * image :
         * count : 1
         * time : 1970-01-01 08:00:00
         */

        private String id;
        private String name;
        private String coordinate_x;
        private String coordinate_y;
        private String money;
        private String none;
        private String city;
        private String image;
        private String count;
        private String time;

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

        public String getCoordinate_x() {
            return coordinate_x == null ? "" : coordinate_x;
        }

        public void setCoordinate_x(String coordinate_x) {
            this.coordinate_x = coordinate_x;
        }

        public String getCoordinate_y() {
            return coordinate_y == null ? "" : coordinate_y;
        }

        public void setCoordinate_y(String coordinate_y) {
            this.coordinate_y = coordinate_y;
        }

        public String getMoney() {
            return money == null ? "" : money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getNone() {
            return none == null ? "" : none;
        }

        public void setNone(String none) {
            this.none = none;
        }

        public String getCity() {
            return city == null ? "" : city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getImage() {
            return image == null ? "" : image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getCount() {
            return count == null ? "" : count;
        }

        public void setCount(String count) {
            this.count = count;
        }

        public String getTime() {
            return time == null ? "" : time;
        }

        public void setTime(String time) {
            this.time = time;
        }
    }
}
