package com.game.mouse.view.fightgame.child;

import java.util.Hashtable;

import javax.microedition.lcdui.Graphics;

import com.game.lib9.L9Config;
import com.game.mouse.view.View;

public class FightSkillView extends FightGameL9Object implements View {
	protected FightSpriteView fightSpriteView;

	protected int x, y;

	protected Hashtable hitObjs = new Hashtable();

	public FightSkillView(FightSpriteView fightSpriteView, String res,
			int loopOffSet, int animation, int initX, int initY) {
		super(res, 32, initX, initY, loopOffSet, 0, false, true, true, 2, 0);
		this.fightSpriteView = fightSpriteView;
		this.x = initX;
		this.y = initY;
		this.init();
	}

	public FightSkillView(FightSpriteView fightSpriteView, String res,
			int initX, int initY) {
		this(fightSpriteView, res, 0, 0, initX, initY);
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
