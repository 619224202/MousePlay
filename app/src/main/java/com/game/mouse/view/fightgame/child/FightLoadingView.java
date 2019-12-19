package com.game.mouse.view.fightgame.child;

import javax.microedition.lcdui.Graphics;

import com.game.lib9.L9Config;
import com.game.lib9.L9Object;
import com.game.mouse.view.View;
import com.game.mouse.view.game.GameFightMainView;
import com.game.mouse.view.game.GameFightView;
import com.model.control.MyImg;

public class FightLoadingView implements View {

	private GameFightMainView gameFightMainView;

	private MyImg bg;

	private MyImg vscard0;

	private MyImg vscard1;

	private L9Object vsObj;

	private int speed = 5;

	private int moveCradX = 0;

	private boolean isEndLoad;

	private int time;

	private long startTime;

	private int showTime = 3;

	private L9Object sprite0;

	private L9Object sprite1;

	public FightLoadingView(GameFightMainView gameFightMainView) {
		this.gameFightMainView = gameFightMainView;
	}

	public void init() {
		bg = new MyImg("gamepage/fight/bg/vs"
				+ gameFightMainView.getPass().getSceneCode() + ".png");
		vscard0 = new MyImg("gamepage/fight/vscard0.png");
		vscard1 = new MyImg("gamepage/fight/vscard1.png");
		vsObj = new L9Object(null, "vs", 2, L9Config.SCR_W / 2,
				L9Config.SCR_H / 2, -1, 0, false, false, true, 0);
		String sprite0res = "fightmouse"
				+ this.gameFightMainView.getUserMouse().getCode() + "0";
		sprite0 = new L9Object(null, sprite0res, 2, -70, 330 + 90, -1, 0, false,
				false, true, 0);
		String sprite1res = "fightcat"
				+ this.gameFightMainView.getGamePageScreen()
						.getIngPkCatSpriteView().getSprite().getCode();
		sprite1 = new L9Object(null, sprite1res, 2, 740 + 105, 330 + 90, -1, 0, false,
				false, true, 0);
		this.startTime = System.currentTimeMillis();
	}

	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		g.fillRect(0, 0, L9Config.SCR_W, L9Config.SCR_H);
		if (bg != null)
			bg.drawImage(g, 0, 0, 0);
		if (vscard0 != null)
			vscard0.drawImage(g, -70 + moveCradX, 270 + 100 - 50, g.VCENTER | g.HCENTER);
		if (vscard1 != null)
			vscard1.drawImage(g, 710 - moveCradX + 630, 270 + 100 - 50, g.VCENTER | g.HCENTER);
		if (sprite0 != null)
			sprite0.paintFrame(g);
		if (sprite1 != null)
			sprite1.paintFrame(g);
		if (vsObj != null)
			vsObj.paintFrame(g);
	}

	public void update() {
		// TODO Auto-generated method stub
		vsObj.doAllFrame();
		sprite0.doAllFrame();
		sprite1.doAllFrame();
		if (moveCradX < 220) {
			speed = speed + 5;
			moveCradX = moveCradX + speed;
		} else {
			addTime();
			if (this.time >= this.showTime) {
				isEndLoad = true;
			}
		}
		sprite0.setPosX(-70 + moveCradX);
		sprite1.setPosX(690 - moveCradX + 550 + 120);
	}

	public boolean isEndLoad() {
		return isEndLoad;
	}

	public void addTime() {
		if (System.currentTimeMillis() - this.startTime >= 1000L) {
			this.time++;
			this.startTime = System.currentTimeMillis();
		}
	}
}
