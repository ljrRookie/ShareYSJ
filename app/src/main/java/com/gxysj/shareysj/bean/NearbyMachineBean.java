package com.gxysj.shareysj.bean;

import java.util.List;

/**
 * Created by 林佳荣 on 2018/4/4.
 * Github：https://github.com/ljrRookie
 * Function ：
 */

public class NearbyMachineBean {

    /**
     * code : 0
     * data : [{"id":"1","name":"悦凯中心","number":"10001","qrcode":"1","introduce":"","coordinate_x":"23.020384","coordinate_y":"113.329321","image":""},{"id":"2","name":"南浦","number":"10002","qrcode":"1","introduce":"","coordinate_x":"23.04023","coordinate_y":"113.299873","image":""},{"id":"3","name":"西朗","number":"10003","qrcode":"1","introduce":"","coordinate_x":"23.081562","coordinate_y":"113.229553","image":""},{"id":"4","name":"东莞","number":"10004","qrcode":"1","introduce":"","coordinate_x":"23.037419","coordinate_y":"113.762013","image":""},{"id":"5","name":"深圳","number":"10005","qrcode":"1","introduce":"","coordinate_x":"22.559939","coordinate_y":"114.065127","image":""},{"id":"6","name":"武汉","number":"10006","qrcode":"1","introduce":"","coordinate_x":"30.593244","coordinate_y":"114.278237","image":""},{"id":"7","name":"长沙","number":"10007","qrcode":"1","introduce":"","coordinate_x":"28.23769","coordinate_y":"112.937424","image":""},{"id":"10","name":"杭州","number":"10010","qrcode":"1","introduce":"","coordinate_x":"30.25483","coordinate_y":"120.208038","image":""}]
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
         * number : 10001
         * qrcode : 1
         * introduce :
         * coordinate_x : 23.020384
         * coordinate_y : 113.329321
         * image :
         */

        private String id;
        private String name;
        private String number;
        private String qrcode;
        private String introduce;
        private String coordinate_x;
        private String coordinate_y;
        private String image;

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

        public String getNumber() {
            return number == null ? "" : number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public String getQrcode() {
            return qrcode == null ? "" : qrcode;
        }

        public void setQrcode(String qrcode) {
            this.qrcode = qrcode;
        }

        public String getIntroduce() {
            return introduce == null ? "" : introduce;
        }

        public void setIntroduce(String introduce) {
            this.introduce = introduce;
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

        public String getImage() {
            return image == null ? "" : image;
        }

        public void setImage(String image) {
            this.image = image;
        }
    }
}
