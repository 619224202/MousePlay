package com.game.mouse.view.fightgame.child;

import java.util.Hashtable;

import javax.microedition.lcdui.Graphics;

import com.game.lib9.L9Config;
import com.game.mouse.view.View;
import com.game.mouse.view.game.GameFightView;

public class FightUpLvView extends FightGameL9Object implements View {
	private GameFightView gameFightView;

	private int x, y;

	public FightUpLvView(GameFightView gameFightView, int initX, int initY) {
		super("uplv", 2, initX, initY, 0, 0, false, true, true, 2, 0);
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
		this.paintFrame(g);
	}

	public void update() {
		// TODO Auto-generated method stub

	}

	public void endAnimation() {
		this.isOver = true;
		gameFightView.endUpWeaponEff(this);
	}
}
