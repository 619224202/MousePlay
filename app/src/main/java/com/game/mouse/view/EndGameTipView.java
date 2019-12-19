package com.game.mouse.view;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

import com.game.lib9.Engine;
import com.game.lib9.L9Object;
import com.game.lib9.L9Screen;
import com.game.mouse.screen.GamePageScreen;
import com.game.mouse.screen.MainMidlet;
import com.game.mouse.screen.ShopPageScreen;
import com.game.mouse.view.game.EndGameView;
import com.game.mouse.view.game.GameFightView;
import com.game.mouse.view.game.GameMainView;
import com.model.control.MyImg;
import com.model.mainServer.MainServer;
import com.model.tool.PubToolKit;

public class EndGameTipView implements EndGameView {
	private L9Screen screen;

	private MyImg bg;

	private MyImg but0;

	private int x;

	private int y;

	private String msg = "";

	private int selButIndex;

	private Font font;

	public EndGameTipView(L9Screen screen, String msg, int initX, int initY) {
		this.screen = screen;
		this.msg = msg;
		this.x = initX;
		this.y = initY;
		this.init();
		font = Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_BOLD, PubToolKit
				.getFontBig16And24Size());
	}

	public void init() {
		// TODO Auto-generated method stub
		bg = new MyImg("gamepage/endgame/bg.png");
		but0 = new MyImg("paygame/qdbut.png");
	}

	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		bg.drawImage(g, x, y, g.VCENTER | g.HCENTER);
		PubToolKit.drawString(msg, (int)((x - (bg.getWidth() - 20) / 2) *(MainMidlet.scaleX)), (int)((y - 40)*MainMidlet.scaleY), (int)((bg
				.getWidth() - 20)*MainMidlet.scaleX), font, 0x000000, g, false);
		but0.drawRegion(g, 0, selButIndex == 0 ? 0 : but0.getHeight() / 2, but0
				.getWidth(), but0.getHeight() / 2, 0, x, y + 70, g.VCENTER
				| g.HCENTER);

	}

	public void update() {
		this.keyPress();
	}

	public void keyPress() {
		int keyCode = Engine.getKeyCode();
		switch (keyCode) {
		case Engine.K_KEY_BACK:
//		case Engine.K_KEY_NUM0:
			keynum0();
			break;
		case Engine.K_KEY_NUM9:
			break;
		case Engine.K_KEY_LEFT:
			selButIndex = selButIndex == 0 ? 1 : 0;
			break;
		case Engine.K_KEY_RIGHT:
			selButIndex = selButIndex == 0 ? 1 : 0;
			break;
		case Engine.K_KEY_FIRE:
			keyfire();
			break;
		default:
		}
	}

	public void keyfire() {
		if (screen instanceof ShopPageScreen)
			((ShopPageScreen) screen).closeTip();
	}

	public void keynum0() {
		if (screen instanceof ShopPageScreen)
			((ShopPageScreen) screen).closeTip();
	}
}
