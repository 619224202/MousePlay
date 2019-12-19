package com.game.mouse.view.maingame.child;

import javax.microedition.lcdui.Graphics;

import com.game.mouse.view.game.GameMainView;
import com.model.control.MyImg;
import com.model.tool.PubToolKit;

/**
 * 
 * @author Administrator 雷电障碍物，碰撞后角色被电击 对应编码号53
 * 
 */
public class OrganThunderView extends OrganView {
	private int time;

	private long startTime;

	private int maxShowTime = 10;

	private int maxHiddenTime = 5;

	private MyImg thunderImg;

	private int direction;

	private int imgX, imgY;

	private boolean isOpenOrgan;

	public OrganThunderView(GameMainView gameMainView, String png, String code,
			int direction, int tileW, int tileH, int initX, int initY) {
		super(gameMainView, png, code, tileW, tileH, initX, initY);
		this.direction = direction;
		switch (direction) {
		case 0:
			thunderImg = new MyImg("organ/3/0.png");
			this.imgX = initX - 10 - 30;
			this.imgY = initY - tileH - 15;
			break;
		case 4:
			thunderImg = new MyImg("organ/3/0.png");
			this.imgX = initX - 10 - 30;
			this.imgY = initY - tileH - 15;
			break;
		case 2:
			thunderImg = new MyImg("organ/3/2.png");
			this.imgX = initX - tileW / 2 - 18;
			this.imgY = initY - 20;
			break;
		default:
			thunderImg = new MyImg("organ/3/2.png");
			this.imgX = initX - tileW / 2 - 18;
			this.imgY = initY - 20;
			break;
		}
		this.startTime = System.currentTimeMillis();
		isOpenOrgan = PubToolKit.getRandomInt(2) == 0;
	}

	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		if (isOpenOrgan) {
			super.paint(g);
		}
		thunderImg.drawRegion(g, 0, 0, thunderImg.getWidth(), thunderImg
				.getHeight(), 0, this.imgX, this.imgY, 0);

	}

	public void update() {
		super.update();
		addTime();
		if (isOpenOrgan) {
			if (time >= maxShowTime) {
				this.startTime();
				isOpenOrgan = false;
			}
		} else {
			if (time >= maxHiddenTime) {
				this.startTime();
				isOpenOrgan = true;
			}
		}
	}

	/**
	 * 开始倒计时
	 */
	public void startTime() {
		this.startTime = System.currentTimeMillis();
		time = 0;
	}

	public void addTime() {
		if (System.currentTimeMillis() - this.startTime >= 1000L) {
			this.time++;
			this.startTime = System.currentTimeMillis();
		}
	}

	public void mainGameHit(MainGameL9Object obj) {
		if (isOpenOrgan) {
			super.mainGameHit(obj);
		}
	}
}
