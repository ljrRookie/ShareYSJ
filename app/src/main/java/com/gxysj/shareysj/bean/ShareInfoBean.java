package com.gxysj.shareysj.bean;

/**
 * Created by 林佳荣 on 2018/4/9.
 * Github：https://github.com/ljrRookie
 * Function ：
 */

public class ShareInfoBean {

    /**
     * code : 0
     * title : 饮水次数
     * content : 饮水次数1234内容
     * image :
     * time : 1970-01-03 19:13:33
     * Invitation_code : 10002
     */

    private String code;
    private String title;
    private String content;
    private String image;
    private String time;
    private String Invitation_code;
    private String url;

    public String getUrl() {
        return url == null ? "" : url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCode() {
        return code == null ? "" : code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public String getInvitation_code() {
        return Invitation_code == null ? "" : Invitation_code;
    }

    public void setInvitation_code(String invitation_code) {
        Invitation_code = invitation_code;
    }
}
