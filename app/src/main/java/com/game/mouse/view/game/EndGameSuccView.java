package com.game.mouse.view.game;

import javax.microedition.lcdui.Graphics;

import com.game.lib9.Engine;
import com.game.lib9.L9Object;
import com.game.mouse.modle.Pass;
import com.game.mouse.modle.UserMouse;
import com.game.mouse.modle.service.UserDataManageService;
import com.game.mouse.screen.FeedPageScreen;
import com.game.mouse.screen.GamePageScreen;
import com.game.mouse.screen.MainMidlet;
import com.game.mouse.screen.PassPageScreen;
import com.game.mouse.screen.ShopPageScreen;
import com.model.control.MyImg;
import com.model.tool.PubToolKit;

public class EndGameSuccView implements EndGameView {
	private GameMainView gameMainView;

	private MyImg bg;

	private L9Object cheeseBgObj;

	private MyImg cheese;

	private MyImg succbut0;

	private MyImg succbut1;

	private MyImg star;

	private MyImg succheader;

	private MyImg succtip;

	private MyImg num;

	private int starW, starH;

	private int starNum;

	private int x;

	private int y;

	private int selButIndex = 1;

	private int passGetCheeseNum;

	public EndGameSuccView(GameMainView gameMainView, int starNum,
			int passGetCheeseNum, int initX, int initY) {
		this.gameMainView = gameMainView;
		this.starNum = starNum;
		this.passGetCheeseNum = passGetCheeseNum;
		this.x = initX;
		this.y = initY;
		this.init();
	}

	public void init() {
		// TODO Auto-generated method stub
		bg = new MyImg("gamepage/endgame/bg.png");
		cheeseBgObj = new L9Object(null, 2, "gamepage/endgame/cheesebg.png",
				125, 96, this.x, this.y + 130, -1, false, false, true, 2);
		cheeseBgObj.setNextFrameTime(3);
		cheese = new MyImg("gamepage/endgame/cheese.png");
		succbut1 = new MyImg("gamepage/endgame/succbut.png");
		succheader = new MyImg("gamepage/endgame/succheader.png");
		if (passGetCheeseNum > 0) {
			succbut0 = new MyImg("gamepage/endgame/feedbut.png");
			succtip = new MyImg("gamepage/endgame/tip0.png");
		} else {
			succbut0 = new MyImg("gamepage/endgame/shopbut.png");
			succtip = new MyImg("gamepage/endgame/tip1.png");
		}
		num = new MyImg("shoppage/num.png");
		star = new MyImg("gamepage/endgame/star.png");
		starW = star.getWidth() / 3;
		starH = star.getHeight() / 2;
	}

	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		bg.drawImage(g, x, y + 30, g.VCENTER | g.HCENTER);
		succheader.drawImage(g, x, y - 100, g.VCENTER | g.HCENTER);
		star.drawRegion(g, 0, starH, starW * 3, starH, 0, x, y - 170, g.VCENTER
				| g.HCENTER);
		star.drawRegion(g, 0, 0, starW * starNum, starH, 0, x - starW * 3 / 2
				+ starW * starNum / 2, y - 170, g.VCENTER | g.HCENTER);
		cheeseBgObj.paintFrame(g);
		cheese.drawImage(g, x, y - 10, g.VCENTER | g.HCENTER);
		succtip.drawImage(g, x, y + 70, g.VCENTER | g.HCENTER);
		System.out.println("this is passGetCheeseNum = " + passGetCheeseNum);
		if (passGetCheeseNum > 0) {
			PubToolKit.drawString(g, this.num.getImg(),
					(passGetCheeseNum > 99 ? 99 : passGetCheeseNum) + "",
					"X0123456789", x + (passGetCheeseNum < 10 ? 50 : 45) + 30,
					y + 67, 27, 31, g.LEFT | g.BOTTOM, 0, 1, -1);
		}
		succbut0.drawRegion(g, 0, selButIndex == 0 ? 0
				: succbut0.getHeight() / 2, succbut0.getWidth(), succbut0
				.getHeight() / 2, 0, x - 65 - 40, y + 110 + 60, g.VCENTER | g.HCENTER);
		succbut1.drawRegion(g, 0, selButIndex == 1 ? 0
				: succbut1.getHeight() / 2, succbut1.getWidth(), succbut1
				.getHeight() / 2, 0, x + 65 + 40, y + 110 + 60, g.VCENTER | g.HCENTER);
	}

	public void update() {
		// TODO Auto-generated method stub

	}

	public void keyPress() {
		int keyCode = Engine.getKeyCode();
		switch (keyCode) {
		case Engine.K_KEY_BACK:
//		case Engine.K_KEY_NUM0:
			this.passPage();
			break;
		case Engine.K_KEY_NUM9:
			break;
		case Engine.K_KEY_LEFT:
			selButIndex = selButIndex == 0 ? 1 : 0;
			System.out.println("selButIndex = " + selButIndex);
			break;
		case Engine.K_KEY_RIGHT:
			selButIndex = selButIndex == 0 ? 1 : 0;
			System.out.println("selButIndex = " + selButIndex);
			break;
		case Engine.K_KEY_FIRE:
			keyfire();
			break;
		default:

		}
	}

	public void keyfire() {
		System.out.println("此关卡得到的奶酪数：" + passGetCheeseNum);
//		UserDataManageService.getInsatnce().updateMouseProp("0", passGetCheeseNum + UserDataManageService.getInsatnce().getPropNumByCode("0"), true);
		switch (selButIndex) {
		case 0:
			this.nextPassPage();
			break;
		case 1:
			if (passGetCheeseNum > 0) {
				this.feedPage();
			} else {
				this.shopPage();
			}
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
	 * 下一关卡
	 */
	public void nextPassPage() {
		Pass pass = gameMainView.getGamePageScreen().getPass();
		UserMouse userMouse = gameMainView.getGamePageScreen().getUserMouse();
		int selSceCode, selPassCode;
		if (pass.getCode() < 5) {
			selSceCode = pass.getSceneCode();
			selPassCode = pass.getCode() + 1;
		} else {
//			selSceCode = pass.getSceneCode() + 1 > 3 ? 3 : pass.getSceneCode() + 1;
//			if(selSceCode == 2){
//				selSceCode = 3;
//			}
			selSceCode = pass.getSceneCode() + 1 > 1 ? 1 : pass.getSceneCode() + 1;
			selPassCode = 0;
		}
		PassPageScreen passPageScreen = new PassPageScreen(Integer
				.parseInt(userMouse.getCode()), selSceCode, selPassCode);
		MainMidlet.engine.changeState(passPageScreen);
	}

	/**
	 * 本关
	 */
	public void passPage() {
		Pass pass = gameMainView.getGamePageScreen().getPass();
		UserMouse userMouse = gameMainView.getGamePageScreen().getUserMouse();
		PassPageScreen passPageScreen = new PassPageScreen(Integer
				.parseInt(userMouse.getCode()), pass.getSceneCode(), pass
				.getCode());
		MainMidlet.engine.changeState(passPageScreen);
	}
}
