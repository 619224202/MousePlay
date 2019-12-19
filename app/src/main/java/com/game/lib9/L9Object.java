package com.game.lib9;

import android.util.Log;

import java.util.Vector;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Sprite;

import com.model.base.JGRectangle;
import com.model.control.MyImg;
import com.studio.motionwelder.MSprite;

/**
 * l9Object:所有对象继承该类，主要实现了对象的创建，移动，碰撞，销毁等操作 该类的画图位置为中下点
 * 
 * @author wanglu
 * 
 */
public class L9Object {
	/**
	 * 对象ID
	 */
	static long objId = 0;
	/**
	 * 对象名字
	 */
	private String objKey;

	private int objType;
	/**
	 * 坐标及判定范围 lastDrawRect：上一帧的画图矩阵 drawRect:当前帧数的画图矩阵 lastColRect:上一帧的碰撞范围
	 * colRect:当前碰撞的范围
	 */
	protected JGRectangle lastDrawRect, drawRect, lastColRect, colRect;

	/**
	 * 上一层对象:用来执行是否局部刷新 处于同一层的对象不进行绘制检测
	 */
	protected L9Object fatherObject;
	/**
	 * 对于游戏UI的一些检测，是否需要和其进行重画检测
	 */
	private Vector childObject;
	/**
	 * 移动速度
	 */
	protected int xSpeed;
	protected int ySpeed;
	/**
	 * posX,posY 横坐标，纵坐标:为画图的左上点
	 */
	protected int posX, posY;

	private Engine engine;
	/**
	 * 判断对象是否存活
	 */
	private boolean is_alive = true;;
	/**
	 * 判断对象是否显示
	 */
	private boolean is_suspended = false;
	/**
	 * 判断对象画图区域是否更新
	 */
	private boolean is_drawUpdate;

	/**
	 * 是否使用动画:true为使用动作编辑器里面的动画 false为使用一般图片
	 */
	private boolean is_useAnimation;

	/**
	 * 播放动画次数 若为-1，则从0开始循环播放，若为其余的数则从该数开始循环播放1次
	 */
	private int loopOffSetNum;
	/**
	 * 动画总帧数
	 */
	private int frameCount;
	/**
	 * 当前动画帧数
	 */
	private int currentFrame;
	/**
	 * 动画间隔：以帧来计算
	 */
	private int nextFrameTime;
	private int nextFrameCurrentTime;

	private L9Player player;

	/**
	 * 图片对象
	 */
	protected MyImg objImg;
	protected Image bufImg;

	/**
	 * 是否使用先绘制后截取的方式对其进行绘制局部刷新
	 */
	private boolean is_drawPartBuf;

	/**
	 * 图片分隔
	 */
	private int[] tileNum = new int[2];
	protected int tileX;
	protected int tileY;
	protected int tileW;
	protected int tileH;

	private boolean is_addToEng;

	private boolean isPlay;

	private boolean isOrien;// 是否翻转

	public long id;

	public int depth;

	private int trans;

	/**
	 * 如果根据objKey拿到的值是.png结尾的话，则加载图片,若根据.anu结尾的话，则加载anu文件 若
	 * 
	 * @param fatherObject
	 * @param drawRect
	 * @param colRect
	 * @param objKey
	 * @param objType
	 * @param engine
	 */
	public L9Object(L9Object fatherObject, String objKey, int objType, int x,
			int y, int loopOffSet, int animation, boolean is_drawPartBuf,
			boolean is_addToEng, boolean isPlay, int depth) {
		this(fatherObject, objKey, objType, x, y, loopOffSet, animation,
				is_drawPartBuf, is_addToEng, isPlay, depth, 0);
	}

	public L9Object(L9Object fatherObject, String objKey, int objType, int x,
			int y, int loopOffSet, int animation, boolean is_drawPartBuf,
			boolean is_addToEng, boolean isPlay, int depth, int trans) {

		this.fatherObject = fatherObject;
		this.objKey = objKey;
		this.objType = objType;
		this.posX = x;
		this.posY = y;
		this.is_drawPartBuf = is_drawPartBuf;
		this.is_addToEng = is_addToEng;
		this.isPlay = isPlay;
		this.depth = depth;
		this.trans = trans;
		is_useAnimation = true;
		initPlayerObject(objKey, loopOffSet, animation);
		is_drawUpdate = true;
		if (is_addToEng) {
			addToEng();
		}
	}

