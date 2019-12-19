package com.model.screen;

import java.io.IOException;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.midlet.MIDlet;

import com.game.lib9.L9Config;
import com.model.mainServer.MainServerInterface;
import com.model.mainServer.config.StringTools;

public class ErrorDialog implements DialogInterface{
	
	private Font strFont = Font.getFont(0, Font.STYLE_BOLD, 16);
	private boolean isRunning;
	private int width;
	private int height;
	private int[] pos = new int[2];
	private Image butImg;
	private String str ="";
	public ErrorDialog(String str) {
		isRunning = true;
		width = 446;
		height = 285;
		pos[0] = L9Config.SCR_W/2-width/2;
		pos[1] = 260-height/2;
		this.str = str;
		if(butImg == null){
			try {
				butImg = Image.createImage("/pay/1.png");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

	
	
	
	public void paint(Graphics g){
		g.setColor(0x000000);
		g.fillRect(0, 0, L9Config.SCR_W, 530);
		g.setColor(0x34e5ec);
		g.fillRect(135, 130, 370, 270);
		g.setColor(0x195271);
		g.fillRect(145, 140, 350, 250);
//		int k = L9Config.SCR_W/2-varLenth/2;
		StringTools.drawString(str, 150, 150, 330, strFont, 0xffffff, g);
		g.drawImage(butImg, L9Config.SCR_W/2, 350, Graphics.VCENTER|Graphics.HCENTER);
	}

	public int getSelOption() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void keyPressed(int keyCode) {
		// TODO Auto-generated method stub
		switch(keyCode){
		case -5:
		case -31:
		case -7:
		case -11:
			isRunning = false;
			break;
		}
	}

	public void release() {
		// TODO Auto-generated method stub
		butImg = null;
	}

	public void update() {
		// TODO Auto-generated method stub
	}

	public void setPos(int x, int y) {
		// TODO Auto-generated method stub
		pos[0] = x-width/2;
		pos[1] = y-height/2;
	}
	
	public boolean isRunning(){
		return isRunning;
	}
	
	public void setStop(){
		isRunning = false;
	}
	
}
