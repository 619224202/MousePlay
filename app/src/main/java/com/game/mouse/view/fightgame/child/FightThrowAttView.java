package com.game.mouse.view.fightgame.child;

import javax.microedition.lcdui.Graphics;

import com.game.lib9.L9Object;
import com.game.mouse.view.View;
import com.game.mouse.view.game.GameFightView;

public class FightThrowAttView extends FightGameL9Object implements View {
	private String code;

	private GameFightView gameFightView;

	private int initX, initY;

	private boolean isEnd;

	private int speed = 20;

	public FightThrowAttView(GameFightView gameFightView, int initX, int initY) {
		super("cheese0", 32, initX, initY, -1, 0, false, true, true, 2, 0);
		this.setNextFrameTime(2);
		this.gameFightView = gameFightView;
		this.initX = initX;
		this.initY = initY;
	}

	public void init() {

	}

	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		this.paintFrame(g);
	}

	public void update() {
		// TODO Auto-generated method stub
		if (this.isEnd) {
			if (this.getCurrentFrame() >= this.getFrameCount() - 1) {
				this.endAnimation();
			}
		} else {
			this.setPosX(this.getPosX() + speed);
		}
	}

	public void endAnimation() {
		this.isOver = true;
		gameFightView.removeGameBeforeView(this);
	}

	public void fightGameHit(FightGameL9Object obj) {
		System.out.println("**************");
		this.setAnimation(1);
		this.isEnd = true;
	}
}
