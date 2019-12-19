package com.game.mouse.view.maingame.child;

import javax.microedition.lcdui.Graphics;

import com.game.lib9.L9Object;
import com.game.mouse.modle.Cat;
import com.game.mouse.modle.Mouse;
import com.game.mouse.modle.Sprite;
import com.game.mouse.view.View;
import com.game.mouse.view.game.GameMainView;
import com.game.mouse.view.maingame.child.SpriteView.SpriteMainGameL9Object;
import com.model.control.MyImg;
import com.model.tool.MathFP;
import com.model.tool.PubToolKit;
import com.studio.motionwelder.MSprite;

/**
 * 迟到奶酪后效果
 * 
 * @author Administrator
 * 
 */
public class CheeseAddView implements View {
	private GameMainView gameMainView;

	private L9Object cheese;

	private int x, y;

	private int initX, initY;

	private int toX, toY;

	private int speed = 20;

	private int radians;

	public CheeseAddView(GameMainView gameMainView, int initX, int initY,
			int toX, int toY) {
		gameMainView.starNum++;
		this.gameMainView = gameMainView;
		this.x = this.initX = initX;
		this.y = this.initY = initY;
		this.toX = toX;
		this.toY = toY;
		radians = MathFP.atan(MathFP.div(MathFP.toFP(toY - initY), MathFP
				.toFP(toX - initX)));
		this.init();
	}

	public void init() {
		// TODO Auto-generated method stub
		cheese = new MainGameL9Object("cheese0", 2, initX, initY, -1, 0, false,
				false, true, 2, 0);
		cheese.setNextFrameTime(2);

	}

	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		cheese.paintFrame(g);
	}

	public void update() {
		// TODO Auto-generated method stub
		speed++;
		int deltaX = MathFP.toInt(MathFP.mul(MathFP.toFP(speed), MathFP
				.cos(radians)));
		int deltaY = MathFP.toInt(MathFP.mul(MathFP.toFP(speed), MathFP
				.sin(radians)));
		this.x = this.x + deltaX;
		this.y = this.y + deltaY;
		cheese.setPosX(this.x);
		cheese.setPosY(this.y);
		cheese.doAllFrame();
		if (this.x > this.toX || this.y < this.toY) {
			this.endAmin();
		}
	}

	public void endAmin() {
		gameMainView.drawStarNum++;
		gameMainView.passGetCheeseNum++;
		gameMainView.removeOtherViews(this);
	}
}