	/**
	 * 创建以图片为主的新对象，初始化对象得画图深度，上一层，对象type 对象数据文件对应的key，每一帧相同图片大小的宽度和高度，初始坐标，
	 * 是否是循环播放动画 该对象创建后会被保存到GameScreen中，主要逻辑doAllFrame()统一执行在gameScreen中
	 * 在GameScreen中，执行主要逻辑时对该对象先统一进行beforFrame()的执行，然后进行doFrame()的行为
	 * 当所有对象得doFrame执行完成后对对象执行finishFrame()方法， 再对进行重新一次的删除，将需要删除的对象在列表里删除
	 * 
	 * @param father:上一层对象
	 * @param objType:对象类型:对其画图层次有关
	 * @param objKey:对象名
	 * @param tileW:对象宽度
	 * @param tileH:对象高度
	 * @param x:对象初始X位置
	 * @param y:对象初始Y位置
	 * @param loopOffSet:是否循环播放
	 * @param is_drawPartBuf：是否用缓冲图片进行局部刷新
	 * @param is_addToEng:是否添加到引擎中，为未添加到引擎，则需要手动调用各类方法
	 */
	public L9Object(L9Object father, int objType, String objKey, int tileW,
			int tileH, int x, int y, int loopOffSet, boolean is_drawPartBuf,
			boolean is_addToEng, boolean isPlay, int depth) {
		this(father, objType, objKey, tileW, tileH, x, y, loopOffSet,
				is_drawPartBuf, is_addToEng, isPlay, depth, 0);
	}

	public L9Object(L9Object father, int objType, String objKey, int tileW,
			int tileH, int x, int y, int loopOffSet, boolean is_drawPartBuf,
			boolean is_addToEng, boolean isPlay, int depth, int trans) {
		this.fatherObject = father;
		this.objType = objType;
		this.objKey = objKey;
		this.tileW = tileW;
		this.tileH = tileH;
		this.posX = x;
		this.posY = y;
		this.loopOffSetNum = loopOffSet;
		this.is_drawPartBuf = is_drawPartBuf;
		this.is_addToEng = is_addToEng;
		this.isPlay = isPlay;
		this.depth = depth;
		this.trans = trans;
		is_useAnimation = false;
		is_drawUpdate = true;
		initImgObject(objKey, tileW, tileH, x, y, loopOffSet);
		if (is_addToEng) {
			// 更新到引擎中
			addToEng();
		}

	}

	/**
	 * 加载图片图片信息等
	 * 
	 * @param objKey
	 * @param tileW
	 * @param tileH
	 * @param x
	 * @param y
	 */
	private void initImgObject(String objKey, int tileW, int tileH, int x,
			int y, int loopOffSet) {
		if (objKey != null) {
			objImg = this.trans != 0 ? new MyImg(objKey, this.trans)
					: new MyImg(objKey);
			tileNum[0] = objImg.getWidth() / tileW;
			tileNum[1] = objImg.getHeight() / tileH;
		}
		tileX = 0;
		tileY = 0;
		is_drawUpdate = true;
		frameCount = tileNum[0] * tileNum[1];
		setLoopOffSet(loopOffSet);
		setNextFrameTime(1);
		drawRect = new JGRectangle();
		lastDrawRect = new JGRectangle();
		colRect = new JGRectangle();
		drawRect.x = x;
		drawRect.y = y;
		drawRect.width = tileW;
		drawRect.height = tileH;
		colRect.x = x - tileW / 2;
		colRect.y = y - tileH;
		colRect.width = tileW;
		colRect.height = tileH;
	};

	private void initPlayerObject(String objKey, int loopOffset, int animation) {
		player = new L9Player(objKey, this, posX, posY, this.trans);
		setNextFrameTime(1);
		player.setAnimation(animation);
		if (loopOffset != -1) {
			player.setLoopOffSet(-1);
			setFrame(loopOffset);
		} else {
			player.setLoopOffSet(0);
		}
		drawRect = new JGRectangle();
		colRect = new JGRectangle();
		lastDrawRect = new JGRectangle();
		is_drawUpdate = true;
		frameCount = player.getFrameCount();
	}

