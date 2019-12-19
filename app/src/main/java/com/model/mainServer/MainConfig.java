package com.model.mainServer;

import com.game.mouse.context.Config;

import javax.microedition.lcdui.Font;

public class MainConfig {
	/**
	 * 地区类别 regionType
	 */
	public static int regionType;
	public final static int Type_ZJDX = 1;
	public final static int Type_JSYX = 2;
	public final static int Type_JSDX = 3;
	public final static int Type_9C = 4; // 9城
	public final static int Type_XMGD = 5; // 炫佳
	public final static int Type_WR = 6; // 威瑞
	public final static int Type_AHDX = 7; // 安徽电信
	public final static int Type_SXDX = 8; // 陕西电信
	public final static int Type_HNDX = 9; // 湖南电信
	public final static int Type_9CFJ = 10; // 九城福建
	public final static int Type_9CSH = 11; // 九城上海
	public final static int Type_HSSW = 12; // 省网华数
	public final static int Type_TJLT = 13; // 天津
	public final static int Type_GDDX = 14;// 广东电信
	public final static int Type_SHDFYX = 15;// 上海东方有线
	public final static int Type_ZSGD = 16;// 中山广电
	public final static int Type_TSJSDX = 17;// 泰山江苏电信
	public final static int Type_CQGD = 18;
	public final static int Type_SHDX = 19;
	public final static int Type_9CSC = 20;
	public final static int Type_9CHB = 21; // 九城上海
	public final static int Type_9CSXYX = 21; // 九城上海

	//下面是我写的代码
	public static int Type_SBY = 22;	//视博云
	public static int Type_HN = 23;		//湖南
	public static int Type_BY = 24;		//包月
	public static int Type_HB = 25;		//湖北
	public static int Type_SC = 26;		//四川

	public static int Area = Type_SC;

	/**
	 * 购买返回值的处理
	 * 
	 * @return
	 */
	public final static int BUY_SUCCESS = 3;
	public final static int BUY_FAILED = 6;
	public final static int BUY_LIMMITED = 9;
	public final static int BUY_NOTBUY = 12;
	public final static int BUY_TIMEOUT = 22;
	public final static int BUY_CONNERR = 23;
	public final static int ORDERMONTH_SUCCESS = 5;
	public final static int ORDERMONTH_FAILED = 7;
	/**
	 * 江苏电信月限额提示
	 */
	public final static int BUY_MONTHFAIL = 13;

	public final static int BUY_CHILDLOCK = 15;

	public static String gameName = "";

	public static String getCoinName() {
		
		return "游戏币";
	}

	public static Font gameFont;
	public static Font smallFont;
	public static Font bigFont;

	MainConfig(){
		if(Area == Type_SBY){
			Config.region = "sbyjf";
		}else if(Area == Type_HN){
			Config.region = "hnjf";
		}else if(Area == Type_HB){
			Config.region = "hbjf";
		}else if(Area == Type_SC){
			Config.region = "scjf";
		}else{
			Config.region = "hbjf";
		}
	}

	public static void checkFont() {
		Font[] font = new Font[3];
		font[0] = Font.getFont(0, 0, 0);
		font[1] = Font.getFont(0, 0, 16);
		font[2] = Font.getFont(0, 0, 16);
		smallFont = font[0];
		bigFont = font[2];
		int smallIndex = 0;
		int bigIndex = 2;
		for (int i = 0; i < 3; i++) {
			if (font[i].getHeight() <= smallFont.getHeight()) {
				smallFont = font[i];
				smallIndex = i;
			}
			if (font[i].getHeight() >= bigFont.getHeight()) {
				bigFont = font[i];
				bigIndex = i;
			}
		}
		for (int i = 0; i < 3; i++) {
			if (i != smallIndex && i != bigIndex) {
				gameFont = font[i];
			}
		}
	}

	public static Font gameBlodFont;
	public static Font smallBlodFont;
	public static Font bigBlodFont;

	public static void checkBlodFont() {
		Font[] font = new Font[3];
		font[0] = Font.getFont(0, Font.STYLE_BOLD, 0);
		font[1] = Font.getFont(0, Font.STYLE_BOLD, 8);
		font[2] = Font.getFont(0, Font.STYLE_BOLD, 16);
		smallBlodFont = font[0];
		bigBlodFont = font[2];
		int smallIndex = 0;
		int bigIndex = 2;
		for (int i = 0; i < 3; i++) {
			if (font[i].getHeight() <= smallBlodFont.getHeight()) {
				smallBlodFont = font[i];
				smallIndex = i;
			}
			if (font[i].getHeight() >= bigBlodFont.getHeight()) {
				bigBlodFont = font[i];
				bigIndex = i;
			}
		}
		for (int i = 0; i < 3; i++) {
			if (i != smallIndex && i != bigIndex) {
				gameBlodFont = font[i];
			}
		}
	}
}
