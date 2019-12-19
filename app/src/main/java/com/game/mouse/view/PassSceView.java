package com.game.mouse.view;

import java.util.Vector;

import javax.microedition.lcdui.Graphics;

import com.game.lib9.Engine;
import com.game.lib9.L9Config;
import com.game.lib9.L9Object;
import com.game.mouse.modle.Pass;
import com.game.mouse.modle.ScenePass;
import com.game.mouse.modle.service.UserDataManageService;
import com.game.mouse.screen.GamePageScreen;
import com.game.mouse.screen.HomePageScreen;
import com.game.mouse.screen.MainMidlet;
import com.game.mouse.screen.PassPageScreen;
import com.model.control.MyImg;
import com.model.tool.PubToolKit;

public class PassSceView implements View {
	private PassPageScreen passPageScreen;

	private ScenePass scenePass;

	private int selPassCode;

	private int x, initX;

	private MyImg sce;

	private MyImg scetitle;

	private int sceTitleW, sceTitleH;

	private boolean ismove;

	private boolean isMoveLeft;

	/**
	 * 是否当前的场景
	 */
	private boolean isPaintSce;

	private int speed = 5;

	private MyImg door;

	private MyImg lock;

	private MyImg star;

	private MyImg num;

	private L9Object iconSelObj;

	public PassSceView(PassPageScreen passPageScreen, int selSceCode,
			int selPassCode, boolean isPaintSce, int initX) {
		this.passPageScreen = passPageScreen;
		this.scenePass = scenePass;
		this.selPassCode = selPassCode;
		this.isPaintSce = isPaintSce;
		this.x = this.initX = initX;
		this.scenePass = UserDataManageService.getInsatnce()
				.getSceneAllPassBySceneCode(selSceCode);
	}

	public void init() {
		// TODO Auto-generated method stub
		sce = new MyImg("passpage/sce" + scenePass.getCode() + ".png");
		scetitle = new MyImg("passpage/scetitle.png");
		sceTitleW = scetitle.getWidth() / 4;
		sceTitleH = scetitle.getHeight();
		this.door = new MyImg("passpage/door.png");
		this.lock = new MyImg("passpage/lock.png");
		this.num = new MyImg("passpage/num.png");
		iconSelObj = new L9Object(null, 2, "passpage/iconsel.png", 102, 130,
				this.x - L9Config.SCR_W / 2 + 50 + 57 + (105 + 125) * selPassCode, 475 + 165, -1,
				false, false, true, 2);
		iconSelObj.setNextFrameTime(3);
		this.star = new MyImg("passpage/star.png");
	}

	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		this.paintSce(g);
		this.paintPassBut(g);
		if (!ismove && isPaintSce) {
			iconSelObj.paintFrame(g);
		}
	}

	/**
	 * 画场景
	 * 
	 * @param g
	 */
	public void paintSce(Graphics g) {
		sce.drawImage(g, x, 185, g.VCENTER | g.HCENTER);
		if (!ismove) {
			scetitle.drawRegion(g, Integer.parseInt(this.scenePass.getCode())
					* sceTitleW, 0, sceTitleW, sceTitleH, 0, x + 240 + 300, 50, 0);
		}
	}

	/**
	 * 画关卡信息
	 * 
	 * @param
	 */
	public void paintPassBut(Graphics g) {
		Vector scepass = scenePass.getPass();
		for (int i = 0; i < scepass.size(); i++) {
			Pass p = (Pass) scepass.elementAt(i);
			door.drawImage(g, this.x - L9Config.SCR_W / 2 + 210 * i + 105, 580,
					g.VCENTER | g.HCENTER);
			if (p.getIsOpen() == 0) {
				lock.drawImage(g, this.x - L9Config.SCR_W / 2 + 210 * i + 105,
						580, g.VCENTER | g.HCENTER);
			} else {
				PubToolKit.drawNum(g, p.getCode() + 1, num.getImg(), this.x
						+ 95 - L9Config.SCR_W / 2 + 210 * i, 580, 0, 0,
						true);
			}
			int len = p.getStarNum() == 3 ? 35 : p.getStarNum() == 2 ? 42 : 50;
			for (int j = 0; j < p.getStarNum(); j++) {
//				star.drawImage(g, this.x - L9Config.SCR_W / 2 + 30 + len + 40 * j
//						+ 210 * i, 610, g.VCENTER | g.HCENTER);
			}
		}
	}

	public void update() {
		// TODO Auto-generated method stub
		if (ismove) {
			speed = speed + 10;
			if (isMoveLeft) {
				if (this.initX - (this.x - speed) <= L9Config.SCR_W) {
					this.x = this.x - speed;
				} else {
					this.x = this.initX - L9Config.SCR_W;
					ismove = false;
					if (this.x < L9Config.SCR_W && this.x > 0) {
						this.isPaintSce = true;
						System.out.println("##############");
						passPageScreen.setSelPassCode(0,this);
						this.selPassCode = 0;
					} else {
						this.isPaintSce = false;
					}
				}
			} else {
				if (this.x + speed - this.initX <= L9Config.SCR_W) {
					this.x = this.x + speed;
				} else {
					this.x = this.initX + L9Config.SCR_W;
					ismove = false;
					if (this.x < L9Config.SCR_W && this.x > 0) {
						this.isPaintSce = true;
						System.out.println("!!!!!!!!!!!!!");
						passPageScreen.setSelPassCode(5,this);
						this.selPassCode = 5;
					} else {
						this.isPaintSce = false;
					}
				}
			}
		} else {
			if (isPaintSce) {
				iconSelObj.setPosX(this.x - L9Config.SCR_W / 2 + 210
						* selPassCode + 157);
				iconSelObj.doAllFrame();
			}
		}
	}

	public boolean isPaintSce() {
		return isPaintSce;
	}

	public void setMove(boolean moveLeft) {
		if (!ismove) {
			this.initX = x;
			ismove = true;
			isMoveLeft = moveLeft;
			this.speed = 5;
		}
	}

	public int getScecode() {
		return Integer.parseInt(scenePass.getCode());
	}

	public int getSelPassCode() {
		return selPassCode;
	}

	public void setSelPassCode(int selPassCode) {
		this.selPassCode = selPassCode;
	}

	public ScenePass getScenePass() {
		return scenePass;
	}

	public boolean isIsmove() {
		return ismove;
	}
}
