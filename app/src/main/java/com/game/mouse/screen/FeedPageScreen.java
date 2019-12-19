package com.game.mouse.screen;

import java.util.Date;

import javax.microedition.lcdui.Graphics;

import com.game.lib9.Engine;
import com.game.lib9.L9Config;
import com.game.lib9.L9EngineLogic;
import com.game.lib9.L9Object;
import com.game.lib9.L9Screen;
import com.game.mouse.context.Config;
import com.game.mouse.context.UserInfo;
import com.game.mouse.gameinfo.GameInfo;
import com.game.mouse.gameinfo.Props;
import com.game.mouse.modle.UserMouse;
import com.game.mouse.modle.service.DataManageService;
import com.game.mouse.modle.service.UserDataManageService;
import com.game.mouse.view.BuyCheeseFeedPageView;
import com.game.mouse.view.BuyCheeseView;
import com.model.base.ParserTbl;
import com.model.control.MyImg;
import com.model.mainServer.MainConfig;
import com.model.mainServer.MainServer;
import com.model.tool.PubToolKit;

/**
 * 喂养页面
 * 
 * @author Administrator
 * 
 */
public class FeedPageScreen extends L9Screen {
	private Graphics g;

	private MyImg bg0Img;

	private MyImg bg1Img;

	private MyImg bgbottomImg;

	private MyImg barexp;

	private MyImg baratt;

	private MyImg barmood;

	private int barW, barH;

	private MyImg iconatt;

	private MyImg iconblood;

	private MyImg iconexp;

	private MyImg iconmood;

	private MyImg addattdes;

	private MyImg addexpdes;

	private MyImg addmooddes;

	private MyImg[] buts = new MyImg[3];

	private MyImg[] butsbathe = new MyImg[3];

	private MyImg[] butsplay = new MyImg[3];

	private MyImg[] butFonts = new MyImg[5];

	private MyImg[] headers = new MyImg[3];

	private MyImg[] mouseheaders = new MyImg[3];

	private MyImg[] mousenames = new MyImg[3];

	private MyImg[] mousedes = new MyImg[3];

	private MyImg[] mousedesname = new MyImg[3];

	private MyImg buttipheader1;

	private MyImg buttipheader2;

	private MyImg buttipheader3;

	private MyImg buttipheader4;

	private MyImg riskdes;

	private MyImg sellprice;

	private MyImg losecheess;

	private MyImg have;

	private MyImg num;

	private MyImg num0;

	private MyImg num1;

	private MyImg num2;

	private MyImg num3;

	private MyImg unit;

	private L9Object selButObj;

	private L9Object[] mousesObj;

	private int butFontW, butFontH;

	private int isInPay;

	private int selButindex;

	private int selRoleIndex;

	private UserMouse[] userMouses;

	/**
	 * 道具code、扣费code、价格、道具名、消耗奶酪数量
	 */
	private String[][] props = {
			{ "propbuymouse1", "", "0", "开通艾米", "0", "购买艾米角色" },
			{ "propbuymouse2", "", "0", "开通米妮", "0", "购买米妮角色" },
			{ "propfeed", "", "0", "喂养", "" + Config.feedNum + "", "喂食恢复心情、永久增加攻击、增加经验值" },
			{ "play", "", "0", "陪玩", "3", "陪玩增加亲密度、恢复心情" },
			{ "bathe", "", "0", "洗澡", "2", "洗澡增加亲密度、恢复心情" }
	};

	/**
	 * 每次增加属性值
	 */
	private int[][][] mousesAdd = {
			{ { 40, 3, 20 }, { 20, 0, 0 }, { 15, 0, 0 } },
			{ { 30, 3, 25 }, { 20, 0, 0 }, { 15, 0, 0 } },
			{ { 30, 2, 20 }, { 20, 0, 0 }, { 15, 0, 0 } } };

	private int mouseAniCount;

	private int cheese0Num;

	private BuyCheeseView buyCheeseDialog;

	private String buyAfterMethod = "";

	public void fristInit() {
		ParserTbl.getInstance().defineMedia(1, "tbl/feed.tbl");
	}

	public synchronized void Init(int loadCount) {
		switch (loadCount) {
		case 0:
			break;
		case 50:
			initData();
			break;
		case 60:
			initImgs(60);
			break;
		case 70:
			initImgs(70);
			break;
		case 80:
			initImgs(80);
			break;
		case 90:
			initImgs(90);
			break;
		case 100:
			initImgs(100);
			break;
		}
	}

