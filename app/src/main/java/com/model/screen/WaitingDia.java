package com.model.screen;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import com.game.lib9.L9Config;
import com.model.mainServer.MainServerInterface;
import com.model.mainServer.config.StringTools;

public class WaitingDia implements DialogInterface {
	private Font strFont = Font.getFont(0, Font.STYLE_BOLD, 16);
	private boolean isRunning;
	private int width;
	private int height;
	private int[] pos = new int[2];
	private Image waitImg;
	private Image strImg;
	private String s = "";
	private MainServerInterface httpServer;

	public WaitingDia() {
		isRunning = true;

		try {
			waitImg = Image.createImage("/pay/buyBg.png");
			strImg = Image.createImage("/pay/hintStr1.png");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		width = waitImg.getWidth();
		height = waitImg.getHeight();
		pos[0] = L9Config.SCR_W/2 - width / 2;
		pos[1] = 260 - height / 2;
		s = "";
	}

	public void paint(Graphics g) {
		g.translate(pos[0], pos[1]);
		g.drawImage(waitImg, 0, 0, 0);
		g.drawImage(strImg, width / 2, 18, Graphics.VCENTER | Graphics.HCENTER);
		StringTools.drawString("正在购买中，请稍后", 10, 50, width - 20, strFont,
				0x000000, g);
		g.translate(-pos[0], -pos[1]);
	}

	public int getSelOption() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void keyPressed(int keyCode) {
		// TODO Auto-generated method stub

	}

	public void release() {
		// TODO Auto-generated method stub
		waitImg = null;
	}

	public void update() {
		// TODO Auto-generated method stub

	}

	public void setPos(int x, int y) {
		// TODO Auto-generated method stub
		pos[0] = x - width / 2;
		pos[1] = y - height / 2;
	}

	public boolean isRunning() {
		return isRunning;
	}

	public void setStop() {
		isRunning = false;
	}

	public void setHttpServer(MainServerInterface httpServer) {
		this.httpServer = httpServer;
	}

	public void setImgName(String imgStr) {
		this.waitImg = null;
		try {
			waitImg = Image.createImage(imgStr);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
