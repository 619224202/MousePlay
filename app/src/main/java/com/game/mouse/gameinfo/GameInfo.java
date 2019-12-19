package com.game.mouse.gameinfo;

import com.game.mouse.context.Config;
import com.model.control.MyImg;

public class GameInfo {

	public static MyImg getBottomImg(int index) {
		MyImg bottomImg;
		if (Config.retunType == 1) {
			bottomImg = new MyImg("pub/keyreturn/bottom" + index + ".png");
		} else {
			bottomImg = new MyImg("pub/key0/bottom" + index + ".png");
		}
		return bottomImg;
	}

	public static MyImg getBuyCHeeseBgImg() {
		MyImg buycheesebg;
		if (Config.retunType == 1) {
			buycheesebg = new MyImg("pub/keyreturn/buycheesebg.png");
		} else {
			buycheesebg = new MyImg("pub/key0/buycheesebg.png");
		}
		return buycheesebg;
	}

	public static MyImg getGameHelpImg() {
		MyImg helpbg;
		if (Config.retunType == 1) {
			helpbg = new MyImg("pub/keyreturn/helpgamebg.png");
		} else {
			helpbg = new MyImg("pub/key0/helpgamebg.png");
		}
		return helpbg;
	}

	public static MyImg getFightHelpImg() {
		MyImg helpbg;
		if (Config.retunType == 1) {
			helpbg = new MyImg("pub/keyreturn/helpfightbg.png");
		} else {
			helpbg = new MyImg("pub/key0/helpfightbg.png");
		}
		return helpbg;
	}

	public static MyImg getReviveButImg() {
		MyImg revivebut;
		if (Config.retunType == 1) {
			revivebut = new MyImg("pub/keyreturn/revivebut.png");
		} else {
			revivebut = new MyImg("pub/key0/revivebut.png");
		}
		return revivebut;
	}
}