	public void initData() {
		userMouses = DataManageService.getInsatnce().getAllUserMouse();
		String mouseCode = UserDataManageService.getInsatnce()
				.getOutPkUserMouse();
		selRoleIndex = Integer.parseInt(mouseCode);
		this.getPropMess();
	}

	public void getPropMess() {
		for (int i = 0; i < props.length; i++) {
			String[] prop = Props.getIntance().getPricePropByCode(props[i][0]);
			if (prop != null) {
				props[i][1] = prop[1];
				props[i][2] = prop[2];
			}
		}
		cheese0Num = UserDataManageService.getInsatnce().getPropNumByCode("0");
	}

	public void initImgs(int loadCount) {
		switch (loadCount) {
		case 60:
			bg0Img = new MyImg("feedpage/bg0.png");
			bg1Img = new MyImg("feedpage/bg1.png");
			bgbottomImg =GameInfo.getBottomImg(0);
			break;
		case 70:
			barexp = new MyImg("feedpage/barexp.png");
			baratt = new MyImg("feedpage/baratt.png");
			barmood = new MyImg("feedpage/barmood.png");
			barW = baratt.getWidth();
			barH = baratt.getHeight() / 2;

			iconblood = new MyImg("feedpage/iconblood.png");
			iconatt = new MyImg("feedpage/iconatt.png");
			iconexp = new MyImg("feedpage/iconexp.png");
			iconmood = new MyImg("feedpage/iconmood.png");

			addmooddes = new MyImg("feedpage/addmooddes.png");
			addattdes = new MyImg("feedpage/addattdes.png");
			addexpdes = new MyImg("feedpage/addexpdes.png");
			break;
		case 80:
			for (int i = 0; i < 3; i++) {
				buts[i] = new MyImg("feedpage/but" + i + ".png");
			}

			for (int i = 0; i < 3; i++) {
				butsbathe[i] = new MyImg("feedpage/butbathe" + i + ".png");
			}

			for (int i = 0; i < 3; i++) {
				butsplay[i] = new MyImg("feedpage/butplay" + i + ".png");
			}

			for (int i = 0; i < 5; i++) {
				butFonts[i] = new MyImg("feedpage/butfont" + i + ".png");
			}
			butFontW = butFonts[0].getWidth() / 2;
			butFontH = butFonts[0].getHeight();
			break;
		case 90:
			for (int i = 0; i < 3; i++) {
				headers[i] = new MyImg("feedpage/header" + i + ".png");
				mouseheaders[i] = new MyImg("feedpage/mouseheader" + i + ".png");
				mousenames[i] = new MyImg("feedpage/mousename" + i + ".png");
				mousedes[i] = new MyImg("feedpage/mousedes" + i + ".png");
				mousedesname[i] = new MyImg("feedpage/mousedesname" + i
						+ ".png");
			}

			num = new MyImg("feedpage/num.png");
			num0 = new MyImg("feedpage/num0.png");
			num1 = new MyImg("feedpage/num0.png");
			num2 = new MyImg("feedpage/num2.png");
			num3 = new MyImg("feedpage/num3.png");

			sellprice = new MyImg("feedpage/sellprice.png");
			losecheess = new MyImg("feedpage/losecheess.png");
			have = new MyImg("feedpage/have.png");
			this.createunit();

			buttipheader1 = new MyImg("feedpage/buttipheader1.png");
			buttipheader2 = new MyImg("feedpage/buttipheader2.png");
			buttipheader3 = new MyImg("feedpage/buttipheader3.png");
			buttipheader4 = new MyImg("feedpage/buttipheader4.png");
			riskdes = new MyImg("feedpage/riskdes.png");

			selButObj = new L9Object(null, 4, "feedpage/selbut.png", 100, 100,
					120 + 75 + 70, 380, -1, false, false, true, 2);
			selButObj.setNextFrameTime(2);
			break;
		case 100:
			mousesObj = new L9Object[3];
			for (int i = 0; i < mousesObj.length; i++) {
				int animation = userMouses[i].isMouseState() == 2 ? 0 : 3;
				mousesObj[i] = new L9Object(null, "mouse" + i, 2, 315 + 320, 405 + 120, -1,
						animation, false, true, true, 2, 0);
				mousesObj[i].setNextFrameTime(4);
			}
			break;
		}
	}

