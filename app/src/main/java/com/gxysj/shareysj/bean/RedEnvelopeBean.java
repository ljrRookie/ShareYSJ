package com.gxysj.shareysj.bean;

/**
 * Created by 林佳荣 on 2018/4/10.
 * Github：https://github.com/ljrRookie
 * Function ：
 */

public class RedEnvelopeBean {

    /**
     * code : 0
     * RedEnvelopesCount : 10
     * BonusMAX : 10
     * number : 0
     * CumulativeCount : 0.0
     */

    private String code;
    private int RedEnvelopesCount;
    private String BonusMAX;
    private int number;
    private String CumulativeCount;

    public String getCode() {
        return code == null ? "" : code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getRedEnvelopesCount() {
        return RedEnvelopesCount;
    }

    public void setRedEnvelopesCount(int redEnvelopesCount) {
        RedEnvelopesCount = redEnvelopesCount;
    }

    public String getBonusMAX() {
        return BonusMAX == null ? "" : BonusMAX;
    }

    public void setBonusMAX(String bonusMAX) {
        BonusMAX = bonusMAX;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getCumulativeCount() {
        return CumulativeCount == null ? "" : CumulativeCount;
    }

    public void setCumulativeCount(String cumulativeCount) {
        CumulativeCount = cumulativeCount;
    }
}
