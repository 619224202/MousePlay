package com.model.mainServer;

import java.lang.reflect.Type;
import java.util.Hashtable;
import java.util.Vector;

import javax.microedition.io.ConnectionNotFoundException;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.game.GameCanvas;
import javax.microedition.midlet.MIDlet;

import com.game.lib9.L9Config;
import com.game.mouse.context.Config;

import com.game.mouse.modle.service.UserDataManageService;
import com.iptv.ipal.impl.IPayOver;
import com.model.screen.BuyDialog;
import com.model.screen.DialogInterface;
import com.model.screen.ErrorDialog;
import com.model.screen.OrderBuyDialog;
import com.model.screen.OverDialog;
import com.model.screen.TwiceDialog;
import com.model.screen.WaitingDia;
import com.store.wanglu.sichuan_module.IptvPay;

public class MainServer {

	public MainServerInterface httpServer;

	private static MainServer instance;

	private MIDlet mainMidlet;

	public MainConfig mainConfig;

	private int exchageMoney;

	private Canvas gameCanvas;

	private Displayable bufCanvas;

	private boolean bRun;

	private int key_Code;

	private int state;
	private DialogInterface dialog;

	private String propCode;
	private String propName;
	private String price;
	private int coinNum;

	private int[] diaPos = new int[2];

	private boolean isBuySuccess;

	private Object obj;

	private boolean isPayEnd;

	private boolean isTest;

	private Thread t1;

	private String lockStr = "";
	private String bufLock = "";

	private boolean isRecharge;

	private int rechargePrice;

	/**
	 * 0是确认按钮路径，1是取消按钮路径，2是购买背景路径，3是充值页面路径，4.充值页面按钮底板路径,5是童锁路径
	 */
	public String[] imgStrs;

	private boolean bUseGameImg;
	private boolean bUseThread;

	private int buyNum;

	private String propDesc;
	
	public static int allPrice = 0;

	//我写的逻辑
	public static int mPropType = 0;	//购买道具的类型

	public boolean mIsInPay = false;	//是否处于支付状态

	/**
	 * 初始化该类
	 * 
	 * @return
	 */
	public static MainServer getInstance() {
		if (instance == null) {
			instance = new MainServer();
		}
		return instance;
	}

	/**
	 * 0是确认按钮路径，1是取消按钮路径，2是购买背景路径，3是充值页面路径，4.充值页面按钮底板路径,5是童锁路径
	 */
	public void setBuyImg(String[] imgStr) {
		this.imgStrs = imgStr;
		bUseGameImg = true;
	}

	public void setYZMImg(String[] imgStr) {

	}

	public void setServerInit(Canvas gameCanvas, MIDlet midlet, int regionType,
			String gameCode, boolean isTest) {
		this.setServerInit(gameCanvas, midlet, regionType, gameCode, "",
						isTest);
	}

	/**
	 * 初始化后执行
	 */
	public void setServerInit(Canvas gameCanvas, MIDlet midlet, int regionType,
			String gameCode, String gameName, boolean isTest) {
		// setFullScreenMode(true);
		MainConfig.checkFont();
		this.gameCanvas = gameCanvas;
		this.mainMidlet = midlet;
		this.imgStrs = new String[] { "/pay/but1.png", "/pay/but2.png",
				"/pay/buyBg.png", "/pay/rechargeBg.png", "/pay/czan.png",
				"/pay/ts.png", "/pay/hintStr2.png" };
		mainConfig = new MainConfig();
		mainConfig.regionType = regionType;
		diaPos[0] = L9Config.SCR_W/2;
		diaPos[1] = 260;
		this.obj = new Object();
		isPayEnd = true;
		isRecharge = false;
		this.isTest = isTest;
		this.httpServer = new SwServer();
		this.getProperty(midlet, gameCode);
	}

