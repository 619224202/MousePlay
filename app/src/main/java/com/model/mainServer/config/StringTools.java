package com.model.mainServer.config;

import java.util.Random;
import java.util.Vector;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public class StringTools {
	public static int drawString(String str, int x, int y, int width,
			Font font, int color, Graphics g) {
		String msg[] = new String[1];
		if (font.stringWidth(str) > width) {
			msg = splitString(str, width, font);
		} else {
			msg[0] = str;
		}
		int temY = y;
		g.setColor(color);
		g.setFont(font);
		for (int i = 0; i < msg.length; i++) {
			int intx = x + width / 2 - font.stringWidth(msg[i]) / 2;
			g.drawString(msg[i], intx, temY, 0);
			temY += font.getHeight();
		}
		return temY;
		// return x+font.stringWidth(msg[i])/2;
	}

	/**
	 * 把字符串分割成字符串数组，根据maxLen分割，每段字符串的长度<=maxLen 单词数字等不能拆开换行的字符串，使用$标示，如：$hello$
	 * 
	 * @param str
	 *            原始字符串
	 * @param maxLen
	 *            分割的字符串的最大长度
	 * @param font
	 *            字符串使用的字体
	 * @return
	 */
	public static String[] splitString(String str, int maxLen, Font font) {
		if (str.length() == 0)
			return null;
		Vector v = new Vector();
		int firstindex = 0;
		char[] cArray = str.toCharArray();
		StringBuffer strBuf = new StringBuffer();
		boolean markBegin = false;// 标记开始标志
		int markIdx = 0;
		// System.out.println("len is: "+font.stringWidth(str));
		// System.out.println("sub len is:
		// "+font.stringWidth(str.substring(0,str.length())));
		if (font.stringWidth(str) <= maxLen) {
			v.addElement(str);
		} else {
			String strMark = "";// 标记的字符串
			int currStrW = 0;// 当前字符串宽度
			String result = "";// 存储每行最终生成的字符串
			for (int i = 0; i < str.length(); i++) {
				String slitStr = str.substring(i, i + 1);
				if ("$".equals(slitStr)) {
					markBegin = !markBegin;
					markIdx = i;
				} else {
					currStrW += font.stringWidth(slitStr);
				}

				if (currStrW >= maxLen) {
					if (markBegin) {// 处于标记字符串中
						v.addElement(str.substring(firstindex, markIdx));
						i = markIdx - 1;
						firstindex = markIdx;
						markBegin = false;

					} else {
						if (currStrW > maxLen) {
							i--;
						}
						v.addElement(str.substring(firstindex, i));
						firstindex = i;
					}
					currStrW = 0;
				}
			}
			// System.out.println("fristindex"+fristindex);
			if (firstindex < str.length()) {// 最后剩余的不满一行的字符串
				v.addElement(str.substring(firstindex));
			}
		}

		String[] result = new String[v.size()];
		for (int i = 0; i < v.size(); i++) {
			result[i] = (String) v.elementAt(i);
		}
		return result;
	}

	public static int currentFrame = 0;

	/**
	 * 画选框：以中心点为坐标
	 * 
	 * @param g
	 * @param image
	 * @param tileW
	 * @param tileH
	 * @param w
	 * @param h
	 * @param x
	 * @param y
	 */
	public static void drawSelIcon(Graphics g, Image image, int w, int h,
			int x, int y) {
		currentFrame++;
		if (currentFrame == 100) {
			currentFrame = 0;
		}
		if ((currentFrame % 4) / 2 == 0) {
			w += 4;
			h += 2;
		} else {
			w += 10;
			h += 4;
		}
		int tileW = image.getWidth() / 2;
		int tileH = image.getHeight() / 2;
		int[] posX = new int[2];
		int[] posY = new int[2];
		posX[0] = x - w / 2;
		posY[0] = y - h / 2;
		posX[1] = x + w / 2;
		posY[1] = y + h / 2;
		g.drawRegion(image, 0, 0, tileW, tileH, 0, posX[0], posY[0],
				Graphics.LEFT | Graphics.TOP);// 左上角
		g.drawRegion(image, tileW, 0, tileW, tileH, 0, posX[1], posY[0],
				Graphics.RIGHT | Graphics.TOP);// 右上角
		g.drawRegion(image, 0, tileH, tileW, tileH, 0, posX[0], posY[1],
				Graphics.LEFT | Graphics.BOTTOM);
		g.drawRegion(image, tileW, tileH, tileW, tileH, 0, posX[1], posY[1],
				Graphics.RIGHT | Graphics.BOTTOM);
	}

	/**
	 * @param g
	 * @param num
	 *            待绘制数字
	 * @param img
	 *            数字图片
	 * @param x
	 *            起始x坐标
	 * @param y
	 *            起始y坐标
	 * @param w
	 *            数字间间隔
	 * @param lr
	 *            是否从左向右绘画
	 * @return 最后的x坐标
	 */
	public static int drawNum(Graphics g, int num, Image img, int x, int y,
			int w, int anchor, boolean lr) {
		StringBuffer sb = new StringBuffer("");
		int a;
		if (num == 0) {
			sb.append(0);
		}
		while (num != 0) {// buffer保存数字逆向排列
			a = num % 10;
			sb.append(a);
			num = num / 10;
		}
		int nw = img.getWidth() / 10;
		int nx = 0, ny = y;
		if (anchor == (Graphics.VCENTER | Graphics.HCENTER)) {
			int len = sb.length() / 2;
			// System.out.println(num+" ,"+len);
			x -= (len * nw + ((len + 1) % 2) * nw / 2);
			anchor = Graphics.LEFT | Graphics.VCENTER;
			// if(len%2==1){
			//				
			// }
		}
		for (int i = 0; i < sb.length(); i++) {// 从左向右绘画
			int n;

			if (lr) {
				n = sb.charAt(sb.length() - i - 1) - '0';
				nx = x + (w + nw) * i;
				g.drawRegion(img, n * nw, 0, nw, img.getHeight(), 0, nx, ny,
						anchor);
			} else {
				n = sb.charAt(i) - '0';
				nx = x - (w + nw) * i - nw;// 多减去一个数字的宽度
				g.drawRegion(img, n * nw, 0, nw, img.getHeight(), 0, nx, ny,
						anchor);
			}
		}
		if (lr) {
			return nx + nw;
		} else {
			return nx;
		}
	}

	public static String encode(String s) {
		String ret = "";
		char[] ch = s.toCharArray();
		int[] changeInt = new int[ch.length];
		for (int i = 0; i < ch.length; i++) {
			changeInt[i] = ch[i];
			char a = (char) changeInt[i];
			ret = ret + "|" + changeInt[i];
		}
		return ret;
	}

	public static Random rd = new Random();

	public static int getRandomInt(int seed) {
		return (rd.nextInt() >>> 1) % seed;
	}

	public static String[] split(String original, String regex) {
		int startIndex = 0;
		Vector v = new Vector();
		String[] str = new String[1];
		int index = 0;
		startIndex = original.indexOf(regex);
		if (startIndex == -1) {
			str[0] = original;
			return str;
		}
		while (startIndex < original.length() && startIndex != -1) {
			v.addElement(original.substring(index, startIndex));
			index = startIndex + regex.length();
			startIndex = original.indexOf(regex, startIndex + regex.length());
		}
		v.addElement(original.substring(index));
		str = new String[v.size()];
		for (int i = 0; i < v.size(); i++) {
			str[i] = (String) v.elementAt(i);
		}
		return str;
	}

}
