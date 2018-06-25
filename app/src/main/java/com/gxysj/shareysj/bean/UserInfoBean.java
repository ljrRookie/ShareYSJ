package com.gxysj.shareysj.bean;

import java.io.Serializable;

/**
 * Created by 林佳荣 on 2018/3/28.
 * Github：https://github.com/ljrRookie
 * Function ：
 */

public class UserInfoBean implements Serializable {


    /**
     * image : file/382577_2018_04_04_02_21_56_216.jpg
     * code : 0
     * gender : 0
     * introduce : android
     * water : 12
     * password_state : 1
     * ids_state : 1
     * ids_image1 : file/138975_2018_03_30_05_56_19_966.jpg
     * givewater : 2
     * ids_image2 : file/996092_2018_03_30_05_56_18_588.jpg
     * full_name : 林佳荣
     * money : 8221.8
     * phone : 15622732935
     * name : ljr
     * ids : 44058219970000000
     * time : 2018-03-27
     * freeWater : 1
     * email : 15622732935@163.com
     * Chargenub : 100
     * Invitation_Water : 1
     * vip_money : 10.0
     * ids_state : 1
     * freeWater_state : true
     * vip : 0
     */
//头像
    private String image;
    //响应码
    private String code;
    //性别：0 男；1 女；2 其他
    private int gender;
    //用户介绍
    private String introduce;
    //饮水次数
    private String water;
    //支付密码是否设置： 0 未设置 ； 1 已设置
    private String password_state;
    //个人认证： 0 审核中 ； 1 通过 ； 2 未通过 ； 3 未提交
    private int ids_state;
    //身份证正面
    private String ids_image1;
   //赠送饮水次数
    private String givewater;
    //身份证反面
    private String ids_image2;
    //真实姓名
    private String full_name;
    //余额
    private String money;
    //手机号码
    private String phone;
    //用户名称
    private String name;
    //身份证号码
    private String ids;
    //注册时间
    private String time;
    //首次免费赠送饮水次数
    private String freeWater;
    //邮箱
    private String email;
    //充电次数
    private int Chargenub;
    //是否领取首次登录赠送饮水次数
    private boolean freeWater_state;
    //未读消息条数
    private int count;
    //邀请用户获得的免费次数
    private String Invitation_Water;
    //会员充值金额
    private String vip_money;
    //是否开通vip ： 0 未开通 ； 1 开通
    private int vip;

    public int getChargenub() {
        return Chargenub;
    }

    public void setChargenub(int chargenub) {
        Chargenub = chargenub;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isFreeWater_state() {
        return freeWater_state;
    }

    public void setFreeWater_state(boolean freeWater_state) {
        this.freeWater_state = freeWater_state;
    }

    public String getImage() {
        return image == null ? "" : image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCode() {
        return code == null ? "" : code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getIntroduce() {
        return introduce == null ? "" : introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getWater() {
        return water == null ? "" : water;
    }

    public void setWater(String water) {
        this.water = water;
    }

    public String getPassword_state() {
        return password_state == null ? "" : password_state;
    }

    public void setPassword_state(String password_state) {
        this.password_state = password_state;
    }

    public int getIds_state() {
        return ids_state;
    }

    public void setIds_state(int ids_state) {
        this.ids_state = ids_state;
    }

    public String getIds_image1() {
        return ids_image1 == null ? "" : ids_image1;
    }

    public void setIds_image1(String ids_image1) {
        this.ids_image1 = ids_image1;
    }

    public String getGivewater() {
        return givewater == null ? "" : givewater;
    }

    public void setGivewater(String givewater) {
        this.givewater = givewater;
    }

    public String getIds_image2() {
        return ids_image2 == null ? "" : ids_image2;
    }

    public void setIds_image2(String ids_image2) {
        this.ids_image2 = ids_image2;
    }

    public String getFull_name() {
        return full_name == null ? "" : full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getMoney() {
        return money == null ? "" : money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getPhone() {
        return phone == null ? "" : phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name == null ? "" : name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIds() {
        return ids == null ? "" : ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public String getTime() {
        return time == null ? "" : time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getFreeWater() {
        return freeWater == null ? "" : freeWater;
    }

    public void setFreeWater(String freeWater) {
        this.freeWater = freeWater;
    }

    public String getEmail() {
        return email == null ? "" : email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getInvitation_Water() {
        return Invitation_Water;
    }

    public void setInvitation_Water(String Invitation_Water) {
        this.Invitation_Water = Invitation_Water;
    }

    public String getVip_money() {
        return vip_money;
    }

    public void setVip_money(String vip_money) {
        this.vip_money = vip_money;
    }

    public int getVip() {
        return vip;
    }

    public void setVip(int vip) {
        this.vip = vip;
    }
}
