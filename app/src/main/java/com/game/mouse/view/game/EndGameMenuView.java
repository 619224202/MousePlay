package com.game.mouse.view.game;

import javax.microedition.lcdui.Graphics;

import com.game.lib9.Engine;
import com.game.lib9.L9Config;
import com.game.lib9.L9Object;
import com.game.mouse.modle.Pass;
import com.game.mouse.modle.UserMouse;
import com.game.mouse.screen.FeedPageScreen;
import com.game.mouse.screen.GamePageScreen;
import com.game.mouse.screen.MainMidlet;
import com.game.mouse.screen.PassPageScreen;
import com.game.mouse.screen.ShopPageScreen;
import com.model.control.MyImg;
import com.model.tool.PubToolKit;

public class EndGameMenuView implements EndGameView {
	private GameMainView gameMainView;

	private MyImg bg;

	private MyImg[] buts = new MyImg[5];

	private int selButIndex;

	private int x;

	private int y;

	private int butW, butH;

	public EndGameMenuView(GameMainView gameMainView, int initX, int initY) {
		this.gameMainView = gameMainView;
		this.x = initX;
		this.y = initY;
		this.init();
	}

	public void init() {
		// TODO Auto-generated method stub
		bg = new MyImg("gamepage/endgame/menubg.png");
		for (int i = 0; i < buts.length; i++) {
			buts[i] = new MyImg("gamepage/endgame/menubut" + i + ".png");
		}
		butW = buts[0].getWidth();
		butH = buts[0].getHeight() / 2;
	}

	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		bg.drawImage(g, x, y, g.VCENTER | g.HCENTER);
		for (int i = 0; i < buts.length; i++) {
			buts[i].drawRegion(g, 0, selButIndex == i ? butH : 0, butW, butH,
					0, x , y + i * (38 + 20) - (63 - 10), g.VCENTER | g.HCENTER);
		}
	}

	public void update() {
		// TODO Auto-generated method stub
	}

	public void keyPress() {
		int keyCode = Engine.getKeyCode();
		switch (keyCode) {
		case Engine.K_KEY_BACK:
//		case Engine.K_KEY_NUM0:
			this.gameMainView.closeEndGameMeunDialog();
			break;
		case Engine.K_KEY_NUM9:
			break;
		case Engine.K_KEY_DOWN:
			selButIndex = selButIndex < 4 ? selButIndex + 1 : 0;
			break;
		case Engine.K_KEY_UP:
			selButIndex = selButIndex > 0 ? selButIndex - 1 : 4;
			break;
		case Engine.K_KEY_FIRE:
			keyfire();
			break;
		default:

		}
	}

	public void keyfire() {
		switch (selButIndex) {
		case 0:
			this.gameMainView.closeEndGameMeunDialog();
			break;
		case 1:
			this.feedPage();
			break;
		case 2:
			this.shopPage();
			break;
		case 3:
			this.startGame();
			break;
		case 4:
			this.passPage();
			break;
		}
	}

	/**
	 * 喂养
	 */
	public void feedPage() {
		FeedPageScreen feedPageScreen = new FeedPageScreen();
		MainMidlet.engine.changeState(feedPageScreen);
	}

	/**
	 * 商城
	 */
	public void shopPage() {
		ShopPageScreen shopPageScreen = new ShopPageScreen();
		MainMidlet.engine.changeState(shopPageScreen);
	}

	/**
	 * 重新开始
	 */
	public void startGame() {
		Pass pass = gameMainView.getGamePageScreen().getPass();
		UserMouse userMouse = gameMainView.getGamePageScreen().getUserMouse();
		int selSceCode = pass.getSceneCode(), selPassCode = pass.getCode();

		int mySelSceCode = selSceCode;
		int mySelPassCode = selPassCode;
		boolean isFirstPlay = true;
		if(selSceCode == 2){
			mySelSceCode = 0;
			isFirstPlay = false;
		}
		if(selSceCode == 3){
			mySelSceCode = 1;
			isFirstPlay = false;
		}

		if(selPassCode == 5){
			mySelSceCode = 3;
			mySelPassCode = 4;
		}

		GamePageScreen gamePageScreen = new GamePageScreen(mySelSceCode,
				mySelPassCode, Integer.parseInt(userMouse.getCode()),isFirstPlay);
		MainMidlet.engine.changeState(gamePageScreen);
	}

	/**
	 * 本关
	 */
	public void passPage() {
		Pass pass = gameMainView.getGamePageScreen().getPass();
		UserMouse userMouse = gameMainView.getGamePageScreen().getUserMouse();

		//我注释的
//		PassPageScreen passPageScreen = new PassPageScreen(Integer
////				.parseInt(userMouse.getCode()), pass.getSceneCode(), pass
////				.getCode());

		//我写的
		PassPageScreen passPageScreen = new PassPageScreen(Integer
				.parseInt(userMouse.getCode()), PassPageScreen.mMySelSceCode, pass
				.getCode());
		MainMidlet.engine.changeState(passPageScreen);
	}
}
