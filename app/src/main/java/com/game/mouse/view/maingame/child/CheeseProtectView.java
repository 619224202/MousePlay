package com.game.mouse.view.maingame.child;

import javax.microedition.lcdui.Graphics;

import com.game.lib9.L9Config;
import com.game.mouse.modle.Mouse;
import com.game.mouse.modle.Sprite;
import com.game.mouse.view.game.GameMainView;
import com.game.mouse.view.maingame.child.SpriteView.SpriteMainGameL9Object;

/**
 * 有保护罩的奶酪
 * 
 * @author Administrator
 * 
 */
public class CheeseProtectView extends CheeseCommonView {

	private boolean isOpen;

	/**
	 * 降落的速度
	 */
	private int speedY;

	/**
	 * 方向 0-向左 1-向右 2-向上 3-向下
	 */
	private int pushdirection;

	private int pushX, pushY;

	private int pushToPointX, pushToPointY;

	/**
	 * 推动的老鼠
	 */
	private MouseSpriteView mouseSpriteView;

	private MainGameL9Object cheeseL9Object;

	/**
	 * 破碎动画播放
	 */
	private boolean isDamagedIng = false;

	public CheeseProtectView(GameMainView gameMainView, MapView mapView,
			int direction, int initX, int initY) {
		super(gameMainView, mapView, null, 0, 0, initX, initY);
		// TODO Auto-generated constructor stub
		this.pushX = initX + 20;
		this.pushY = initY + 40;
	}

	public void newObject(int initX, int initY) {
		cheeseL9Object = new MainGameL9Object("cheese1", 16, initX, initY, -1,
				this.direction, false, true, true, 2, 0) {
			public void mainGameHit(MainGameL9Object obj) {
				if (isClockWise == 1) {
					bhitPush(obj);
				}
			}
		};
		cheeseL9Object.setNextFrameTime(2);
		super.newObject(initX, initY);
	}

	public void paint(Graphics g) {
		if (isOpen) {
			super.paint(g);
		} else {
			cheeseL9Object.paintFrame(g);
		}
	}

	public void update() {
		if (isOpen) {
			super.update();
		} else {
			if (this.state == 1) {
				this.pushmove();
				cheeseL9Object.setPosX(pushX);
				cheeseL9Object.setPosY(pushY);
			} else if (this.state == 0 && this.isDamagedIng) {
				if (cheeseL9Object.getCurrentFrame() >= cheeseL9Object
						.getFrameCount() - 1) {
					this.isDamagedIng = false;
					this.isOpen = true;
					clockWiseObj.setPosX(cheeseL9Object.getPosX());
					clockWiseObj.setPosY(cheeseL9Object.getPosY());
					antiClockWiseObj.setPosX(cheeseL9Object.getPosX());
					antiClockWiseObj.setPosY(cheeseL9Object.getPosY());
				}
			}
		}
	}

	/**
	 * 推动移动
	 */
	public void pushmove() {
		switch (direction) {
		case 0:		//向左
			if (this.pushX - mouseSpriteView.getSpeed() > pushToPointX) {
				this.pushX = this.pushX - mouseSpriteView.getSpeed();
			} else {
				this.pushX = pushToPointX;
				this.direction = 3;
				int[] point = this.gameMainView.getMapView()
						.getFristPoint0FromDirection(this.pushX, this.pushY,
								this.direction, false);
				pushToPointX = point[0];
				pushToPointY = point[1];
			}
			break;
		case 1:		//向右
			System.out.println("this.pushX = " + pushX + ",mouseSpriteView.getSpeed = " + mouseSpriteView.getSpeed() + ",pushToPointX = " + pushToPointX);
			if (this.pushX + mouseSpriteView.getSpeed() < pushToPointX) {
				this.pushX = this.pushX + mouseSpriteView.getSpeed();
			} else {
				this.pushX = pushToPointX;
				this.direction = 3;
				int[] point = this.gameMainView.getMapView()
						.getFristPoint0FromDirection(this.pushX, this.pushY,
								this.direction, false);
				pushToPointX = point[0];
				pushToPointY = point[1];
			}
			break;
		default:
			if (speedY < 4) {
				speedY++;
			} else {
				speedY += 3;
			}
			if (this.pushY + speedY < pushToPointY) {
				this.pushY = this.pushY + speedY;
			} else {
				this.pushY = pushToPointY;
				this.state = 0;
				this.isDamagedIng = true;
				cheeseL9Object.setAnimation(1);
				if (this.pushToPointY > L9Config.SCR_H) {
					this.isEnd = true;
				}
			}
		}
	}

	/**
	 * 碰撞推
	 * 
	 * @param obj
	 */
	public void bhitPush(MainGameL9Object obj) {
		if (obj instanceof SpriteMainGameL9Object) {
			SpriteView spriteView = ((SpriteMainGameL9Object) obj)
					.getSpriteView();
			Sprite sprite = spriteView.getSprite();
			if (spriteView.getIsClockWise() == 1
					&& spriteView.getClockWiseObj() == obj
					|| spriteView.getIsClockWise() == 0
					&& spriteView.getAntiClockWiseObj() == obj) {
				if (!isOpen && this.state == 0) {
					if (sprite instanceof Mouse) {
						int directiontmp = (spriteView.isClockWise == 1 && spriteView.direction != 4)
								|| (spriteView.isClockWise == 0 && spriteView.direction == 4) ? 1
								: 0;
						if ((directiontmp == 1 && spriteView.getX() < this.pushX)
								|| (directiontmp == 0 && spriteView.getX() > this.pushX)) {
							this.mouseSpriteView = (MouseSpriteView) spriteView;
							initPush(directiontmp);
						}

					}
				}
			}
		}
	}

	public void bhit(MainGameL9Object obj) {
		if (isOpen) {
			super.bhit(obj);
		}
	}

	/**
	 * 初始化推效果
	 */
	public void initPush(int direction) {
		this.direction = direction;
		this.state = 1;
		int[] point = this.gameMainView.getMapView()
				.getFristPoint0FromDirection(this.pushX, this.pushY,
						this.direction, true);
		System.out.println("p[0] = " + point[0] + ",p[1] = " + point[1] + ",direction = " + direction);
		//direction:0 --- 向左	1 --- 向右
//		pushToPointX = this.direction == 1 ? (point[0] + width) : (point[0] - width);
		pushToPointX = this.direction == 1 ? (pushX + width) : (pushX - width);
		pushToPointY = point[1];
	}
}