	/**
	 * 实例化单位
	 */
	public void createunit() {
		String priceName = MainServer.getInstance().getPriceName();
		if ("元".equals(priceName)) {
			unit = new MyImg("feedpage/unity.png");
		} else if ("元宝".equals(priceName)) {
			unit = new MyImg("feedpage/unityb.png");
		} else if ("TV豆".equals(priceName)) {
			unit = new MyImg("feedpage/unittvd.png");
		} else if ("TV币".equals(priceName)) {
			unit = new MyImg("feedpage/unittvb.png");
		} else if ("代币".equals(priceName)) {
			unit = new MyImg("feedpage/unitdb.png");
		} else if ("i豆".equals(priceName)) {
			unit = new MyImg("feedpage/unitid.png");
		} else if ("沃金币".equals(priceName)) {
			unit = new MyImg("feedpage/unitwjb.png");
		} else {
			unit = new MyImg("feedpage/unityxb.png");
		}
	}

	public void Paint() {
		// TODO Auto-generated method stub
		if (this.g == null) {
			this.g = Engine.FG;
		}
		bg1Img.drawImage(g, 0, 0, 0);
		mousesObj[selRoleIndex].paintFrame(g);
		bg0Img.drawImage(g, 0, 0, 0);
		this.paintMouseMes();
		this.paintBut();
		bgbottomImg
				.drawImage(g, 0, L9Config.SCR_H - bgbottomImg.getHeight(), 0);
		if (isInPay == 2) {
			MainServer.getInstance().paint(g);
		} else if (isInPay == 1 && buyCheeseDialog != null) {
			buyCheeseDialog.paint(g);
		}
	}

	/**
	 * 画选中老鼠的信息
	 */
	public void paintMouseMes() {
		headers[selRoleIndex].drawImage(g, 93, 85, g.VCENTER | g.HCENTER);
		PubToolKit.drawNum(g, userMouses[selRoleIndex].getLv(), num.getImg(),
				userMouses[selRoleIndex].getLv() >= 10 ? 38 : 47, 170, 0, 0,
				true);
		mousenames[selRoleIndex].drawImage(g, 220, 20, 0);
		for (int i = 0; i < userMouses[selRoleIndex].getMaxhp(); i++) {
			iconblood.drawImage(g, 310 + i * 55, 20, 0);
		}
		int showbarW = barW * userMouses[selRoleIndex].getExp() / 100;
		barexp.drawRegion(g, 0, barH, barW, barH, 0, 255, 83, 0);
		barexp.drawRegion(g, 0, 0, showbarW > barW ? barW : showbarW, barH, 0,
				255, 83, 0);
		PubToolKit.drawString(g, this.num3.getImg(), userMouses[selRoleIndex].getExp() + "/" + 100, "/0123456789", 395, 97, 22, 25, g.VCENTER
				| g.HCENTER, 0, 2, -1);
		iconexp.drawImage(g, 185, 70, 0);

		int lvaddatt = 100 * mousesAdd[selRoleIndex][0][1]
				/ mousesAdd[selRoleIndex][0][2];
		showbarW = barW * userMouses[selRoleIndex].getNowAttByMood()
				/ userMouses[selRoleIndex].getMaxAtt(lvaddatt);
		baratt.drawRegion(g, 0, barH, barW, barH, 0, 600, 83, 0);
		baratt.drawRegion(g, 0, 0, showbarW > barW ? barW : showbarW, barH, 0,
				600, 83, 0);

		PubToolKit.drawString(g, this.num3.getImg(), userMouses[selRoleIndex]
				.getNowAttByMood()
				+ "/" + userMouses[selRoleIndex].getMaxAtt(lvaddatt),
				"/0123456789", 730, 97, 22, 25, g.VCENTER | g.HCENTER, 0, 2, -1);
		iconatt.drawImage(g, 535, 70, 0);
		showbarW = barW * userMouses[selRoleIndex].getNowMoodByTime() / 100;
		showbarW = showbarW > 0 ? showbarW : 0;
		barmood.drawRegion(g, 0, barH, barW, barH, 0, 960, 83, 0);
		barmood.drawRegion(g, 0, 0, showbarW > barW ? barW : showbarW, barH, 0,
				960, 83, 0);
		PubToolKit.drawString(g, this.num3.getImg(), userMouses[selRoleIndex]
				.getNowMoodByTime()
				+ "/" + 100, "/0123456789", 1100, 97, 22, 25, g.VCENTER
				| g.HCENTER, 0, 2, -1);
		iconmood.drawImage(g, 900, 70, 0);
		this.paintmousedesc();
		PubToolKit.drawNum(g, cheese0Num, this.num2.getImg(), 1088, 523, 0, 0,
				true);
	}

