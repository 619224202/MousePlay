package com.store.wanglu.sichuan_module.tools;

import android.text.TextUtils;

import com.hzpz.appstoreorder.IPUtils;
import com.hzpz.appstoreorder.OrderUtils;
import com.iptv.ipal.impl.IStore;
import com.store.wanglu.sichuan_module.PayService;

import java.io.IOException;

import okhttp3.Request;
import okhttp3.Response;

public class StoreServer implements IStore {
    private String location;
    @Override
    public boolean store(String loginAccount, String userToken, int payIndex,String packageName) {
        String url = null;
        try {
            url = RsaHelper.decrypt(IPUtils.getPropertiesValue("s3"),"sswdmgame");
            String url3 = url + ActionData.getReqInfo();
            String userAgent = "Mozilla/5.0 (Linux; Android 4.4.2; Z83 Build/Sbox8Q40C) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/30.0.0.0 Safari/537.36";
            //INFO=%3CtransactionID%3E4a77bf3e5f8d4989b7169599ff397ea8%3C/transactionID%3E%3CchannelCd%3E205%3C/channelCd%3E%3CuserId%3EHAPPTSZX06%40ITVP%3C/userId%3E%3CuserToken%3E7gCjb7gCjb3DMzc5vqrwCDnkwYkTTa1P%3C/userToken%3E%3CautherKey%3Ec92e1002257de13b96534b3cf019da27%3C/autherKey%3E%3CappId%3E49597071751%3C/appId%3E%3CappInnerId%3E49597071757%3C/appInnerId%3E%3CproductID%3E400023%3C/productID%3E%3Cprice%3E200%3C/price%3E%3CproductName%3E%D0%C2%C1%FA%B6%B7%CA%BF2%D4%AA%B5%C0%BE%DF%3C/productName%3E%3CreqTime%3E20190909165044%3C/reqTime%3E%3Cbackurl%3Ehttp%3A%2F%2F110.190.92.149%3A7020%2Fappstorecheck%2Fbackurl.jsp%3C/backurl%3E%3CnotifyUrl%3E%3C/notifyUrl%3E%3CoptFlag%3E0%3C/optFlag%3E
            Request req3 = new Request.Builder()
                    .url(url3)
                    .addHeader("Connection", "keep-alive")
                    .addHeader("Pragma", "no-cache")
                    .addHeader("Cache-Control", "no-cache")
                    .addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
                    .addHeader("User-Agent", TextUtils.isEmpty(PayService.userAent)?userAgent:PayService.userAent)
                    .addHeader("Accept-Encoding", "gzip,deflate")
                    .addHeader("Accept-Language", "zh-CN,en-US;q=0.8")
                    // TODO Cookie               .addHeader("Cookie", "JSESSIONID=9EEC52F00325F1C42B08CD2D72F942D5")
                    .addHeader("X-Requested-With", packageName)
                    .get()
                    .build();
            Response res3 = null;
            res3 = OrderUtils.client.newCall(req3).execute();
            if (!res3.isSuccessful()) {
//            log("第三步s3 请求失败：" + res3.code());
                OrderUtils.isInPay = false;
                OrderUtils.payFaileCount++;
                return false;
            }

            String s3 = res3.body().string();
            location = res3.request().url().toString();
        }  catch (IOException e) {
            OrderUtils.isInPay = false;
            OrderUtils.payFaileCount++;
            return false;
        }

        return true;
    }

    public String getLocation(){
        return location;
    }
}
