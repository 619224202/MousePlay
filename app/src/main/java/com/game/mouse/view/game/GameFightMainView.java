package com.game.mouse.view.game;

import javax.microedition.lcdui.Graphics;

import com.game.mouse.modle.Cat;
import com.game.mouse.modle.Pass;
import com.game.mouse.modle.UserMouse;
import com.game.mouse.screen.GamePageScreen;
import com.game.mouse.view.View;
import com.game.mouse.view.fightgame.child.FightLoadingView;

public class GameFightMainView implements View {
	private GamePageScreen gamePageScreen;

	private FightLoadingView fightLoadingView;

	private GameFightView gameFightView;

	private int loadCount = 0;

	private boolean threadLoad = false;

	/**
	 * 是否结束load
	 */
	private boolean endLoad;

	private int time;

	private long startTime;

	public GameFightMainView(GamePageScreen gamePageScreen) {
		this.gamePageScreen = gamePageScreen;
	}

	public void init() {
		// TODO Auto-generated method stub
		fightLoadingView = new FightLoadingView(this);
		fightLoadingView.init();
	}

	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		if (endLoad) {
			gameFightView.paint(g);
		} else {
			fightLoadingView.paint(g);
		}
	}

	public void update() {
		// TODO Auto-generated method stub
		if (endLoad) {
			gameFightView.update();
		} else {
			if (loadCount == 0) {
				Cat cat = (Cat) gamePageScreen.getIngPkCatSpriteView()
						.getSprite();
				gameFightView = new GameFightView(this, cat);
				if (threadLoad) {
					theadLoadInitState();
				}
			}
			if (!threadLoad)
				loadInitState();
			if (loadCount > 100 && fightLoadingView.isEndLoad()) {
				this.endLoad = true;
			}
			fightLoadingView.update();
		}
	}

	public void endLoading() {
		this.fightLoadingView = null;
	}

	/**
	 * 每帧加载state的init方法
	 */
	private void loadInitState() {
		gameFightView.init(loadCount);
		loadCount += 5;
	}

	/**
	 * 线程加载state的init方法
	 */
	private void theadLoadInitState() {
		new Thread() {
			public void run() {
				while (true) {
					gameFightView.init(loadCount);
					loadCount += 5;
					if (loadCount > 100) {
						endLoad = true;
						break;
					}
				}
			}
		}.start();
	}

	public void release() {

	}

	public Pass getPass() {
		return gamePageScreen.getPass();
	}

	public UserMouse getUserMouse() {
		return gamePageScreen.getUserMouse();
	}

	public void goToGameMain(boolean isWin) {
		gamePageScreen.changeFightToGame(isWin);
	}

	public GamePageScreen getGamePageScreen() {
		return gamePageScreen;
	}
}
