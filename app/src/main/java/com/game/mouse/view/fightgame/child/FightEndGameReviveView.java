package com.game.mouse.view.fightgame.child;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

import com.game.lib9.Engine;
import com.game.lib9.L9Object;
import com.game.mouse.context.Config;
import com.game.mouse.gameinfo.GameInfo;
import com.game.mouse.screen.GamePageScreen;
import com.game.mouse.view.game.EndGameView;
import com.game.mouse.view.game.GameFightView;
import com.game.mouse.view.game.GameMainView;
import com.model.control.MyImg;
import com.model.mainServer.MainServer;
import com.model.tool.PubToolKit;

public class FightEndGameReviveView implements EndGameView {
	private GameFightView gameFightView;

	private MyImg bg;

	private MyImg but;

	private MyImg butback;

	private int x;

	private int y;

	private int butW, butH;

	private int selButIndex;

	private FightSpriteMouseView fightSpriteMouseView;

	private StringBuffer reviveMsg = new StringBuffer();

	private Font font;

	public FightEndGameReviveView(GameFightView gameFightView,
			FightSpriteMouseView fightSpriteMouseView, int initX, int initY) {
		this.gameFightView = gameFightView;
		this.fightSpriteMouseView = fightSpriteMouseView;
		String priceName = MainServer.getInstance().getPriceName();
		if (fightSpriteMouseView == null) {
			reviveMsg.append(gameFightView.getSpriteMouseView().getSprite()
					.getName());
			reviveMsg.append("、"
					+ gameFightView.getRecruitingSpriteMouseView().getSprite()
							.getName());
			if (Config.isTipMoney) {
				reviveMsg.append("已经死亡,是否花费" + gameFightView.props[4][2] + ""
						+ priceName + "复活他们继续战斗?");
			} else {
				reviveMsg.append("已经死亡,是否复活他们继续战斗?");
			}

		} else {
			reviveMsg.append(fightSpriteMouseView.getSprite().getName());
			if (Config.isTipMoney) {
				reviveMsg.append("已经死亡,是否花费" + gameFightView.props[3][2] + ""
						+ priceName + "复活继续战斗?");
			} else {
				reviveMsg.append("已经死亡,是否复活继续战斗?");
			}
		}
		this.x = initX;
		this.y = initY;
		this.init();
		font = Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_BOLD, PubToolKit
				.getFontBig16And24Size());
	}

	public void init() {
		// TODO Auto-generated method stub
		bg = new MyImg("gamepage/endgame/bg.png");
		but = new MyImg("gamepage/endgame/revivebut0.png");
		butW = but.getWidth();
		butH = but.getHeight() / 2;
		butback = GameInfo.getReviveButImg();
	}

	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		bg.drawImage(g, x, y, g.VCENTER | g.HCENTER);
		PubToolKit.drawString(reviveMsg.toString(), x - (bg.getWidth() - 40)
				/ 2, y - 40, bg.getWidth() - 40, font, 0x000000, g);
		but.drawRegion(g, 0, butH, butW, butH, 0, x, y + 80, g.VCENTER
						| g.HCENTER);
		butback.drawImage(g, x, y + 110, g.VCENTER | g.HCENTER);
	}

	public void update() {
		// TODO Auto-generated method stub
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
		if (fightSpriteMouseView == null) {
			gameFightView.toBuyReviveAllMouse();
		} else {
			gameFightView.toBuyReviveMouse(fightSpriteMouseView);
		}
	}

	public void keynum0() {
		if (fightSpriteMouseView == null) {
			gameFightView.goToGameMain(false);
		} else {
			gameFightView.closeRevive();
		}
	}
}
