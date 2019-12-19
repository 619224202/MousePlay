package com.game.lib9;

import android.util.DisplayMetrics;

import java.util.Stack;
import java.util.Vector;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.midlet.MIDlet;

import com.game.mouse.context.Config;
import com.model.control.ImageManager;
import com.model.control.MSDateManager;
import com.model.mainServer.MainConfig;
import com.model.mainServer.MainServer;
import com.store.wanglu.sichuan_module.IptvPay;

/**
 * Lib9引擎的核心类，可以看成是一个状态机，程序的各个部分(每个状态即实现了L9IState接口类的实例)都被装入这个状态机得以运行，同时该类还实现了对按键系统支持，通过对按键体系的分离，使得可以在每个状态中去判断按键(与用户交互)，大大简化的程序的开发
 * 
 * @author not attributable
 * @version 1.0
 */

public class Engine extends Canvas implements Runnable {
	/**
	 * 屏幕的宽
	 */
	public int SCR_W;
	/**
	 * 屏幕的高
	 */
	public int SCR_H;

	private Vector objVector;

	// 0,1,2;
	public int drawJalousieState;

	public boolean isChange;

	private StringBuffer paintsb = new StringBuffer();

	private int loadCount = -1;

	/**
	 * 内存回收频率
	 */
	private int gcframe = 0;

	public Engine(MIDlet app) {
		// super(false);
		setFullScreenMode(true);
		SCR_W = L9Config.SCR_W;
		SCR_H = L9Config.SCR_H;
		if (L9Config.bUseDoubleBuffer) {
			imgBuffer = Image.createImage(L9Config.SCR_W, L9Config.SCR_H); // 创建缓冲图片
			FG = imgBuffer.getGraphics(); // FBackImage_G;
		} else {
			// FG = this.getGraphics();
		}
		setDisplay(app, this);

		setFps(L9Config.appFps);

		new Thread(this).start();
	}

	private int appInPaintingCout = 0, wait = 0;

	private long maxRunTime = 0, minRunTime = 0;

