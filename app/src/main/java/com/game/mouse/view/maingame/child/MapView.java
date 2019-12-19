package com.game.mouse.view.maingame.child;

import java.util.Vector;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import com.game.lib9.L9Config;
import com.game.mouse.modle.service.DataManageService;
import com.game.mouse.screen.MainMidlet;
import com.game.mouse.view.View;
import com.game.mouse.view.game.GameMainView;
import com.model.control.ImageManager;
import com.model.control.MyImg;
import com.model.tool.MathFP;

public class MapView implements View {
	private Image bgImg;

	/**
	 * 从headerHight纵坐标开始画地图
	 */
	private int headerHight = 110;

	private int headerWidth = 230;
//	private int headerWidth = 250;

	private MyImg mapImg;

	private MyImg gameBg;

	private int imgW = 27, imgH = 27;

//	private int imgW = 40, imgH = 40;

	/**
	 * 得到最长的坐标点
	 */
	private int maxPointX;

	/**
	 * 最大跳跃的格数
	 */
	private int maxJumpIndex = 4;

	private int[][] mapDraws;

	private int[][] maps;

	private int[][] datas;

	private int scene;

	private int pass;

	private GameMainView gameMainView;

	//我写的逻辑
	private boolean mType;

	public MapView(GameMainView gameMainView, int scene, int pass,boolean type) {
		mType = type;
		if(type){	//代表没有场景切换（场景3->1，场景4->0）
			this.scene = scene;
			this.pass = pass;
		}else{
			this.scene = scene + 2;
			this.pass = pass + 2;
		}
		int[][][] mapsmes = DataManageService.getInsatnce().getMapsMessage(
				scene, pass);
		this.gameMainView = gameMainView;
		mapDraws = mapsmes[0];
		maps = mapsmes[1];
		datas = mapsmes[2];
	}

	public void init() {
		// TODO Auto-generated method stub
		this.maxPointX = (getMaxIndex() + 1) * imgW;
		this.createMapbgImg();
		mapImg.release(true);
		gameBg.release(true);
	}

	public void clearBgImg() {
		bgImg = null;
	}

	public void createMapbgImg() {
		mapImg = new MyImg("gamepage/sce/" + this.scene + "/maprect.png");
		gameBg = new MyImg("gamepage/sce/" + this.scene + "/bg.png");

		if(scene != 3){
			headerHight = 110;
			headerWidth = 230;
		}else{
			headerHight = 110;
			headerWidth = 230;
		}

		int lenth = mapDraws[0].length;
		int imgW = (int)(MainMidlet.scaleX*mapDraws[0].length * 80);
		int imgH = (int)(L9Config.SCR_H*MainMidlet.scaleX);
		bgImg = Image.createImage(imgW, imgH);
		Graphics g = bgImg.getGraphics();
		paintMapImg(g);
	}

