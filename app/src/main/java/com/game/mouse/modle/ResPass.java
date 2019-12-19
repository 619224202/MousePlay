package com.game.mouse.modle;

import java.util.Hashtable;
import java.util.Vector;

public class ResPass {
	private int code;

	private Vector cats = new Vector();

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public Vector getCats() {
		return cats;
	}

	public void setCats(Vector cats) {
		this.cats = cats;
	}

	public Hashtable getCatMsg(int catCode) {
		for (int i = 0; i < this.cats.size(); i++) {
			Hashtable catMsg = (Hashtable) cats.elementAt(i);
			if (Integer.parseInt(catMsg.get("code").toString()) == catCode) {
				return catMsg;
			}
		}
		return null;
	}
}
