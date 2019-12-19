package com.game.mouse.view.game;

import java.util.Enumeration;
import java.util.Vector;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import com.game.lib9.Engine;
import com.game.lib9.L9Config;
import com.game.lib9.L9EngineLogic;
import com.game.lib9.L9Object;
import com.game.mouse.context.Config;
import com.game.mouse.context.UserInfo;
import com.game.mouse.gameinfo.GameInfo;
import com.game.mouse.gameinfo.Props;
import com.game.mouse.modle.service.UserDataManageService;
import com.game.mouse.screen.GamePageScreen;
import com.game.mouse.screen.MainMidlet;
import com.game.mouse.view.BuyCheeseView;
import com.game.mouse.view.TimeView;
import com.game.mouse.view.View;
import com.game.mouse.view.maingame.child.BuyCheeseMainGameView;
import com.game.mouse.view.maingame.child.CatSpriteView;
import com.game.mouse.view.maingame.child.CheeseAddView;
import com.game.mouse.view.maingame.child.CheeseCommonView;
import com.game.mouse.view.maingame.child.CheeseView;
import com.game.mouse.view.maingame.child.DoorView;
import com.game.mouse.view.maingame.child.EndGameHelpView;
import com.game.mouse.view.maingame.child.EndGamePassDoorView;
import com.game.mouse.view.maingame.child.EndGameReviveView;
import com.game.mouse.view.maingame.child.InitMapsTool;
import com.game.mouse.view.maingame.child.MapView;
import com.game.mouse.view.maingame.child.MouseSpriteView;
import com.game.mouse.view.maingame.child.OrganMoveSpriteMoveView;
import com.game.mouse.view.maingame.child.OrganMoveView;
import com.game.mouse.view.maingame.child.OrganView;
import com.game.mouse.view.maingame.child.SpriteView;
import com.game.mouse.view.newhand.NewHandDialog;
import com.game.mouse.view.newhand.NewHandView;
import com.model.control.MyImg;
import com.model.mainServer.MainConfig;
import com.model.mainServer.MainServer;
import com.model.tool.PubToolKit;

public class GameMainView implements View {
	public GamePageScreen gamePageScreen;

	// private MyImg bg;

	private MyImg bgbutbg;

	private MyImg bgbottomImg;

	private MyImg header;

	private MyImg iconblood;

	private MyImg cheese;

	private MyImg[] buticons = new MyImg[3];

	private MyImg[] propImgs = new MyImg[4];

	/**
	 * 偷取次数
	 */
	public int passStealNum = 0;

	/**
	 * 杀死猫个数
	 */
	public int passKillCatNum = 0;

	/**
	 * 本过获得的所有奶酪数
	 */
	public int passGetCheeseNum = 0;

	/**
	 * 道具个数
	 */
	public static int[] propNum = new int[4];

	public int propCheeseStarNum = 0;

	/**
	 * 道具code、扣费code、价格、购买数量、道具名、对应数量的code
	 */
	public String[][] props = {
			{ "uncheese0", "", "0", "0", "使用万能奶酪", "0", "恢复心情、喂养使用,购买万能奶酪" },
			{ "speedcheese", "", "0", "0", "使用速度奶酪", "1", "控制速度使用,购买速度奶酪" },
			{ "propprotect", "", "0", "0", "获得变身头盔", "3", "购买变身头盔,防止机关陷阱伤害" },
			{ "propblood", "", "0", "0", "使用回血胶囊", "2", "购买回血胶囊,恢复血量" },
			{ "proprevive", "", "0", "0", "复活补满体力", "-1", "复活并补满体力，继续游戏" },
			{ "proppassdoor", "", "0", "0", "直接过关", "-1", "购买直接过关,未收集满奶酪过关" } };

	private int cheeseW, cheeseH;

	private MyImg num;

	public int starNum;

	/**
	 * 头部显示的奶酪数量
	 */
	public int drawStarNum;

	private DoorView doorView;

	private SpriteView spriteView;

	private Vector catSpriteViews = new Vector();

	private Vector cheeseViews = new Vector();

	private Vector organViews = new Vector();

	private Vector otherViews = new Vector();

	private MapView mapView;

	public int initMoveX, moveX, maxMoveX;

	private int gameState = 0;

	private EndGameView endGameView;

	private BuyCheeseView buyCheeseDialog;

	private int isInPay;

	private String buyAfterMethod = "";

