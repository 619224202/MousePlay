package com.game.mouse.screen;

import javax.microedition.lcdui.Graphics;
import javax.microedition.midlet.MIDlet;

import com.game.lib9.Engine;
import com.game.lib9.L9Config;
import com.game.lib9.L9IState;
import com.game.mouse.context.Config;
import com.model.mainServer.MainConfig;
import com.model.mainServer.MainServer;

public class MainScreen extends Engine implements L9IState {
	private Graphics g;

	private String[] imgName = { "/paygame/qdbut.png", "/paygame/qxbut.png",
			"/paygame/buybg.png", "/paygame/czbg.png", "/paygame/czbut.png",
			"/paygame/lock.png", "/paygame/tr.png" };

	private String[] yzmButName = new String[] { "/paygame/but3.png",
			"/paygame/but4.png", "/paygame/yzm.png" };

	public MainScreen(MIDlet app) {
		super(app);

		//初始化数据
		if(Config.region == null || Config.region.length() == 0){
			Config.region = "";
		}

		this.getMyResURL(app);
		Config.returnUrl = app.getAppProperty("returnURL");
		Config.regionType = MainConfig.Type_ZJDX;
		Config.retunType = 0;
		Config.openSound = 1;
		MainServer.getInstance().setServerInit(this, app, Config.regionType,
				"猫和老鼠", false);
		MainServer.getInstance().setXY(L9Config.SCR_W/2, 250);
		if (MainServer.getInstance().getResPath() != null
				&& !"".equals(MainServer.getInstance().getResPath())) {
			Config.resURL = MainServer.getInstance().getResPath();
		}
//		if (!"ZJDX".equals(Config.region) || !"TSJSDX".equals(Config.region)) {
//			MainServer.getInstance().setBuyImg(imgName);
//			MainServer.getInstance().setYZMImg(yzmButName);
//		}
		this.changeState(this);
	}

	boolean bFlag;

	public void Paint() {

	}

	public void Update() {
		MainIndexScreen mainIndexScreen = new MainIndexScreen();
		this.changeState(mainIndexScreen);
	}

	public void Release() {
		// TODO Auto-generated method stub

	}

	public void stop() {

	}

	/**
	 * 是否画百叶窗
	 */
	public boolean isDrawJalousieStateBegin() {
		return false;
	}

	public boolean isDrawJalousieStateEnd() {
		return false;
	}

	public void dialogLogic(int selectOPtion) {
		// TODO Auto-generated method stub
	}

	public boolean isload() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean threadLoad() {
		// TODO Auto-generated method stub
		return false;
	}

	public int loadIndex() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void fristInit() {
		// TODO Auto-generated method stub
	}

	public void Init(int loadCount) {

	}

	/**
	 * 获得自己的资源文件
	 * 
	 * @param app
	 */
	public void getMyResURL(MIDlet app) {
		Config.resURL = app.getAppProperty("ImageURL");
		if (Config.resURL != null && !Config.resURL.startsWith("http://")) {
			Config.resURL = app.getAppProperty("serverIp") + "/"
					+ app.getAppProperty("serverProject") + "/" + Config.resURL;
		}
	}
}
