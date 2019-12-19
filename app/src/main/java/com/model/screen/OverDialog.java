package com.model.screen;

import java.io.IOException;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import com.game.lib9.L9Config;
import com.model.mainServer.config.StringTools;

public class OverDialog implements DialogInterface {

	public final static int Type_Suc = 1;
	public final static int Type_Failed = 2;
	public final static int Type_Limmit = 3;
	public final static int Type_NotBuy = 4;

	private int currentFrame;

	private Image bgImg;
	private Image strImg;

	private boolean isRunning;

	private int[] posXY = new int[2];

	private Font strFont = Font.getFont(0, Font.STYLE_BOLD, 16);

	private String str;

	private int width;
	private int height;

	/**
	 * 
	 * 
	 */

	public OverDialog(String str) {
		try {
			bgImg = Image.createImage("/pay/buyBg.png");
			strImg = Image.createImage("/pay/hintStr1.png");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		currentFrame = 10;
		isRunning = true;
		this.str = str;
		width = bgImg.getWidth();
		height = bgImg.getHeight();
		posXY[0] = L9Config.SCR_W/2 - width / 2;
		posXY[1] = 260 - height / 2;
	}

	public int getSelOption() {
		// TODO Auto-generated method stub
		return 0;
	}

	public boolean isRunning() {
		// TODO Auto-generated method stub
		return isRunning;
	}

	public void setImg(String imgName, String tipImgStr) {
		this.bgImg = null;
		try {
			bgImg = Image.createImage(imgName);
			if (tipImgStr != null && !"".equals(tipImgStr)) {
				strImg = Image.createImage(tipImgStr);
			} else {
				strImg = null;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void keyPressed(int keyCode) {
		// TODO Auto-generated method stub

	}

	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		g.translate(posXY[0], posXY[1]);
		g.drawImage(bgImg, 0, 0, 0);
		if (strImg != null) {
			g.drawImage(strImg, width / 2, 18, Graphics.VCENTER
					| Graphics.HCENTER);
		}
		StringTools.drawString(str, 10, height/2, width - 20, strFont, 0x000000, g);
		g.translate(-posXY[0], -posXY[1]);
	}

	public void release() {
		// TODO Auto-generated method stub
		bgImg = null;
		strImg = null;
	}

	public void setPos(int x, int y) {
		// TODO Auto-generated method stub
		posXY[0] = x - bgImg.getWidth() / 2;
		posXY[1] = y - bgImg.getHeight() / 2;
	}

	public void update() {
		// TODO Auto-generated method stub
		if (currentFrame > 0) {
			currentFrame--;
			if (currentFrame == 0) {
				isRunning = false;
			}
		}
	}

}