	/**
	 * 购买成功后调用按左右键控制速度标识
	 */
	private boolean buyAfterControlSpeedKeyLeft = false;

	/**
	 * 正在对战的猫
	 */
	private CatSpriteView catIngPKSpriteView;

	private NewHandView newhandView;

	private L9Object startTipObj;

	private boolean mIsFirstPlay = true;

	public static int addSpeedType = 0;	//0 -- 教学 1 -- 消耗奶酪1  2 -- 消耗奶酪2

	public GameMainView(GamePageScreen gamePageScreen,boolean type) {
		this.gamePageScreen = gamePageScreen;
		this.mIsFirstPlay = type;
		mapView = new MapView(this, this.gamePageScreen.getPass()
				.getSceneCode(), this.gamePageScreen.getPass().getCode(),type);
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
		this.getPropNum();
	}

	public void getPropNum() {
		for (int i = 0; i < 4; i++) {
			propNum[i] = UserDataManageService.getInsatnce().getPropNumByCode(
					props[i][5]);
		}
	}

	/**
	 * 新增获得的奶酪个数
	 */
	public void addGetCheese(int num) {
		if (this.getGamePageScreen().isFirstLast()) {
			propCheeseStarNum += num;
//			passGetCheeseNum++;
		}
	}

	public void init() {

	}

	public void init(int loadCount) {
		// TODO Auto-generated method stub
		switch (loadCount) {
		case 40:
			this.getPropMess();
			break;
		case 50:
			this.initImg();
			break;
		case 60:
			mapView.init();
			break;
		case 70:
			this.initMapsSprites();
			break;
		case 80:
			this.initMapsMoveMsg();
			break;
		case 90:
			if (Config.newHand == 1
					&& this.gamePageScreen.getPass().getSceneCode() == 0
					&& this.gamePageScreen.getPass().getCode() == 0 && mIsFirstPlay) {
				this.newHand();
			}
			break;
		case 100:
			this.initMapsDoor();
			break;
		}
	}

	public void initImg() {
		// bg = new MyImg("gamepage/sce/"
		// + this.gamePageScreen.getPass().getSceneCode() + "/bg.png");
		bgbutbg = new MyImg("gamepage/bgbutbg.png");
		bgbottomImg = GameInfo.getBottomImg(1);
		header = new MyImg("gamepage/header"
				+ this.gamePageScreen.getUserMouse().getCode() + ".png");
		iconblood = new MyImg("gamepage/iconblood.png");
		for (int i = 0; i < propImgs.length; i++) {
			propImgs[i] = new MyImg("gamepage/icon" + i + ".png");
		}
		for (int i = 0; i < buticons.length; i++) {
			buticons[i] = new MyImg("gamepage/buticon" + i + ".png");
		}
		cheese = new MyImg("gamepage/cheese.png");
		cheeseW = cheese.getWidth() / 2;
		cheeseH = cheese.getHeight();
		num = new MyImg("shoppage/num.png");
	}

	/**
	 * 初始化地图上所有物体
	 */
	public void initMapsSprites() {
		spriteView = InitMapsTool.initSpriteMouse(this, mapView,
				this.gamePageScreen.getUserMouse());
		spriteView.init();
		catSpriteViews = InitMapsTool.initSpriteCats(this, mapView,
				gamePageScreen.getPass());
		for (int i = 0; i < catSpriteViews.size(); i++) {
			((SpriteView) catSpriteViews.elementAt(i)).init();
		}
		cheeseViews = InitMapsTool.initAllcheeses(this, mapView);
		for (int i = 0; i < cheeseViews.size(); i++) {
			((CheeseView) cheeseViews.elementAt(i)).init();
		}

		organViews = InitMapsTool.initOrgan(this, mapView);
		for (int i = 0; i < organViews.size(); i++) {
			((OrganView) organViews.elementAt(i)).init();
		}
	}

	public void initMapsDoor() {
		doorView = InitMapsTool.initDoor(this, mapView);
		doorView.init();
	}

	/**
	 * 初始化地图移到
	 */
	public void initMapsMoveMsg() {
		this.maxMoveX = L9Config.SCR_W - (mapView.getMaxPointX() + spriteView.getSpriteW() / 2);
		if (mapView.getMaxPointX() + spriteView.getSpriteW() / 2 >  L9Config.SCR_W
				&& spriteView.getX() >  L9Config.SCR_W/2) {
			if (mapView.getMaxPointX() + spriteView.getSpriteW() / 2
					- spriteView.getX() <=  L9Config.SCR_W/2) {
				this.moveX = this.initMoveX = this.maxMoveX;
			} else {
				this.moveX = this.initMoveX =  L9Config.SCR_W/2 - spriteView.getX();
			}
		}
	}

