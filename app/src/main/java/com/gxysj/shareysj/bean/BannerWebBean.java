package com.gxysj.shareysj.bean;

/**
 * Created by 林佳荣 on 2018/4/12.
 * Github：https://github.com/ljrRookie
 * Function ：
 */

public class BannerWebBean {

    /**
     * code : 0
     * id : 1
     * title : 广告1
     * content : 123123213
     * image : image/appwidget_preview.png
     * time : 1970-01-01 08:00:00
     * url : 12
     */

    private String code;
    private String id;
    private String title;
    private String content;
    private String image;
    private String time;
    private String url;

    public String getCode() {
        return code == null ? "" : code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getId() {
        return id == null ? "" : id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title == null ? "" : title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content == null ? "" : content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImage() {
        return image == null ? "" : image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTime() {
        return time == null ? "" : time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUrl() {
        return url == null ? "" : url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
