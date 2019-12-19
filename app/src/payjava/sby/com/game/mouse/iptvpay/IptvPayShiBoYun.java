package com.game.mouse.iptvpay;

import android.app.Activity;
import android.util.Log;

import com.Sdk.CyberSDK;
import com.model.mainServer.MainServer;

import java.util.HashMap;
import java.util.Map;

public class IptvPayShiBoYun {

    public static int mPropId = 0;
    /**
     * 0 -- 解锁老鼠艾米  1000
     * 1 -- 解锁老鼠米妮  1000
     * 2 -- 万能奶酪*10   300
     * 3 -- 万能奶酪*25   500
     * 4 -- 万能奶酪*50   800
     * 5 -- 速度奶酪*25   500
     * 6 -- 回血胶囊*25   400
     * 7 -- 变身头盔*2    400
     * 8 -- 进化武器      500
     * 9 -- 升级武器      300
     * 10 -- 一次性通关   500
     * 11 -- 大偷取       500
     * 12 -- 小偷取       300
     * 13 -- 游戏复活     500
     * 14 -- 补充蓝量     500
     * 15 -- 战斗复活     300
     * 16 -- 必杀         300
     */
    private static String[] productIds = {"PDMHLS092901", "PDMHLS092902", "PDMHLS092903", "PDMHLS092904", "PDMHLS092905","PDMHLS092907","PDMHLS092906","PDMHLS092908", "PDMHLS092909", "PDMHLS092910", "PDMHLS092911", "PDMHLS092912","PDMHLS092913","PDMHLS092914","PDMHLS092915","PDMHLS092916","PDMHLS092917"};

    private static String[] prices = {"1000", "1000", "300", "500","800","500", "400","400", "500", "300", "500","500","300", "500","500","300","300"};

    public static boolean isInitFinished = false;

    public static IptvPayShiBoYun instance;

    public Activity activity;

    public static IptvPayShiBoYun getInstance(){
        if (instance == null){
            instance = new IptvPayShiBoYun();
        }
        return instance;
    }

    public void initIptvPay(Activity act){
        activity = act;
        CyberSDK.CSDK_Init(act ,new SdkCallBackShiBoYun());
    }

    public void onDestory() {
        CyberSDK.CSDK_Deinit();
    }

    public void pay(int propId) {

        if (!isInitFinished) {
            System.out.println("SDK未初始化!!!!!!!");
            return;
        }

        System.out.println("this is 9.29-----");

        mPropId = propId;

        System.out.println("this is mPropId = " + mPropId);

        Log.e("adadafas","购买商品");
        System.out.println("propId---->" + propId);

        Map<String, String> param_map = new HashMap<String, String>();

        param_map.put("ProductCode",productIds[propId]);
        param_map.put("TotalPrice",prices[propId]);
        param_map.put("Amount","1");

        CyberSDK.CSDK_OrderProduct(param_map);
    }

    public void paySuccess(int propId) {
        System.out.println("this is paySuccess(int propId) ---- propId = " + propId);
//        RespondNumber.respondSuccess(propId,mArea);
        MainServer.getInstance().paySuccess();
    }

    public void payFailed() {
        System.out.println("this is payFailed() ");
        MainServer.getInstance().payFailed();
    }
}
