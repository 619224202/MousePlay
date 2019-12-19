package com.game.mouse.screen;

import javax.microedition.lcdui.Graphics;

import com.game.lib9.Engine;
import com.game.lib9.L9Config;
import com.game.lib9.L9EngineLogic;
import com.game.lib9.L9Object;
import com.game.lib9.L9Screen;
import com.game.mouse.context.Config;
import com.game.mouse.dialog.TipNoHealthDialog;
import com.game.mouse.gameinfo.GameInfo;
import com.game.mouse.modle.Pass;
import com.game.mouse.modle.ScenePass;
import com.game.mouse.modle.UserMouse;
import com.game.mouse.modle.service.DataManageService;
import com.game.mouse.modle.service.UserDataManageService;
import com.game.mouse.view.BuyCheeseFeedPageView;
import com.game.mouse.view.BuyCheeseView;
import com.game.mouse.view.PassCloudView;
import com.game.mouse.view.PassSceView;
import com.model.base.ParserTbl;
import com.model.control.MyImg;
import com.model.mainServer.MainConfig;
import com.model.mainServer.MainServer;
import com.model.tool.PubToolKit;

public class PassPageScreen extends L9Screen {
	private Graphics g;

	private TipNoHealthDialog tipNoHealthDialog;

	private MyImg bg;

	private MyImg bgbottomImg;

	private PassSceView[] passSceViews = new PassSceView[2];

	private MyImg vsbg;

	private PassCloudView[] cloudViews = new PassCloudView[2];

	private MyImg num;

	private MyImg num2;

	private MyImg cheese;

	/**
	 * 恢复心情需要的奶酪
	 */
	private int renewMoodCheese = 10;

	private int cheese0Num;

	private int isInPay;

	private String buyAfterMethod = "";

	private BuyCheeseView buyCheeseDialog;

	private L9Object talkSprite0;

	private L9Object talkSprite1;

	private int selSceCode;

	private int selPassCode;

	private int mouseCode;

	private MyImg talk0;

	private MyImg talk1;

	private String[][] talks = { { "2", "哇，好多好吃的奶酪！", "-1", "" },
			{ "2", "有只小白猫！", "0", "谁偷了我的奶酪！" },
			{ "2", "笨猫，拿个机器猫吓唬我！", "4", "喵。。。喵。。。" },
			{ "2", "以为加个罩，就偷吃不到了！", "1", "保护奶酪的任务就靠我了！" },
			{ "2", "两只笨猫，看我的！", "0", "花猫咱们同心协力！" },
			{ "2", "笨猫你能抓到我嘛！", "1", "这次一定要逮到小老鼠！" },
			{ "2", "看我的速度！", "0", "用发霉的奶酪，毒死你！" },
			{ "2", "好多小花猫，在劫难逃了！", "1", "咱们一起围杀他！" },
			{ "2", "笨猫，抓不到我！", "1", "真狡猾的老鼠。。" },
			{ "2", "这猫真懒，就知道睡觉！", "8", "呜 呜。。" },
			{ "2", "蓝猫,刚来就这么嚣张！", "3", "来收拾你这爱偷吃的家伙！" },
			{ "2", "这次没路了，和你拼了！", "1", "看你往哪走！" },
			{ "2", "这猫傻了，来回跑！", "5", "主人让我呆着看着奶酪！" },
			{ "2", "别把我的奶酪拖走了！", "2", "等着老鼠上钩吧！" },
			{ "2", "这肥猫吃的这么肥！", "6", "好吃、奶酪真好吃！" },
			{ "2", "好像有人跟着我！", "7", "追你到天涯！" },
			{ "2", "懒猫在睡觉去偷吃点！", "8", "主人不在，睡一觉！" },
			{ "2", "这肥猫装了机关,怎么回去！", "6", "看你下次还敢偷吃我的奶酪！" },
			{ "2", "这地方真够大！", "6", "小子你来错地方了！" },
			{ "2", "又是你这幽灵！", "5", "别想从我这里通过！" },
			{ "2", "这次怎么走呢！", "7", "体验下猫捉老鼠吧！" },
			{ "2", "绕迷路了，头晕！", "7", "这次你还往哪跑！" },
			{ "2", "这鬼地方，下次再也不来了！", "7", "有来无回，呵呵。。" },
			{ "2", "终于要到家团聚了！", "7", "没这么容易！" } };

