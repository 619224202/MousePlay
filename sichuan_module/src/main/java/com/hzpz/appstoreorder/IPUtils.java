package com.hzpz.appstoreorder;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Properties;

/**
 * Created by lyf on 2019-09-09.
 */
public class IPUtils {

    private static Properties payProperties ;

    public static String getIP(Context context){

        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();)
                {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && (inetAddress instanceof Inet4Address))
                    {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        }
        catch (SocketException ex){
            ex.printStackTrace();
        }
        return null;
    }

    public static void loadPayProperties(Context context){
        if(payProperties==null){
            InputStream is = null;
            payProperties = new Properties();
            try {
                is = context.getApplicationContext().getAssets().open("tenstep.properties");
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
                payProperties.load(bufferedReader);
            } catch (IOException e) {
                is = null;
                payProperties = null;
            }
        }
    }

    public static String getPropertiesValue(String key){
        String value = "";
        if(payProperties!=null) {
            value = payProperties.getProperty(key);
        }
        return value;
    }
}
