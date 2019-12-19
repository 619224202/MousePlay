package com.game.mouse.modle;

import java.util.Vector;

import com.game.mouse.modle.service.DataManageService;

public class Pass {
	private int sceneCode;

	private int code;

	private int exp;

	private int isOpen;

	private int starNum;

	private boolean isfrist;

	private ResPass resPass;

	public Pass(int sceneCode, int code) {
		this.sceneCode = sceneCode;
		this.code = code;
	}

	public int getSceneCode() {
		return sceneCode;
	}

	public void setSceneCode(int sceneCode) {
		this.sceneCode = sceneCode;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public int getExp() {
		return exp;
	}

	public void setExp(int exp) {
		this.exp = exp;
	}

	public int getIsOpen() {
		return isOpen;
	}

	public void setIsOpen(int isOpen) {
		this.isOpen = isOpen;
	}

	public int getStarNum() {
		return starNum;
	}

	public void setStarNum(int starNum) {
		this.starNum = starNum;
	}

	public boolean isIsfrist() {
		return isfrist;
	}

	public void setIsfrist(boolean isfrist) {
		this.isfrist = isfrist;
	}

	public ResPass getResPass() {
		if (resPass == null) {
			resPass = DataManageService.getInsatnce().getScenePassMsg(
					this.sceneCode, this.code);
		}
		return resPass;
	}
}
