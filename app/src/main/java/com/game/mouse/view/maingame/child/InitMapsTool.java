package com.game.mouse.view.maingame.child;

import android.util.Log;

import java.util.Hashtable;
import java.util.Vector;

import com.game.mouse.modle.Cat;
import com.game.mouse.modle.Mouse;
import com.game.mouse.modle.Pass;
import com.game.mouse.view.game.GameMainView;

public class InitMapsTool {

	/**
	 * 初始化主角色老鼠
	 */
	public static MouseSpriteView initSpriteMouse(GameMainView gameMainView,
			MapView mapView, Mouse mouse) {
		int[] mousepoint = mapView.getPoint(10, 0);
		int isClockWise = 1;
		if (mousepoint[0] == -1) {
			mousepoint = mapView.getPoint(11, 0);
			isClockWise = 0;
		}
		MouseSpriteView spriteView = new MouseSpriteView(gameMainView, mapView,
				mouse, isClockWise, mousepoint[0], mousepoint[1], mousepoint[2]);
		return spriteView;
	}

	/**
	 * 实例化猫对象
	 * 
	 * @param gameMainView
	 * @param mapView
	 * @param cat
	 * @param catpoint
	 * @return
	 */
	public static CatSpriteView newCatSpriteView(GameMainView gameMainView,
			MapView mapView, Cat cat, int[] catpoint) {
		CatSpriteView catSpriteView;
		switch (cat.getMoveType()) {
		case 0:
			if (cat.isJump()) {
				catSpriteView = new CatSpriteChaseView(gameMainView, mapView,
						cat, 0, catpoint[0], catpoint[1], catpoint[2]);
			} else {
				catSpriteView = new CatSpriteView(gameMainView, mapView, cat,
						0, catpoint[0], catpoint[1], catpoint[2]);
			}
			break;
		case 1:
			catSpriteView = new CatSpriteLineMoveView(gameMainView, mapView,
					cat, 0, catpoint[0], catpoint[1], catpoint[2]);
			break;
		default:
			catSpriteView = new CatSpriteSleepView(gameMainView, mapView, cat,
					catpoint[0], catpoint[1], catpoint[2]);
		}
		return catSpriteView;
	}