	private L9Object addMoodObj;

	//我写的逻辑
	public static int mMySelSceCode = 0;

	public PassPageScreen(int mouseCode) {
		this.mouseCode = mouseCode;
		ScenePass lastOpenScene = UserDataManageService.getInsatnce()
				.getLastOpenScene();
		Pass lastOpenPass = UserDataManageService.getInsatnce()
				.getLastOpenPass(lastOpenScene);
		this.selSceCode = Integer.parseInt(lastOpenScene.getCode());
		this.selPassCode = lastOpenPass.getCode();
		this.getProps();
	}

	public PassPageScreen(int mouseCode, int selSceCode, int selPassCode) {
		this.mouseCode = mouseCode;
		this.selSceCode = selSceCode;
		this.selPassCode = selPassCode;
		this.getProps();
	}

	public void getProps() {
		cheese0Num = UserDataManageService.getInsatnce().getPropNumByCode("0");
	}

	public void setSelPassCode(int selPassCode) {
		this.selPassCode = selPassCode;
		this.newtalk(null);
	}

	public void setSelPassCode(int selPassCode, PassSceView passSceView) {
		this.selPassCode = selPassCode;
		this.newtalk(passSceView);
	}

	public void newtalk(PassSceView passSceView) {
		Pass selpass = getSelPass(passSceView);
		if (selpass != null && selpass.getIsOpen() == 1) {
			String sprite0res = "fightmouse" + mouseCode + "0";
			talkSprite0 = new L9Object(null, sprite0res, 2, 50 + 165, 360 + 80, -1, 0,
					false, false, true, 0);
			talkSprite0.setNextFrameTime(2);
			talk0 = new MyImg("passpage/talk/" + (selSceCode * 6 + selPassCode)
					+ "0.png");
			if (!"-1".equals(talks[selSceCode * 6 + selPassCode][2])) {
				String sprite1res = "fightcat"
						+ talks[selSceCode * 6 + selPassCode][2];
				talkSprite1 = new L9Object(null, sprite1res, 2, 590 + 165 + 370, 360 + 80, -1,
						0, false, false, true, 0);
				talkSprite1.setNextFrameTime(2);
				talk1 = new MyImg("passpage/talk/"
						+ (selSceCode * 6 + selPassCode) + "1.png");
			} else {
				talkSprite1 = null;
				talk1 = null;
			}

			//我写的逻辑
			if(selSceCode == 0 && selPassCode == 0){
				talk0 = null;
				vsbg = null;
			}else{
				vsbg = null;
				vsbg = new MyImg("passpage/vsbg.png");
			}

		} else {
			talkSprite0 = null;
			talkSprite1 = null;
			talk0 = null;
			talk1 = null;
		}
	}

	public void Init(int loadCount) {
		switch (loadCount) {
		case 0:
			this.bg = new MyImg("passpage/bg.png");
			this.bgbottomImg = GameInfo.getBottomImg(0);
			this.vsbg = new MyImg("passpage/vsbg.png");
			this.num = new MyImg("passpage/num.png");
			this.num2 = new MyImg("feedpage/num2.png");
			this.cheese = new MyImg("passpage/cheese.png");
			break;
		case 10:
			for (int i = 0; i < this.cloudViews.length; i++) {
				if (i == 0) {
					this.cloudViews[i] = new PassCloudView(i, 105, 105, 105,
							200);
				} else {
					this.cloudViews[i] = new PassCloudView(i, 530, 235, 450,
							530);
				}
				this.cloudViews[i].init();
			}
			break;
		case 30:
			ParserTbl.getInstance().defineMedia(1, "tbl/pass.tbl");
			break;
		case 50:
			this.passSceViews[0] = new PassSceView(this, this.selSceCode,
					this.selPassCode, true, L9Config.SCR_W / 2);
			this.passSceViews[0].init();
			break;
		case 100:
			this.newtalk(null);
			break;
		}
	}

