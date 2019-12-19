package com.game.mouse.screen;

import java.util.Vector;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

import com.game.lib9.Engine;
import com.game.lib9.L9Object;
import com.game.lib9.L9Screen;
import com.game.mouse.modle.Pass;
import com.game.mouse.modle.ScenePass;
import com.game.mouse.modle.UserMouse;
import com.game.mouse.modle.service.DataManageService;
import com.game.mouse.modle.service.UserDataManageService;
import com.model.control.MyImg;
import com.model.mainServer.MainConfig;
import com.model.mainServer.MainServer;
import com.model.tool.PubToolKit;

public class RankPageScreen extends L9Screen {
	private Graphics g;

	private MyImg bgImg;

	private Vector rank;

	private Font strFont;

	private int page;

	private String[] myRank;

	private int mouseNum;

	private int cheeseNum;

	private int winNum;

	private int passNum;

	public void fristInit() {
		bgImg = new MyImg("rankpage/bg.png");
		strFont = Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_BOLD, 8);
		rank = MainServer.getInstance().getRank(0);
		myRank = MainServer.getInstance().getUserRank(0);
		if ((rank == null || rank.size() == 0) && myRank != null
				&& myRank.length > 0) {
			myRank[0] = "1";
			rank = new Vector();
			rank.addElement(myRank);
		}
		this.getPropNum();
		this.getPassNum();
		this.getMouseNum();
	}

	/**
	 * 获得道具数量
	 */
	public void getPropNum() {
		cheeseNum = UserDataManageService.getInsatnce().getPropNumByCode("0");
		winNum = UserDataManageService.getInsatnce().getWinNum();
	}

	/**
	 * 获取关卡数据
	 */
	public void getPassNum() {
		ScenePass lastOpenScene = UserDataManageService.getInsatnce()
				.getLastOpenScene();
		Pass lastOpenPass = UserDataManageService.getInsatnce()
				.getLastOpenPass(lastOpenScene);
		passNum = ((Integer.parseInt(lastOpenScene.getCode()) * 6 + lastOpenPass
				.getCode()) + 1) * 100 / 24;
	}

	public void getMouseNum() {
		UserMouse[] userMouses = DataManageService.getInsatnce()
				.getAllUserMouse();
		for (int i = 0; i < userMouses.length; i++) {
			if (userMouses[i].isIsbuy()) {
				mouseNum++;
			}
		}
	}

	public void Paint() {
		// TODO Auto-generated method stub
		if (this.g == null) {
			this.g = Engine.FG;
		}
		bgImg.drawImage(g, 0, 0, 0);
		g.setFont(strFont);
		for (int i = page * 10; rank != null && i < rank.size()
				&& i < (page + 1) * 10; i++) {
			String[] r = (String[]) rank.elementAt(i);
			if (r[1] != null && r[2] != null) {
				PubToolKit.drawString(r[0], 175, 130 + i * 34, 170, strFont,
						0x000000, g);
				PubToolKit.drawString(r[2], 335, 130 + i * 34, 140, strFont,
						0x000000, g);
				PubToolKit.drawString(r[1], 440, 130 + i * 34, 200, strFont,
						0x000000, g);
			}
		}
		if (myRank[1] != null && myRank[2] != null) {
			PubToolKit
					.drawString(myRank[2], 60, 340, 140, strFont, 0xffffff, g);
			PubToolKit
					.drawString(myRank[1], 35, 412, 180, strFont, 0xffffff, g);
			PubToolKit
					.drawString(myRank[0], 60, 375, 140, strFont, 0xffffff, g);
			PubToolKit.drawString(mouseNum + "", 85, 125, 40, strFont,
					0xffffff, g);
			PubToolKit.drawString(winNum + "", 85, 160, 40, strFont, 0xffffff,
					g);
			PubToolKit.drawString(cheeseNum + "", 85, 195, 40, strFont,
					0xffffff, g);
			PubToolKit.drawString(passNum + "", 85, 233, 40, strFont, 0xffffff,
					g);
		}
	}

	public void Update() {
		this.keyPress();
	}

	public void keyPress() {
		int keyCode = Engine.getKeyCode();
		switch (keyCode) {
		case Engine.K_KEY_LEFT:
			break;
		case Engine.K_KEY_UP:
			break;
		case Engine.K_KEY_DOWN:
			break;
		case Engine.K_KEY_RIGHT:
			break;
		case Engine.K_KEY_BACK:
//		case Engine.K_KEY_NUM0:
			this.keynum0();
			break;
		}
	}

	public void keynum0() {
		this.goGame();
	}

	public void goGame() {
		HomePageScreen homePageScreen = new HomePageScreen(3);
		MainMidlet.engine.changeState(homePageScreen);
	}

	public boolean isload() {
		// TODO Auto-generated method stub
		return false;
	}
}
