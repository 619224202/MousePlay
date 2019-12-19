package com.game.mouse.iptvpay;

import android.app.Activity;
import android.util.Log;

import com.model.mainServer.MainServer;

import org.json.JSONObject;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.UUID;

public class IptvPayHuNan {
    /**
     * 0 -- 解锁老鼠艾米  10
     * 1 -- 解锁老鼠米妮  10
     * 2 -- 万能奶酪*10   3
     * 3 -- 万能奶酪*25   5
     * 4 -- 万能奶酪*50   8
     * 5 -- 速度奶酪*25   5
     * 6 -- 回血胶囊*2    3
     * 7 -- 变身头盔*2    3
     * 8 -- 进化武器      5
     * 9 -- 升级武器      5
     * 10 -- 一次性通关   5
     * 11 -- 大偷取       5
     * 12 -- 小偷取       3
     * 13 -- 游戏复活     5
     * 14 -- 补充蓝量     5
     * 15 -- 战斗复活     3
     * 16 -- 必杀         3
     */
    private static String[] productIds = {"tvp132505001", "tvp132505002",
            "tvp132505003", "tvp132505004", "tvp132505005",
            "tvp132505007","tvp132505006", "tvp132505008",
            "tvp132505009", "tvp132505010", "tvp132505011",
            "tvp132505012","tvp132505013","tvp132505014",
            "tvp132505015","tvp132505016","tvp132505017"};

    private static String[] prices = {"10", "10",
            "3", "5", "8",
            "3","5", "3",
            "5", "5","5",
            "5","3","5",
            "5","3","3"};

    private static String key = "5187116359510906891";

    private static String cpId = "tvc132";

    private static String gameId = "tvg132505";

    //是否联网游戏，"true"or"false"，如果是联网游戏，消费接口中的notifyUrl参数不能为空，并且返回结果以后台回调为准。
    private static String isNetGame = "false";

    //SDK返回当前平台号（乐视小米等），便于游戏使用
    private static String platformId;

    //SDK返回的合法账号，如果是第一次启动游戏，则生成新的账号。游戏用这个账号当做游戏用户标识
    private static String account;

    //每个渠道的道具最高价格不一样，这个是当前渠道的道具最高价格。如果有超过此价格的道具，则不显示此道具
    private static String maxPrice;

    private static TVSDKClient tvsdk;

    public static boolean isInitFinished = false;

    public static IptvPayHuNan instance;

    public Activity activity;

    public static IptvPayHuNan getInstance(){
        if (instance == null){
            instance = new IptvPayHuNan();
        }
        return instance;
    }

    public static void login(){
        tvsdk.login();
    }

    public void initIptvPay(Activity act){
        activity = act;

        tvsdk = new TVSDKClient();

        HashMap<String, String> info = new HashMap<String, String>();
        info.put("key", key);
        info.put("cpId", cpId);
        info.put("gameId", gameId);
        info.put("isNetGame",isNetGame);
        info.put("deviceId", getLocalMacAddress());
        info.put("userId", "");//这里传空。。。由SDK生成新ID

        String userInfo = tvsdk.init(info,act);
        System.out.println("userInfo::::"+userInfo);

        Log.d("pay","pay init success");

        try {
            JSONObject jo = new JSONObject(userInfo);
            platformId = jo.getString("bossId");//SDK返回当前平台号（乐视小米等），便于游戏使用
            account = jo.getString("userId");//SDK返回的合法账号，如果是第一次启动游戏，则生成新的账号。游戏用这个账号当做游戏用户标识
            String maxPrice = jo.getString("maxPrice");//每个渠道的道具最高价格不一样，这个是当前渠道的道具最高价格。如果有超过此价格的道具，则不显示此道具
        } catch (org.json.JSONException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }

    public void onDestory() {
    }

    public void pay(int propId) {
        System.out.println(" ---- HuNan ---- public void pay() ---- propId = " + propId);

        if (!isInitFinished) {
            System.out.println("SDK未初始化!!!!!!!");
            return;
        }

        HashMap<String, String> payInfo = new HashMap<String, String>();
        payInfo.put("productId", productIds[propId]);
        payInfo.put("notifyUrl", "");
        payInfo.put("gameExtend", "");//扩展字段
        payInfo.put("orderCode", getOnlyID());//唯一订单号，可用此方法来生成
        payInfo.put("userId", account);//用户账号

        String result = tvsdk.pay(payInfo);

        if (result != null) {
            System.out.println("this is result---------" + result);         //0--成功
            JSONObject jo;
            try {
                jo = new JSONObject(result);
                System.out.println("this is jo.getString payResult-------" + jo.getString("payResult"));
                if(jo.getString("payResult").equals("true")){
                    paySuccess(propId);
                }else{
                    payFailed();
                }
            } catch (org.json.JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            payFailed();
        }
    }

    public void paySuccess(int propId) {
        System.out.println("this is paySuccess(int propId) ---- propId = " + propId);
        MainServer.getInstance().paySuccess();
    }

    public void payFailed() {
        System.out.println("this is payFailed() ");
        MainServer.getInstance().payFailed();
    }


    private static String getLocalMacAddress() {
        // TODO Auto-generated method stub
        byte[] mac = null;
        StringBuffer sb = new StringBuffer();
        try {
            Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();
            while (netInterfaces.hasMoreElements()) {
                NetworkInterface ni =  netInterfaces.nextElement();
                Enumeration<InetAddress> address = ni.getInetAddresses();

                while (address.hasMoreElements()) {
                    InetAddress ip = (InetAddress) address.nextElement();
                    if (ip.isAnyLocalAddress()||!(ip instanceof Inet4Address)||
                            ip.isLoopbackAddress()) {
                        continue;
                    }
                    if (ip.isSiteLocalAddress()) {
                        mac = ni.getHardwareAddress();
                    }else if(!ip.isLinkLocalAddress()) {
                        mac = ni.getHardwareAddress();
                        break;
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (mac != null) {
            for (int i = 0; i < mac.length; i++) {
                sb.append(parseByte(mac[i]));
            }
            return sb.substring(0,sb.length()-1).replace(":", "");
        }else {
            return "000000FFFFFF";
        }
    }

    private static String parseByte(byte b) {
        // TODO Auto-generated method stub
        String string = "00"+Integer.toHexString(b);
        return string.substring(string.length()-3);
    }

    private static String getOnlyID(){
        return UUID.randomUUID().toString().replace("-", "");
    }
}