	public void paint(Graphics g) {
		long timeAppbeforeTmp = System.currentTimeMillis();
//		if (L9Config.bUseFakeInterrupt) { // 软中断处理
//			long dt = System.currentTimeMillis() - lastFrameTime;
//			lastFrameTime = System.currentTimeMillis();
//			if (dt > L9Config.FakeInterruptWait) {
//				wait++;
//				pauseApp();
//				resumeApp();
//			}
//		}
//		if (bAppPaused || bAppInPainting) { // 暂停或者正在绘制中
//			appInPaintingCout++;
//			return;
//		}
		bAppInPainting = true;

		if (L9Config.bUseDoubleBuffer && !bUseGlobalGraphics) { // 双缓冲
			if (imgBuffer == null) {
				return;
			}
			FG = imgBuffer.getGraphics(); // FBackImage_G;
		} else {
			FG = g;
		}
		// System.out.println("画图 "+L9Config.bUseDrawPart);
		
		long appbeforePaintTime = System.currentTimeMillis() - timeAppbeforeTmp;
		long appPaintTimeTmp = System.currentTimeMillis();
		appPaint();
		long appPaintTime = System.currentTimeMillis() - appPaintTimeTmp;
		long appBackPaintTimeTmp = System.currentTimeMillis();

		if (L9Config.bUseDoubleBuffer && !bUseGlobalGraphics) {
			g.drawImage(imgBuffer, 0, 0, 0);
		}
		if (L9Config.bShowMemory) {
			g.setColor(0xffffff);
			g.fillRect(0, 0, L9Config.SCR_W, L9Config.FONT_H);
			g.setColor(0);
			String str = "" + Runtime.getRuntime().freeMemory() + "/"
					+ Runtime.getRuntime().totalMemory();
			g.drawString(str, L9Config.SCR_W - 0, 0, g.TOP | g.RIGHT);
		}

		if (L9Config.bShowFps) {
			g.setColor(0xffffff);
			paintsb = new StringBuffer("fps:");
			paintsb.append(_fps);
			g.drawString(paintsb.toString(), 60, 180, g.TOP | g.LEFT);
		}
		if (L9Config.bShowParam) {
			String str = "屏幕:" + getWidth() + "X" + getHeight() + " 键值:"
					+ _keyCode;
			g.setColor(0xffffff);
			g.fillRect(0, 0, 160, L9Config.FONT_H);
			g.setColor(0);
			g.drawString(str, 0, 0, g.TOP | g.LEFT);
		}
		if (drawJalousieState == 1) {
			L9EngineLogic.getInstance().paintJalousie(g);
			if (L9EngineLogic.getInstance().isDrawOver()) {
				drawJalousieState = 2;
				if (!isChange) {
					L9Config.bUseDrawPart = is_bufDrawPartInit;
				}
			}
		}

		if (L9Config.bShowPaintTime) {
			g.setColor(0xffffff);
			long appBackPaintTime = System.currentTimeMillis()
					- appBackPaintTimeTmp;
			if (minRunTime > m_l_DT || minRunTime == 0) {
				minRunTime = m_l_DT;
			}

			if (maxRunTime < m_l_DT || maxRunTime == 0) {
				maxRunTime = m_l_DT;
			}
			paintsb = new StringBuffer();

			paintsb.append(",minRunTime:");
			paintsb.append(minRunTime);
			paintsb.append(",maxRunTime:");
			paintsb.append(maxRunTime);
			paintsb.append(",upRunTime:");
			paintsb.append(m_l_DT);
			paintsb.append(",m_frameRate:");
			paintsb.append(m_frameRate);
			paintsb.append(",appInPaintingCout:");
			paintsb.append(appInPaintingCout);
			paintsb.append(",wait:");
			paintsb.append(wait);
			FG.drawString(paintsb.toString(), 0, L9Config.SCR_H - 150, FG.TOP
					| FG.LEFT);
			paintsb = new StringBuffer();
			paintsb.append("pantTime:");
			paintsb.append(pantTime);
			paintsb.append(",updateTime:");
			paintsb.append(updateTime);
			paintsb.append(",appPaintTime:");
			paintsb.append(appPaintTime);
			paintsb.append(",appBeforeTime:");
			paintsb.append(appbeforePaintTime);
			paintsb.append(",appBackTime:");
			paintsb.append(appBackPaintTime);
			FG.drawString(paintsb.toString(), -20, L9Config.SCR_H - 70, FG.TOP
					| FG.LEFT);
			// FG.drawString("appBackTime:" + appBackPaintTime, -20,
			// L9Config.SCR_H - 70, FG.TOP | FG.LEFT);
		}

		frameCount++;
		bInterruptNotify = false;
		bAppInPainting = false;
	}

	// //////////////////////////////////状态机/////////////////////////////////////////////////////////////////////
	/**
	 * 程序的当前状态,就是实现了L9IState接口的对象
	 */
	public L9IState appState;
	private boolean bFirstInitState = false;
	/**
	 * 程序的主线程是否处理暂停的状态，处于中断中或者需要进入以用户交互的界面中往往需要设置为true
	 */
	public boolean bAppPaused;
	private Image imgBuffer = null;
	/**
	 * 图形对象，用来显示程序的画面
	 */
	public static Graphics FG = null;
	private boolean bAppInPainting = false;
	/**
	 * 程序对象，可以通过这个对象获取或者设置程序层面的信息
	 */
	public static MIDlet Application;
	/**
	 * 应用程序的显示容器，程序所绘制的将在该对象上显示
	 */
	public static Displayable appDisplay;
	/**
	 * 应用程序在切换显示容器时，先保存当前的显示容器，这样便于以后返回到上一个显示容器
	 */
	public static Displayable lastAppDisplay;

	private boolean is_bufDrawPartInit;
	private boolean is_bufDrawPartEnd;

