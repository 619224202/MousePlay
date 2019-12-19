package com.game.mouse.view.fightgame.child;

import javax.microedition.lcdui.Graphics;

import com.game.mouse.view.View;
import com.model.control.MyImg;
import com.model.tool.PubToolKit;

public class FightLoseBloodView implements View {
	private MyImg num;

	private FightSpriteView fightSpriteView;

	private int x, y;

	private int loseNum;

	private int speed = 10;

	private int movelength;

	public FightLoseBloodView(FightSpriteView fightSpriteView, int initX,
			int initY, int loseNum) {
		this.fightSpriteView = fightSpriteView;
		this.x = initX;
		this.y = initY;
		this.loseNum = loseNum;
		this.init();
	}

	public void init() {
		// TODO Auto-generated method stub
		num = new MyImg("gamepage/fight/num2.png");
	}

	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		PubToolKit.drawString(g, this.num.getImg(), "-" + loseNum,
				"-0123456789", x, y, 17, 24, g.VCENTER | g.HCENTER, 0, 2, -1);
	}

	public void update() {
		// TODO Auto-generated method stub
		movelength += speed;
		this.y = this.y - speed;
		if (movelength > 100) {
			this.end();
		}
	}

	public void end() {
		fightSpriteView.destrySpriteBeforeView(this);
	}
}
