package com.game.mouse.view.fightgame.child;

import java.util.Hashtable;

import javax.microedition.lcdui.Graphics;

import com.game.lib9.L9Config;
import com.game.mouse.context.Config;
import com.game.mouse.gameinfo.Props;
import com.game.mouse.modle.Pass;
import com.game.mouse.view.View;
import com.game.mouse.view.game.GameFightView;
import com.model.tool.PubToolKit;

public class FightHandView implements View {
	private GameFightView gameFightView;

	private int x, y;

	private FightGameL9Object fightGameL9Object;

	private boolean isBigSteal;

	public FightHandView(GameFightView gameFightView, boolean isBigSteal,
			int initX, int initY) {
		this.isBigSteal = isBigSteal;
		String res = isBigSteal ? "gamepage/fight/hand0.png"
				: "gamepage/fight/hand1.png";
		fightGameL9Object = new FightGameL9Object(2, res, 84, 69, initX, initY,
				8, false, false, true, 2) {
			public void endAnimation() {
				endAnim();
			}
		};
		fightGameL9Object.setNextFrameTime(3);
		this.gameFightView = gameFightView;
		this.x = initX;
		this.y = initY;
		this.init();
	}

	public void init() {
		// TODO Auto-generated method stub
	}

	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		fightGameL9Object.paintFrame(g);
	}

	public void update() {
		// TODO Auto-generated method stub
		fightGameL9Object.doAllFrame();
	}

	public void endAnim() {
		int stealNum = 0;
		Pass pass = gameFightView.getPass();
		String[] propUncheese0 = Props.getIntance().getPricePropByCode(
				"uncheese0");
		String[] propUncheese1 = Props.getIntance().getPricePropByCode(
				"uncheese1");
		String[] propUncheese2 = Props.getIntance().getPricePropByCode(
				"uncheese2");
		if (!isBigSteal) {
			String[] propUncheese = null;
			if (Config.stealNumType == 2) {
				propUncheese = propUncheese1;
			} else if (Config.stealNumType == 1) {
				propUncheese = propUncheese0;
			} else {
				propUncheese = propUncheese0;
			}
			stealNum = Integer.parseInt(propUncheese[3]) + 2
					+ PubToolKit.getRandomInt(pass.getSceneCode() + 1);
		} else if (isBigSteal) {
			String[] propUncheese = null;
			if (Config.stealNumType == 2) {
				propUncheese = propUncheese2;
			} else if (Config.stealNumType == 1) {
				propUncheese = propUncheese2;
			} else {
				propUncheese = propUncheese1;
			}
			stealNum = Integer.parseInt(propUncheese[3]) + 2
					+ PubToolKit.getRandomInt(pass.getSceneCode() + 1);
		}
		gameFightView.removeGameBeforeView(this);
		gameFightView.tipStealMsg(isBigSteal, stealNum);
	}
}
