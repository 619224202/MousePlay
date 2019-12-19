package com.model.screen;

import java.io.IOException;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import com.game.lib9.L9Config;
import com.model.mainServer.MainConfig;
import com.model.mainServer.MainServer;
import com.model.mainServer.config.StringTools;

public class OrderBuyDialog implements DialogInterface {
	/**
	 * 图片
	 */
	private Image bgImg;
	private Image[] butImg;
	private Image strImg;
	private Image xzImg;
	public int selectOption;
	public final static int Yes_Option = 1;
	public final static int No_Option = 2;
	private int currentFrame;

	public boolean isRunning;
	private MainServer mainServer;
	private int[] drawPos = new int[2];
	private int width;
	private int height;
	private Font strFont ;
	private int strHight;

	private int color;

	public OrderBuyDialog() {
		strFont=MainConfig.gameFont;
		isRunning = true;
		selectOption = Yes_Option;
		loadImg();
		width = bgImg.getWidth();
		height = bgImg.getHeight();
		this.drawPos[0] = L9Config.SCR_W/2 - width / 2;
		this.drawPos[1] = 260 - height / 2;
		color = 0x000000;
		strHight = strFont.getHeight();
	}

	public void loadImg() {
		try {
			bgImg = Image.createImage("/pay/buyBg.png");
			butImg = new Image[2];
			for (int i = 0; i < 2; i++) {
				butImg[i] = Image.createImage("/pay/btn" + ((i + 1)) + ".png");
			}
			xzImg = Image.createImage("/pay/xk.png");
			strImg = Image.createImage("/pay/hintStr2.png");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void mainPro() {
		if (currentFrame > 0) {
			currentFrame--;
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
			if (selectOption == No_Option) {
				isRunning = false;
			} else {
				if (currentFrame == 0) {
					isRunning = false;
				}
			}
			break;
		case -31:
		case -7:
		case -11:
			selectOption = No_Option;
			isRunning = false;
			break;
		}
	}

	public void setImg(String bgImgStr, String yesButImgStr,
			String noButImgStr, String tipImgStr, String xzImgStr) {
		try {
			bgImg = null;
			butImg[0] = null;
			butImg[1] = null;
			bgImg = Image.createImage(bgImgStr);
			butImg = new Image[2];
			butImg[0] = Image.createImage(yesButImgStr);
			butImg[1] = Image.createImage(noButImgStr);
			if (tipImgStr != null && !"".equals(tipImgStr)) {
				strImg = Image.createImage(tipImgStr);
			} else {
				strImg = null;
			}
			if (xzImgStr != null && !"".equals(xzImgStr)) {
				xzImg = Image.createImage(xzImgStr);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void paint(Graphics g) {
		g.translate(drawPos[0], drawPos[1]);
		g.drawImage(bgImg, 0, 0, 0);
		if (strImg != null) {
			g.drawImage(strImg, width / 2, 18, Graphics.VCENTER
					| Graphics.HCENTER);
		}
		this.paintStrs(g);
		if (selectOption == Yes_Option) {
			paintBut(g, butImg[0], 90, 190, true);
			paintBut(g, butImg[1], 218, 190, false);
		} else {
			paintBut(g, butImg[0], 90, 190, false);
			paintBut(g, butImg[1], 218, 190, true);
		}

		g.translate(-(drawPos[0]), -(drawPos[1]));
	}

	private void paintStrs(Graphics g) {
		StringTools.drawString("非包月用户不能使用该道具,开通包月畅玩游戏", 10, height / 3,
				width - 20, strFont, color, g);
	}

	private void paintBut(Graphics g, Image btnImg, int x, int y, boolean flag) {
		if (flag) {
			g.drawRegion(btnImg, 0, 0, btnImg.getWidth(), btnImg.getHeight()/2, 0, x, y, Graphics.VCENTER
					| Graphics.HCENTER);
			StringTools.drawSelIcon(g, xzImg, 86, 36, x, y);
		} else {
			g.drawRegion(btnImg, 0, btnImg.getHeight()/2, btnImg.getWidth(), 
					btnImg.getHeight()/2, 0, x, y, Graphics.VCENTER| Graphics.HCENTER);
		}
	}

	public int getSelOption() {
		// TODO Auto-generated method stub
		return selectOption;
	}

	public void release() {
		// TODO Auto-generated method stub
		bgImg = null;
		for (int i = 0; i < 2; i++) {
			butImg[i] = null;
		}

		xzImg = null;
		strImg = null;
	}

	public void update() {
		// TODO Auto-generated method stub
		mainPro();
	}

	public void setPos(int x, int y) {
		// TODO Auto-generated method stub
		this.drawPos[0] = x - width / 2;
		this.drawPos[1] = y - height / 2;
	}

	public boolean isRunning() {
		return isRunning;
	}

}
