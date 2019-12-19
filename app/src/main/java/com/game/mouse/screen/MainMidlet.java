package com.game.mouse.screen;

import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.KeyEvent;
import android.view.WindowManager;

import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

import com.game.lib9.Engine;
import com.game.mouse.modle.service.MusicServiceManager;
import com.game.mouse.modle.service.MyGameData;
import com.iptv.ipal.impl.IPayOver;
import com.model.mainServer.MainConfig;
import com.model.mainServer.MainServer;
import com.store.wanglu.sichuan_module.IptvPay;


public class MainMidlet extends MIDlet {

    public static Engine engine;

    public static Display defaultDisplay;

    public static float scaleX, scaleY;

    public MainMidlet() {

    }

    public void pauseApp() {
        MusicServiceManager.getInstance().pauseMusic();
        engine.pauseApp();
    }

    @Override
    protected void onResume() {
        MusicServiceManager.getInstance().resumeMusic();
        super.onResume();
    }

    public void startApp() {
        MyGameData.activity = this;
        System.out.println("mid1 = " + this);


        WindowManager windowManager = (WindowManager) getBaseContext().getSystemService(WINDOW_SERVICE);
        defaultDisplay = windowManager.getDefaultDisplay();

        scaleX = defaultDisplay.getWidth() / 1280.0f;
        scaleY = defaultDisplay.getHeight() / 720.0f;
        this.engine = new MainScreen(this);
        engine.resumeApp();
        //初始化视博云sdk
        if (MainConfig.Area == MainConfig.Type_SC) {
            IptvPay.getInstance().initIptvPay(this);
        }

//        BackgroundMusic.getInstance(getApplicationContext()).playBackgroundMusic("menu.mp3", true);
        MusicServiceManager.getInstance().createMusic(this);
    }

    public void pay(int propId) {
        IptvPay.getInstance().pay(propId, new IPayOver() {
            @Override
            public void paySuccess(int propId) {
                MainServer.getInstance().paySuccess();
            }

            @Override
            public void payFailed(int propId) {
                MainServer.getInstance().payFailed();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IptvPay.getInstance().payResult(requestCode, resultCode, data);

        super.onActivityResult(requestCode, resultCode, data);
    }



    public void parserXML(byte[] data) {

    }

    protected void destroyApp(boolean arg0) throws MIDletStateChangeException {
        // TODO Auto-generated method stub
        MusicServiceManager.getInstance().stopService();
        MainServer.getInstance().endGame();
    }

}
