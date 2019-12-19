package com.game.mouse.view.maingame.child;

import com.game.mouse.modle.Cat;
import com.game.mouse.view.game.GameMainView;
import com.model.tool.PubToolKit;

/**
 * 追着老鼠跑的猫 该种猫可以上下跳跃、可以加速
 * 
 * @author Administrator
 * 
 */
public class CatSpriteChaseView extends CatSpriteView {
	private int time;

	private long startTime;

	private int maxTime = 3;

	private boolean isAddSpeed;

	private boolean isRecordTime;

	public CatSpriteChaseView(GameMainView gameMainView, MapView mapView,
			Cat cat, int isClockWise, int direction, int initX, int initY) {
		super(gameMainView, mapView, cat, isClockWise, direction, initX, initY);
		// TODO Auto-generated constructor stub
	}

	public void update() {
		if (isRecordTime) {
			addTime();
			if (time >= maxTime) {
				jumpChaseMouse();
			}
		}
		super.update();
	}

	/**
	 * 跳跃追踪老鼠
	 */
	public void jumpChaseMouse() {
		int val0 = mapView.getPointValue(this.pointMapX, this.pointMapY);
		int val1 = mapView.getPointValue(this.getGameMainView().getSpriteView()
				.getPointMapX(), this.getGameMainView().getSpriteView()
				.getPointMapY());
		if ((this.direction == 0 || this.direction == 4)
				&& (this.getGameMainView().getSpriteView().getDirection() == 0 || this
						.getGameMainView().getSpriteView().getDirection() == 4)
				&& val0 != 2 && val1 != 2) {
			boolean isTwoPointWay = mapView.isTwoPointWay(this.direction,
					this.pointMapX, this.pointMapY, this.getGameMainView()
							.getSpriteView().getPointMapX(), this
							.getGameMainView().getSpriteView().getPointMapY());
			if (isTwoPointWay) {
				if (PubToolKit.getRandomInt(2) == 0 && !isAddSpeed) {
					// 如果在同一直线加速二分之一的概率做加减追赶
					this.speed = this.speed + 5;
					isAddSpeed = true;
				}
			} else {
				if ((this.getGameMainView().getSpriteView().getY() > this
						.getY() && this.getDirection() == 4)
						|| (this.getGameMainView().getSpriteView().getY() < this
								.getY() && this.getDirection() == 0)) {
					// 是否需要跳跃，如果老鼠在上方，需要跳跃到上方，如果在下方需要跳跃到下方
					this.jump();
					isTwoPointWay = mapView.isTwoPointWay(this.direction,
							this.pointMapX, this.pointMapY, this
									.getGameMainView().getSpriteView()
									.getPointMapX(), this.getGameMainView()
									.getSpriteView().getPointMapY());
					if (isTwoPointWay && !isAddSpeed) {
						this.speed = this.speed + 5;
						isAddSpeed = true;
					} else if (isAddSpeed) {
						isAddSpeed = false;
						this.speed = this.speed - 5;
					}
				}
			}
		}
		startTime();
	}

	/**
	 * 开始倒计时
	 */
	public void startTime() {
		this.startTime = System.currentTimeMillis();
		time = 0;
		isRecordTime = true;
		maxTime = PubToolKit.getRandomInt(2) + 2;
	}

	public void addTime() {
		if (System.currentTimeMillis() - this.startTime >= 1000L) {
			this.time++;
			this.startTime = System.currentTimeMillis();
		}
	}

	public void startMove() {
		super.startMove();
		this.startTime();
	}

	public void stopMove() {
		super.stopMove();
		this.isRecordTime = false;
	}

}