	/**
	 * 设置程序和设置程序的显示容器
	 * 
	 * @param app
	 *            MIDlet
	 * @param disp
	 *            Displayable
	 */
	public void setDisplay(MIDlet app, Displayable disp) {
		if (lastAppDisplay != disp) {
			Application = app;
			appDisplay = disp;
			// 记录上一次的显示容器
			lastAppDisplay = Display.getDisplay(app).getCurrent();

			Display.getDisplay(Application).setCurrent(appDisplay);
			setFullScreenMode(true);
		}
	}

	/**
	 * 切换到上一次的显示容器，调用setDisplay设置显示容器的时候将会记录上一次显示容器，调用backLastDisplay返回上一次显示容器后会将lastAppDisplay=null
	 */
	public void backLastDisplay() {
		if (lastAppDisplay != null) {
			setDisplay(Application, lastAppDisplay);
			lastAppDisplay = null;
		}
	}

	private boolean bUseGlobalGraphics = false;

	/**
	 * 在使用双缓冲(即L9Config.bUseDoubleBuffer为true)的情况下，使用全局的Graphics
	 * 
	 * @param bUse
	 *            boolean
	 */
	public void setGlobalGraphics(boolean bUse) {
		bUseGlobalGraphics = bUse;
	}

	/**
	 * 在使用双缓冲(即L9Config.bUseDoubleBuffer为true)且使用全局的Graphics的情况下用来绘制缓存图片的内容
	 */
	public void drawBufferImage() {
		if (bUseGlobalGraphics && L9Config.bUseDoubleBuffer) {
			FG.drawImage(imgBuffer, 0, 0, 0);
		}
	}

	private L9IState stateBuf;

	/**
	 * 用来切换状态机的状态,如果state为null,将退出程序
	 * 
	 * @param state
	 *            L9IState
	 */
	public void changeState(L9IState state) {
		if (appState != null) {
			if (appState.isDrawJalousieStateEnd()) {
				drawJalousieState = 1;
				stateBuf = state;
				isChange = true;
				L9EngineLogic.getInstance().setDrawJalousie(true);
				return;
			}
		}
		goToChangeBufState(state);

	}

	/**
	 * 加载init方法
	 */
	private void initState() {
		if (loadCount == -1) {
			appState.fristInit();
			if (!appState.isload()) {
				loadCount = 105;
			} else {
				loadCount = 0;
			}
			if (appState.isload() && appState.threadLoad()) {
				this.theadLoadInitState();
			}
		} else if (appState.isload() && !appState.threadLoad()) {
			loadInitState();
		}
	}

	/**
	 * 每帧加载state的init方法
	 */
	private void loadInitState() {
		appState.Init(loadCount);
		loadCount += 5;
	}

	/**
	 * 线程加载state的init方法
	 */
	private void theadLoadInitState() {
		new Thread() {
			public void run() {
				while (true) {
					appState.Init(loadCount);
					loadCount += 5;
					if (loadCount > 100) {
						break;
					}
				}
			}
		}.start();
	}

	private void updateState() {
		if (appState != null) {
			if (loadCount <= 100) {
				initState();
			} else {
				appState.Update();
			}
		}
		if (drawJalousieState != 1) {
			if (isChange) {
				goToChangeBufState(stateBuf);
				isChange = false;
			} else {
			}
		} else {
			return;
		}

	}

	private void goToChangeBufState(L9IState stateBuf) {
		if (stateBuf == null) {
			quitApp();
			return;
		}
		if (appState != null) {
			appState.Release();
			appState = null;
			L9EngineLogic.getInstance().doRemoveAllObjects();
			MSDateManager.getInstance().clearAll();
			ImageManager.getInstance().clearImgs();
			System.gc();
		}
		this.isLoadRelease = false;
		LoadPage.getInstance().init();
		appState = stateBuf;
		resetKey(); // 状态改变时应该清除按键
		loadCount = -1;
		if (appState.isDrawJalousieStateBegin()) {
			is_bufDrawPartInit = L9Config.bUseDrawPart;
			// System.out.println("改变前"+is_bufDrawPartInit);
			L9Config.bUseDrawPart = false;
			// System.out.println("改变后"+is_bufDrawPartInit);
			isChange = false;
			drawJalousieState = 1;
			L9EngineLogic.getInstance().setDrawJalousie(false);
			// return;
		}
		// 表示已经进入到具体的状态了
		if (!bFirstInitState) {
			bFirstInitState = true;
		}
	}

