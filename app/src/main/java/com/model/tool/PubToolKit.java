package com.model.tool;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import java.io.InputStream;

import java.util.Random;
import java.util.Vector;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import com.game.lib9.Engine;
import com.game.lib9.L9Config;
import com.game.mouse.screen.MainMidlet;
import com.model.mainServer.MainServer;


/**
 * 类说明
 * 
 * 创建时间：2010-12-14 下午04:39:41
 * 
 * @author Chaos Song
 */

public class PubToolKit {
	/**
	 * 居中画
	 * 
	 * @param str
	 * @param x
	 * @param y
	 * @param width
	 * @param font
	 * @param color
	 * @param g
	 * @return 绘制完成之后的坐标
	 */
	public static int drawString(String str, int x, int y, int width,
			Font font, int color, Graphics g) {
		String msg[] = new String[1];
		float fWidth = width*MainMidlet.scaleX;
		if (font.stringWidth(str) >fWidth) {
			msg = splitString(str, (int)(fWidth), font);
		} else {
			msg[0] = str;
		}
		int temY = y;
		g.setColor(color);
		g.setFont(font);
		for (int i = 0; i < msg.length; i++) {
			int intx = (int)(x*MainMidlet.scaleX + fWidth / 2 - font.stringWidth(msg[i]) / 2);
			g.drawString(msg[i], intx, (int)(temY*MainMidlet.scaleY), 0);
			temY += font.getHeight();
		}
		return temY;
	}

