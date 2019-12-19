package com.game.mouse.modle;

import java.util.Hashtable;

import com.game.mouse.modle.service.DataManageService;

public class Cat extends Sprite {
	/**
	 * 是否可以吃奶酪
	 */
	private boolean isEatCheese;

	/**
	 * 是否可以携带奶酪
	 */
	private boolean isCarryCheese;

	private int[] organs;

	private int moveType;

	/**
	 * 是否可以跳跃
	 */
	private boolean isJump;

	public Cat(String code, int hp, int fightHp, int att, int lv) {
		this.code = code;
		this.lv = lv;
		this.hp = hp;
		this.fightMaxHp = this.fightHp = fightHp;
		this.att = att;
		Hashtable catmsg = DataManageService.getInsatnce().getCatMsgByCode(
				Integer.parseInt(code));
		this.moveType = ((Integer) catmsg.get("moveType")).intValue();
		this.speed = ((Integer) catmsg.get("speed")).intValue();
		this.isJump = ((Integer) catmsg.get("jump")).intValue() == 1;
		this.isEatCheese = ((Integer) catmsg.get("eatCheese")).intValue() == 1;
		this.isCarryCheese = ((Integer) catmsg.get("carryCheese")).intValue() == 1;
		this.organs = (int[]) catmsg.get("organs");
	}

	/**
	 * 是否陷阱有作用
	 * 
	 * @return
	 */
	public boolean ishitOrgan(String code) {
		if (this.organs != null && this.organs.length > 0) {
			for (int i = 0; i < this.organs.length; i++) {
				if (this.organs[i] == Integer.parseInt(code)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 是否可以吃奶酪
	 * 
	 * @return
	 */
	public boolean isEatCheese() {
		return isEatCheese;
	}

	public boolean isCarryCheese() {
		return isCarryCheese;
	}

	public int getMoveType() {
		return moveType;
	}

	public boolean isJump() {
		return isJump;
	}
}
