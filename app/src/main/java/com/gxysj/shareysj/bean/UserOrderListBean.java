package com.gxysj.shareysj.bean;

import java.util.List;

/**
 * Created by 林佳荣 on 2018/3/30.
 * Github：https://github.com/ljrRookie
 * Function ：
 */

public class UserOrderListBean {

    /**
     * code : 0
     * data : [{"id":"6","time":"2018-03-30 15:28:42","count_money":"15.7","state":"2","list":[{"commodity_id":1,"commodity_name":"百岁山","commodity_image":"image/market_portrait.png","nubs":2,"money":5.8,"orderid":"10"},{"commodity_id":2,"commodity_name":"电池","commodity_image":"image/batterydoctor_icon.png","nubs":1,"money":9.9,"orderid":"11"}]},{"id":"7","time":"2018-03-30 15:44:45","count_money":"12.8","state":"2","list":[{"commodity_id":1,"commodity_name":"百岁山","commodity_image":"image/market_portrait.png","nubs":1,"money":2.9,"orderid":"12"},{"commodity_id":2,"commodity_name":"电池","commodity_image":"image/batterydoctor_icon.png","nubs":1,"money":9.9,"orderid":"13"}]},{"id":"8","time":"2018-03-30 15:46:35","count_money":"12.8","state":"2","list":[{"commodity_id":1,"commodity_name":"百岁山","commodity_image":"image/market_portrait.png","nubs":1,"money":2.9,"orderid":"14"},{"commodity_id":2,"commodity_name":"电池","commodity_image":"image/batterydoctor_icon.png","nubs":1,"money":9.9,"orderid":"15"}]},{"id":"9","time":"2018-03-30 15:46:47","count_money":"12.8","state":"2","list":[{"commodity_id":1,"commodity_name":"百岁山","commodity_image":"image/market_portrait.png","nubs":1,"money":2.9,"orderid":"16"},{"commodity_id":2,"commodity_name":"电池","commodity_image":"image/batterydoctor_icon.png","nubs":1,"money":9.9,"orderid":"17"}]},{"id":"10","time":"2018-03-30 15:47:25","count_money":"35.5","state":"2","list":[{"commodity_id":1,"commodity_name":"百岁山","commodity_image":"image/market_portrait.png","nubs":2,"money":5.8,"orderid":"18"},{"commodity_id":2,"commodity_name":"电池","commodity_image":"image/batterydoctor_icon.png","nubs":3,"money":29.7,"orderid":"19"}]}]
     * time : 1522396396232
     */

    private String code;
    private String time;
    private List<DataBean> data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 6
         * time : 2018-03-30 15:28:42
         * count_money : 15.7
         * state : 2
         * list : [{"commodity_id":1,"commodity_name":"百岁山","commodity_image":"image/market_portrait.png","nubs":2,"money":5.8,"orderid":"10"},{"commodity_id":2,"commodity_name":"电池","commodity_image":"image/batterydoctor_icon.png","nubs":1,"money":9.9,"orderid":"11"}]
         */

        private String id;
        private String time;
        private String count_money;
        private String state;
        private List<ListBean> list;

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

        public String getCount_money() {
            return count_money;
        }

        public void setCount_money(String count_money) {
            this.count_money = count_money;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            /**
             * commodity_id : 1
             * commodity_name : 百岁山
             * commodity_image : image/market_portrait.png
             * nubs : 2
             * money : 5.8
             * orderid : 10
             */

            private int commodity_id;
            private String commodity_name;
            private String commodity_image;
            private int nubs;
            private double money;
            private String orderid;

            public int getCommodity_id() {
                return commodity_id;
            }

            public void setCommodity_id(int commodity_id) {
                this.commodity_id = commodity_id;
            }

            public String getCommodity_name() {
                return commodity_name;
            }

            public void setCommodity_name(String commodity_name) {
                this.commodity_name = commodity_name;
            }

            public String getCommodity_image() {
                return commodity_image;
            }

            public void setCommodity_image(String commodity_image) {
                this.commodity_image = commodity_image;
            }

            public int getNubs() {
                return nubs;
            }

            public void setNubs(int nubs) {
                this.nubs = nubs;
            }

            public double getMoney() {
                return money;
            }

            public void setMoney(double money) {
                this.money = money;
            }

            public String getOrderid() {
                return orderid;
            }

            public void setOrderid(String orderid) {
                this.orderid = orderid;
            }
        }
    }
}