	public void Paint() {
		// TODO Auto-generated method stub
		if (this.g == null) {
			this.g = Engine.FG;
		}
		bg.drawImage(g, 0, 0, 0);
		cheese.drawImage(g, 30 + 20, 20, g.VCENTER | g.HCENTER);
		PubToolKit.drawNum(g, cheese0Num > 99 ? 99 : cheese0Num, this.num2
				.getImg(), 70 + 50, 23, 0, g.VCENTER | g.HCENTER, true);
		for (int i = 0; i < this.cloudViews.length; i++) {
			this.cloudViews[i].paint(g);
		}
		for (int i = 0; i < this.passSceViews.length; i++) {
			if (this.passSceViews[i] != null)
				this.passSceViews[i].paint(g);
		}
		Pass pass = getSelPass(null);
		if (talkSprite0 != null && pass != null) {
			talkSprite0.paintFrame(g);
			if (talkSprite1 != null) {
				talkSprite1.paintFrame(g);
			}

			if(vsbg != null) {
				vsbg.drawImage(g, L9Config.SCR_W / 2, 335 + 80, g.VCENTER | g.HCENTER);
			}
			if (talk0 != null)
				talk0.drawImage(g, 183 + 245, 340 + 80, g.VCENTER | g.HCENTER);
			if (talkSprite1 != null) {
				if (talk1 != null)
					talk1.drawImage(g, 610 + 245, 340 + 80, g.VCENTER | g.HCENTER);
			}
		}
		if (addMoodObj != null) {
			addMoodObj.paintFrame(g);
		}
		bgbottomImg
				.drawImage(g, 0, L9Config.SCR_H - bgbottomImg.getHeight(), 0);
		if (tipNoHealthDialog != null) {
			tipNoHealthDialog.paint(g);
		}
		if (isInPay == 2) {
			MainServer.getInstance().paint(g);
		} else if (isInPay == 1 && buyCheeseDialog != null) {
			buyCheeseDialog.paint(g);
		}

	}

	public void Release() {
		// TODO Auto-generated method stub

	}

	public void Update() {
		for (int i = 0; i < L9EngineLogic.getInstance().objects.size; i++) {
			((L9Object) L9EngineLogic.getInstance().objects.values[i])
					.doAllFrame();
		}
		L9EngineLogic.getInstance().flushRemoveList();
		L9EngineLogic.getInstance().flushAddList();
		if (talkSprite0 != null) {
			talkSprite0.doAllFrame();
			if (talkSprite1 != null) {
				talkSprite1.doAllFrame();
			}
		}
		for (int i = 0; i < this.cloudViews.length; i++) {
			this.cloudViews[i].update();
		}
		for (int i = 0; i < this.passSceViews.length; i++) {
			if (this.passSceViews[i] != null)
				this.passSceViews[i].update();
		}
		if (addMoodObj != null) {
			addMoodObj.doAllFrame();
		}
		if (isInPay == 0) {
			if (tipNoHealthDialog != null) {
				tipNoHealthDialog.update();
				tipNoHealthDialog.keyPress();
			} else {
				keyPress();
			}
		} else if (isInPay == 1) {
			if (buyCheeseDialog != null) {
				buyCheeseDialog.update();
			}
		} else {
			isInPay();
		}

	}

	public void isInPay() {
		if (isInPay == 2) {
			int keyCode = Engine.getKeyCode();
			MainServer.getInstance().update(
					Engine.getKeyCodeByLogicKey(keyCode));
			if (MainServer.getInstance().isPayEnd()) {
				isInPay = buyCheeseDialog != null ? 1 : 0;
				if (MainServer.getInstance().isBuySuccess()) {
					System.out.println(">>>PassPageScreen>>>>>>>购买道具成功<<<<<<<<<<");
					buySucc();
				} else {
					System.out.println(">>>PassPageScreen>>>>>>>购买道具失败<<<<<<<<<<");
				}
			}
		}
	}