	/**
	 * 必须实现的抽象类方法，用来执行应用的程序逻辑
	 */
	public void appUpdate() {
		if (appState != null) {
			// appState.Update();
			updateState();
		}
	}

	private boolean isLoadRelease = false;

	/**
	 * 必须实现的抽象类方法，绘制应用的画面
	 */
	public void appPaint() {
		if (appState != null) {
			if (loadCount > 100) {
				appState.Paint();
				if (!isLoadRelease) {
					isLoadRelease = true;
					LoadPage.getInstance().Release();
				}
			} else if (appState.isload() && loadCount <= 100) {
				LoadPage.getInstance().paint(FG, loadCount < 0 ? 0 : loadCount,
						appState.loadIndex());
			}
		}
	}

	// ////////////////////////State
	// Manager//////////////////////////////////////////////
	private Stack sState = new Stack();

	/**
	 * 将状态机的当前状态入栈
	 */
	public void pushState() {
		pushState(appState);
	}

	/**
	 * 将状态state入栈
	 * 
	 * @param state
	 *            L9IState
	 */
	public void pushState(L9IState state) {
		sState.push(state);
	}

	/**
	 * 将状态出栈，返回出栈的状态,如果堆栈为空将返回null
	 * 
	 * @return L9IState
	 */
	public L9IState popState() {
		L9IState state = null;
		if (sState.size() > 0) {
			state = (L9IState) sState.pop();
		}
		return state;
	}

	/**
	 * 返回出栈的状态,但是不出栈,如果堆栈为空将返回null
	 * 
	 * @return L9IState
	 */
	public L9IState topState() {
		return (L9IState) (sState.peek());
	}

	/**
	 * 返回状态堆栈的大小
	 * 
	 * @return int
	 */
	public int getStateSize() {
		return sState.size();
	}

	/**
	 * 清空状态机堆栈
	 */
	public void clearState() {
		sState.removeAllElements();
	}

	/**
	 * 程序暂停
	 */
	public void pauseApp() {
//		bAppPaused = true;
	}

	/**
	 * 继续运行程序
	 */
	public void resumeApp() {
		if (bAppPaused) {
			bAppPaused = false;
			setDisplay(Application, this);
			bInterruptNotify = true;
			repaint();
			resetKey();
		}
	}

	/**
	 * 退出程序
	 */
	public void quitApp() {
//		MainServer.getInstance().endGameTime();
		appState = null;
		IptvPay.getInstance().onDestory();
		Application.notifyDestroyed();
	}

	// ///////////////////////////////////////按键系统//////////////////////////////////////////////////////////
	// /////////////////logic key value////////////////////////////////////////
	public final static int K_KEY_INVALID = -1;
	public final static int K_KEY_NUM0 = (1 << 0);
	public final static int K_KEY_NUM1 = (1 << 1);
	public final static int K_KEY_NUM2 = (1 << 2);
	public final static int K_KEY_NUM3 = (1 << 3);
	public final static int K_KEY_NUM4 = (1 << 4);
	public final static int K_KEY_NUM5 = (1 << 5);
	public final static int K_KEY_NUM6 = (1 << 6);
	public final static int K_KEY_NUM7 = (1 << 7);
	public final static int K_KEY_NUM8 = (1 << 8);
	public final static int K_KEY_NUM9 = (1 << 9);
	public final static int K_KEY_UP = (1 << 10);
	public final static int K_KEY_DOWN = (1 << 11);
	public final static int K_KEY_LEFT = (1 << 12);
	public final static int K_KEY_RIGHT = (1 << 13);
	public final static int K_KEY_FIRE = (1 << 14);
	public final static int K_KEY_SOFT_L = (1 << 15);
	public final static int K_KEY_SOFT_R = (1 << 16);
	public final static int K_KEY_STAR = (1 << 17);
	public final static int K_KEY_POUND = (1 << 18);
	public final static int K_KEY_SEND = (1 << 19);
	public final static int K_KEY_END = (1 << 20);
	public final static int K_KEY_BACK = (1 << 21);
	public final static int K_KEY_BACK2 = (1 << 18);
	public final static int nKEY_NUM = 22;
	// / Previous frame keys pressed.
	private static int m_keys_pressed;
	// / Previous frame keys released.
	private static int m_keys_released;
	// / Previous frame keys state.
	private static int m_keys_state;
	// / Current keys state.
	private static int m_current_keys_state;
	// / Current keys pressed.
	private static int m_current_keys_pressed;
	// / Current keys released.
	private static int m_current_keys_released;