	protected void addToEng() {
		objId++;
		this.id = objId;
		L9EngineLogic.getInstance().markAddObject(this);
		// 添加到引擎中
	}

	/**
	 * 和对象碰撞后的逻辑
	 */
	public void hit(L9Object obj) {

	}

	/**
	 * 游戏逻辑
	 */
	protected void doFrame() {

	}

	public void doAllFrame() {
		if (isOver) {
			L9EngineLogic.getInstance().markRemoveObject(this);
			return;
		}
		beforeFrame();
		if (isPlay) {
			animationUpdate();
			if (isOver) {
				return;
			}
		}
		doFrame();
		if (!is_addToEng) {
			frameFinished();
		}
	}

	/**
	 * 游戏执行过程中人物动画的播放逻辑 更新人物动画
	 */
	public void animationUpdate() {
		if (loopOffSetNum == -1) { // 如果是循环播放
			nextFrameCurrentTime += 1;
			if (nextFrameCurrentTime >= nextFrameTime) { // 如果到了下一帧
				nextFrameCurrentTime = 0;
				if (!is_useAnimation) { // 如果是播放图片的动画
					currentFrame++;
					if (currentFrame >= frameCount) {
						currentFrame = 0;
					}
					setFrame(currentFrame);
				} else {
					player.update();
					currentFrame = player.getCurrentFrame();
				}
			}
		} else {
			nextFrameCurrentTime++;
			if (nextFrameCurrentTime >= nextFrameTime) { // 如果到了下一帧
				nextFrameCurrentTime = 0;
				if (!is_useAnimation) {
					currentFrame++;
					if (currentFrame >= frameCount) {
						currentFrame = 0;
						loopOffSetNum = loopOffSetNum - 1;
						if (loopOffSetNum == 0) {
							endAnimation();
							isPlay = false;
						}
					}
					setFrame(currentFrame);
				} else {
					player.update();
					currentFrame = player.getCurrentFrame();
				}
			}
		}

		is_drawUpdate = true;
	}

	/**
	 * 画图
	 */
	public void paintFrame(Graphics g) {
		drawFrame(g);
	}

	/**
	 * 画动画：当是使用动作编辑器时
	 * 
	 * @param g
	 */
	protected void drawPlayer(Graphics g) {
		player.paint(g);
	}

	/**
	 * 当使用图片时
	 * 
	 * @param g
	 */
	protected void drawImg(Graphics g) {
		// int x = g.getClipX();
		// int y = g.getClipY();
		// int w=g.getClipWidth();
		// int h=g.getClipHeight();
		// g.translate(posX-tileW/2,posY-tileH);
		// g.setClip(tileX*tileW, tileY*tileH, tileW, tileH);
		// objImg.drawImage(g, 0, 0, 0);
		// g.translate(-(posX-tileW/2),-(posY-tileH));
		// g.setClip(x, y, w, h);

		if (objKey != null) {
			if (getSpriteOrientation() == 0) {
				objImg.drawRegion(g, tileW * tileX, tileH * tileY, tileW,
						tileH, 0, posX, posY, Graphics.HCENTER
								| Graphics.BOTTOM);
			} else {
				objImg.drawRegion(g, tileW * tileX, tileH * tileY, tileW,
						tileH, Sprite.TRANS_MIRROR, posX, posY,
						Graphics.HCENTER | Graphics.BOTTOM);
			}
		} else {
			g.setColor(0xff0000);
			g.fillRect(posX - tileW / 2, posY - tileH, tileW, tileH);
		}
	}

	public void drawColAndDraw(Graphics g) {
		if (L9Config.bShowObjectColAndDraw) {
			JGRectangle myColRect = new JGRectangle();
			setColBox(myColRect);
			g.setColor(0xff0000);
			g.drawRect(myColRect.x, myColRect.y, myColRect.width,
					myColRect.height);
			g.setColor(0x00ff00);
			g.drawRect(drawRect.x - drawRect.width / 2, drawRect.y
					- drawRect.height, drawRect.width, drawRect.height);
			g.setColor(0x0000ff);
			g.drawRect(lastDrawRect.x - lastDrawRect.width / 2, lastDrawRect.y
					- lastDrawRect.height, lastDrawRect.width,
					lastDrawRect.height);
		}
	}