	/**
	 * 购买成功
	 */
	public void buySucc() {
		if ("buyCheese".equals(buyAfterMethod)) {
			this.buyCheese();
		}
	}

	public void startChangeSce(boolean isMoveLeft) {
		if (isMoveLeft) {
//			this.selSceCode = this.selSceCode < 3 ? this.selSceCode + 1 : 0;
//			if(this.selSceCode == 2){
//				this.selSceCode = 3;
//			}
			if(this.selSceCode >= 1){
				return;
			}
			this.selSceCode = this.selSceCode == 1 ? 0 : 1;
//			System.out.println(" ---- this.selSceCode = " + this.selSceCode + " ---- isMoveLeft ---- ");
		} else {
//			this.selSceCode = this.selSceCode > 0 ? this.selSceCode - 1 : 3;
//			if(this.selSceCode == 2){
//				this.selSceCode = 1;
//			}
			if(this.selSceCode <= 0){
				return;
			}
			this.selSceCode = this.selSceCode == 1 ? 0 : 1;
//			System.out.println(" ---- this.selSceCode = " + this.selSceCode + " ---- isMoveRight ---- ");
		}
		for (int i = 0; i < this.passSceViews.length; i++) {
			if (this.passSceViews[i] == null
					|| (!this.passSceViews[i].isPaintSce() && this.passSceViews[i]
							.getScecode() != this.selSceCode)) {
				int initX = isMoveLeft ? (L9Config.SCR_W / 2 + L9Config.SCR_W)
						: (L9Config.SCR_W / 2 - L9Config.SCR_W);
				this.passSceViews[i] = new PassSceView(this, this.selSceCode,
						this.selPassCode, false, initX);
				this.passSceViews[i].init();
				this.passSceViews[i].setMove(isMoveLeft);
			} else {
				this.passSceViews[i].setMove(isMoveLeft);
			}
		}
	}

	/**
	 * 获得当前显示的场景
	 * 
	 * @return
	 */
	public PassSceView getPassSceView() {
		PassSceView passSceView = null;
		for (int i = 0; i < this.passSceViews.length; i++) {
			if (this.passSceViews[i] != null
					&& this.passSceViews[i].isPaintSce()) {
				passSceView = this.passSceViews[i];
			}
		}
		return passSceView;
	}

