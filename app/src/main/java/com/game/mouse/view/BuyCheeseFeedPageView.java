package com.game.mouse.view;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

import com.game.lib9.Engine;
import com.game.lib9.L9Config;
import com.game.lib9.L9IState;
import com.game.lib9.L9Screen;
import com.game.mouse.gameinfo.Props;
import com.game.mouse.modle.service.UserDataManageService;
import com.game.mouse.screen.FeedPageScreen;
import com.game.mouse.screen.PassPageScreen;
import com.game.mouse.view.BuyCheeseView;
import com.game.mouse.view.game.GameMainView;
import com.model.control.MyImg;
import com.model.mainServer.MainServer;
import com.model.tool.PubToolKit;

public class BuyCheeseFeedPageView extends BuyCheeseView {
	private L9Screen screen;

	/**
	 * 道具code、扣费code、价格、购买数量、道具名、对应数量的code
	 */
	private String[][] propsmsg = {
			{ "uncheese0", "", "0", "0", "万能奶酪", "0", "恢复心情、喂养使用,购买万能奶酪" },
			{ "uncheese1", "", "0", "0", "万能奶酪", "0", "恢复心情、喂养使用,购买万能奶酪" },
			{ "uncheese2", "", "0", "0", "万能奶酪", "0", "恢复心情、喂养使用,购买万能奶酪" }};

	public BuyCheeseFeedPageView(L9Screen screen) {
		this.screen = screen;
	}

	public String[][] getprops() {
		return this.propsmsg;
	}

	public void toBuyCheese() {
		if (screen instanceof FeedPageScreen) {
			((FeedPageScreen) screen).toBuyCheese(props[selButIndex]);
		} else if (screen instanceof PassPageScreen) {
			((PassPageScreen) screen).toBuyCheese(props[selButIndex]);
		}

	}

	public void close() {
		if (screen instanceof FeedPageScreen) {
			((FeedPageScreen) screen).closeBuyCheeseDialog();
		} else if (screen instanceof PassPageScreen) {
			((PassPageScreen) screen).closeBuyCheeseDialog();
		}
	}
}
