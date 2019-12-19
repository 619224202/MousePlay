package com.model.tool;

import com.game.mouse.context.Config;


/**
 * 播放声音控制类
 * 
 * @author Administrator
 * 
 */
public final class PlaySoundService {
	private String[] urls = { "/sound/bg.wav", "/sound/button.wav",
			"/sound/pao.wav", "/sound/lvup.wav" };
	private String[] types = { "audio/x-wav" };
	private SoundPlayer[] soundPlayers = new SoundPlayer[4];
	private static PlaySoundService playSoundService = null;

	private boolean isopen = false;

	private PlaySoundService() {
		isopen = false;
		if (isopen) {
			for (int i = 0; i < 4; i++) {
				this.soundPlayers[i] = new SoundPlayer(i);
			}
		}
	}

	public static void initSoundService() {
		if (playSoundService == null)
			playSoundService = new PlaySoundService();
	}

	public static void play(int index) {
		initSoundService();
		//System.out.println("播放>>>>>>"+playSoundService.isopen);
		if (playSoundService.isopen) {
			if (playSoundService != null) {
				if (index == 0) {
					if (!playSoundService.soundPlayers[0].isStarted()) {
						playSoundService.soundPlayers[0].createSound(
								playSoundService.urls[index], -1,
								playSoundService.types[0]);
					}
					playSoundService.soundPlayers[0].play();
					return;
				}
				if (!playSoundService.soundPlayers[index].isLoad()) {
					loadPlay(index);
				}
				if (!playSoundService.soundPlayers[index].getStart())
					playSoundService.soundPlayers[index].rePlay();
			}
		}
	}

	public static void loadPlay(int index) {
		initSoundService();
		if (playSoundService.isopen) {
			if (playSoundService != null) {
				if (!playSoundService.soundPlayers[index].isLoad()) {
					playSoundService.soundPlayers[index].createSound(
							playSoundService.urls[index], 1,
							playSoundService.types[0]);
				}
			}
		}
	}

	public static void stopbg() {
		initSoundService();
		if (playSoundService.isopen) {
			if (playSoundService.soundPlayers[0] != null
					&& playSoundService.soundPlayers[0].isLoad())
				playSoundService.soundPlayers[0].stop();
		}
	}

}