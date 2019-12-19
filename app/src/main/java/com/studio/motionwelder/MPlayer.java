/*
 * COPYRIGHT - MOTIONWELDER
 */
package com.studio.motionwelder;

import android.util.Log;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Sprite;

import com.game.lib9.Engine;
import com.game.lib9.L9Config;
import com.game.mouse.screen.MainMidlet;


public abstract class MPlayer {
	
	private MSpriteData data;
	
	/** Current Animation */
	private int animation;
	
	/** Current Frame */
	private int frame;
	
	/** Frame Count */
	private int frameCount;
	
	private int loopOffset;
	
	/** Delay Count */
	private int delayCount;
	
	private int framePoolPointer;
	
//	private boolean isPlaying;
	
	private int[] rect = new int[4];
	
	private final byte IMAGE_FLAG_NONE				= 0x00; // 0000 0000
	private final byte IMAGE_FLAG_HFLIP				= 0x02; // 0000 0010 
	private final byte IMAGE_FLAG_VFLIP				= 0x04; // 0000 0100
	
	private final byte ELLIPSE_FLAG_NONE				= 0x01; // 0000 0001
	private final byte ELLIPSE_FLAG_FILLED			= 0x03; // 0000 0011
	
	private final byte LINE_FLAG 						= 0x05; // 0000 0101 
	
	private final byte RECTANGLE_FLAG_NONE			= 0x07; // 0000 0111
	private final byte RECTANGLE_FLAG_FILLED			= 0x09; // 0000 1001
	
	private final byte ROUNDEDRECTANGLE_FLAG_NONE 	= 0x0b; // 0000 1011
	private final byte ROUNDEDRECTANGLE_FLAG_FILLED 	= 0x0d; // 0000 1101
	
	private final byte POSITIONERRECTANGLE_FLAG 		= 0x0f; // 0000 1111
	
	/*
	 * 分辨率比例
	 */
//	private float sx =(float)(Engine.initMix()[0]/L9Config.SCR_W.0);
//	private float sy =(float)(Engine.initMix()[1]/L9Config.SCR_H.0);
	
	private float sx2 =(float)(Engine.initMix()[0]/1280.0);
	private float sy2 =(float)(Engine.initMix()[1]/720.0);
	/**
	 * @param data sprite data to be played
	 */
	public MPlayer(MSpriteData data){
		this.data = data;
	}
	
	/**
	 * Sets Animation
	 * @param id Sets player to play animation referring to this id.
	 */
	public void setAnimation(int id){
		animation = id;
		
		int pos = (animation<<1);
		frameCount = (data.animationTable[pos+1] - data.animationTable[pos] +1);
		Log.v("FrameCount","frameCount = " + data.animationTable.length);
		setFrame(0);
		notifyStartOfAnimation();
	}
	
	/**
	 * @return animtion id for the current animation of a player
	 */
	public int getAnimation(){
		return animation;
	}
	
	/**
	 * @return frameCount for current animation
	 */
	public int getFrameCount(){
		return frameCount;
	}
	
	/**
	 * @return current frame that is played
	 */
	public int getCurrentFrame(){
		return frame;
	}
	
	/**
	 * Sets Frame
	 * @param frame sets the player to this frame
	 */
	public void setFrame(int frame){
		this.frame = frame;
		delayCount = 0;
		int frameIndex = data.animationTable[animation<<1] +frame;
		framePoolPointer = data.frameTable[(frameIndex<<2)];
	}
	
	
	public void setLoopOffset(int val){
		this.loopOffset = val;
	}
	
	/**
	 * @return Animation Count
	 */
	public int getAnimationCount(){
		return (data.animationTable.length>>>1);
	}
	
	
	/**
	 * @return Number of Collisiton rect for current frame
	 */
	
