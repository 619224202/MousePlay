package com.game.mouse.view.maingame.child;

import java.util.Enumeration;
import java.util.Hashtable;

import javax.microedition.lcdui.Graphics;

import com.game.mouse.modle.Cat;
import com.game.mouse.modle.Mouse;
import com.game.mouse.modle.Sprite;
import com.game.mouse.view.View;
import com.game.mouse.view.game.GameMainView;
import com.game.mouse.view.maingame.child.SpriteView.SpriteMainGameL9Object;

/**
 * 
 * @author Administrator 基础障碍物 对应编号为50
 */
public class OrganView extends MainGameL9Object implements View {
	protected String code;

	protected GameMainView gameMainView;

	/**
	 * 碰撞对象列表
	 */
	protected Hashtable hitMainGameL9Objects = new Hashtable();

	protected Enumeration e;

	public OrganView(GameMainView gameMainView, String png, String code,
			int tileW, int tileH, int initX, int initY) {
		super(png, 32, tileW, tileH, initX, initY, -1, false, true, true, 2, 0);
		this.gameMainView = gameMainView;
		this.code = code;
	}

	public OrganView(GameMainView gameMainView, String code, int initX,
			int initY) {
		super("organ" + code, 32, initX, initY, -1, 0, false, true, true, 2, 0);
		this.code = code;
	}

	public void init() {
		// TODO Auto-generated method stub

	}

	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		this.paintFrame(g);
	}

	public void update() {
		// TODO Auto-generated method stub
		e = hitMainGameL9Objects.elements();
		while (e.hasMoreElements()) {
			MainGameL9Object mainGameL9Object = (MainGameL9Object) e
					.nextElement();
			if (!this.getColRect().intersects(mainGameL9Object.getColRect())) {
				hitMainGameL9Objects.remove(mainGameL9Object.toString());
			}
		}
	}

	public void mainGameHit(MainGameL9Object obj) {
		if (obj instanceof SpriteMainGameL9Object
				&& !hitMainGameL9Objects.contains(obj)) {
			SpriteView spriteView = ((SpriteMainGameL9Object) obj)
					.getSpriteView();
			Sprite sprite = spriteView.getSprite();
			if ((spriteView.getIsClockWise() == 1
					&& spriteView.getClockWiseObj() == obj || spriteView
					.getIsClockWise() == 0
					&& spriteView.getAntiClockWiseObj() == obj)
					&& spriteView.flashCount == 0) {
				if (sprite instanceof Cat
						&& ((Cat) sprite).ishitOrgan(this.code)) {
					spriteView.injured(20);
					hitMainGameL9Objects.put(obj.toString(), obj);
				} else if (sprite instanceof Mouse) {
					MouseSpriteView mouseSpriteView = (MouseSpriteView) spriteView;
					if (!mouseSpriteView.isUseProtect) {
						mouseSpriteView.injured(20);
						hitMainGameL9Objects.put(obj.toString(), obj);
					}
				}
			}
		}
	}
}
