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
import com.model.base.ParserTbl;
import com.model.control.MSDateManager;
import com.model.tool.PlaySoundService;
import com.model.tool.PubToolKit;

public class StoryPageScreen extends L9Screen {
	private Graphics g;

	private L9Object story;

	private boolean isEndStory;

	private int maxStory = Config.story == 0 ? 2 : 1;

	private int crrStory = 0;

	public void fristInit() {
		ParserTbl.getInstance().defineMedia(1, "tbl/story.tbl");
		this.newStory();
		story.setNextFrameTime(2);
		PlaySoundService.play(0);
	}

	public void newStory() {
		String res = "story" + Config.story + "" + crrStory;
		if (story != null) {
			story = null;
			String resTmp = "story" + Config.story + "" + (crrStory - 1);
			MSDateManager.getInstance().removeAnuKey(resTmp, true);
		}
		story = new L9Object(null, res, 2, L9Config.SCR_W / 2,
				L9Config.SCR_H / 2, 1, 0, false, false, true, 2, 0) {
			public void endAnimation() {
				crrStory++;
				if (crrStory >= maxStory) {
					isEndStory = true;
				} else {
					newStory();
				}
			}
		};
	}

	public void Paint() {
		// TODO Auto-generated method stub
		if (this.g == null) {
			this.g = Engine.FG;
			g.fillRect(0, 0, L9Config.SCR_W, L9Config.SCR_H);
		}
		g.setClip(0, (int)(85 * MainMidlet.scaleY), (int)(L9Config.SCR_W * MainMidlet.scaleX), (int)(348* MainMidlet.scaleY));
		if (story != null) {
			story.paintFrame(g);
		}
		g.setClip(0, 0, (int)(L9Config.SCR_W * MainMidlet.scaleX), (int)(L9Config.SCR_H*MainMidlet.scaleY));
		if (Config.endStoryEnterKey) {
			PubToolKit.drawString("按确定键跳过", 230, L9Config.SCR_H - 30, 170, Font
					.getFont(Font.FACE_MONOSPACE, Font.STYLE_BOLD,
							Font.SIZE_LARGE), 0xffffff, g);
		} else {
			PubToolKit.drawString("按7键跳过", 255, L9Config.SCR_H - 30, 150, Font
					.getFont(Font.FACE_MONOSPACE, Font.STYLE_BOLD,
							Font.SIZE_LARGE), 0xffffff, g);
		}
	}

	public void Update() {
		if (story != null) {
			story.doAllFrame();
		}
		if (isEndStory) {
			this.goGame();
		}
		keyPress();
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
		case Engine.K_KEY_NUM7:
			this.keynum7();
			break;
		}
	}

	public void keyleft() {
	}

	public void keyright() {
	}

	public void keyfire() {
		if (Config.endStoryEnterKey)
			this.goGame();
	}

	public void keynum7() {
		if (!Config.endStoryEnterKey)
			this.goGame();
	}

	public void goGame() {
		HomePageScreen homePageScreen = new HomePageScreen();
		MainMidlet.engine.changeState(homePageScreen);
	}

	public boolean isload() {
		// TODO Auto-generated method stub
		return false;
	}
}