	public int getNumberOfCollisionRect(){
		int count=0;
		int startIndex = data.frameTableIndex[framePoolPointer<<1];
		int endIndex = data.frameTableIndex[(framePoolPointer<<1)+1];
		while(startIndex<endIndex){
			startIndex+=3;
			if((byte)data.framePoolTable[startIndex++]==POSITIONERRECTANGLE_FLAG){
				count++;
			}
		}
		
		return count;
	}
	
	/**
	 * @return Returns Collision Rect at given index
	 */
	public int[] getCollisionRect(int index){
		int count=-1;
		int startIndex = data.frameTableIndex[framePoolPointer<<1];
		int endIndex = data.frameTableIndex[(framePoolPointer<<1)+1];
		while(startIndex<endIndex){
			int clipIndex = data.framePoolTable[startIndex++];
			int x = data.framePoolTable[startIndex++]; // + sprite.getSpriteX()
			int y = data.framePoolTable[startIndex++]; // + sprite.getSpriteY()
			if((byte)data.framePoolTable[startIndex++]==POSITIONERRECTANGLE_FLAG){
				count++;
				if(count==index){
					clipIndex=clipIndex<<1;
					rect[2] = data.positionerRectangleClipPool[clipIndex];
					rect[3] = data.positionerRectangleClipPool[clipIndex+1];
					// changing x and y
					byte spriteOrientation = getSpriteOrientation();
					if(spriteOrientation==1){// flip h
						x = -x-rect[2];
					} else if(spriteOrientation==2){// flip v
						y = -y-rect[3];
					}
					rect[0] = x;
					rect[1] = y;

					return rect;
				}
			}
		}
		
		return null;
	}

	
	/**  
	 * This method will update sprite to next frame  
	 */
	
	public void update(){
		int frameIndex = data.animationTable[animation<<1] +frame;
		if(delayCount<data.frameTable[(frameIndex<<2) +1]){
			delayCount++;
			return;
		}
		
		// check for end of animation
		if(frame>=frameCount-1){
			if(loopOffset<0){
				notifyEndOfAnimation();
//				isPlaying = false;
//				if(sprite!=null)
//					sprite.endOfAnimation();
				return;
			} else {
				frame = loopOffset-1;
			}
		}
		
		setFrame(frame+1);
		frameIndex = data.animationTable[animation<<1] +frame;
		
		int xInc = data.frameTable[(frameIndex<<2) +2];
		int yInc = data.frameTable[(frameIndex<<2) +3];
//		data.frameTable[(frameIndex<<2) +2]
//		data.frameTable[(frameIndex<<2) +3]
//		if(sprite!=null)
//			sprite.updateSpritePosition(getSpriteOrientation()==1?-xInc:xInc,getSpriteOrientation()==2?-yInc:yInc);
//		else{
//			spriteX+=getSpriteOrientation()==1?xInc:-xInc;
//			spriteY+=getSpriteOrientation()==2?yInc:-yInc;
//		}
		updateSpritePositionBy(getSpriteOrientation()==1?-xInc:xInc,getSpriteOrientation()==2?-yInc:yInc);
		delayCount++;
	}
	
/*
 * 
 *       [CLIP INDEX][X][Y][FLAG]
 *       [CLIP INDEX][X][Y][FLAG]   - FRAME 0
 *       [CLIP INDEX][X][Y][FLAG]
 */
	/**
	 *  @param g Graphics object on which frame is rendered
	 */
	public void drawFrame(Graphics g){
		int startIndex = data.frameTableIndex[framePoolPointer<<1];
		int endIndex = data.frameTableIndex[(framePoolPointer<<1)+1];
		
		int clipX = g.getClipX() - g.getTranslateX();
		int clipY = g.getClipY() - g.getTranslateY();
		int clipW = g.getClipWidth();
		int clipH = g.getClipHeight();
		
		while(startIndex<endIndex){
			int clipIndex = data.framePoolTable[startIndex++];
			int x = data.framePoolTable[startIndex++]; // + sprite.getSpriteX()
			int y = data.framePoolTable[startIndex++]; // + sprite.getSpriteY()
			byte flag = (byte)data.framePoolTable[startIndex++];
			drawClip(g,x,y,clipIndex,flag);
			
			// reset the clip position
//			System.out.println("----------"+clipX+","+clipY+","+clipW+","+clipH);
			g.setClip((int)(clipX * MainMidlet.scaleX),(int)(clipY * MainMidlet.scaleY),(int)(clipW * MainMidlet.scaleX),(int)(clipH * MainMidlet.scaleY));
		}
	}
	
