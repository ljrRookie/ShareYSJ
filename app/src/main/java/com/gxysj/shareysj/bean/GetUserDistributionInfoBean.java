package com.gxysj.shareysj.bean;

/**
 * Created by 林佳荣 on 2018/3/31.
 * Github：https://github.com/ljrRookie
 * Function ：
 */

public class GetUserDistributionInfoBean {

    /**
     * image : file/55382_2018_03_31_10_20_23_929.jpg
     * code : 0
     * commissionCount : 0
     * name : ljr
     * InvitationCode : 10002
     * Offline : 0
     * commission : 0.0
     * time : 2018-03-27
     * Franchiser : 0
     */

    private String image;
    private String code;
    private String commissionCount;
    private String name;
    private String InvitationCode;
    private int Offline;
    private String commission;
    private String time;
    private String Franchiser;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCommissionCount() {
        return commissionCount;
    }

    public void setCommissionCount(String commissionCount) {
        this.commissionCount = commissionCount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInvitationCode() {
        return InvitationCode;
    }

    public void setInvitationCode(String InvitationCode) {
        this.InvitationCode = InvitationCode;
    }

    public int getOffline() {
        return Offline;
    }

    public void setOffline(int Offline) {
        this.Offline = Offline;
    }

    public String getCommission() {
        return commission;
    }

    public void setCommission(String commission) {
        this.commission = commission;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getFranchiser() {
        return Franchiser;
    }

    public void setFranchiser(String Franchiser) {
        this.Franchiser = Franchiser;
    }
}
