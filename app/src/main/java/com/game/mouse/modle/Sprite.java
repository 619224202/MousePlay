package com.game.mouse.modle;

public class Sprite {
	protected String code;

	protected String name;

	/**
	 * 当前有几滴血
	 */
	protected int hp;

	/**
	 * 最大几滴血
	 */
	protected int maxhp;

	/**
	 * 对战时候的血量
	 */
	protected int fightHp;

	/**
	 * 对战时候的最大血量
	 */
	protected int fightMaxHp;

	/**
	 * 对战时候的蓝量
	 */
	protected int fightBlue;

	/**
	 * 对战时候的最大蓝
	 */
	protected int fightMaxBlue=100;

	/**
	 * 攻击力
	 */
	protected int att;

	protected int speed=8;

	protected int lv;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getHp() {
		return hp;
	}

	public void setHp(int hp) {
		this.hp = hp;
	}

	public int getMaxhp() {
		return maxhp;
	}

	public void setMaxhp(int maxhp) {
		this.maxhp = maxhp;
	}

	public int getFightHp() {
		return fightHp;
	}

	public void setFightHp(int fightHp) {
		this.fightHp = fightHp;
	}

	public int getFightMaxHp() {
		return fightMaxHp;
	}

	public void setFightMaxHp(int fightMaxHp) {
		this.fightMaxHp = fightMaxHp;
	}

	public int getFightBlue() {
		return fightBlue;
	}

	public void setFightBlue(int fightBlue) {
		this.fightBlue = fightBlue;
	}

	public int getFightMaxBlue() {
		return fightMaxBlue;
	}

	public void setFightMaxBlue(int fightMaxBlue) {
		this.fightMaxBlue = fightMaxBlue;
	}

	public int getAtt() {
		return att;
	}

	public void setAtt(int att) {
		this.att = att;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public int getLv() {
		return lv;
	}

	public void setLv(int lv) {
		this.lv = lv;
	}

}
