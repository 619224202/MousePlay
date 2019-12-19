package com.game.mouse.view.fightgame.child;

import java.util.Hashtable;

import javax.microedition.lcdui.Graphics;

import com.game.lib9.L9Config;
import com.game.mouse.view.View;
import com.game.mouse.view.game.GameFightView;

public class FightAddHpView extends FightGameL9Object implements View {
	private FightSpriteView fightSpriteView;

	private int x, y;

	public FightAddHpView(FightSpriteView fightSpriteView, int initX, int initY) {
		super("addhp", 2, initX, initY, 0, 0, false, true, true, 2, 0);
		this.setNextFrameTime(3);
		this.fightSpriteView = fightSpriteView;
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
		fightSpriteView.endHp(this);
	}
}
