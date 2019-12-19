package com.game.mouse.modle.service;

import android.content.Context;
import android.content.SharedPreferences;

import com.game.mouse.screen.MainMidlet;

/*
*
* MagicFly writes in Aug. 16, 2019
* */

public class MyGameData {
    static MyGameData instance;

    public static MainMidlet activity;

    public String content = "";

    MyGameData() {

    }

    public static MyGameData getInstance() {
        if (instance == null) {
            instance = new MyGameData();
        }
        return instance;
    }

    public void loadData() {
        try {
            SharedPreferences sp = activity.getSharedPreferences("myData",Context.MODE_PRIVATE);
            if(sp.getString("stringKey","").equals("")){
                System.out.println(" ----- 数据为空 -----");
            }else{
                System.out.println("this is content = " + content);
                content = sp.getString("stringKey","");
            }
        } catch (Exception e) {
            System.out.println("this is Exception ---- LoadData()");
        }
    }

    public void saveData(String value) {
        try {
            SharedPreferences sp = activity.getSharedPreferences("myData",Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("stringKey",value);
            boolean flag = editor.commit();
            if(flag){
                System.out.println(" ---- 数据保存成功 ----");
            }else{
                System.out.println(" ---- 数据保存失败 ----");
            }
        } catch (Exception e) {
            System.out.println("this is Exception ---- SaveData()");
        }
    }
}