	/**
	 * 画老鼠描述信息
	 */
	public void paintmousedesc() {
		switch (selButindex) {
		case 0:
			mousedesname[selRoleIndex].drawImage(g, 1025, 188, 0);
			mousedes[selRoleIndex].drawImage(g, 990, 238, 0);
			if (userMouses[selRoleIndex].isIsbuy()) {
				have.drawImage(g, 1020, 465, 0);
			} else {
//				sellprice.drawImage(g, 505, 285, 0);
				int propIndex = selRoleIndex == 2 ? 1 : 0;
//				PubToolKit.drawNum(g, Integer.parseInt(props[propIndex][2]),
//						num0.getImg(),
//						555 - (props[propIndex][2].length() - 1) * 3, 288, 0,
//						0, true);
//				unit.drawImage(g, 575, 286, 0);
			}
			break;
		case 1:
			buttipheader1.drawImage(g, 520 + 505, 128 + 140 - 80, 0);
			iconmood.drawImage(g, 470 + 520 - 30, 158 + 175 - 80, 0);
			addmooddes.drawImage(g, 510 + 555 - 30, 163 + 175 - 80, 0);
			PubToolKit.drawNum(g, mousesAdd[selRoleIndex][0][0], num1.getImg(),
					mousesAdd[selRoleIndex][0][0] >= 10 ? (580 + 555) : (585 + 555), 165 + 175 - 80, 0, 0,
					true);
			iconatt.drawImage(g, 470 + 520 - 30, 203 + 195 - 80, 0);
			addattdes.drawImage(g, 510 + 555 - 30, 213 + 195 - 80, 0);
			PubToolKit.drawNum(g, mousesAdd[selRoleIndex][0][1], num1.getImg(),
					mousesAdd[selRoleIndex][0][1] >= 10 ? (580 + 555) : (585 + 555), 215 + 195 - 80, 0, 0,
					true);
			iconexp.drawImage(g, 470 + 520 - 30, 248 + 215 - 80, 0);
			addexpdes.drawImage(g, 510 + 555 - 30, 258 + 215 - 80, 0);
			PubToolKit.drawNum(g, mousesAdd[selRoleIndex][0][2], num1.getImg(),
					mousesAdd[selRoleIndex][0][2] >= 10 ? (580 + 555) : (585 + 555), 260 + 215 - 80, 0, 0,
					true);
//			if (cheese0Num > Integer.parseInt(props[2][4])) {
				losecheess.drawImage(g, 485 + 520, 285 + 235 - 80, 0);
				PubToolKit.drawNum(g, Integer.parseInt(props[2][4]), num0
						.getImg(), 595 + 600, 288 + 235 - 80, 0, 0, true);
//			} else {
//				sellprice.drawImage(g, 505, 285, 0);
//				PubToolKit.drawNum(g, Integer.parseInt(props[2][2]), num0
//						.getImg(), 555, 288, 0, 0, true);
//				unit.drawImage(g, 575, 286, 0);
//			}
			break;
		case 2:
			buttipheader2.drawImage(g, 520 + 505, 128 + 140 - 80, 0);
			iconmood.drawImage(g, 470 + 520 - 30, 158 + 175 - 80, 0);

			addmooddes.drawImage(g, 510 + 555 - 30, 163 + 175 - 80, 0);
			PubToolKit.drawNum(g, mousesAdd[selRoleIndex][1][0], num1.getImg(),
					mousesAdd[selRoleIndex][1][0] >= 10 ? (580 + 555) : (585 + 555), 165 + 175 - 80, 0, 0,
					true);
			iconatt.drawImage(g, 470 + 520 - 30, 203 + 195 - 80, 0);
			addattdes.drawImage(g, 510 + 555 - 30, 213 + 195 - 80, 0);
			PubToolKit.drawNum(g, mousesAdd[selRoleIndex][1][1], num1.getImg(),
					mousesAdd[selRoleIndex][1][1] >= 10 ? (580 + 555) : (585 + 555), 215 + 195 - 80, 0, 0,
					true);
			iconexp.drawImage(g, 470 + 520 - 30, 248 + 215 - 80, 0);
			addexpdes.drawImage(g, 510 + 555 - 30, 258 + 215 - 80, 0);
			PubToolKit.drawNum(g, mousesAdd[selRoleIndex][1][2], num1.getImg(),
					mousesAdd[selRoleIndex][1][2] >= 10 ? (580 + 555) : (585 + 555), 260 + 215 - 80, 0, 0,
					true);
			losecheess.drawImage(g, 485 + 520, 285 + 235 - 80, 0);
			PubToolKit.drawNum(g, Integer.parseInt(props[3][4]), num0.getImg(),
					595 + 600, 288 + 235 - 80, 0, 0, true);
			break;
		case 3:
			buttipheader3.drawImage(g, 520 + 505, 128 + 140 - 80, 0);
			iconmood.drawImage(g, 470 + 520 - 30, 158 + 175 - 80, 0);
			addmooddes.drawImage(g, 510 + 555 - 30, 163 + 175 - 80, 0);
			PubToolKit.drawNum(g, mousesAdd[selRoleIndex][2][0], num1.getImg(),
					mousesAdd[selRoleIndex][2][0] >= 10 ? (580 + 555) : (585 + 555), 165 + 175 - 80, 0, 0,
					true);
			iconatt.drawImage(g, 470 + 520 - 30, 203 + 195 - 80, 0);
			addattdes.drawImage(g, 510 + 555 - 30, 213 + 195 - 80, 0);
			PubToolKit.drawNum(g, mousesAdd[selRoleIndex][2][1], num1.getImg(),
					mousesAdd[selRoleIndex][2][1] >= 10 ? (580 + 555) : (585 + 555), 215 + 195 - 80, 0, 0,
					true);
			iconexp.drawImage(g, 470 + 520 - 30, 248 + 215 - 80, 0);
			addexpdes.drawImage(g, 510 + 555 - 30, 258 + 215 - 80, 0);
			PubToolKit.drawNum(g, mousesAdd[selRoleIndex][2][2], num1.getImg(),
					mousesAdd[selRoleIndex][2][2] >= 10 ? (580 + 555) : (585 + 555), 260 + 215 - 80, 0, 0,
					true);
			losecheess.drawImage(g, 485 + 520, 285 + 235 - 80, 0);
			PubToolKit.drawNum(g, Integer.parseInt(props[4][4]), num0.getImg(),
					595 + 600, 288 + 235 - 80, 0, 0, true);
			break;
		case 4:
			buttipheader4.drawImage(g, 520 + 505, 128 + 140 - 80, 0);
			riskdes.drawImage(g, 485 + 505, 158 + 160 - 80, 0);
			break;
		}
	}

