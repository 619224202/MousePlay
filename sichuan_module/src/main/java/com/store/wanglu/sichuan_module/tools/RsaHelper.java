package com.store.wanglu.sichuan_module.tools;

import android.util.Base64;

import org.bouncycastle.crypto.InvalidCipherTextException;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;


public class RsaHelper {

        /**
         *
         * rsa加密
         * @param s
         * @param password
         * @return
         * @throws InvalidCipherTextException
         * @throws IOException
         */
        public static String encrypt(String s, String password) {
            byte[] date = Base64.encode(s.getBytes(),Base64.DEFAULT);
            String encryptStr = new String(date);
            return encryptStr;

        }

        /**
         *
         * rsa解密
         * @param s
         * @param password
         * @return
         * @throws InvalidCipherTextException
         * @throws IOException
         */
        public static String decrypt(String s, String password) {
           byte[] date = Base64.decode(s.getBytes(),Base64.DEFAULT);
            String encryptStr = new String(date);
            return encryptStr;
        }


    public static byte[] encryptDES(byte[]data,byte[]password){
        byte[] ret =null;
        if(data != null && data.length > 0){
            if(password != null && password.length == 8){
                try{
                    Cipher cipher= Cipher.getInstance("DES");
                    SecretKey Speckey=new SecretKeySpec(password,"DES");
                    cipher.init(Cipher.ENCRYPT_MODE,Speckey);
                    ret=cipher.doFinal(data);

                }catch(NoSuchAlgorithmException e){
                    e.printStackTrace();
                }catch(NoSuchPaddingException e){
                    e.printStackTrace();
                }catch(InvalidKeyException e){
                    e.printStackTrace();
                }catch(BadPaddingException e){
                    e.printStackTrace();
                }catch(IllegalBlockSizeException e){
                    e.printStackTrace();
                }
            }
        }
        return ret;
    }

    public static byte[] decryptDES(byte[] data,byte[] password){
        byte[] ret = null;

        if(data != null && data.length > 0){
//            if(password != null&& password.length == 8){
                try{
                    Cipher cipher = Cipher.getInstance("DES");

                    SecretKey Speckey=new SecretKeySpec(password,"DES");

                    cipher.init(Cipher.DECRYPT_MODE,Speckey);
                    ret = cipher.doFinal(data);
                }catch(NoSuchAlgorithmException e){
                    e.printStackTrace();
                }catch(NoSuchPaddingException e){
                    e.printStackTrace();
                }catch(InvalidKeyException e){
                    e.printStackTrace();
                }catch(BadPaddingException e){
                    e.printStackTrace();
                }catch(IllegalBlockSizeException e){
                    e.printStackTrace();
                }
        }
        return ret;
    }


}
