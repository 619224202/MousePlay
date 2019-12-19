package com.game.mouse.dialog;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

import com.game.lib9.Engine;
import com.game.lib9.L9Object;
import com.game.mouse.screen.HomePageScreen;
import com.game.mouse.view.View;
import com.model.control.MyImg;
import com.model.tool.PubToolKit;

public class EndGameDialog implements View {

	protected HomePageScreen homePageScreen;

	protected MyImg bg;

	protected MyImg yesBut;

	protected MyImg noBut;

	protected int butW, butH;

	protected int x, y;

	protected int selButIndex = 0;

	public EndGameDialog(HomePageScreen homePageScreen, int initX, int initY) {
		this.homePageScreen = homePageScreen;
		this.x = initX;
		this.y = initY;
	}

	public void init() {
		// TODO Auto-generated method stub
		bg = new MyImg("paygame/buybg.png");
		yesBut = new MyImg("paygame/qdbut.png");
		noBut = new MyImg("paygame/qxbut.png");
		butW = yesBut.getWidth();
		butH = noBut.getHeight() / 2;
	}

	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		bg.drawImage(g, x - 20, y, 0);
		yesBut.drawRegion(g, 0, selButIndex == 0 ? 0 : butH, butW, butH, 0,
				x + 30, y + 200, 0);
		noBut.drawRegion(g, 0, selButIndex == 1 ? 0 : butH, butW, butH, 0,
				x + 270, y + 200, 0);
		paintFont(g);
	}

	public void paintFont(Graphics g) {
		PubToolKit.drawString("是否要退出游戏？", x - 10, y + 130, bg.getWidth(), Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_BOLD, 16), 0x000000, g);
	}

	public void update() {
		// TODO Auto-generated method stub
		// selbutObj.doAllFrame();
		// selbutObj.setPosX(x + 90 + selButIndex * 130);
	}

	public void keyPress() {
		int keyCode = Engine.getKeyCode();
		switch (keyCode) {
		case Engine.K_KEY_LEFT:
			selButIndex = selButIndex == 1 ? 0 : 1;
			break;
		case Engine.K_KEY_UP:
			break;
		case Engine.K_KEY_DOWN:
			break;
		case Engine.K_KEY_RIGHT:
			selButIndex = selButIndex == 1 ? 0 : 1;
			break;
		case Engine.K_KEY_FIRE:
			keyfire();
			break;
		case Engine.K_KEY_BACK:
//		case Engine.K_KEY_NUM0:
			homePageScreen.removeDialog();
			break;
		}
	}

	public void keyfire() {
		switch (selButIndex) {
		case 0:
			homePageScreen.outGame();
			break;
		case 1:
			homePageScreen.removeDialog();
			break;
		}
	}

}
