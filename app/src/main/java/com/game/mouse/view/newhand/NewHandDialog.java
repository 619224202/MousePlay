package com.game.mouse.view.newhand;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

import com.game.lib9.Engine;
import com.game.lib9.L9Config;
import com.game.lib9.L9Object;
import com.game.mouse.screen.HomePageScreen;
import com.game.mouse.screen.MainMidlet;
import com.game.mouse.view.game.GameMainView;
import com.game.mouse.view.maingame.child.SpriteView;
import com.model.control.MyImg;
import com.model.tool.PubToolKit;

public class NewHandDialog implements NewHandView {

	private GameMainView gameMainView;

	private MyImg newhandbg;

	private MyImg newhandheader;

	private MyImg newhanderror;

	private MyImg newhandright;

	private MyImg newhandArrow;

	private MyImg newhandArrowYellow;

	private boolean isShow = true;

	private String[] msg = { "亲爱的小朋友，您好!我是小喵,欢迎来到猫和老鼠,下面就由我为您介绍下游戏玩法!按确定键继续!",
			"请按 “确定键” 开始游戏！", "小朋友你真聪明，看小老鼠是不是跑动了，等跑一圈在告诉你下一步吧!",
			"地图中闪烁的”叉“表示按确定键不可跳跃，”钩“可以跳跃,请按 ”确定键“！", "真棒，老鼠成功跳跃到了下方的台阶！",
			"小喵来告诉您怎么使用 ”左右键” 加减速,先来看看怎么加速的吧！",
			"老鼠朝向与遥控器 ”左右键” 同向时加速,反向时减速！请按闪烁的键！",
			"小朋友”左右键” 是来控制速度的，要记清楚了噢！按确定键继续!",
			"小老鼠需要收集地图中绿色箭头指示的3个奶酪后，钻进黄色箭头指示的洞门顺利过关！按确定键继续.",
			"小朋友，小喵就为你介绍到这里，还有好多乐趣等你发现噢！按确定键关闭" };

	private MyImg[] msgImg = new MyImg[10];

	private int[] anims = { 1, 0, -1, 1, -1, -1, -2, 1, 1, 1 };

	private int msgindex;

	private boolean isKeyGame;

	/**
	 * 是否离开原点
	 */
	private boolean isMoveInitPoint;

	private boolean isAddTime;

	private int time;

	private long startTime;

	private int maxTime = 3;

	private int mouseSpeed;

	private boolean isHavaAdd = false;

	/**
	 * 闪烁
	 */
	private int flashCount = 0;

	private Font font;

	private L9Object butObj1;

	private L9Object butObj2;

	private int mouseDirection = -1;

	public void init() {
		// TODO Auto-generated method stub
		newhandbg = new MyImg("gamepage/newhandbg.png");
		newhandheader = new MyImg("gamepage/newhandheader.png");
		newhanderror = new MyImg("gamepage/newhanderror.png");
		newhandright = new MyImg("gamepage/newhandright.png");
		newhandArrow = new MyImg("gamepage/newhandArrow.png");
		newhandArrowYellow = new MyImg("gamepage/newhandArrowYellow.png");
		butObj1 = new L9Object(null, "newhandbut", 2, 570 + 550, L9Config.SCR_H - 55,
				-1, anims[msgindex], false, false, true, 0);
		butObj1.setNextFrameTime(2);
		butObj2 = new L9Object(null, "newhandbut", 2, 480 + 500, L9Config.SCR_H - 55,
				-1, anims[msgindex], false, false, true, 0);
		butObj2.setNextFrameTime(2);
		for (int i = 0; i < msgImg.length; i++) {
			msgImg[i] = new MyImg("gamepage/newhandtalk/" + i + ".png");
		}
	}

