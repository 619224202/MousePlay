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

public class FightEndGameRunAwayView implements EndGameView {
	private GameFightView gameFightView;

	private MyImg bg;

	private MyImg but0;

	private MyImg but1;

	private int x;

	private int y;

	private String msg = "";

	private int selButIndex;

	private Font font;

	private boolean isRunAway;

	public FightEndGameRunAwayView(GameFightView gameFightView,
			boolean isRunAway, int initX, int initY) {
		this.gameFightView = gameFightView;
		this.isRunAway = isRunAway;
		this.selButIndex = this.isRunAway ? 1 : 0;
		this.x = initX;
		this.y = initY;
		if (isRunAway) {
			this.msg = "从猫的口中逃脱，需要消耗一点体力、无法获得猫身上的奶酪和积分，您确认要脱离战斗？";
		} else {
			this.msg = "很遗憾您已经被猫死死的逮住，无法脱离战斗!";
		}

		this.init();
		font = Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_BOLD, PubToolKit
				.getFontBig16And24Size());
	}

	public void init() {
		// TODO Auto-generated method stub
		bg = new MyImg("gamepage/endgame/bg.png");
		but0 = new MyImg("paygame/qdbut.png");
		but1 = new MyImg("paygame/qxbut.png");
	}

	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		bg.drawImage(g, x, y, g.VCENTER | g.HCENTER);
		PubToolKit.drawString(msg, x - (bg.getWidth()-40)/2, y - 40, bg.getWidth()-40, font, 0x000000, g);
		if (isRunAway) {
			but0.drawRegion(g, 0, selButIndex == 0 ? 0 : but0.getHeight() / 2,
					but0.getWidth(), but0.getHeight() / 2, 0, x - 65, y + 70,
					g.VCENTER | g.HCENTER);
			but1.drawRegion(g, 0, selButIndex == 1 ? 0 : but1.getHeight() / 2,
					but1.getWidth(), but1.getHeight() / 2, 0, x + 65, y + 70,
					g.VCENTER | g.HCENTER);
		} else {
			but0.drawRegion(g, 0, selButIndex == 0 ? 0 : but0.getHeight() / 2,
					but0.getWidth(), but0.getHeight() / 2, 0, x, y + 70,
					g.VCENTER | g.HCENTER);
		}
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
		if (isRunAway) {
			switch (selButIndex) {
			case 0:
				gameFightView.goToGameMain(false);
				break;
			case 1:
				gameFightView.closeRunAway();
				break;
			}
		} else {
			gameFightView.closeRunAway();
		}
	}

	public void keynum0() {
		gameFightView.closeRunAway();
	}
}
