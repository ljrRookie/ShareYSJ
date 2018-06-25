package com.gxysj.shareysj.network;

import com.vondear.rxtools.RxTool;

/**
 * Created by 林佳荣 on 2018/2/9.
 * Github：https://github.com/ljrRookie
 * Function ：所有相关请求
 */

public class HttpConstants {
    public static final String ROOT_URL = "https://app.zshshz.com/";
    // public static final String ROOT_URL = "http://192.168.0.223/";
    /**
     * 获取验证码
     */
    public static String GetCode = ROOT_URL + "GetCode";

    /**
     * 注册用户
     */
    public static String RegUser = ROOT_URL + "RegUser";

    /**
     * 账号密码登录
     */
    public static String LoginUser = ROOT_URL + "LoginUser";

    /**
     * 找回密码
     */
    public static String UserBackPassword = ROOT_URL + "UserBackPassword";

    /**
     * 获取用户资料
     */
    public static String GetUserInfo = ROOT_URL + "GetUserInfo";

    /**
     * 修改用户资料
     */
    public static String UpdateUserInfo = ROOT_URL + "UpdateUserInfo";
    /**
     * 修改手机号码
     */
    public static String UserUpdatePhone = ROOT_URL + "UserUpdatePhone";
    /**
     * 修改登陆密码
     */
    public static String UserUpdatePassword = ROOT_URL + "UserUpdatePassword";
    /**
     * 修改支付密码
     */
    public static String UpdateUserPayment = ROOT_URL + "UpdateUserPayment";
    /**
     * 获取售货机商品
     */
    public static String GetMachineCommodityList = ROOT_URL + "GetMachineCommodityList";
    /**
     * 我的广告
     */
    public static String GetUserAdvertisement = ROOT_URL + "GetUserAdvertisement";
    /**
     * 提交订单
     */
    public static String SubmitCommodityOrder = ROOT_URL + "SubmitCommodityOrder";
    /**
     * 点点物订单
     */
    public static String GetUserOrderList = ROOT_URL + "GetUserOrderList";
    /**
     * 点点物订单详情
     */
    public static String GetOrderInfo = ROOT_URL + "GetOrderInfo";