	/**
	 * 画按钮
	 */
	public void paintBut() {
		buts[0].drawImage(g, 75 - 15 + 75, 408 + 85 + 50, 0);
		buts[1].drawImage(g, 175 + 55 - 15 + 115, 408 + 85 + 50, 0);
		butsplay[selRoleIndex].drawImage(g, 275 + 110 - 15 + 155, 408 + 85 + 50, 0);
		butsbathe[selRoleIndex].drawImage(g, 375 + 165 - 15 + 195, 408 + 85 + 50, 0);
		buts[2].drawImage(g, 475 + 240 - 15 + 230, 403 + 20 + 125, 0);
		for (int i = 0; i < 5; i++) {
			int px = (i == 2) ? -5 : 0;
			int py = (i == 0) ? -5 : 0;
			if(i == 0){
				py += 5;
			}
			butFonts[i].drawRegion(g, selButindex == i ? butFontW : 0, 0,
					butFontW, butFontH, 0, 170 + i * 200 + px, 455 + py + 175, 0);
		}
		this.paintMuoseBut();
	}

	public void paintMuoseBut() {
		if (selButindex == 0) {
			mouseheaders[0].drawImage(g, 175, 235, 0);
			mouseheaders[1].drawImage(g, 175, 300 + 69 - 30, 0);
			mouseheaders[2].drawImage(g, 175, 350 + 153 - 60, 0);
			selButObj.setPosY(selRoleIndex == 0 ? (310 + 17) : selRoleIndex == 1 ? (360 + 50 + 20) : (410 + 83 + 40));
			selButObj.paintFrame(g);
		}
	}

	public void Update() {
		mousesObj[selRoleIndex].doAllFrame();
		for (int i = 0; i < L9EngineLogic.getInstance().objects.size; i++) {
			((L9Object) L9EngineLogic.getInstance().objects.values[i])
					.doAllFrame();
		}
		L9EngineLogic.getInstance().flushRemoveList();
		L9EngineLogic.getInstance().flushAddList();
		if (isInPay == 0) {
			selButObj.doAllFrame();
				keyPress();
		} else if (isInPay == 1) {
			if (buyCheeseDialog != null) {
				buyCheeseDialog.update();
			}
		} else {
			isInPay();
		}
		changeMouseAnimation();
	}

