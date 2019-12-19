package com.game.lib9;

import java.io.IOException;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import com.game.mouse.screen.MainMidlet;
import com.model.control.MyImg;
import com.model.tool.PubToolKit;

public class LoadPage {
	private static LoadPage loadPage;

//	private MyImg loadfristsx;

	private MyImg loadingBg;

	private MyImg loading;

	private int loadingW, loadingH;

	private int randTip;

	private String[] tips = {"奔跑的距离越远，获得的奖励越丰富"};

	public static synchronized LoadPage getInstance() {
		if (loadPage == null)
			loadPage = new LoadPage();
		return loadPage;
	}

	public void init() {
		randTip = PubToolKit.getRandomInt(loadPage.tips.length);
		Image localImg = null;

//		loadfristsx = new MyImg("/load/loadfristsx.png");
		loadingBg = new MyImg("load/loading_bg.png");
		loading = new MyImg("load/loading.png");
		loadingW = loading.getWidth();
		loadingH = loading.getHeight();

	}

	public void paint(Graphics g, int loadCount, int loadIndex) {
		switch (loadIndex) {
		case 1:
			paintLoad(g, loadCount);
			break;
		default:
			paintLoad(g, loadCount);
			break;
		}
	}

	public void paintLoad(Graphics g, int loadCount) {
		loadingBg.drawImage(g, 0, 0, 0);
		PubToolKit.drawString(tips[randTip], (int)(420*MainMidlet.scaleX), (int)(610 * MainMidlet.scaleY), (int)(450 * MainMidlet.scaleX), Font.getFont(
				Font.FACE_MONOSPACE, Font.STYLE_BOLD, 16), 0xffffff, g);
		loading.drawRegion(g, 0, loadingH, loadingW, loadingH, 0, 50, 645, 0);
//		loading.drawRegion(g, 0, 0, loadCount >= 100 ? (int)(loadingW*MainMidlet.scaleX) : loadCount
//				* loadingW / 100, loadingH, 0, 50, 615, 0);
	}

	public void paintLoad1(Graphics g, int loadCount) {
		g.fillRect(0, 0, (int)(L9Config.SCR_W*MainMidlet.scaleX), (int)(L9Config.SCR_H*MainMidlet.scaleY));
//		loadfristsx.drawImage(g, (int)(155*MainMidlet.scaleX), (int)(210*MainMidlet.scaleY), 0);
	}

	public void Release() {
		if (loadingBg != null) {
			loadingBg = null;
//			loadfristsx = null;
			loading = null;
			System.gc();
		}
	}
}
