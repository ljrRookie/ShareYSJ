package com.gxysj.shareysj.bean;

/**
 * Created by 林佳荣 on 2018/4/3.
 * Github：https://github.com/ljrRookie
 * Function ：
 */

public class GetAdvertisementInfoBean {

    /**
     * code : 0
     * id : 23
     * title : ljr的广告
     * content : 看看吧发个回个话很反感干哈哈哈哈哈刚回家你不会
     * image : file/578789_2018_04_03_10_01_47_955.jpg,file/566424_2018_04_03_10_01_46_408.jpg,file/116323_2018_04_03_10_01_47_720.jpg
     * state : 0
     * state_time : 未开始
     * end_time : 未开始
     */

    private String code;
    private String id;
    private String title;
    private String content;
    private String image;
    private String state;
    private String state_time;
    private String end_time;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getState_time() {
        return state_time;
    }

    public void setState_time(String state_time) {
        this.state_time = state_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }
}