	public void isInPay() {
		if (isInPay == 2) {
			int keyCode = Engine.getKeyCode();
			MainServer.getInstance().update(
					Engine.getKeyCodeByLogicKey(keyCode));
			if (MainServer.getInstance().isPayEnd()) {
				isInPay = buyCheeseDialog != null ? 1 : 0;
				if (MainServer.getInstance().isBuySuccess()) {
					System.out.println(">>>FeedPageScreen>>>>>>>购买道具成功<<<<<<<<<<");
					buySucc();
				} else {
					System.out.println(">>>FeedPageScreen>>>>>>>购买道具失败<<<<<<<<<<");
				}
			}
		}
	}

	/**
	 * 改变anu状态
	 */
	public void changeMouseAnimation() {
		if (mousesObj[selRoleIndex] != null && !mousesObj[selRoleIndex].isOver) {
			if (userMouses[selRoleIndex].isMouseState() == 2) {
				if (mousesObj[selRoleIndex].getCurrentFrame() >= mousesObj[selRoleIndex]
						.getFrameCount() - 1) {
					mouseAniCount++;
				}
				if (mousesObj[selRoleIndex].getAnimation() != 1
						&& mouseAniCount >= 2) {
					mousesObj[selRoleIndex].setAnimation(1);
					mouseAniCount = 0;
				} else if (mousesObj[selRoleIndex].getAnimation() != 0
						&& mouseAniCount >= 1) {
					mousesObj[selRoleIndex].setAnimation(0);
					mouseAniCount = 0;
				}
			} else if (userMouses[selRoleIndex].isMouseState() == 0) {
				if (mousesObj[selRoleIndex].getCurrentFrame() >= mousesObj[selRoleIndex]
						.getFrameCount() - 1) {
					mouseAniCount++;
				}
				if (mousesObj[selRoleIndex].getAnimation() != 3
						&& mouseAniCount >= 2) {
					mousesObj[selRoleIndex].setAnimation(3);
					mouseAniCount = 0;
				} else if (mousesObj[selRoleIndex].getAnimation() != 2
						&& mouseAniCount >= 1) {
					mousesObj[selRoleIndex].setAnimation(2);
					mouseAniCount = 0;
				}
			} else {
				if (mousesObj[selRoleIndex].getAnimation() != 0) {
					mousesObj[selRoleIndex].setAnimation(0);
					mouseAniCount = 0;
				}
			}
		}
	}

	public void Release() {
		// TODO Auto-generated method stub


	}

	public void keyPress() {
		int keyCode = Engine.getKeyCode();
		switch (keyCode) {
		case Engine.K_KEY_LEFT:
			this.keyleft();
			break;
		case Engine.K_KEY_UP:
			this.keyup();
			break;
		case Engine.K_KEY_DOWN:
			this.keydown();
			break;
		case Engine.K_KEY_RIGHT:
			this.keyright();
			break;
		case Engine.K_KEY_FIRE:
			this.keyfire();
			break;
		case Engine.K_KEY_BACK:
//		case Engine.K_KEY_NUM0:
			this.keynum0();
			break;
		}
	}

	public void keyleft() {
		if (Config.isfeedcankeyleft || userMouses[selRoleIndex].isIsbuy()) {
			selButindex = selButindex > 0 ? selButindex - 1 : 4;
		}
	}

	public void keyright() {
		if (Config.isfeedcankeyleft || userMouses[selRoleIndex].isIsbuy()) {
			selButindex = selButindex < 4 ? selButindex + 1 : 0;
		}
	}

	public void keyup() {
		if (selButindex == 0) {
			selRoleIndex = selRoleIndex > 0 ? selRoleIndex - 1 : 2;
		}
	}

	public void keydown() {
		if (selButindex == 0) {
			selRoleIndex = selRoleIndex < 2 ? selRoleIndex + 1 : 0;
		}
	}

