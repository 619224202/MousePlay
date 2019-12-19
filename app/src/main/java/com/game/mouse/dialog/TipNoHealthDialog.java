package com.game.mouse.dialog;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

import com.game.lib9.Engine;
import com.game.lib9.L9Object;
import com.game.mouse.screen.HomePageScreen;
import com.game.mouse.screen.PassPageScreen;
import com.game.mouse.view.View;
import com.model.control.MyImg;
import com.model.tool.PubToolKit;

public class TipNoHealthDialog implements View {

	protected PassPageScreen passPageScreen;

	protected MyImg bg;

	protected MyImg feedbut;

	protected MyImg moodbut;

	protected int x, y;

	protected int selButIndex = 1;

	private int renewMoodCheese;

	private Font font;

	public TipNoHealthDialog(PassPageScreen passPageScreen, int initX,
			int initY, int renewMoodCheese) {
		this.passPageScreen = passPageScreen;
		this.x = initX;
		this.y = initY;
		this.renewMoodCheese = renewMoodCheese;
		font = Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_BOLD, PubToolKit
				.getFontBig16And24Size());
	}

	public void init() {
		// TODO Auto-generated method stub
		bg = new MyImg("paygame/buybg.png");
		feedbut = new MyImg("passpage/feedbut.png");
		moodbut = new MyImg("passpage/moodbut.png");
	}

	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		bg.drawImage(g, x, y, g.VCENTER | g.HCENTER);
		feedbut.drawRegion(g, 0,
				selButIndex == 0 ? 0 : feedbut.getHeight() / 2, feedbut
						.getWidth(), feedbut.getHeight() / 2, 0, x - 80,
				y + 70, g.VCENTER | g.HCENTER);
		moodbut.drawRegion(g, 0,
				selButIndex == 1 ? 0 : moodbut.getHeight() / 2, moodbut
						.getWidth(), moodbut.getHeight() / 2, 0, x + 65,
				y + 70, g.VCENTER | g.HCENTER);
		paintFont(g);
	}

	public void paintFont(Graphics g) {
		PubToolKit.drawString("您的宠物心情低于30%，您可以去喂养室更换其它角色或使用"
				+ this.renewMoodCheese + "个万能奶酪恢复心情!", x - (bg.getWidth()-40)/2, y - 50, bg.getWidth()-40,
				font, 0x000000, g);
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
			passPageScreen.removeDialog();
			break;
		}
	}

	public void keyfire() {
		switch (selButIndex) {
		case 0:
			passPageScreen.goFeedPage();
			break;
		case 1:
			passPageScreen.renewMood();
			break;
		}
	}

}
