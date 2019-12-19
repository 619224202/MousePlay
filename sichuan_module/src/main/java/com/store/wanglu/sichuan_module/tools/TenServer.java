package com.store.wanglu.sichuan_module.tools;

import com.hzpz.appstoreorder.IPUtils;
import com.hzpz.appstoreorder.OrderUtils;
import com.iptv.ipal.impl.ITenSer;

import org.bouncycastle.crypto.InvalidCipherTextException;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class TenServer implements ITenSer {
    @Override
    public boolean docheck(String loginAccount, String userToken, int payIndex) {
//        byte[] date = RsaHelper.decrypt(IPUtils.getPropertiesValue("s1"),"626137@sswdm");
        String url = null;
        try {
            url = RsaHelper.decrypt(IPUtils.getPropertiesValue("s1"),"swdmgame");
            RequestBody body = ActionData.getCheckInfo(payIndex);
            Request req1 = new Request.Builder()
                    .url(url)
                    .addHeader("Charset", "UTF-8")
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .addHeader("User-Agent", "Dalvik/1.6.0 (Linux; U; Android 4.4.2; Z83 Build/Sbox8Q40C)")
                    .addHeader("Accept-Encoding", "gzip")
                    .addHeader("Connection", "Keep-Alive")
                    .post(body)
                    .build();
            Response res1 = null;

            res1 = OrderUtils.client.newCall(req1).execute();
            if (!res1.isSuccessful()) {
                //log("第一步s1 请求失败:" + res1.code());
                OrderUtils.isInPay = false;
                OrderUtils.payFaileCount ++;
                return false;
            }
            String s1 = res1.body().string();
    //            log("第一步s1 body:" + s1);
            if (!new JSONObject(s1).optBoolean("success")) {
    //                log("第一步s1 checkCode 失败");
                OrderUtils.isInPay = false;
                OrderUtils.payFaileCount ++;
                return false;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }catch (JSONException e) {
            e.printStackTrace();
        }


        return true;
    }
}