	/**
	 * 
	 * @param propCode:道具code
	 * @param price:道具价格
	 * @param propName:道具名称
	 * @param bSetSecond:是否使用3秒：只在浙江电信使用
	 * @param buyNum:购买数量，当数量为-1时则显示购买XX,否则则购买X个XX
	 */
	public void buyProp(String propCode, String price, String propName,
			boolean bSetSecond, int buyNum, String des) {
		// TODO Auto-generated method stub
		buyProp(propCode, price, propName, bSetSecond, buyNum, des, false,
				false, false);

	}

	/**
	 * 
	 * @param propCode:道具code
	 * @param price:道具价格
	 * @param propName:道具名称
	 * @param bSetSecond:是否使用3秒：只在浙江电信使用
	 * @param buyNum:购买数量，当数量为-1时则显示购买XX,否则则购买X个XX
	 * @param des:描述
	 * @param bUseDes:是否显示描述，显示后则在道具栏目显示(des(当前还具备XX元宝))
	 */
	public void buyProp(String propCode, String price, String propName,
			boolean bSetSecond, int buyNum, String des, boolean bUseDes) {
		// TODO Auto-generated method stub
		buyProp(propCode, price, propName, bSetSecond, buyNum, des, bUseDes,
				false, false);

	}

	/**
	 * 
	 * @param propCode:道具Code
	 * @param price：道具价格
	 * @param propName:道具名称
	 * @param bSetSecond:是否使用3秒
	 * @param buyNum:购买数量，当数量为-1时则显示购买XX,否则则购买X个XX
	 * @param des：描述
	 * @param bUseDes:是否使用描述购买道具,则在道具栏显示(des(当前还具备XX元宝))
	 * @param bUseThread:是否使用线程
	 */
	public void buyProp(String propCode, String price, String propName,
			boolean bSetSecond, int buyNum, String des, boolean bUseDes,
			boolean bUseThread, String propImgName) {
		this.buyProp(propCode, price, propName, bSetSecond, buyNum, des,
				bUseDes, bUseThread, false);
	}

	/**
	 * 
	 * @param propCode:道具Code
	 * @param price：道具价格
	 * @param propName:道具名称
	 * @param bSetSecond:是否使用3秒
	 * @param buyNum:购买数量，当数量为-1时则显示购买XX,否则则购买X个XX
	 * @param des：描述
	 * @param bUseDes:是否使用描述购买道具,则在道具栏显示(des(当前还具备XX元宝))
	 * @param bUseThread:是否使用线程
	 */
	public void buyProp(String propCode, String price, String propName,
			boolean bSetSecond, int buyNum, String des, boolean bUseDes,
			boolean bUseThread, String propImgName, int propImgpx, int propImgpy) {
		this.buyProp(propCode, price, propName, bSetSecond, buyNum, des,
				bUseDes, bUseThread, false);
	}

	public void buyProp(String propCode, String price, String propName,
			boolean bSetSecond, int buyNum, String des, boolean bUseDes,
			boolean bUseThread, boolean isbeforeBuyTip, String tipMsg,
			String propImgName, int propImgpx, int propImgpy) {
		this.buyProp(propCode, price, propName, bSetSecond, buyNum, des,
				bUseDes, bUseThread, false);
	}

	public void buyProp(String propCode, String price, String propName,
			boolean bSetSecond, int buyNum, String des, boolean bUseDes,
			boolean bUseThread) {
		this.buyProp(propCode, price, propName, bSetSecond, buyNum, des,
				bUseDes, bUseThread, false);
	}