	/**
	 * 初始化地图上的猫角色
	 */
	public static Vector initSpriteCats(GameMainView gameMainView,
			MapView mapView, Pass pass) {
		int hp = 1;
		Vector catSpriteViews = new Vector();
		Vector catspoint20 = mapView.getAllPoint(20);
		Hashtable catmsg = pass.getResPass().getCatMsg(20);
		Cat cat;
		CatSpriteView catSpriteView;
		if (catspoint20 != null && catspoint20.size() > 0 && catmsg != null) {
			cat = new Cat("0", hp, Integer
					.parseInt(catmsg.get("hp").toString()), Integer
					.parseInt(catmsg.get("att").toString()), Integer
					.parseInt(catmsg.get("lv").toString()));
			for (int i = 0; i < catspoint20.size(); i++) {
				int[] catpoint = (int[]) catspoint20.elementAt(i);
				catSpriteViews.addElement(newCatSpriteView(gameMainView,
						mapView, cat, catpoint));
			}
		}

		Vector catspoint21 = mapView.getAllPoint(21);
		catmsg = pass.getResPass().getCatMsg(21);
		if (catspoint21 != null && catspoint21.size() > 0 && catmsg != null) {
			cat = new Cat("1", hp, Integer
					.parseInt(catmsg.get("hp").toString()), Integer
					.parseInt(catmsg.get("att").toString()), Integer
					.parseInt(catmsg.get("lv").toString()));
			for (int i = 0; i < catspoint21.size(); i++) {
				int[] catpoint = (int[]) catspoint21.elementAt(i);
				catSpriteViews.addElement(newCatSpriteView(gameMainView,
						mapView, cat, catpoint));
			}
		}

		Vector catspoint22 = mapView.getAllPoint(22);
		catmsg = pass.getResPass().getCatMsg(22);
		if (catspoint22 != null && catspoint22.size() > 0 && catmsg != null) {
			cat = new Cat("2", hp, Integer
					.parseInt(catmsg.get("hp").toString()), Integer
					.parseInt(catmsg.get("att").toString()), Integer
					.parseInt(catmsg.get("lv").toString()));
			for (int i = 0; i < catspoint22.size(); i++) {
				int[] catpoint = (int[]) catspoint22.elementAt(i);
				catSpriteViews.addElement(newCatSpriteView(gameMainView,
						mapView, cat, catpoint));
			}
		}

		Vector catspoint23 = mapView.getAllPoint(23);
		catmsg = pass.getResPass().getCatMsg(23);
		if (catspoint23 != null && catspoint23.size() > 0 && catmsg != null) {
			cat = new Cat("3", hp, Integer
					.parseInt(catmsg.get("hp").toString()), Integer
					.parseInt(catmsg.get("att").toString()), Integer
					.parseInt(catmsg.get("lv").toString()));
			for (int i = 0; i < catspoint23.size(); i++) {
				int[] catpoint = (int[]) catspoint23.elementAt(i);
				catSpriteViews.addElement(newCatSpriteView(gameMainView,
						mapView, cat, catpoint));
			}
		}

		Vector catspoint24 = mapView.getAllPoint(24);
		catmsg = pass.getResPass().getCatMsg(24);
		if (catspoint24 != null && catspoint24.size() > 0 && catmsg != null) {
			cat = new Cat("4", hp, Integer
					.parseInt(catmsg.get("hp").toString()), Integer
					.parseInt(catmsg.get("att").toString()), Integer
					.parseInt(catmsg.get("lv").toString()));
			for (int i = 0; i < catspoint24.size(); i++) {
				int[] catpoint = (int[]) catspoint24.elementAt(i);
				catSpriteViews.addElement(newCatSpriteView(gameMainView,
						mapView, cat, catpoint));
			}
		}

		Vector catspoint25 = mapView.getAllPoint(25);
		catmsg = pass.getResPass().getCatMsg(25);
		if (catspoint25 != null && catspoint25.size() > 0 && catmsg != null) {
			cat = new Cat("5", hp, Integer
					.parseInt(catmsg.get("hp").toString()), Integer
					.parseInt(catmsg.get("att").toString()), Integer
					.parseInt(catmsg.get("lv").toString()));
			for (int i = 0; i < catspoint25.size(); i++) {
				int[] catpoint = (int[]) catspoint25.elementAt(i);
				catSpriteViews.addElement(newCatSpriteView(gameMainView,
						mapView, cat, catpoint));
			}
		}

		Vector catspoint26 = mapView.getAllPoint(26);
		catmsg = pass.getResPass().getCatMsg(26);
		if (catspoint26 != null && catspoint26.size() > 0 && catmsg != null) {
			cat = new Cat("6", hp, Integer
					.parseInt(catmsg.get("hp").toString()), Integer
					.parseInt(catmsg.get("att").toString()), Integer
					.parseInt(catmsg.get("lv").toString()));
			for (int i = 0; i < catspoint26.size(); i++) {
				int[] catpoint = (int[]) catspoint26.elementAt(i);
				catSpriteViews.addElement(newCatSpriteView(gameMainView,
						mapView, cat, catpoint));
			}
		}

		Vector catspoint27 = mapView.getAllPoint(27);
		catmsg = pass.getResPass().getCatMsg(27);
		if (catspoint27 != null && catspoint27.size() > 0 && catmsg != null) {
			cat = new Cat("7", hp, Integer
					.parseInt(catmsg.get("hp").toString()), Integer
					.parseInt(catmsg.get("att").toString()), Integer
					.parseInt(catmsg.get("lv").toString()));
			for (int i = 0; i < catspoint27.size(); i++) {
				int[] catpoint = (int[]) catspoint27.elementAt(i);
				catSpriteViews.addElement(newCatSpriteView(gameMainView,
						mapView, cat, catpoint));
			}
		}

		Vector catspoint28 = mapView.getAllPoint(28);
		catmsg = pass.getResPass().getCatMsg(28);
		if (catspoint28 != null && catspoint28.size() > 0 && catmsg != null) {
			cat = new Cat("8", hp, Integer
					.parseInt(catmsg.get("hp").toString()), Integer
					.parseInt(catmsg.get("att").toString()), Integer
					.parseInt(catmsg.get("lv").toString()));
			for (int i = 0; i < catspoint28.size(); i++) {
				int[] catpoint = (int[]) catspoint28.elementAt(i);
				catSpriteViews.addElement(newCatSpriteView(gameMainView,
						mapView, cat, catpoint));
			}
		}
		return catSpriteViews;
	}

