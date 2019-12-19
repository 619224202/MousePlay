package com.game.mouse.view.maingame.child;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

import com.game.lib9.Engine;
import com.game.lib9.L9Config;
import com.game.lib9.L9IState;
import com.game.lib9.L9Screen;
import com.game.mouse.gameinfo.Props;
import com.game.mouse.modle.service.UserDataManageService;
import com.game.mouse.view.BuyCheeseView;
import com.game.mouse.view.game.GameMainView;
import com.model.control.MyImg;
import com.model.mainServer.MainConfig;
import com.model.mainServer.MainServer;
import com.model.tool.PubToolKit;

public class BuyCheeseMainGameView extends BuyCheeseView {
	private GameMainView gameMainView;

	private String[][] propsmsg = {
			{ "speedcheese", "", "0", "0", "速度奶酪", "1", "控制速度使用,购买速度奶酪" },
			{ "uncheese0", "", "0", "0", "万能奶酪", "0", "恢复心情、喂养使用,购买万能奶酪" },
			{ "uncheese1", "", "0", "0", "万能奶酪", "0", "恢复心情、喂养使用,购买万能奶酪" },
			{ "uncheese2", "", "0", "0", "万能奶酪", "0", "恢复心情、喂养使用,购买万能奶酪" } };

	public BuyCheeseMainGameView(GameMainView gameMainView) {
		this.gameMainView = gameMainView;
	}

	public String[][] getprops() {
		return this.propsmsg;
	}

	public void toBuyCheese() {
		if(selButIndex == 0){
			MainServer.mPropType = 5;
		}else if(selButIndex == 1){
			MainServer.mPropType = 2;
		}
		else if(selButIndex == 2){
			MainServer.mPropType = 3;
		}
		else if(selButIndex == 3){
			MainServer.mPropType = 4;
		}
		gameMainView.toBuyCheese(props[selButIndex]);
	}

	public void close() {
		gameMainView.closeBuyCheeseDialog();
	}
}
