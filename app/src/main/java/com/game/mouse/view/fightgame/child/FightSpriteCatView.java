package com.game.mouse.view.fightgame.child;

import com.game.lib9.L9Config;
import com.game.mouse.modle.Cat;
import com.game.mouse.modle.Sprite;
import com.game.mouse.modle.UserMouse;
import com.game.mouse.view.View;
import com.game.mouse.view.game.GameFightView;
import com.model.tool.PubToolKit;

public class FightSpriteCatView extends FightSpriteView {

	public FightSpriteCatView(GameFightView gameFightView, Cat cat, int initX,
			int initY) {
		super(gameFightView, cat, "fightcat" + cat.getCode(), 16, initX, initY);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 普通攻击
	 */
	public void commonAtt(FightSpriteView fightSpriteView) {
		spriteFightL9Object.setAnimation(1);
		state = 1;
		attType = 0;
		this.bfightSpriteView = fightSpriteView;
		this.bfightSpriteView.attFightSpriteView = this;
	}

	/**
	 * 武器攻击
	 */
	public void weaponAtt(FightSpriteView fightSpriteView) {
		spriteFightL9Object.setAnimation(1);
		state = 1;
		attType = 1;
		this.bfightSpriteView = fightSpriteView;
		this.bfightSpriteView.attFightSpriteView = this;
	}

	/**
	 * 必杀技
	 * 
	 * @param fightSpriteView
	 */
	public void killAtt(FightSpriteView fightSpriteView) {
		spriteFightL9Object.setAnimation(3);
		state = 1;
		attType = 2;
		this.bfightSpriteView = fightSpriteView;
		this.bfightSpriteView.attFightSpriteView = this;
	}

	/**
	 * 受伤
	 */
	public void injured(boolean isSkillHit) {
		if (gameFightView.isUserRound()) {
			spriteFightL9Object.setAnimation(5);
			System.out.println("animationCount="+spriteFightL9Object.getAnimation());
			state = 3;
			int losehp = 0;
			if (!isSkillHit) {
				switch (attFightSpriteView.attType) {
				case 0:
					if (attFightSpriteView.getSprite() instanceof UserMouse) {
						losehp = ((UserMouse) attFightSpriteView.getSprite())
								.getInitAtt();
					} else {
						losehp = attFightSpriteView.getSprite().getAtt();
					}
					break;
				case 1:
					if (attFightSpriteView.getSprite() instanceof UserMouse) {
						losehp = ((UserMouse) attFightSpriteView.getSprite())
								.getFightAllAtt();
					} else {
						losehp = attFightSpriteView.getSprite().getAtt();
					}
					break;
				case 2:
					int num = PubToolKit.getRandomInt(6) + 4;
					if (attFightSpriteView.getSprite() instanceof UserMouse) {
						losehp = ((UserMouse) attFightSpriteView.getSprite())
								.getFightAllAtt();
					} else {
						losehp = attFightSpriteView.getSprite().getAtt();
					}
					losehp = losehp * num;
					break;
				}
			} else {
				if (attFightSpriteView != null) {
					int num = PubToolKit.getRandomInt(6) + 4;
					if (attFightSpriteView.getSprite() instanceof UserMouse) {
						losehp = ((UserMouse) attFightSpriteView.getSprite())
								.getFightAllAtt();
					} else {
						losehp = attFightSpriteView.getSprite().getAtt();
					}
					losehp = losehp * num;
				}
			}
			this.losehp(losehp);
			FightLoseBloodView fightLoseBloodView = new FightLoseBloodView(
					this, this.x, this.y - 50, losehp);
			gameFightView.addGameBeforeView(fightLoseBloodView);
		}
	}

	/**
	 * 播放攻击前动画
	 */
	public void playAttAnimation() {
		switch (state) {
		case 1:
			if (spriteFightL9Object.getCurrentFrame() >= spriteFightL9Object
					.getFrameCount() - 1) {
				this.playAttBeforeAnimationEnd();
			}
			break;
		case 2:
			if (spriteFightL9Object.getCurrentFrame() >= spriteFightL9Object
					.getFrameCount() - 1) {
				this.playAttAfterAnimationEnd();
			}
			break;
		case 3:
			if (spriteFightL9Object.getCurrentFrame() >= spriteFightL9Object
					.getFrameCount() - 1) {
				this.playInjuredAnimationEnd();
			}
			break;
		default:
			break;
		}

	}

	/**
	 * 攻击前动画结束
	 */
	public void playAttBeforeAnimationEnd() {
		switch (attType) {
		case 0:
			this.state = 2;
			spriteFightL9Object.setAnimation(2);
			bfightSpriteView.injured(false);
			break;
		case 1:
			this.state = 2;
			spriteFightL9Object.setAnimation(2);
			bfightSpriteView.injured(false);
			break;
		case 2:
			this.state = 2;
			spriteFightL9Object.setAnimation(4);
			skillAtt();
			break;
		}
	}

	public void skillAtt() {
		switch (Integer.parseInt(this.sprite.getCode())) {
		case 0:
			this.skillFire();
			break;
		case 1:
			this.skillThunder();
			break;
		case 2:
			this.skillFire();
			break;
		case 3:
			this.skillDart();
			break;
		case 4:
			this.skillShoot();
			break;
		case 5:
			this.skillThunder();
			break;
		case 6:
			this.skillWater();
			break;
		case 7:
			this.skillFire();
			break;
		default:
			this.skillWind();
			break;
		}
	}

	/**
	 * 风技能
	 */
	public void skillWind() {
		FightSkillWindView fightSkillWindView = new FightSkillWindView(this,
				"fightskillwind", this.x - 40, this.y + 20, true);
		gameFightView.addGameBeforeView(fightSkillWindView);
	}

	/**
	 * 飞镖技能
	 */
	public void skillDart() {
		FightSkillView fightSkillView = new FightSkillView(this,
				"fightskilldart", this.x - 40, this.y + 20);
		gameFightView.addGameBeforeView(fightSkillView);
	}

	/**
	 * 火技能
	 */
	public void skillFire() {
		FightSkillView fightSkillView = new FightSkillView(this,
				"fightskillfireright", this.x - 100, this.y + 20);
		gameFightView.addGameBeforeView(fightSkillView);
		fightSkillView.setSpriteOrientation(true);
	}

	/**
	 * 雷电技能
	 */
	public void skillThunder() {
		FightSkillView fightSkillView = new FightSkillView(this,
				"fightskillthunder", bfightSpriteView.x, bfightSpriteView.y);
		gameFightView.addGameBeforeView(fightSkillView);
	}

	/**
	 * 水技能
	 */
	public void skillWater() {
		FightSkillView fightSkillView = new FightSkillView(this,
				"fightskillwater", L9Config.SCR_W, this.y + 40);
		gameFightView.addGameBeforeView(fightSkillView);
	}

	/**
	 * 发射子弹技能
	 */
	public void skillShoot() {
		FightSkillView fightSkillView = new FightSkillView(this,
				"fightskillshoot", this.x - 20, this.y);
		gameFightView.addGameBeforeView(fightSkillView);
	}

	/**
	 * 
	 * 攻击结束后初始化新回合
	 */
	public void playAttAfterAnimationEnd() {
		switch (attType) {
		case 0:
			this.state = 0;
			spriteFightL9Object.setAnimation(0);
			this.gameFightView.notifyAttEnd(this);
			break;
		case 1:
			this.state = 0;
			spriteFightL9Object.setAnimation(0);
			this.gameFightView.notifyAttEnd(this);
			break;
		case 2:
			this.state = 0;
			spriteFightL9Object.setAnimation(0);
			break;
		}
	}

	/**
	 * 技能消除
	 */
	public void endSkill(View view) {
		this.destrySpriteBeforeView(view);
		this.gameFightView.notifyAttEnd(this);
	}

	/**
	 * 受伤动画结束
	 */
	public void playInjuredAnimationEnd() {
		this.state = 0;
		switch (attType) {
		case 0:
			spriteFightL9Object.setAnimation(0);
			break;
		case 1:
			spriteFightL9Object.setAnimation(0);
			break;
		case 2:
			spriteFightL9Object.setAnimation(0);
			break;
		}
	}

	public boolean isDie() {
		// TODO Auto-generated method stub
		if (this.sprite.getFightHp() <= 0 && !gameFightView.isUserRound()) {
			if (this.state != 4) {
				spriteFightL9Object.setAnimation(6);
				gameFightView.notifyDie(this);
				this.state = 4;
			}
			return true;
		}
		return false;
	}
}
