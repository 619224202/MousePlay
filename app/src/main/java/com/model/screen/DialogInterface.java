package com.model.screen;

import javax.microedition.lcdui.Graphics;

public interface DialogInterface {
	public void keyPressed(int keyCode);
	public void update();
	public void paint(Graphics g);
	public void release();
	public int getSelOption();
	public void setPos(int x,int y);
	public boolean isRunning();
}