	protected void drawClip(Graphics g,int x,int y,int clipIndex,byte flag){
//		byte type = (byte)(flag&0x0f);
		// check for image type flag
		if((flag&0x01)==0){
			byte imageId = (byte)((flag&0xf8)>>3);
			byte orientation = (byte)(flag&0x07);
			orientation=(byte)(orientation>>1);
//			if(L9Config.spriteUseClip){
//				drawImageClip(g,x,y,imageId,clipIndex,orientation);
//			}else{
				drawImageClip1(g,x,y,imageId,clipIndex,orientation);
//			}
		} else if(flag == ELLIPSE_FLAG_NONE || flag == ELLIPSE_FLAG_FILLED) {
			//[w][h][startAngle][endAngle][color]
			int index = clipIndex*5;
			drawEllipseClip(g,x,y,data.ellipseClipPool[index],data.ellipseClipPool[index+1],data.ellipseClipPool[index+2],data.ellipseClipPool[index+3],data.ellipseClipPool[index+4],flag == ELLIPSE_FLAG_FILLED);
		} else if(flag ==LINE_FLAG) {
			 //[x2][y2][color]
			int index = clipIndex*3;
			drawLineClip(g,x,y,data.lineClipPool[index],data.lineClipPool[index+1],data.lineClipPool[index+2]);
		} else if(flag == RECTANGLE_FLAG_NONE || flag == RECTANGLE_FLAG_FILLED) {
			//[w][h][color]
			int index = clipIndex*3;
			drawRectangleClip(g,x,y,data.rectangleClipPool[index],data.rectangleClipPool[index+1],data.rectangleClipPool[index+2],flag == RECTANGLE_FLAG_FILLED);
		} else if(flag == ROUNDEDRECTANGLE_FLAG_NONE || flag == ROUNDEDRECTANGLE_FLAG_FILLED) {
			//[w][h][arcwidth][archeight][color]
			int index = clipIndex*5;
			drawRoundedRectangleClip(g,x,y,data.roundedRectangleClipPool[index],data.roundedRectangleClipPool[index+1],data.roundedRectangleClipPool[index+2],data.roundedRectangleClipPool[index+3],data.roundedRectangleClipPool[index+4],flag == ROUNDEDRECTANGLE_FLAG_FILLED);
		}
	}
	
/*	
 sprite oritn->			| 0   1   2
 					 ---------------				
 clip oritn			  0 | 0   1   2
 					  1 | 1   0   3
 					  2 | 2   3   0
 	         
 	         0 - no orientation
 	         1 - flip H
 	         2 - flip V
 	         3 - rotate 180 - NOT RECOMENDED
*/	
	
