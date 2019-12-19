package com.game.mouse.view.fightgame.child;

import com.game.mouse.modle.Cat;
import com.game.mouse.modle.Mouse;
import com.game.mouse.modle.UserMouse;
import com.game.mouse.view.View;
import com.game.mouse.view.game.GameFightView;
import com.model.tool.PubToolKit;

public class FightSpriteMouseView extends FightSpriteView {

	public FightSpriteMouseView(GameFightView gameFightView, Mouse mouse,
			int initX, int initY) {
		super(gameFightView, mouse, "fightmouse" + mouse.getCode() + ""
				+ ((UserMouse) mouse).getWeaponStage(), 8, initX, initY);
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
		spriteFightL9Object.setAnimation(4);
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
		spriteFightL9Object.setAnimation(6);
		state = 1;
		attType = 2;
		this.bfightSpriteView = fightSpriteView;
		this.bfightSpriteView.attFightSpriteView = this;
	}

	/**
	 * 受伤
	 */
	public void injured(boolean isSkillHit) {
		if (!gameFightView.isUserRound()) {
			spriteFightL9Object.setAnimation(8);
			state = 3;
			int losehp = 0;
			if (!isSkillHit) {
				switch (attFightSpriteView.attType) {
				case 0:
					losehp = attFightSpriteView.getSprite().getAtt();
					break;
				case 1:
					losehp = attFightSpriteView.getSprite().getAtt();
					break;
				case 2:
					int num = PubToolKit.getRandomInt(6) + 3;
					losehp = attFightSpriteView.getSprite().getAtt() * num;
					break;
				}
			} else {
				int num = PubToolKit.getRandomInt(6) + 3;
				if (attFightSpriteView != null) {
					losehp = attFightSpriteView.getSprite().getAtt() * num;
				} else {
					losehp = gameFightView.getSpriteCatView().getSprite()
							.getAtt()
							* num;
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
			spriteFightL9Object.setAnimation(5);
			bfightSpriteView.injured(false);
			break;
		case 2:
			this.state = 2;
			spriteFightL9Object.setAnimation(7);
			switch (Integer.parseInt(this.sprite.getCode())) {
			case 0:
				this.skillFire();
				break;
			case 1:
				this.skillStone();
				break;
			case 2:
				this.skillWind();
				break;
			}
			break;
		}
	}

	/**
	 * 风技能
	 */
	public void skillWind() {
		String res = "fightskillwind";
		if (((UserMouse) this.sprite).getWeaponStage() > 0) {
			res = "fightskillbigwind";
		}
		FightSkillWindView fightSkillWindView = new FightSkillWindView(this,
				res, this.x + 40, this.y + 20, false);
		gameFightView.addGameBeforeView(fightSkillWindView);
	}

	/**
	 * 火技能
	 */
	public void skillFire() {
		String res = "fightskillfire";
		if (((UserMouse) this.sprite).getWeaponStage() > 0) {
			res = "fightskillbigfire";
		}
		FightSkillView fightSkillView = new FightSkillView(this, res,
				this.x + 100, this.y + 20);
		gameFightView.addGameBeforeView(fightSkillView);
	}

	/**
	 * 石头技能
	 */
	public void skillStone() {
		String res = "fightskillstone";
		if (((UserMouse) this.sprite).getWeaponStage() > 0) {
			res = "fightskillbigstone";
		}
		FightSkillView fightSkillView = new FightSkillView(this, res,
				this.x + 40, this.y + 20);
		gameFightView.addGameBeforeView(fightSkillView);
	}

	/**
	 * 技能消除
	 */
	public void endSkill(View view) {
		this.destrySpriteBeforeView(view);
		this.gameFightView.notifyAttEnd(this);
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
			spriteFightL9Object.setAnimation(3);
			this.gameFightView.notifyAttEnd(this);
			break;
		case 2:
			this.state = 0;
			spriteFightL9Object.setAnimation(0);
			break;
		}
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
			spriteFightL9Object.setAnimation(3);
			break;
		case 2:
			spriteFightL9Object.setAnimation(0);
			break;
		}
	}

	public boolean isDie() {
		// TODO Auto-generated method stub
		if (this.sprite.getFightHp() <= 0) {
			if (this.state != 4 && gameFightView.isUserRound()) {
				spriteFightL9Object.setAnimation(9);
				gameFightView.notifyDie(this);
				this.state = 4;
			}
			return true;
		}
		return false;
	}

	/**
	 * 复活
	 */
	public void revive() {
		this.state = 0;
		this.flashCount = 10;
		spriteFightL9Object.setAnimation(0);
		this.sprite.setFightHp(this.sprite.getFightMaxHp());
	}

	public void endHp(FightAddHpView fightAddHpView) {
		super.endHp(fightAddHpView);
		gameFightView.initChangeRound(false);
	}

	/**
	 * 改变进化武器后形态
	 */
	public void chageUpStageWeapon() {
		this.res = "fightmouse" + sprite.getCode() + ""
				+ ((UserMouse) sprite).getWeaponStage();
		this.init();
		this.changeWeaponStand();
	}

	public void changeWeaponStand() {
		spriteFightL9Object.setAnimation(3);
	}
}
