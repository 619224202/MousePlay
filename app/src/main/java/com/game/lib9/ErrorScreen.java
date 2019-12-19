package com.game.lib9;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

import com.model.tool.PubToolKit;

public class ErrorScreen extends L9Screen {
	/**
	 * 错误位置
	 */
	private int errorState;
	/**
	 * 错误种类
	 */
	// public final static int STBID_ERROR = 1;
	public final static int GAMEID_ERROR = 2;
	public final static int SERVER_ERROR = 3;
	public final static int SERVER_FAILED = 4;
	public final static int PROPERTY_NULL = 1;

	public Font font = Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_BOLD,
			Font.SIZE_LARGE);

	private Graphics g;

	private String str;

	/**
	 * 一个中文字节的长度
	 */
	private int varLenth;

	/**
	 * 
	 */
	public ErrorScreen(int errorState, String str) {
		this.errorState = errorState;

		varLenth = font.stringWidth("游");
		this.str = str;
	}

	public void Paint() {
		if (g == null) {
			g = Engine.FG;
		}
		int k = 250;
		g.setColor(0x000000);
		g.fillRect(0, 0, L9Config.SCR_W, L9Config.SCR_H);
		switch (errorState) {

		case PROPERTY_NULL:
			// g.setColor(0x000000);
			// g.fillRect(0, 0, L9Config.SCR_W, L9Config.SCR_H);
			// g.setColor(0xffffff);
			// g.drawString("游戏环境错误："+str, (L9Config.SCR_W-9*varLenth)/2, 250, 0);
			k = PubToolKit.drawString("游戏环境错误：.." + str, 200, 250, 300, font,
					0xffffff, g);
			break;
		case SERVER_FAILED:
			// g.setColor(0x000000);
			// g.fillRect(0, 0, L9Config.SCR_W, L9Config.SCR_H);
			// g.setColor(0xffffff);
			// g.drawString("游戏网络连接失败.."+str, (L9Config.SCR_W-9*varLenth)/2, 250, 0);

			k = PubToolKit.drawString("游戏网络连接失败.." + str, 200, 250, 300, font,
					0xffffff, g);
			break;
		default:

			break;
		}
		// g.drawString("(按0或确定键退出)",(L9Config.SCR_W-9*varLenth)/2 , 400, 0);

	}

	public void Release() {
		// TODO Auto-generated method stub

	}

	public void Update() {
		int keyCode = Engine.getKeyCode();
		if (keyCode == Engine.K_KEY_NUM0 || keyCode == Engine.K_KEY_FIRE) {
			// MainMidlet.engine.quitApp();
		}
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
}
