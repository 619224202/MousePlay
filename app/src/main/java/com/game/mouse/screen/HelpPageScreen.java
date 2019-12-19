package com.game.mouse.screen;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

import com.game.lib9.Engine;
import com.game.lib9.L9Config;
import com.game.lib9.L9EngineLogic;
import com.game.lib9.L9IState;
import com.game.lib9.L9Object;
import com.game.lib9.L9Screen;
import com.game.mouse.context.Config;
import com.game.mouse.dialog.EndGameDialog;
import com.model.base.ParserTbl;
import com.model.control.MyImg;
import com.model.tool.PubToolKit;

public class HelpPageScreen extends L9Screen {
	private Graphics g;

	private MyImg bg;

	private MyImg[] msg = new MyImg[5];

	private int selIndex;

	private L9Object iconSelObj;

	public void fristInit() {
		bg = new MyImg("helppage/bg.png");
		for (int i = 0; i < msg.length; i++) {
			msg[i] = new MyImg("helppage/" + i + ".png");
		}
		iconSelObj = new L9Object(null, 2, "helppage/selbut.png", 212, 70, 100 + 15,
				140 + 130, -1, false, false, true, 2);
		iconSelObj.setNextFrameTime(3);
	}

	public void Paint() {
		// TODO Auto-generated method stub
		if (this.g == null) {
			this.g = Engine.FG;
		}
		bg.drawImage(g, 0, 0, 0);
		msg[selIndex].drawImage(g, 210 + 50, 80 + 30, 0);
		iconSelObj.paintFrame(g);
	}

	public void Update() {
		keyPress();
		int positionY = 170 + 105 * (selIndex + 1);
		if(selIndex == 2){
			positionY -= 3;
		}else if(selIndex == 3){
			positionY -= 14;
		}else if(selIndex == 4){
			positionY -= 20;
		}
		iconSelObj.setPosY(positionY);
		iconSelObj.doAllFrame();
	}

	public void keyPress() {
		int keyCode = Engine.getKeyCode();
		switch (keyCode) {
		case Engine.K_KEY_LEFT:
			break;
		case Engine.K_KEY_UP:
			this.keyup();
			break;
		case Engine.K_KEY_DOWN:
			this.keydown();
			break;
		case Engine.K_KEY_RIGHT:
			break;
		case Engine.K_KEY_BACK:
//		case Engine.K_KEY_NUM0:
			this.keynum0();
			break;
		}
	}

	public void keyup() {
		selIndex = selIndex > 0 ? selIndex - 1 : 4;
	}

	public void keydown() {
		selIndex = selIndex < 4 ? selIndex + 1 : 0;
	}

	public void keynum0() {
		this.goGame();
	}

	public void goGame() {
		HomePageScreen homePageScreen = new HomePageScreen(4);
		MainMidlet.engine.changeState(homePageScreen);
	}

	public boolean isload() {
		// TODO Auto-generated method stub
		return false;
	}
}