	public static int drawString(String str, int x, int y, int width,
			Font font, int color, Graphics g, boolean isDrawLeft) {
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
			int intx = x;
			if (!isDrawLeft) {
				intx = x + width / 2 - font.stringWidth(msg[i]) / 2;
			}
			g.drawString(msg[i], intx, temY, 0);
			temY += font.getHeight();
		}
		return temY;
		// return x+font.stringWidth(msg[i])/2;
	}

	/**
	 * 画文字与图片一起
	 */
	// public static int drawString(Graphics g, String str, Font font, int
	// width,
	// int x, int y, int color, String[] replaceStr, Image[] replaceImg) {
	// if (replaceStr == null || replaceImg == null || replaceStr.length == 0
	// || replaceImg.length == 0||replaceImg.length!=replaceStr.length) {
	// return drawString(str, x, y, width, font, color, g);
	// } else {
	//			
	// }
	// }
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
			if (firstindex < str.length()) {// 最后剩余的不满一行的字符串
				v.addElement(str.substring(firstindex));
			}
		}

		String[] result = new String[v.size()];
		for (int i = 0; i < v.size(); i++) {
			result[i] = replaceToNull((String) v.elementAt(i), '$');
		}
		return result;
	}

	/**
	 * 该字符串，画每行长度为length的长度 返回宽度和长度
	 */

	public static int[] drawString(Graphics g, String s, Font font, int lenth,
			int x, int y, int color) {
		int index = (s.length() % lenth == 0) ? s.length() / lenth : (s
				.length()
				/ lenth + 1);
		StringBuffer[] strBuf = new StringBuffer[index];
		int h = index * font.getHeight();
		g.setColor(color);
		g.setFont(font);
		int tempY = y - h;
		int tempX = 0;
		for (int i = 0; i < index; i++) {
			strBuf[i] = new StringBuffer();
			if (i < (index - 1)) {
				strBuf[i].append(s.substring(i * lenth, (i + 1) * lenth));
			} else {
				strBuf[i].append(s.substring(i * lenth));
			}
			if (tempX == 0) {
				tempX = x - font.stringWidth(strBuf[0].toString()) / 2;
			}
			g.drawString(strBuf[i].toString(), tempX, tempY, 0);
			tempY += font.getHeight();
		}
		int[] pos = new int[2];
		pos[0] = font.stringWidth(strBuf[0].toString());
		pos[1] = h;
		return pos;
	}

	public static String replaceToNull(String s, char c) {
		char[] cArray = s.toCharArray();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < cArray.length; i++) {
			if (c != cArray[i]) {
				sb.append(cArray[i]);
			}
		}
		return sb.toString();
	}

	private static final Random rd = new Random();

	/**
	 * 取得seed以内的随机正整数
	 * 
	 * @param seed
	 * @return
	 */
	public static int getRandomInt(int seed) {
		return (rd.nextInt() >>> 1) % seed;
	}

	public static Vector getNumVector(int l) {
		int count = l;
		Vector numVector = new Vector();
		while (count != 0) {
			int k = count % 10;
			numVector.insertElementAt(new Integer(k), 0);
			count = count / 10;
		}
		return numVector;
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
		int nw= (int)((img.getWidth()/10)/MainMidlet.scaleX);
		int nx = 0, ny = y;
		if (anchor == (Graphics.VCENTER | Graphics.HCENTER)) {
			int len = sb.length() / 2;
			x -= ((len * nw + ((len + 1) % 2) * nw / 2));
			anchor = Graphics.LEFT | Graphics.VCENTER;
		}
		for (int i = 0; i < sb.length(); i++) {// 从左向右绘画
			int n;
			if (lr) {
				n = sb.charAt(sb.length() - i - 1) - '0';
				nx = x + (w+nw) * i;
				g.drawRegion(img, (int)(n * nw * MainMidlet.scaleX), 0,(int)(nw* MainMidlet.scaleX), (int)(img.getHeight() * MainMidlet.scaleY), 0, (int)(nx*MainMidlet.scaleX), (int)(ny*MainMidlet.scaleY),
						anchor);
			} else {
				n = sb.charAt(i) - '0';
				nx = x - (w + nw) * i - nw;// 多减去一个数字的宽度
				g.drawRegion(img, (int)(n * nw*MainMidlet.scaleX), 0, (int)(nw*MainMidlet.scaleX), (int)(img.getHeight()*MainMidlet.scaleY), 0, (int)(nx*MainMidlet.scaleX), (int)(ny*MainMidlet.scaleY),
						anchor);
			}
		}
		if (lr) {
			return nx + nw;
		} else {
			return nx;
		}
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
			int w, int anchor, boolean lr, boolean c) {
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
		int nw = img.getWidth() / 11;
		int nx = 0, ny = y;
		if (anchor == (Graphics.VCENTER | Graphics.HCENTER)) {
			int len = sb.length() / 2;
			anchor = Graphics.LEFT | Graphics.VCENTER;
			x -= len * nw;
		}
		g.drawRegion(img, 0, 0, nw, img.getHeight(), 0, x, y, anchor);
		for (int i = 0; i < sb.length(); i++) {// 从左向右绘画
			int n;

			if (lr) {
				n = sb.charAt(sb.length() - i - 1) - '0';
				nx = x + (w + nw) * (i + 1);
				// System.out.println(n+1);
				g.drawRegion(img, (n + 1) * nw, 0, nw, img.getHeight(), 0, nx,
						ny, anchor);
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

	public static int getAnchorOffset(int anchor, int w, int h) {
		int offX = 0;
		int offY = 0;
		if ((anchor & 0x40) != 0)
			throw new IllegalArgumentException();
		if ((anchor & 0x1) != 0)
			offX = w >> 1;
		else if ((anchor & 0x8) != 0)
			offX = w;

		if ((anchor & 0x2) != 0)
			offY = h >> 1;
		else if ((anchor & 0x20) != 0)
			offY = h;

		return (offX << 16 | offY);
	}

	public static void drawString(Graphics g, Image img, String str,
			String fontStoreroom, int x, int y, int clipW, int clipH,
			int anchor, int space, int numDigit, int color) {
		StringBuffer sb = new StringBuffer();
		sb.append(str);
		for (int i = sb.length(); i < numDigit; ++i) {
			sb.insert(0, "0");
		}

		str = sb.toString();
		int strLength = str.length();
		int offset = getAnchorOffset(anchor, strLength * clipW + space, clipH);
		x -= ((offset & 0xFFFF0000) >> 16);
		y -= (offset & 0xFFFF);
		for (int i = 0; i < strLength;) {
			int charId = fontStoreroom.indexOf(str.charAt(i));
			if (charId == -1) {
				g.setClip((int)(x * MainMidlet.scaleX), (int)(y * MainMidlet.scaleY), (int)(clipW * MainMidlet.scaleX), (int)(Font.getFont(0, 1, 16).getHeight() * MainMidlet.scaleY));
				g.setColor(color);
				g.drawChar(str.charAt(i), (int)(x*MainMidlet.scaleX),(int)(y*MainMidlet.scaleY), 0);
			} else {
				g.setClip((int)(x * MainMidlet.scaleX), (int)(y * MainMidlet.scaleY), (int)(clipW * MainMidlet.scaleX), (int)(clipH * MainMidlet.scaleY));
				g.drawImage(img, (int)((x - charId * clipW)*MainMidlet.scaleX), (int)(y*MainMidlet.scaleY), 0);
			}
			++i;
			x += clipW + space;
		}
		g.setClip(0, 0, (int)(L9Config.SCR_W * MainMidlet.scaleX), (int)(L9Config.SCR_H * MainMidlet.scaleY));
	}

	public static int[] getSigleInt(int num) {

		String relStr = String.valueOf(num);
		int size = relStr.length();
		int[] result = new int[size];
		String temp = null;
		for (int i = 0; i < size; i++) {
			temp = relStr.substring(i, i + 1);
			result[i] = Integer.valueOf(temp).intValue();
		}
		return result;
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

	/**
	 * 截取XML中指定标签的值
	 * 
	 * @param ori
	 *            xml的字符串
	 * @param regex
	 *            截取的标签元素
	 * @return
	 */
	public static String[] splitXml(String ori, String regex) {
		if (ori == null || ori.length() == 0)
			return null;
		Vector v = new Vector();
		StringBuffer sb = new StringBuffer();
		String startTag = sb.append("<").append(regex).append(">").toString();
		StringBuffer endSb = new StringBuffer();
		String endTag = endSb.append("<").append("/").append(regex).append(">")
				.toString();
		int startIndex = 0;
		int endIndex = 0;
		startIndex = ori.indexOf(startTag);
		endIndex = ori.indexOf(endTag);
		while (startIndex < ori.length() && endIndex != -1 && startIndex != -1) {
			v.addElement(ori
					.substring(startIndex + startTag.length(), endIndex));
			System.out.println(ori.substring(startIndex + startTag.length(),
					endIndex));
			startIndex = ori.indexOf(startTag, endIndex + endTag.length());
			endIndex = ori.indexOf(endTag, startIndex);
		}
		int count = v.size();
		if (count == 0)
			return null;

		String[] str = new String[30];

		for (int i = 0; i < count; i++) {
			str[i] = (String) v.elementAt(i);
		}

		return str;
	}

	public static String getTxtData(String filename, String sEncode) {
		String strReturn = "";
		InputStream in = null;
		ByteArrayOutputStream baos = null;

		try {
			in = MainMidlet.getResourceAsStream(filename);
			String line;
			while ((line = readline(in)) != null) {
				strReturn += line.trim();
			}
			strReturn = strReturn.trim();
//			baos = new ByteArrayOutputStream();
//			// 循环读取数据
//			int ch;
//			while ((ch = in.read()) != -1) {
//				baos.write(ch);
//			}
//			in.close();
//			strReturn = new String(baos.toByteArray());
//			baos.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			in = null;
		}
		return strReturn.trim();
	}

//	public static String getTxtDataOne(String filename, String sEncode) {
//		String strReturn = "";
//		InputStream instr = null;
//		InputStreamReader in = null;
//		String ret = "";
//		try {
//			instr = "".getClass().getResourceAsStream(filename);
//			in = new InputStreamReader(instr);
//			String line;
//			while ((line = readline(in)) != null) {
//				strReturn += line.trim();
//			}
//			ret = strReturn.trim();
//			// ret = new String(strReturn.trim().getBytes(), sEncode);
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			try {
//				in.close();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			in = null;
//			instr = null;
//			System.gc();
//		}
//		return ret;
//	}

	public static String readline(InputStream in) {
		int ch;
		StringBuffer line = null;
		try {
			while (true) {
				ch = in.read();
				if (ch == -1) {
					if (line == null)
						return null;
					return line.toString();
				}
				if (ch == 10 || ch == 13) {
					// eol if we read other characters, ignore if not
					if (line != null)
						return line.toString();
				} else {
					if (line == null)
						line = new StringBuffer();
					line.append((char) ch);
				}
			}
		} catch (IOException e) {
			if (line == null)
				return null;
			return line.toString();
		}
	}

	private Font strFont;
	/**
	 * 画字体
	 */
	{
		strFont = Font.getFont(0, 0,
				Font.SIZE_MEDIUM);
	}

	public static void fillScreen(Graphics g) {
		g.setClip(0, 0, (int)(745 * MainMidlet.scaleX), (int)(535 * MainMidlet.scaleY));
		g.setColor(0);
		g.fillRect(0, 0, 745, 535);
	}

	public static int getFont16Height() {
		Font font1 = Font.getFont(0, 0, 16);
		return font1.getHeight();
	}

	public static int getFont8Height() {
		Font font1 = Font.getFont(0, 0, 8);
		return font1.getHeight();
	}

	/**
	 * 获得大于20的号字
	 * 
	 * @return
	 */
	public static int getFontBig16And24Size() {
		int font8 = getFont8Height();
		int font16 = getFont16Height();
		if (font16 <= 24) {
			return 16 + 10;
		} else if (font16 > 24) {
			return 8 + 10;
		} else if (getFont8Height() >= 16) {
			return 8 + 10;
		} else {
			return 16 + 10;
		}
	}
}
