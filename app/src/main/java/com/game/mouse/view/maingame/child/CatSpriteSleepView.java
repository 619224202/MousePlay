package com.game.mouse.view.maingame.child;

import com.game.mouse.modle.Cat;
import com.game.mouse.view.game.GameMainView;

/**
 * 来回走动的老鼠
 * 
 * @author Administrator
 * 
 */
public class CatSpriteSleepView extends CatSpriteView {

	public CatSpriteSleepView(GameMainView gameMainView, MapView mapView,
			Cat cat,  int direction, int initX, int initY) {
		super(gameMainView, mapView, cat, 1, direction, initX, initY);
		// TODO Auto-generated constructor stub
	}

	public void startMove() {
		super.stopMove();
	}
}
