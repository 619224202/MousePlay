package com.game.mouse.view.maingame.child;

import java.util.Vector;

import com.game.mouse.modle.Cat;
import com.game.mouse.view.game.GameMainView;
import com.model.tool.PubToolKit;

/**
 * 普通老鼠循环走动
 * 
 * @author Administrator
 * 
 */
public class CatSpriteView extends SpriteView {
	private int time;

	private long startTime;

	private int maxTime = 6;

	private boolean isTalk;

	protected CheeseCommonView pushCheeseView;

	/**
	 * 吃的奶酪
	 */
	protected Vector eatCheeseViews = new Vector();

	private String[] talkmsg = { "gamepage/talk0.png", "gamepage/talk1.png" };

	public CatSpriteView(GameMainView gameMainView, MapView mapView, Cat cat,
			int isClockWise, int direction, int initX, int initY) {
		super(gameMainView, mapView, cat, isClockWise, direction, 8, initX,
				initY);
	}

	/**
	 * 
	 */
	public void hitArrowOrgan() {
		super.hitArrowOrgan();
		if (pushCheeseView != null) {
			pushCheeseView.state = 0;
			pushCheeseView.bcrray = false;
			pushCheeseView.catSpriteView = null;
			pushCheeseView = null;
		}
	}

	/**
	 * 新增奶酪吃的奶酪数量
	 * 
	 * @param cheeseView
	 */
	public void addEatCheeseViews(CheeseView cheeseView) {
		eatCheeseViews.addElement(cheeseView);
		this.startAndStopTalk();
	}

	public Vector getEatCheeseViews() {
		return eatCheeseViews;
	}

	public void update() {
		super.update();
		if (eatCheeseViews.size() > 0) {
			this.addTime();
			this.endTime();
		}
	}

	/**
	 * 开始倒计时
	 */
	public void startAndStopTalk() {
		this.startTime = System.currentTimeMillis();
		time = 0;
		if (isTalk) {
			maxTime = 6;
			this.talk("");
			isTalk = false;
		} else {
			maxTime = 3;
			this.talk(talkmsg[PubToolKit.getRandomInt(talkmsg.length)]);
			isTalk = true;
		}
	}

	public void addTime() {
		if (System.currentTimeMillis() - this.startTime >= 1000L) {
			this.time++;
			this.startTime = System.currentTimeMillis();
		}
	}

	/**
	 * 倒计时结束
	 */
	public void endTime() {
		if (this.time >= this.maxTime) {
			this.startAndStopTalk();
		}
	}

}
