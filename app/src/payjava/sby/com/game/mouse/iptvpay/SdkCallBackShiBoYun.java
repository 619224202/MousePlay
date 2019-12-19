package com.game.mouse.iptvpay;

import com.Sdk.CyberSDK;
import com.Sdk.Interface.ISdkCallBack;
import com.Sdk.Vo.CSDK_Result;
import com.Sdk.Vo.GetAppProductInfoResult;
import com.Sdk.Vo.GetRankListInfoResult;
import com.Sdk.Vo.GetUserInfoResult;
import com.Sdk.Vo.OrderProductResult;

/**
 * Created by zuixian on 2018/9/26.
 */
public class SdkCallBackShiBoYun implements ISdkCallBack {
    @Override
    public void InitCalBack(CSDK_Result csdk_result) {
        System.out.println("初始化回调结果----->csdk_result.result_code" + csdk_result.result_code + ", ----- csdk_result.result_desc" + csdk_result.result_desc);
        IptvPayShiBoYun.isInitFinished = csdk_result.result_code == 0;
    }

    @Override
    public void GetUserInfoCallBack(GetUserInfoResult getUserInfoResult) {
        System.out.println("this is GetUserInfoCallBack()----------");
    }

    @Override
    public void DeinitCalBack(CSDK_Result csdk_result) {

    }

    @Override
    public void SubmitUserScore1CallBack(CSDK_Result csdk_result) {
        System.out.println("this is SubmitUserScore1CallBack()----------");
    }

    @Override
    public void GetRankListInfoCallBack(GetRankListInfoResult getRankListInfoResult) {
        System.out.println("this is GetRankListInfoCallBack()----------");
    }

    @Override
    public void GetAppProductInfoCallBack(GetAppProductInfoResult getAppProductInfoResult) {
        System.out.println("this is GetAppProductInfoCallBack()----------");
    }

    @Override
    public void OrderStateCallback(String s, int i, String s1) {
        System.out.println("OrderStateCallback--" + s + " -- i = " + i + " -- s1 = " + s1);
        if (i == 2) {
            CyberSDK.CSDK_ConfirmOrder(s,0);
        } else if (i == 9 || i == 4 || i == 3) {
            //支付失败
            IptvPayShiBoYun.getInstance().payFailed();
        } else if (i == 0) {
            //支付成功
            IptvPayShiBoYun.getInstance().paySuccess(IptvPayShiBoYun.mPropId);
        }
    }

    @Override
    public void OrderProductCallback(OrderProductResult orderProductResult) {
        System.out.println("this is OrderProductCallback()----------");
    }

    @Override
    public void ConfirmOrderCallBack(CSDK_Result csdk_result) {
        System.out.println("this is ConfirmOrderCallBack()----------");
    }
}
