package com.model.tool;

import java.io.IOException;
import java.io.InputStream;

import javax.microedition.media.Manager;
import javax.microedition.media.MediaException;
import javax.microedition.media.Player;

public class MusicPlayer {
	static MusicPlayer music;
	static MusicPlayer music2;
	private Player p;
	//private Player[] p;
	String url = "";
	
	private InputStream[] is;

	public static final String AUDIO_TYPE_WAV = "audio/x-wav";
	public static final String AUDIO_TYPE_MP3 = "audio/mpeg";
	public static final String AUDIO_TYPE_MID ="audio/midi";
	public static MusicPlayer getInstance() {
		if (music == null) {
			
			music = new MusicPlayer();
		}
		return music;
	}
	
	public static MusicPlayer getInstance1() {
		if (music2 == null) {
			
			music2 = new MusicPlayer();
		}
		return music2;
	} 
	
	public MusicPlayer(){
		is = new InputStream[4];
//		p = new Player[4];
	//	b = -1;
	}
	
	public void setPlayMusic(String[] urls){
		for(int i = 0;i<4;i++){
			is[i] = getClass().getResourceAsStream(urls[i]);
		}
//		for(int i = 0;i<data.length;i++){
//			is[i] = new ByteArrayInputStream(data[i]);
//			try {
//				p[i] = Manager.createPlayer(is[i], AUDIO_TYPE_WAV);
//				p[i].stop();
//				p[i].realize();
//				p[i].prefetch();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (MediaException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
	}

	byte[] b;
	//int b;
	public synchronized void playgames(byte[] data,String type) {

		
	}
	
	public synchronized void stop(int k){

	}
	
	public synchronized void playgames(InputStream is,String type,boolean flag){
		
	}
	
	public void playgames(String url,int index) {
//		if(!Config.sound){
//			return;
//		}
		String type = "";
		if (url.toLowerCase().endsWith("wav") ) { // 如果后缀名为wav
			type = AUDIO_TYPE_WAV;
		} else if (url.toLowerCase().endsWith("mp3") ) { // 如果后缀名为mid
			type = AUDIO_TYPE_MP3;
		} else if (url.toLowerCase().endsWith("midi")||url.toLowerCase().endsWith("mid")) { // 如果后缀名为mid
			type = AUDIO_TYPE_MID;
		}
	//	stopPlayer();
		if(Runtime.getRuntime().freeMemory()<200000){
			System.out.println("声音播放失败");
			return;
		}
		
//		System.out.println(is.toString());
		int lenth = 0;
		int k = 0;
		
//		System.out.println("aaaaaaaaaaaaaaaaaa11111111111 = "+this.url.equals(url));
		try {
			if (p == null) {
				this.url = url;
				System.out.println("创建声音。。。。。。。。。。。。。。。。。。。");
				InputStream is = getClass().getResourceAsStream(url);
				//InputStream is = getClass().getResourceAsStream(url);
				p = Manager.createPlayer(is, type);
				is = null;
				p.realize();
				p.prefetch();
				p.setMediaTime(0);
				p.setLoopCount(index);
//				System.out.println("aaaaaaaaaaaaa");
				p.start();
//				p.setLoopCount(-1);
			} else {
				if (!this.url.equals(url)) {
					stopPlayer();
					this.url = url;
					//InputStream is = getClass().getResourceAsStream(url);
					InputStream is = getClass().getResourceAsStream(url);
					p = Manager.createPlayer(is, type);
					p.realize();
					p.prefetch();
					p.start();
				} else {
					if(p.getState()!=Player.CLOSED){
						p.stop();
					}
//					System.out.println("当前的声音时间为 = "+p.getMediaTime());
//					if(p.getMediaTime() != 0){
					p.realize();
					p.prefetch();
					p.setMediaTime(-1);
					p.setLoopCount(index);
					//}
					p.start();
//					System.out.println("设置完后的声音时间是 = "+p.getMediaTime());
				}
			}
//			is.close();
			
		} catch (IOException e) {
			e.getMessage();
		} catch (MediaException e) {
			e.getMessage();
		}
	}

	public synchronized void stopPlayer() {
		try {
//			if (p!= null) {
//				for(int i = 0;i<p.length;i++){
//					p[i].stop();
//					p[i].close();
//					p[i] = null;
//				}
//			}
//				p = null;
			
			
			if(p!=null){
				p.stop();
				p.close();
			}
			p = null;
			System.gc();
			}catch (MediaException ex) {
				ex.printStackTrace();
			}
			
	}
	
	public synchronized void release(){
//		try {
//			if (p != null) {
//				for(int i = 0;i<p.length;i++){
//					p[i].stop();
//					p[i].close();
//					p[i] = null;
//				}
//				}
//			p = null;
//		}catch (MediaException ex) {
//			ex.printStackTrace();
//		}
	}
}
