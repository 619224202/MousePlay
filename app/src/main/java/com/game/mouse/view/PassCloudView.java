package com.game.mouse.view;

import javax.microedition.lcdui.Graphics;

import com.model.control.MyImg;

public class PassCloudView implements View {

	private MyImg cloud;

	private int code;

	private int x, y, initX;

	private int minX, maxX;

	private int speed=1;

	private boolean isMoveLeft;

	public PassCloudView(int code, int initX, int initY, int minX, int maxX) {
		this.code = code;
		this.x = this.initX = initX;
		this.y = initY;
		this.minX = minX;
		this.maxX = maxX;
	}

	public void init() {
		// TODO Auto-generated method stub
		this.cloud = new MyImg("passpage/cloud" + code + ".png");
	}

	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		cloud.drawImage(g, this.x, this.y, g.VCENTER | g.HCENTER);
	}

	public void update() {
		// TODO Auto-generated method stub
		this.move();
	}

	public void move() {
		if (isMoveLeft) {
			if (this.x - speed > this.minX) {
				this.x = this.x - speed;
			} else {
				this.x = this.minX;
				isMoveLeft = false;
			}
		} else {
			if (this.x + speed < this.maxX) {
				this.x = this.x + speed;
			} else {
				this.x = this.maxX;
				isMoveLeft = true;
			}
		}
	}

}
