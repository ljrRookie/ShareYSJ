package com.gxysj.shareysj.network;

import android.app.Activity;

import com.gxysj.shareysj.bean.ADPriceBean;
import com.gxysj.shareysj.bean.AppImageBean;
import com.gxysj.shareysj.bean.AppUpdateBean;
import com.gxysj.shareysj.bean.BannerWebBean;
import com.gxysj.shareysj.bean.BounsDetail;
import com.gxysj.shareysj.bean.ChargingInfo;
import com.gxysj.shareysj.bean.FranchiserInfoBean;
import com.gxysj.shareysj.bean.GetAdvertisementBean;
import com.gxysj.shareysj.bean.GetAdvertisementInfoBean;
import com.gxysj.shareysj.bean.GetAppHomeInfoBean;
import com.gxysj.shareysj.bean.GetCityBean;
import com.gxysj.shareysj.bean.GetCodeBean;

import com.gxysj.shareysj.bean.GetOrderInfoBean;
import com.gxysj.shareysj.bean.GetUserDistributionInfoBean;
import com.gxysj.shareysj.bean.GetUuid;
import com.gxysj.shareysj.bean.GiftBagBean;
import com.gxysj.shareysj.bean.InvestMachineBean;
import com.gxysj.shareysj.bean.LoginUserBean;
import com.gxysj.shareysj.bean.MachineGoodBean;
import com.gxysj.shareysj.bean.MyOfflineBean;
import com.gxysj.shareysj.bean.NearbyMachineBean;
import com.gxysj.shareysj.bean.RedEnvelopeBean;
import com.gxysj.shareysj.bean.RedEnvelopesTypeBean;
import com.gxysj.shareysj.bean.ShareInfoBean;
import com.gxysj.shareysj.bean.ShopOrderBean;
import com.gxysj.shareysj.bean.UserAdBean;
import com.gxysj.shareysj.bean.UserAlipayRechargeBalanceBean;
import com.gxysj.shareysj.bean.UserGiftBean;
import com.gxysj.shareysj.bean.UserInfoBean;
import com.gxysj.shareysj.bean.UserInvestmentMachineBean;
import com.gxysj.shareysj.bean.UserNewsLogBean;
import com.gxysj.shareysj.bean.UserOrderListBean;
import com.gxysj.shareysj.bean.UserRedEnvelopeBean;
import com.gxysj.shareysj.bean.UserRedLogBean;
import com.gxysj.shareysj.bean.UserTransactionDetailedBean;
import com.gxysj.shareysj.bean.WaterMoneyListBean;
import com.ljr.delegate_sdk.okhttp.CommonOkHttpClient;
import com.ljr.delegate_sdk.okhttp.listener.DisposeDataHandle;
import com.ljr.delegate_sdk.okhttp.listener.DisposeDataListener;
import com.ljr.delegate_sdk.okhttp.request.CommonRequest;
import com.ljr.delegate_sdk.okhttp.request.RequestParams;

/**
 * Created by 林佳荣 on 2018/2/9.
 * Github：https://github.com/ljrRookie
 * Function ：不同业务的请求
 */

public class RequestCenter {


    /**
     * 根据参数发送所有post请求
     */

    public static void getRequest(String url, RequestParams params,
                                  DisposeDataListener listener, Class<?> clazz) {
        CommonOkHttpClient.get(CommonRequest.createGetRequest(url, params), new DisposeDataHandle(listener, clazz));
    }

