package com.game.mouse.view.maingame.child;

import com.game.mouse.context.GameConfigInfo;
import com.game.mouse.screen.MainMidlet;
import com.game.mouse.view.game.GameMainView;
import com.model.tool.MathFP;

/**
 * @author Administrator 上下移动，左右移动的 对应地图障碍物编号为51，是左右移动的 52是上下移动的
 */
public class OrganMoveSpriteMoveView extends OrganMoveView {
	private int width, height;

	public OrganMoveSpriteMoveView(GameMainView gameMainView, String code, int tileW, int tileH, int initX, int initY, int moveType, int minPoint, int maxPoint) {
		super(gameMainView, "organ/4/" + gameMainView.getMapView().getScene() + ".png", code, tileW, tileH, initX, initY, moveType, minPoint, maxPoint);
		this.width = tileW;
		this.height = tileH;
		this.speed = 6;
	}

	/**
	 * 判断是否可以跳跃到移到的障碍物上
	 * @param spriteView
	 * @return
	 */
	public int[] isjump(SpriteView spriteView) {
		int[] jump = { 0, 0, 0, 0 };
		int thisPointY = this.y - gameMainView.getMapView().getHeaderHight() - height;
		int length = thisPointY - spriteView.getPointMapY();
		if ((spriteView.getDirection() == 4 && length == gameMainView.getMapView().getImgH() * 4) || (spriteView.getDirection() == 0 && length == -gameMainView
						.getMapView().getImgH() * 6)) {
			if (this.moveType == 1) {
				if (spriteView.getX() - (this.x - this.width / 2 - spriteView.spriteW / 2) > 0 && (this.x + this.width / 2 + spriteView.spriteW / 2) - spriteView.getX() > 0) {
					jump[0] = 1;
					jump[1] = spriteView.getDirection() == 0 ? 4 : 0;
					jump[2] = spriteView.getX() - gameMainView.getMapView().getHeaderWidth();
					jump[3] = ((jump[1] == 0 ? this.y - this.height : this.y) - gameMainView.getMapView().getHeaderHight());
				}
			}
		}
		return jump;
	}

	public void mainGameHit(MainGameL9Object obj) {

	}

	/**
	 * @param direction
	 * @param pointX
	 * @param pointY
	 * @return nextPointX,nextPointY,direction
	 */
	public int[] moveToPointByDirection(int spriteW, int isClockWise,
			int direction, int pointX, int pointY, int speed) {
//		float f_spriteW = spriteW* MainMidlet.scaleX;
//		float f_pointX = pointX*MainMidlet.scaleX;
//		float f_pointY = pointY*MainMidlet.scaleY;
		int[] ret = new int[4];
		System.out.println(" ----- isClockWise = " + isClockWise);
		if (isClockWise == 1) {
			int[] retTmp = moveToPointClockWise(spriteW, direction, pointX, pointY, speed);
//			int[] retTmp = moveToPointClockWise((int)f_spriteW, direction, (int)f_pointX, (int)f_pointY, speed);
			ret[0] = 1;
			ret[1] = retTmp[0];
			ret[2] = retTmp[1];
			ret[3] = retTmp[2];
		} else {
			int[] retTmp = moveToPointAntiClockWise(spriteW, direction, pointX, pointY, speed);
//			int[] retTmp = moveToPointClockWise((int)f_spriteW, direction, (int)f_pointX, (int)f_pointY, speed);
			ret[0] = 0;
			ret[1] = retTmp[0];
			ret[2] = retTmp[1];
			ret[3] = retTmp[2];
		}
		return ret;
	}

