package com.game.mouse.view.fightgame.child;

import javax.microedition.lcdui.Graphics;

import com.game.lib9.Engine;
import com.game.lib9.L9Config;
import com.game.lib9.L9Object;
import com.game.mouse.view.View;
import com.game.mouse.view.game.GameFightView;
import com.model.control.MyImg;
import com.model.tool.PubToolKit;

public class FightTimeCountView implements View {
	private GameFightView gameFightView;

	private int time;

	private long startTime;

	private int maxTime = 10;

	private MyImg num;

	private boolean isShowTime;

	private boolean userRound;

	private boolean isResponseRound;

	public FightTimeCountView(GameFightView gameFightView, boolean userRound) {
		// TODO Auto-generated constructor stub
		this.gameFightView = gameFightView;
		this.userRound = userRound;
		this.maxTime = userRound ? 10 : 1;
	}

	public void init() {
		// TODO Auto-generated method stub
		num = new MyImg("gamepage/fight/num.png");
	}

	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		if (userRound && isShowTime) {
			PubToolKit.drawNum(g, maxTime - time, this.num.getImg(),
					L9Config.SCR_W / 2, L9Config.SCR_H / 2,  0, g.VCENTER
							| g.HCENTER, true);
		}
	}

	public void update() {
		if (isShowTime) {
			// TODO Auto-generated method stub
			addTime();
			endTime();
		}
	}

	/**
	 * 开始倒计时
	 */
	public void startTime() {
		this.startTime = System.currentTimeMillis();
		time = 0;
		this.isShowTime = true;
	}

	public void addTime() {
		if (System.currentTimeMillis() - this.startTime >= 1000L) {
			this.time++;
			this.startTime = System.currentTimeMillis();
		}
	}

	/**
	 * 倒计时结束
	 */
	public void endTime() {
		if (this.time >= this.maxTime) {
			if ((userRound && this.gameFightView.getSpriteCatView().getState() == 0)
					|| (!userRound && (this.gameFightView.getSpriteMouseView()
							.getState() == 0 || this.gameFightView
							.getRecruitingSpriteMouseView().getState() == 0))) {
				responseRound();
			}
		}
	}

	/**
	 * 响应用户回合
	 * 
	 * @param index
	 */
	public void responseRound() {
		isShowTime = false;
		if (!isResponseRound) {
			isResponseRound = true;
			if (userRound) {
				this.gameFightView.userRespondUserRound(gameFightView
						.getSelButIndex(), false, true);
			} else {
				int randnum = gameFightView.getSpriteCatView().getSprite()
						.getLv() * 8;
				if (PubToolKit.getRandomInt(100) < randnum) {
					this.gameFightView.respondNpcRound(2);
				} else {
					this.gameFightView.respondNpcRound(0);
				}
			}
		}
	}

	public boolean isResponseRound() {
		return isResponseRound;
	}

	public void setResponseRound(boolean isResponseRound) {
		this.isResponseRound = isResponseRound;
	}

	public void stopTime() {
		isShowTime = false;
	}

	public void reviveTime() {
		isShowTime = true;
	}
}
