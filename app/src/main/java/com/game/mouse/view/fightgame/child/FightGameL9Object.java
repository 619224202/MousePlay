package com.game.mouse.view.fightgame.child;

import com.game.lib9.L9Object;

public class FightGameL9Object extends L9Object {

	public FightGameL9Object(int objType, String objKey, int tileW, int tileH,
			int x, int y, int loopOffSet, boolean is_drawPartBuf,
			boolean is_addToEng, boolean isPlay, int depth) {
		super(null, objType, objKey, tileW, tileH, x, y, loopOffSet,
				is_drawPartBuf, is_addToEng, isPlay, depth);
		// TODO Auto-generated constructor stub
	}

	public FightGameL9Object(String objKey, int objType, int x, int y,
			int loopOffSet, int animation, boolean is_drawPartBuf,
			boolean is_addToEng, boolean isPlay, int depth, int trans) {
		super(null, objKey, objType, x, y, loopOffSet, animation,
				is_drawPartBuf, is_addToEng, isPlay, depth, trans);
		// TODO Auto-generated constructor stub
	}

	public void hit(L9Object obj) {
		if (obj instanceof FightGameL9Object) {
			this.fightGameHit((FightGameL9Object)obj);
		}
	}

	/**
	 * 所有MainGame对象需要碰撞需要覆盖这个类
	 */
	public void fightGameHit(FightGameL9Object obj) {

	}
}
