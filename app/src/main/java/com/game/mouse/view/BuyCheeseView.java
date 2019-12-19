package com.game.mouse.view;

import javax.microedition.lcdui.Graphics;

import com.game.lib9.Engine;
import com.game.lib9.L9Config;
import com.game.mouse.gameinfo.GameInfo;
import com.game.mouse.gameinfo.Props;
import com.game.mouse.modle.service.UserDataManageService;
import com.model.control.MyImg;
import com.model.mainServer.MainServer;
import com.model.tool.PubToolKit;

import java.lang.reflect.Array;

public abstract class BuyCheeseView implements View {
	private int x, y;

	private MyImg buycheesebg;

	private MyImg butbuy;

	private MyImg num;

	private MyImg num00;

	/**
	 * 单位
	 */
	private MyImg unit;

	private MyImg buycheesebut;

	private int butBgW, butBgH;

	private MyImg speedCheeseName;

	private MyImg speedCheeseImg;

	private MyImg unCheeseName;

	private MyImg unCheeseImg;

	public static int selButIndex;

	/**
	 * 道具code、扣费code、价格、购买数量、道具名、对应数量的code
	 */
	protected String[][] props;

	public BuyCheeseView() {
		this.x = L9Config.SCR_W / 2;
		this.y = L9Config.SCR_H / 2;
	}

	public abstract String[][] getprops();

	//存的是购买奶酪的数量
	public void getPropMess() {
		this.props = this.getprops();
		for (int i = 0; i < props.length; i++) {
			String[] prop = Props.getIntance().getPricePropByCode(props[i][0]);
			if (prop != null) {
				props[i][1] = prop[1];
				props[i][2] = prop[2];
				props[i][3] = prop[3];
				props[i][4] = props[i][4];
				props[i][6] = props[i][6] + "X" + prop[3];
				System.out.println("----- props[" + i + "][2] = " + props[i][2] + "------");
			}
		}
	}

	public void init() {
		// TODO Auto-generated method stub
		buycheesebg = GameInfo.getBuyCHeeseBgImg();
		buycheesebut = new MyImg("gamepage/endgame/buycheesebut.png");
		butBgW = buycheesebut.getWidth();
		butBgH = buycheesebut.getHeight() / 2;
		num = new MyImg("shoppage/num.png");
		num00 = new MyImg("shoppage/num00.png");
		butbuy = new MyImg("shoppage/butbuy.png");
		speedCheeseName = new MyImg("shoppage/prop/1name.png");
		speedCheeseImg = new MyImg("shoppage/prop/1.png");
		unCheeseName = new MyImg("shoppage/prop/0name.png");
		unCheeseImg = new MyImg("shoppage/prop/0.png");
		this.getPropMess();
		this.createunit();
	}

	/**
	 * 实例化单位
	 */
	public void createunit() {
		String priceName = MainServer.getInstance().getPriceName();
		System.out.println(" ---- priceName = " + priceName + " ---- ");
		if ("元".equals(priceName)) {
			unit = new MyImg("shoppage/unity.png");
		} else if ("元宝".equals(priceName)) {
			unit = new MyImg("shoppage/unityb.png");
		} else if ("TV豆".equals(priceName)) {
			unit = new MyImg("shoppage/unittvd.png");
		} else if ("TV币".equals(priceName)) {
			unit = new MyImg("shoppage/unittvb.png");
		} else if ("代币".equals(priceName)) {
			unit = new MyImg("shoppage/unitdb.png");
		} else if ("i豆".equals(priceName)) {
			unit = new MyImg("shoppage/unitid.png");
		} else if("游戏币".equals(priceName)){
			unit = new MyImg("shoppage/unityxb.png");
		}else if("云币".equals(priceName)){
			unit = new MyImg("shoppage/unitsby.png");
		}
	}

	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		buycheesebg.drawImage(g, L9Config.SCR_W / 2, L9Config.SCR_H / 2,
				g.VCENTER | g.HCENTER);
		for (int i = 0; i < 3; i++) {
			boolean issel = (i == 0 && selButIndex == 0 || i == 1
					&& selButIndex > 0 && selButIndex < props.length - 1 || i == 2
					&& selButIndex == props.length - 1);
			int indexProp = i
					+ (selButIndex == props.length - 1 ? props.length - 3
							: selButIndex > 0 ? selButIndex - 1 : 0);
			buycheesebut.drawRegion(g, 0, issel ? butBgH : 0, butBgW, butBgH,
					0, this.x, this.y + 130 * (i - 1) + 70, g.VCENTER | g.HCENTER);
			if (props[indexProp][0].indexOf("speedcheese") >= 0) {
				speedCheeseImg.drawImage(g, x - 220,
						this.y - 40 + 130 * (i - 1) - 80 + 70, 0);
				speedCheeseName.drawImage(g, x - 50 - 20,
						this.y - 25 + 130 * (i - 1) - 20 + 70, 0);
			} else {
				unCheeseImg
						.drawImage(g, x - 170 - 50, this.y - 40 + 130 * (i - 1) - 80 + 70, 0);
				unCheeseName
						.drawImage(g, x - 50 - 20, this.y - 25 + 130 * (i - 1) - 20 + 70, 0);
			}
			String buyNumStr = Integer.parseInt(props[indexProp][3]) < 10 ? "X0" + props[indexProp][3] : "X" + props[indexProp][3];
			PubToolKit.drawString(g, this.num.getImg(), buyNumStr, "X0123456789", this.x - 95 - 130 + 50, this.y + 15 + 130 * (i - 1) - 140 + 70 + 100, 27,
					31, 0, 0, 2, -1);
			PubToolKit.drawString(g, this.num00.getImg(), props[indexProp][2], ".0123456789", this.x - (props[indexProp][2].length() > 3 ? 68 : 60),
					this.y + 12 + 130 * (i - 1) - 100 + 70, 14, 20, 0, 0,
					props[indexProp][2].length(), 0x000000);
			unit.drawImage(g, this.x - 8, this.y + 10 + 130 * (i - 1) - 100 + 70, 0);
			butbuy.drawImage(g, this.x + 30 + 50, this.y - 20 + 130 * (i - 1) - 80 + 70, 0);
		}
	}

	public void update() {
		// TODO Auto-generated method stub
		keyPress();
	}

	public void keyPress() {
		int keyCode = Engine.getKeyCode();
		switch (keyCode) {
		case Engine.K_KEY_LEFT:
			break;
		case Engine.K_KEY_UP:
			selButIndex = selButIndex > 0 ? selButIndex - 1 : 0;
			break;
		case Engine.K_KEY_DOWN:
			selButIndex = selButIndex < props.length - 1 ? selButIndex + 1
					: props.length - 1;
			break;
		case Engine.K_KEY_RIGHT:
			break;
		case Engine.K_KEY_FIRE:
			keyfire();
			break;
		case Engine.K_KEY_BACK:
//		case Engine.K_KEY_NUM0:
			keynum0();
			break;
		}
	}

	public void keyfire() {
		this.toBuyCheese();
		UserDataManageService.getInsatnce().mySaveGameData();
	}

	public void keynum0() {
		this.close();
	}

	public String[] getBuyIngProp() {
		return props[selButIndex];
	}

	public abstract void toBuyCheese();

	public abstract void close();
}
