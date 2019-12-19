package com.game.mouse.screen;

import android.view.KeyEvent;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

import com.game.gameData.GameConfig;
import com.game.lib9.Engine;
import com.game.lib9.L9Config;
import com.game.lib9.L9EngineLogic;
import com.game.lib9.L9Object;
import com.game.lib9.L9Screen;
import com.game.mouse.context.Config;
import com.game.mouse.dialog.EndGameDialog;
import com.game.mouse.gameinfo.GameInfo;
import com.game.mouse.modle.service.UserDataManageService;
import com.model.base.ParserTbl;
import com.model.control.MyImg;
import com.model.mainServer.MainServer;
import com.model.tool.PlaySoundService;
import com.model.tool.PubToolKit;

import java.sql.SQLOutput;

public class HomePageScreen extends L9Screen {
	private Graphics g;

	private MyImg bgImg;

	private MyImg logo;

	private MyImg bgbottomImg;

	private MyImg butstart;

	private MyImg butFont;

	private MyImg[] buts = new MyImg[5];

	private int butFontW, butFontH;

	private L9Object cat;

	private L9Object mouse;

	private int mouseAniCount = 0;

	private int selButIndex;

	private EndGameDialog endGameDialog;

	private int selmouseCode;

	public HomePageScreen() {

	}

	public HomePageScreen(int selIndex) {
		this.selButIndex = selIndex;
	}

	public void fristInit() {
		System.out.println("this is HomePageScreen ---- GameData = " + UserDataManageService.getInsatnce().getDataString());

		selmouseCode = Integer.parseInt(UserDataManageService.getInsatnce()
				.getOutPkUserMouse());
		ParserTbl.getInstance().defineMedia(1, "tbl/home.tbl");
		bgImg = new MyImg("homepage/bg.jpg");
		logo = new MyImg("homepage/logo.png");
		bgbottomImg = GameInfo.getBottomImg(0);
		butstart = new MyImg("homepage/butstart.png");
		butFont = new MyImg("homepage/butfont.png");
		for (int i = 0; i < 5; i++) {
			buts[i] = new MyImg("homepage/but" + i + ".png");
		}
		cat = new L9Object(null, "cat", 2, 500 + 445 + 295, 350 + 170 + 145, -1, 0, false, true, true,
				2, 0);
		cat.setNextFrameTime(4);
		mouse = new L9Object(null, "mouse0", 2, 160, 420 + 160, -1, 0,
				false, true, true, 2, 0);
		mouse.setNextFrameTime(2);
		butFontW = butFont.getWidth();
		butFontH = butFont.getHeight() / 6;
		
	}

	public void Paint() {
		// TODO Auto-generated method stub
		if (this.g == null) {
			this.g = Engine.FG;
		}
		bgImg.drawImage(g, 0, 0, 0);
		logo.drawImage(g, 195 + 200, 10, 0);
		cat.paintFrame(g);
		mouse.paintFrame(g);
		butstart.drawImage(g, 250 + 180, 352, 0);
		if (Config.isRank) {
			butFont.drawRegion(g, 0, selButIndex * butFontH, butFontW,
					butFontH, 0, 270, 362, 0);
			for (int i = 0; i < 5; i++) {
				if (selButIndex == i) {
					buts[i].drawRegion(g, buts[i].getWidth() / 2, 0, buts[i]
							.getWidth() / 2, buts[i].getHeight(), 0,
							115 + i * 700, 415, 0);
				} else {
					buts[i].drawRegion(g, 0, 0, buts[i].getWidth() / 2, buts[i]
							.getHeight(), 0, 115 + i * 70, 415, 0);
				}
			}
		} else {
			int indexSelBut = selButIndex >= 3 ? selButIndex + 1 : selButIndex;
			int butPositionY = indexSelBut * butFontH;
			if(indexSelBut == 0){
				butPositionY = butPositionY + 2;
            }else if(indexSelBut == 1){
				butPositionY = butPositionY + 13;
            }else if(indexSelBut == 2){
				butPositionY = butPositionY + 25;
			}else if(indexSelBut == 4){
				butPositionY = butPositionY - 10;
			}else if(indexSelBut == 5){
				butPositionY = butPositionY + 2;
			}
			butFont.drawRegion(g, 0, butPositionY, butFontW,
					butFontH, 0, 270 + 190, 362, 0);
			for (int i = 0; i < 5; i++) {
//				int index = i >= 3 ? i + 1 : i;
				int index = i;
				if (selButIndex == i) {
					buts[index].drawRegion(g, buts[index].getWidth() / 2, 0,
							buts[index].getWidth() / 2,
							buts[index].getHeight(), 0, 145 + i * 90 + 120, 420 + 120, 0);
				} else {
					buts[index].drawRegion(g, 0, 0, buts[index].getWidth() / 2,
							buts[i].getHeight(), 0, 145 + i * 90 + 120, 420 + 120, 0);
				}
			}
		}
		if (!"".equals(Config.version)) {
			int versionY = Config.versionpostion == 1 ? 0 : 465;
			PubToolKit.drawString(Config.version, 560, versionY, 80, Font
					.getFont(Font.FACE_MONOSPACE, Font.STYLE_BOLD, 16),
					0xffffff, g);
			g.setColor(0x000000);
			g.drawString("按返回键返回", 630 + 430, 500 + 185, Graphics.RIGHT|Graphics.TOP);
		}
		bgbottomImg
				.drawImage(g, 0, L9Config.SCR_H - bgbottomImg.getHeight(), 0);
		if (endGameDialog != null) {
			endGameDialog.paint(g);
		}
	}