	/**
	 * 顺时针 根据角色的x值和y值，当前走动方向，得到下一次的方向 返回下一帧的方向坐标
	 * @param direction
	 * @param pointX
	 * @param pointY
	 * @param spriteSpeed
	 * @return
	 */
	public int[] moveToPointClockWise(int spriteW, int direction, int pointX, int pointY, int spriteSpeed) {
		int thisPointY = this.y - gameMainView.getMapView().getHeaderHight() - height;
		int maxPointX = this.x + this.width / 2;
		int minPointX = this.x - this.width / 2;
		int minPointY = thisPointY;
		int maxPointY = thisPointY + 2 * gameMainView.getMapView().getImgH();
		int angleW = 10, angleH = 10;
		int[] ret = new int[3];
		int lastWidth = 0;
		int radians = MathFP.div(MathFP.mul(MathFP.toFP(45), MathFP.PI), MathFP.toFP(180));
		int deltaX, deltaY;
		int tmpspeed = 0;
		switch (direction) {
		case 0:
			if (this.moveToMin) {
				tmpspeed = spriteSpeed - this.speed > GameConfigInfo.minspeed ? this.speed
						+ (spriteSpeed - this.speed)
						: this.speed + GameConfigInfo.minspeed;
			} else {
				tmpspeed = spriteSpeed + this.speed < GameConfigInfo.maxspeed ? spriteSpeed
						+ this.speed
						: GameConfigInfo.maxspeed;
			}
			lastWidth = maxPointX - angleW - pointX;
			if (lastWidth - tmpspeed <= 0) {
				deltaX = MathFP.toInt(MathFP.mul(MathFP.toFP(tmpspeed
						- lastWidth), MathFP.cos(radians)));
				// y方向增量
				deltaY = MathFP.toInt(MathFP.mul(MathFP.toFP(tmpspeed
						- lastWidth), MathFP.tan(radians)));
				ret[0] = 1;
				ret[1] = pointX + deltaX;
				ret[2] = pointY + (speed - deltaY);
			} else {
				ret[0] = 0;
				ret[1] = pointX + tmpspeed;
				ret[2] = pointY;
			}
			break;
		case 1:
			tmpspeed = spriteSpeed;
			deltaY = tmpspeed; // y方向增量
			ret[0] = 2;
			ret[1] = maxPointX;
			ret[2] = pointY + deltaY;
			break;
		case 2:
			tmpspeed = spriteSpeed;
			lastWidth = maxPointY - angleH - pointY;
			if (lastWidth - tmpspeed <= 0) {
				deltaX = MathFP.toInt(MathFP.mul(MathFP.toFP(tmpspeed
						- lastWidth), MathFP.cos(radians)));
				// y方向增量
				deltaY = MathFP.toInt(MathFP.mul(MathFP.toFP(tmpspeed
						- lastWidth), MathFP.sin(radians)));
				ret[0] = 3;
				ret[1] = pointX - deltaX;
				ret[2] = pointY + deltaY;
			} else {
				ret[0] = 2;
				ret[1] = maxPointX;
				ret[2] = pointY + tmpspeed;
			}
			break;
		case 3:
			tmpspeed = spriteSpeed;
			deltaX = tmpspeed; // X方向增量
			ret[0] = 4;
			ret[1] = pointX - deltaX;
			ret[2] = maxPointY;
			break;
		case 4:
			if (this.moveToMin) {
				tmpspeed = spriteSpeed + this.speed < GameConfigInfo.maxspeed ? spriteSpeed
						+ this.speed
						: GameConfigInfo.maxspeed;
			} else {
				tmpspeed = spriteSpeed - this.speed > GameConfigInfo.minspeed ? this.speed
						+ (spriteSpeed - this.speed)
						: this.speed + GameConfigInfo.minspeed;
			}
			lastWidth = pointX - (minPointX + angleW);
			if (lastWidth - tmpspeed <= 0) {
				deltaX = MathFP.toInt(MathFP.mul(MathFP.toFP(tmpspeed
						- lastWidth), MathFP.cos(radians)));
				// y方向增量
				deltaY = MathFP.toInt(MathFP.mul(MathFP.toFP(tmpspeed
						- lastWidth), MathFP.sin(radians)));
				ret[0] = 5;
				ret[1] = pointX - deltaX;
				ret[2] = pointY - deltaY;
			} else {
				ret[0] = 4;
				ret[1] = pointX - tmpspeed;
				ret[2] = pointY;
			}
			break;
		case 5:
			tmpspeed = spriteSpeed;
			deltaY = tmpspeed; // Y方向增量
			ret[0] = 6;
			ret[1] = minPointX;
			ret[2] = pointY - deltaY;
			break;
		case 6:
			tmpspeed = spriteSpeed;
			lastWidth = pointY - (minPointY + angleH);
			if (lastWidth - tmpspeed <= 0) {
				deltaX = MathFP.toInt(MathFP.mul(MathFP.toFP(tmpspeed
						- lastWidth), MathFP.cos(radians)));
				// y方向增量
				deltaY = MathFP.toInt(MathFP.mul(MathFP.toFP(tmpspeed
						- lastWidth), MathFP.sin(radians)));
				ret[0] = 7;
				ret[1] = pointX - deltaX;
				ret[2] = pointY - deltaY;
			} else {
				ret[0] = 6;
				ret[1] = minPointX;
				ret[2] = pointY - tmpspeed;
			}
			break;
		case 7:
			tmpspeed = spriteSpeed;
			deltaX = tmpspeed; // X方向增量
			ret[0] = 0;
			ret[1] = pointX + deltaX;
			ret[2] = minPointY;
			break;
		}
		return ret;
	}

