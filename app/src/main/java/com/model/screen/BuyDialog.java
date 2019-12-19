package com.model.screen;

import java.io.IOException;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import com.game.lib9.L9Config;
import com.game.mouse.screen.MainMidlet;
import com.model.mainServer.MainConfig;
import com.model.mainServer.MainServer;
import com.model.mainServer.config.StringTools;

public class BuyDialog implements DialogInterface {
	/**
	 * 图片
	 */
	private Image bgImg;
	private Image[] butImg;
	private Image strImg;
	private Image xzImg;
	public int selectOption;
	public final static int Yes_Option = 1;
	public final static int No_Option = 2;

	private int currentFrame;

	private int price;
	private int buyNum;
	private String propName;
	public static boolean isRunning;
	private MainServer mainServer;
	private String priceName;
	private int[] drawPos = new int[2];
	private int width;
	private int height;
	private Font strFont;
	private String priceStr;
	private String diaStr;
	private boolean isByCoin;
	private int strHight;
	private int coinNum;

	private String showStr;
	private boolean isShowStr;

	private int color;

	private boolean bInitOver = false;

	public BuyDialog(boolean flag, String price, String propName, int buyNum,
			MainServer mainServer, int coinNum) {

		strFont = MainConfig.gameFont;
		this.price = Integer.parseInt(price);
		this.propName = propName;
		this.buyNum = buyNum;
		this.mainServer = mainServer;
		this.coinNum = coinNum;
		isRunning = true;
		selectOption = Yes_Option;
		loadImg();
		width = bgImg.getWidth();
		height = bgImg.getHeight();
		this.drawPos[0] = (int)((L9Config.SCR_W/2 - width / 2)*(MainMidlet.scaleX));
		this.drawPos[1] = (int)((360 - height / 2)*MainMidlet.scaleY);
		color = 0x000000;
		priceName = "游戏币";
		if(MainConfig.Area == MainConfig.Type_SBY){
			priceName = "云币";
		}else if(MainConfig.Area == MainConfig.Type_HN){
			priceName = "元";
		}else if(MainConfig.Area == MainConfig.Type_HB){
			priceName = "元";
		}else if(MainConfig.Area == MainConfig.Type_SC){
			priceName = "元";
		}else{
			priceName = "游戏币";
		}
		isByCoin = false;
		strHight = strFont.getHeight() + 10;
		priceStr = price + priceName;
		if (buyNum != -1) {
			diaStr = "是否购买" + buyNum + "个" + propName;
		} else {
			diaStr = "是否" + propName;
		}
		bInitOver = true;
	}

