package com.game.mouse.view;

import javax.microedition.lcdui.Graphics;

public interface View {
	public void init() ;
	
	public void paint(Graphics g);

	public void update();

}