	public void keyfire() {
		if (!userMouses[selRoleIndex].isIsbuy()) {	//判断老鼠是否拥有
			toBuyMouse();
			return;
		}
		switch (selButindex) {
		case 0:		//角色
			selButindex = 1;
			break;
		case 1:		//喂养
			System.out.println("this is Integer.parseInt(props[2][4]) = " + Integer.parseInt(props[2][4]));
			if (cheese0Num >= Integer.parseInt(props[2][4])) {
				System.out.println(" ----- this is 1 ------ ");
				feedMouse();
			} else {
				System.out.println(" ----- this is 2 ------ ");
//				toBuyFeed();
				tipBuyCheess();
			}
			break;
		case 2:		//陪玩
			if (cheese0Num >= Integer.parseInt(props[3][4])) {
				playMouse();
			} else {
				tipBuyCheess();
			}
			break;
		case 3:		//洗澡
			if (cheese0Num >= Integer.parseInt(props[4][4])) {
				batheMouse();
			} else {
				tipBuyCheess();
			}
			break;
		case 4:		//冒险
			gotoGame();
			break;
		}
		savaData();
	}

	public void gotoGame() {
		if (!userMouses[selRoleIndex].isOutPk()) {
			String code = UserDataManageService.getInsatnce()
					.getOutPkUserMouse();
			final UserMouse restMouse = DataManageService.getInsatnce()
					.getUserMouseByCode(code);
			userMouses[selRoleIndex].setMood(userMouses[selRoleIndex]
					.getNowMoodByTime());
			userMouses[selRoleIndex].setUpMoodTime(new Date().getTime());
			restMouse.setMood(restMouse.getNowAttByMood());
			restMouse.setUpMoodTime(new Date().getTime());
			new Thread() {
				public void run() {
					UserDataManageService.getInsatnce().setUserMouseOutPk(
							userMouses[selRoleIndex], restMouse);
				}
			}.start();
		}
		PassPageScreen passPageScreen = new PassPageScreen(Integer
				.parseInt(userMouses[selRoleIndex].getCode()));
		MainMidlet.engine.changeState(passPageScreen);
	}

	public void keynum0() {
		HomePageScreen homePageScreen = new HomePageScreen(1);
		MainMidlet.engine.changeState(homePageScreen);
	}

	/**
	 * 购买角色
	 */
	public void toBuyMouse() {
		isInPay = 2;
		buyAfterMethod = "buyMouse";
		int propIndex = selRoleIndex == 1 ? 1 : 0;
		switch (Config.regionType) {
		case MainConfig.Type_GDDX:
		case MainConfig.Type_SXDX:
			MainServer.getInstance().buyProp(props[propIndex][1],
					props[propIndex][2], props[propIndex][3], false, -1,
					props[propIndex][5], false, false,
					"/propimg/" + (propIndex + 4) + ".png");
			break;
		default:	//计费接口

			MainServer.mPropType = propIndex;

			MainServer.getInstance().buyProp(props[propIndex][1],
					props[propIndex][2], props[propIndex][3], false, -1,
					props[propIndex][5], false, false);
		}

	}

	/**
	 * 购买喂食
	 */
	public void toBuyFeed() {
		System.out.println("this is Config.regionType = " + Config.regionType);
		isInPay = 2;
		buyAfterMethod = "feedMouse";
		switch (Config.regionType) {
		case MainConfig.Type_GDDX:
		case MainConfig.Type_SXDX:
			MainServer.getInstance().buyProp(props[2][1], props[2][2],
					props[2][3], false, -1, props[2][5], false, false,
					"/propimg/" + (selRoleIndex + 4) + ".png");
			break;
		default:
			MainServer.getInstance().buyProp(props[2][1], props[2][2],
					props[2][3], false, -1, props[2][5], false, false);
		}
	}

	/**
	 * 购买奶酪对话框
	 */
	public void tipBuyCheess() {
		isInPay = 1;
		buyCheeseDialog = new BuyCheeseFeedPageView(this);
		buyCheeseDialog.init();
	}

	/**
	 * 购买成功
	 */
	public void buySucc() {
		if ("feedMouse".equals(buyAfterMethod)) {
			this.feedMouse();
		} else if ("buyMouse".equals(buyAfterMethod)) {
			this.buyMouse();
		} else if ("buyCheese".equals(buyAfterMethod)) {
			this.buyCheese();
		}
		savaData();
	}

