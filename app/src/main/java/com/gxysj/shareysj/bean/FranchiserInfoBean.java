package com.gxysj.shareysj.bean;

/**
 * Created by 林佳荣 on 2018/4/8.
 * Github：https://github.com/ljrRookie
 * Function ：
 */

public class FranchiserInfoBean {

    /**
     * code : 0
     * Investment_money : 0
     * image : file/851081_2018_04_08_11_47_10_209.jpg
     * money : 0
     * machine : 0
     * name : ljr
     * id : 2
     * Cumulative_money : 0
     */

    private int code;
    private String Investment_money;
    private String image;
    private String money;
    private String machine;
    private String name;
    private int id;
    private String Cumulative_money;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getInvestment_money() {
        return Investment_money == null ? "" : Investment_money;
    }

    public void setInvestment_money(String investment_money) {
        Investment_money = investment_money;
    }

    public String getImage() {
        return image == null ? "" : image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getMoney() {
        return money == null ? "" : money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getMachine() {
        return machine == null ? "" : machine;
    }

    public void setMachine(String machine) {
        this.machine = machine;
    }

    public String getName() {
        return name == null ? "" : name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCumulative_money() {
        return Cumulative_money == null ? "" : Cumulative_money;
    }

    public void setCumulative_money(String cumulative_money) {
        Cumulative_money = cumulative_money;
    }
}