	/**
	 * 逆时针 根据角色的x值和y值，当前走动方向，得到下一次的方向 返回下一帧的方向坐标
	 * 
	 * @param direction
	 * @param pointX
	 * @param pointY
	 * @param spriteSpeed
	 * @return
	 */
	public int[] moveToPointAntiClockWise(int spriteW, int direction,
			int pointX, int pointY, int spriteSpeed) {
		int thisPointY = this.y - gameMainView.getMapView().getHeaderHight()
				- height;
		int maxPointX = this.x + this.width / 2;
		int minPointX = this.x - this.width / 2;
		int minPointY = thisPointY;
		int maxPointY = thisPointY + 2 * gameMainView.getMapView().getImgH();
		int angleW = 10, angleH = 10;
		int[] ret = new int[3];
		int lastWidth = 0;
		int radians = MathFP.div(MathFP.mul(MathFP.toFP(45), MathFP.PI), MathFP
				.toFP(180));
		int deltaX, deltaY;
		int tmpspeed = 0;
		switch (direction) {
		case 0:
			if (this.moveToMin) {
				tmpspeed = spriteSpeed + this.speed < GameConfigInfo.maxspeed ? spriteSpeed
						+ this.speed
						: GameConfigInfo.maxspeed;
			} else {
				tmpspeed = spriteSpeed - this.speed > GameConfigInfo.minspeed ? this.speed
						+ (spriteSpeed - this.speed)
						: this.speed + GameConfigInfo.minspeed;
			}
			lastWidth = pointX - (minPointX + angleW);
			if (lastWidth - tmpspeed <= 0) {
				deltaX = MathFP.toInt(MathFP.mul(MathFP.toFP(tmpspeed - lastWidth), MathFP.cos(radians)));
				// y方向增量
				deltaY = MathFP.toInt(MathFP.mul(MathFP.toFP(tmpspeed - lastWidth), MathFP.sin(radians)));
				ret[0] = 1;
				ret[1] = pointX - deltaX;
				ret[2] = pointY - deltaY;
			} else {
				ret[0] = 0;
				ret[1] = pointX - tmpspeed;
				ret[2] = pointY;
			}
			break;
		case 1:
			tmpspeed = spriteSpeed;
			deltaY = tmpspeed; // y方向增量
			ret[0] = 2;
			ret[1] = minPointX;
			ret[2] = pointY + deltaY;
			break;
		case 2:
			tmpspeed = spriteSpeed;
			lastWidth = maxPointY - angleH - pointY;
			if (lastWidth - tmpspeed <= 0) {
				deltaX = MathFP.toInt(MathFP.mul(MathFP.toFP(tmpspeed - lastWidth), MathFP.cos(radians)));
				// y方向增量
				deltaY = MathFP.toInt(MathFP.mul(MathFP.toFP(tmpspeed - lastWidth), MathFP.sin(radians)));
				ret[0] = 3;
				ret[1] = pointX - deltaX;
				ret[2] = pointY + deltaY;
			} else {
				ret[0] = 2;
				ret[1] = minPointX;
				ret[2] = pointY + tmpspeed;
			}
			break;
		case 3:
			tmpspeed = spriteSpeed;
			deltaX = tmpspeed; // X方向增量
			ret[0] = 4;
			ret[1] = pointX + deltaX;
			ret[2] = maxPointY;
			break;
		case 4:
			if (this.moveToMin) {
				tmpspeed = spriteSpeed - this.speed > GameConfigInfo.minspeed ? this.speed + (spriteSpeed - this.speed) : this.speed + GameConfigInfo.minspeed;
			} else {
				tmpspeed = spriteSpeed + this.speed < GameConfigInfo.maxspeed ? spriteSpeed + this.speed : GameConfigInfo.maxspeed;
			}
			lastWidth = maxPointX - angleW - pointX;
			if (lastWidth - tmpspeed <= 0) {
				deltaX = MathFP.toInt(MathFP.mul(MathFP.toFP(tmpspeed - lastWidth), MathFP.cos(radians)));
				// y方向增量
				deltaY = MathFP.toInt(MathFP.mul(MathFP.toFP(tmpspeed - lastWidth), MathFP.tan(radians)));
				ret[0] = 5;
				ret[1] = pointX + deltaX;
				ret[2] = pointY + (tmpspeed - deltaY);
			} else {
				ret[0] = 4;
				ret[1] = pointX + tmpspeed;
				ret[2] = pointY;
			}
			break;
		case 5:
			tmpspeed = spriteSpeed;
			deltaY = tmpspeed;
			ret[0] = 6;
			ret[1] = maxPointX;
			ret[2] = pointY - deltaY;
			break;
		case 6:
			tmpspeed = spriteSpeed;
			lastWidth = pointY - (minPointY + angleH);
			if (lastWidth - tmpspeed <= 0) {
				deltaX = MathFP.toInt(MathFP.mul(MathFP.toFP(tmpspeed
						- lastWidth), MathFP.cos(radians)));
				// y方向增量
				deltaY = MathFP.toInt(MathFP.mul(MathFP.toFP(tmpspeed
						- lastWidth), MathFP.sin(radians)));
				ret[0] = 7;
				ret[1] = pointX - deltaX;
				ret[2] = pointY - deltaY;
			} else {
				ret[0] = 6;
				ret[1] = maxPointX;
				ret[2] = pointY - tmpspeed;
			}
			break;
		case 7:
			tmpspeed = spriteSpeed;
			deltaX = tmpspeed; // X方向增量
			ret[0] = 0;
			ret[1] = pointX - deltaX;
			ret[2] = minPointY;
			break;
		}
		return ret;
	}
}