	/**
	 * 喂食增加经验，并且永久增加攻击力
	 */
	public void feedMouse() {

		mousesObj[selRoleIndex].setAnimation(4);
		mouseAniCount = 0;
		int beforeAddLv = userMouses[selRoleIndex].getLv();
		userMouses[selRoleIndex].addNowMood(mousesAdd[selRoleIndex][0][0]);
		userMouses[selRoleIndex].addAtt(mousesAdd[selRoleIndex][0][1]);
		userMouses[selRoleIndex].addExp(mousesAdd[selRoleIndex][0][2]);
		int afferaddLv = userMouses[selRoleIndex].getLv();
		final boolean isAddScore = afferaddLv > beforeAddLv
				&& afferaddLv == userMouses[selRoleIndex].getMaxLv();
		new Thread() {
			public void run() {
				cheese0Num = cheese0Num - Integer.parseInt(props[2][4]) > 0 ? cheese0Num - Integer.parseInt(props[2][4]) : 0;
				UserDataManageService.getInsatnce().updateMouseProp("0", cheese0Num, true);

				UserDataManageService.getInsatnce().updateMouse(
						userMouses[selRoleIndex]);
				if (isAddScore) {
					UserInfo.score += 500;
					MainServer.getInstance().updateScore(UserInfo.score);
				}
			}
		}.start();
	}

	/**
	 * 陪玩恢复心情
	 */
	public void playMouse() {
		mousesObj[selRoleIndex].setAnimation(5);
		mouseAniCount = 0;
		userMouses[selRoleIndex].addNowMood(mousesAdd[selRoleIndex][1][0]);
		new Thread() {
			public void run() {
				cheese0Num = cheese0Num - Integer.parseInt(props[3][4]) > 0 ? cheese0Num - Integer.parseInt(props[3][4]) : 0;
				UserDataManageService.getInsatnce().updateMouseProp("0", cheese0Num, true);
				UserDataManageService.getInsatnce().updateMouse(userMouses[selRoleIndex]);
			}
		}.start();
	}

	/**
	 * 洗澡恢复心情
	 */
	public void batheMouse() {
		mousesObj[selRoleIndex].setAnimation(6);
		mouseAniCount = 0;
		userMouses[selRoleIndex].addNowMood(mousesAdd[selRoleIndex][2][0]);
		new Thread() {
			public void run() {
				cheese0Num = cheese0Num - Integer.parseInt(props[4][4]) > 0 ? cheese0Num
						- Integer.parseInt(props[4][4])
						: 0;
				UserDataManageService.getInsatnce().updateMouseProp("0",
						cheese0Num, true);
				UserDataManageService.getInsatnce().updateMouse(
						userMouses[selRoleIndex]);
			}
		}.start();
	}

	/**
	 * 购买奶酪
	 */
	public void toBuyCheese(String[] props) {
		System.out.println(" ---- toBuyCheese ---- Config.regionType = " + Config.regionType);
		isInPay = 2;
		buyAfterMethod = "buyCheese";
		switch (Config.regionType) {
		case MainConfig.Type_GDDX:
		case MainConfig.Type_SXDX:
			MainServer.getInstance().buyProp(props[1], props[2], props[4],
					false, Integer.parseInt(props[3]), props[6], false, false,
					"/propimg/" + props[5] + ".png");
			break;
		default:		//计费接口
			MainServer.mPropType = BuyCheeseView.selButIndex + 2;

			MainServer.getInstance().buyProp(props[1], props[2], props[4],
					false, Integer.parseInt(props[3]), props[6], false, false);
		}

		//我写的逻辑
//		if(MainMidlet.Area.equals("SBY")){
//			System.out.println(" ---- 123456 ----- ");
//			MainServer.getInstance().buyProp(props[1], props[2], props[4],
//					false, Integer.parseInt(props[3]), props[6], false, false);
//		}
	}

	public void buyCheese() {
		String[] props = buyCheeseDialog.getBuyIngProp();
		cheese0Num += Integer.parseInt(props[3]);
		UserDataManageService.getInsatnce().updateMouseProp("0", cheese0Num, true);
		this.closeBuyCheeseDialog();
		switch (selButindex) {
		case 1:
			feedMouse();
			break;
		case 2:
			playMouse();
			break;
		case 3:
			batheMouse();
			break;
		}
	}

	public void buyMouse() {
		userMouses[selRoleIndex].setIsbuy(true);
		new Thread() {
			public void run() {
				UserDataManageService.getInsatnce().buyMouses(
						userMouses[selRoleIndex]);
				UserInfo.score += 50;
				MainServer.getInstance().updateScore(UserInfo.score);
			}
		}.start();
	}

	public void closeBuyCheeseDialog() {
		isInPay = 0;
		buyCheeseDialog = null;
	}

	public void savaData(){
		UserDataManageService.getInsatnce().mySaveGameData();
	}
}