	public void Release() {
		// TODO Auto-generated method stub

	}

	public void Update() {
		for (int i = 0; i < L9EngineLogic.getInstance().objects.size; i++) {
			((L9Object) L9EngineLogic.getInstance().objects.values[i])
					.doAllFrame();
		}
		L9EngineLogic.getInstance().flushRemoveList();
		L9EngineLogic.getInstance().flushAddList();
		this.changeMouseAnimation();
		if (endGameDialog != null) {
			endGameDialog.update();
			endGameDialog.keyPress();
		} else {
			keyPress();
		}
	}

	public void keyPress() {
		int keyCode = Engine.getKeyCode();
		switch (keyCode) {
		case Engine.K_KEY_LEFT:
			this.keyleft();
			break;
		case Engine.K_KEY_UP:
			break;
		case Engine.K_KEY_DOWN:
			break;
		case Engine.K_KEY_RIGHT:
			this.keyright();
			break;
		case Engine.K_KEY_FIRE:
			this.keyfire();
			break;
		case Engine.K_KEY_BACK:
//		case Engine.K_KEY_NUM0:
			this.keynum0();
			break;
		}
	}

	public void keyleft() {
		selButIndex = selButIndex > 0 ? selButIndex - 1 : (Config.isRank ? 5 : 4);
	}

	public void keyright() {
		selButIndex = selButIndex < (Config.isRank ? 5 : 4) ? selButIndex + 1 : 0;
	}

	public void changeMouseAnimation() {
		if (mouse.getCurrentFrame() >= mouse.getFrameCount() - 1) {
			mouseAniCount++;
		}
		if (mouse.getAnimation() == 0 && mouseAniCount >= 2) {
			mouse.setAnimation(1);
			mouseAniCount = 0;
		} else if (mouse.getAnimation() == 1 && mouseAniCount >= 1) {
			mouse.setAnimation(0);
			mouseAniCount = 0;
		}
	}

	public void keyfire() {
		switch (selButIndex) {
		case 0:
			PassPageScreen passPageScreen = new PassPageScreen(selmouseCode);
			MainMidlet.engine.changeState(passPageScreen);
			break;
		case 1:
			FeedPageScreen feedPageScreen = new FeedPageScreen();
			MainMidlet.engine.changeState(feedPageScreen);
			break;
		case 2:
			ShopPageScreen shopPageScreen = new ShopPageScreen();
			MainMidlet.engine.changeState(shopPageScreen);
			break;
		case 3:
			if (Config.isRank) {
				RankPageScreen rankPageScreen = new RankPageScreen();
				MainMidlet.engine.changeState(rankPageScreen);
			} else {
				HelpPageScreen helpPageScreen = new HelpPageScreen();
				MainMidlet.engine.changeState(helpPageScreen);
			}
			break;
		case 4:
			if (Config.isRank) {
				HelpPageScreen helpPageScreen = new HelpPageScreen();
				MainMidlet.engine.changeState(helpPageScreen);
			} else {
				this.keynum0();
			}
			break;
		case 5:
			this.keynum0();
			break;
		default:
			break;
		}
	}

	public void keynum0() {
		if (endGameDialog == null && this.g != null) {
			endGameDialog = new EndGameDialog(this, 466, 215);
			endGameDialog.init();
		}
//		if(ConstantSW.isQuite){
//			outGame();
//		}else{
//			if (endGameDialog == null && this.g != null) {
//				endGameDialog = new EndGameDialog(this, 166, 145);
//				endGameDialog.init();
//			}
//		}
	}

	public void outGame() {
		MainServer.getInstance().endGame();
		MainMidlet.engine.quitApp();
	}

	public void removeDialog() {
		this.endGameDialog = null;
	}

	public boolean isload() {
		// TODO Auto-generated method stub
		return false;
	}
	
	public boolean threadLoad() {
		// TODO Auto-generated method stub
		return false;
	}

}