	// please don't use this if you are using Nokia Direct Graphics, instead use one below this.  
	protected void drawImageClip(Graphics g,int x,int y,byte imageId,int clipIndex,byte orientation){
		
		int index = clipIndex*4;
		int clipX = data.imageClipPool[index++];
		int clipY = data.imageClipPool[index++];
		int clipW = data.imageClipPool[index++];
		int clipH = data.imageClipPool[index++];
		
		byte spriteOrientation = getSpriteOrientation();
		
		if(orientation == spriteOrientation){ // if user have same operation as wat clip is having.. than.. flip&flip = normal
			orientation = 0;
		} else if(orientation==0 || spriteOrientation==0){
			orientation = (byte)(orientation + spriteOrientation); // take non zero value	
		} else {
			//orientation = 3;
			System.out.println("FLIP H and FLIP V, cannot be used at a same time, use your own implementation");
			return;
		}
		
		// changing x and y
		if(spriteOrientation==1){// flip h
			x = -x-clipW;
		} else if(spriteOrientation==2){// flip v
			y = -y-clipH;
		}
		
		
		// render image based on whether it is cliped or not  
		if(data.splitImageClips){
			Image img = ((Image[][])data.imageVector.elementAt(imageId))[clipIndex-data.imageIndexTable[imageId]][orientation];
			
			int xPos = x + getSpriteDrawX();
			int yPos = y + getSpriteDrawY();
			
			g.drawImage(img,xPos,yPos,20);
		} else {
			Image[] imageArr = (Image[])data.imageVector.elementAt(imageId);
			
			if(orientation==1){ // flip h
				clipX = imageArr[0].getWidth() - clipW - clipX;
			} else if(orientation==1){
				clipY = imageArr[0].getHeight() - clipH - clipY;
			}
			
			int xPos = x + getSpriteDrawX();
			int yPos = y + getSpriteDrawY();
			g.clipRect(xPos,yPos,clipW,clipH);
			
			g.drawImage(imageArr[orientation],xPos-clipX,yPos-clipY,20);
		}
	}
	
protected void drawImageClip1(Graphics g,int x,int y,byte imageId,int clipIndex,byte orientation){
		
		int index = clipIndex*4;
		int clipX = data.imageClipPool[index++];
		int clipY = data.imageClipPool[index++];
		int clipW = data.imageClipPool[index++];
		int clipH = data.imageClipPool[index++];
		
		byte spriteOrientation = getSpriteOrientation();
//		System.out.println("orie = "+orientation);
		if(orientation == spriteOrientation){ // if user have same operation as wat clip is having.. than.. flip&flip = normal
			orientation = 0;
		} else if(orientation==0 || spriteOrientation==0){
			orientation = (byte)(orientation + spriteOrientation); // take non zero value	
		} else {
			//orientation = 3;
			System.out.println("FLIP H and FLIP V, cannot be used at a same time, use your own implementation");
			return;
		}
		
		
		// changing x and y
		if(spriteOrientation==1){// flip h
			x = -x-clipW;
		} else if(spriteOrientation==2){// flip v
			y = -y-clipH;
		}
		
		
		// render image based on whether it is cliped or not  
		if(data.splitImageClips){
			Image img = ((Image[][])data.imageVector.elementAt(imageId))[clipIndex-data.imageIndexTable[imageId]][orientation];
			
			int xPos = x + getSpriteDrawX();
			int yPos = y + getSpriteDrawY();
//			System.out.println("画图。。。。。。。。。。。。。。。。。。。。。。");
//			g.drawImage(img,xPos,yPos,20);
//			g.drawRegion(img, clipX, clipY, clipW, clipH, 0, xPos, yPos, 20);
			
			g.drawRegion(img, (int)(sx2*clipX), (int)(sy2*clipY), (int)(sx2*clipW), (int)(sy2*clipH), 0, (int)(xPos*sx2), (int)(yPos*sy2), 20);
		} else {
			Image[] imageArr = (Image[])data.imageVector.elementAt(imageId);
//			System.out.println("ori2 = "+orientation);
//			if(orientation==1){ // flip h
//				clipX = imageArr[0].getWidth() - clipW - clipX;
//			} else if(orientation==1){
//				clipY = imageArr[0].getHeight() - clipH - clipY;
//			}
//			if(orientation==1){ // flip h
//				clipX = imageArr[0].getWidth() - clipW - clipX;
//				clipY = imageArr[0].getHeight() - clipH - clipY;
//			}
			
			int xPos = x + getSpriteDrawX();
			int yPos = y + getSpriteDrawY();
			
			int trans = 0;
			if(getSpriteOrientation() == MSprite.ORIENTATION_NONE){
				if(orientation == 0){
					trans = 0;
					
				}else if (orientation == 1){
					trans = Sprite.TRANS_MIRROR;
					
				}else if(orientation == 2){
					trans = Sprite.TRANS_MIRROR_ROT180;
				}
			}else if(getSpriteOrientation() == MSprite.ORIENTATION_FLIP_H ){
				if(orientation == 0){
					trans = 0;
				}else if (orientation == 1){
					trans = Sprite.TRANS_MIRROR;
//					System.out.println("ssssssssssssssssssssssssss");
				}else if(orientation == 2){
					trans = Sprite.TRANS_MIRROR_ROT180;
				}
			}else if(getSpriteOrientation() == MSprite.ORIENTATION_FLIP_V || orientation == MSprite.ORIENTATION_FLIP_V){
				trans = Sprite.TRANS_MIRROR_ROT180;
			}
			//图片 图片的x坐标  y坐标 图片宽度,长度,是否翻转,画布上位置的x坐标,y坐标,布局方式
			g.drawRegion(imageArr[0], (int)(clipX*sx2), (int)(sy2*clipY), (int)(sx2*clipW), (int)(sy2*clipH), trans, (int)(xPos*sx2), (int)(yPos*sy2), 20);
//			g.drawRegion(imageArr[0], clipX, clipY, clipW, clipH, trans, xPos, yPos, 20);
		}
	}
	
