package com.game.mouse.modle;

public class Mouse extends Sprite {
	public Mouse(String code, int maxhp) {
		this.code = code;
		this.hp = this.maxhp = maxhp;
	}

	public Mouse(String code, int maxhp, int fightMaxHp, int att) {
		this.code = code;
		this.att = att;
		this.hp = this.maxhp = maxhp;
		this.fightHp = this.fightMaxHp = fightMaxHp;
		this.fightBlue = this.fightMaxBlue = 100;
	}

	public String getName() {
		if ("0".equals(code)) {
			this.name = "艾米";
		} else if ("1".equals(code)) {
			this.name = "米妮";
		} else {
			this.name = "杰米";
		}
		return this.name;
	}
}
