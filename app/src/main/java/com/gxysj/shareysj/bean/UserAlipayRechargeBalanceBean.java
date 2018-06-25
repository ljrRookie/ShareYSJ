package com.gxysj.shareysj.bean;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by 林佳荣 on 2018/4/10.
 * Github：https://github.com/ljrRookie
 * Function ：
 */

public class UserAlipayRechargeBalanceBean {
    private String code;
    private String data;
    /**
     * appid : wx4e454178e430a37a
     * noncestr : bosdYrAg10kvD4zv
     * package : Sign=WXPay
     * partnerid : 1498734332
     * prepayid : wx05163013821086c7c9c3c26f1429277024
     * sign : EEA3CD5CD81219EB92568216E38431EE
     * timestamp : 1525509013
     */

    private String appid;
    private String noncestr;
    @JSONField(name = "package")
    private String packageX;
    private String partnerid;
    private String prepayid;
    private String sign;
    private String timestamp;

    public String getCode() {
        return code == null ? "" : code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getData() {
        return data == null ? "" : data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getAppid() {
        return appid == null ? "" : appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getNoncestr() {
        return noncestr == null ? "" : noncestr;
    }

    public void setNoncestr(String noncestr) {
        this.noncestr = noncestr;
    }

    public String getPackageX() {
        return packageX;
    }

    public void setPackageX(String packageX) {
        this.packageX = packageX;
    }

    public String getPartnerid() {
        return partnerid == null ? "" : partnerid;
    }

    public void setPartnerid(String partnerid) {
        this.partnerid = partnerid;
    }

    public String getPrepayid() {
        return prepayid == null ? "" : prepayid;
    }

    public void setPrepayid(String prepayid) {
        this.prepayid = prepayid;
    }

    public String getSign() {
        return sign == null ? "" : sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getTimestamp() {
        return timestamp == null ? "" : timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