	public void loadImg() {
		try {
			bgImg = Image.createImage("/pay/buyDia.png");
			butImg = new Image[2];
			for (int i = 0; i < 2; i++) {
				butImg[i] = Image.createImage("/pay/but" + ((i + 3)) + ".png");
			}
			xzImg = Image.createImage("/pay/xk.png");
			strImg = Image.createImage("/pay/hintStr2.png");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void mainPro() {
		if(!bInitOver){
			return;
		}
		if (currentFrame > 0) {
			currentFrame--;
		}
	}

	public void keyPressed(int keyCode) {
		if(!bInitOver){
			return;
		}
		System.out.println("this is keyCode = " + keyCode);
		switch (keyCode) {
		case -1:
			break;
		case -2:
			break;
		case -3:
			selectOption = selectOption == Yes_Option ? No_Option : Yes_Option;
			break;
		case -4:
			selectOption = selectOption == Yes_Option ? No_Option : Yes_Option;
			break;
		case -5:
			if (selectOption == No_Option) {
				isRunning = false;
			} else {
				if (currentFrame == 0) {
					isRunning = false;
				}
			}
			break;
		case -31:
		case -7:
		case -11:
			selectOption = No_Option;
			isRunning = false;
			break;
		}
	}

	public void setShowStr(String showStr) {
		isShowStr = true;
		if (showStr.indexOf("{price}") > 0)
			showStr = showStr.substring(0, showStr.indexOf("{price}")) + price
					+ showStr.substring(showStr.indexOf("{price}") + 7);
		if (showStr.indexOf("{unit}") > 0)
			showStr = showStr.substring(0, showStr.indexOf("{unit}"))
					+ priceName
					+ showStr.substring(showStr.indexOf("{unit}") + 6);
		this.showStr = showStr;
		if (mainServer.mainConfig.regionType == MainConfig.Type_HNDX
				|| mainServer.mainConfig.regionType == MainConfig.Type_AHDX) {
			String s = "(您当前还剩余" + coinNum
					+ mainServer.mainConfig.getCoinName() + ")";
			this.showStr += s;
		}
	}

	public void setColor(int color) {
		this.color = color;
	}

	public void setImg(String bgImgStr, String yesButImgStr,
			String noButImgStr, String tipImgStr, String xzImgStr) {
		try {
			bgImg = null;
			butImg[0] = null;
			butImg[1] = null;
			bgImg = Image.createImage(bgImgStr);
			butImg = new Image[2];
			butImg[0] = Image.createImage(yesButImgStr);
			butImg[1] = Image.createImage(noButImgStr);
			if (tipImgStr != null && !"".equals(tipImgStr)) {
				strImg = Image.createImage(tipImgStr);
			} else {
				strImg = null;
			}
			if (xzImgStr != null && !"".equals(xzImgStr)) {
				xzImg = Image.createImage(xzImgStr);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void paint(Graphics g) {
		if(!bInitOver){
			return;
		}

		g.translate(drawPos[0], drawPos[1]);
		g.drawImage(bgImg, -75, 0, 0);
		if (strImg != null) {
			g.drawImage(strImg, width / 2, 18, Graphics.VCENTER
					| Graphics.HCENTER);
		}
		if (selectOption == Yes_Option) {
			paintBut(g, butImg[0], 90, 290, true);
			paintBut(g, butImg[1], 250, 290, false);
		} else {
			paintBut(g, butImg[0], 90, 290, false);
			paintBut(g, butImg[1], 250, 290, true);
		}
		if (!isShowStr) {
			paintStrs(g);
		} else {
			StringTools.drawString(showStr, 10 + 100, 50, width - 20, strFont, color,
					g);
		}
		g.translate(-(drawPos[0]), -(drawPos[1]));
	}

	private void paintStrs(Graphics g) {
		if (isByCoin) {
			if (coinNum >= price) {
				StringTools.drawString(diaStr, 50, 50, width - 20, strFont,
						color, g);
				StringTools.drawString("资费" + priceStr, 50, 60 + strHight,
						width - 20, strFont, color, g);
				StringTools.drawString("您当前的" + priceName + "数量：" + coinNum
						+ priceName, 50, 70 + strHight * 2, width - 20,
						strFont, color, g);
			} else {
				StringTools.drawString(diaStr, 50, 55, width - 20, strFont,
						color, g);
				StringTools.drawString("资费" + priceStr, 50, 60 + strHight,
						width - 20, strFont, color, g);
				StringTools.drawString("您当前的" + priceName + "数量：" + coinNum
						+ priceName, 45, 65 + strHight * 2, width - 10, strFont,
						color, g);

				//省去商店购买框“游戏币不足，明天再来玩吧”
//				StringTools.drawString(priceName + "不足，明天再來玩吧!", 5 + 80,
//						70 + strHight * 3, width - 10, strFont, color, g);
			}
		} else {
			StringTools.drawString(diaStr, 20, height / 2 - strHight,
					width - 20, strFont, color, g);
			StringTools.drawString("资费" + priceStr, 20, height / 2, width - 20,
					strFont, color, g);
		}
	}

	private void paintBut(Graphics g, Image btnImg, int x, int y, boolean flag) {
		if (flag) {
			g.drawRegion(btnImg, 0, 0, btnImg.getWidth(),
					btnImg.getHeight() / 2, 0, x, y, Graphics.VCENTER
							| Graphics.HCENTER);
			StringTools.drawSelIcon(g, xzImg, btnImg.getWidth() + 10, btnImg.getHeight() / 2 + 10, x + 5, y - 20);
		} else {
			g.drawRegion(btnImg, 0, btnImg.getHeight() / 2, btnImg.getWidth(),
					btnImg.getHeight() / 2, 0, x, y, Graphics.VCENTER
							| Graphics.HCENTER);
		}
	}

	public int getSelOption() {
		// TODO Auto-generated method stub
		return selectOption;
	}

	public void release() {
		// TODO Auto-generated method stub
		bInitOver = false;
		bgImg = null;
		for (int i = 0; i < 2; i++) {
			butImg[i] = null;
		}

		xzImg = null;
		strImg = null;
	}

	public void update() {
		// TODO Auto-generated method stub
		mainPro();
	}

	public void setPos(int x, int y) {
		// TODO Auto-generated method stub
		this.drawPos[0] = x - width / 2;
		this.drawPos[1] = y - height / 2;
	}

	public boolean isRunning() {
		return isRunning;
	}
}