	/**
	 * 初始化奶酪
	 */
	public static Vector initAllcheeses(GameMainView gameMainView,
			MapView mapView) {
		Vector cheeseViews = new Vector();
		Vector cheesesPoint40 = mapView.getAllPoint(40);
		for (int i = 0; i < cheesesPoint40.size(); i++) {
			int[] point = (int[]) cheesesPoint40.elementAt(i);
			cheeseViews.addElement(new CheeseCommonView(gameMainView, mapView,
					null, 0, point[0], point[1], point[2]));
		}
		Vector cheesesPoint41 = mapView.getAllPoint(41);
		for (int i = 0; i < cheesesPoint41.size(); i++) {
			int[] point = (int[]) cheesesPoint41.elementAt(i);
			cheeseViews.addElement(new CheeseProtectView(gameMainView, mapView,
					point[0], point[1], point[2]));
		}
		Vector cheesesPoint42 = mapView.getAllPoint(42);
		for (int i = 0; i < cheesesPoint42.size(); i++) {
			int[] point = (int[]) cheesesPoint42.elementAt(i);
			if (point[0] == 0) {
				cheeseViews.addElement(new CheeseMoldyView(gameMainView,
						mapView, 0, point[0], point[1], point[2]));
			}
		}
		return cheeseViews;
	}

	/**
	 * 初始化陷阱
	 * 
	 * @param gameMainView
	 * @param mapView
	 * @return
	 */
	public static Vector initOrgan(GameMainView gameMainView, MapView mapView) {
		Vector organViews = new Vector();
		Vector organViewsTmp = InitMapsTool.initNormalOrgan(gameMainView,
				mapView);
		for (int i = 0; i < organViewsTmp.size(); i++) {
			organViews.addElement(organViewsTmp.elementAt(i));
		}
		organViewsTmp = InitMapsTool.initOrganMove(gameMainView, mapView);
		for (int i = 0; i < organViewsTmp.size(); i++) {
			organViews.addElement(organViewsTmp.elementAt(i));
		}
		organViewsTmp = InitMapsTool.initOrganMoveSpriteMove(gameMainView,
				mapView);
		for (int i = 0; i < organViewsTmp.size(); i++) {
			organViews.addElement(organViewsTmp.elementAt(i));
		}
		organViewsTmp = InitMapsTool.initOrganArrow(gameMainView, mapView);
		for (int i = 0; i < organViewsTmp.size(); i++) {
			organViews.addElement(organViewsTmp.elementAt(i));
		}
		organViewsTmp = InitMapsTool.initOrganThunder(gameMainView, mapView);
		for (int i = 0; i < organViewsTmp.size(); i++) {
			organViews.addElement(organViewsTmp.elementAt(i));
		}
		return organViews;
	}

	/**
	 * 初始化普通的机关陷阱
	 */
	public static Vector initNormalOrgan(GameMainView gameMainView,
			MapView mapView) {
		Vector organViews = new Vector();
		Vector organPoint50 = mapView.getAllPoint(50);
		for (int i = 0; i < organPoint50.size(); i++) {
			int[] point = (int[]) organPoint50.elementAt(i);
			String res = "";
			int tileW = 0, tileH = 0;
			int initX = point[1], initY = point[2];
			switch (point[0]) {
			case 2:
				tileW = 10;
				tileH = 33;
				res = "organ/0/1.png";
				break;
			case 4:
				tileW = 33;
				tileH = 10;
				initY = initY + tileH;
				res = "organ/0/2.png";
				break;
			case 6:
				tileW = 10;
				tileH = 33;
				initY = initX - tileW;
				res = "organ/0/3.png";
				break;
			default:
				tileW = 33;
				tileH = 10;
				res = "organ/0/0.png";
				break;
			}
			organViews.addElement(new OrganView(gameMainView, res, "0", tileW,
					tileH, initX, initY));
		}
		return organViews;
	}

