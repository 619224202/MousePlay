package com.hzpz.appstoreorder;

import android.content.Context;
import android.text.TextUtils;

import com.iptv.ipal.impl.ISecond;
import com.iptv.ipal.impl.IStore;
import com.iptv.ipal.impl.ITaken;
import com.iptv.ipal.impl.ITenSer;
import com.store.wanglu.sichuan_module.BuildConfig;
import com.store.wanglu.sichuan_module.IptvPay;
import com.store.wanglu.sichuan_module.PayService;
import com.store.wanglu.sichuan_module.tools.ActionData;
import com.store.wanglu.sichuan_module.tools.SecondServer;
import com.store.wanglu.sichuan_module.tools.StoreServer;
import com.store.wanglu.sichuan_module.tools.Taken;
import com.store.wanglu.sichuan_module.tools.TenServer;


import java.math.BigInteger;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;

/**
 * Created by lyf on 2019-08-29.
 */
public class OrderUtils {

    public static OkHttpClient client;
    private static final HashMap<String, List<okhttp3.Cookie>> cookieStore = new HashMap<>();

    static {
        client = new OkHttpClient.Builder()
                .cookieJar(new CookieJar() {
                    @Override
                    public void saveFromResponse(HttpUrl httpUrl, List<okhttp3.Cookie> list) {
                        cookieStore.put(httpUrl.host(), list);
                    }

                    @Override
                    public List<okhttp3.Cookie> loadForRequest(HttpUrl httpUrl) {
                        List<okhttp3.Cookie> cookies = cookieStore.get(httpUrl.host());
                        return cookies != null ? cookies : new ArrayList<okhttp3.Cookie>();
                    }
                })
                .build();
    }

    public static boolean isInPay = false;
    public static int payFaileCount = 0;

    public static void start(final Context context, final String loginAccount, final String userToken,int payIndex) throws Exception {
        isInPay = true;

        final String productID = IptvPay.offerIds[payIndex], productName = IptvPay.proNames[payIndex], productDesc = productName;
        final String fee = IptvPay.proPrices[payIndex];

        String reqInfo = ActionData.getReqInfo();

        ITenSer tenSer = new TenServer();
        if(!tenSer.docheck(loginAccount,userToken, payIndex)){
            System.out.println("art:Rejecting error in 1");
            return;
        }

        ITaken takenSer = new Taken();
        if(!takenSer.takeNotes(loginAccount,userToken,payIndex)){
            System.out.println("art:Rejecting error in 2");
            return;
        }

        IStore storeSer = new StoreServer();
        if(!storeSer.store(loginAccount,userToken,payIndex,IptvPay.packageName)){
            System.out.println("art:Rejecting error in 3");
            return;
        }
        String locationRef = ((StoreServer)storeSer).getLocation();

        ISecond secondSer = new SecondServer();
        if(!secondSer.doSecond(IptvPay.packageName,loginAccount,locationRef,payIndex)){
            System.out.println("art:Rejecting error in 4");
            return;
        }

        int allCount = PayService.getInstance().getPayCount();
        PayService.getInstance().setPayCount(allCount+ (Integer.parseInt(fee)/100));
        PayService.getInstance().savePayInfo(IptvPay.gameCode,loginAccount,IptvPay.proPrices[payIndex],IptvPay.proNames[payIndex],1);
//        log("第六步s6 body:" + s6);
        isInPay = false;
        System.out.println("art:Rejecting error in -1");

    }



    private static void log(String body) {
//        Timber.e("#######:" + body);
    }

    private static String sub(String content, String sub) {
        return content.substring(content.indexOf(sub) + sub.length(),
                content.indexOf("&", content.indexOf(sub)));
    }

    private static String getMD5(String str) {
        try {
            MessageDigest instance = MessageDigest.getInstance("MD5");
            instance.update(str.getBytes());
            return new BigInteger(1, instance.digest()).toString(16);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String encode(String str) {
        try {
            return URLEncoder.encode(str, "gbk");
        } catch (Exception unused) {
            if (TextUtils.isEmpty(str)) {
                str = BuildConfig.FLAVOR;
            }
            return str;
        }
    }
}
