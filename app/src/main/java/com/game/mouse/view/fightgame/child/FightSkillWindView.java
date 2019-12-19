package com.game.mouse.view.fightgame.child;

import java.util.Hashtable;

import javax.microedition.lcdui.Graphics;

import com.game.lib9.L9Config;
import com.game.mouse.view.View;

public class FightSkillWindView extends FightSkillView {
	private int speed = 30;

	private boolean isMoveLeft;

	public FightSkillWindView(FightSpriteView fightSpriteView, String res,
			int initX, int initY, boolean isMoveLeft) {
		super(fightSpriteView, res, -1, 0, initX, initY);
		this.isMoveLeft = isMoveLeft;
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
		if (isMoveLeft) {
			this.x = this.x - speed;
			this.setPosX(this.x);
			if (this.x < 0) {
				this.endAnimation();
			}
		} else {
			this.x = this.x + speed;
			this.setPosX(this.x);
			if (this.x > L9Config.SCR_W) {
				this.endAnimation();
			}
		}
	}

	public void endAnimation() {
		this.isOver = true;
		fightSpriteView.endSkill(this);
	}

	public void addHitObj(Object obj) {
		hitObjs.put(obj.toString(), obj);
	}

	/**
	 * 
	 * @param obj
	 * @return
	 */
	public boolean isContainsHitObj(Object obj) {
		return hitObjs.contains(obj);
	}

	public FightSpriteView getFightSpriteView() {
		return fightSpriteView;
	}

}
