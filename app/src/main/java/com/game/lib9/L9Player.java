package com.game.lib9;

import javax.microedition.lcdui.Graphics;

import com.model.base.JGRectangle;
import com.model.control.MSDateManager;
import com.studio.motionwelder.MSprite;
import com.studio.motionwelder.MSpriteAnimationPlayer;
import com.studio.motionwelder.MSpriteData;

/**
 * 动画显示及碰撞等
 * 
 * @author wanglu
 * 
 */
public class L9Player implements MSprite {
	/**
	 * anu对应的data
	 */
	private MSpriteData data;
	/**
	 * 动画对应的player
	 */
	private MSpriteAnimationPlayer player;
	/**
	 * anu的坐标，相对坐标
	 */
	private int[] pos = new int[2];
	/**
	 * 所属的Obj
	 */
	private L9Object fatherObj;

	public static final int drawType = 0;
	public static final int colType = 1;
	public static final int atkType = 2;

	private boolean bDestroy;

	/**
	 * 
	 * @param salerName:anu文件对应的图片名
	 *            比如 hero-------hero.anu, 从hashTable中获取
	 */
	public L9Player(String anuKey, L9Object fatherObj, int initX, int initY,
			int trans) {
		data = MSDateManager.getInstance().getMSpriteData(anuKey,trans);
		player = new MSpriteAnimationPlayer(data, this);
		this.fatherObj = fatherObj;
		this.pos[0] = initX;
		this.pos[1] = initY;
	}

	/**
	 * 设置播放动画
	 * 
	 * @param animationId
	 */
	public void setAnimation(int animationId) {
		player.setAnimation(animationId);
	}

	/**
	 * 获取当前动画的动画ID号
	 */
	public int getAnimation() {
		return player.getAnimation();
	}

	/**
	 * 获取本套动画的动画总数
	 */
	public int getAnimationCount() {
		return player.getAnimationCount();
	}

	/**
	 * 设置当前帧数
	 * 
	 * @param frameId
	 */
	public void setFrame(int frameId) {
		player.setFrame(frameId);
	}

	/**
	 * 获取当前动画的帧数
	 */
	public int getFrameCount() {
		return player.getFrameCount();
	}

	/**
	 * 获取当前帧数
	 */
	public int getCurrentFrame() {
		return player.getCurrentFrame();
	}

	/**
	 * 设置当前动画从第几帧播放，是否循环播放，-1为从0开始的循环播放
	 */
	public void setLoopOffSet(int loopOffSet) {
		player.setLoopOffset(loopOffSet);
	}

	/**
	 * 获取当前帧画图范围
	 */
	public JGRectangle getDrawRect() {
		JGRectangle rect = getRectByType(drawType);
		if (rect != null) {
			rect.x = rect.x + rect.width / 2;
			rect.y = rect.y + rect.height;
		}
		return rect;
	}

	/**
	 * 获取当前帧碰撞范围
	 * 
	 * @return
	 */
	public JGRectangle getColRect() {
		return getRectByType(colType);
	}

	/**
	 * 获取当前帧攻击范围
	 * 
	 * @return
	 */
	public JGRectangle getAtkRect() {
		return getRectByType(atkType);
	}

	public JGRectangle getRectByType(int rectType) {
		int[] col = new int[4];
		JGRectangle rect = new JGRectangle();
		if (rectType < player.getNumberOfCollisionRect()) {
			col = player.getCollisionRect(rectType);
			rect.setRect(getSpriteDrawX() + col[0], getSpriteDrawY() + col[1],
					col[2], col[3]);
		}
		return rect;
	}

	/**
	 * 销毁
	 */
	public void clear() {
		data = null;
		player = null;
	}

	public void update() {
		if (!bDestroy) {
			player.update();
		}
	}

	public void paint(Graphics g) {
		if (!bDestroy) {
			player.drawFrame(g);
		}
	}

	/**
	 * 设置横向画图位置
	 */
	public void setSpriteDrawX(int x) {
		pos[0] = x;
	}

	/**
	 * 设置纵向画图位置
	 */
	public void setSpriteDrawY(int y) {
		pos[1] = y;
	}

	public void endOfAnimation() {
		// TODO Auto-generated method stub
		fatherObj.setIsPlay(false);
		fatherObj.endAnimation();
	}

	public int getSpriteDrawX() {
		// TODO Auto-generated method stub
		return fatherObj.getPosX();
	}

	public int getSpriteDrawY() {
		// TODO Auto-generated method stub
		return fatherObj.getPosY();
	}

	public byte getSpriteOrientation() {
		// TODO Auto-generated method stub
		return fatherObj.getSpriteOrientation();
	}

	public void updateSpritePosition(int deltaX, int deltaY) {
		// TODO Auto-generated method stub
		fatherObj.updateSpritePosition(deltaX, deltaY);
	}

	public boolean isBDestroy() {
		return bDestroy;
	}

	public void setBDestroy(boolean destroy) {
		bDestroy = destroy;
	}

}