    /**
     * 认证身份
     */
    public static String UserIDSAuthentication = ROOT_URL + "UserIDSAuthentication";
    /**
     * 余额支付
     */
    public static String BalancePurchaseCommodity = ROOT_URL + "BalancePurchaseCommodity";
    /**
     * 我的推广
     */
    public static String GetUserDistributionInfo = ROOT_URL + "GetUserDistributionInfo";
    /**
     * 交易明细
     */
    public static String UserTransactionDetailed = ROOT_URL + "UserTransactionDetailed";
    /**
     * 意见反馈
     */
    public static String UserFeedback = ROOT_URL + "UserFeedback";
    /**
     * 广告价格
     */
    public static String GetAdvertisementPriceList = ROOT_URL + "GetAdvertisementPriceList";
    /**
     * 提现
     */
    public static String UserWithdrawalsAlipay = ROOT_URL + "UserWithdrawalsAlipay";
    /**
     * 广告列表
     */
    public static String GetAdvertisementList = ROOT_URL + "GetAdvertisementList";
    public static String UserAddAdvertisement = ROOT_URL + "UserAddAdvertisement";
    public static String BalancePaymentAdvertisement = ROOT_URL + "BalancePaymentAdvertisement";
    public static String GetAppHomeInfo = ROOT_URL + "GetAppHomeInfo";
    public static String GetAdvertisementInfo = ROOT_URL + "GetAdvertisementInfo";
    public static String UserGetOfflineList = ROOT_URL + "UserGetOfflineList";
    public static String GetUserBonusDetailedList = ROOT_URL + "GetUserBonusDetailedList";
    public static String GetWaterMoneyList = ROOT_URL + "GetWaterMoneyList";
    public static String UserBalancePaymentWater = ROOT_URL + "UserBalancePaymentWater";
    public static String GetNearbyMachine = ROOT_URL + "GetNearbyMachine";
    public static String GetFranchiserInfo = ROOT_URL + "GetFranchiserInfo";
    public static String UserApplyFranchiser = ROOT_URL + "UserApplyFranchiser";
    public static String WeixinLogin = ROOT_URL + "WeixinLogin";
    public static String BindingUserName = ROOT_URL + "BindingUserName";
    public static String GetChargingInfo = ROOT_URL + "GetChargingInfo";
    public static String GetUserBonusDetailed = ROOT_URL + "GetUserBonusDetailed";
    public static String GetUserInvestmentDetailed = ROOT_URL + "GetUserInvestmentDetailed";
    public static String GetUserInvestmentMachineList = ROOT_URL + "GetUserInvestmentMachineList";
    public static String GetShareInfo = ROOT_URL + "GetShareInfo";
    public static String UserGetFreeWater = ROOT_URL + "UserGetFreeWater";
    /**
     * 支付宝微信(app)充值余额
     */
    public static String UserAlipayRechargeBalance = ROOT_URL + "UserAlipayRechargeBalance";
    /**
     * 支付宝微信(app)购买商品
     */
    public static String UserAlipayCommodity = ROOT_URL + "UserAlipayCommodity";
    /**
     * 支付宝微信(app)购买广告
     */
    public static String UserAlipayAdvertisement = ROOT_URL + "UserAlipayAdvertisement";
    /**
     * 支付宝微信(app)购买饮水次数
     */
    public static String UserAlipayWater = ROOT_URL + "UserAlipayWater";
    public static String AppUpdate = ROOT_URL + "AppUpdate";
    public static String GetRedEnvelopeList = ROOT_URL + "GetRedEnvelopeList";
    public static String UserRedEnvelope = ROOT_URL + "UserRedEnvelope";
    public static String UserRedEnvelopesLog = ROOT_URL + "UserRedEnvelopesLog";
    public static String GetRedEnvelopesTypes = ROOT_URL + "GetRedEnvelopesTypes";
    public static String BalanceRedEnvelopes = ROOT_URL + "BalanceRedEnvelopes";
    public static String UserAlipayRedEnvelopesNub = ROOT_URL + "UserAlipayRedEnvelopesNub";
    public static String GetUserNewsLog = ROOT_URL + "GetUserNewsLog";
    public static String GetArticleInfo = ROOT_URL + "GetArticleInfo";
    public static String GetMachineList = ROOT_URL + "GetMachineList";
    /**
     * 加盟商余额投资机箱
     */
    public static String UserInvestmentMachine = ROOT_URL + "UserInvestmentMachine";
    public static String UserAlipayInvestmentMachine = ROOT_URL + "UserAlipayInvestmentMachine";
    public static String UserUseWater = ROOT_URL + "UserUseWater";
    public static String UserUpdateAdvertisement = ROOT_URL + "UserUpdateAdvertisement";
    /**
     * 获取投资列表城市
     */
    public static String InvestmentCityList = ROOT_URL + "InvestmentCityList";
    /**
     * 查询饮水是否已经出水
     */
    public static String VerificationWater = ROOT_URL+"VerificationWater";
    public static String VerificationCommodity = ROOT_URL+"VerificationCommodity";
    public static String GetMachineInfo = ROOT_URL+"GetMachineInfo";
    public static String GetAppImage = ROOT_URL+"GetAppImage";
    public static String UserCharging = ROOT_URL+"UserCharging";
    public static String BalancePaymentVip = ROOT_URL+"BalancePaymentVip";
    public static String GiftBagList = ROOT_URL+"GiftBagList";
    public static String UserReceiveGiftBagList = ROOT_URL+"UserReceiveGiftBagList";
    public static String GetUserGiftBagList = ROOT_URL+"GetUserGiftBagList";
    public static String UserAlipayVip = ROOT_URL + "UserAlipayVip";
    public static String UserUseReceiveGiftBag = ROOT_URL + "UserUseReceiveGiftBag";
}