	/**
	 *  USE THIS DRAW METHOD, IF YOU ARE USING NOKIA DIRECT GRAPHICS
	 */
	/*
	protected void drawImageClip(Graphics g,int x,int y,byte imageId,int clipIndex,byte orientation){
		
		int index = clipIndex*4;
		int clipX = data.imageClipPool[index++];
		int clipY = data.imageClipPool[index++];
		int clipW = data.imageClipPool[index++];
		int clipH = data.imageClipPool[index++];
		
		byte spriteOrientation = getSpriteOrientation();
		
		if(orientation == spriteOrientation){ // if user have same operation as wat clip is having.. than.. flip&flip = normal
			orientation = 0;
		} else if(orientation==0 || spriteOrientation==0){
			orientation = (byte)(orientation + spriteOrientation); // take non zero value	
		} else {
			//orientation = 3;
			System.out.println("FLIP H and FLIP V, cannot be used at a same time, use your own implementation");
			return;
		}
		
		// changing x and y
		if(spriteOrientation==1){// flip h
			x = -x-clipW;
		} else if(spriteOrientation==2){// flip v
			y = -y-clipH;
		}
		
		
		// render image based on whether it is cliped or not  
		if(data.splitImageClips){
			Image img = ((Image[][])data.imageVector.elementAt(imageId))[clipIndex-data.imageIndexTable[imageId]][0];
			
			int xPos = x + getSpriteDrawX();
			int yPos = y + getSpriteDrawY();
			
			if(orientation==MSprite.ORIENTATION_NONE)
				g.drawImage(img,xPos,yPos,20);
			else if(orientation==MSprite.ORIENTATION_FLIP_H){
				DirectGraphics dg = DirectUtils.getDirectGraphics(g);
				dg.drawImage(img,xPos,yPos,20,DirectGraphics.FLIP_HORIZONTAL);
			}else if(orientation==MSprite.ORIENTATION_FLIP_V){
				DirectGraphics dg = DirectUtils.getDirectGraphics(g);
				dg.drawImage(img,xPos,yPos,20,DirectGraphics.FLIP_VERTICAL);
			}
		} else {
			Image[] imageArr = (Image[])data.imageVector.elementAt(imageId);
			
			if(orientation==1){ // flip h
				clipX = imageArr[0].getWidth() - clipW - clipX;
			} else if(orientation==1){
				clipY = imageArr[0].getHeight() - clipH - clipY;
			}
			
			int xPos = x + getSpriteDrawX();
			int yPos = y + getSpriteDrawY();
			g.clipRect(xPos,yPos,clipW,clipH);
			
			if(orientation==MSprite.ORIENTATION_NONE){
				g.drawImage(imageArr[0],xPos-clipX,yPos-clipY,20);
		    }else if(orientation==MSprite.ORIENTATION_FLIP_H){
				DirectGraphics dg = DirectUtils.getDirectGraphics(g);
				dg.drawImage(imageArr[0],xPos-clipX,yPos-clipY,20,DirectGraphics.FLIP_HORIZONTAL);
			}else if(orientation==MSprite.ORIENTATION_FLIP_V){
				DirectGraphics dg = DirectUtils.getDirectGraphics(g);
				dg.drawImage(imageArr[0],xPos-clipX,yPos-clipY,20,DirectGraphics.FLIP_VERTICAL);
			}
		}
	}
	*/
	
