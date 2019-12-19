package com.game.mouse.view.maingame.child;

import java.util.Vector;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

import com.game.lib9.L9Config;
import com.game.lib9.L9Object;
import com.game.mouse.context.GameConfigInfo;
import com.game.mouse.modle.Mouse;
import com.game.mouse.modle.Sprite;
import com.game.mouse.view.View;
import com.game.mouse.view.game.GameMainView;
import com.model.control.MyImg;
import com.model.tool.PubToolKit;
import com.studio.motionwelder.MSprite;

public class SpriteView implements View {

	protected int spriteW = 80, spriteH = 50;

	protected int speed = 8;

	protected int minspeed = GameConfigInfo.minspeed;

	protected int maxspeed = GameConfigInfo.maxspeed;

	protected MapView mapView;

	/**
	 * 是否顺时针
	 */
	protected int isClockWise = 1;

	/**
	 * 方向
	 */
	protected int direction;

	protected int pointMapX;

	protected int pointMapY;

	protected int initPointMapX;

	protected int initPointMapY;

	protected SpriteMainGameL9Object clockWiseObj;

	protected SpriteMainGameL9Object antiClockWiseObj;

	protected SpriteMainGameL9Object dieEffectObj;

	protected GameMainView gameMainView;

	private int objType;

	protected Sprite sprite;

	/**
	 * 0-静止，1-走动
	 */
	protected int state;

	/**
	 * 是否消失
	 */
	protected boolean isdie = false;

	protected boolean dieAnimationEnd = false;

	protected boolean isshow = true;

	protected int flashCount = 0;

	protected OrganMoveSpriteMoveView organMoveSpriteMoveView;

	protected MyImg talkbg;

	protected MyImg talkmsg;

	private Font font;

