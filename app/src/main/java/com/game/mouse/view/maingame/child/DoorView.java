package com.game.mouse.view.maingame.child;

import javax.microedition.lcdui.Graphics;

import com.game.lib9.L9Config;
import com.game.lib9.L9Object;
import com.game.mouse.modle.Cat;
import com.game.mouse.modle.Mouse;
import com.game.mouse.modle.Sprite;
import com.game.mouse.screen.GamePageScreen;
import com.game.mouse.view.View;
import com.game.mouse.view.game.GameMainView;
import com.game.mouse.view.maingame.child.SpriteView.SpriteMainGameL9Object;

public class DoorView extends MainGameL9Object implements View {

	private GameMainView gameMainView;

	private int initX, initY;

	private boolean isopen;

	private int initAnim;

	public DoorView(GameMainView gameMainView, int animation, int initX,
			int initY) {
		super("door", 64, initX, initY, -1, animation, false, true, true, 2, 0);
		this.setNextFrameTime(2);
		this.gameMainView = gameMainView;
		this.initAnim = animation;
		this.initX = initX;
		this.initY = initY;
	}

	public void init() {
		// TODO Auto-generated method stub
		if (!this.begGameIsMoveMap()) {
			this.gameMainView.endShowDoorStartGame();
		}
	}

	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		this.paintFrame(g);
	}

	public void update() {
		// TODO Auto-generated method stub
		if (!isopen) {
			if (isBegGameIsMoveMap) {
				this.begGameMoveMap();
			}
		} else {
			if (this.getCurrentFrame() == this.getFrameCount() - 1) {
				isopen = false;
				this.setAnimation(initAnim);
				gameMainView.passDoor();
			}
		}
	}

	public int getX() {
		return initX;
	}

	private int begGameMoveMapSpeed = 0;

	/**
	 * 0-停留 1-向右移动 2-向左移动
	 */
	private int isBegGameMoveMapState;

	private int afterStopMoveMapState;

	/**
	 * 是否已经停留过
	 */
	private boolean isHaveStop;

	/**
	 * 是否移动镜头
	 */
	private boolean isBegGameIsMoveMap;

	private int time;

	private long startTime;

	/**
	 * 开始游戏是否拉伸镜头,显示出洞门的位置
	 * 
	 * @return
	 */
	public boolean begGameIsMoveMap() {
		if (gameMainView.getMoveX() == 0 && this.initX > L9Config.SCR_W) {
			isBegGameMoveMapState = 1;
			isBegGameIsMoveMap = true;
		} else if (gameMainView.getMoveX() != 0) {
			int length = this.initX - gameMainView.getSpriteView().getX() > 0 ? this.initX
					- gameMainView.getSpriteView().getX()
					: gameMainView.getSpriteView().getX() - this.initX;
			if (length > L9Config.SCR_W / 2) {
				isBegGameMoveMapState = initX
						- gameMainView.getSpriteView().getX() > 0 ? 1 : 2;
				isBegGameIsMoveMap = true;
			}
		}
		return isBegGameIsMoveMap;
	}

	/**
	 * 游戏开始镜头拉伸
	 */
	public void begGameMoveMap() {
		if (isBegGameMoveMapState == 2) {
			begGameMoveMapSpeed += 5;
			gameMainView.moveMap(begGameMoveMapSpeed);
			if (!isHaveStop
					&& (this.initX + gameMainView.getMoveX() >= L9Config.SCR_W/2 || gameMainView
							.getMoveX() >= 0)) {
				isBegGameMoveMapState = 0;
				begGameMoveMapSpeed = 0;
				this.startTime = System.currentTimeMillis();
				afterStopMoveMapState = 1;
			} else if (isHaveStop
					&& gameMainView.getMoveX() >= gameMainView.getInitMoveX()) {
				isBegGameIsMoveMap = false;
				this.gameMainView.endShowDoorStartGame();
			}
		} else if (isBegGameMoveMapState == 1) {
			begGameMoveMapSpeed += 5;
			gameMainView.moveMap(-begGameMoveMapSpeed);
			if (!isHaveStop
					&& (this.initX + gameMainView.getMoveX() <= L9Config.SCR_W/2 || gameMainView
							.getMaxMoveX() >= gameMainView.getMoveX())) {
				isBegGameMoveMapState = 0;
				begGameMoveMapSpeed = 0;
				this.startTime = System.currentTimeMillis();
				afterStopMoveMapState = 2;
			} else if (isHaveStop
					&& gameMainView.getMoveX() <= gameMainView.getInitMoveX()) {
				isBegGameIsMoveMap = false;
				this.gameMainView.endShowDoorStartGame();
			}
		} else {
			// 停留三秒切换回去
			this.addTime();
			if (this.time >= 2) {
				isBegGameMoveMapState = afterStopMoveMapState;
				isHaveStop = true;
			}
		}
	}

	public void addTime() {
		if (System.currentTimeMillis() - this.startTime >= 1000L) {
			this.time++;
			this.startTime = System.currentTimeMillis();
		}
	}

	public boolean isBegGameIsMoveMap() {
		return isBegGameIsMoveMap;
	}

	public void changePassDoorAnim(int isClockWise, int mousedirection) {
		int anim = this.initAnim;
		switch (mousedirection) {
		case 0:
		case 7:
			anim = isClockWise == 1 ? 8 : 4;
			break;
		case 1:
		case 2:
			anim = isClockWise == 1 ? 9 : 5;
			break;
		case 3:
		case 4:
			anim = isClockWise == 1 ? 10 : 6;
			break;
		case 5:
		case 6:
			anim = isClockWise == 1 ? 11 : 7;
			break;
		}
		this.setAnimation(anim);
		this.setFrame(1);
		isopen = true;
	}
}
