package com.game.mouse.screen;

import javax.microedition.lcdui.Graphics;

import com.game.lib9.L9IState;
import com.game.lib9.L9Screen;
import com.game.mouse.context.Config;
import com.game.mouse.context.UserInfo;
import com.game.mouse.modle.UserMouse;
import com.game.mouse.modle.service.DataManageService;
import com.game.mouse.modle.service.UserDataManageService;
import com.model.mainServer.MainConfig;
import com.model.mainServer.MainServer;
import com.model.tool.PlaySoundService;

public class MainIndexScreen extends L9Screen {
	private Graphics g;

	private boolean isFirstLogin;

	private L9IState pageScreen;

	public void Init(int loadCount) {
		switch (loadCount) {
		case 0:
			Config.story = 0;
			break;
		case 20:
			MainServer.getInstance().login();
			PlaySoundService.play(0);
			break;
		case 30:
			
//			MainServer.getInstance().beginGameTime();
			break;
		case 40:
			UserInfo.score = MainServer.getInstance().getScore(0);
			break;
		case 90:
			break;
		case 100:
			if (UserDataManageService.getInsatnce().isFirstLogin()) {
				UserMouse mouse = DataManageService.getInsatnce()
						.getUserMouseByCode("0");
				UserDataManageService.getInsatnce().updateMouse(mouse);
				if (Config.openStory == 1) {
					pageScreen = new StoryPageScreen();
				} else {
					pageScreen = new HomePageScreen();
				}
			} else {
				pageScreen = new HomePageScreen();
			}
			
			break;
		}
	}

	boolean bFlag;

	public void Paint() {

	}

	public void Update() {
		MainMidlet.engine.changeState(pageScreen);
	}

	public void stop() {

	}

	public int loadIndex() {
		// TODO Auto-generated method stub
		return Config.regionType == MainConfig.Type_SXDX ? 1 : 0;
	}

	public boolean threadLoad() {
		// TODO Auto-generated method stub
//		return Config.regionType != MainConfig.Type_XCXJ;
		return false;
	}
}