	/**
	 * 
	 * @param propCode:道具Code
	 * @param price：道具价格
	 * @param propName:道具名称
	 * @param bSetSecond:是否使用3秒
	 * @param buyNum:购买数量，当数量为-1时则显示购买XX,否则则购买X个XX
	 * @param des：描述
	 * @param bUseDes:是否使用描述购买道具,则在道具栏显示(des(当前还具备XX元宝))
	 * @param bUseThread:是否使用线程
	 */
	public void buyProp(String propCode, String price, String propName,
			boolean bSetSecond, int buyNum, String des, boolean bUseDes,
			boolean bUseThread, boolean isTwice) {
		bRun = true;
		state = 999;
		isBuySuccess = false;
		isPayEnd = false;
		isRecharge = false;
		this.propCode = propCode;
		this.price = price;
		this.propName = propName;
		this.bUseThread = bUseThread;
		this.buyNum = buyNum;
		this.propDesc = des;
        state = 999;
        IptvPay.getInstance().pay(mPropType, new IPayOver() {
            @Override
            public void paySuccess(int propId) {
                MainServer.this.paySuccess();
                UserDataManageService.getInsatnce().mySaveGameData();
            }

            @Override
            public void payFailed(int propId) {
                MainServer.this.payFailed();
            }
        });
//		isPayEnd = true;
//		isBuySuccess = true;
//		if (ConstantSW.isUserOrderMonth) {
//		System.out.println(" ***** coinNum = " + coinNum);
//			if (coinNum == 0) {
//				coinNum = httpServer.getVirtualCoin();
//			}
//			coinNum = 2000;

//			dialog = new BuyDialog(bSetSecond, price, propName, buyNum, this,
//					coinNum);
//			dialog.setPos(diaPos[0], diaPos[1]);
//			this.state = 0;
//			if (bUseDes) {
//				((BuyDialog) dialog).setShowStr(des);
//			}


//			if (this.bUseGameImg) {
//				String tipString = imgStrs.length > 7 ? imgStrs[7]
//						: "/pay/hintStr2.png";
//				String xkStr = imgStrs.length > 8 ? imgStrs[8] : "";
//
//				((BuyDialog) dialog).setImg(imgStrs[2], imgStrs[0], imgStrs[1],
//						tipString, xkStr);
//			}
//		} else {
//			dialog = new OrderBuyDialog();
//			dialog.setPos(diaPos[0], diaPos[1]);
//			this.state = 3;
//			if (bUseDes) {
//				((BuyDialog) dialog).setShowStr(des);
//			}
//			if (this.bUseGameImg) {
//				String tipString = imgStrs.length > 7 ? imgStrs[7]
//						: "/pay/hintStr2.png";
//				String xkStr = imgStrs.length > 8 ? imgStrs[8] : "";
//				((OrderBuyDialog) dialog).setImg(imgStrs[2], imgStrs[0],
//						imgStrs[1], tipString, xkStr);
//			}
//		}
	}

	public boolean isPayEnd() {
		return isPayEnd;
	}

	public void setXY(int x, int y) {
		diaPos[0] = x;
		diaPos[1] = y;
	}

	public String getData(int index) {
		// TODO Auto-generated method stub
//		return httpServer.getData(index);
		return "";
	}

	public String getData() {
		// TODO Auto-generated method stub
//		return httpServer.getData();
		return "";
	}

	public Vector getRank(int type) {
		// TODO Auto-generated method stub
//		return httpServer.getRank(type);
		return null;
	}

	public int getScore(int type) {
		// TODO Auto-generated method stub
//		return httpServer.getScore(type);
		return 0;
	}

	public String[] getUserRank(int type) {
		// TODO Auto-generated method stub
//		return httpServer.getUserRank(type);
		return null;
	}

	public int getVirtualCoin() {
		// TODO Auto-generated method stub
		return httpServer.getVirtualCoin();
	}

	public boolean login() {
//		return httpServer.login();
		return true;
	}

	public String getSysData() {
//		return httpServer.getSysData();
		return "";
	}

	public void saveData(int index, String data) {
		// TODO Auto-generated method stub
//		httpServer.saveData(index, data);
	}

	public void saveData(String data) {
		// TODO Auto-generated method stub
//		httpServer.saveData(data);
	}

	public void updateScore(int type, int score) {
		// TODO Auto-generated method stub
//		httpServer.updateScore(type, score);
	}

	public void updateScore(int score) {
//		httpServer.updateScore(score);
	}