	/**
	 * 初始化移动的障碍物
	 */
	public static Vector initOrganMove(GameMainView gameMainView,
			MapView mapView) {
		Vector organViews = new Vector();
		Vector organPoint51 = mapView.getAllPoint(51);
		System.out.println("加载>>>>>>>>>>>>>");
		for (int i = 0; i < organPoint51.size(); i++) {
			int[] point = (int[]) organPoint51.elementAt(i);
			String res = "";
			int initX = point[1], initY = point[2];
			int minPointX = 0, maxPointX = 0;
			int tileW = 20, tileH = 33;
			System.out.println(" ---- point[0] = " + point[0] + " ---- ");
			switch (point[0]) {
			case 0:
				minPointX = mapView.getFristPoint0FromDirection(initX, initY,
						0, true)[0]
						+ mapView.getImgW() + tileW;
				maxPointX = mapView.getFristPoint0FromDirection(initX, initY,
						1, true)[0]
						- mapView.getImgW() - tileW;
				res = "organ/1/1.png";
				break;
			case 4:
				minPointX = mapView.getFristPoint0FromDirection(initX, initY
						- mapView.getImgH(), 0, true)[0]
						+ mapView.getImgW() + tileW;
				maxPointX = mapView.getFristPoint0FromDirection(initX, initY
						- mapView.getImgW(), 1, true)[0]
						- mapView.getImgW() - tileW;
				initY = initY + tileH;
				res = "organ/1/1.png";
				break;
			case 2:
				maxPointX = mapView.getFristPoint0FromDirection(initX, initY,
						1, false)[0]
						- tileW / 2;
				res = "organ/1/1.png";
				initX = initX + tileW / 2;
				minPointX = initX;
				break;
			case 6:
				minPointX = mapView.getFristPoint0FromDirection(initX, initY,
						0, false)[0]
						+ tileW / 2;
				initX = initX - tileW / 2;
				maxPointX = initX;
				res = "organ/1/1.png";
				break;
			default:
				minPointX = mapView.getFristPoint0FromDirection(initX, initY,
						0, false)[0]
						+ tileW;
				maxPointX = mapView.getFristPoint0FromDirection(initX, initY,
						1, false)[0]
						- tileW;
				res = "organ/1/1.png";
				break;
			}
			System.out.println("minPoint="+minPointX+",maxPoint="+maxPointX);
			organViews.addElement(new OrganMoveView(gameMainView, res, "1",
					tileW, tileH, initX, initY, 1, minPointX, maxPointX));
		}
		Vector organPoint52 = mapView.getAllPoint(52);
		for (int i = 0; i < organPoint52.size(); i++) {
			int[] point = (int[]) organPoint52.elementAt(i);
			String res = "";
			int initX = point[1], initY = point[2];
			int minPointY = 0, maxPointY = 0;
			int tileW = 33, tileH = 20;
			switch (point[0]) {
			case 0:
				minPointY = mapView.getFristPoint0FromDirection(initX, initY,
						2, false)[1]
						+ tileH;
				initY = initY;
				maxPointY = initY;
				res = "organ/1/0.png";
				break;
			case 4:
				maxPointY = mapView.getFristPoint0FromDirection(initX, initY,
						3, false)[1];
				initY = initY + tileH;
				minPointY = initY;
				res = "organ/1/0.png";
				break;
			case 2:
				minPointY = mapView.getFristPoint0FromDirection(initX
						- mapView.getImgW(), initY, 2, true)[1]
						+ tileH + mapView.getImgH();
				maxPointY = mapView.getFristPoint0FromDirection(initX
						- mapView.getImgW(), initY, 3, true)[1]
						- mapView.getImgH();
				initX = initX + tileW / 2;
				res = "organ/1/0.png";
				break;
			case 6:
				minPointY = mapView.getFristPoint0FromDirection(initX, initY,
						2, true)[1]
						+ tileH + mapView.getImgH();
				maxPointY = mapView.getFristPoint0FromDirection(initX, initY,
						3, true)[1]
						- mapView.getImgH();
				initX = initX - tileW / 2;
				res = "organ/1/0.png";
				break;
			default:
				minPointY = mapView.getFristPoint0FromDirection(initX, initY,
						2, false)[1]
						+ tileH;
				maxPointY = mapView.getFristPoint0FromDirection(initX, initY,
						3, false)[1];
				res = "organ/1/0.png";
				break;
			}
			organViews.addElement(new OrganMoveView(gameMainView, res, "1",
					tileW, tileH, initX, initY, 0, minPointY, maxPointY));
		}
		return organViews;
	}

	public static Vector initOrganMoveSpriteMove(GameMainView gameMainView,
			MapView mapView) {
		Vector organViews = new Vector();
		Vector organPoint53 = mapView.getAllPoint(53);
		for (int i = 0; i < organPoint53.size(); i++) {
			int[] point = (int[]) organPoint53.elementAt(i);
			int initX = point[1], initY = point[2];
			int minPointX = 0, maxPointX = 0;
			int minPointY = 0, maxPointY = 0;
			int tileW = 120 , tileH = 40;
			if (point[0] == -1) {
				minPointY = mapView.getFristPoint0FromDirection(initX, initY,
						2, false)[1];
				maxPointY = mapView.getFristPoint0FromDirection(initX, initY,
						3, false)[1];
			}
			if (maxPointY - minPointY >= 10 * mapView.getImgH()) {
				minPointX = mapView.getFristPoint0FromDirection(initX,
						minPointY + 4 * mapView.getImgH(), 0, false)[0]
						+ tileW / 2 + 2 * mapView.getImgW();
				maxPointX = mapView.getFristPoint0FromDirection(initX,
						minPointY + 4 * mapView.getImgH(), 1, false)[0]
						- tileW / 2 - 2 * mapView.getImgW();
				initY = minPointY + 4 * mapView.getImgH() + tileH;
				initX = minPointX;
				if (maxPointX - minPointX > 0) {
					organViews.addElement(new OrganMoveSpriteMoveView(
							gameMainView, "1", tileW, tileH, initX, initY, 1,
							minPointX, maxPointX));
				}
			}
		}
		return organViews;
	}

