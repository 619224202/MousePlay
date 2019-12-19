package com.game.mouse.view.maingame.child;

import com.game.mouse.view.game.GameMainView;

/**
 * @author Administrator 上下移动，左右移动的 对应地图障碍物编号为51，是左右移动的 52是上下移动的
 * 
 */
public class OrganMoveView extends OrganView {
	/**
	 * 0-上下移动 1-左右移动
	 */
	protected int moveType = 0;

	protected int minPoint, maxPoint;

	protected int speed = 10;

	protected boolean moveToMin;

	protected int x, y;

	private boolean isStop;

	public OrganMoveView(GameMainView gameMainView, String png, String code,
			int tileW, int tileH, int initX, int initY, int moveType,
			int minPoint, int maxPoint) {
		super(gameMainView, png, code, tileW, tileH, initX, initY);
		this.moveType = moveType;
		this.minPoint = minPoint;
		this.maxPoint = maxPoint;
		this.x = initX;
		this.y = initY;
	}

	public void update() {
		super.update();
		if (!isStop) {
			this.move();
		}
		this.setPosX(this.x);
		this.setPosY(this.y);
	}

	public void move() {
		if (this.moveType == 1) {
			if (moveToMin) {	//左右移动
				this.moveRightToLeft();
			} else {
				this.moveLeftToRight();
			}
		} else {	//上下移动
			if (moveToMin) {
				this.moveBottomToUp();
			} else {
				this.moveUpToBottom();
			}
		}
	}

	/**
	 * 从左边移动到右边
	 */
	public void moveLeftToRight() {
		if (this.x + speed >= maxPoint) {
			this.x = maxPoint;
			moveToMin = true;
		} else {
			this.x = this.x + speed;
		}
	}

	/**
	 * 从右边移动到左边
	 */
	public void moveRightToLeft() {
		if (this.x - speed <= minPoint) {
			this.x = minPoint;
			moveToMin = false;
		} else {
			this.x = this.x - speed;
		}
	}

	/**
	 * 从上方移到到下方
	 */
	public void moveUpToBottom() {
		if (this.y + speed >= maxPoint) {
			this.y = maxPoint;
			moveToMin = true;
		} else {
			this.y = this.y + speed;
		}
	}

	/**
	 * 从右边移动到左边
	 */
	public void moveBottomToUp() {
		if (this.y - speed <= minPoint) {
			this.y = minPoint;
			moveToMin = false;
		} else {
			this.y = this.y - speed;
		}
	}

	public void setStop(boolean isStop) {
		this.isStop = isStop;
	}
}