	private int m_current_PointerX;
	private int m_current_PointerY;

	private int PointerX = -1;
	private int PointerY = -1;
	private int DragPointerX = -1;
	private int DragPointerY = -1;

	/**
	 * 调用该函数用来模拟按键效果，使用传统按键系统的游戏移动到触摸屏中为了减少工作量常常需要模拟按键效果
	 * 
	 * @param keyCode
	 *            int
	 */
	public void logicKeyPressed(int keyCode) {
		int keyFlag = getLogicKeyByKeyCode(keyCode);

		m_current_keys_pressed = keyFlag;
		// System.out.println("aaa"+m_current_keys_state);
		m_current_keys_state = keyFlag;

		m_keys_state = 0;
	}

	private static int actionCode;

	protected void keyPressed(int keyCode) {
		if (drawJalousieState != 1) {
			// actionCode = getGameAction(keyCode);
			logicKeyPressed(keyCode);
		}

		// System.out.println(getKeyCode() == K_KEY_RIGHT);
	}

	protected void keyReleased(int keyCode) {
		// actionCode = getGameAction(keyCode);
		int keyFlag = getLogicKeyByKeyCode(keyCode);

		m_current_keys_released = keyFlag;
		m_current_keys_state = keyFlag;
		m_keys_state = 0;
	}

	protected void keyRepeated(int keyCode) {
		// keyPressed(keyCode); //进入输入文字界面后返回，会出现问题
	}

	/**
	 * 在帧驱动的应用或者游戏(帧驱动的方式往往是游戏)中每一帧都要清除按键值，isKeyPressed,isKeyReleased将会受到影响,而isKeyUp,isKeyDown则需要手动
	 * 调用
	 */
	public void updateKey() {

		m_keys_pressed = m_current_keys_pressed;
		m_keys_released = m_current_keys_released;
		m_keys_state = m_current_keys_state;
		m_current_keys_pressed = 0;
		m_current_keys_released = 0;

		PointerX = m_current_PointerX;
		PointerY = m_current_PointerY;
		m_current_PointerX = -1;
		m_current_PointerY = -1;
	}

	/**
	 * 清除按键值
	 */
	public static void resetKey() {
		m_keys_pressed = 0;
		m_keys_released = 0;
		m_keys_state = 0;
		m_current_keys_state = 0;
		m_current_keys_pressed = 0;
		m_current_keys_released = 0;
	}

	/**
	 * 判断某个键是否已经被释放了，注意参数是逻辑键值，这个函数与isKeyReleased的区别在于在应用程序的每一帧isKeyPressed对应的键值都会被清除，
	 * 而isKeyDown则必须使用ResetKey或者ResetAKey进行清除
	 * 
	 * @param logicKey
	 *            int
	 * @return boolean
	 */
	public boolean isKeyDown(int logicKey) {
		return (m_keys_state & (1 << logicKey)) != 0;
	}

	/**
	 * 判断某个键是否已经被按下了，注意参数是逻辑键值，这个函数与isKeyPressed的区别在于在应用程序的每一帧isKeyPressed对应的键值都会被清除，
	 * 而isKeyUp则必须使用ResetKey或者ResetAKey进行清除
	 * 
	 * @param logicKey
	 *            int
	 * @return boolean
	 */
	public boolean isKeyUp(int logicKey) {
		return (m_keys_state & (1 << logicKey)) == 0;
	}