    public static void postRequest(String url, RequestParams params,
                                   DisposeDataListener listener, Class<?> clazz) {

        CommonOkHttpClient.post(CommonRequest.createPostRequest(url, params), new DisposeDataHandle(listener, clazz));


    }
    public static void InvestmentCityList( DisposeDataListener listener){
        RequestCenter.postRequest(HttpConstants.InvestmentCityList, null, listener, GetCityBean.class);
    }
    /**
     * 获取验证码
     *
     * @param phoneNum 手机号码
     * @param type     获取类型
     * @param listener
     */
    public static void GetCodeData(String phoneNum, String type, DisposeDataListener listener) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("username", phoneNum);
        requestParams.put("type", type);
        RequestCenter.postRequest(HttpConstants.GetCode, requestParams, listener, GetCodeBean.class);
    }

    /**
     * 注册用户
     *
     * @param params
     * @param listener
     */
    public static void RegUserData(RequestParams params, DisposeDataListener listener) {
        RequestCenter.postRequest(HttpConstants.RegUser, params, listener, GetCodeBean.class);
    }

    /**
     * 账号密码登录
     *
     * @param params
     * @param listener
     */
    public static void LoginUser(RequestParams params, DisposeDataListener listener) {
        RequestCenter.postRequest(HttpConstants.LoginUser, params, listener, LoginUserBean.class);
    }

    /**
     * 找回密码
     *
     * @param requestParams
     * @param listener
     */
    public static void UserBackPassword(RequestParams requestParams, DisposeDataListener listener) {
        RequestCenter.postRequest(HttpConstants.UserBackPassword, requestParams, listener, GetCodeBean.class);
    }

    /**
     * 获取用户资料
     *
     * @param requestParams
     * @param listener
     */
    public static void GetUserInfo(RequestParams requestParams, DisposeDataListener listener) {
        RequestCenter.postRequest(HttpConstants.GetUserInfo, requestParams, listener, UserInfoBean.class);
    }

    /**
     * 修改手机号码
     *
     * @param requestParams
     * @param listener
     */
    public static void UserUpdatePhone(RequestParams requestParams, DisposeDataListener listener) {
        RequestCenter.postRequest(HttpConstants.UserUpdatePhone, requestParams, listener, GetCodeBean.class);
    }

    /**
     * 修改个人信息
     */
    public static void UpdateUserInfo(RequestParams requestParams, DisposeDataListener listener) {
        RequestCenter.postRequest(HttpConstants.UpdateUserInfo, requestParams, listener, GetCodeBean.class);
    }

    /**
     * 修改登录密码
     *
     * @param requestParams
     * @param listener
     */
    public static void UserUpdatePassword(RequestParams requestParams, DisposeDataListener listener) {
        RequestCenter.postRequest(HttpConstants.UserUpdatePassword, requestParams, listener, GetCodeBean.class);
    }

    /**
     * 修改支付密码
     */
    public static void UpdateUserPayment(RequestParams requestParams, DisposeDataListener listener) {
        RequestCenter.postRequest(HttpConstants.UpdateUserPayment, requestParams, listener, GetCodeBean.class);
    }

    /**
     * 获取售货机商品
     */
    public static void GetMachineCommodityList( RequestParams requestParams, DisposeDataListener listener) {

        RequestCenter.postRequest(HttpConstants.GetMachineCommodityList, requestParams, listener, MachineGoodBean.class);

    }

    /**
     * 我的广告
     */
    public static void GetUserAdvertisement(RequestParams requestParams, DisposeDataListener listener) {
        RequestCenter.postRequest(HttpConstants.GetUserAdvertisement, requestParams, listener, UserAdBean.class);
    }

    /**
     * 点点物提交订单
     *
     * @param requestParams
     * @param listener
     */
    public static void SubmitCommodityOrder(RequestParams requestParams, DisposeDataListener listener) {
        RequestCenter.postRequest(HttpConstants.SubmitCommodityOrder, requestParams, listener, ShopOrderBean.class);
    }

    /**
     * 点点物订单
     *
     * @param requestParams
     * @param listener
     */
    public static void GetUserOrderList(RequestParams requestParams, DisposeDataListener listener) {
        RequestCenter.postRequest(HttpConstants.GetUserOrderList, requestParams, listener, UserOrderListBean.class);
    }

    /**
     * 点点物订单详情
     *
     * @param requestParams
     * @param listener
     */
    public static void GetOrderInfo(RequestParams requestParams, DisposeDataListener listener) {
        RequestCenter.postRequest(HttpConstants.GetOrderInfo, requestParams, listener, GetOrderInfoBean.class);
    }

    /**
     * 点点物 - 余额购买
     *
     * @param requestParams
     * @param listener
     */
    public static void BalancePurchaseCommodity(RequestParams requestParams, DisposeDataListener listener) {
        RequestCenter.postRequest(HttpConstants.BalancePurchaseCommodity, requestParams, listener, GetCodeBean.class);
    }

    /**
     * 获取用户推广详情
     *
     * @param requestParams
     * @param listener
     */
    public static void GetUserDistributionInfo(RequestParams requestParams, DisposeDataListener listener) {
        RequestCenter.postRequest(HttpConstants.GetUserDistributionInfo, requestParams, listener, GetUserDistributionInfoBean.class);
    }

    /**
     * @param requestParams
     * @param listener
     */
    public static void UserTransactionDetailed(RequestParams requestParams, DisposeDataListener listener) {
        RequestCenter.postRequest(HttpConstants.UserTransactionDetailed, requestParams, listener, UserTransactionDetailedBean.class);
    }

    /**
     * 意见反馈
     *
     * @param requestParams
     * @param listener
     */
    public static void UserFeedback(RequestParams requestParams, DisposeDataListener listener) {
        RequestCenter.postRequest(HttpConstants.UserFeedback, requestParams, listener, GetCodeBean.class);

    }

    /**
     * 广告价格
     *
     * @param listener
     */
    public static void GetAdvertisementPriceList(DisposeDataListener listener) {
        RequestCenter.postRequest(HttpConstants.GetAdvertisementPriceList, null, listener, ADPriceBean.class);
    }

    public static void UserWithdrawalsAlipay(RequestParams requestParams, DisposeDataListener listener) {
        RequestCenter.postRequest(HttpConstants.UserWithdrawalsAlipay, requestParams, listener, GetCodeBean.class);
    }

    public static void GetAdvertisementList(RequestParams requestParams, DisposeDataListener listener) {
        RequestCenter.postRequest(HttpConstants.GetAdvertisementList, requestParams, listener, GetAdvertisementBean.class);
    }

    public static void BalancePaymentAdvertisement(RequestParams requestParams, DisposeDataListener listener) {
        RequestCenter.postRequest(HttpConstants.BalancePaymentAdvertisement, requestParams, listener, GetAdvertisementBean.class);

    }

    public static void GetAppHomeInfo(Activity activity, DisposeDataListener listener) {
       // YSJLoading.showLoading(activity);
        RequestCenter.postRequest(HttpConstants.GetAppHomeInfo, null, listener, GetAppHomeInfoBean.class);
       // YSJLoading.stopLoading();
    }

    public static void GetAdvertisementInfo(RequestParams params, DisposeDataListener listener) {
        RequestCenter.postRequest(HttpConstants.GetAdvertisementInfo, params, listener, GetAdvertisementInfoBean.class);
    }

    public static void UserGetOfflineList(RequestParams requestParams, DisposeDataListener listener) {
        RequestCenter.postRequest(HttpConstants.UserGetOfflineList, requestParams, listener, MyOfflineBean.class);
    }

    public static void GetUserBonusDetailedList(RequestParams requestParams, DisposeDataListener listener) {
        RequestCenter.postRequest(HttpConstants.GetUserBonusDetailedList, requestParams, listener, BounsDetail.class);
    }

    public static void GetWaterMoneyList(RequestParams params, DisposeDataListener listener) {
        RequestCenter.postRequest(HttpConstants.GetWaterMoneyList, params, listener, WaterMoneyListBean.class);
    }

    public static void UserBalancePaymentWater(RequestParams requestParams, DisposeDataListener listener) {
        RequestCenter.postRequest(HttpConstants.UserBalancePaymentWater, requestParams, listener, GetCodeBean.class);
    }

    public static void GetNearbyMachine(RequestParams params, DisposeDataListener listener) {
        RequestCenter.postRequest(HttpConstants.GetNearbyMachine, params, listener, NearbyMachineBean.class);
    }

    public static void GetFranchiserInfo(RequestParams requestParams, DisposeDataListener listener) {
        RequestCenter.postRequest(HttpConstants.GetFranchiserInfo, requestParams, listener, FranchiserInfoBean.class);
    }

    public static void WeixinLogin(RequestParams params, DisposeDataListener listener) {
        RequestCenter.postRequest(HttpConstants.WeixinLogin, params, listener, GetCodeBean.class);
    }

    public static void BindingUserName(RequestParams params, DisposeDataListener listener) {
        RequestCenter.postRequest(HttpConstants.BindingUserName, params, listener, LoginUserBean.class);
    }


    public static void GetChargingInfo(DisposeDataListener listener) {
        RequestCenter.postRequest(HttpConstants.GetChargingInfo, null, listener, ChargingInfo.class);
    }

    public static void GetUserBonusDetailed(RequestParams requestParams, DisposeDataListener listener) {
        RequestCenter.postRequest(HttpConstants.GetUserBonusDetailed, requestParams, listener, BounsDetail.class);
    }

    public static void GetUserInvestmentDetailed(RequestParams requestParams, DisposeDataListener listener) {
        RequestCenter.postRequest(HttpConstants.GetUserInvestmentDetailed, requestParams, listener, BounsDetail.class);
    }

    public static void GetUserInvestmentMachineList(RequestParams requestParams, DisposeDataListener listener) {
        RequestCenter.postRequest(HttpConstants.GetUserInvestmentMachineList, requestParams, listener, UserInvestmentMachineBean.class);
    }

    public static void GetShareInfo(RequestParams requestParams, DisposeDataListener listener) {
        RequestCenter.postRequest(HttpConstants.GetShareInfo, requestParams, listener, ShareInfoBean.class);
    }

    public static void UserGetFreeWater(RequestParams requestParams, DisposeDataListener listener) {
        RequestCenter.postRequest(HttpConstants.UserGetFreeWater, requestParams, listener, GetCodeBean.class);
    }

    /**
     * 支付宝微信(app)充值余额
     *
     * @param requestParams
     * @param listener
     */
    public static void UserAlipayRechargeBalance(RequestParams requestParams, DisposeDataListener listener) {
        RequestCenter.postRequest(HttpConstants.UserAlipayRechargeBalance, requestParams, listener, UserAlipayRechargeBalanceBean.class);
    }

    /**
     * 支付宝微信(app)购买商品
     * @param requestParams
     * @param listener
     */
    public static void UserAlipayCommodity(RequestParams requestParams, DisposeDataListener listener) {
        RequestCenter.postRequest(HttpConstants.UserAlipayCommodity, requestParams, listener, UserAlipayRechargeBalanceBean.class);
    }
    /**
     * 支付宝微信(app)购买广告
     * @param requestParams
     * @param listener
     */
    public static void UserAlipayAdvertisement(RequestParams requestParams, DisposeDataListener listener) {
        RequestCenter.postRequest(HttpConstants.UserAlipayAdvertisement, requestParams, listener, UserAlipayRechargeBalanceBean.class);
    }

    public static void UserAlipayWater(RequestParams requestParams, DisposeDataListener listener) {
        RequestCenter.postRequest(HttpConstants.UserAlipayWater, requestParams, listener, UserAlipayRechargeBalanceBean.class);
    }

    public static void AppUpdate(RequestParams params,DisposeDataListener listener) {
        RequestCenter.postRequest(HttpConstants.AppUpdate, params, listener,AppUpdateBean.class);
    }

    public static void GetRedEnvelopeList(RequestParams requestParams,DisposeDataListener listener) {
        RequestCenter.postRequest(HttpConstants.GetRedEnvelopeList, requestParams, listener,RedEnvelopeBean.class);
    }

    public static void UserRedEnvelope(RequestParams requestParams,DisposeDataListener listener) {
        RequestCenter.postRequest(HttpConstants.UserRedEnvelope, requestParams, listener,UserRedEnvelopeBean.class);
    }
    public static void UserRedEnvelopesLog(RequestParams requestParams,DisposeDataListener listener) {
        RequestCenter.postRequest(HttpConstants.UserRedEnvelopesLog, requestParams, listener,UserRedLogBean.class);
    }
    public static void GetRedEnvelopesTypes(DisposeDataListener listener) {
        RequestCenter.postRequest(HttpConstants.GetRedEnvelopesTypes, null, listener,RedEnvelopesTypeBean.class);
    }

    public static void BalanceRedEnvelopes(RequestParams requestParams, DisposeDataListener listener) {
        RequestCenter.postRequest(HttpConstants.BalanceRedEnvelopes, requestParams, listener, GetCodeBean.class);
    }

    public static void UserAlipayRedEnvelopesNub(RequestParams requestParams, DisposeDataListener listener) {
        RequestCenter.postRequest(HttpConstants.UserAlipayRedEnvelopesNub, requestParams, listener, UserAlipayRechargeBalanceBean.class);
    }
    public static void GetUserNewsLog(RequestParams requestParams,DisposeDataListener listener) {
        RequestCenter.postRequest(HttpConstants.GetUserNewsLog, requestParams, listener, UserNewsLogBean.class);
    }

    public static void GetArticleInfo(RequestParams params,DisposeDataListener listener) {
        RequestCenter.postRequest(HttpConstants.GetArticleInfo, params, listener, BannerWebBean.class);
    }
    public static void GetMachineList(RequestParams params,DisposeDataListener listener) {
        RequestCenter.postRequest(HttpConstants.GetMachineList, params, listener, InvestMachineBean.class);
    }

    public static void UserInvestmentMachine(RequestParams requestParams, DisposeDataListener listener) {
        RequestCenter.postRequest(HttpConstants.UserInvestmentMachine, requestParams, listener, GetCodeBean.class);
    }
    public static void BalancePaymentVip(RequestParams requestParams, DisposeDataListener listener) {
        RequestCenter.postRequest(HttpConstants.BalancePaymentVip, requestParams, listener, GetCodeBean.class);
    }

    public static void UserAlipayInvestmentMachine(RequestParams requestParams, DisposeDataListener listener) {
        RequestCenter.postRequest(HttpConstants.UserAlipayInvestmentMachine, requestParams, listener, UserAlipayRechargeBalanceBean.class);
    }
    public static void UserAlipayVip(RequestParams requestParams, DisposeDataListener listener) {
        RequestCenter.postRequest(HttpConstants.UserAlipayVip, requestParams, listener, UserAlipayRechargeBalanceBean.class);
    }
    public static void UserUseWater(RequestParams requestParams, DisposeDataListener listener) {
        RequestCenter.postRequest(HttpConstants.UserUseWater, requestParams, listener, GetUuid.class);
    }

    public static void VerificationWater(RequestParams requestParams, DisposeDataListener listener) {
        RequestCenter.postRequest(HttpConstants.VerificationWater, requestParams, listener, GetCodeBean.class);
    }

    public static void VerificationCommodity(RequestParams requestParams, DisposeDataListener listener) {
        RequestCenter.postRequest(HttpConstants.VerificationCommodity, requestParams, listener, GetCodeBean.class);
    }

    public static void BigScreenAdvertisement( DisposeDataListener listener){
        RequestCenter.postRequest(HttpConstants.ROOT_URL+"BigScreenAdvertisement", null, listener, null);
    }

    public static void GetMachineInfo(RequestParams requestParams, DisposeDataListener listener) {
        RequestCenter.postRequest(HttpConstants.GetMachineInfo, requestParams, listener, ShopOrderBean.class);

    }

    public static void GetAppImage( DisposeDataListener listener) {
        RequestCenter.postRequest(HttpConstants.GetAppImage, null, listener, AppImageBean.class);
    }

    public static void UserCharging(RequestParams requestParams, DisposeDataListener listener) {
        RequestCenter.postRequest(HttpConstants.UserCharging, requestParams, listener, GetCodeBean.class);

    }

    public static void GiftBagList(RequestParams requestParams, DisposeDataListener listener) {
        RequestCenter.postRequest(HttpConstants.GiftBagList, requestParams, listener, GiftBagBean.class);
    }

    public static void UserReceiveGiftBagList(RequestParams requestParams, DisposeDataListener listener) {
        RequestCenter.postRequest(HttpConstants.UserReceiveGiftBagList, requestParams, listener, GetCodeBean.class);
    }

    public static void GetUserGiftBagList(RequestParams requestParams, DisposeDataListener listener) {
        RequestCenter.postRequest(HttpConstants.GetUserGiftBagList, requestParams, listener, UserGiftBean.class);
    }

    public static void UserUseReceiveGiftBag(RequestParams requestParams, DisposeDataListener listener) {
        RequestCenter.postRequest(HttpConstants.UserUseReceiveGiftBag, requestParams, listener, GetCodeBean.class);
    }
}
