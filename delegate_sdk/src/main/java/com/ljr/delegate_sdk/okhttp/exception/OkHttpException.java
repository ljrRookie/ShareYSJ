package com.ljr.delegate_sdk.okhttp.exception;

import android.accounts.AccountManager;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Created by 林佳荣 on 2018/2/8.
 * Github：https://github.com/ljrRookie
 * Function ：自定义异常类，返回code，emsg到业务层
 */

public class OkHttpException extends Exception{
    private static final long serialVersionUID = 1L;
    private int code;
    private Object emsg=null;
    private WeakHashMap<Integer, String> mErrorMsg = null;

    public OkHttpException(int code, Object emsg) {
        this.emsg = emsg;
        this.code = code;
        Log.e("okhttp", "code = "+ code +"msg = "+emsg);
        mErrorMsg = new WeakHashMap<>();
        initErrorMsg(mErrorMsg);
    }

    private void initErrorMsg(WeakHashMap<Integer, String> errorMsg) {
        errorMsg.put(8888,"公钥失效");errorMsg.put(9999,"请求参数错误");
        errorMsg.put(7777,"服务器异常");errorMsg.put(6666,"请求方式错误");
        errorMsg.put(5555,"权限不足");
        errorMsg.put(10000,"token失效");errorMsg.put(10001,"账号不能为空");
        errorMsg.put(10002,"密码不能为空");errorMsg.put(10003,"type不能为空");
        errorMsg.put(10004,"帐号密码错误");errorMsg.put(10005,"帐号不存在");
        errorMsg.put(10006,"帐号冻结中");errorMsg.put(10007, "帐号异常需要验证短信登录");
        errorMsg.put(10008,"验证码不能为空");errorMsg.put(10009,"登录失败");
        errorMsg.put(10010,"验证码错误");errorMsg.put(10011,"验证码超时");
        errorMsg.put(10012,"邀请码不存在");errorMsg.put(10013,"密码不能设置太简单");
        errorMsg.put(10014,"密码最少6位");errorMsg.put(10015,"帐号已存在");
        errorMsg.put(10016,"注册帐号失败");errorMsg.put(10017,"帐号或验证码错误");
        errorMsg.put(10018,"手机号码频率限制");errorMsg.put(10019,"发送验证码失败");
        errorMsg.put(10020,"新密码不能为空");errorMsg.put(10021,"找回密码失败");
        errorMsg.put(10022,"用户id不能为空");errorMsg.put(10023,"token不能为空");
        errorMsg.put(10016,"注册帐号失败");errorMsg.put(10016,"注册帐号失败");
        errorMsg.put(10024,"用户不存在");errorMsg.put(10025,"修改资料失败");
        errorMsg.put(10026,"原支付密码不能为空");errorMsg.put(10027,"新支付密码不能为空");
        errorMsg.put(10028,"原支付密码错误");errorMsg.put(10029,"支付密码修改失败");
        errorMsg.put(10030,"登录密码修改失败");errorMsg.put(10031,"原密码不能为空");
        errorMsg.put(10032,"新密码不能为空");errorMsg.put(10033,"原密码错误");
        errorMsg.put(10034,"原手机号码不能为空");errorMsg.put(10035,"新手机号码不能为空");
        errorMsg.put(10036,"手机号码错误");errorMsg.put(10037,"手机号码已存在");
        errorMsg.put(10038,"修改手机号失败");errorMsg.put(10039,"两次手机号码不能相同");
        errorMsg.put(10040,"页数不能为空");errorMsg.put(10041,"每页数量不能为空");
        errorMsg.put(10042,"售货机id不能为空");errorMsg.put(10043,"售货机不存在");
        errorMsg.put(10044,"两次密码不能相同");errorMsg.put(10045,"商品订单号不能为空");
        errorMsg.put(10046,"商品不存在");errorMsg.put(10047,"库存不足");
        errorMsg.put(10048,"下单失败");errorMsg.put(10049,"订单id不能为空");
        errorMsg.put(10050,"订单不存在");errorMsg.put(10051,"订单和用户不相同");
        errorMsg.put(10052,"姓名不能为空");errorMsg.put(10053,"身份证号不能为空");
        errorMsg.put(10054,"已经实名认证过");errorMsg.put(10055,"身份证照片不能空");
        errorMsg.put(10056,"身份证认证失败");errorMsg.put(10057,"订单号不能为空");
        errorMsg.put(10058,"支付密码不能为空");errorMsg.put(10059,"订单不存在");
        errorMsg.put(10060,"订单不是待付款状态");errorMsg.put(10061,"余额不足");
        errorMsg.put(10062,"购买商品失败");errorMsg.put(10063,"未设置支付密码");
        errorMsg.put(10064,"支付密码错误");errorMsg.put(10065,"支付宝帐号不能为空");
        errorMsg.put(10066,"姓名不能为空");errorMsg.put(10067,"提现金额不能为空");
        errorMsg.put(10068,"当天提现次数不足");errorMsg.put(10069,"提现金额过大");
        errorMsg.put(10070,"提现金额过小");errorMsg.put(10071,"支付宝提现失败");
        errorMsg.put(10072,"意见反馈内容不能为空");errorMsg.put(10073,"提交反馈失败");
        errorMsg.put(10074,"最少上传一张图片");errorMsg.put(10075,"广告标题不能为空");
        errorMsg.put(10076,"广告内容不能为空");errorMsg.put(10077,"广告时间不能为空");
        errorMsg.put(10078,"广告时间套餐不存在");errorMsg.put(10079,"发布广告失败");
        errorMsg.put(10080,"订单异常");errorMsg.put(10081,"排序key不能为空");
        errorMsg.put(10082,"广告id 不能为空");errorMsg.put(10083,"广告不存在");
        errorMsg.put(10084,"饮水充值id不能为空");errorMsg.put(10085,"饮水充值不存在");
        errorMsg.put(10086,"充值饮水失败");errorMsg.put(10087,"首页广告id不能为空");
        errorMsg.put(10088,"首页广告不存在");errorMsg.put(10089,"北纬坐标不能为空");
        errorMsg.put(10090,"东经坐标不能为空");errorMsg.put(10091,"加盟商名称不能为空");
        errorMsg.put(10092,"营业执照编号不能为空");errorMsg.put(10093,"未通过实名认证");
        errorMsg.put(10094,"加盟商头像不能为空");errorMsg.put(10095,"营业执照图不能为空");
        errorMsg.put(10096,"申请加盟商失败");errorMsg.put(10097,"未开通加盟商");
        errorMsg.put(10098,"加盟商审核中");errorMsg.put(10099,"加盟商审核未通过");
        errorMsg.put(10100,"微信登录code不能为空");errorMsg.put(10101,"未绑定手机号码");
        errorMsg.put(10102,"微信登录失败");errorMsg.put(10103,"手机号码不能为空");
        errorMsg.put(10104,"绑定手机号码失败");errorMsg.put(10105,"充电次数不足");
        errorMsg.put(10106,"已经领取过免费次数");errorMsg.put(10107,"领取免费饮水次数失败");
        errorMsg.put(10108,"充值金额不能为空");errorMsg.put(10109,"充值金额过大");
        errorMsg.put(10110,"充值金额过小");errorMsg.put(10111,"充值失败");
        errorMsg.put(10112,"红包次数不足");errorMsg.put(10113,"抢红包失败");
        errorMsg.put(10114,"红包套餐不存在");errorMsg.put(10115,"购买抢红包次数失败");
        errorMsg.put(10116,"用户饮水次数不足");errorMsg.put(10117,"用户饮水失败");
        errorMsg.put(10118,"登录异常");errorMsg.put(10119,"投资金额不能为空");
        errorMsg.put(10120,"剩余投资金额不足");errorMsg.put(10121,"用户未申请加盟商");
        errorMsg.put(10122,"投资失败");errorMsg.put(10123,"冷热水类型不能为空");
        errorMsg.put(10124,"投资金额过小");errorMsg.put(10125,"投资金额过大");
        errorMsg.put(10126,"加盟商冻结中");errorMsg.put(10127,"广告未支付");
        errorMsg.put(10128,"当前状态不能重新发布");errorMsg.put(10129,"修改失败");
        errorMsg.put(10130,"售货机维护中");errorMsg.put(10131,"出水异常");
        errorMsg.put(10132,"uuid不存在");errorMsg.put(10133,"出水异常");
        errorMsg.put(10134,"正在出水中");errorMsg.put(10135,"正在出货");
        errorMsg.put(10136,"出货异常");errorMsg.put(10143,"售货机不存在");
        errorMsg.put(10144,"售货机维护中");errorMsg.put(-1,"购买点点物");
        errorMsg.put(-2,"使用饮水");

        errorMsg.put(20131,"请勿重复购买会员等级");
        errorMsg.put(20132,"购买会员等级失败");
        errorMsg.put(20134,"礼包不存在");errorMsg.put(20135,"礼包领取失败");
        errorMsg.put(20136,"已经领取过礼包");errorMsg.put(20137,"礼包已过期");
        errorMsg.put(20138 ,"礼包使用失败"); errorMsg.put(20139 ,"礼包已使用");
    }

    public int getCode() {
        return code;
    }

    public Object getEmsg() {

        for (Integer errorCode : mErrorMsg.keySet()) {
            if(errorCode==code) {
                Log.e("okhttp", "错误循环");
                if(errorCode != 10099){
                    emsg = mErrorMsg.get(errorCode);
                    Log.e("okhttp", "更新 emsg = "+emsg);
                }

            }
        }
        if(code == 10000){
            emsg = "登录异常，请重新登录";
        }
        Log.e("okhttp", "错误信息：code：" + code + "  具体错误：" + emsg);

        return emsg;
    }
}
