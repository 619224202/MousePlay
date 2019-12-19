package com.game.mouse.view.maingame.child;

import com.game.mouse.modle.Cat;
import com.game.mouse.view.game.GameMainView;

/**
 * 在直线上来回走动的猫
 * 
 * @author Administrator
 * 
 */
public class CatSpriteLineMoveView extends CatSpriteView {

	public CatSpriteLineMoveView(GameMainView gameMainView, MapView mapView, Cat cat,
			int isClockWise, int direction, int initX, int initY) {
		super(gameMainView, mapView, cat, isClockWise, direction, initX, initY);
		// TODO Auto-generated constructor stub
	}

	/**
	 * point赋值
	 * 
	 * @param point
	 */
	public void getNextMapPoint(int[] point) {
		if (this.direction != point[1]) {
			this.isClockWise = this.isClockWise == 1 ? 0 : 1;
			this.direction = (this.direction == 2 ? 6 : this.direction == 6 ? 2
					: this.direction);
			if (this.direction == 2 || this.direction == 6) {
				if (isClockWise == 1) {
					clockWiseObj.setAnimation(this.direction);
				} else {
					antiClockWiseObj.setAnimation(this.direction);
				}
			}
		} else {
			super.getNextMapPoint(point);
		}
	}

	/**
	 * 移动方式
	 */
	public void moveType() {
		int[] point = this.mapView.moveToPointByDirection(this.spriteW / 2,
				this.isClockWise, direction, this.pointMapX, this.pointMapY,
				speed);
		getNextMapPoint(point);
		changePointMapToPoint();
	}
}