	public void keyPress() {
		int keyCode = Engine.getKeyCode();
		switch (keyCode) {
		case Engine.K_KEY_LEFT:
			this.keyleft();
			break;
		case Engine.K_KEY_UP:
			keyup();
			break;
		case Engine.K_KEY_DOWN:
			keydown();
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
		PassSceView passSceView = this.getPassSceView();
		if (passSceView != null && !passSceView.isIsmove()) {
			if (passSceView.getSelPassCode() > 0) {
				passSceView.setSelPassCode(passSceView.getSelPassCode() - 1);
				this.setSelPassCode(passSceView.getSelPassCode());
			} else {
				this.startChangeSce(false);
			}
		}
	}

	public void keyright() {
		PassSceView passSceView = this.getPassSceView();
		if (passSceView != null && !passSceView.isIsmove()) {
			if (passSceView.getSelPassCode() < 5) {
				passSceView.setSelPassCode(passSceView.getSelPassCode() + 1);
				this.setSelPassCode(passSceView.getSelPassCode());
			} else {
				this.startChangeSce(true);
			}
		}
	}

	public void keyup() {
	}

	public void keydown() {

	}

	public void keyfire() {
		PassSceView passSceView = this.getPassSceView();
		if (passSceView != null && !passSceView.isIsmove()) {
			Pass p = (Pass) passSceView.getScenePass().getPass().elementAt(
					selPassCode);
//			if (p.getIsOpen() == 1) {
				this.startGame(false);
//			}
		}
	}

	public void keynum0() {
		HomePageScreen homePageScreen = new HomePageScreen(0);
		MainMidlet.engine.changeState(homePageScreen);
	}

	public void removeDialog() {
		this.tipNoHealthDialog = null;
	}

	public void startGame(boolean isGoGame) {
		if (!isGoGame) {
			UserMouse userMouse = DataManageService.getInsatnce()
					.getUserMouseByCode(mouseCode + "");
			if (Config.isTipNoHealth && userMouse.getNowMoodByTime() <= 30) {
				this.tipNoHealth();
			} else {
				isGoGame = true;
			}
		}
		if (isGoGame) {

			System.out.println(" ---- selSceCode = " + selSceCode + ", ---- selPassCode = " + selPassCode);

		    int mySelSceCode = selSceCode;
		    int mySelPassCode = selPassCode;
			boolean isFirstPlay = true;

			if(selSceCode == 1 && selPassCode == 5){
				mySelSceCode = 0;
			}

		    if(selSceCode == 2){
                mySelSceCode = 0;
                isFirstPlay = false;
            }
		    if(selSceCode == 3){
                mySelSceCode = 1;
                isFirstPlay = false;
            }

			mMySelSceCode = selSceCode;

			GamePageScreen gamePageScreen = new GamePageScreen(mySelSceCode,
					mySelPassCode, this.mouseCode,isFirstPlay);
			MainMidlet.engine.changeState(gamePageScreen);
		}
	}

	public void tipNoHealth() {
		tipNoHealthDialog = new TipNoHealthDialog(this, L9Config.SCR_W / 2,
				L9Config.SCR_H / 2 - 15, renewMoodCheese);
		tipNoHealthDialog.init();
	}

	public void goFeedPage() {
		FeedPageScreen feedPageScreen = new FeedPageScreen();
		MainMidlet.engine.changeState(feedPageScreen);
	}

	/**
	 * 得到选中的关卡
	 * 
	 * @return
	 */
	public Pass getSelPass(PassSceView passSceView) {
		if (passSceView == null) {
			passSceView = this.getPassSceView();
		}
		if (passSceView != null && !passSceView.isIsmove()) {
			Pass p = (Pass) passSceView.getScenePass().getPass().elementAt(
					selPassCode);
			return p;
		}
		return null;
	}

	/**
	 * 恢复心情
	 */
	public void renewMood() {
		if (cheese0Num - renewMoodCheese >= 0) {
			final UserMouse userMouse = DataManageService.getInsatnce()
					.getUserMouseByCode(mouseCode + "");
			userMouse.addNowMood(70);
			new Thread() {
				public void run() {
					cheese0Num = cheese0Num - renewMoodCheese > 0 ? cheese0Num
							- renewMoodCheese : 0;
					UserDataManageService.getInsatnce().updateMouseProp("0",
							cheese0Num, true);
					UserDataManageService.getInsatnce().updateMouse(userMouse);
				}
			}.start();
			this.tipNoHealthDialog = null;
			this.addMoodEffect();
		} else {
			tipBuyCheess();
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

		//我写的逻辑
//		if(MainMidlet.Area.equals("SBY")){
//			MainServer.getInstance().buyProp(props[1], props[2], props[4],
//					false, Integer.parseInt(props[3]), props[6], false, false);
//		}
	}

	public void buyCheese() {
		String[] props = buyCheeseDialog.getBuyIngProp();
		cheese0Num += Integer.parseInt(props[3]);
		UserDataManageService.getInsatnce().updateMouseProp("0", cheese0Num,
				true);
		this.closeBuyCheeseDialog();
		renewMood();
	}

	public void closeBuyCheeseDialog() {
		isInPay = 0;
		buyCheeseDialog = null;
	}

	/**
	 * 恢复心情效果
	 */
	public void addMoodEffect() {
		this.addMoodObj = new L9Object(null, "addmood", 2, 50, 360, 1, 0,
				false, false, true, 0) {
			public void endAnimation() {
				removeMoodEffect();
			}
		};
	}

	public void removeMoodEffect() {
		this.addMoodObj = null;
		this.startGame(true);
	}
}
