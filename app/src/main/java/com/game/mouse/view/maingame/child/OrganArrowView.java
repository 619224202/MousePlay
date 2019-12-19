package com.game.mouse.view.maingame.child;

import com.game.mouse.view.game.GameMainView;
import com.game.mouse.view.maingame.child.SpriteView.SpriteMainGameL9Object;

/**
 * 
 * @author Administrator
 *箭头障碍物，碰撞后改变方向
 *对应编号为53(顺时针箭头)
 *对应编号为54（逆时针箭头）
 */
public class OrganArrowView extends OrganView {
	private int isClockWise = 1;

	public OrganArrowView(GameMainView gameMainView, String png, String code,
			int isClockWise, int tileW, int tileH, int initX, int initY) {
		super(gameMainView, png, code, tileW, tileH, initX, initY);
		this.isClockWise = isClockWise;
	}

	public void mainGameHit(MainGameL9Object obj) {
		if (obj instanceof SpriteMainGameL9Object
				&& !hitMainGameL9Objects.contains(obj)) {
			SpriteView spriteView = ((SpriteMainGameL9Object) obj)
					.getSpriteView();
			if (spriteView.getIsClockWise() == 1
					&& spriteView.getClockWiseObj() == obj
					|| spriteView.getIsClockWise() == 0
					&& spriteView.getAntiClockWiseObj() == obj) {
				if (spriteView.getIsClockWise() != isClockWise) {
					spriteView.hitArrowOrgan();
					hitMainGameL9Objects.put(obj.toString(), obj);
				}
			}
		}
	}
}