	/**
	 * 初始化箭头障碍物
	 */
	public static Vector initOrganArrow(GameMainView gameMainView,
			MapView mapView) {
		Vector organViews = new Vector();
		// 55是顺时针箭头障碍物
		Vector organPoint55 = mapView.getAllPoint(55);
		for (int i = 0; i < organPoint55.size(); i++) {
			int[] point = (int[]) organPoint55.elementAt(i);
			String res = "";
			int tileW = 0, tileH = 0;
			int initX = point[1], initY = point[2];
			int isClockWise = 0;
			switch (point[0]) {
			case 0:
				tileW = 24;
				tileH = 20;
				res = "organ/2/0.png";
				isClockWise = 1;
				break;
			case 4:
				tileW = 24;
				tileH = 20;
				initY = initY + tileH;
				res = "organ/2/0.png";
				isClockWise = 0;
				break;
			case 2:
				tileW = 20;
				tileH = 24;
				res = "organ/2/1.png";
				isClockWise = 1;
				break;
			default:
				tileW = 20;
				tileH = 24;
				initX = initX - tileW;
				res = "organ/2/1.png";
				isClockWise = 0;
				break;
			}
			organViews.addElement(new OrganArrowView(gameMainView, res, "1",
					isClockWise, tileW, tileH, initX, initY));
		}

		Vector organPoint56 = mapView.getAllPoint(56);
		for (int i = 0; i < organPoint56.size(); i++) {
			int[] point = (int[]) organPoint56.elementAt(i);
			String res = "";
			int tileW = 0, tileH = 0;
			int initX = point[1], initY = point[2];
			int isClockWise = 0;
			switch (point[0]) {
			case 0:
				tileW = 24;
				tileH = 20;
				res = "organ/2/2.png";
				isClockWise = 0;
				break;
			case 4:
				tileW = 24;
				tileH = 20;
				initY = initY + tileH;
				res = "organ/2/2.png";
				isClockWise = 1;
				break;
			case 2:
				tileW = 20;
				tileH = 24;
				res = "organ/2/3.png";
				isClockWise = 0;
				break;
			default:
				tileW = 20;
				tileH = 24;
				initX = initX - tileW;
				res = "organ/2/3.png";
				isClockWise = 1;
				break;
			}
			organViews.addElement(new OrganArrowView(gameMainView, res, "1",
					isClockWise, tileW, tileH, initX, initY));
		}
		return organViews;
	}

	/**
	 * 初始化雷电陷阱
	 */
	public static Vector initOrganThunder(GameMainView gameMainView,
			MapView mapView) {
		Vector organViews = new Vector();
		Vector organPoint57 = mapView.getAllPoint(57);
		for (int i = 0; i < organPoint57.size(); i++) {
			int[] point = (int[]) organPoint57.elementAt(i);
			String res = "";
			int tileW = 0, tileH = 0;
			int initX = point[1], initY = point[2];
			System.out.println("this is point[1] = " + point[1] + ",point[0] = " + point[0]);
			switch (point[0]) {
			case 0:
				tileW = 21;
				tileH = 100;
				initY = initY + 10;
				res = "organ/3/1.png";
				break;
			case 4:
				tileW = 21;
				tileH = 100;
				initY = initY - 10 + tileH;
				res = "organ/3/1.png";
				break;
			case 2:
				tileW = 100;
				tileH = 21;
				initX = initX + tileW / 2 - 10;
				res = "organ/3/3.png";
				break;
			default:
				tileW = 100;
				tileH = 21;
				initX = initX - tileW / 2 + 10;
				res = "organ/3/3.png";
				break;
			}
			organViews.addElement(new OrganThunderView(gameMainView, res, "2",
					point[0], tileW, tileH, initX + 50, initY));
		}
		return organViews;
	}

	/**
	 * 初始化洞门
	 */
	public static DoorView initDoor(GameMainView gameMainView, MapView mapView) {
		DoorView doorView = null;
		Vector door = mapView.getAllPoint(100);
		if (door.size() > 0) {
			int[] point = (int[]) door.elementAt(0);
			int animation = point[0] == 2 ? 1 : point[0] == 4 ? 2
					: point[0] == 6 ? 3 : 0;
			System.out.println("this is animation = " + animation);
			doorView = new DoorView(gameMainView, animation, point[1], point[2]);
		}
		return doorView;
	}
}