	public NewHandDialog(GameMainView gameMainView) {
		this.gameMainView = gameMainView;
		this.init();
		font = Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_ITALIC, PubToolKit
				.getFontBig16And24Size());
	}

	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		newhandheader.drawImage(g, 0, L9Config.SCR_H - 150 - 106, 0);
		newhandbg.drawImage(g, 90 + 70 + 70, L9Config.SCR_H - 163, 0);
		this.paintMsgImg(g);
		if (msgindex == 3) {
			this.paintTipJump(g);
		}
		if (msgindex == 8) {
			paintTipArrow(g);
		}

		if (anims[msgindex] != -1) {
			butObj1.paintFrame(g);
			if (anims[msgindex] == -2) {
				butObj2.paintFrame(g);
			}
		}
	}

	public void paintMsgImg(Graphics g) {
		msgImg[msgindex].drawImage(g, 628, L9Config.SCR_H - 76,
				g.VCENTER | g.HCENTER);
	}

	public void paintMsg(Graphics g) {
		int width = 460, begX = 145;
		if (anims[msgindex] == -2) {
			width = 280;
			begX = 250;
		} else if (anims[msgindex] > -1) {
			width = 380;
		}
		PubToolKit.drawString(msg[msgindex], begX,
				L9Config.SCR_H - 100 * 2 / 3, width, font, 0x000000, g);
	}

	public void paintTipJump(Graphics g) {
		if (flashCount % 4 != 0) {
			newhanderror.drawImage(g, 280 + 285 + 20, 80 + 60 - 20, 0);
			newhandright.drawImage(g, 100 + 285 - 80, 150 + 65, 0);
		}
		flashCount++;
	}

	public void paintTipArrow(Graphics g) {
		if (flashCount % 4 != 0) {
			newhandArrow.drawImage(g, 470 + 300 + 90, 50 + 60 - 30, 0);
			newhandArrow.drawImage(g, 408 + 300 + 75, 290 + 60 + 50, 0);
			newhandArrow.drawImage(g, 130 + 300 - 25, 290 + 60 + 50, 0);
			newhandArrowYellow.drawImage(g, 550 + 300 + 125, 270 + 60 + 35, 0);
		}
		flashCount++;
	}

	/**
	 * 开始倒计时
	 */
	public void startTime() {
		this.startTime = System.currentTimeMillis();
		time = 0;
		this.isAddTime = true;
	}

	public void addTime() {
		if (System.currentTimeMillis() - this.startTime >= 1000L) {
			this.time++;
			this.startTime = System.currentTimeMillis();
		}
	}

	public void update() {
		// TODO Auto-generated method stub
		listenKeyFireAfter();
		listenJumpAfter();
		listenLeftAndRightKeyBut();
		listenKeyLeftAfter();
		listenKeyRightAfter();
		if (isAddTime) {
			this.addTime();
			if (this.time == this.maxTime) {
				isAddTime = false;
				this.mouseSpeed = this.gameMainView.getSpriteView().getSpeed();
				this.time = 0;
				if (msgindex < msg.length - 1) {
					msgindex++;
					if (anims.length - 1 > msgindex && anims[msgindex] == -1) {
						this.startTime();
					}
				}
			}
		}
		isKeyGame = false;
		if (msgindex != 6
				|| ((!isHavaAdd && this.gameMainView.getSpriteView()
						.getDirection() == 4) || (isHavaAdd && this.gameMainView
						.getSpriteView().getDirection() == 0))) {
			butObj1.doAllFrame();
		}

		if (msgindex == 6
				&& (isHavaAdd
						&& this.gameMainView.getSpriteView().getDirection() == 4 || !isHavaAdd
						&& this.gameMainView.getSpriteView().getDirection() == 0)) {
			butObj2.doAllFrame();
		}
	}

	/**
	 * 监听确定键值开始游戏后的响应
	 */
	public void listenKeyFireAfter() {
		if (msgindex == 2) {
			if (!isMoveInitPoint
					&& !this.gameMainView.getSpriteView().isInitPoint()) {
				isMoveInitPoint = true;
			} else if (isMoveInitPoint
					&& this.gameMainView.getSpriteView().isInitPoint()) {
				msgindex = 3;
			}
		}
	}

	public void listenJumpAfter() {
		if (msgindex == 3) {
			if (this.gameMainView.getSpriteView().getPointMapY()
					- this.gameMainView.getSpriteView().getInitPointMapY() > 80) {
				msgindex = 4;
				this.startTime();
			}
		}
	}

	/**
	 * 监听显示左右加速
	 */
	public void listenLeftAndRightKeyBut() {
		if (msgindex == 6) {
			int direction = gameMainView.getSpriteView().getDirection();
			if ((direction == 0 || direction == 4)
					&& mouseDirection != direction) {
				mouseDirection = direction;
				if (mouseDirection == 0) {
					butObj2.setAnimation(3);
					butObj1.setAnimation(6);
				} else {
					butObj2.setAnimation(4);
					butObj1.setAnimation(5);
				}
			}
		}
	}

	/**
	 * 监听按左键后的响应
	 */
	public void listenKeyLeftAfter() {
		if (msgindex == 6) {
			if (this.mouseSpeed != 0
					&& this.gameMainView.getSpriteView().getSpeed() > this.mouseSpeed) {
				isHavaAdd = true;
			} else if (this.mouseSpeed != 0
					&& this.gameMainView.getSpriteView().getSpeed() < this.mouseSpeed) {
				msgindex = 7;
				butObj1.setAnimation(anims[msgindex]);
			}
		}
	}

	/**
	 * 监听按右键后的响应
	 */
	public void listenKeyRightAfter() {
		if (msgindex == 6) {
			if (this.mouseSpeed != 0
					&& this.gameMainView.getSpriteView().getSpeed() > this.mouseSpeed) {
				isHavaAdd = true;
			} else if (this.mouseSpeed != 0
					&& this.gameMainView.getSpriteView().getSpeed() < this.mouseSpeed) {
				msgindex = 7;
				butObj1.setAnimation(anims[msgindex]);
			}
		}
	}

	public void showNewHand() {
		// TODO Auto-generated method stub

	}

	/**
	 * 是否在原点
	 * 
	 * @return
	 */
	public boolean isInitPoint() {
		return this.gameMainView.getSpriteView().isInitPoint();
	}

	public void keyPress() {
		int keyCode = Engine.getKeyCode();
		switch (keyCode) {
		case Engine.K_KEY_NUM7:
			keyNum7();
			break;
		case Engine.K_KEY_NUM1:
			keyNum1();
			break;
		case Engine.K_KEY_NUM2:
			keyNum2();
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

	public void keyleft() {
		if (msgindex == 6) {
			if ((!isHavaAdd && this.gameMainView.getSpriteView().getDirection() == 0)
					|| (isHavaAdd && this.gameMainView.getSpriteView()
							.getDirection() == 4)) {
				this.mouseSpeed = this.gameMainView.getSpriteView().getSpeed();
				isKeyGame = true;
			}
		}
	}

	public void keyright() {
		if (msgindex == 6) {
			if ((!isHavaAdd && this.gameMainView.getSpriteView().getDirection() == 4)
					|| (isHavaAdd && this.gameMainView.getSpriteView()
							.getDirection() == 0)) {
				this.mouseSpeed = this.gameMainView.getSpriteView().getSpeed();
				isKeyGame = true;
			}
		}
	}

	public void keyup() {

	}

	public void keydown() {

	}

	public void keyNum7() {
		this.gameMainView.removeNewHand();
	}

	public void keyNum1() {

	}

	public void keyNum2() {

	}

	public void keyNum9() {

	}

	public void keyfire() {
		switch (msgindex) {
		case 0:
			msgindex = 1;
			butObj1.setAnimation(anims[msgindex]);
			break;
		case 1:
			msgindex = 2;
			isKeyGame = true;
			break;
		case 2:
			break;
		case 3:
			isKeyGame = true;
			break;
		case 4:
			break;
		case 5:
			break;
		case 6:
			break;
		case 7:
			msgindex = 8;
			break;
		case 8:
			msgindex = 9;
			break;
		case 9:
			msgindex = 10;
			this.gameMainView.removeNewHand();
			break;
		}
	}

	/**
	 * 按键是否响应游戏
	 * 
	 * @return
	 */
	public boolean isKeyGame() {
		return isKeyGame;
	}
}
