package com.game.mouse.screen;

import javax.microedition.lcdui.Graphics;

import com.game.lib9.Engine;
import com.game.lib9.L9Screen;
import com.game.mouse.modle.Pass;
import com.game.mouse.modle.ScenePass;
import com.game.mouse.modle.UserMouse;
import com.game.mouse.modle.service.DataManageService;
import com.game.mouse.modle.service.UserDataManageService;
import com.game.mouse.view.BuyCheeseView;
import com.game.mouse.view.game.GameFightMainView;
import com.game.mouse.view.game.GameMainView;
import com.game.mouse.view.maingame.child.CatSpriteView;
import com.model.base.ParserTbl;

public class GamePageScreen extends L9Screen {
	private Graphics g;

	private GameMainView gameMainView;

	private GameFightMainView gameFightMainView;

	private BuyCheeseView endGameBuyCheeseDialog;

	/**
	 * 游戏状态
	 */
	private int gameState;

	private Pass pass;

	private UserMouse userMouse;

	private boolean isFirstLast;

	public GamePageScreen(int scene, int pass, int mouseCode,boolean type) {	//type用来表示是不是将关场景重置了（第三个场景变成了第一个场景，第四个场景变成了第二个场景）
		this.pass = (Pass) UserDataManageService.getInsatnce()
				.getSceneAllPassBySceneCode(scene).getPass().elementAt(pass);
		this.userMouse = DataManageService.getInsatnce().getUserMouseByCode(
				mouseCode + "");
		gameMainView = new GameMainView(this,type);
	}

	/**
	 * 是否在玩刚开启的关卡
	 * 
	 * @return
	 */
	public boolean getIsFirstLast() {
		ScenePass lastOpenScene = UserDataManageService.getInsatnce()
				.getLastOpenScene();
		Pass lastOpenPass = UserDataManageService.getInsatnce()
				.getLastOpenPass(lastOpenScene);
		return lastOpenPass.getCode() == pass.getCode();
	}

	public void Init(int loadCount) {
		// TODO Auto-generated method stub
		switch (loadCount) {
		case 0:
			initData();
			break;
		case 10:
			this.isFirstLast = this.getIsFirstLast();
			break;
		case 20:
			ParserTbl.getInstance().defineMedia(1, "tbl/game.tbl");
			break;
		case 40:
			gameMainView.init(40);
			break;
		case 50:
			gameMainView.init(50);
			break;
		case 60:
			gameMainView.init(60);
			break;
		case 70:
			gameMainView.init(70);
			break;
		case 80:
			gameMainView.init(80);
			break;
		case 90:
			gameMainView.init(90);
			break;
		case 100:
			gameMainView.init(100);
			break;
		}
	}

	/**
	 * 初始化数据
	 */
	public void initData() {

	}

	public void Paint() {
		// TODO Auto-generated method stub
		if (this.g == null) {
			this.g = Engine.FG;
		}
		if (gameState == 1) {
			gameFightMainView.paint(g);
		} else {
			gameMainView.paint(g);
		}
	}

	public void Update() {
		if (gameState == 1) {
			gameFightMainView.update();
		} else {
			gameMainView.update();
		}
	}

	/**
	 * 游戏页面切换PK页面
	 */
	public void changeGameToFight() {
		gameState = 1;
		gameMainView.getMapView().clearBgImg();
		gameFightMainView = new GameFightMainView(this);
		gameFightMainView.init();
	}

	/**
	 * PK页面切换到游戏
	 * 
	 * @param isWin
	 */
	public void changeFightToGame(boolean isWin) {
		gameFightMainView.release();
		gameFightMainView = null;
		gameMainView.getMapView().createMapbgImg();
		gameMainView.getPropNum();
		UserDataManageService.getInsatnce().updateMouse(userMouse);
		if (isWin) {
			this.getIngPkCatSpriteView().die();
			gameMainView.passKillCatNum++;
		} else {
			gameMainView.getSpriteView().injured(50);
		}
		gameState = 0;
	}

	public Pass getPass() {
		return pass;
	}

	public UserMouse getUserMouse() {
		return userMouse;
	}

	/**
	 * 得到正在对战的猫
	 * 
	 * @return
	 */
	public CatSpriteView getIngPkCatSpriteView() {
		return gameMainView.getCatIngPKSpriteView();
	}

	public boolean isFirstLast() {
		return this.isFirstLast;
	}

	/**
	 * 增加本关偷取数量
	 */
	public void addPassStealNum() {
		gameMainView.passStealNum++;
	}

	/**
	 * 新增本关的偷取奶酪数量数量
	 * 
	 * @param num
	 */
	public void addPassGetCheeseNum(int num) {
		gameMainView.passGetCheeseNum += num;
	}

}