	public void goToGame() {
		bRun = false;
		isPayEnd = true;
		BuyDialog.isRunning = true;
//		if (bUseThread) {
//			Display.getDisplay(mainMidlet).setCurrent(bufCanvas);
//			((Canvas) bufCanvas).repaint();
//		}
	}

	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		if (dialog != null && dialog.isRunning()) {
			dialog.paint(g);
		}
	}

	public void keyPressed(int keyCode) {
		switch (state) {
		case 0:// 购买对话框
			toBuyLogic(keyCode);
			break;
		case 1:// 等待对话框
			break;
		case 2:// 成功或失败对话框
			break;
		case 3:// 二次确认提示框
			toOrderMonthTwiceLogic(keyCode);
			break;
		case 4:// 订购包月
			toOrderMonthILogic(keyCode);
			break;
		case 5:
			toOrderMonthILogic(keyCode);
			break;
		case 6:
			break;
		case 7:
			break;
		case 99:
			dialog.keyPressed(keyCode);
			break;
		}
		// repaint();
	}

	public void keyReleased(int keyCode) {
	}

	public void toBuyLogic(int keyCode) {
		dialog.keyPressed(keyCode);
		if (!dialog.isRunning()) {
			System.out.println(" ---- toBuyLogic() ---- ");
			dialog.release();
			state = 999;
			if (dialog.getSelOption() == BuyDialog.Yes_Option) {
				//这里修改逻辑
				if(!mIsInPay) {
					mIsInPay = true;
					if(MainConfig.Area == MainConfig.Type_SC){
						IptvPay.getInstance().pay(mPropType, new IPayOver() {
							@Override
							public void paySuccess(int propId) {
								MainServer.this.paySuccess();
								UserDataManageService.getInsatnce().mySaveGameData();
							}

							@Override
							public void payFailed(int propId) {
								MainServer.this.payFailed();
							}
						});
					}else if(MainConfig.Area == MainConfig.Type_BY){
						paySuccess();
					}
				}
				System.out.println(" ---- toBuyLogic ---- ");
//				buy("");
			} else {
				goToGame();
				isBuySuccess = false;
			}
		}
	}

	/**
	 * 消费道具解析
	 * 
	 * @param flag
	 */
	public void buyIngLogic(boolean flag) {
		if (httpServer.getServerData() != null) {
			int returnCode = ParseSW.parserBuy(httpServer.getServerData(),
					isTest);
			parseBuy(returnCode, flag);
		} else if (httpServer.isBuyTimeOut()) {
			parseBuy(MainConfig.BUY_SUCCESS, flag);
		}
	}

	public void buy(String lockStr) {

//		goToGame();

		//调用订购
		isPayEnd = true;
		isBuySuccess = true;

		mIsInPay = false;

		System.out.println(" ---- buy ---- ");

//		int priceNum = Integer.parseInt(price);
//		httpServer.buyProp(propCode, priceNum + "", propName, lockStr);
//		state = 1;
//		dialog = new WaitingDia();
//		((WaitingDia) dialog).setHttpServer(httpServer);
//		dialog.setPos(diaPos[0], diaPos[1]);
//		if (bUseGameImg) {
//			((WaitingDia) dialog).setImgName(imgStrs[2]);
//		}
	}

	public void toOrderMonthILogic(int keyCode) {
		dialog.keyPressed(keyCode);
		if (!dialog.isRunning()) {
			dialog.release();
			if (dialog.getSelOption() == OrderBuyDialog.Yes_Option) {
				orderMonth("");
			} else {
				goToGame();
				isBuySuccess = false;
			}
		}
	}

	/**
	 * 订购包月解析
	 * 
	 * @param flag
	 */
	public void orderMonthIngLogic(boolean flag) {
		if (httpServer.getServerData() != null) {
			int returnCode = ParseSW.parserMonthBuy(httpServer.getServerData(),
					isTest);
			parseBuy(returnCode, flag);
		}
	}

	public void toOrderMonthTwiceLogic(int keyCode) {
		dialog.keyPressed(keyCode);
		if (!dialog.isRunning()) {
			dialog.release();
			if (dialog.getSelOption() == BuyDialog.Yes_Option) {
				createTwiceOrderMonthDialog();
			} else {
				goToGame();
				isBuySuccess = false;
			}
		}
	}

	public void orderMonth(String lockStr) {
		httpServer.orderMonth(lockStr);
		state = 5;
		dialog = new WaitingDia();
		((WaitingDia) dialog).setHttpServer(httpServer);
		dialog.setPos(diaPos[0], diaPos[1]);
		if (bUseGameImg) {
			((WaitingDia) dialog).setImgName(imgStrs[2]);
		}
	}

	public void createTwiceOrderMonthDialog() {
		dialog = new TwiceDialog();
		state = 4;
		dialog.setPos(diaPos[0], diaPos[1]);
	}

	public void update() {
		switch (state) {
		case 0:// 二次确认页面
			if (dialog != null)
				dialog.update();
			break;
		case 1:// 正在购买页面
			if (dialog != null)
				dialog.update();
			buyIngLogic(true);
			break;
		case 2:// 成功或失败页面
			if (dialog != null) {
				dialog.update();
				if (!dialog.isRunning()) {
					dialog.release();
					goToGame();
				}
			} else {
				this.goToGame();
			}
			break;
		case 3:// 二次确认
			if (dialog != null)
				dialog.update();
			break;
		case 4:// 充值
			if (dialog != null)
				dialog.update();
			break;
		case 5:// 正在订购包月
			if (dialog != null)
				dialog.update();
			orderMonthIngLogic(false);
			break;
		case 6:// 童锁
			if (dialog != null)
				dialog.update();
			break;
		case 7:// 充值二次确认
			if (dialog != null)
				dialog.update();
			break;
		case 99:// 错误对话框
			if (dialog != null)
				errorLogic();
			break;
		}
	}

	public void update(int keyCode) {
		keyPressed(keyCode);
		update();
	}

	public void quitGame() throws ConnectionNotFoundException {
		((SwServer) httpServer).saveURL(ConstantSW.returnUrl);
		mainMidlet.platformRequest(ConstantSW.returnUrl);
		mainMidlet.notifyDestroyed();
	}
	
	public void addViricalCoin(int addNum){
		((SwServer) httpServer).addViricalCoin(coinNum);
	}

	private void twiceLogic(int keyCode) {
		dialog.keyPressed(keyCode);
		if (!dialog.isRunning()) {
			dialog.release();
			if (dialog.getSelOption() == BuyDialog.Yes_Option) {
				buy("");
			} else {
				goToGame();
				isBuySuccess = false;
			}
		}
	}

	private void parseBuy(int returnCode, boolean flag) {
		String str = "";
		switch (returnCode) {
		case MainConfig.BUY_SUCCESS:
			coinNum = coinNum - Integer.parseInt(price) > 0 ? coinNum
					- Integer.parseInt(price) : 0;
			isBuySuccess = true;
			str = "购买道具成功";
			dialog = new OverDialog(str);
			if (bUseGameImg) {
				String tipString = imgStrs.length > 7 ? imgStrs[7]
						: "/pay/hintStr1.png";
				((OverDialog) dialog).setImg(imgStrs[2], tipString);
			}
			dialog.setPos(diaPos[0], diaPos[1]);
			state = 2;
			break;
		case MainConfig.BUY_TIMEOUT:
			isBuySuccess = false;
			if (flag) {
				str = "购买道具超时，请重试！";
			} else {
				str = "充值超时，请重试";
			}
			dialog = new OverDialog(str);
			if (bUseGameImg) {
				String tipString = imgStrs.length > 7 ? imgStrs[7]
						: "/pay/hintStr1.png";
				((OverDialog) dialog).setImg(imgStrs[2], tipString);
			}
			dialog.setPos(diaPos[0], diaPos[1]);
			state = 2;
			break;
		case MainConfig.BUY_CONNERR:
			isBuySuccess = false;
			if (flag) {
				str = "购买失败，网络发生错误,请重试！";
			} else {
				str = "充值失败，网络发生错误,请重试";
			}
			dialog = new OverDialog(str);
			if (bUseGameImg) {
				String tipString = imgStrs.length > 7 ? imgStrs[7]
						: "/pay/hintStr1.png";
				((OverDialog) dialog).setImg(imgStrs[2], tipString);
			}
			dialog.setPos(diaPos[0], diaPos[1]);
			state = 2;
			break;
		case MainConfig.BUY_FAILED:
			isBuySuccess = false;
			str = "购买道具失败";
			dialog = new OverDialog(str);
			if (bUseGameImg) {
				String tipString = imgStrs.length > 7 ? imgStrs[7]
						: "/pay/hintStr1.png";
				((OverDialog) dialog).setImg(imgStrs[2], tipString);
			}
			dialog.setPos(diaPos[0], diaPos[1]);
			state = 2;
			break;
		case MainConfig.ORDERMONTH_SUCCESS:
			isBuySuccess = true;
			str = "恭喜您订购包月成功";
			dialog = new OverDialog(str);
			if (bUseGameImg) {
				String tipString = imgStrs.length > 7 ? imgStrs[7]
						: "/pay/hintStr1.png";
				((OverDialog) dialog).setImg(imgStrs[2], tipString);
			}
			dialog.setPos(diaPos[0], diaPos[1]);
			state = 2;
			ConstantSW.isUserOrderMonth = true;
			break;
		case MainConfig.ORDERMONTH_FAILED:
			isBuySuccess = false;
			str = "订购包月失败";
			dialog = new OverDialog(str);
			if (bUseGameImg) {
				String tipString = imgStrs.length > 7 ? imgStrs[7]
						: "/pay/hintStr1.png";
				((OverDialog) dialog).setImg(imgStrs[2], tipString);
			}
			dialog.setPos(diaPos[0], diaPos[1]);
			state = 2;
			break;
		case MainConfig.BUY_MONTHFAIL:
			isBuySuccess = false;
			break;
		case MainConfig.BUY_LIMMITED:
			isBuySuccess = false;
			break;
		case MainConfig.BUY_NOTBUY:
			isBuySuccess = false;
			break;
		case MainConfig.BUY_CHILDLOCK:
			// 显示童锁
			break;
		default:
			break;
		}
	}

	public boolean isBuySuccess() {
		return isBuySuccess;
	}

	public void errorLogic() {
		dialog.update();
		if (!dialog.isRunning()) {
			try {
				mainMidlet.platformRequest(ConstantSW.returnUrl);
			} catch (ConnectionNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			mainMidlet.notifyDestroyed();
		}
	}

	public void getProperty(MIDlet midlet, String gameCode) {
//		ConstantSW.serverIp = midlet.getAppProperty("serverIp");
//		if (midlet.getAppProperty("serverProject") != null
//				&& !"".equals(midlet.getAppProperty("serverProject"))) {
//			ConstantSW.serverProject = midlet.getAppProperty("serverProject");
//		}
//		ConstantSW.stbId = midlet.getAppProperty("stbID");
////		ConstantSW.stbId = midlet.getAppProperty("stbID");
//		String businessId = midlet.getAppProperty("businessID");
//		if(businessId==null ||  businessId.length()==0){
//			businessId = midlet.getAppProperty("userId");
//		}
//		ConstantSW.businessId = businessId;
//		ConstantSW.userToken = midlet.getAppProperty("userToken");
//		ConstantSW.gameCode = midlet.getAppProperty("gameCode");
//		if (midlet.getAppProperty("productCode") != null
//				&& !"".equals(midlet.getAppProperty("productCode"))) {
//			ConstantSW.productCode = midlet.getAppProperty("productCode");
//		}
//		ConstantSW.returnUrl = midlet.getAppProperty("returnURL");
//		if(ConstantSW.returnUrl==null || ConstantSW.returnUrl.length()==0){
//			ConstantSW.returnUrl = midlet.getAppProperty("returnUrl");
//		}
//		if (midlet.getAppProperty("userOrderMonth") != null
//				&& !"".equals(midlet.getAppProperty("userOrderMonth"))) {
//			ConstantSW.isUserOrderMonth = Integer.parseInt(midlet
//					.getAppProperty("userOrderMonth")) == 1;
//		}
//		if (ConstantSW.stbId == null || ConstantSW.stbId.equals("")) {
//			String s = "获取启动参数失败(stbId缺少)";
//			System.out.println(s);
//			toError(s);
//			return;
//		}
//		if (ConstantSW.businessId == null || ConstantSW.businessId.equals("")) {
//			String s = "获取启动参数失败(businessId缺少)";
//			toError(s);
//			return;
//		}
//		if (ConstantSW.serverIp == null || ConstantSW.serverIp.equals("")) {
//			String s = "获取网络连接地址失败";
//			toError(s);
//			return;
//		}
//		if (ConstantSW.productCode == null || ConstantSW.productCode.equals("")) {
//			String s = "获取订购包月编码失败";
//			toError(s);
//			return;
//		}
	}

	public void toError(String s) {
		dialog = new ErrorDialog(s);
		state = 99;

	}

	public String getPriceName() {
	    if(MainConfig.Area == MainConfig.Type_SBY){
            return "云币";
        }
	    else if(MainConfig.Area == MainConfig.Type_HN){
			return "元";
		}
	    else if(MainConfig.Area == MainConfig.Type_HB){
			return "元";
		}
		else if(MainConfig.Area == MainConfig.Type_SC){
			return "元";
		}else{
	        return "游戏币";
        }
	}

	public void saveUrl(String url) {
		((SwServer) httpServer).saveURL(url);
	}

	public Hashtable getGamePropPrice() {
		// if (mainConfig.regionType == MainConfig.Type_AHDX
		// || mainConfig.regionType == MainConfig.Type_TJLT) {
		// Hashtable propsPrice = ((SwServer) httpServer).getGamePropPrice();
		// return propsPrice;
		// } else {
		// return null;
		// }
		return null;
	}

	/**
	 * 保存用户信息
	 * 
	 * @param loginSeq
	 * @param state
	 * @param memory
	 * @param otherPrams
	 */
	public boolean saveGamesStbLogs(String loginSeq, int state, long memory,
			String otherPrams) {
//		return ((SwServer) httpServer).saveGamesStbLogs(loginSeq, state,
//				memory, otherPrams);
		return false;
	}

	public boolean isOpenSound() {
		return false;
	}

	/**
	 * 获取资源文件
	 * 
	 * @param resPath
	 * @return
	 */
	public byte[] getRes(String resPath) {
		return null;
	}

	public String getResPath() {
		return null;
	}

	public void saveRemoteUserMsg() {

	}
	
	public byte[] getImgRes(String imaName){
		return null;
	}
	
	public void endGame(){

		System.out.println("正在退出游戏中 --------- ");

		UserDataManageService.getInsatnce().mySaveGameData();	//我写的本地储存数据方法

		System.exit(0);

		//我做的修改（注释了原来的代码）
//		saveUrl(ConstantSW.returnUrl);
//		try {
//			mainMidlet.platformRequest(ConstantSW.returnUrl);
//		} catch (ConnectionNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}

	public void playMedia(String soundUrl, int count, int isreplce) {
//		((SwServer) httpServer).playMedia(soundUrl, count, isreplce);
	}

	public void paySuccess(){
		buy("");
		goToGame();
	}

	public void payFailed(){
		goToGame();
		isPayEnd = true;
		mIsInPay = false;
		isBuySuccess = false;
	}
}