	/**
	 * 画缓冲图片
	 * 
	 * @param g
	 * @param imgX
	 * @param imgY
	 * @param imgW
	 * @param imgH
	 * @param x
	 * @param y
	 */
	public void drawBufPartImg(Graphics g, int imgX, int imgY, int imgW,
			int imgH, int x, int y) {
		// bufImg.drawRegion(g, imgX, imgY, imgW, imgH, 0, x, y, 0);
		// int cx = g.getClipX();
		// int cy = g.getClipY();
		// int cw = g.getClipWidth();
		// int ch = g.getClipHeight();
		// g.translate(x, y);
		// g.setClip(imgX, imgY, imgW, imgH);
		// drawBufImg(g);
		// g.translate(-x, -y);
		// g.setClip(cx, cy, cw, ch);
		g.drawRegion(bufImg, imgX, imgY, imgW, imgH, 0, x, y, Graphics.BOTTOM
				| Graphics.HCENTER);
	}

	public void drawBufImg(Graphics g) {
		// bufImg.drawImage(g,posX , posY, 0);
		// System.out.println("画背景了、、、、、、");
		g.drawImage(bufImg, posX, posY, Graphics.BOTTOM | Graphics.HCENTER);
	}

	/**
	 * 画当前帧的部分信息
	 * 
	 * @param g
	 * @param ux
	 * @param uy
	 * @param uw
	 * @param uh
	 */
	public void drawUpdate(Graphics g, int ux, int uy, int uw, int uh) {
		// JGRectangle jg1 = new JGRectangle(ux-uw/2,uy-uh,uw,uh);
		JGRectangle jg2 = new JGRectangle(drawRect.x - drawRect.width / 2,
				drawRect.y - drawRect.height, drawRect.width, drawRect.height);
		JGRectangle bufRect = jg2.intersection(ux - uw / 2, uy - uh, uw, uh);
		int x = drawRect.x - drawRect.width / 2;
		int y = drawRect.y - drawRect.height;
		if (!is_drawPartBuf) {
			if (bufRect.width > 0 && bufRect.height > 0) {
				objImg.drawRegion(g, bufRect.x - (posX - tileW / 2), bufRect.y
						- (posY - tileH), bufRect.width, bufRect.height, 0, x
						+ bufRect.x, y + bufRect.y, 0);
			}
		} else {
			if (bufRect.width > 0 && bufRect.height > 0) {
				drawBufPartImg(g, bufRect.x, bufRect.y, bufRect.width,
						bufRect.height, x + bufRect.x + bufRect.width / 2, y
								+ bufRect.y + bufRect.height);
			}
		}
	}

	protected void drawNull(Graphics g, int ux, int uy, int uw, int uh) {
		int x = ux - uw / 2;
		int y = uy - uh;
		g.setColor(0xffffff);
		g.fillRect(x, y, uw, uh);
	}

	/**
	 * 画当前坐标位置
	 * 
	 * @param g
	 */
	protected void drawFrame(Graphics g) {
		drawColAndDraw(g);
		if (is_alive && is_drawUpdate && !isOver) {
			if (is_drawPartBuf) {
				drawBufImg(g);
			} else {
				if (is_useAnimation) {
					drawPlayer(g);
				} else {
					drawImg(g);
				}
			}
			// is_drawUpdate = false;
		}
	}

	public void drawPart(Graphics g) {
		if (is_alive && is_drawUpdate) {
			if (fatherObject != null) {
				fatherObject.drawUpdate(g, lastDrawRect.x, lastDrawRect.y,
						lastDrawRect.width, lastDrawRect.height);
			} else {
				drawNull(g, lastDrawRect.x, lastDrawRect.y, lastDrawRect.width,
						lastDrawRect.height);
			}
		}
	}

	/**
	 * 帧数结束后的逻辑
	 */
	public void frameFinished() {
		// 逻辑结束后设置他上次画图的坐标和范围
		setDrawRect();
		if (L9Config.bUseDrawPart && is_drawPartBuf) {
			// createBufImg();
		}
	}

