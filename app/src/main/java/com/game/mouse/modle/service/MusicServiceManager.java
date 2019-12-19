package com.game.mouse.modle.service;

import android.content.Context;
import android.content.Intent;

/**
 * Created by wanglu on 2019/8/2.
 */
public class MusicServiceManager {
    private static MusicServiceManager instance;
    private Intent musicIntent;
    private Context context;
    public static MusicServiceManager getInstance(){
        if(instance==null){
            instance = new MusicServiceManager();
        }
        return instance;
    }

    public void createMusic(Context context){
        this.context = context;
        musicIntent = new Intent(context, MyIntentService.class);
        musicIntent.setAction(MyIntentService.ACTION_MUSIC);
        context.startService(musicIntent);
    }

    public void pauseMusic(){
//        musicIntent = new Intent(context, MyIntentService.class);
//        musicIntent.setAction(MyIntentService.ACTION_MUSIC_STOP);
//        context.startService(musicIntent);
    }

    public void stopMusic(){
//        musicIntent = new Intent(context, MyIntentService.class);
//        musicIntent.setAction(MyIntentService.ACTION_MUSIC_STOP);
//        context.startService(musicIntent);
    }

    public void resumeMusic(){
//        musicIntent = new Intent(context, MyIntentService.class);
//        musicIntent.setAction(MyIntentService.ACTION_MUSIC_RESUME);
//        context.startService(musicIntent);
    }

    public void stopService(){
        context.stopService(musicIntent);
    }

    private MusicServiceManager(){
    }


}
