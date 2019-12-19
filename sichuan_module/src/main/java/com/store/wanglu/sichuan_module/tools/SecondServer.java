package com.store.wanglu.sichuan_module.tools;

import android.text.TextUtils;

import com.hzpz.appstoreorder.IPUtils;
import com.hzpz.appstoreorder.OrderUtils;
import com.iptv.ipal.impl.ISecond;
import com.store.wanglu.sichuan_module.PayService;

import org.bouncycastle.crypto.InvalidCipherTextException;

import java.io.IOException;

import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SecondServer implements ISecond {
    @Override
    public boolean doSecond(String packageName, String loginAccount, String location,int payIndex) {

        String url6 = null;
        try {
            url6 = RsaHelper.decrypt(IPUtils.getPropertiesValue("s6"),"swdmgame");
            RequestBody requestBody = ActionData.getSecondInfo(loginAccount,payIndex);
            String userAgent = "Mozilla/5.0 (Linux; Android 4.4.2; Z83 Build/Sbox8Q40C) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/30.0.0.0 Safari/537.36";

            Request req6 = new Request.Builder()
                    .url(url6)
                    .addHeader("Connection", "keep-alive")
                    .addHeader("Pragma", "no-cache")
                    .addHeader("Cache-Control", "no-cache")
                    .addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
                    .addHeader("Origin", "http://110.190.92.69:8193")
                    .addHeader("User-Agent", TextUtils.isEmpty(PayService.userAent)?userAgent:PayService.userAent)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .addHeader("Referer", location)
                    .addHeader("Accept-Encoding", "gzip,deflate")
                    .addHeader("Accept-Language", "zh-CN,en-US;q=0.8")
                    // TODO Cookie               .addHeader("Cookie", "JSESSIONID=9EEC52F00325F1C42B08CD2D72F942D5")
                    .addHeader("X-Requested-With", packageName)
                    .post(requestBody)
                    .build();
            Response res6 = null;
            res6 = OrderUtils.client.newCall(req6).execute();
            if (!res6.isSuccessful()) {
//            log("第六步s6 请求失败:" + res6.code());
                OrderUtils.payFaileCount ++;
                OrderUtils.isInPay = false;
                return false;
            }

//        log("第六步s6 重定向地址:" + location);

            String s6 = res6.body().string();
        } catch (IOException e) {
            OrderUtils.payFaileCount++;
            OrderUtils.isInPay = false;
            return false;
        }

        return true;
    }
}
