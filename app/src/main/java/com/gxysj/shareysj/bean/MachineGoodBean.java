package com.gxysj.shareysj.bean;

import java.util.List;

/**
 * Created by 林佳荣 on 2018/3/29.
 * Github：https://github.com/ljrRookie
 * Function ：
 */

public class MachineGoodBean {

    /**
     * code : 0
     * data : [{"id":"1","name":"百岁山","price":"3.5","price1":"2.9","image":"image/market_portrait.png","stock":"9","sales_volume":"0","number":"1"},{"id":"2","name":"电池","price":"10.0","price1":"9.9","image":"image/batterydoctor_icon.png","stock":"8","sales_volume":"0","number":"1"}]
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
         * name : 百岁山
         * price : 3.5
         * price1 : 2.9
         * image : image/market_portrait.png
         * stock : 9
         * sales_volume : 0
         * number : 1
         */

        private String id;
        private String name;
        private String price;
        private String price1;
        private String image;
        private String stock;
        private String sales_volume;
        private String number;
        private int count = 0;
        private String uuid;

        public String getUuid() {
            return uuid == null ? "" : uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getPrice1() {
            return price1;
        }

        public void setPrice1(String price1) {
            this.price1 = price1;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getStock() {
            return stock;
        }

        public void setStock(String stock) {
            this.stock = stock;
        }

        public String getSales_volume() {
            return sales_volume;
        }

        public void setSales_volume(String sales_volume) {
            this.sales_volume = sales_volume;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }
    }
}
