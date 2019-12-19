package com.game.mouse.modle;

import java.util.Vector;

public class ScenePass {
	private String code;

	private int isOpen;

	private Vector pass = new Vector();

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public int getIsOpen() {
		return isOpen;
	}

	public void setIsOpen(int isOpen) {
		this.isOpen = isOpen;
	}

	public Vector getPass() {
		return pass;
	}

	public void setPass(Vector pass) {
		this.pass = pass;
	}

	public void addPass(Pass p) {
		pass.addElement(p);
	}
}
