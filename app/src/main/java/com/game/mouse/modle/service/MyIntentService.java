package com.game.mouse.modle.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import com.game.mouse.screen.BackgroundMusic;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class MyIntentService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_FOO = "com.swkj.bear.iptvbearandroid.service.action.FOO";
    private static final String ACTION_BAZ = "com.swkj.bear.iptvbearandroid.service.action.BAZ";

    public static final String ACTION_MUSIC = "com.swkj.bear.iptvbearandroid.service.action.MUSIC";
    public static final String ACTION_MUSIC_STOP = "com.swkj.bear.iptvbearandroid.service.action.MUSICSTOP";
    public static final String ACTION_MUSIC_RESUME = "com.swkj.bear.iptvbearandroid.service.action.MUSICRESUME";
    public static final String ACTION_MUSIC_PAUSE = "com.swkj.bear.iptvbearandroid.service.action.MUSICPAUSE";
    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "com.swkj.bear.iptvbearandroid.service.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.swkj.bear.iptvbearandroid.service.extra.PARAM2";

    private BackgroundMusic backMusic;

    public MyIntentService() {
        super("MyIntentService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionFoo(Context context, String param1, String param2) {
        Intent intent = new Intent(context, MyIntentService.class);
        intent.setAction(ACTION_FOO);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionBaz(Context context, String param1, String param2) {
        Intent intent = new Intent(context, MyIntentService.class);
        intent.setAction(ACTION_BAZ);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    public static void startActionMusic(Context context, String param1, String param2) {
        Intent intent = new Intent(context, MyIntentService.class);
        intent.setAction(ACTION_MUSIC);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    public static void startActionMusicPause(Context context, String param1, String param2) {
        Intent intent = new Intent(context, MyIntentService.class);
        intent.setAction(ACTION_MUSIC_PAUSE);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    public static void startActionMusicResume(Context context, String param1, String param2) {
        Intent intent = new Intent(context, MyIntentService.class);
        intent.setAction(ACTION_MUSIC_RESUME);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    public static void startActionMusicStop(Context context, String param1, String param2) {
        Intent intent = new Intent(context, MyIntentService.class);
        intent.setAction(ACTION_MUSIC_STOP);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_FOO.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionFoo(param1, param2);
            } else if (ACTION_BAZ.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionBaz(param1, param2);
            }
            if(ACTION_MUSIC.equals(action)){
                handleActionMusic();
            }else if(ACTION_MUSIC_PAUSE.equals(action)){
                handleActionMusicPause();
            }else if(ACTION_MUSIC_RESUME.equals(action)){
                handleActionMusicResume();
            }else if(ACTION_MUSIC_STOP.equals(action)){
                handleActionMusicStop();
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionFoo(String param1, String param2) {
        // TODO: Handle action Foo
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleActionBaz(String param1, String param2) {
        // TODO: Handle action Baz
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void handleActionMusic() {
        BackgroundMusic.getInstance(this).playBackgroundMusic("bgm.mp3",true);
        System.out.println("play background   bgm.mp3, addr="+this.toString());
    }
    private void handleActionMusicStop() {
        BackgroundMusic.getInstance(this).stopBackgroundMusic();
        System.out.println("stop background   bgm.mp3");
    }
    private void handleActionMusicResume() {
        BackgroundMusic.getInstance(this).resumeBackgroundMusic();
        System.out.println("resume background   bgm.mp3,addr="+this.toString());
    }

    private void handleActionMusicPause() {
        BackgroundMusic.getInstance(this).pauseBackgroundMusic();
        System.out.println("pause background   bgm.mp3");
    }

}
