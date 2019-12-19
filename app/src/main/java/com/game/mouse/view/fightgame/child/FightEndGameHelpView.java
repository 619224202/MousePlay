package com.game.mouse.view.fightgame.child;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

import com.game.lib9.Engine;
import com.game.lib9.L9Object;
import com.game.mouse.gameinfo.GameInfo;
import com.game.mouse.screen.GamePageScreen;
import com.game.mouse.view.game.EndGameView;
import com.game.mouse.view.game.GameFightView;
import com.game.mouse.view.game.GameMainView;
import com.model.control.MyImg;
import com.model.mainServer.MainServer;
import com.model.tool.PubToolKit;

public class FightEndGameHelpView implements EndGameView {
	private GameFightView gameFightView;

	private MyImg bg;

	private int x;

	private int y;

	public FightEndGameHelpView(GameFightView gameFightView, int initX,
			int initY) {
		this.gameFightView = gameFightView;
		this.x = initX;
		this.y = initY;
		this.init();
	}

	public void init() {
		// TODO Auto-generated method stub
		bg = GameInfo.getFightHelpImg();
	}

	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		bg.drawImage(g, x, y, g.VCENTER | g.HCENTER);

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
		case Engine.K_KEY_FIRE:
			keyfire();
			break;
		case Engine.K_KEY_NUM9:
			keynum9();
			break;
		default:
		}
	}

	public void keyfire() {
		gameFightView.closeHelp();
	}

	public void keynum0() {
		gameFightView.closeHelp();
	}

	public void keynum9() {
		gameFightView.closeHelp();
	}
}
