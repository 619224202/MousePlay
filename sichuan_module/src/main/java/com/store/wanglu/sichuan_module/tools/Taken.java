package com.store.wanglu.sichuan_module.tools;

import com.hzpz.appstoreorder.IPUtils;
import com.hzpz.appstoreorder.OrderUtils;
import com.iptv.ipal.impl.ITaken;

import org.bouncycastle.crypto.InvalidCipherTextException;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Taken implements ITaken {
    @Override
    public boolean takeNotes(String loginAccount, String userToken, int payIndex) {
        try {
            String url = RsaHelper.decrypt(IPUtils.getPropertiesValue("s2"),"swdmgame");
            RequestBody body = ActionData.getTakenNotes(loginAccount,userToken,payIndex);
            Request req2 = new Request.Builder()
                    .url(url)
                    .addHeader("Charset", "UTF-8")
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .addHeader("User-Agent", "Dalvik/1.6.0 (Linux; U; Android 4.4.2; Z83 Build/Sbox8Q40C)")
                    .addHeader("Accept-Encoding", "gzip")
                    .addHeader("Connection", "Keep-Alive")
                    .post(body).build();
            Response res2 = null;
            res2 = OrderUtils.client.newCall(req2).execute();
            if (!res2.isSuccessful()) {
//            log("第二步s2 请求失败:" + res2.code());
                OrderUtils.isInPay = false;
                OrderUtils.payFaileCount ++;
                return false;
            }
            String s2 = res2.body().string();
//        log("第二步s2 body:" + s2);
            if (!new JSONObject(s2).optBoolean("success")) {
//            log("第二步s2 takeNotesLog 失败");
                OrderUtils.payFaileCount ++;
                OrderUtils.isInPay = false;
                return false;
            }
        }  catch (IOException e) {
            OrderUtils.payFaileCount ++;
            OrderUtils.isInPay = false;
//            e.printStackTrace();
            return false;
        }catch (JSONException e) {
            OrderUtils.payFaileCount ++;
            OrderUtils.isInPay = false;
            return false;
        }
//        String url2 = "http://110.190.92.149:7020/appstorecheck/takeNotesLog";
        // price=200&appId=49597071751&appInnerId=49597071757&userToken=7gCjb7gCjb3DMzc5vqrwCDnkwYkTTa1P&reqTime=20190909165044&userId=HAPPTSZX06%40ITVP&channelCd=&transactionID=4a77bf3e5f8d4989b7169599ff397ea8&
        // productName=%E6%96%B0%E9%BE%99%E6%96%97%E5%A3%AB2%E5%85%83%E9%81%93%E5%85%B7&productID=


        return false;
    }
}
