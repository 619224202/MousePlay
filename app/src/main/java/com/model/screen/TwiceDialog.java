package com.model.screen;

import java.io.IOException;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import com.game.lib9.L9Config;
import com.model.mainServer.config.StringTools;

public class TwiceDialog implements DialogInterface {

	/**
	 * ͼƬ
	 */
	private Image bgImg;
	private Image but0;
	private Image but0_s;
	private Image but1;
	private Image but1_s;
	public int selectOption;
	public final static int Yes_Option = 1;
	public final static int No_Option = 2;
	public boolean isRunning;
	private int[] drawPos = new int[2];
	private int width;
	private int height;

	public TwiceDialog() {
		isRunning = true;
		selectOption = No_Option;
		loadImg();
		this.drawPos[0] = L9Config.SCR_W/2;
		this.drawPos[1] = 260;
		width = bgImg.getWidth();
		height = bgImg.getHeight();

	}

	public void loadImg() {
		try {
			bgImg = Image.createImage("/pay/paybg.png");
			but0 = Image.createImage("/pay/but0.png");
			but0_s = Image.createImage("/pay/but0_s.png");
			but1 = Image.createImage("/pay/but1.png");
			but1_s = Image.createImage("/pay/but1_s.png");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void keyPressed(int keyCode) {
		switch (keyCode) {
		case -1:
			break;
		case -2:
			break;
		case -3:
			selectOption = Yes_Option;
			break;
		case -4:
			selectOption = No_Option;
			break;
		case -5:
			isRunning = false;
			break;
		case -31:
		case -7:
		case -11:
		case -32:
			selectOption = No_Option;
			isRunning = false;
			break;
		}
	}

	public void paint(Graphics g) {
		g.translate(drawPos[0] - width / 2, drawPos[1] - (height + 1) / 2);
		g.drawImage(bgImg, 0, 0, 0);
		if (selectOption == Yes_Option) {
			g.drawImage(but0_s, 70, 250, 0);
			g.drawImage(but1, 250, 250, 0);
		} else {
			g.drawImage(but0, 70, 250, 0);
			g.drawImage(but1_s, 250, 250, 0);
		}
		g
				.translate(-(drawPos[0] - width / 2),
						-(drawPos[1] - (height + 1) / 2));
	}

	public int getSelOption() {
		// TODO Auto-generated method stub
		return selectOption;
	}

	public void release() {
		// TODO Auto-generated method stub
		bgImg = null;
		but0 = null;
		but0_s = null;
		but1 = null;
		but1_s = null;
	}

	public void update() {
		// TODO Auto-generated method stub
	}

	public void setPos(int x, int y) {
		// TODO Auto-generated method stub
		this.drawPos[0] = x;
		this.drawPos[1] = y;
	}

	public boolean isRunning() {
		return isRunning;
	}

}
