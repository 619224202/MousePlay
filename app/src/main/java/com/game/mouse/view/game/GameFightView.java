package com.game.mouse.view.game;

import java.util.Enumeration;
import java.util.Hashtable;

import javax.microedition.lcdui.Graphics;

import com.game.lib9.Engine;
import com.game.lib9.L9Config;
import com.game.lib9.L9EngineLogic;
import com.game.lib9.L9Object;
import com.game.mouse.context.Config;
import com.game.mouse.context.GameConfigInfo;
import com.game.mouse.context.UserInfo;
import com.game.mouse.gameinfo.GameInfo;
import com.game.mouse.gameinfo.Props;
import com.game.mouse.modle.Cat;
import com.game.mouse.modle.Mouse;
import com.game.mouse.modle.Pass;
import com.game.mouse.modle.UserMouse;
import com.game.mouse.modle.service.UserDataManageService;
import com.game.mouse.view.View;
import com.game.mouse.view.fightgame.child.FightEndGameHelpView;
import com.game.mouse.view.fightgame.child.FightEndGameReviveView;
import com.game.mouse.view.fightgame.child.FightEndGameRunAwayView;
import com.game.mouse.view.fightgame.child.FightEndGameStealMsgView;
import com.game.mouse.view.fightgame.child.FightHandView;
import com.game.mouse.view.fightgame.child.FightSpriteCatView;
import com.game.mouse.view.fightgame.child.FightSpriteMouseView;
import com.game.mouse.view.fightgame.child.FightSpriteView;
import com.game.mouse.view.fightgame.child.FightTimeCountView;
import com.game.mouse.view.fightgame.child.FightUpLvView;
import com.model.control.MyImg;
import com.model.mainServer.MainConfig;
import com.model.mainServer.MainServer;
import com.model.tool.PubToolKit;

public class GameFightView implements View {
	private GameFightMainView gameFightMainView;

	private MyImg bg;

	private MyImg headerbg0;

	private MyImg headername0;

	private MyImg header0;

	private MyImg headerbg1;

	private MyImg headername1;

	private MyImg header1;

	private MyImg headervs;

	private MyImg blood;

	private MyImg blue;

	private MyImg bottom;

	private MyImg[] buts = new MyImg[5];

	private MyImg[] butwins = new MyImg[3];

	private MyImg bgbottomImg;

	private MyImg num1;

	private boolean isWin;

	private int bloodW, bloodH;

	private int selButIndex;

	/**
	 * 第几种攻击方式
	 */
	private int attIndex;

	private FightTimeCountView fightTimeCountView;

	/**
	 * 当前角色
	 */
	private FightSpriteMouseView spriteMouseView;

	/**
	 * 招募的老鼠
	 */
	private FightSpriteMouseView recruitingSpriteMouseView;

	/**
	 * 对方的猫
	 */
	private FightSpriteCatView spriteCatView;

	private Hashtable clildSpriteBackViews = new Hashtable();

	private Hashtable clildSpriteBeforeViews = new Hashtable();

	private EndGameView endGameView;

	private Enumeration eClildSpriteViews;

	/**
	 * 道具code、扣费code、价格、购买数量、道具名、对应数量的code
	 */
	public String[][] props = {
			{ "propblue", "", "0", "0", "补满蓝", "-1", "补满蓝,使用猛攻、召唤消耗" },
			{ "propkill", "", "0", "0", "必杀", "-1", "购买必杀,很强的伤害" },
			{ "propblood", "", "0", "0", "回血胶囊", "2", "购买回血胶囊,恢复血量" },
			{ "proprevivepk", "", "0", "0", "复活再战", "-1", "购买复活,继续战斗" },
			{ "propreviveallpk", "", "0", "0", "全体复活再战", "-1", "购买全体复活,继续战斗" },
			{ "propbigsteal", "", "0", "0", "大偷取", "-1", "购买大偷取,偷取奶酪数量很多" },
			{ "propsteal", "", "0", "0", "小偷取", "-1", "购买小偷取,偷取奶酪数量多" },
			{ "propweaponstage", "", "0", "0", "进化武器", "-1", "购买进化武器" },
			{ "propweaponlv", "", "0", "0", "升级武器", "-1", "购买升级武器" } };
	/**
	 * 是否用户回合
	 */
	private boolean userRound;

	/**
	 * 回合进行中按键标识
	 */
	private boolean isUserRoundkey;

	/**
	 * 当招募的时候用户回合攻击次数
	 */
	private int userRoundAttCount;

	private boolean isInPay;

	private String buyAfterMethod = "";

	private MyImg propBloodImg;

	private MyImg num;

	private int propBloodNum;

	/**
	 * 本次战斗是否可以逃跑
	 */
	private boolean isRunAway;

	public GameFightView(GameFightMainView gameFightMainView, Cat cat) {
		this.gameFightMainView = gameFightMainView;
		spriteMouseView = new FightSpriteMouseView(this, this.gameFightMainView
				.getUserMouse(), 130 + 125 + 60, 400 + 150);
		spriteCatView = new FightSpriteCatView(this, cat, 500 + 475 - 60, 400 + 10 + 140);
		isRunAway = PubToolKit.getRandomInt(3) == 0;
	}

	public void init() {

	}

