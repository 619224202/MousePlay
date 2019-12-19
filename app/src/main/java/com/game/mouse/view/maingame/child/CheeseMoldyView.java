package com.game.mouse.view.maingame.child;

import javax.microedition.lcdui.Graphics;

import com.game.mouse.modle.Cat;
import com.game.mouse.modle.Mouse;
import com.game.mouse.modle.Sprite;
import com.game.mouse.view.TimeView;
import com.game.mouse.view.game.GameMainView;
import com.game.mouse.view.maingame.child.SpriteView.SpriteMainGameL9Object;
import com.model.control.MyImg;

/**
 * 发霉的奶酪
 * 
 * @author Administrator
 * 
 */
public class CheeseMoldyView implements CheeseView, TimeView {

	private GameMainView gameMainView;

	private MapView mapView;

	private int time;

	private long startTime;

	/**
	 * 发霉时间
	 */
	private int moldyTime = 20;

	private boolean isstop = true;

	/**
	 * 是否发霉
	 */
	private boolean ismoldy = false;

	private MainGameL9Object cheeseObj;

	private MainGameL9Object moldyCheeseObj;

	private int initX, initY;

	private MyImg cheese;

	private int cheeseW, cheeseH;

	private int type;

	private int addNum = 1;

	public CheeseMoldyView(GameMainView gameMainView, MapView mapView,
			int type, int direction, int initX, int initY) {
		this.initX = initX;
		this.initY = initY;
		this.gameMainView = gameMainView;
		this.mapView = mapView;
	}

	public void init() {
		// TODO Auto-generated method stub
		cheese = new MyImg("cheese/2/0.png");
		cheeseW = cheese.getWidth() / 2;
		cheeseH = cheese.getHeight();
		moldyCheeseObj = new MainGameL9Object("cheese/2/1.png", 32, 39, 29,
				initX, initY, -1, false, true, true, 2, 0) {
			public void mainGameHit(MainGameL9Object obj) {
				if (ismoldy) {
					bhit(obj);
				}
			}
		};
		this.initY = this.initY + cheeseH / 3;
		cheeseObj = new MainGameL9Object("cheese/2/0.png", 32, 76, 76, initX,
				initY, -1, false, true, true, 2, 0) {
			public void mainGameHit(MainGameL9Object obj) {
				if (!ismoldy) {
					bhit(obj);
				}
			}
		};
	}

	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		if (ismoldy) {
			moldyCheeseObj.paintFrame(g);
		} else {
			cheeseObj.setFrame(0);
			cheeseObj.paintFrame(g);
			int h = time * cheeseH / moldyTime;
			cheese.drawRegion(g, cheeseW, h, cheeseW, cheeseH - h, 0, initX,
					initY, g.HCENTER | g.BOTTOM);
		}
	}

	public void update() {
		if (!isstop && !ismoldy) {
			if (this.time >= this.moldyTime) {
				ismoldy = true;
			} else {
				addTime();
			}
		}
	}

	public void addTime() {
		if (System.currentTimeMillis() - this.startTime >= 1000L) {
			this.time++;
			this.startTime = System.currentTimeMillis();
		}
	}

	public void startReckonTime() {
		// TODO Auto-generated method stub
		this.startTime = System.currentTimeMillis();
		isstop = false;
	}

	public void stopReckonTime() {
		// TODO Auto-generated method stub
		this.isstop = true;
	}

	public void recoverReckonTime() {
		this.isstop = false;
	}

	public int getType() {
		return type;
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
					endAnimation();
				} else if (sprite instanceof Mouse) {
					switch (this.getType()) {
					case 0:
						if (!ismoldy) {
							gameMainView.addGetCheese(addNum);
						} else {
							gameMainView.getSpriteView().injured(20);
						}
						break;
					case 1:
						if (!ismoldy) {
							gameMainView.addGetCheese(addNum);
						} else {
							gameMainView.getSpriteView().injured(20);
						}
						break;
					}
					gameMainView.addOtherViews(new CheeseAddView(
							this.gameMainView, spriteView.getX(), spriteView
									.getY(), gameMainView.starNum * 40 + 530, 50));
					endAnimation();
				}
			}
		}
	}

	public void endAnimation() {
		cheeseObj.isOver = true;
		moldyCheeseObj.isOver = true;
		gameMainView.removeCheeseViews(this);
	}
}
