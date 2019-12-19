package com.game.mouse.view.maingame.child;

import com.game.lib9.L9Config;
import com.game.mouse.modle.Cat;
import com.game.mouse.modle.Mouse;
import com.game.mouse.modle.Sprite;
import com.game.mouse.modle.UserMouse;
import com.game.mouse.view.game.GameMainView;

public class MouseSpriteView extends SpriteView {

	private int time;

	private long startTime;

	/**
	 * 最大使用保护罩时间,每3级增加一点保护时间
	 */
	private int useProtectProTime = 5;

	protected boolean isUseProtect;

	private DoorView hitDoorView;

	public MouseSpriteView(GameMainView gameMainView, MapView mapView,
			Mouse mouse, int isClockWise, int direction, int initX, int initY) {
		super(gameMainView, mapView, mouse, isClockWise, direction, 4, initX,
				initY);
		useProtectProTime = ((UserMouse) sprite).getUseProtectProTime();
	}

	public boolean isUseProtect() {
		return isUseProtect;
	}

	public void addTime() {
		if (System.currentTimeMillis() - this.startTime >= 1000L) {
			this.time++;
			this.startTime = System.currentTimeMillis();
		}
	}

	public void useProtect() {
		if (isUseProtect) {
			addTime();
			if (this.time >= useProtectProTime) {
				isUseProtect = false;
				returnNormal();
			}
		}
	}

	public void update() {
		super.update();
		this.useProtect();
		if (hitDoorView != null) {
			if (!getSpriteMainGameL9Object().getColRect().intersects(
					hitDoorView.getColRect())) {
				hitDoorView = null;
			}
		}
	}

	/**
	 * point赋值
	 * 
	 * @param point
	 */
	public void getNextMapPoint(int[] point) {
		this.moveMap(point[2], this.pointMapX);
		this.direction = point[1];
		this.pointMapX = point[2];
		this.pointMapY = point[3];
	}

	/**
	 * 判断地图是否移动，如果需要移动则移动地图
	 * 
	 * @param pointX
	 * @param upPointX
	 */
	public void moveMap(int pointX, int upPointX) {
		if ((this.direction == 0 || this.direction == 4) && pointX > L9Config.SCR_W/2
				&& mapView.getMaxPointX() + spriteW / 2 > L9Config.SCR_W
				&& pointX < mapView.getMaxPointX() + spriteW / 2 - L9Config.SCR_W/2) {
			gameMainView.moveMap(-(pointX - upPointX));
		}
	}

	public void bhit(MainGameL9Object obj) {
		if (obj instanceof SpriteMainGameL9Object && !isdie && flashCount == 0) {
			// 与猫相撞
			SpriteView spriteView = ((SpriteMainGameL9Object) obj)
					.getSpriteView();
			Sprite sprite = spriteView.getSprite();
			if (sprite instanceof Cat && !spriteView.isdie) {
				if ((spriteView.getIsClockWise() == 1 && spriteView
						.getClockWiseObj() == obj)
						|| (spriteView.getIsClockWise() == 0 && spriteView
								.getAntiClockWiseObj() == obj))
					gameMainView.pkCat((CatSpriteView) spriteView);
			}
		} else if (obj instanceof DoorView && this.isshow
				&& hitDoorView == null) {
			DoorView doorView = ((DoorView) obj);
			// int len = doorView.getPosX() - this.getX() > 0 ?
			// doorView.getPosX()
			// - this.getX() : this.getX() - doorView.getPosX();
			// if (len < 20) {
			hitDoorAfter(doorView);
			// }
		}
	}

	/**
	 * 使用保护
	 */
	public void useprotect() {
		int initX = isClockWise == 1 ? clockWiseObj.getPosX()
				: antiClockWiseObj.getPosX();
		int initY = isClockWise == 1 ? clockWiseObj.getPosY()
				: antiClockWiseObj.getPosY();
		this.release();
		String res = this.getSpriteName() + "p" + this.sprite.getCode();
		newSpriteObject(res, initX, initY);
		isUseProtect = true;
		this.startTime = System.currentTimeMillis();
		this.time = 0;
	}

	/**
	 * 恢复正常状态
	 */
	public void returnNormal() {
		int initX = isClockWise == 1 ? clockWiseObj.getPosX()
				: antiClockWiseObj.getPosX();
		int initY = isClockWise == 1 ? clockWiseObj.getPosY()
				: antiClockWiseObj.getPosY();
		this.release();
		String res = this.getSpriteName() + this.sprite.getCode();
		newSpriteObject(res, initX, initY);
	}

	/**
	 * 死亡动画后提示复活
	 */
	public void dieAnimationEnd() {
		dieAnimationEnd = true;
		dieEffectObj.isOver = true;
		dieEffectObj = null;
		gameMainView.tipReviveMouse();
	}

	/**
	 * 老鼠与门碰撞后执行
	 * 
	 * @param doorView
	 */
	public void hitDoorAfter(DoorView doorView) {
		hitDoorView = doorView;
		this.state = 0;
		if (gameMainView.starNum < 3) {
			gameMainView.tipPassDoor();
		} else {
			passDoor();
		}
	}

	/**
	 * 老鼠走进门
	 */
	public void passDoor() {
		hitDoorView.changePassDoorAnim(this.isClockWise, this.getDirection());
		this.isshow = false;
		clockWiseObj.setPosX(hitDoorView.getPosX());
		clockWiseObj.setPosY(hitDoorView.getPosY());
		antiClockWiseObj.setPosX(hitDoorView.getPosX());
		antiClockWiseObj.setPosY(hitDoorView.getPosY());
	}

	/**
	 * 不通过洞门继续游戏
	 */
	public void noPassDoor() {
		this.state = 1;
		this.hitArrowOrgan();
	}

	public void die() {
		super.die();
		gameMainView.stopAll();
	}

	public void revive() {
		super.revive();
		gameMainView.startAll();
	}
}