	public void init(int loadCount) {
		// TODO Auto-generated method stub
		switch (loadCount) {
		case 0:
			bg = new MyImg("gamepage/fight/bg/pk"
					+ this.gameFightMainView.getPass().getSceneCode() + ".png");
			break;
		case 20:
			spriteMouseView.init();
			break;
		case 50:
			spriteCatView.init();
			break;
		case 80:
			headerbg0 = new MyImg("gamepage/fight/headerbg0.png");
			headerbg1 = new MyImg("gamepage/fight/headerbg1.png");
			headervs = new MyImg("gamepage/fight/headervs.png");
			header0 = new MyImg("sprite/fight/mouse/header"
					+ spriteMouseView.getSprite().getCode() + ".png");
			headername0 = new MyImg("sprite/fight/mouse/name"
					+ spriteMouseView.getSprite().getCode() + ".png");
			headername1 = new MyImg("sprite/fight/cat/name"
					+ spriteCatView.getSprite().getCode() + ".png");
			header1 = new MyImg("sprite/fight/cat/header"
					+ spriteCatView.getSprite().getCode() + ".png");
			blood = new MyImg("gamepage/fight/blood.png");
			blue = new MyImg("gamepage/fight/blue.png");
			num1 = new MyImg("gamepage/fight/num1.png");
			bottom = new MyImg("gamepage/fight/bottom.png");
			for (int i = 0; i < 5; i++) {
				if (i != 1) {
					buts[i] = new MyImg("gamepage/fight/but" + i + ".png");
				}
			}
			for (int i = 0; i < butwins.length; i++) {
				butwins[i] = new MyImg("gamepage/fight/butwin" + i + ".png");
			}
			bloodW = blood.getWidth();
			bloodH = blood.getHeight() / 2;
			propBloodImg = new MyImg("gamepage/fight/iconblood.png");
			num = new MyImg("shoppage/num.png");
			break;
		case 90:
			newBgbottomImg();
			break;
		case 100:
			this.getPropMess();
			startPK();
			break;
		}
	}

	/**
	 * 根据武器初始化底部提示
	 */
	public void newBgbottomImg() {
		UserMouse userMouse = (UserMouse) spriteMouseView.getSprite();
		if (userMouse.getWeaponStage() < 1) {
			bgbottomImg = GameInfo.getBottomImg(2);
		} else {
			bgbottomImg = GameInfo.getBottomImg(3);
		}
		String imgName = "gamepage/fight/but1" + userMouse.getCode() + ""
				+ userMouse.getWeaponStage() + ".png";
		buts[1] = new MyImg(imgName);
	}

