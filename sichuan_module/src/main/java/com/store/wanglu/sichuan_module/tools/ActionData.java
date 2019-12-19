package com.store.wanglu.sichuan_module.tools;

import com.android.smart.terminal.iptv.aidl.IServiceCfg;
import com.store.wanglu.sichuan_module.IptvPay;

import cn.sccl.iptv.feesdk.a;
import cn.sccl.iptv.feesdk.utils.HttpModel;
import okhttp3.FormBody;
import okhttp3.RequestBody;

public class ActionData {
    private static String reqInfo;
    private static HttpModel model;

    public static String getReqInfo(IServiceCfg mstub,int propIndex,boolean bInit){
        if(bInit) {
            a pdata = new a();
            model = IptvPay.getHttpModel(propIndex);
            reqInfo = pdata.a(mstub, model);
        }
        return reqInfo;
    }

    public static RequestBody getCheckInfo(int propIndex){

        final String productID = IptvPay.offerIds[propIndex];

        RequestBody bd1 = new FormBody.Builder()
                .add("appInnerId", model.getAppInnerId())
                .add("appcode",  model.getAppId())
                .add("transactionID", model.getTransactionID())
                .add("feecode", productID)
                .build();
        return bd1;
    }

    public static RequestBody getTakenNotes(String userCode,String userToken,int propIndex){

        RequestBody bd2 = new FormBody.Builder()
                .add("price", model.getProdPrice())
                .add("appId", model.getAppId())
                .add("appInnerId", model.getAppInnerId())
                .add("userToken", userToken)
                .add("reqTime", model.getReqTime())
                .add("userId", userCode)
                .add("channelCd", "")
                .add("transactionID", model.getTransactionID())
                .add("productName", model.getProdName())
                .add("productID", "")
                .build();
        return bd2;
    }

    public static String getReqInfo(){
        return reqInfo;
    }

    public static RequestBody getSecondInfo(String userCode,int propIndex){
        final String productID = IptvPay.offerIds[propIndex];
        final String purchaseType = "1";
        final String chargingValue = "0";
        final String spID = "HZSW";
        RequestBody bd6 = new FormBody.Builder()
                .add("loginAccount", userCode)
                .add("productID", productID)
                .add("fee", model.getProdPrice())
                .add("purchaseType", purchaseType)
                .add("chargingValue", chargingValue)
                .add("spID", spID)
                .add("reqInfo", reqInfo)
                .build();
        return bd6;
    }

}
