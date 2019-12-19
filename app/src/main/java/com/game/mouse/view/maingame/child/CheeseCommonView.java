package com.game.mouse.view.maingame.child;

import javax.microedition.lcdui.Graphics;

import com.game.mouse.modle.Cat;
import com.game.mouse.modle.Mouse;
import com.game.mouse.modle.Sprite;
import com.game.mouse.view.View;
import com.game.mouse.view.game.GameMainView;
import com.game.mouse.view.maingame.child.SpriteView.SpriteMainGameL9Object;
import com.studio.motionwelder.MSprite;

/**
 * 普通奶酪
 * 
 * @author Administrator
 * 
 */
public class CheeseCommonView implements CheeseView {

	protected GameMainView gameMainView;

	protected MapView mapView;

	protected CatSpriteView catSpriteView;

	protected int state;

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

	protected MainGameL9Object clockWiseObj;

	protected MainGameL9Object antiClockWiseObj;

	protected int type;

	protected int addNum = 1;

	protected boolean isEnd;

//	protected int width = 110, height = 230;

	protected int width = 0, height = 0;

	protected int carrayLen = 5;

	protected boolean bcrray = false;

	private boolean isStop;

	public CheeseCommonView(GameMainView gameMainView, MapView mapView,
			CatSpriteView catSpriteView, int type, int direction, int initX,
			int initY) {
		this.gameMainView = gameMainView;
		this.mapView = mapView;
		this.catSpriteView = catSpriteView;
		this.direction = direction;
		this.pointMapX = initX - mapView.getHeaderWidth();
		this.pointMapY = initY - mapView.getHeaderHight();
	}

	public void init() {
		// TODO Auto-generated method stub
		int tmpInitX = this.pointMapX + mapView.getHeaderWidth();
		int tmpInitY = this.pointMapY + mapView.getHeaderHight();
		newObject(tmpInitX, tmpInitY);
		changePointMapToPoint();
	}

	/**
	 * 创建对象
	 * 
	 * @param res
	 * @param initX
	 * @param initY
	 */
	public void newObject(int initX, int initY) {
		clockWiseObj = new MainGameL9Object("cheese0", 16, initX, initY, -1,
				this.direction, false, true, true, 2, 0) {
			public void mainGameHit(MainGameL9Object obj) {
				if (isClockWise == 1) {
					bhit(obj);
				}
			}
		};
		clockWiseObj.setNextFrameTime(2);
		antiClockWiseObj = new MainGameL9Object("cheese0", 16, initX, initY,
				-1, this.direction, false, true, true, 2,
				MSprite.ORIENTATION_FLIP_H) {
			public void mainGameHit(MainGameL9Object obj) {
				if (isClockWise == 0) {
					bhit(obj);
				}
			}
		};
		antiClockWiseObj.setNextFrameTime(2);
		changePointMapToPoint();
	}

	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		if (isClockWise == 1) {
			clockWiseObj.paintFrame(g);
		} else {
			antiClockWiseObj.setSpriteOrientation(true);
			antiClockWiseObj.paintFrame(g);
		}
	}

	public void update() {
		// TODO Auto-generated method stub
		if (state == 1 && catSpriteView != null) {
			this.move();
		} else if (!isStop && bcrray && state == 0 && catSpriteView != null) {
			if (mapView.getTwoPointLen(this.pointMapX, this.pointMapY,
					catSpriteView.getPointMapX(), catSpriteView.getPointMapY()) >= (catSpriteView
					.getSpriteW()
					/ 2 + carrayLen + width / 2)) {
				state = 1;
			}
		}
		this.isend();
	}

	/**
	 * 移动
	 */
	public void move() {
		this.isClockWise = catSpriteView.isClockWise;
		int[] point = this.gameMainView.getMapView().moveToPointByDirection(15,
				this.isClockWise, this.direction, this.pointMapX,
				this.pointMapY, catSpriteView.getSpeed());
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
		clockWiseObj.setPosX(this.pointMapX + this.gameMainView.getMapView().getHeaderWidth());
		clockWiseObj.setPosY(this.pointMapY
				+ this.gameMainView.getMapView().getHeaderHight());
		antiClockWiseObj.setPosX(this.pointMapX + this.gameMainView.getMapView().getHeaderWidth());
		antiClockWiseObj.setPosY(this.pointMapY
				+ this.gameMainView.getMapView().getHeaderHight());
	}

	public void bhit(MainGameL9Object obj) {
		if (obj instanceof SpriteMainGameL9Object) {
			SpriteView spriteView = ((SpriteMainGameL9Object) obj)
					.getSpriteView();
			if (spriteView.getIsClockWise() == 1
					&& spriteView.getClockWiseObj() == obj
					|| spriteView.getIsClockWise() == 0
					&& spriteView.getAntiClockWiseObj() == obj) {
				Sprite sprite = spriteView.getSprite();
				if (sprite instanceof Cat && ((Cat) sprite).isEatCheese()) {
					if (isClockWise == 1) {
						clockWiseObj.setAnimation(1);
					} else {
						antiClockWiseObj.setAnimation(1);
					}
					this.isEnd = true;
					((CatSpriteView) spriteView).addEatCheeseViews(this);
				} else if (!this.bcrray && sprite instanceof Cat
						&& ((Cat) sprite).isCarryCheese()) {
					if (((CatSpriteView) spriteView).pushCheeseView == null) {
						this.catSpriteView = (CatSpriteView) spriteView;
						this.bcrray = true;
						this.isClockWise = catSpriteView.isClockWise;
						this.direction = catSpriteView.direction;
						this.catSpriteView.pushCheeseView = this;
						if (this.isClockWise == 1) {
							clockWiseObj.setAnimation(this.direction);
						} else {
							antiClockWiseObj.setAnimation(this.direction);
						}
					}
				} else if (sprite instanceof Mouse) {
					switch (this.getType()) {
					case 0:
						gameMainView.addGetCheese(addNum);
						break;
					case 1:
						gameMainView.addGetCheese(addNum);
						break;
					}
					gameMainView.addOtherViews(new CheeseAddView(
							this.gameMainView, spriteView.getX(), spriteView
									.getY(), gameMainView.starNum * 40 + 530,
							50));
					if (isClockWise == 1) {
						clockWiseObj.setAnimation(1);
					} else {
						antiClockWiseObj.setAnimation(1);
					}
					this.isEnd = true;
				}
			}
		}
	}

	public void isend() {
		if (this.isEnd) {
			this.endAnimation();
		}
	}

	public void endAnimation() {
		clockWiseObj.isOver = true;
		antiClockWiseObj.isOver = true;
		gameMainView.removeCheeseViews(this);
	}

	public int getType() {
		return type;
	}

	public void stop() {
		if (bcrray) {
			this.isStop = true;
			this.state = 0;
		}
	}

	public void start() {
		if (bcrray) {
			this.isStop = false;
			this.state = 1;
		}
	}
}