	public void getPropMess() {
		for (int i = 0; i < props.length; i++) {
			String[] prop = Props.getIntance().getPricePropByCode(props[i][0]);
			if (prop != null) {
				props[i][1] = prop[1];
				props[i][2] = prop[2];
				props[i][3] = prop[3];
			}
		}
		propBloodNum = UserDataManageService.getInsatnce().getPropNumByCode(
				props[2][5]);
	}

	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		bg.drawImage(g, 0, 0, 0);
		eClildSpriteViews = clildSpriteBackViews.elements();
		while (eClildSpriteViews.hasMoreElements()) {
			((View) eClildSpriteViews.nextElement()).paint(g);
		}
		if (userRound) {
			spriteCatView.paint(g);
			spriteMouseView.paint(g);
			if (recruitingSpriteMouseView != null) {
				recruitingSpriteMouseView.paint(g);
			}
		} else {
			spriteMouseView.paint(g);
			if (recruitingSpriteMouseView != null) {
				recruitingSpriteMouseView.paint(g);
			}
			spriteCatView.paint(g);
		}
		eClildSpriteViews = clildSpriteBeforeViews.elements();
		while (eClildSpriteViews.hasMoreElements()) {
			((View) eClildSpriteViews.nextElement()).paint(g);
		}
		if (fightTimeCountView != null)
			fightTimeCountView.paint(g);
		this.paintHeader(g);
		this.paintBottom(g);
		if (endGameView != null) {
			endGameView.paint(g);
		}
		if (isInPay) {
			MainServer.getInstance().paint(g);
		}
	}

	public void paintHeader(Graphics g) {
		this.paintMouseHeader(g);
		headervs.drawImage(g, L9Config.SCR_W / 2 - 20, 20, 0);
		this.paintCatHeader(g);
		this.paintProp(g);
	}

	/**
	 * 画老鼠的头像
	 * 
	 * @param g
	 */
	public void paintMouseHeader(Graphics g) {
		header0.drawImage(g, 70, 56 + 14, g.VCENTER | g.HCENTER);
        headerbg0.drawImage(g, 0, 0, 0);
		headername0.drawImage(g, 130 + 35, 12, 0);
		PubToolKit.drawNum(g, spriteMouseView.getSprite().getLv(), num1.getImg(), spriteMouseView.getSprite().getLv() >= 10 ? 105 : 110, 25 - 10, 0, 0, true);
		blood.drawRegion(g, 0, bloodH, bloodW, bloodH, 0, 118 + 20, 45 + 10, 0);
		int drawBloodW = spriteMouseView.getSprite().getFightMaxHp() == 0 || spriteMouseView.getSprite().getFightHp() > spriteMouseView.getSprite().getFightMaxHp() ? bloodW : bloodW
				* spriteMouseView.getSprite().getFightHp()
				/ spriteMouseView.getSprite().getFightMaxHp();

		blood.drawRegion(g, 0, 0, drawBloodW, bloodH, 0, 118 + 20, 45 + 10, 0);
		blue.drawRegion(g, 0, bloodH, bloodW, bloodH, 0, 118 + 20, 65 + 15, 0);

		int drawBlueW = spriteMouseView.getSprite().getFightMaxBlue() == 0
				|| spriteMouseView.getSprite().getFightBlue() > spriteMouseView
						.getSprite().getFightMaxBlue() ? bloodW : bloodW
				* spriteMouseView.getSprite().getFightBlue()
				/ spriteMouseView.getSprite().getFightMaxBlue();
		blue.drawRegion(g, 0, 0, drawBlueW, bloodH, 0, 118 + 20, 65 + 15, 0);
	}

	public void paintProp(Graphics g) {
		propBloodImg.drawImage(g, 120, 90 + 50, 0);
		PubToolKit.drawString(g, this.num.getImg(), "X"
				+ (propBloodNum > 99 ? 99 : propBloodNum), "X0123456789", 160,
				120 + 50, 27, 31, g.LEFT | g.BOTTOM, 0, 2, -1);
	}

	/**
	 * 画猫的头像
	 * 
	 * @param g
	 */
	public void paintCatHeader(Graphics g) {
		header1.drawImage(g, L9Config.SCR_W / 2 + 265 + 300 + 15, 56 + 10, g.VCENTER | g.HCENTER);
        headerbg1.drawImage(g, L9Config.SCR_W / 2 + 50 + 295, 0, 0);
		headername1.drawImage(g, L9Config.SCR_W / 2 + 360, 12, 0);
		int lvX = L9Config.SCR_W / 2 + (spriteCatView.getSprite().getLv() >= 10 ? (180 + 335) : (185 + 335));
		PubToolKit.drawNum(g, spriteCatView.getSprite().getLv(), num1.getImg(),
				lvX, 25 - 10, 0, 0, true);
		blood.drawRegion(g, 0, bloodH, bloodW, bloodH, 0,
				L9Config.SCR_W / 2 + 70 + 290, 52 + 20, 0);
		int drawBloodW = spriteCatView.getSprite().getFightMaxHp() == 0
				|| spriteCatView.getSprite().getFightHp() > spriteCatView
						.getSprite().getFightMaxHp() ? bloodW : bloodW
				* spriteCatView.getSprite().getFightHp()
				/ spriteCatView.getSprite().getFightMaxHp();
		blood.drawRegion(g, 0, 0, drawBloodW, bloodH, 0,
				L9Config.SCR_W / 2 + 70 + 290, 52 + 20, 0);
	}

	public void paintBottom(Graphics g) {
		this.paintBottomBut(g);
		bgbottomImg
				.drawImage(g, 0, L9Config.SCR_H - bgbottomImg.getHeight(), 0);
	}

	/**
	 * 画底部按钮
	 * 
	 * @param g
	 */
	public void paintBottomBut(Graphics g) {
		bottom.drawImage(g, L9Config.SCR_W / 2, 470 + 155, g.VCENTER | g.HCENTER);
		if (isWin) {
			this.paintBottomButwin(g);
		} else {
			this.paintBottomButFight(g);
		}
	}

	/**
	 * 画战斗中底部按钮
	 * 
	 * @param g
	 */
	public void paintBottomButFight(Graphics g) {
		for (int i = 0; i < 5; i++) {
			if (selButIndex == i) {
				buts[i].drawRegion(g, buts[i].getWidth() / 2, 0, buts[i]
						.getWidth() / 2, buts[i].getHeight(), 0, 300 + i * 140,
						580, 0);
			} else {
				buts[i].drawRegion(g, 0, 0, buts[i].getWidth() / 2, buts[i]
						.getHeight(), 0, 300 + i * 140, 580, 0);
			}
		}
	}

	/**
	 * 画胜利时的底部按钮
	 * 
	 * @param g
	 */
	public void paintBottomButwin(Graphics g) {
		for (int i = 0; i < 3; i++) {
			if (selButIndex == i) {
				butwins[i].drawRegion(g, buts[i].getWidth() / 2, 0, buts[i]
						.getWidth() / 2, buts[i].getHeight(), 0, 470 + i * 140,
						580, 0);
			} else {
				butwins[i].drawRegion(g, 0, 0, buts[i].getWidth() / 2, buts[i]
						.getHeight(), 0, 470 + i * 140, 580, 0);
			}
		}
	}

	public void update() {
		// TODO Auto-generated method stub
		spriteMouseView.update();
		if (recruitingSpriteMouseView != null) {
			recruitingSpriteMouseView.update();
		}
		spriteCatView.update();
		for (int i = 0; i < L9EngineLogic.getInstance().objects.size; i++) {
			((L9Object) L9EngineLogic.getInstance().objects.values[i])
					.doAllFrame();
		}
		L9EngineLogic.getInstance().checkCollision(32, 8 | 16);// 检查猫、老鼠被技能相撞
		L9EngineLogic.getInstance().flushRemoveList();
		L9EngineLogic.getInstance().flushAddList();
		eClildSpriteViews = clildSpriteBackViews.elements();
		while (eClildSpriteViews.hasMoreElements()) {
			((View) eClildSpriteViews.nextElement()).update();
		}

		eClildSpriteViews = clildSpriteBeforeViews.elements();
		while (eClildSpriteViews.hasMoreElements()) {
			((View) eClildSpriteViews.nextElement()).update();
		}

		if (!isInPay) {
			keyPress();
			if (endGameView != null) {
				endGameView.update();
			} else if (fightTimeCountView != null) {
				fightTimeCountView.update();
			}
		} else {
			isInPay();
		}
	}

	public void isInPay() {
		if (isInPay) {
			int keyCode = Engine.getKeyCode();
			MainServer.getInstance().update(
					Engine.getKeyCodeByLogicKey(keyCode));
			if (MainServer.getInstance().isPayEnd()) {
				isInPay = false;
				if (MainServer.getInstance().isBuySuccess()) {
					System.out.println(">>>GameFightView>>>>>>>购买道具成功<<<<<<<<<<");
					buySucc();
				} else {
					System.out.println(">>>GameFightView>>>>>>>购买道具失败<<<<<<<<<<");
					buyFail();
				}
			}
		}
	}

	public void keyPress() {
		if (endGameView != null) {
			endGameView.keyPress();
		} else {
			int keyCode = Engine.getKeyCode();
			switch (keyCode) {
			case Engine.K_KEY_BACK:
//			case Engine.K_KEY_NUM0:
				keyNum0();
				break;
			case Engine.K_KEY_NUM9:
				keyNum9();
				break;
			case Engine.K_KEY_LEFT:
				keyleft();
				break;
			case Engine.K_KEY_RIGHT:
				keyright();
				break;
			case Engine.K_KEY_FIRE:
				keyfire();
				break;
			case Engine.K_KEY_UP:
				keyup();
				break;
			case Engine.K_KEY_NUM1:
				keyNum1();
				break;
			case Engine.K_KEY_NUM2:
				keyNum2();
				break;
			case Engine.K_KEY_NUM3:
				keyNum3();
				break;
			case Engine.K_KEY_NUM4:
				keyNum4();
				break;
			case Engine.K_KEY_NUM5:
				keyNum5();
				break;
			default:

			}
		}
	}

	public void keyNum0() {
		if (isWin) {
			this.goToGameMain(true);
		} else {
			this.tipRunAway();
		}
	}

	public void keyNum1() {
		if (isWin) {
			this.toBuyBigSteal();
		} else {
			if (userRound
					&& this.isUserRoundkey
					&& ((recruitingSpriteMouseView != null && selButIndex != 3) || recruitingSpriteMouseView == null)) {
				this.isUserRoundkey = false;
				this.userRespondUserRound(0, false, false);
			}
		}
	}

	public void keyNum2() {
		if (isWin) {
			this.toBuySteal();
		} else {
			if (userRound
					&& this.isUserRoundkey
					&& ((recruitingSpriteMouseView != null && selButIndex != 3) || recruitingSpriteMouseView == null)) {
				this.isUserRoundkey = false;
				this.userRespondUserRound(1, false, false);
			}
		}
	}

	public void keyNum3() {
		if (isWin) {
			this.goToGameMain(true);
		} else {
			if (userRound && this.isUserRoundkey && ((recruitingSpriteMouseView != null && selButIndex != 3) || recruitingSpriteMouseView == null)) {
				this.isUserRoundkey = false;
				this.userRespondUserRound(2, false, false);
			}
		}
	}

	public void keyNum4() {
		if (userRound && this.isUserRoundkey
				&& recruitingSpriteMouseView == null) {
			this.isUserRoundkey = false;
			this.userRespondUserRound(3, false, false);
		}
	}

	public void keyNum5() {
		if (userRound
				&& this.isUserRoundkey
				&& ((recruitingSpriteMouseView != null && selButIndex != 3) || recruitingSpriteMouseView == null)) {
			this.isUserRoundkey = false;
			this.userRespondUserRound(4, false, false);
		}
	}

	public void keyup() {
		if (userRound && spriteMouseView.getState() == 0) {
			// 当角色站立的时候才可以进化或升级武器
			this.spriteMouseView.changeWeaponStand();
			UserMouse userMouse = (UserMouse) spriteMouseView.getSprite();
			if (userMouse.getWeaponStage() < 1) {
				this.toUpStageWeapon();
			} else {
				this.toUpLvWeapon();
			}
		}
	}

	public void keyNum9() {
		this.tipHelp();
	}

	public void keyfire() {
		if (isWin) {
			switch (selButIndex) {
			case 0:
				this.toBuyBigSteal();
				break;
			case 1:
				this.toBuySteal();
				break;
			case 2:
				this.goToGameMain(true);
				break;
			}
		} else {
			if (userRound && this.isUserRoundkey && ((recruitingSpriteMouseView != null && selButIndex != 3) || recruitingSpriteMouseView == null)) {
				this.isUserRoundkey = false;
				this.userRespondUserRound(selButIndex, false, false);
			}
		}
	}

	public void release() {
		// TODO Auto-generated method stub

	}

	public void initChangeRound(boolean userRound) {
		this.userRound = userRound;
		this.isUserRoundkey = this.userRound;
		if (this.userRound) {
			this.userRoundAttCount = 0;
		}
		fightTimeCountView = new FightTimeCountView(this, userRound);
		fightTimeCountView.init();
		fightTimeCountView.startTime();
	}

	public void addGameBeforeView(View view) {
		clildSpriteBeforeViews.put(view.toString(), view);
	}

	public void addGameeBackView(View view) {
		clildSpriteBackViews.put(view.toString(), view);
	}

	public void removeGameBackView(View view) {
		Enumeration e = clildSpriteBackViews.keys();
		while (e.hasMoreElements()) {
			String key = (String) e.nextElement();
			if (clildSpriteBackViews.get(key) == view) {
				clildSpriteBackViews.remove(key);
			}
		}
	}

	public void removeGameBeforeView(View view) {
		Enumeration e = clildSpriteBeforeViews.keys();
		while (e.hasMoreElements()) {
			String key = (String) e.nextElement();
			if (clildSpriteBeforeViews.get(key) == view) {
				clildSpriteBeforeViews.remove(key);
			}
		}
	}

	/**
	 * 
	 * @param className
	 * @return
	 */
	public boolean isGameBeforeViewType(String className) {
		Enumeration e = clildSpriteBeforeViews.keys();
		while (e.hasMoreElements()) {
			String key = (String) e.nextElement();
			if (clildSpriteBeforeViews.get(key).getClass().getName().indexOf(
					className) > -1) {
				return true;
			}
		}
		return false;
	}

	public FightSpriteMouseView getFightSpriteMouseView() {
		FightSpriteMouseView fightSpriteMouseView = null;
		if (recruitingSpriteMouseView != null
				&& ((recruitingSpriteMouseView.isDie() && recruitingSpriteMouseView
						.getX() > spriteMouseView.getX()) || (spriteMouseView
						.isDie() && spriteMouseView.getX() > recruitingSpriteMouseView
						.getX()))) {
			this.changeMousePoint();
		}
		if (recruitingSpriteMouseView != null
				&& !recruitingSpriteMouseView.isDie()
				&& recruitingSpriteMouseView.getX() > spriteMouseView.getX()) {
			fightSpriteMouseView = recruitingSpriteMouseView;
		} else {
			fightSpriteMouseView = spriteMouseView;
		}
		return fightSpriteMouseView;
	}

	/**
	 * 响应用户操作
	 * 
	 * @param index
	 * @param isAfterBuy
	 * @param isUseSecond
	 */
	public synchronized void userRespondUserRound(int index,
			boolean isAfterBuy, boolean isUseSecond) {
		FightSpriteMouseView fightSpriteMouseView = this
				.getFightSpriteMouseView();
		attIndex = index;
		int loseBlue, blue;
		switch (index) {
		case 0:
			fightSpriteMouseView.commonAtt(spriteCatView);
			fightTimeCountView.setResponseRound(true);
			break;
		case 1:
			loseBlue = 25;
			if (!isAfterBuy
					&& spriteMouseView.getSprite().getFightBlue() - loseBlue >= 0) {
				blue = spriteMouseView.getSprite().getFightBlue() - loseBlue;
				spriteMouseView.getSprite().setFightBlue(blue);
				fightSpriteMouseView.weaponAtt(spriteCatView);
				if (fightTimeCountView != null)
					fightTimeCountView.setResponseRound(true);
			} else if (isAfterBuy) {
				fightSpriteMouseView.weaponAtt(spriteCatView);
				if (fightTimeCountView != null)
					fightTimeCountView.setResponseRound(true);
			} else {
				if (Config.isAttAutTip || !fightTimeCountView.isResponseRound()) {
					this.selButIndex = 1;
					this.isUserRoundkey = true;
					toBuyBlue(isUseSecond);
				} else {
					userRespondUserRound(0, isAfterBuy, isUseSecond);
				}
			}
			break;
		case 2:
			if (isAfterBuy) {
				this.isUserRoundkey = false;
				fightSpriteMouseView.killAtt(spriteCatView);
				if (fightTimeCountView != null)
					fightTimeCountView.setResponseRound(true);
			} else {
				if (Config.isAttAutTip || !fightTimeCountView.isResponseRound()) {
					this.selButIndex = 2;
					this.isUserRoundkey = true;
					toBuyKill(isUseSecond);
				} else {
					userRespondUserRound(0, isAfterBuy, isUseSecond);
				}
			}
			break;
		case 3:
			loseBlue = 75;
			if (!isAfterBuy
					&& spriteMouseView.getSprite().getFightBlue() - loseBlue >= 0) {
				blue = spriteMouseView.getSprite().getFightBlue() - loseBlue;
				spriteMouseView.getSprite().setFightBlue(blue);
				this.recruiting();
				this.selButIndex = 0;
				if (fightTimeCountView != null)
					fightTimeCountView.setResponseRound(true);
			} else if (isAfterBuy) {
				this.recruiting();
				this.selButIndex = 0;
				if (fightTimeCountView != null)
					fightTimeCountView.setResponseRound(true);
			} else {
				if (Config.isAttAutTip || !fightTimeCountView.isResponseRound()) {
					this.selButIndex = 3;
					this.isUserRoundkey = true;
					this.toBuyBlue(isUseSecond);
				} else {
					userRespondUserRound(0, isAfterBuy, isUseSecond);
				}
			}
			break;
		case 4:
			if (!isUseSecond) {
				if (!spriteMouseView.isDie()
						&& spriteMouseView.getSprite().getFightMaxHp() > spriteMouseView
								.getSprite().getFightHp()) {
					if (Config.isAttAutTip
							|| !fightTimeCountView.isResponseRound()) {
						this.selButIndex = 4;
						this.useBlood(isUseSecond);
					} else {
						userRespondUserRound(0, isAfterBuy, isUseSecond);
					}
				} else {
					this.isUserRoundkey = true;
				}
			} else {
				this.userRespondUserRound(0, false, false);
			}
			break;
		}
	}

	/**
	 * 响应电脑操作
	 * 
	 * @param index
	 */
	public synchronized void respondNpcRound(int index) {
		FightSpriteMouseView fightSpriteMouseView = null;
		if (recruitingSpriteMouseView != null
				&& !recruitingSpriteMouseView.isDie()
				&& recruitingSpriteMouseView.getX() > spriteMouseView.getX()) {
			fightSpriteMouseView = recruitingSpriteMouseView;
		} else {
			fightSpriteMouseView = spriteMouseView;
		}
		switch (index) {
		case 0:
			spriteCatView.commonAtt(fightSpriteMouseView);
			break;
		case 1:
			spriteCatView.weaponAtt(fightSpriteMouseView);
			break;
		case 2:
			spriteCatView.killAtt(fightSpriteMouseView);
			break;
		}
	}

	public void endLoading() {
		initChangeRound(true);
	}

	public void startPK() {
		this.initChangeRound(true);
	}

	public void keyleft() {
		if (isWin) {
			selButIndex = selButIndex > 0 ? selButIndex - 1 : 2;
		} else {
			selButIndex = selButIndex > 0 ? selButIndex - 1 : 4;
			if (recruitingSpriteMouseView != null && selButIndex == 3) {
				selButIndex = 2;
			}
		}
	}

	public void keyright() {
		if (isWin) {
			selButIndex = selButIndex < 2 ? selButIndex + 1 : 0;
		} else {
			selButIndex = selButIndex < 4 ? selButIndex + 1 : 0;
			if (recruitingSpriteMouseView != null && selButIndex == 3) {
				selButIndex = 4;
			}
		}
	}

	public FightSpriteMouseView getSpriteMouseView() {
		return spriteMouseView;
	}

	public FightSpriteCatView getSpriteCatView() {
		return spriteCatView;
	}

	public FightSpriteMouseView getRecruitingSpriteMouseView() {
		return recruitingSpriteMouseView;
	}

	public boolean isUserRound() {
		return userRound;
	}

	public int getSelButIndex() {
		return selButIndex;
	}

	/**
	 * 通知攻击结束
	 */
	public void notifyAttEnd(FightSpriteView fightSpriteView) {
		if (fightSpriteView instanceof FightSpriteCatView) {
			initChangeRound(true);
		} else if (recruitingSpriteMouseView == null) {
			// 如果不存在招募的角色
			initChangeRound(false);
		} else if (recruitingSpriteMouseView != null) {
			if (!spriteMouseView.isDie() && !recruitingSpriteMouseView.isDie()) {
				userRoundAttCount++;
				if (userRoundAttCount == 1) {
					this.changeMouseFight(attIndex);
				} else {
					initChangeRound(false);
				}
			} else {
				initChangeRound(false);
			}
		}
	}

	/**
	 * 切换招募角色位置
	 */
	public void changeMousePoint() {
		int spriteMouseX = spriteMouseView.getX();
		spriteMouseView.setX(recruitingSpriteMouseView.getX());
		recruitingSpriteMouseView.setX(spriteMouseX);
	}

	/**
	 * 切换招募角色和当前角色的攻击
	 */
	public void changeMouseFight(int attIndex) {
		this.changeMousePoint();
		if (attIndex == 1) {
			if (spriteMouseView.getSprite().getFightBlue() - 25 >= 0) {
				this.userRespondUserRound(attIndex, false, false);
			} else {
				this.userRespondUserRound(0, false, false);
			}
		} else {
			this.userRespondUserRound(attIndex, attIndex == 2, false);
		}
	}

	/**
	 * 通知死亡
	 */
	public void notifyDie(FightSpriteView fightSpriteView) {
		if (fightSpriteView instanceof FightSpriteCatView) {
			this.tipStealMouse();
			this.fightTimeCountView = null;
		} else if (spriteMouseView == fightSpriteView) {
			if (recruitingSpriteMouseView != null
					&& recruitingSpriteMouseView.getState() == 4) {
				this.tipReviveMouse(null);
				this.fightTimeCountView = null;
			} else {
				this.tipReviveMouse(spriteMouseView);
			}
		} else if (recruitingSpriteMouseView == fightSpriteView) {
			if (spriteMouseView.getState() == 4) {
				this.tipReviveMouse(null);
				this.fightTimeCountView = null;
			} else {
				this.tipReviveMouse(recruitingSpriteMouseView);
			}
		}
	}

	/**
	 * 招募角色
	 */
	public void recruiting() {
		Mouse recMouseTmp = (Mouse) this.spriteMouseView.getSprite();
		Mouse recMouse = null;
		switch (Integer.parseInt(recMouseTmp.getCode())) {
		case 0:
			if (PubToolKit.getRandomInt(2) == 0) {
				// recMouse = new Mouse("1", 1, recMouseTmp.getFightMaxHp() / 2,
				// recMouseTmp.getAtt());
				recMouse = new UserMouse("1");
			} else {
				// recMouse = new Mouse("2", 1, recMouseTmp.getFightMaxHp() / 2,
				// recMouseTmp.getAtt());
				recMouse = new UserMouse("2");
			}
			break;
		case 1:
			if (PubToolKit.getRandomInt(2) == 0) {
				// recMouse = new Mouse("0", 1, recMouseTmp.getFightMaxHp(),
				// recMouseTmp.getAtt());
				recMouse = new UserMouse("0");
			} else {
				// recMouse = new Mouse("2", 1, recMouseTmp.getFightMaxHp() / 2,
				// recMouseTmp.getAtt());
				recMouse = new UserMouse("2");
			}
			break;
		case 2:
			if (PubToolKit.getRandomInt(2) == 0) {
				// recMouse = new Mouse("0", 1, recMouseTmp.getFightMaxHp() / 2,
				// recMouseTmp.getAtt());
				recMouse = new UserMouse("0");
			} else {
				// recMouse = new Mouse("1", 1, recMouseTmp.getFightMaxHp() / 2,
				// recMouseTmp.getAtt());
				recMouse = new UserMouse("1");
			}
			break;
		}
		recruitingSpriteMouseView = new FightSpriteMouseView(this, recMouse,
				30, 400 + 140);
		recruitingSpriteMouseView.init();
		userRoundAttCount = 1;
		changeMouseFight(0);
	}

	/**
	 * 偷取猫的奶酪
	 */
	public void stealCat(boolean isBigSteal) {
		this.gameFightMainView.getGamePageScreen().addPassStealNum();
		endGameView = null;
		FightHandView fightHandView = new FightHandView(this, isBigSteal,
				spriteCatView.getX(), spriteCatView.getY());
		this.addGameBeforeView(fightHandView);
	}

	/**
	 * 使用血瓶
	 */
	public void useBlood(boolean isUseSecond) {
		if (propBloodNum > 0) {
			propBloodNum = propBloodNum - 1;
			new Thread() {
				public void run() {
					UserDataManageService.getInsatnce().updateMouseProp(
							props[2][5], propBloodNum, true);
				}
			}.start();
			spriteMouseView.addHp();
			if (fightTimeCountView != null)
				fightTimeCountView.setResponseRound(true);
		} else {
			this.isUserRoundkey = true;
			toBuyBlood(isUseSecond);
		}
	}

	/**
	 * 提示复活加满血继续战斗老鼠
	 */
	public void tipReviveMouse(FightSpriteMouseView fightSpriteMouseView) {
		if (!Config.isTipAuto) {
			endGameView = new FightEndGameReviveView(this,
					fightSpriteMouseView, L9Config.SCR_W / 2,
					L9Config.SCR_H / 2 - 15);
		} else {
			if (fightSpriteMouseView != null) {
				this.toBuyReviveMouse(fightSpriteMouseView);
			} else {
				this.toBuyReviveAllMouse();
			}
		}
	}

	/**
	 * 提示逃跑
	 */
	public void tipRunAway() {
		endGameView = new FightEndGameRunAwayView(this, isRunAway,
				L9Config.SCR_W / 2, L9Config.SCR_H / 2 - 15);
	}

	/**
	 * 关闭逃跑对话框
	 */
	public void closeRunAway() {
		endGameView = null;
	}

	/**
	 * 提示帮助
	 */
	public void tipHelp() {
		endGameView = new FightEndGameHelpView(this, L9Config.SCR_W / 2,
				L9Config.SCR_H / 2 - 15);
	}

	/**
	 * 关闭帮助
	 */
	public void closeHelp() {
		endGameView = null;
	}

	public void buyFail() {
		if (fightTimeCountView != null && fightTimeCountView.isResponseRound()) {
			// 倒计时结束，购买失败，使用图片攻击
			if (userRound)
				userRespondUserRound(0, false, false);
		}
	}

	/**
	 * 购买成功
	 */
	public void buySucc() {
		if ("buyReviveMouse".equals(buyAfterMethod)) {
			this.buyReviveMouse();
		} else if ("buyReviveAllMouse".equals(buyAfterMethod)) {
			this.buyReviveAllMouse();
		} else if ("buyBlue".equals(buyAfterMethod)) {
			this.buyBlue();
		} else if ("buySteal".equals(buyAfterMethod)) {
			this.buySteal();
		} else if ("buyBigSteal".equals(buyAfterMethod)) {
			this.buyBigSteal();
		} else if ("buyBlood".equals(buyAfterMethod)) {
			this.buyBlood();
		} else if ("buyKill".equals(buyAfterMethod)) {
			this.buyKill();
		} else if ("buyUpStageWeapon".equals(buyAfterMethod)) {
			this.buyUpStageWeapon();
		} else if ("buyUpLvWeapon".equals(buyAfterMethod)) {
			this.buyUpLvWeapon();
		}
	}

	/**
	 * 提示偷取对话框
	 */
	public void tipStealMouse() {
		isWin = true;
		selButIndex = 0;
	}

	/**
	 * 提示偷取的结果
	 */
	public void tipStealMsg(boolean isBigSteal, final int stealNum) {
		endGameView = new FightEndGameStealMsgView(this, isBigSteal, stealNum,
				L9Config.SCR_W / 2, L9Config.SCR_H / 2);
		this.gameFightMainView.getGamePageScreen()
				.addPassGetCheeseNum(stealNum);
		new Thread() {
			public void run() {
				int chessNum = UserDataManageService.getInsatnce()
						.getPropNumByCode("0")
						+ stealNum;
				UserDataManageService.getInsatnce().updateMouseProp("0",
						chessNum, true);
			}
		}.start();
	}

	public void closeStealMsg(boolean isBigSteal) {
		endGameView = null;
		if (isBigSteal) {
			this.toBuyBigSteal();
		} else {
			this.toBuySteal();
		}
	}

	/**
	 * 购买复活,复活单体
	 */
	public void toBuyReviveMouse(FightSpriteMouseView fightSpriteMouseView) {
		isInPay = true;
		buyAfterMethod = "buyReviveMouse";
		switch (Config.regionType) {
		case MainConfig.Type_GDDX:
		case MainConfig.Type_SXDX:
			MainServer.getInstance().buyProp(props[3][1], props[3][2],
					props[3][4], false, -1, props[3][6], false, false,
					"/propimg/15.png");
			break;
		default:

			MainServer.mPropType = 15;

			MainServer.getInstance().buyProp(props[3][1], props[3][2],
					props[3][4], false, -1, props[3][6], false, false);
			break;
		}
	}

	/**
	 * 复活恢复血量-复活单体
	 */
	public void buyReviveMouse() {
		endGameView = null;
		if (spriteMouseView.getState() == 4) {
			spriteMouseView.revive();
		} else if (recruitingSpriteMouseView != null
				&& recruitingSpriteMouseView.getState() == 4) {
			recruitingSpriteMouseView.revive();
		}
		if (recruitingSpriteMouseView == null) {
			this.initChangeRound(true);
		}
	}

	/**
	 * 购买复活所有角色
	 */
	public void toBuyReviveAllMouse() {
		isInPay = true;
		buyAfterMethod = "buyReviveAllMouse";
		switch (Config.regionType) {
		case MainConfig.Type_GDDX:
		case MainConfig.Type_SXDX:
			MainServer.getInstance().buyProp(props[4][1], props[4][2],
					props[4][4], false, -1, props[4][6], false, false,
					"/propimg/15.png");
			break;
		default:

			MainServer.mPropType = 15;

			MainServer.getInstance().buyProp(props[4][1], props[4][2],
					props[4][4], false, -1, props[4][6], false, false);
			break;
		}
	}

	/**
	 * 补满蓝瓶
	 * 
	 * @param isUseSecond
	 */
	public void toBuyBlue(boolean isUseSecond) {
		isInPay = true;
		buyAfterMethod = "buyBlue";
		switch (Config.regionType) {
		case MainConfig.Type_GDDX:
		case MainConfig.Type_SXDX:
			MainServer.getInstance().buyProp(props[0][1], props[0][2],
					props[0][4], isUseSecond, -1, props[0][6], false, false,
					"/propimg/11.png");
			break;
		default:	//计费接口

			MainServer.mPropType = 14;

			MainServer.getInstance().buyProp(props[0][1], props[0][2],
					props[0][4], isUseSecond, -1, props[0][6], false, false);
			break;
		}
	}

	/**
	 * 购买血瓶
	 */
	public void toBuyBlood(boolean isUseSecond) {
		isInPay = true;
		buyAfterMethod = "buyBlood";
		switch (Config.regionType) {
		case MainConfig.Type_GDDX:
		case MainConfig.Type_SXDX:
			MainServer.getInstance().buyProp(props[2][1], props[2][2],
					props[2][4], isUseSecond, Integer.parseInt(props[2][3]),
					props[2][6], false, false,
					"/propimg/" + props[2][5] + ".png");
			break;
		default:	//计费接口

			MainServer.mPropType = 6;

			MainServer.getInstance().buyProp(props[2][1], props[2][2],
					props[2][4], isUseSecond, Integer.parseInt(props[2][3]),
					props[2][6], false, false);
			break;
		}
	}

	/**
	 * 血瓶购买成功
	 */
	public void buyBlood() {
		propBloodNum = propBloodNum + Integer.parseInt(props[2][3]);
		this.useBlood(false);
	}

	/**
	 * 补满蓝成功
	 */
	public void buyBlue() {
		int blue = this.spriteMouseView.getSprite().getFightMaxBlue();
		this.spriteMouseView.getSprite().setFightBlue(blue);
		userRespondUserRound(selButIndex, true, false);
	}

	/**
	 * 复活所有角色
	 */
	public void buyReviveAllMouse() {
		endGameView = null;
		spriteMouseView.revive();
		recruitingSpriteMouseView.revive();
		this.initChangeRound(true);
	}

	/**
	 * 购买大偷取
	 */
	public void toBuyBigSteal() {
		if (!this.isGameBeforeViewType(FightHandView.class.getName())) {
			isInPay = true;
			buyAfterMethod = "buyBigSteal";
			switch (Config.regionType) {
			case MainConfig.Type_GDDX:
			case MainConfig.Type_SXDX:
				MainServer.getInstance().buyProp(props[5][1], props[5][2],
						props[5][4], false, -1, props[5][6], false, false,
						"/propimg/13.png");
				break;
			default:		//计费接口
				MainServer.mPropType = 11;

				MainServer.getInstance().buyProp(props[5][1], props[5][2],
						props[5][4], false, -1, props[5][6], false, false);
				break;
			}
		}
	}

	public void buyBigSteal() {
		this.stealCat(true);
	}

	/**
	 * 购买小偷取
	 */
	public void toBuySteal() {
		if (!this.isGameBeforeViewType(FightHandView.class.getName())) {
			isInPay = true;
			buyAfterMethod = "buySteal";
			switch (Config.regionType) {
			case MainConfig.Type_GDDX:
			case MainConfig.Type_SXDX:
				MainServer.getInstance().buyProp(props[6][1], props[6][2],
						props[6][4], false, -1, props[6][6], false, false,
						"/propimg/14.png");
				break;
			default:		//计费接口
				MainServer.mPropType = 12;

				MainServer.getInstance().buyProp(props[6][1], props[6][2],
						props[6][4], false, -1, props[6][6], false, false);
				break;
			}
		}
	}

	public void buySteal() {
		this.stealCat(false);
	}

	public void toBuyKill(boolean isUseSecond) {
		isInPay = true;
		buyAfterMethod = "buyKill";
		switch (Config.regionType) {
		case MainConfig.Type_GDDX:
		case MainConfig.Type_SXDX:
			MainServer.getInstance().buyProp(props[1][1], props[1][2],
					props[1][4], isUseSecond, -1, props[1][6], false, false,
					"/propimg/12.png");
			break;
		default:	//计费接口

			MainServer.mPropType = 16;

			MainServer.getInstance().buyProp(props[1][1], props[1][2],
					props[1][4], isUseSecond, -1, props[1][6], false, false);
			break;
		}
	}

	public void buyKill() {
		userRespondUserRound(selButIndex, true, false);
	}

	/**
	 * 进化武器
	 */
	public void toUpStageWeapon() {
		UserMouse userMouse = (UserMouse) spriteMouseView.getSprite();
		if (userMouse.getWeaponStage() < 1) {
			isInPay = true;
			buyAfterMethod = "buyUpStageWeapon";
			switch (Config.regionType) {
			case MainConfig.Type_GDDX:
			case MainConfig.Type_SXDX:
				MainServer.getInstance().buyProp(
						props[7][1],
						props[7][2],
						props[7][4],
						false,
						-1,
						props[7][6],
						false,
						false,
						"/propimg/"
								+ (Integer.parseInt(userMouse.getCode()) + 7)
								+ ".png");
				break;
			default:	//计费接口

				MainServer.mPropType = 8;

				MainServer.getInstance().buyProp(props[7][1], props[7][2],
						props[7][4], false, -1, props[7][6], false, false);
			}
		}
	}

	public void buyUpStageWeapon() {
		UserMouse userMouse = (UserMouse) spriteMouseView.getSprite();
		if (userMouse.getWeaponStage() < 1) {
			userMouse.setWeaponStage(userMouse.getWeaponStage() + 1);
			GameConfigInfo.weapon[Integer.parseInt(userMouse.getCode())][0] = userMouse
					.getWeaponStage();
			final UserMouse saveUserMouse = userMouse;
			new Thread() {
				public void run() {
					UserDataManageService.getInsatnce().updateMouse(
							saveUserMouse);
					UserInfo.score += 50;
					MainServer.getInstance().updateScore(UserInfo.score);
				}
			}.start();
			spriteMouseView.setSprite(userMouse);
			spriteMouseView.chageUpStageWeapon();
			newBgbottomImg();
			startUpWeaponEff();
		}
	}

	/**
	 * 升级武器
	 */
	public void toUpLvWeapon() {
		UserMouse userMouse = (UserMouse) spriteMouseView.getSprite();
		if (userMouse.getWeaponLv() < 10) {
			isInPay = true;
			buyAfterMethod = "buyUpLvWeapon";
			switch (Config.regionType) {
			case MainConfig.Type_GDDX:
			case MainConfig.Type_SXDX:
				MainServer.getInstance().buyProp(
						props[8][1],
						props[8][2],
						props[8][4],
						false,
						-1,
						props[8][6],
						false,
						false,
						"/propimg/"
								+ (Integer.parseInt(userMouse.getCode()) + 7)
								+ ".png");
				break;
			default:	//计费接口
				MainServer.mPropType = 9;

				MainServer.getInstance().buyProp(props[8][1], props[8][2],
						props[8][4], false, -1, props[8][6], false, false);
			}
		}
	}

	public void buyUpLvWeapon() {
		UserMouse userMouse = (UserMouse) spriteMouseView.getSprite();
		if (userMouse.getWeaponLv() < 10) {
			userMouse.setWeaponLv(userMouse.getWeaponLv() + 1);
			GameConfigInfo.weapon[Integer.parseInt(userMouse.getCode())][1] = userMouse
					.getWeaponLv();
			final UserMouse saveUserMouse = userMouse;
			new Thread() {
				public void run() {
					UserDataManageService.getInsatnce().updateMouse(
							saveUserMouse);
				}
			}.start();
			spriteMouseView.setSprite(userMouse);
			startUpWeaponEff();
		}
	}

	/**
	 * 开始升级或进化武器效果
	 */
	public void startUpWeaponEff() {
		if (userRound && spriteMouseView.getState() == 0) {
			isUserRoundkey = false;
			fightTimeCountView.stopTime();
			FightUpLvView fightUpLvView = new FightUpLvView(this,
					spriteMouseView.getX() + 20, spriteMouseView.getY());
			this.addGameBeforeView(fightUpLvView);
		}
	}

	/**
	 * 结束升级或进化武器效果
	 * 
	 * @param fightUpLvView
	 */
	public void endUpWeaponEff(FightUpLvView fightUpLvView) {
		isUserRoundkey = true;
		fightTimeCountView.reviveTime();
		this.removeGameBeforeView(fightUpLvView);
	}

	public void closeRevive() {
		endGameView = null;
		if (recruitingSpriteMouseView == null
				|| recruitingSpriteMouseView.isDie() && spriteMouseView.isDie()) {
			this.goToGameMain(false);
		}
	}

	public void goToGameMain(boolean isWin) {
		if (!this.isGameBeforeViewType(FightHandView.class.getName())) {
			gameFightMainView.goToGameMain(isWin);
		}
	}

	public Pass getPass() {
		return gameFightMainView.getPass();
	}
}
