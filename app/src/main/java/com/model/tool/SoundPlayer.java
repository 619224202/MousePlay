package com.model.tool;

import java.io.IOException;
import java.io.InputStream;

import javax.microedition.media.Controllable;
import javax.microedition.media.Manager;
import javax.microedition.media.MediaException;
import javax.microedition.media.Player;
import javax.microedition.media.PlayerListener;
import javax.microedition.media.control.VolumeControl;

public final class SoundPlayer implements PlayerListener {
	private Player player;
	private int loopCount = -1;
	private boolean started = false;
    private boolean isload=false;
	public SoundPlayer(int loopCount) {
		this.loopCount = loopCount;
	}

	private void load(String url, int loopCount, String type) {
		InputStream is;
		try {
			is = getClass().getResourceAsStream(url);
			this.player = Manager.createPlayer(is, type);
			this.player.addPlayerListener(this);
			this.player.realize();
			this.player.prefetch();
			this.player.setLoopCount(loopCount);
			isload=true;
			if (this.loopCount == 0) {
				setLevel(100);
				return;
			}
			setLevel(100);
			return;
		} catch (IOException e) {
			e.printStackTrace();
			return;
		} catch (MediaException e1) {
			e1.printStackTrace();
		}
	}

	public final void createSound(String url, int paramInt, String paramString2) {
		if (this.player == null) {
			load(url, paramInt, paramString2);
			return;
		}
		if (this.player != null) {
			this.player.removePlayerListener(this);
			this.player.deallocate();
			this.player.close();
			this.player = null;
			started = false;
		}
		load(url, paramInt, paramString2);
	}

	public final boolean isStarted() {
		return this.started;
	}

	public final void play() {
		try {
			if (this.player != null)
				this.player.start();
			return;
		} catch (MediaException localMediaException2) {
			MediaException localMediaException1;
			(localMediaException1 = localMediaException2).printStackTrace();
		}
	}
	
	public void stop() {
		try {
			if (this.player.getState() == Player.STARTED) {
				this.player.stop();
			}
		} catch (MediaException e) {
			e.printStackTrace();
		}
	}

	public final void rePlay() {
		try {
			if (this.player != null) {
				this.player.setMediaTime(-2153172997903482880L);
				this.player.start();
			}
			return;
		} catch (MediaException e) {
			e.printStackTrace();
		}
	}

	private void setLevel(int paramInt) {
		((VolumeControl) this.player.getControl("VolumeControl")).setLevel(100);
	}

	public final void playerUpdate(Player paramPlayer, String paramString,
			Object paramObject) {
		try {
			if (paramString.equals("started")) {
				this.started = true;
				return;
			}
			if (paramString.equals("endOfMedia"))
				return;
			if ((paramString == "stopped") || (paramString == "error")
					|| (paramString == "deviceUnavailable")
					|| (paramString == "closed")) {
				this.player.stop();
				return;
			}
		} catch (Exception localException) {
		}
	}

	/**
	 * 是否已经播放
	 * 
	 * @return
	 */
	public final boolean getStart() {
		return this.player.getState() == 400;
	}

	public boolean isLoad() {
		return isload;
	}

	public void setIsload(boolean isload) {
		this.isload = isload;
	}

}

/*
 * Location: C:\Users\Administrator\Desktop\ddfly.jar Qualified Name: l JD-Core
 * Version: 0.6.0
 */