	public void paintMapImg(Graphics g) {
		// TODO Auto-generated method stub
		System.out.println("this is mapDraws[0].length * 80 = " + mapDraws[0].length);
		if (mapDraws[0].length < 16) {
			gameBg.drawRegion(g, 0, 0, mapDraws[0].length * 80, gameBg
					.getHeight(), 0, 0, 0, 0);
		} else if (mapDraws[0].length == 16) {
			gameBg.drawImage(g, 0, 0, 0);
		} else {
			int lenth = mapDraws[0].length;
			int posX = 0;
			for (int i = 0; i < lenth / 16; i++) {
				gameBg.drawImage(g, posX, 0, 0);
				posX += L9Config.SCR_W;
			}
			gameBg.drawRegion(g, 0, 0, (lenth % 16) * 80, gameBg.getHeight(),
					0, posX, 0, 0);
		}
		for (int i = 0; i < mapDraws.length; i++) {
			for (int j = 0; j < mapDraws[i].length; j++) {
				if (mapDraws[i][j] - 1 >= 0) {
					int indexX = (mapDraws[i][j] - 1) % 5;
					int indexY = (mapDraws[i][j] - 1) / 5;
					mapImg.drawRegion(g, indexX * 54, indexY * 54, 54, 54, 0,
							j * 54 + headerWidth, i * 54 + headerHight, 0);
				}
			}
		}
	}

	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		g.setClip(-(int)(gameMainView.moveX * MainMidlet.scaleX), 0, (int)(1280* MainMidlet.scaleX),(int)(720*MainMidlet.scaleY));
		g.drawImage(bgImg, 0, 0, 0);

	}

	public void update() {
		// TODO Auto-generated method stub
	}

	/**
	 * 获得一种类型的第几个坐标
	 * 
	 * @param type
	 * @param index
	 * @return
	 */
	public int[] getPoint(int type, int index) {
		int[] retPoint = { -1, headerWidth, headerHight };
//		int imgW = 42;
//		int imgH = 42;
		int crrIndex = 0;
		for (int i = 0; i < datas.length; i++) {
			for (int j = 0; j < datas[i].length; j++) {
				if (datas[i][j] == type) {
					if (crrIndex == index) {
						if (maps[i + 1][j] != 0 && maps[i][j - 1] != 0
								&& maps[i][j + 1] != 0 && maps[i - 1][j] == 0) {
							retPoint[0] = 0;
							retPoint[1] = j * imgW;
							retPoint[2] = i * imgH;
						} else if (maps[i - 1][j] != 0 && maps[i][j - 1] != 0
								&& maps[i][j + 1] != 0 && maps[i + 1][j] == 0) {
							retPoint[0] = 4;
							retPoint[1] = j * imgW;
							retPoint[2] = (i + 1) * imgH;
						} else if (maps[i][j - 1] != 0 && maps[i + 1][j] != 0
								&& maps[i - 1][j] != 0 && maps[i][j + 1] == 0) {
							retPoint[0] = 2;
							retPoint[1] = (j + 1) * imgW;
							retPoint[2] = i * imgH;
						} else if (maps[i][j + 1] != 0 && maps[i + 1][j] != 0
								&& maps[i - 1][j] != 0 && maps[i][j - 1] == 0) {
							retPoint[0] = 6;
							retPoint[1] = j * imgW;
							retPoint[2] = i * imgH;
						} else {
							retPoint[0] = 0;
							retPoint[1] = j * imgW;
							retPoint[2] = i * imgH;
						}
						retPoint[1] = retPoint[1] + headerWidth;
						retPoint[2] = retPoint[2] + headerHight;
						return retPoint;
					} else {
						crrIndex++;
					}
				}
			}
		}
		return retPoint;
	}

	/**
	 * 获得一种类型所有坐标
	 * 
	 * @param type
	 * @return
	 */
	public Vector getAllPoint(int type) {
		int[] retPoint;
		Vector allPoint = new Vector();
		for (int i = 0; i < datas.length; i++) {
			for (int j = 0; j < datas[i].length; j++) {
				if (datas[i][j] == type) {
					retPoint = new int[3];
					if (maps[i + 1][j] != 0 && maps[i][j - 1] != 0
							&& maps[i][j + 1] != 0 && maps[i - 1][j] == 0) {
						retPoint[0] = 0;
						retPoint[1] = j * imgW;
						retPoint[2] = i * imgH;
					} else if (maps[i - 1][j] != 0 && maps[i][j - 1] != 0
							&& maps[i][j + 1] != 0 && maps[i + 1][j] == 0) {
						retPoint[0] = 4;
						retPoint[1] = j * imgW;
						retPoint[2] = (i + 1) * imgH;
					} else if (maps[i][j - 1] != 0 && maps[i + 1][j] != 0
							&& maps[i - 1][j] != 0 && maps[i][j + 1] == 0) {
						retPoint[0] = 2;
						retPoint[1] = (j + 1) * imgW;
						retPoint[2] = i * imgH;
					} else if (maps[i][j + 1] != 0 && maps[i + 1][j] != 0
							&& maps[i - 1][j] != 0 && maps[i][j - 1] == 0) {
						retPoint[0] = 6;
						retPoint[1] = j * imgW;
						retPoint[2] = i * imgH;
					} else {
						retPoint[0] = -1;
						retPoint[1] = j * imgW;
						retPoint[2] = i * imgH;
					}
					retPoint[1] = retPoint[1] + headerWidth;
					retPoint[2] = retPoint[2] + headerHight;
					allPoint.addElement(retPoint);
				}
			}
		}
		return allPoint;
	}

	public int getMaxIndex() {
		int maxIndexX = 0;
		for (int i = 0; i < maps.length; i++) {
			for (int j = 0; j < maps[i].length; j++) {
				if (maps[i][j] == 1 || maps[i][j] == 2 || maps[i][j] == 3) {
					if (j > maxIndexX)
						maxIndexX = j;
				}
			}
		}
		return maxIndexX;
	}

	/**
	 * 
	 * 
	 * @param direction
	 * @param pointX
	 * @param pointY
	 * @return nextPointX,nextPointY,direction
	 */
	public int[] moveToPointByDirection(int spriteW, int isClockWise,
			int direction, int pointX, int pointY, int speed) {
		int[] ret = new int[4];
		if (isClockWise == 1) {
			int[] retTmp = moveToPointClockWise(spriteW, direction, pointX,
					pointY, speed);
			ret[0] = 1;
			ret[1] = retTmp[0];
			ret[2] = retTmp[1];
			ret[3] = retTmp[2];
		} else {
			int[] retTmp = moveToPointAntiClockWise(spriteW, direction, pointX,
					pointY, speed);
			ret[0] = 0;
			ret[1] = retTmp[0];
			ret[2] = retTmp[1];
			ret[3] = retTmp[2];
		}
		return ret;
	}

	/**
	 * 顺时针 根据角色的x值和y值，当前走动方向，得到下一次的方向 返回下一帧的方向坐标
	 * 
	 * @param direction
	 * @param pointX
	 * @param pointY
	 * @param speed
	 * @return
	 */
	public int[] moveToPointClockWise(int spriteW, int direction, int pointX,
			int pointY, int speed) {
		int[] ret = new int[3];
		int mapIndexX = pointX / imgW;
		int mapIndexY = pointY / imgH;
		int lastWidth = 0;
		int radians = MathFP.div(MathFP.mul(MathFP.toFP(45), MathFP.PI), MathFP
				.toFP(180));
		int deltaX, deltaY;
		switch (direction) {
		case 0:
			if (maps[mapIndexY][mapIndexX] == 2
					&& maps[mapIndexY][mapIndexX + 1] == 0) {
				lastWidth = (mapIndexX + 1) * imgW - pointX;
				if (lastWidth - speed <= 0) {
					deltaX = MathFP.toInt(MathFP.mul(MathFP.toFP(lastWidth),
							MathFP.cos(radians)));
					// y方向增量
					deltaY = MathFP.toInt(MathFP.mul(MathFP.toFP(lastWidth),
							MathFP.tan(radians)));
					ret[0] = 1;
					ret[1] = pointX + deltaX;
					ret[2] = pointY + (speed - deltaY);
				} else {
					ret[0] = 0;
					ret[1] = pointX + speed;
					ret[2] = pointY;
				}
			} else if (maps[mapIndexY][mapIndexX + 1] == 3) {
				lastWidth = (mapIndexX + 1) * imgW - pointX;
				if (lastWidth - speed <= 0) {
					deltaY = MathFP.toInt(MathFP.mul(MathFP.toFP(lastWidth),
							MathFP.tan(radians)));
					ret[0] = 6;
					ret[1] = (mapIndexX + 1) * imgW;
					ret[2] = pointY + (speed - deltaY);
				} else {
					ret[0] = 0;
					ret[1] = pointX + speed;
					ret[2] = pointY;
				}
			} else {
				ret[0] = 0;
				ret[1] = pointX + speed;
				ret[2] = pointY;
			}
			break;
		case 1:
			mapIndexX = pointX % imgW == 0 ? mapIndexX - 1 : mapIndexX;
			lastWidth = (mapIndexX + 1) * imgW - pointX;
			deltaY = speed
					- MathFP.toInt(MathFP.mul(MathFP.toFP(lastWidth), MathFP
							.tan(radians)))
					+ MathFP.toInt(MathFP.mul(MathFP.toFP(lastWidth), MathFP
							.sin(radians))); // y方向增量
			ret[0] = 2;
			ret[1] = (mapIndexX + 1) * imgW;
			ret[2] = pointY + deltaY;
			break;
		case 2:
			if (maps[mapIndexY][mapIndexX - 1] == 2
					&& maps[mapIndexY + 1][mapIndexX - 1] == 0) {
				lastWidth = (mapIndexY + 1) * imgH - pointY;
				if (lastWidth - speed <= 0) {
					deltaX = MathFP.toInt(MathFP.mul(MathFP.toFP(lastWidth),
							MathFP.cos(radians)));
					// y方向增量
					deltaY = MathFP.toInt(MathFP.mul(MathFP.toFP(lastWidth),
							MathFP.sin(radians)));
					ret[0] = 3;
					ret[1] = pointX - deltaX;
					ret[2] = pointY + deltaY;
				} else {
					ret[0] = 2;
					ret[1] = pointX;
					ret[2] = pointY + speed;
				}
			} else if (maps[mapIndexY + 1][mapIndexX - 1] == 3) {
				lastWidth = (mapIndexY + 1) * imgH - pointY;
				if (lastWidth - speed <= 0) {
					deltaX = MathFP.toInt(MathFP.mul(MathFP.toFP(lastWidth),
							MathFP.cos(radians)));
					ret[0] = 0;
					ret[1] = pointX - deltaX;
					ret[2] = (mapIndexY + 1) * imgH;
				} else {
					ret[0] = 2;
					ret[1] = pointX;
					ret[2] = pointY + speed;
				}
			} else {
				ret[0] = 2;
				ret[1] = pointX;
				ret[2] = pointY + speed;
			}
			break;
		case 3:
			mapIndexY = pointY % imgH == 0 ? mapIndexY - 1 : mapIndexY;
			lastWidth = (mapIndexY + 1) * imgH - pointY;
			deltaX = speed
					- MathFP.toInt(MathFP.mul(MathFP.toFP(lastWidth), MathFP
							.tan(radians)))
					+ MathFP.toInt(MathFP.mul(MathFP.toFP(lastWidth), MathFP
							.sin(radians))); // X方向增量
			ret[0] = 4;
			ret[1] = pointX - deltaX;
			ret[2] = (mapIndexY + 1) * imgH;
			break;
		case 4:
			if (maps[mapIndexY - 1][mapIndexX] == 2
					&& maps[mapIndexY - 1][mapIndexX - 1] == 0) {
				lastWidth = pointX - mapIndexX * imgW;
				if (lastWidth - speed <= 0) {
					deltaX = MathFP.toInt(MathFP.mul(MathFP.toFP(lastWidth),
							MathFP.cos(radians)));
					// y方向增量
					deltaY = MathFP.toInt(MathFP.mul(MathFP.toFP(lastWidth),
							MathFP.sin(radians)));
					ret[0] = 5;
					ret[1] = pointX - deltaX;
					ret[2] = pointY - deltaY;
				} else {
					ret[0] = 4;
					ret[1] = pointX - speed;
					ret[2] = pointY;
				}
			} else if (maps[mapIndexY - 1][mapIndexX - 1] == 3) {
				lastWidth = pointX - mapIndexX * imgW;
				if (lastWidth - speed <= 0) {

					// y方向增量
					deltaY = MathFP.toInt(MathFP.mul(MathFP.toFP(lastWidth),
							MathFP.sin(radians)));
					ret[0] = 2;
					ret[1] = mapIndexX * imgW;
					ret[2] = pointY - deltaY;
				} else {
					ret[0] = 4;
					ret[1] = pointX - speed;
					ret[2] = pointY;
				}
			} else {
				ret[0] = 4;
				ret[1] = pointX - speed;
				ret[2] = pointY;
			}
			break;
		case 5:
			lastWidth = pointX - mapIndexX * imgW;
			deltaY = speed
					- MathFP.toInt(MathFP.mul(MathFP.toFP(lastWidth), MathFP
							.tan(radians)))
					+ MathFP.toInt(MathFP.mul(MathFP.toFP(lastWidth), MathFP
							.sin(radians))); // Y方向增量
			ret[0] = 6;
			ret[1] = mapIndexX * imgW;
			ret[2] = pointY - deltaY;
			break;
		case 6:
			if (maps[mapIndexY][mapIndexX] == 2
					&& maps[mapIndexY - 1][mapIndexX] == 0) {
				lastWidth = pointY - mapIndexY * imgH;
				if (lastWidth - speed <= 0) {
					deltaX = MathFP.toInt(MathFP.mul(MathFP.toFP(lastWidth),
							MathFP.cos(radians)));
					// y方向增量
					deltaY = MathFP.toInt(MathFP.mul(MathFP.toFP(lastWidth),
							MathFP.sin(radians)));
					ret[0] = 7;
					ret[1] = pointX - deltaX;
					ret[2] = pointY - deltaY;
				} else {
					ret[0] = 6;
					ret[1] = pointX;
					ret[2] = pointY - speed;
				}
			} else if (maps[mapIndexY - 1][mapIndexX] == 3) {
				lastWidth = pointY - mapIndexY * imgH;
				if (lastWidth - speed <= 0) {
					deltaX = MathFP.toInt(MathFP.mul(MathFP.toFP(lastWidth),
							MathFP.cos(radians)));
					ret[0] = 4;
					ret[1] = pointX - deltaX;
					ret[2] = mapIndexY * imgH;
				} else {
					ret[0] = 6;
					ret[1] = pointX;
					ret[2] = pointY - speed;
				}
			} else {
				ret[0] = 6;
				ret[1] = pointX;
				ret[2] = pointY - speed;
			}
			break;
		case 7:
			lastWidth = pointY - mapIndexY * imgH;
			deltaX = speed
					- MathFP.toInt(MathFP.mul(MathFP.toFP(lastWidth), MathFP
							.tan(radians)))
					+ MathFP.toInt(MathFP.mul(MathFP.toFP(lastWidth), MathFP
							.sin(radians))); // X方向增量
			ret[0] = 0;
			ret[1] = pointX + deltaX;
			ret[2] = mapIndexY * imgH;
			break;
		}
		return ret;
	}

	/**
	 * 逆时针 根据角色的x值和y值，当前走动方向，得到下一次的方向 返回下一帧的方向坐标
	 * 
	 * @param direction
	 * @param pointX
	 * @param pointY
	 * @param speed
	 * @return
	 */
	public int[] moveToPointAntiClockWise(int spriteW, int direction,
			int pointX, int pointY, int speed) {
		int[] ret = new int[3];
		int mapIndexX = pointX / imgW;
		int mapIndexY = pointY / imgH;
		int lastWidth = 0;
		int radians = MathFP.div(MathFP.mul(MathFP.toFP(45), MathFP.PI), MathFP
				.toFP(180));
		int deltaX, deltaY;
		switch (direction) {
		case 0:
			if (maps[mapIndexY][mapIndexX] == 2
					&& maps[mapIndexY][mapIndexX - 1] == 0) {
				lastWidth = pointX - mapIndexX * imgW;
				if (lastWidth - speed <= 0) {
					deltaX = MathFP.toInt(MathFP.mul(MathFP.toFP(lastWidth),
							MathFP.cos(radians)));
					// y方向增量
					deltaY = MathFP.toInt(MathFP.mul(MathFP.toFP(lastWidth),
							MathFP.tan(radians)));
					ret[0] = 1;
					ret[1] = pointX - deltaX;
					ret[2] = pointY + (speed - deltaY);
				} else {
					ret[0] = 0;
					ret[1] = pointX - speed;
					ret[2] = pointY;
				}
			} else if (maps[mapIndexY][mapIndexX - 1] == 3) {
				lastWidth = mapIndexX * imgW - pointX;
				if (lastWidth - speed <= 0) {
					deltaY = MathFP.toInt(MathFP.mul(MathFP.toFP(lastWidth),
							MathFP.tan(radians)));
					ret[0] = 6;
					ret[1] = mapIndexX * imgW;
					ret[2] = pointY - (speed - deltaY);
				} else {
					ret[0] = 0;
					ret[1] = pointX - speed;
					ret[2] = pointY;
				}
			} else {
				ret[0] = 0;
				ret[1] = pointX - speed;
				ret[2] = pointY;
			}
			break;
		case 1:
			lastWidth = pointX - mapIndexX * imgW;
			deltaY = speed
					- MathFP.toInt(MathFP.mul(MathFP.toFP(lastWidth), MathFP
							.tan(radians)))
					+ MathFP.toInt(MathFP.mul(MathFP.toFP(lastWidth), MathFP
							.sin(radians))); // Y方向增量
			ret[0] = 2;
			ret[1] = mapIndexX * imgW;
			ret[2] = pointY - deltaY;
			break;
		case 2:
			if (maps[mapIndexY][mapIndexX] == 2
					&& maps[mapIndexY + 1][mapIndexX] == 0) {
				lastWidth = (mapIndexY + 1) * imgH - pointY;
				if (lastWidth - speed <= 0) {
					deltaX = MathFP.toInt(MathFP.mul(MathFP.toFP(lastWidth),
							MathFP.cos(radians)));
					// y方向增量
					deltaY = MathFP.toInt(MathFP.mul(MathFP.toFP(lastWidth),
							MathFP.sin(radians)));
					ret[0] = 3;
					ret[1] = pointX + deltaX;
					ret[2] = pointY + deltaY;
				} else {
					ret[0] = 2;
					ret[1] = pointX;
					ret[2] = pointY + speed;
				}
			} else if (maps[mapIndexY + 1][mapIndexX] == 3) {
				lastWidth = (mapIndexY + 1) * imgH - pointY;
				if (lastWidth - speed <= 0) {
					deltaX = MathFP.toInt(MathFP.mul(MathFP.toFP(lastWidth),
							MathFP.cos(radians)));
					ret[0] = 0;
					ret[1] = pointX - deltaX;
					ret[2] = (mapIndexY + 1) * imgH;
				} else {
					ret[0] = 2;
					ret[1] = pointX;
					ret[2] = pointY + speed;
				}
			} else {
				ret[0] = 2;
				ret[1] = pointX;
				ret[2] = pointY + speed;
			}
			break;
		case 3:
			mapIndexY = pointY % imgH == 0 ? mapIndexY - 1 : mapIndexY;
			lastWidth = (mapIndexY + 1) * imgH - pointY;
			deltaX = speed
					- MathFP.toInt(MathFP.mul(MathFP.toFP(lastWidth), MathFP.tan(radians)))
					+ MathFP.toInt(MathFP.mul(MathFP.toFP(lastWidth), MathFP.sin(radians))); // X方向增量
			ret[0] = 4;
			ret[1] = pointX + deltaX;
			ret[2] = (mapIndexY + 1) * imgH;
			break;
		case 4:
			if (maps[mapIndexY - 1][mapIndexX] == 2
					&& maps[mapIndexY - 1][mapIndexX + 1] == 0) {
				lastWidth = (mapIndexX + 1) * imgW - pointX;
				if (lastWidth - speed <= 0) {
					deltaX = MathFP.toInt(MathFP.mul(MathFP.toFP(lastWidth),
							MathFP.cos(radians)));
					// y方向增量
					deltaY = MathFP.toInt(MathFP.mul(MathFP.toFP(lastWidth),
							MathFP.sin(radians)));
					ret[0] = 5;
					ret[1] = pointX + deltaX;
					ret[2] = pointY - deltaY;
				} else {
					ret[0] = 4;
					ret[1] = pointX + speed;
					ret[2] = pointY;
				}
			} else if (maps[mapIndexY - 1][mapIndexX + 1] == 3) {
				lastWidth = (mapIndexX + 1) * imgW - pointX;
				if (lastWidth - speed <= 0) {
					deltaY = MathFP.toInt(MathFP.mul(MathFP.toFP(lastWidth),
							MathFP.sin(radians)));
					ret[0] = 2;
					ret[1] = (mapIndexX + 1) * imgW;
					ret[2] = pointY + deltaY;
				} else {
					ret[0] = 4;
					ret[1] = pointX + speed;
					ret[2] = pointY;
				}
			} else {
				ret[0] = 4;
				ret[1] = pointX + speed;
				ret[2] = pointY;
			}
			break;
		case 5:
			mapIndexX = pointX % imgW == 0 ? mapIndexX - 1 : mapIndexX;
			lastWidth = (mapIndexX + 1) * imgW - pointX;
			deltaY = speed
					- MathFP.toInt(MathFP.mul(MathFP.toFP(lastWidth), MathFP
							.tan(radians)))
					+ MathFP.toInt(MathFP.mul(MathFP.toFP(lastWidth), MathFP
							.sin(radians))); // Y方向增量
			ret[0] = 6;
			ret[1] = (mapIndexX + 1) * imgW;
			ret[2] = pointY - deltaY;
			break;
		case 6:
			if (maps[mapIndexY][mapIndexX - 1] == 2
					&& maps[mapIndexY - 1][mapIndexX - 1] == 0) {
				lastWidth = pointY - mapIndexY * imgH;
				if (lastWidth - speed <= 0) {
					deltaX = MathFP.toInt(MathFP.mul(MathFP.toFP(lastWidth),
							MathFP.cos(radians)));
					// y方向增量
					deltaY = MathFP.toInt(MathFP.mul(MathFP.toFP(lastWidth),
							MathFP.sin(radians)));
					ret[0] = 7;
					ret[1] = pointX - deltaX;
					ret[2] = pointY - deltaY;
				} else {
					ret[0] = 6;
					ret[1] = pointX;
					ret[2] = pointY - speed;
				}
			} else if (maps[mapIndexY - 1][mapIndexX - 1] == 3) {
				lastWidth = pointY - mapIndexY * imgH;
				if (lastWidth - speed <= 0) {
					deltaX = MathFP.toInt(MathFP.mul(MathFP.toFP(lastWidth),
							MathFP.cos(radians)));
					ret[0] = 4;
					ret[1] = pointX + deltaX;
					ret[2] = mapIndexY * imgH;
				} else {
					ret[0] = 6;
					ret[1] = pointX;
					ret[2] = pointY - speed;
				}
			} else {
				ret[0] = 6;
				ret[1] = pointX;
				ret[2] = pointY - speed;
			}
			break;
		case 7:
			lastWidth = pointY - mapIndexY * imgH;
			deltaX = speed
					- MathFP.toInt(MathFP.mul(MathFP.toFP(lastWidth), MathFP
							.tan(radians)))
					+ MathFP.toInt(MathFP.mul(MathFP.toFP(lastWidth), MathFP
							.sin(radians))); // X方向增量
			ret[0] = 0;
			ret[1] = pointX - deltaX;
			ret[2] = mapIndexY * imgH;
			break;
		}
		return ret;
	}

	public int getImgW() {
		return imgW;
	}

	public void setImgW(int imgW) {
		this.imgW = imgW;
	}

	public int getImgH() {
		return imgH;
	}

	public void setImgH(int imgH) {
		this.imgH = imgH;
	}

	public int[] getJumpAfterPoint(int isClockWise, int direction, int pointX,
			int pointY) {
		int mapIndexX = pointX / imgW;
		int mapIndexY = pointY / imgH;
		int[] jumpAfterPoint = null;
		int n = 0;
		switch (direction) {
		case 0:
			while ((mapIndexY--) > 0 && n <= maxJumpIndex) {
				if (n == maxJumpIndex
						&& (maps[mapIndexY][mapIndexX] == 1 || maps[mapIndexY][mapIndexX] == 2)) {
					jumpAfterPoint = new int[3];
					jumpAfterPoint[0] = 4;
					jumpAfterPoint[1] = pointX;
					jumpAfterPoint[2] = (mapIndexY + 1) * imgH;
					break;
				}
				n++;
			}
			break;
		case 4:
			while ((mapIndexY++) < maps.length - 1 && n < maxJumpIndex) {
				if ((n == maxJumpIndex - 1)
						&& (maps[mapIndexY][mapIndexX] == 1 || maps[mapIndexY][mapIndexX] == 2)) {
					jumpAfterPoint = new int[3];
					jumpAfterPoint[0] = 0;
					jumpAfterPoint[1] = pointX;
					jumpAfterPoint[2] = mapIndexY * imgH;
					break;
				}
				n++;
			}
		}
		return jumpAfterPoint;
	}

	public int getMaxPointX() {
		return maxPointX;
	}

	/**
	 * 从direction方向得到以坐标原点pointX，pointY的第一个地图障碍
	 * 
	 * @direction 0-向右，1-向左 2-向下 3-向上
	 * @param initPointX
	 * @param initPointY
	 * @return
	 */
	public int[] getMapImgPoint(int direction, int initPointX, int initPointY,
			int toPointX, int toPointY) {
		int[] point = { toPointX, toPointY };
		int mapIndexX = initPointX / imgW;
		int mapIndexY = initPointY / imgH;
		if (direction == 0) {
			point[1] = initPointY;
			for (int j = mapIndexX; j < maps[mapIndexY].length; j++) {
				if (maps[mapIndexY][j] == 1 || maps[mapIndexY][j] == 2
						|| maps[mapIndexY][j] == 3) {
					point[0] = j * imgW;
					break;
				}
			}
		} else if (direction == 1) {
			point[1] = initPointY;
			for (int j = mapIndexX; j > 0; j--) {
				if (maps[mapIndexY][j] == 1 || maps[mapIndexY][j] == 2
						|| maps[mapIndexY][j] == 3) {
					point[0] = j * (imgW + 1);
					break;
				}
			}
		} else if (direction == 2) {
			point[0] = initPointX;
			for (int i = mapIndexY; i < maps.length; i++) {
				if (maps[i][mapIndexX] == 1 || maps[i][mapIndexX] == 2
						|| maps[i][mapIndexX] == 3) {
					point[1] = i * imgH;
					break;
				}
			}
		} else {
			point[0] = initPointX;
			for (int i = mapIndexY; i > 0; i--) {
				if (maps[i][mapIndexX] == 1 || maps[i][mapIndexX] == 2
						|| maps[i][mapIndexX] == 3) {
					point[1] = (i + 1) * imgH;
					break;
				}
			}
		}
		return point;
	}

	public int getHeaderHight() {
		return headerHight;
	}

	public int getHeaderWidth() {
		return headerWidth;
	}

	/**
	 * 从当前点坐标的，向左、右、上、下得到为0或者不为0的坐标
	 * 
	 * @param x
	 *            direction
	 * @return
	 */
	public int[] getFristPoint0FromDirection(int x, int y, int direction,
			boolean is0) {
		int[] point = { 0, 0 };
		int mapIndexX = (x - headerWidth) / imgW;
		int mapIndexY = (y - headerHight) / imgH;
//		int mapIndexX = (x) / imgW;
//		int mapIndexY = (y) / imgH;
		for (int n = 1; true; n++) {
			if (direction == 0) {
				if (is0 && mapIndexX - n >= 0
						&& maps[mapIndexY][mapIndexX - n] == 0) {
					point[0] = (mapIndexX - n + 1) * imgW;
					point[1] = y;
					break;
				} else if (is0 && mapIndexX - n >= 0
						&& maps[mapIndexY][mapIndexX - n] == 3) {
					point[0] = (mapIndexX - n) * imgW;
					point[1] = y;
					break;
				} else if (!is0 && mapIndexX - n >= 0
						&& maps[mapIndexY][mapIndexX - n] != 0) {
					point[0] = (mapIndexX - n + 1) * imgW;
					point[1] = y;
					break;
				} else if (n > maps[mapIndexY].length) {
					point[0] = -imgW;
					point[1] = y;
					break;
				}
			} else if (direction == 1) {

				if (is0 && mapIndexX + n <= maps[mapIndexY].length - 1
						&& maps[mapIndexY][mapIndexX + n] == 0) {
					point[0] = (mapIndexX + n) * imgW;
					point[1] = y;
					break;
				} else if (is0 && mapIndexX + n <= maps[mapIndexY].length - 1
						&& maps[mapIndexY][mapIndexX + n] == 3) {
					point[0] = (mapIndexX + n + 1) * imgW;
					point[1] = y;
					break;
				} else if (!is0 && mapIndexX + n <= maps[mapIndexY].length - 1
						&& maps[mapIndexY][mapIndexX + n] != 0) {
					point[0] = (mapIndexX + n) * imgW;
					point[1] = y;
					break;
				} else if (n > maps[mapIndexY].length) {
					point[0] = (n + 1) * imgW;
					point[1] = y;
					break;
				}
			} else if (direction == 2) {
				if (is0 && mapIndexY - n >= 0
						&& maps[mapIndexY - n][mapIndexX] == 0) {
					point[0] = x ;
					point[1] = (mapIndexY - n + 1) * imgH ;
					break;
				} else if (is0 && mapIndexY - n >= 0
						&& maps[mapIndexY - n][mapIndexX] == 3) {
					point[0] = x ;
					point[1] = (mapIndexY - n) * imgH ;
					break;
				} else if (!is0 && mapIndexY - n >= 0
						&& maps[mapIndexY - n][mapIndexX] != 0) {
					point[0] = x ;
					point[1] = (mapIndexY - n + 1) * imgH ;
					break;
				} else if (n > maps.length) {
					point[0] = x;
					point[1] = -imgH;
					break;
				}
			} else {
				if (is0 && mapIndexY + n <= maps.length - 1
						&& maps[mapIndexY + n][mapIndexX] == 0) {
					point[0] = x ;
					point[1] = (mapIndexY + n) * imgH ;
					break;
				} else if (is0 && mapIndexY + n <= maps.length - 1
						&& maps[mapIndexY + n][mapIndexX] == 3) {
					point[0] = x ;
					point[1] = (mapIndexY + n + 1) * imgH;
					break;
				} else if (!is0 && mapIndexY + n <= maps.length - 1
						&& maps[mapIndexY + n][mapIndexX] != 0) {
					point[0] = x;
					point[1] = (mapIndexY + n) * imgH;
					break;
				} else if (n > maps.length) {
					point[0] = x;
					point[1] = L9Config.SCR_H + imgH;
					break;
				}
			}
		}
		point[0] = point[0] + headerWidth;
		point[1] = point[1] + headerHight;
		return point;
	}

	/**
	 * 
	 * 得到两个点之间距离
	 * 

	 * @return
	 */
	public int getTwoPointLen(int mapIndexX0, int mapIndexY0, int mapIndexX1,
			int mapIndexY1) {
		if (mapIndexX0 == mapIndexX1) {
			return mapIndexY1 - mapIndexY0 > 0 ? mapIndexY1 - mapIndexY0
					: mapIndexY0 - mapIndexY1;
		} else if (mapIndexY0 == mapIndexY1) {
			return mapIndexX1 - mapIndexX0 > 0 ? mapIndexX1 - mapIndexX0
					: mapIndexX0 - mapIndexX1;
		} else {
			int lenX = mapIndexX1 - mapIndexX0 > 0 ? mapIndexX1 - mapIndexX0
					: mapIndexX0 - mapIndexX1;
			int lenY = mapIndexY1 - mapIndexY0 > 0 ? mapIndexY1 - mapIndexY0
					: mapIndexY0 - mapIndexY1;
			return lenX + lenY;
		}
	}

	/**
	 * 
	 * 判断两点是否是一条通路
	 * 
	 * @param pointMapX0
	 * @param pointMapY0
	 * @return
	 */
	public boolean isTwoPointWay(int direction, int pointMapX0, int pointMapY0,
			int pointMapX1, int pointMapY1) {
		int pointMapX = pointMapX0, pointMapY = pointMapY0;
		int n = 0;
		while (n < 500) {
			// 如果顺时针走一遍能达到指定点，说明此路是通的
			if (pointMapX > maps.length * imgW - 40 || pointMapX < 40) {
				break;
			}
			int[] ret = this.moveToPointAntiClockWise(0, direction, pointMapX,
					pointMapY, 10);
			direction = ret[0];
			pointMapX = ret[1];
			pointMapY = ret[2];
			if (n > 0 && pointMapX0 / imgW == pointMapX / imgW
					&& pointMapY / imgH == pointMapY0 / imgH) {
				return false;
			} else if (pointMapX1 / imgW == pointMapX / imgW
					&& pointMapY / imgH == pointMapY1 / imgH) {
				return true;
			}
			n++;
		}
		return false;
	}

	public int[][] getMaps() {
		return maps;
	}

	public int getScene() {
		return scene;
	}

	public int getPass() {
		return pass;
	}

	/**
	 * 得到指定坐标的值
	 * 
	 * @param pointMapX
	 * @param pointMapY
	 * @return
	 */
	public int getPointValue(int pointMapX, int pointMapY) {
		return maps[pointMapY / imgH][pointMapX / imgW];
	}
}