	public void update() {
		if (isInPay == 0) {
			keyPress();
			mapView.update();
			for (int i = 0; i < cheeseViews.size(); i++) {
				((CheeseView) cheeseViews.elementAt(i)).update();
			}
			for (int i = 0; i < organViews.size(); i++) {
				((OrganView) organViews.elementAt(i)).update();
			}
			if (spriteView != null)
				spriteView.update();
			for (int i = 0; i < catSpriteViews.size(); i++) {
				((SpriteView) catSpriteViews.elementAt(i)).update();
			}
			for (int i = 0; i < otherViews.size(); i++) {
				((View) otherViews.elementAt(i)).update();
			}
			doorView.update();
			for (int i = 0; i < L9EngineLogic.getInstance().objects.size; i++) {
				((L9Object) L9EngineLogic.getInstance().objects.values[i])
						.doAllFrame();
			}
			L9EngineLogic.getInstance().checkCollision(8 | 64, 4);// 检查猫、洞门与老鼠的碰撞情况
			L9EngineLogic.getInstance().checkCollision(4 | 8, 16 | 32);// 检查猫、老鼠与奶酪、机关碰撞情况
			L9EngineLogic.getInstance().flushRemoveList();
			L9EngineLogic.getInstance().flushAddList();
			if (endGameView != null) {
				endGameView.update();
			}
			if (newhandView != null)
				newhandView.update();
		} else {
			if (isInPay == 1) {
				if (buyCheeseDialog != null) {
					buyCheeseDialog.update();
				}
			} else {
				isInPay();
			}
		}
	}

	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		if (moveX != 0) {
			g.translate(moveX, 0);
		}
		mapView.paint(g);
		paintSprites(g);
		if (moveX != 0) {
			g.translate(-moveX, 0);
		}
		for (int i = 0; i < otherViews.size(); i++) {
			((View) otherViews.elementAt(i)).paint(g);
		}
		paintHeader(g);
		paintBut(g);
		if (startTipObj != null) {
			startTipObj.paintFrame(g);
		}
		if (endGameView != null)
			endGameView.paint(g);
		if (newhandView != null) {
			newhandView.paint(g);
		}
		if (isInPay == 2) {
			MainServer.getInstance().paint(g);
		} else if (isInPay == 1 && buyCheeseDialog != null) {
			buyCheeseDialog.paint(g);
		}
	}

	/**
	 * 画背景
	 */
	public void paintbg(Graphics g) {
		// bg.drawImage(g, 0, 0, 0);
		// if (-moveX > 0) {
		// bg.drawImage(g, L9Config.SCR_W, 0, 0);
		// }
	}

	/**
	 * 画sprites
	 */
	public void paintSprites(Graphics g) {
		doorView.paint(g);
		for (int i = 0; i < organViews.size(); i++) {
			((OrganView) organViews.elementAt(i)).paint(g);
		}
		for (int i = 0; i < cheeseViews.size(); i++) {
			((CheeseView) cheeseViews.elementAt(i)).paint(g);
		}
		if (spriteView != null) {
			spriteView.paint(g);
		}
		for (int i = 0; i < catSpriteViews.size(); i++) {
			((SpriteView) catSpriteViews.elementAt(i)).paint(g);
		}
	}

	/**
	 * 画头部信息
	 */
	public void paintHeader(Graphics g) {
		header.drawImage(g, 7, 0, 0);
		for (int i = 0; spriteView != null
				&& i < spriteView.getSprite().getHp(); i++) {
			iconblood.drawImage(g, 140 + i * 50, (20 + 20), 0);
		}
		for (int i = 0; i < propImgs.length; i++) {
			int pyX = (i == 1 ? -10 : i == 3 ? -5 : 0);
			int pyY = (i >= 2 ? 10 : 0);
			propImgs[i].drawImage(g, (160 + 200) + (70 + 70) * i, 40 + pyY + 35, g.LEFT | g.BOTTOM);
			PubToolKit.drawString(g, this.num.getImg(), "X"
					+ (propNum[i] > 99 ? 99 : propNum[i]), "X0123456789", (180 + 200)
					+ (70 + 70) * i + pyX, 55 + 35, 27, 31, g.LEFT | g.BOTTOM, 0, 2, -1);
		}
		for (int i = 0; i < 3; i++) {
			cheese.drawRegion(g, drawStarNum > i ? 0 : cheeseW, 0, cheeseW,
					cheeseH, 0, (480 + 450) + i * (50 + 70), 55 + 35, g.LEFT | g.BOTTOM);
		}
	}

	/**
	 * 画底部面板
	 */
	public void paintBut(Graphics g) {
		bgbutbg.drawImage(g, L9Config.SCR_W / 2, 465 + 180, g.VCENTER | g.HCENTER);
		for (int i = 0; i < 3; i++) {
			buticons[i].drawImage(g, i * 180 + 170 + 230, 420 + 180 - 15, 0);
		}
		bgbottomImg
				.drawImage(g, 0, L9Config.SCR_H - bgbottomImg.getHeight(), 0);
	}

	public SpriteView getSpriteView() {
		return spriteView;
	}

	public GamePageScreen getGamePageScreen() {
		return gamePageScreen;
	}

	public void keyPress() {
		if (newhandView != null) {
			newhandView.keyPress();
			if (newhandView == null || !newhandView.isKeyGame()) {
				return;
			}
		}
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
			case Engine.K_KEY_UP:
				keyup();
				break;
			case Engine.K_KEY_DOWN:
				keydown();
				break;
			case Engine.K_KEY_FIRE:
				keyfire();
				break;
			default:

			}
		}
	}

	public void keyleft() {
		if (gameState == 2) {
			this.controlSpeed(true);
		}
	}

	public void keyright() {
		if (gameState == 2) {
			this.controlSpeed(false);
		}
	}

	public void keyup() {
		this.useBlood();
	}

	public void keydown() {
		this.useprotect();
	}

	public void keyNum0() {
		if (endGameView == null) {
			new Thread() {
				public void run() {
					for (int i = 0; i < 4; i++) {
						UserDataManageService.getInsatnce().updateMouseProp(
								props[i][5], propNum[i], false);
					}
					UserDataManageService.getInsatnce().updateEndGameUserData(
							false, gamePageScreen.getPass().getSceneCode(),
							gamePageScreen.getPass().getCode(), 0);
				}
			}.start();
			this.stopAll();
			endGameView = new EndGameMenuView(this, L9Config.SCR_W / 2,
					L9Config.SCR_H / 2);
		}
	}

	public void keyNum9() {
		this.tipHelp();
	}

	public void keyfire() {
		if (gameState == 2) {
			this.spriteView.jump();
		} else if (gameState == 1) {
			this.spriteStartMove();
			this.cheeseMoldyStart();
			this.gameState = 2;
			this.startTipObj = null;
		}
	}

	public void moveMap(int addMove) {
		if (addMove > 0) {
			if (moveX + addMove <= 0) {
				moveX += addMove;
			} else {
				moveX = 0;
			}
		} else {
			if (moveX + addMove >= this.maxMoveX) {
				moveX += addMove;
			} else {
				moveX = this.maxMoveX;
			}
		}
	}

	/**
	 * 移除SpriteView
	 * 
	 * @param view
	 */
	public void removeSprite(SpriteView view) {
		if (view == this.spriteView) {
			this.spriteView.release();
			this.spriteView = null;
		} else {
			SpriteView spriteView;
			Enumeration e = catSpriteViews.elements();
			while (e.hasMoreElements()) {
				spriteView = (SpriteView) e.nextElement();
				if (spriteView == view) {
					for (int i = 0; i < ((CatSpriteView) spriteView)
							.getEatCheeseViews().size(); i++) {
						this.addOtherViews(new CheeseAddView(this, spriteView
								.getX(), spriteView.getY() + 20 * i,
								(starNum + i) * 40 + 530, 50));
					}
					spriteView.release();
					catSpriteViews.removeElement(spriteView);
				}
			}
		}
	}

	/**
	 * 移除奶酪
	 * 
	 * @param view
	 */
	public void removeCheeseViews(View view) {
		CheeseView cheeseView;
		Enumeration e = cheeseViews.elements();
		while (e.hasMoreElements()) {
			cheeseView = (CheeseView) e.nextElement();
			if (cheeseView == view) {
				cheeseViews.removeElement(cheeseView);
			}
		}
	}

	/**
	 * 新增其他奶酪
	 * 
	 * @param view
	 */
	public void addOtherViews(View view) {
		otherViews.addElement(view);
	}

	/**
	 * 移除其他的元素
	 * 
	 * @param view
	 */
	public void removeOtherViews(View view) {
		View tmpView;
		Enumeration e = otherViews.elements();
		while (e.hasMoreElements()) {
			tmpView = (View) e.nextElement();
			if (tmpView == view) {
				otherViews.removeElement(view);
			}
		}
	}

	public int loadIndex() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void pkCat(CatSpriteView catSpriteView) {
		catSpriteView.getSprite().setFightHp(
				catSpriteView.getSprite().getFightMaxHp());
		catIngPKSpriteView = catSpriteView;
		spriteView.getSprite().setFightHp(
				spriteView.getSprite().getFightMaxHp());
		gamePageScreen.changeGameToFight();
	}

	/**
	 * 拉屏显示洞门结束
	 */
	public void endShowDoorStartGame() {
		gameState = 1;
		if (newhandView == null) {
			startTipObj = new L9Object(null, 2, "gamepage/starttip.png", 124,
					105, L9Config.SCR_W / 2 + 600, L9Config.SCR_H / 2 + 20 + 285, -1, false,
					true, true, 2);
			startTipObj.setNextFrameTime(3);
		}
	}

	public int getMaxMoveX() {
		return maxMoveX;
	}

	public MapView getMapView() {
		return mapView;
	}

	public int getMoveX() {
		return moveX;
	}

	public int getInitMoveX() {
		return initMoveX;
	}

	public CatSpriteView getCatIngPKSpriteView() {
		return catIngPKSpriteView;
	}

	public Vector getOrganMoveSpriteMoveView() {
		Vector tmpOrganViews = new Vector();
		for (int i = 0; i < organViews.size(); i++) {
			OrganView organView = (OrganView) organViews.elementAt(i);
			if (organView instanceof OrganMoveSpriteMoveView) {
				tmpOrganViews.addElement(organView);
			}
		}
		return tmpOrganViews;
	}

	public void cheeseMoldyStart() {
		CheeseView cheeseView = null;
		for (int i = 0; i < cheeseViews.size(); i++) {
			cheeseView = (CheeseView) cheeseViews.elementAt(i);
			if (cheeseView instanceof TimeView) {
				((TimeView) cheeseView).startReckonTime();
			}
		}
	}

	public void cheeseMoldyStop() {
		CheeseView cheeseView = null;
		for (int i = 0; i < cheeseViews.size(); i++) {
			cheeseView = (CheeseView) cheeseViews.elementAt(i);
			if (cheeseView instanceof TimeView) {
				((TimeView) cheeseView).stopReckonTime();
			}
		}
	}

	public void cheeseStartMove() {
		CheeseView cheeseView = null;
		for (int i = 0; i < cheeseViews.size(); i++) {
			cheeseView = (CheeseView) cheeseViews.elementAt(i);
			if (cheeseView instanceof CheeseCommonView) {
				((CheeseCommonView) cheeseView).start();
			}
		}
	}

	public void cheeseStopMove() {
		CheeseView cheeseView = null;
		for (int i = 0; i < cheeseViews.size(); i++) {
			cheeseView = (CheeseView) cheeseViews.elementAt(i);
			if (cheeseView instanceof CheeseCommonView) {
				((CheeseCommonView) cheeseView).stop();
			}
		}
	}

	/**
	 * 所有角色开始运动
	 */
	public void spriteStartMove() {
		spriteView.startMove();
		for (int i = 0; i < catSpriteViews.size(); i++) {
			((SpriteView) catSpriteViews.elementAt(i)).startMove();
		}
	}

	/**
	 * 停止所有移动的东西
	 */
	public void stopAll() {
		this.spriteStopMove();
		this.organStop();
		this.cheeseMoldyStop();
		this.cheeseStopMove();
	}

	/**
	 * 停止所有移动的东西
	 */
	public void startAll() {
		this.spriteStartMove();
		this.organStart();
		this.cheeseMoldyStart();
		this.cheeseStartMove();
	}

	/**
	 * 所有角色停止运动
	 */
	public void spriteStopMove() {
		spriteView.stopMove();
		for (int i = 0; i < catSpriteViews.size(); i++) {
			((SpriteView) catSpriteViews.elementAt(i)).stopMove();
		}
	}

	/**
	 * 所有机关开启
	 */
	public void organStart() {
		for (int i = 0; i < organViews.size(); i++) {
			OrganView organView = (OrganView) organViews.elementAt(i);
			if (organView instanceof OrganMoveView) {
				((OrganMoveView) organView).setStop(false);
			}
		}
	}

	/**
	 * 所有机关关闭
	 */
	public void organStop() {
		for (int i = 0; i < organViews.size(); i++) {
			OrganView organView = (OrganView) organViews.elementAt(i);
			if (organView instanceof OrganMoveView) {
				((OrganMoveView) organView).setStop(true);
			}
		}
	}

	/**
	 * 通过洞门
	 */
	public void passDoor() {
		this.endGame(true);
	}

	/**
	 * 游戏结束
	 * 
	 * @param issucc
	 */
	public void endGame(final boolean issucc) {
		int passScore = passStealNum * 20 + passKillCatNum * 10 + starNum * 10;
		UserInfo.score = passScore + UserInfo.score;
		stopAll();
		if (issucc) {
			propNum[0] = propNum[0] + propCheeseStarNum;
			//MainConfig.coinNum = MainConfig.coinNum + 10;
			new Thread() {
				public void run() {
					for (int i = 0; i < 4; i++) {
						UserDataManageService.getInsatnce().updateMouseProp(
								props[i][5], propNum[i], false);
					}
					UserDataManageService.getInsatnce().updateEndGameUserData(
							issucc, gamePageScreen.getPass().getSceneCode(),
							gamePageScreen.getPass().getCode(), starNum);
					MainServer.getInstance().updateScore(UserInfo.score);
					if (!"".equals(Config.loginSeq) && Config.runState == 2) {
						Config.runState = 3;
						MainServer.getInstance().saveGamesStbLogs(
								Config.loginSeq, Config.runState,
								Runtime.getRuntime().totalMemory(),
								"ftp:" + MainMidlet.engine.get_fps());
					}
				}
			}.start();
			endGameView = new EndGameSuccView(this, starNum, passGetCheeseNum,
					L9Config.SCR_W / 2, L9Config.SCR_H / 2);
		} else {
			new Thread() {
				public void run() {
					UserDataManageService.getInsatnce().updateEndGameUserData(
							issucc, gamePageScreen.getPass().getSceneCode(),
							gamePageScreen.getPass().getCode(), starNum);
					MainServer.getInstance().updateScore(UserInfo.score);
					if (!"".equals(Config.loginSeq) && Config.runState == 2) {
						Config.runState = 3;
						MainServer.getInstance().saveGamesStbLogs(
								Config.loginSeq, Config.runState,
								Runtime.getRuntime().totalMemory(),
								"ftp:" + MainMidlet.engine.get_fps());
					}
				}
			}.start();
			endGameView = new EndGameFailView(this, 0, passGetCheeseNum,
					L9Config.SCR_W / 2, L9Config.SCR_H / 2);
		}
	}

	/**
	 * 提示复活老鼠
	 */
	public void tipReviveMouse() {
		if (!Config.isTipAuto) {
			endGameView = new EndGameReviveView(this, L9Config.SCR_W / 2,
					L9Config.SCR_H / 2 - 15);
		} else {
			MainServer.mPropType = 13;
			this.toBuyRevive();
		}
	}

	/**
	 * 提示直接过关
	 */
	public void tipPassDoor() {
		if (!Config.isTipAuto) {
			endGameView = new EndGamePassDoorView(this, L9Config.SCR_W / 2,
					L9Config.SCR_H / 2 - 15);
			this.stopAll();
		} else {
			this.toBuyPassDoor();
		}
	}

	/**
	 * 提示帮助
	 */
	public void tipHelp() {
		endGameView = new EndGameHelpView(this, L9Config.SCR_W / 2,
				L9Config.SCR_H / 2 - 15);
		this.stopAll();
	}

	/**
	 * 关闭帮助
	 */
	public void closeHelp() {
		endGameView = null;
		this.startAll();
	}

	public void isInPay() {
		if (isInPay == 2) {
			int keyCode = Engine.getKeyCode();
			MainServer.getInstance().update(
					Engine.getKeyCodeByLogicKey(keyCode));
			if (MainServer.getInstance().isPayEnd()) {
				isInPay = buyCheeseDialog != null ? 1 : 0;
				if (MainServer.getInstance().isBuySuccess()) {
					System.out.println(">>>GameMainView>>>>>>>购买道具成功<<<<<<<<<<");
					buySucc();
				} else {
					System.out.println(">>>GameMainView>>>>>>>购买道具失败<<<<<<<<<<");
					buyFail();
				}
			}
		}
	}

	/**
	 * 使用保护
	 */
	public void useprotect() {
		if (!((MouseSpriteView) spriteView).isUseProtect()
				&& !((MouseSpriteView) spriteView).isIsdie()) {
			if (newhandView != null) {
				((MouseSpriteView) spriteView).useprotect();
			} else {
				if (propNum[2] > 0) {
					propNum[2] = propNum[2] - 1;
					new Thread() {
						public void run() {
							UserDataManageService.getInsatnce()
									.updateMouseProp(props[2][5], propNum[2],
											true);
						}
					}.start();
					((MouseSpriteView) spriteView).useprotect();
				} else {
					this.toBuyProtected();
				}
			}
		}
	}

	/**
	 * 使用保护
	 */
	public void useBlood() {
		if (spriteView.getSprite().getMaxhp() > spriteView.getSprite().getHp()
				&& !((MouseSpriteView) spriteView).isIsdie()) {
			if (newhandView != null) {
				spriteView.addBlood(1);
			} else {
				if (propNum[3] > 0) {
					propNum[3] = propNum[3] - 1;
					new Thread() {
						public void run() {
							UserDataManageService.getInsatnce()
									.updateMouseProp(props[3][5], propNum[2],
											true);
						}
					}.start();
					spriteView.addBlood(1);
				} else {
					this.toBuyBlood();
				}
			}
		}
	}

	/**
	 * 使用奶酪控制速度
	 * 
	 * @param keyLeft
	 */
	public void controlSpeed(boolean keyLeft) {
		if (!((MouseSpriteView) spriteView).isIsdie()) {
			if (newhandView != null) {
				// if (spriteView.isControlSpeed(keyLeft)) {
				addSpeedType = 0;
				spriteView.controlSpeed(keyLeft);
				// }
			} else {
				System.out.println(" ----- spriteView.getDirection() = " + spriteView.getDirection());
				if(spriteView.getDirection() == 0 || spriteView.getDirection() == 4) {
					if (propNum[1] > 0) {
						// if (spriteView.isControlSpeed(keyLeft)) {
						addSpeedType = 1;
						spriteView.controlSpeed(keyLeft);
						propNum[1] -= 1;
						// }
					} else if (propNum[0] > 0) {
						// if (spriteView.isControlSpeed(keyLeft)) {
						addSpeedType = 2;
						spriteView.controlSpeed(keyLeft);
						propNum[0] -= 1;
						// }
					} else {
						buyAfterControlSpeedKeyLeft = keyLeft;
						toBuyCheeseDialog();
					}
				}
			}
		}
	}

	/**
	 * 老鼠不过关
	 */
	public void mouseNoPassDoor() {
		endGameView = null;
		this.startAll();
		((MouseSpriteView) spriteView).noPassDoor();
	}

	public void buyFail() {

	}

	/**
	 * 购买成功
	 */
	public void buySucc() {
		if ("buyProtected".equals(buyAfterMethod)) {
			this.buyProtected();
		} else if ("buyBlood".equals(buyAfterMethod)) {
			this.buyBlood();
		} else if ("buyRevive".equals(buyAfterMethod)) {
			this.buyRevive();
		} else if ("buyCheese".equals(buyAfterMethod)) {
			this.buyCheese();
		} else if ("buyPassDoor".equals(buyAfterMethod)) {
			this.buyPassDoor();
		}
	}

	/**
	 * 购买保护
	 */
	public void toBuyProtected() {
		isInPay = 2;
		buyAfterMethod = "buyProtected";
		switch (Config.regionType) {
		case MainConfig.Type_GDDX:
		case MainConfig.Type_SXDX:
			MainServer.getInstance().buyProp(props[2][1], props[2][2],
					props[2][4], false, Integer.parseInt(props[2][3]),
					props[2][6], false, false,
					"/propimg/" + props[2][5] + ".png");
			break;
		default:	//计费接口

			MainServer.mPropType = 7;

			MainServer.getInstance().buyProp(props[2][1], props[2][2],
					props[2][4], false, Integer.parseInt(props[2][3]),
					props[2][6], false, false);
		}
	}

	public void buyProtected() {
		propNum[2] += Integer.parseInt(props[2][3]);
		this.useprotect();
	}

	/**
	 * 购买血瓶
	 */
	public void toBuyBlood() {
		isInPay = 2;
		buyAfterMethod = "buyBlood";
		switch (Config.regionType) {
		case MainConfig.Type_GDDX:
		case MainConfig.Type_SXDX:
			MainServer.getInstance().buyProp(props[3][1], props[3][2],
					props[3][4], false, Integer.parseInt(props[3][3]),
					props[3][6], false, false,
					"/propimg/" + props[3][5] + ".png");
			break;
		default:	//计费接口

			MainServer.mPropType = 6;

			MainServer.getInstance().buyProp(props[3][1], props[3][2],
					props[3][4], false, Integer.parseInt(props[3][3]),
					props[3][6], false, false);
		}
	}

	public void buyBlood() {
		propNum[3] += Integer.parseInt(props[3][3]);
		this.useBlood();
	}

	/**
	 * 购买复活
	 */
	public void toBuyRevive() {
		isInPay = 2;
		buyAfterMethod = "buyRevive";
		switch (Config.regionType) {
		case MainConfig.Type_GDDX:
		case MainConfig.Type_SXDX:
			MainServer.getInstance().buyProp(props[4][1], props[4][2],
					props[4][4], false, -1, props[4][6], false, false,
					"/propimg/15.png");
			break;
		default:
			MainServer.getInstance().buyProp(props[4][1], props[4][2],
					props[4][4], false, -1, props[4][6], false, false);
		}
	}

	/**
	 * 购买复活成功
	 */
	public void buyRevive() {
		endGameView = null;
		spriteView.revive();
	}

	/**
	 * 购买奶酪对话框
	 */
	public void toBuyCheeseDialog() {
		isInPay = 1;
		buyCheeseDialog = new BuyCheeseMainGameView(this);
		buyCheeseDialog.init();
	}

	/**
	 * 购买奶酪
	 */
	public void toBuyCheese(String[] props) {
		isInPay = 2;
		buyAfterMethod = "buyCheese";
		switch (Config.regionType) {
		case MainConfig.Type_GDDX:
		case MainConfig.Type_SXDX:
			MainServer.getInstance().buyProp(props[1], props[2], props[4],
					false, Integer.parseInt(props[3]), props[6], false, false,
					"/propimg/" + props[5] + ".png");
			break;
		default:
			MainServer.getInstance().buyProp(props[1], props[2], props[4],
					false, Integer.parseInt(props[3]), props[6], false, false);
		}
	}

	/**
	 * 买奶酪成功
	 */
	public void buyCheese() {
		if (buyCheeseDialog != null) {
			String[] props = buyCheeseDialog.getBuyIngProp();
			if ("0".equals(props[5])) {
				propNum[0] += Integer.parseInt(props[3]);
			} else if ("1".equals(props[5])) {
				propNum[1] += Integer.parseInt(props[3]);
			}
			this.closeBuyCheeseDialog();
			controlSpeed(buyAfterControlSpeedKeyLeft);
		}
	}

	/**
	 * 购买直接过关
	 */
	public void toBuyPassDoor() {
		isInPay = 2;
		buyAfterMethod = "buyPassDoor";
		switch (Config.regionType) {
		case MainConfig.Type_GDDX:
		case MainConfig.Type_SXDX:
			MainServer.getInstance().buyProp(props[5][1], props[5][2],
					props[5][4], false, -1, props[5][6], false, false,
					"/propimg/10.png");
			break;
		default:
			MainServer.getInstance().buyProp(props[5][1], props[5][2],
					props[5][4], false, -1, props[5][6], false, false);
		}
	}

	/**
	 * 购买成功直接过关
	 */
	public void buyPassDoor() {
		endGameView = null;
		((MouseSpriteView) spriteView).passDoor();
	}

	public void closeBuyCheeseDialog() {
		isInPay = 0;
		buyCheeseDialog = null;
	}

	/**
	 * 新手引导
	 * 
	 *
	 */
	public void newHand() {
		newhandView = new NewHandDialog(this);
	}

	public void removeNewHand() {
		newhandView = null;
	}

	public void closeEndGameMeunDialog() {
		this.endGameView = null;
		this.startAll();
	}
}
