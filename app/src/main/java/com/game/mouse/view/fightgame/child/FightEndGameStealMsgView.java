package com.game.mouse.view.fightgame.child;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

import com.game.lib9.Engine;
import com.game.lib9.L9Object;
import com.game.mouse.screen.GamePageScreen;
import com.game.mouse.view.game.EndGameView;
import com.game.mouse.view.game.GameFightView;
import com.game.mouse.view.game.GameMainView;
import com.model.control.MyImg;
import com.model.mainServer.MainServer;
import com.model.tool.PubToolKit;

public class FightEndGameStealMsgView implements EndGameView {
	private GameFightView gameFightView;

	private MyImg bg;

	private MyImg but0;

	private MyImg but1;

	private int x;

	private int y;

	private String stealMsg = "";

	private int selButIndex;

	private boolean isBigSteal;

	private Font font;

	public FightEndGameStealMsgView(GameFightView gameFightView,
			boolean isBigSteal, int stealNum, int initX, int initY) {
		this.gameFightView = gameFightView;
		this.isBigSteal = isBigSteal;
		this.x = initX;
		this.y = initY;
		if (stealNum == 0) {
			stealMsg = "很遗憾您没有偷取到任何奶酪!";
		} else {
			stealMsg = "您运气真好，恭喜您偷取到" + stealNum + "个奶酪!";
		}
		this.init();
		font = Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_BOLD, PubToolKit
				.getFontBig16And24Size());
	}

	public void init() {
		// TODO Auto-generated method stub
		bg = new MyImg("gamepage/endgame/bg.png");
		but0 = new MyImg("gamepage/endgame/stealbut0.png");
		but1 = new MyImg("gamepage/endgame/stealbut1.png");
	}

	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		bg.drawImage(g, x, y, g.VCENTER | g.HCENTER);
		PubToolKit.drawString(stealMsg, x - (bg.getWidth() - 40) / 2, y - 40,
				bg.getWidth() - 40, font, 0x000000, g);
		but0.drawRegion(g, 0, selButIndex == 0 ? 0 : but0.getHeight() / 2, but0
				.getWidth(), but0.getHeight() / 2, 0, x - 65 - 40, y + 110 + 50, g.VCENTER
				| g.HCENTER);
		but1.drawRegion(g, 0, selButIndex == 1 ? 0 : but1.getHeight() / 2, but1
				.getWidth(), but1.getHeight() / 2, 0, x + 65 + 40, y + 110 + 50, g.VCENTER
				| g.HCENTER);
	}

	public void update() {

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
		switch (selButIndex) {
		case 0:
			gameFightView.goToGameMain(true);
			break;
		case 1:
			gameFightView.closeStealMsg(isBigSteal);
			break;
		}

	}

	public void keynum0() {
		gameFightView.goToGameMain(true);
	}
}