	/**
	 * 判断是否按下了键
	 * 
	 * @return boolean
	 */
	public boolean isAnyKeyPressed() {
		for (int i = 0; i < nKEY_NUM; i++) {
			if ((m_keys_pressed & (1 << i)) != 0) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断是否有键被释放了
	 * 
	 * @return int
	 */
	public boolean isAnyKeyReleased() {
		for (int i = 0; i < nKEY_NUM; i++) {
			if ((m_keys_released & (1 << i)) != 0) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断某个键是否已经被按下了，注意参数是逻辑键值
	 * 
	 * @param logicKey
	 *            int
	 * @return boolean
	 */
	public static boolean isKeyPressed(int logicKey) {
			return ((m_keys_pressed & logicKey) != 0);
	}

	/**
	 * 判断某个键是否被释放了，注意参数是逻辑键值
	 * 
	 * @param logicKey
	 *            int
	 * @return boolean
	 */
	public boolean isKeyReleased(int logicKey) {
	    return (m_keys_released & logicKey) != 0;
	}

	private static int _keyCode = -1;

	/**
	 * 根据键值返回键的逻辑值
	 * 
	 * @param keyCode
	 *            int
	 * @return int
	 */
	public static int getLogicKeyByKeyCode(int keyCode) {
		_keyCode = keyCode; //用来检查按键
		System.out.println(" ---- keyCode = " + keyCode + " ----- ");
		switch(keyCode){
			case -7:
			case -31:
			case -32:
			case -6:
			case -9:
			case -8:
			case 0:
				return K_KEY_BACK;
//	       case 0:								//遥控上的返回
//	      	 return K_KEY_BACK;
			case 1:								//遥控上的向上键
			case 11:								//键盘上的C
				return K_KEY_UP;
			case 6:								//遥控上的向下键
			case 10:								//键盘上的B
				return K_KEY_DOWN;
			case 9:							//键盘上的A键
			case 2:							//遥控上的向左键
				return K_KEY_LEFT;
			case 12:							//键盘上的额D键
			case 5:							//遥控上的向右键
				return K_KEY_RIGHT;
			case 8:							//键盘上的回车键 && 遥控上的确定键
				return K_KEY_FIRE;
		}
		if ((keyCode >= Canvas.KEY_NUM0) && (keyCode <= Canvas.KEY_NUM9)) {
			return (1 << (keyCode - Canvas.KEY_NUM0));
		}
		return -1;
	}

	/**
	 * 根据逻辑键返回按键值
	 * 
	 * @param logicKey
	 *            int
	 * @return int
	 */
	public static int getKeyCodeByLogicKey(int logicKey) {
		if ((logicKey & K_KEY_NUM0) != 0) {
			return Canvas.KEY_NUM0;
		}
		if ((logicKey & K_KEY_NUM1) != 0) {
			return Canvas.KEY_NUM1;
		}
		if ((logicKey & K_KEY_NUM2) != 0) {
			return Canvas.KEY_NUM2;
		}
		if ((logicKey & K_KEY_NUM3) != 0) {
			return Canvas.KEY_NUM3;
		}
		if ((logicKey & K_KEY_NUM4) != 0) {
			return Canvas.KEY_NUM4;
		}
		if ((logicKey & K_KEY_NUM5) != 0) {
			return Canvas.KEY_NUM5;
		}
		if ((logicKey & K_KEY_NUM6) != 0) {
			return Canvas.KEY_NUM6;
		}
		if ((logicKey & K_KEY_NUM7) != 0) {
			return Canvas.KEY_NUM7;
		}
		if ((logicKey & K_KEY_NUM8) != 0) {
			return Canvas.KEY_NUM8;
		}
		if ((logicKey & K_KEY_NUM9) != 0) {
			return Canvas.KEY_NUM9;
		}
		if ((logicKey & K_KEY_STAR) != 0) {
			return Canvas.KEY_STAR;
		}
		if ((logicKey & K_KEY_POUND) != 0) {
			return Canvas.KEY_POUND;
		}
		if ((logicKey & K_KEY_UP) != 0) {
			return L9Config.PHONE_UP;
		}
		if ((logicKey & K_KEY_DOWN) != 0) {
			return L9Config.PHONE_DOWN;
		}
		if ((logicKey & K_KEY_LEFT) != 0) {
			return L9Config.PHONE_LEFT;
		}
		if ((logicKey & K_KEY_RIGHT) != 0) {
			return L9Config.PHONE_RIGHT;
		}
		if ((logicKey & K_KEY_FIRE) != 0) {
			return L9Config.PHONE_FIRE;
		}
		if ((logicKey & K_KEY_SOFT_L) != 0) {
			return L9Config.PHONE_SOFT_L;
		}
		if ((logicKey & K_KEY_SOFT_R) != 0) {
			return L9Config.PHONE_SOFT_R;
		}
		if ((logicKey & K_KEY_BACK) != 0) {
			return -31;
		}
		return 0;
	}

	/**
	 * 帧计数,每一帧都回增加1,在以帧驱动的应用(常常是游戏)中非常有用
	 */
	public int frameCount = 0;
	private long lastFrameTime;
	private boolean bInterruptNotify;
	private long m_lBeginTime;
	private long m_l_DT;
	private long m_frameRate;
	private long _fps = 15;

	/**
	 * 设置程序的fps,就是每秒钟显示的帧数
	 * 
	 * @param fps
	 *            int
	 */
	public void setFps(int fps) {
		_fps = fps;
		m_frameRate = ((long) 1000) / _fps;
	}

	private long updateTime = 0;

	private long pantTime = 0;

	public void run() {
		// Change_State(State_Main_Menu); //change state
		while (appState != null || !bFirstInitState) {
			if (!bAppPaused) {
				m_lBeginTime = System.currentTimeMillis();
				try {
					frameCount++;
					updateKey();
					appUpdate();
					updateTime = System.currentTimeMillis() - m_lBeginTime;
					long pantTimeTmp = System.currentTimeMillis();
					repaint(0, 0, L9Config.SCR_W, L9Config.SCR_H);
					serviceRepaints();
					pantTime = System.currentTimeMillis() - pantTimeTmp;
					m_l_DT = System.currentTimeMillis() - m_lBeginTime;
					// while (m_l_DT < m_frameRate) { //FRAME_RATE){
					// // Thread.sleep(50);
					// Thread.yield();
					// m_l_DT = System.currentTimeMillis() - m_lBeginTime;
					// }

					if (m_l_DT < m_frameRate) { // FRAME_RATE){
						Thread.sleep(m_frameRate - m_l_DT);
						// Thread.yield();
						m_l_DT = System.currentTimeMillis() - m_lBeginTime;
					}
					// if (L9Config.bShowFps) {
					if (m_l_DT <= 0) {
						m_l_DT = 1;
					}
					_fps = ((long) 1000) / m_l_DT;
					// }
					// System.out.println("m_frameRate="+m_frameRate);
					if (L9Config.isgc) {
						if (gcframe >= L9Config.gcframe) {
							System.gc();
						} else {
							gcframe++;
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			// System.out.println("list_elements_index="+List_Elements_Index);
		} // while App_State
	}

	/**
	 * 判断某点是否在某个线段之间
	 * 
	 * @param pX
	 *            int
	 * @param leftX
	 *            int
	 * @param rightX
	 *            int
	 * @return boolean
	 */
	public boolean isInLine(int pX, int leftX, int rightX) {
		return pX >= leftX && pX < rightX;
	}

	/**
	 * 判断某个点是否在矩形中，参数rect表示矩形的左上角和右下角两点坐标
	 * 
	 * @param pX
	 *            int
	 * @param pY
	 *            int
	 * @param rect
	 *            int[]
	 * @return boolean
	 */
	public boolean isInRect(int pX, int pY, L9Rect rect) {
		return isInLine(pX, rect.Left, rect.Right)
				&& isInLine(pY, rect.Top, rect.Bottom);
	}

	// /////////////////////////中断相关////////////////////////////////////////////////
	protected void hideNotify() {
		pauseApp();
	}

	protected void showNotify() {
		resumeApp();
	}

	protected void pointerPressed(int x, int y) {
		m_current_PointerX = x;
		m_current_PointerY = y;
	}

	protected void pointerReleased(int x, int y) {
		DragPointerX = -1;
		DragPointerY = -1;
	}

	protected void pointerDragged(int x, int y) {
		DragPointerX = x;
		DragPointerY = y;
	}

	/**
	 * 判断是否处于拖动状态
	 * 
	 * @return boolean
	 */
	public boolean isDragPointer() {
		return DragPointerX != -1 && DragPointerY != -1;
	}

	/**
	 * 返回触摸点的X坐标
	 * 
	 * @return int
	 */
	public int getPointerX() {
		return PointerX;
	}

	/**
	 * 返回触摸点的Y坐标
	 * 
	 * @return int
	 */
	public int getPointerY() {
		return PointerY;
	}

	/**
	 * 在处于拖动状态的时候返回当前点的X坐标
	 * 
	 * @return int
	 */
	public int getDragPointerX() {
		return DragPointerX;
	}

	/**
	 * 在处于拖动状态的时候返回当前点的Y坐标
	 * 
	 * @return int
	 */
	public int getDragPointerY() {
		return DragPointerY;
	}

	// ////////////////////////////////////////////消息对话框/////////////////////////////////////////////////////////////
	/**
	 * 显示消息对话框，默认居中显示，如果是触摸屏的话，可支持拖动
	 * 
	 * @param sTitle
	 *            String
	 * @param strMsg
	 *            String
	 * @param btnText
	 *            String
	 * @param Dialog_W
	 *            int
	 */
	public void showMsgDialog(String sTitle, String strMsg, String btnText,
			int Dialog_W) {
		pushState();
		setGlobalGraphics(true);
		L9DialogMsg dialogMsg = new L9DialogMsg(this);
		dialogMsg.setMsgDialog(sTitle, strMsg, btnText, Dialog_W);
		changeState(dialogMsg);
	}

	/**
	 * 显示确认对话框，默认居中显示，如果是触摸屏的话，可支持拖动
	 * 
	 * @param sTitle
	 *            String
	 * @param strMsg
	 *            String
	 * @param sYes
	 *            String
	 * @param sNo
	 *            String
	 * @param Dialog_W
	 *            int
	 */
	public void showYesNoDialog(String sTitle, String strMsg, String sYes,
			String sNo, int Dialog_W) {
		pushState();
		setGlobalGraphics(true);

		L9DialogYesNo dialogYesNo = new L9DialogYesNo(this);
		dialogYesNo.setYesNoDialog(sTitle, strMsg, sYes, sNo, Dialog_W);
		changeState(dialogYesNo);
	}

	/**
	 * 清屏操作，用color颜色值来填充整个屏幕
	 * 
	 * @param color
	 *            int
	 */
	public void fillScreen(int color) {
		FG.setColor(color);
		FG.fillRect(0, 0, SCR_W, SCR_H);
	}

	/**
	 * 获取屏幕分辨率
	 */
	public static int[] initMix(){
		int[] px = new int[2];
		DisplayMetrics dsDisplayMetrics = new DisplayMetrics();
		Application.getWindowManager().getDefaultDisplay().getMetrics(dsDisplayMetrics);
		px[0] = dsDisplayMetrics.widthPixels;
		px[1] = dsDisplayMetrics.heightPixels;
		return px;
	}
	/**
	 * 获得按键键值
	 */
	public static int getKeyCode() {
		if(MainConfig.Area == MainConfig.Type_SC){
			return m_keys_pressed;
		}else {
			return m_keys_released;
		}
	}

	public static int getKeyRepeatedKey() {
		return m_keys_state;
	}

	public Vector getObjVector() {
		return objVector;
	}

	public void setObjVector(Vector objVector) {
		this.objVector = objVector;
	}

	public long get_fps() {
		return _fps;
	}
}
