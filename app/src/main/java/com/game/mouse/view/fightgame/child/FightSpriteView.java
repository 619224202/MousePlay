package com.game.mouse.view.fightgame.child;

import javax.microedition.lcdui.Graphics;

import com.game.lib9.L9Object;
import com.game.mouse.modle.Cat;
import com.game.mouse.modle.Mouse;
import com.game.mouse.modle.Sprite;
import com.game.mouse.view.View;
import com.game.mouse.view.game.GameFightView;
import com.game.mouse.view.game.GameMainView;
import com.game.mouse.view.maingame.child.MapView;
import com.game.mouse.view.maingame.child.SpriteView;
import com.model.tool.PubToolKit;

public abstract class FightSpriteView implements View {
	protected Sprite sprite;

	protected GameFightView gameFightView;

	protected String res;

	private int objType;

	/**
	 * 状态 0-静止 1-攻击前 2-攻击后 3-受伤 4-死亡
	 */
	protected int state;

	protected int initX, initY;

	protected int x, y;

	protected SpriteFightL9Object spriteFightL9Object;

	/**
	 * 攻击方式
	 */
	protected int attType;

	/**
	 * 被攻击的对象
	 */
	protected FightSpriteView bfightSpriteView;

	/**
	 * 攻击的对象
	 */
	protected FightSpriteView attFightSpriteView;

	protected int flashCount = 0;

	public FightSpriteView(GameFightView gameFightView, Sprite sprite,
			String res, int objType, int initX, int initY) {
		this.gameFightView = gameFightView;
		this.sprite = sprite;
		this.res = res;
		this.objType = objType;
		this.initX = initX;
		this.initY = initY;
		this.x = this.initX;
		this.y = this.initY;
	}

	public Sprite getSprite() {
		return sprite;
	}

	public void setSprite(Sprite sprite) {
		this.sprite = sprite;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void init() {
		spriteFightL9Object = new SpriteFightL9Object(this, this.res,
				this.objType, this.initX, this.initY, -1, 0, false, true, true,
				2, 0) {
			public void fightGameHit(FightGameL9Object obj) {
				bhit(obj);
			}
		};
	}

	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		if (flashCount == 0 || flashCount % 2 == 0) {
			if (spriteFightL9Object != null) {
				spriteFightL9Object.paintFrame(g);
			}
		}
	}

	public void update() {
		// TODO Auto-generated method stub
		spriteFightL9Object.setPosX(this.x);
		spriteFightL9Object.setPosY(this.y);
		this.isDie();
		this.playAttAnimation();
		if (flashCount > 0) {
			flashCount--;
		}
	}

	public abstract void playAttAnimation();

	public abstract boolean isDie();

	public abstract void injured(boolean isSkillHit);

	/**
	 * 攻击前动画结束
	 */
	public abstract void playAttBeforeAnimationEnd();

	/**
	 * 攻击后动画结束
	 */
	public abstract void playAttAfterAnimationEnd();

	/**
	 * 受伤动画结束
	 */
	public abstract void playInjuredAnimationEnd();

	public abstract void endSkill(View view);

	public void destrySpriteBeforeView(View view) {
		gameFightView.removeGameBeforeView(view);
	}

	class SpriteFightL9Object extends FightGameL9Object {
		private FightSpriteView fightSpritewView;

		public SpriteFightL9Object(FightSpriteView fightSpritewView,
				String objKey, int objType, int x, int y, int loopOffSet,
				int animation, boolean is_drawPartBuf, boolean is_addToEng,
				boolean isPlay, int depth, int trans) {
			super(objKey, objType, x, y, loopOffSet, animation, is_drawPartBuf,
					is_addToEng, isPlay, depth, trans);
			this.fightSpritewView = fightSpritewView;
		}

		public FightSpriteView getFightSpritewView() {
			return this.fightSpritewView;
		}
	}

	public SpriteFightL9Object getSpriteFightL9Object() {
		return spriteFightL9Object;
	}

	/**
	 * 被相撞
	 * 
	 * @param obj
	 */
	public void bhit(FightGameL9Object obj) {
		if (obj instanceof FightSkillView) {
			FightSkillView fightSkillView = (FightSkillView) obj;
			if (!this.getClass().getName().equals(
					fightSkillView.getFightSpriteView().getClass().getName())
					&& !fightSkillView.isContainsHitObj(this)) {
				this.injured(true);
				fightSkillView.addHitObj(this);
			}
		}
	}

	/**
	 * 减少血量
	 * 
	 * @param hp
	 */
	public void losehp(int hp) {
		int losehp = this.sprite.getFightHp() - hp > 0 ? this.sprite
				.getFightHp()
				- hp : 0;
		this.sprite.setFightHp(losehp);
	}

	/**
	 * 加血
	 */
	public void addHp() {
		FightAddHpView fightAddHpView = new FightAddHpView(this, this.x, this.y);
		this.gameFightView.addGameBeforeView(fightAddHpView);

	}

	/**
	 * 加血完成
	 * 
	 * @param fightAddHpView
	 */
	public void endHp(FightAddHpView fightAddHpView) {
		this.sprite.setFightHp(this.sprite.getFightMaxHp());
		this.destrySpriteBeforeView(fightAddHpView);
	}
}