	public void createBufImg() {
	}

	public boolean isOver;

	public void setOver(boolean isOver) {
		this.isOver = isOver;
	}

	/**
	 * 执行本帧之前的行为
	 */
	public void beforeFrame() {
		if (!L9Config.bUseDrawPart) {
			is_drawUpdate = true;
			return;
		}
		if (!lastDrawRect.equals(drawRect)) {
			lastDrawRect.x = drawRect.x;
			lastDrawRect.y = drawRect.y;
			lastDrawRect.width = drawRect.width;
			lastDrawRect.height = drawRect.height;
			is_drawUpdate = true;
		} else {
			is_drawUpdate = false;
		}

		// if(isSuspend()){
		// L9EngineLogic.getInstance().markAddObject(this);
		// }
		// bufImg = null;
	}

	/**
	 * 结束动画时的逻辑，记得要重写,在L9Player中的endAnimation()方法中调用
	 * 
	 */
	public void endAnimation() {
	}

	/**
	 * 更新动画位置,在L9Player中的updateSpritePosition(int deltaX, int deltaY)方法中调用
	 */
	public void updateSpritePosition(int deltaX, int deltaY) {
	}

	protected void destroy() {
		if (is_useAnimation) {
			player.clear();
		} else {
			if (objImg != null) {
				objImg.release();
			}
		}
		objImg = null;
		bufImg = null;
	};

	public void removeDone() {
		is_alive = false;
		isOver = true;
		destroy();
	}

	public void remove() {

	}

	/**
	 * 动画是否翻转 MSprite.ORIENTATION_FLIP_H
	 * 
	 */
	public byte getSpriteOrientation() {
		if (this.isOrien) {
			return MSprite.ORIENTATION_FLIP_H;
		} else {
			return 0;
		}
	}

	public void setSpriteOrientation(boolean isOrien) {
		this.isOrien = isOrien;
	}

	public int getXSpeed() {
		return xSpeed;
	}

	public void setXSpeed(int speed) {
		xSpeed = speed;
	}

	public int getYSpeed() {
		return ySpeed;
	}

	public void setYSpeed(int speed) {
		ySpeed = speed;
	}

	public int getObjType() {
		return objType;
	}

	public void setObjType(int objType) {
		this.objType = objType;
	}

	public JGRectangle getLastDrawRect() {
		return lastDrawRect;
	}

	public void setLastDrawRect(JGRectangle lastDrawRect) {
		this.lastDrawRect = lastDrawRect;
	}

	public JGRectangle getDrawRect() {
		return drawRect;
	}

	public void setDrawRect(JGRectangle drawRect) {
		this.drawRect = drawRect;
	}

	public JGRectangle getLastColRect() {
		return lastColRect;
	}

	public void setLastColRect(JGRectangle lastColRect) {
		this.lastColRect = lastColRect;
	}

	public JGRectangle getColRect() {
		return colRect;
	}

	public void setColRect(JGRectangle colRect) {
		this.colRect = colRect;
	}

	public boolean isAlive() {
		return is_alive;
	}

	public void setAlive(boolean b_alive) {
		this.is_alive = b_alive;
	}

	/**
	 * 对象是否暂停
	 * 
	 * @return
	 */
	public boolean isSuspend() {
		return is_suspended;
	}

	/**
	 * 设置暂停
	 */
	public void setSuspended() {
		this.is_suspended = true;
	}

	/**
	 * 设置为使用
	 */
	public void setResume() {
		this.is_suspended = false;
	}

	public String getObjKey() {
		return objKey;
	}

	/**
	 * 设置画图位置 逻辑执行完后在画图之前调用
	 */
	protected void setDrawRect() {
		if (is_useAnimation) {
			drawRect.x = player.getDrawRect().x;
			drawRect.y = player.getDrawRect().y;
			drawRect.width = player.getDrawRect().width;
			drawRect.height = player.getDrawRect().height;
			colRect.x = player.getColRect().x;
			colRect.y = player.getColRect().y;
			colRect.width = player.getColRect().width;
			colRect.height = player.getColRect().height;
		} else {
			drawRect.x = posX;
			drawRect.y = posY;
			// drawRect.width = colRect.width;
			// drawRect.height = colRect.height;
		}
	}

