package com.store.wanglu.sichuan_module;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.hzpz.appstoreorder.IPUtils;
import com.iptv.ipal.impl.IPayOver;
import com.store.wanglu.sichuan_module.tools.RsaHelper;

import org.bouncycastle.crypto.InvalidCipherTextException;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import cn.sccl.iptv.feesdk.StartOrderActivity;
import cn.sccl.iptv.feesdk.utils.ConstantLib;
import cn.sccl.iptv.feesdk.utils.HttpModel;


/**
 * Created by zuixian on 2018/5/22.
 */
public class IptvPay {

    public static int mBuyTime;

    /**
     * 0 -- 解锁老鼠艾米  5
     * 1 -- 解锁老鼠米妮  5
     * 2 -- 万能奶酪*10   2
     * 3 -- 万能奶酪*25   5
     * 4 -- 万能奶酪*25   5
     * 5 -- 速度奶酪*25   5
     * 6 -- 回血胶囊*2    5
     * 7 -- 变身头盔*2    5
     * 8 -- 进化武器      5
     * 9 -- 升级武器      5
     * 10 -- 一次性通关   5
     * 11 -- 大偷取       5
     * 12 -- 小偷取       2
     * 13 -- 游戏复活     5
     * 14 -- 补充蓝量     5
     * 15 -- 战斗复活     2
     * 16 -- 必杀         2
     */
    //道具ID
    public static String[] productIds = {"49597072189", "49597072189",
            "49597072186", "49597072189" , "49597072189" , "49597072189", "49597072189", "49597072189",
            "49597072189","49597072189", "49597072189", "49597072189","49597072186","49597072189", "49597072189", "49597072186","49597072186"};

    // 产品名称
    public static String[] proNames = {"解锁老鼠艾米", "解锁老鼠米妮",
            "万能奶酪*10", "万能奶酪*25", "万能奶酪*25", "速度奶酪*25", "回血胶囊*2","变身头盔*2","进化武器","升级武器","一次性通关","大偷取","小偷取","游戏复活","补充蓝量","战斗复活","必杀"};

    // 产品价格
    public static String[] proPrices = {"5", "5", "2", "5", "5",
            "5", "5", "5", "5", "5",
            "5", "5","2", "5", "5", "2","2"};

    //销售品ID
    public static String[] offerIds = {"400024", "400024", "400023", "400024", "400024",
            "400024", "400024", "400024", "400024", "400024",
            "400024", "400024", "400023", "400024", "400024","400023", "400023"};

    // 产品订购渠道编码
    private static String channelCd = "205";

    // 认证密钥
    private static String key = "HZSW205Y";

    // 认证密钥
    private static String autherKey = "HZSW205Y";

    //产品接入平台编码
    private static String systemCd = "115";

    //应用ID
    private static String appId = "49597072180";

    //登录账号
    public static String accNbr = "";

    //登录token
    public static String userToken = "";

    //游戏code
    public static String gameCode = "aaa";

    public static String packageName = "";


    public static boolean isInitFinished = false;

    public static IptvPay instance;

    private static Activity act;

    private IPayOver payOver;

    private int propId;

    public static IptvPay getInstance(){
        if (instance == null){
            instance = new IptvPay();
        }
        return instance;
    }

    public void initIptvPay(Activity a){
        act = a;
        isInitFinished = true;
        packageName = act.getApplication().getPackageName();
        IPUtils.loadPayProperties(act);
        PayService.getInstance().bindService(act);
    }

    public void test(){

        payResult(ConstantLib.CODE_FOR_PAY,ConstantLib.RESULT_OF_PAY,new Intent());
    }

    public void onDestory() {
        PayService.getInstance().unBindService(act);
    }

    public void pay(int propId,int time,IPayOver payOver) {

        if (!isInitFinished) {
            System.out.println("SDK未初始化!!!!!!!");
            return;
        }

        mBuyTime = time;
        pay(propId,payOver);
    }

    public void pay(int propId,IPayOver payOver){
        this.payOver = payOver;
        this.propId = propId;
        Intent intent = new Intent(act, StartOrderActivity.class);
        intent.putExtra(ConstantLib.TRANS_HTTP_MODE_KEY,IptvPay.getHttpModel(propId));
        act.startActivityForResult(intent, ConstantLib.CODE_FOR_PAY);
    }

    public void payResult(int requestCode,int resultCode,Intent data){
        if (requestCode == ConstantLib.CODE_FOR_PAY && resultCode == ConstantLib.RESULT_OF_PAY) {
            // 支付状态码，0：支付成功，1：支付失败 默认值为1
            int payStateCode = data.getIntExtra(ConstantLib.KEY_FOR_PAY_RESULT_CODE, ConstantLib.PAY_FAILED_CODE);
            if (payStateCode == ConstantLib.PAY_SUCCEED_CODE) {
                payOver.paySuccess(propId);
            } else {
                payOver.payFailed(propId);
//                PayService.getInstance().savePayInfo(0,IptvPay.proPrices[propId],IptvPay.proNames[propId]);
            }
            //保存订购数据
            PayService.getInstance().savePayInfo(payStateCode,IptvPay.proPrices[propId],IptvPay.proNames[propId]);
        }
    }


    public static HttpModel getHttpModel(int propId){

        String time = getTime();

        autherKey = getMD5(channelCd + time + key);

        HttpModel httpModel = new HttpModel();
        httpModel.setTransactionID(getOnlyID()); // 流水号 String
        httpModel.setChannelCd(channelCd); // 产品订购渠道编码
        httpModel.setKey(key); // 认证密钥
        httpModel.setProdName(proNames[propId]); // 产品名称
        httpModel.setProdPrice(proPrices[propId]); // 产品价格
        httpModel.setReqTime(time); // 请求时间
        httpModel.setAutherKey(autherKey) ;// 认证密钥
        httpModel.setSystemCd(systemCd) ;//产品接入平台编码
        httpModel.setAppId (appId) ;//应用ID
        httpModel.setAppInnerId (productIds[propId]);//道具ID
        httpModel.setOfferId (offerIds[propId]);//销售品ID
        httpModel.setAccNbr(accNbr);//登录账号
        httpModel.setUserToken(userToken);//登录token
        httpModel.setOrderType("0");

//        act.pay(httpModel);

        return httpModel;
    }

    // 流水号 String
    private static String getOnlyID(){
        return UUID.randomUUID().toString().replace("-", "");
    }

    // 请求时间
    private static String getTime(){
        return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
    }

    public void paySuccess(int propId) {
        //java回调C++
//        RespondNumber.respondSuccess(propId,mBuyTime);
        //
    }

    public void payFailed() {
//        RespondNumber.respondFailed(mBuyTime);
    }

    /**
     * 对字符串md5加密(小写+字母)
     *
     * @param str 传入要加密的字符串
     * @return  MD5加密后的字符串
     */
    public static String getMD5(String str) {
        try {
            // 生成一个MD5加密计算摘要
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 计算md5函数
            md.update(str.getBytes());
            // digest()最后确定返回md5 hash值，返回值为8为字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
            // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
            return new BigInteger(1, md.digest()).toString(16);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