	public SpriteView(GameMainView gameMainView, MapView mapView,
			Sprite sprite, int isClockWise, int direction, int objType,
			int initX, int initY) {
		this.gameMainView = gameMainView;
		this.mapView = mapView;
		this.sprite = sprite;
		this.speed = sprite.getSpeed();
		this.isClockWise = isClockWise;
		this.direction = direction;
		this.objType = objType;
		this.initPointMapX = this.pointMapX = initX - mapView.getHeaderWidth();
		this.initPointMapY = this.pointMapY = initY - mapView.getHeaderHight();
		font = Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_ITALIC, PubToolKit
				.getFontBig16And24Size());
	}

	public void init() {
		// TODO Auto-generated method stub
		talkbg = new MyImg("gamepage/talkbg.png");
		String res = getSpriteName() + sprite.getCode();
		int tmpInitX = this.pointMapX + mapView.getHeaderWidth();
		int tmpInitY = this.pointMapY + mapView.getHeaderHight();
		newSpriteObject(res, tmpInitX, tmpInitY);
		changePointMapToPoint();
	}

	public String getSpriteName() {
		return sprite instanceof Mouse ? "mouse" : "cat";
	}

	/**
	 * 创建对象
	 * 
	 * @param res
	 * @param initX
	 * @param initY
	 */
	public void newSpriteObject(String res, int initX, int initY) {
		int animation = 0;
		if (this.state == 0 && (sprite instanceof Mouse)) {
			animation = this.direction == 2 ? 9 : this.direction == 4 ? 10
					: this.direction == 6 ? 11 : 8;
		} else {
			animation = direction;
		}
		clockWiseObj = new SpriteMainGameL9Object(this, res, this.objType,
				initX, initY, -1, animation, false, true, true, 2, 0) {
			public void mainGameHit(MainGameL9Object obj) {
				if (isClockWise == 1) {
					bhit(obj);
				}
			}
		};
		clockWiseObj.setNextFrameTime(2);
		antiClockWiseObj = new SpriteMainGameL9Object(this, res, this.objType,
				initX, initY, -1, animation, false, true, true, 2,
				MSprite.ORIENTATION_FLIP_H) {
			public void mainGameHit(MainGameL9Object obj) {
				if (isClockWise == 0) {
					bhit(obj);
				}
			}
		};
		antiClockWiseObj.setNextFrameTime(2);
	}

	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		if (!isdie && isshow) {
			if (flashCount == 0 || flashCount % 2 == 0) {
				if (isClockWise == 1) {
					if (clockWiseObj != null) {
						clockWiseObj.paintFrame(g);
					}
				} else {
					if (antiClockWiseObj != null) {
						antiClockWiseObj.setSpriteOrientation(true);
						antiClockWiseObj.paintFrame(g);
					}
				}
			}
		}
		if (isdie && dieEffectObj != null && !dieAnimationEnd) {
			dieEffectObj.paintFrame(g);
		}
		if (talkmsg != null && talkmsg != null) {
			talkbg.drawImage(g, this.getX() + 10, this.getY()
					- (this.direction == 0 ? 70 : 20), 0);
			talkmsg.drawImage(g, this.getX() + 10, this.getY()
					- (this.direction == 0 ? 70 : 20), 0);
		}
	}

	public void update() {
		// TODO Auto-generated method stub
		if (!isdie && state == 1) {
			this.moveType();
		}
		if (flashCount > 0) {
			flashCount--;
		}
//		System.out.println(" ----- direction = " + direction + " ----- ");
	}

	/**
	 * 移动方式
	 */
	public void moveType() {
		int[] point;
		if (organMoveSpriteMoveView != null) {
			point = organMoveSpriteMoveView.moveToPointByDirection(this.spriteW / 2, this.isClockWise, direction, this.pointMapX, this.pointMapY, speed);
		} else {
			point = this.mapView.moveToPointByDirection(this.spriteW / 2, this.isClockWise, direction, this.pointMapX, this.pointMapY, speed);
		}
		if (this.direction != point[1]) {
			if (isClockWise == 1) {
				clockWiseObj.setAnimation(point[1]);
			} else {
				antiClockWiseObj.setAnimation(point[1]);
			}
		}
		getNextMapPoint(point);
		changePointMapToPoint();
	}

	/**
	 * point赋值
	 * 
	 * @param point
	 */
	public void getNextMapPoint(int[] point) {
		this.direction = point[1];
		this.pointMapX = point[2];
		this.pointMapY = point[3];
	}

	/**
	 * 将地图坐标改成anu坐标
	 */
	public void changePointMapToPoint() {
		clockWiseObj.setPosX(this.pointMapX + mapView.getHeaderWidth());
		clockWiseObj.setPosY(this.pointMapY + mapView.getHeaderHight());
		antiClockWiseObj.setPosX(this.pointMapX + mapView.getHeaderWidth());
		antiClockWiseObj.setPosY(this.pointMapY + mapView.getHeaderHight());
	}

	/**
	 * 跳跃
	 */
	public boolean jump() {
		if (this.organMoveSpriteMoveView == null) {
			int[] jumpAfterPoint = mapView.getJumpAfterPoint(this.isClockWise, this.direction, this.pointMapX, this.pointMapY);
			if (jumpAfterPoint != null) {
				this.isClockWise = this.isClockWise == 1 ? 0 : 1;
				this.direction = jumpAfterPoint[0];
				this.pointMapX = jumpAfterPoint[1];
				this.pointMapY = jumpAfterPoint[2];
				changeAnimation();
			} else {
				Vector organMoves = gameMainView.getOrganMoveSpriteMoveView();
				for (int i = 0; i < organMoves.size(); i++) {
					OrganMoveSpriteMoveView organMoveSpriteMoveView = (OrganMoveSpriteMoveView) organMoves.elementAt(i);
					int[] jumpPoint = organMoveSpriteMoveView.isjump(this);
					if (jumpPoint[0] == 1) {
						this.isClockWise = this.isClockWise == 1 ? 0 : 1;
						this.direction = jumpPoint[1];
						this.pointMapX = jumpPoint[2];
						this.pointMapY = jumpPoint[3];
						changeAnimation();
						this.organMoveSpriteMoveView = organMoveSpriteMoveView;
						return true;
					}
				}
			}
			return jumpAfterPoint != null;
		} else {
			int[] jumpAfterPoint = mapView.getJumpAfterPoint(this.isClockWise, this.direction, this.pointMapX, this.pointMapY);
			if (jumpAfterPoint != null) {
				this.isClockWise = this.isClockWise == 1 ? 0 : 1;
				this.direction = jumpAfterPoint[0];
				this.pointMapX = jumpAfterPoint[1];
				this.pointMapY = jumpAfterPoint[2];
				this.organMoveSpriteMoveView = null;
				changeAnimation();
			}
			return jumpAfterPoint != null;
		}

	}

	public void changeAnimation() {
		if (isClockWise == 1) {
			clockWiseObj.setAnimation(this.direction);
		} else {
			antiClockWiseObj.setAnimation(this.direction);
		}
	}

	/**
	 * 撞到箭头障碍物，角色变化
	 */
	public void hitArrowOrgan() {
		int tmpDirection = 0;
		switch (this.getDirection()) {
		case 1:
			tmpDirection = 7;
			break;
		case 2:
			tmpDirection = 6;
			break;
		case 3:
			tmpDirection = 5;
			break;
		case 4:
			tmpDirection = 4;
			break;
		case 5:
			tmpDirection = 3;
			break;
		case 6:
			tmpDirection = 2;
			break;
		case 7:
			tmpDirection = 1;
			break;
		}
		this.setIsClockWise(this.getIsClockWise() == 1 ? 0 : 1);
		this.setDirection(tmpDirection);
		this.changeAnimation();
	}

	/**
	 * 加速
	 * 
	 * @param addspeed
	 */
	public void addSpeed(int addspeed) {
		if (addspeed > 0) {
			if (this.speed + addspeed <= this.maxspeed) {
				this.speed += addspeed;
			} else {
				this.speed = this.maxspeed;
				if(GameMainView.addSpeedType == 1){
					GameMainView.propNum[1] += 1;
				}else if(GameMainView.addSpeedType == 2){
					GameMainView.propNum[0] += 1;
				}
			}
		} else if (addspeed < 0) {
			if (this.speed + addspeed >= this.minspeed) {
				this.speed += addspeed;
			} else {
				this.speed = minspeed;
				if(GameMainView.addSpeedType == 1){
					GameMainView.propNum[1] += 1;
				}else if(GameMainView.addSpeedType == 2){
					GameMainView.propNum[0] += 1;
				}
			}
		}

	}

	/**
	 * 受伤
	 */
	public void injured(int flashCount) {
		sprite.setHp(sprite.getHp() - 1);
		if (sprite.getHp() <= 0) {
			this.die();
		} else {
			this.flashCount = flashCount > 20 ? flashCount : 20;
		}
	}

	public void bhit(MainGameL9Object obj) {

	}

	public boolean isIsdie() {
		return isdie;
	}

	public L9Object getClockWiseObj() {
		return clockWiseObj;
	}

	public L9Object getAntiClockWiseObj() {
		return antiClockWiseObj;
	}

	public GameMainView getGameMainView() {
		return gameMainView;
	}

	class SpriteMainGameL9Object extends MainGameL9Object {
		private SpriteView spriteView;

		public SpriteMainGameL9Object(SpriteView spriteView, String objKey, int objType, int x, int y, int loopOffSet, int animation, boolean is_drawPartBuf, boolean is_addToEng, boolean isPlay,
				int depth, int trans) {
			super(objKey, objType, x, y, loopOffSet, animation, is_drawPartBuf,
					is_addToEng, isPlay, depth, trans);
			this.spriteView = spriteView;
		}

		public SpriteView getSpriteView() {
			return this.spriteView;
		}
	}

	public Sprite getSprite() {
		return sprite;
	}

	public boolean isControlSpeed(boolean isleft) {
		boolean isaddspeed = this.getControlSpeed(isleft);
		if (isaddspeed) {
			return (this.speed + 5) <= this.maxspeed;
		} else {
			return (this.speed - 5) >= this.minspeed;
		}

	}

	public boolean getControlSpeed(boolean isleft) {
		boolean isaddspeed = false;
		if (isleft) {
			if (this.isClockWise == 1) {
				if (this.direction == 3 || this.direction == 4
						|| this.direction == 5 || this.direction == 6) {
					isaddspeed = true;
				}
			} else {
				if (this.direction == 0 || this.direction == 5
						|| this.direction == 6 || this.direction == 7) {
					isaddspeed = true;
				}
			}
		} else {
			if (this.isClockWise == 1) {
				if (this.direction == 0 || this.direction == 1
						|| this.direction == 2 || this.direction == 7) {
					isaddspeed = true;
				}
			} else {
				if (this.direction == 1 || this.direction == 2
						|| this.direction == 3 || this.direction == 4) {
					isaddspeed = true;
				}
			}
		}
		return isaddspeed;
	}

	/**
	 * 根据左右按键加速减速
	 * 
	 * @param isleft
	 */
	public void controlSpeed(boolean isleft) {
		if (this.direction == 0 || this.direction == 4) {
			boolean isaddspeed = this.getControlSpeed(isleft);
			this.addSpeed(isaddspeed ? 5 : -5);
		}
	}

	public void startMove() {
		this.state = 1;
		if (isClockWise == 1) {
			clockWiseObj.setAnimation(this.direction);
		} else {
			antiClockWiseObj.setAnimation(this.direction);
		}
	}

	public void stopMove() {
		this.state = 0;
	}

	public int getX() {
		return isClockWise == 1 ? clockWiseObj.getPosX() : antiClockWiseObj
				.getPosX();
	}

	public int getY() {
		return isClockWise == 1 ? clockWiseObj.getPosY() : antiClockWiseObj
				.getPosY();
	}

	public int getInitPointMapX() {
		return initPointMapX;
	}

	public int getInitPointMapY() {
		return initPointMapY;
	}

	public void setXY(int x, int y) {
		clockWiseObj.setPosX(x);
		clockWiseObj.setPosY(y);
		antiClockWiseObj.setPosX(x);
		antiClockWiseObj.setPosY(y);
	}

	public int getSpriteW() {
		return spriteW;
	}

	public int getSpeed() {
		return speed;
	}

	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}

	public void setSprite(Sprite sprite) {
		this.sprite = sprite;
	}

	/**
	 * 死亡
	 */
	public void die() {
		isdie = true;
		dieEffectObj = new SpriteMainGameL9Object(this, "dieeffect", 2, this
				.getX(), this.getY(), 1, 0, false, true, true, 2, 0) {
			public void endAnimation() {
				dieAnimationEnd();
			}
		};
		dieEffectObj.setNextFrameTime(2);
		this.state = 0;
	}

	/**
	 * 死亡动画结束
	 */
	public void dieAnimationEnd() {
		clockWiseObj.isOver = true;
		antiClockWiseObj.isOver = true;
		dieEffectObj.isOver = true;
		dieEffectObj = null;
		dieAnimationEnd = true;
		gameMainView.removeSprite(this);
	}

	public int getPointMapX() {
		return pointMapX;
	}

	public int getPointMapY() {
		return pointMapY;
	}

	public int getIsClockWise() {
		return isClockWise;
	}

	public void setIsClockWise(int isClockWise) {
		this.isClockWise = isClockWise;
	}

	/**
	 * 加血
	 */
	public void addBlood(int num) {
		if (num > 0) {
			if (this.getSprite().getMaxhp() > this.sprite.getHp() + num) {
				this.getSprite().setHp(this.sprite.getHp() + num);
			} else {
				this.getSprite().setHp(this.getSprite().getMaxhp());
			}
		}
	}

	public void release() {
		if (clockWiseObj != null) {
			clockWiseObj.isOver = true;
			clockWiseObj = null;
		}
		if (antiClockWiseObj != null) {
			antiClockWiseObj.isOver = true;
			antiClockWiseObj = null;
		}
		if (dieEffectObj != null) {
			dieEffectObj.isOver = true;
			dieEffectObj = null;
		}
	}

	/**
	 * 复活
	 */
	public void revive() {
		this.getSprite().setHp(this.sprite.getMaxhp());
		this.flashCount = 30;
		isdie = false;
		this.state = 1;
		dieAnimationEnd = false;
	}

	/**
	 * 说话
	 * 
	 * @param talkmsg
	 */
	public void talk(String talkmsg) {
		if ("".equals(talkmsg)) {
			this.talkmsg = null;
		} else {
			this.talkmsg = new MyImg(talkmsg);
		}
	}

	/**
	 * 是否走到原点
	 * 
	 * @return
	 */
	public boolean isInitPoint() {
		return this.initPointMapX / mapView.getImgH() == this.pointMapX
				/ mapView.getImgH()
				&& this.initPointMapY / mapView.getImgW() == this.pointMapY
						/ mapView.getImgW();
	}

	public SpriteMainGameL9Object getSpriteMainGameL9Object() {
		if (isClockWise == 1) {
			return clockWiseObj;
		} else {
			return antiClockWiseObj;
		}
	}
}