	public boolean setColBox(JGRectangle bBox) {
		if (is_useAnimation) {
			if ((colRect = player.getColRect()).width == 0) {
				colRect = player.getRectByType(L9Player.drawType);
			}
		}
		if (this.colRect != null && (colRect.width > 0 && colRect.height > 0)) {
			bBox.setRect(colRect.x, colRect.y, colRect.width, colRect.height);
			return true;
		}
		return false;
	}

	public boolean setAtkBox(JGRectangle bBox) {
		return setColBox(bBox);
	}

	public int getPosX() {
		return posX;
	}

	public void setPosX(int posX) {
		this.posX = posX;
		this.colRect.x = posX - getColRect().width / 2;
	}

	public int getPosY() {
		return posY;
	}

	public void setPosY(int posY) {
		this.posY = posY;
		this.colRect.y = posY - getColRect().height;
	}

	public boolean isIs_alive() {
		return is_alive;
	}

	public void setIs_alive(boolean is_alive) {
		this.is_alive = is_alive;
	}

	public boolean isIs_suspended() {
		return is_suspended;
	}

	public void setIs_suspended(boolean is_suspended) {
		this.is_suspended = is_suspended;
	}

	public boolean isIs_drawUpdate() {
		return is_drawUpdate;
	}

	public void setIs_drawUpdate(boolean is_drawUpdate) {
		this.is_drawUpdate = is_drawUpdate;
	}

	public boolean isIs_useAnimation() {
		return is_useAnimation;
	}

	public void setIs_useAnimation(boolean is_useAnimation) {
		this.is_useAnimation = is_useAnimation;
	}

	public void setAnimation(int index) {
		if (this.is_useAnimation) {
			player.setAnimation(index);
		}
	}

	/**
	 * 获得当前动画的当前帧数
	 * 
	 * @return
	 */
	public int getCurrentFrame() {
		if (is_useAnimation) {
			return player.getCurrentFrame();
		} else {
			return currentFrame;
		}
	}

	public int getAnimation() {
		if (is_useAnimation) {
			return player.getAnimation();
		}
		return 0;
	}

	public void setNextFrameTime(int nextFrameTime) {
		this.nextFrameTime = nextFrameTime;
		// nextFrameCurrentTime = 0;
	}

	// 如果画图时调用该方法，则画图时的判定框和实际的判定框不符合
	public void setFrame(int frame) {
		if (objKey == null) {
			return;
		}
		if (!is_useAnimation) {
			if (frame > frameCount) {
				System.out.println("Error>>>动画帧越界");
				return;
			}
			this.currentFrame = frame;
			tileX = currentFrame % tileNum[0];
			tileY = currentFrame / tileNum[0];
			setNextFrameTime(nextFrameTime);
		} else {
			player.setFrame(frame);
		}

	}

	public void setLoopOffSet(int loopOffSet) {
		if (!is_useAnimation) {
			this.loopOffSetNum = loopOffSet;
			if (loopOffSet == -1) {
				setFrame(0);
			} else if (loopOffSetNum < frameCount) {
				setFrame(loopOffSet);
			} else {
				setFrame(0);
			}
		} else {
			player.setLoopOffSet(loopOffSet);
		}
	}

	public int getLoopOffSet() {
		return this.loopOffSetNum;
	}

	public void setDrawWH(int w, int h) {
		this.drawRect.width = w;
		this.drawRect.height = h;
	}

	public void setColWH(int x, int y, int w, int h) {
		this.colRect.x = x;
		this.colRect.y = y;
		this.colRect.width = w;
		this.colRect.height = h;
	}

	public int getTileW() {
		return tileW;
	}

	public int getTileH() {
		return tileH;
	}

	public void setIsPlay(boolean isPlay) {
		this.isPlay = isPlay;
	}

	public boolean isPlay() {
		return isPlay;
	}

	public String toString() {
		return "class:" + this.getClass().getName() + ",l9object:" + id + ";";
	}

	public int getFrameCount() {
		if (is_useAnimation) {
			return player.getFrameCount();
		} else {
			return frameCount;
		}

	}

	public L9Player getPlayer() {
		return this.player;
	}

	public int getTrans() {
		return trans;
	}
}