	protected void drawEllipseClip(Graphics g,int x,int y,int width,int height,int startAngle,int endAngle,int color,boolean isFilled){
		
		byte spriteOrientation = getSpriteOrientation();
		// changing x and y
		if(spriteOrientation==1){// flip h
			x = -x-width;
		} else if(spriteOrientation==2){// flip v
			y = -y-height;
		}

		int xPos = x + getSpriteDrawX();
		int yPos = y + getSpriteDrawY();
		
		g.setColor(color);
		if(isFilled)
			g.fillArc(xPos,yPos,width,height,startAngle,endAngle);
		else
			g.drawArc(xPos,yPos,width,height,startAngle,endAngle);
	}
	
	protected void drawLineClip(Graphics g,int x1,int y1,int x2,int y2,int color){
		byte spriteOrientation = getSpriteOrientation();
		// changing x and y
		if(spriteOrientation==1){// flip h
			x1 = -x1;
			x2 = -x2;
		} else if(spriteOrientation==2){// flip v
			y1 = -y1;
			y2 = -y2;
		}

		int xPos1 = x1 + getSpriteDrawX();
		int xPos2 = x2 + getSpriteDrawX();
		
		int yPos1 = y1 + getSpriteDrawY();
		int yPos2 = y2 + getSpriteDrawY();
		
		g.setColor(color);
		g.drawLine(xPos1,yPos1,xPos2,yPos2);
	}
	
	protected void drawRectangleClip(Graphics g,int x,int y,int width,int height,int color,boolean isFilled){
		g.setColor(color);

		byte spriteOrientation = getSpriteOrientation();
		// changing x and y
		if(spriteOrientation==1){// flip h
			x = -x-width;
		} else if(spriteOrientation==2){// flip v
			y = -y-height;
		}
		
		int xPos = x + getSpriteDrawX();
		int yPos = y + getSpriteDrawY();

		if(isFilled)
			g.fillRect(xPos,yPos,width,height);
		else
			g.drawRect(xPos,yPos,width,height);		
	}
	
	protected void drawRoundedRectangleClip(Graphics g,int x,int y,int width,int height,int arcWidth,int arcHeight,int color,boolean isFilled){
		byte spriteOrientation = getSpriteOrientation();
		// changing x and y
		if(spriteOrientation==1){// flip h
			x = -x-width;
		} else if(spriteOrientation==2){// flip v
			y = -y-height;
		}

		int xPos = x + getSpriteDrawX();
		int yPos = y + getSpriteDrawY();
		
		g.setColor(color);
		if(isFilled)
			g.fillRoundRect(xPos,yPos,width,height,arcWidth,arcHeight);
		else	
			g.drawRoundRect(xPos,yPos,width,height,arcWidth,arcHeight);		
	}
	
	/**
	 * @return spriteX
	 */
	protected abstract int getSpriteDrawX();
	
	/**
	 * @return spriteY
	 */
	protected abstract int getSpriteDrawY();
	
	/**
	 * Updates the sprite position by xinc, and yinc 
	 */
	protected abstract void updateSpritePositionBy(int xinc,int yinc);
	
	/**
	 * @return sprite orientation
	 */
	protected abstract byte getSpriteOrientation();
	
	/**
	 * Method to notify start of animation
	 */
	protected abstract void notifyStartOfAnimation();
	
	/**
	 * Method to notify end of animation
	 */
	protected abstract void notifyEndOfAnimation();
}
