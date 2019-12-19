package com.model.base;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Vector;

import com.game.mouse.screen.MainMidlet;
import com.model.control.MSDateManager;
import com.model.tool.PubToolKit;

public class ParserTbl {

	private static ParserTbl instance;

	public static ParserTbl getInstance() {
		if (instance == null) {
			instance = new ParserTbl();
		}
		return instance;
	}

	public void defineMedia(int readtype, String filename) {
		int lnr = 1;
		filename = "/" + filename;
		try {
			InputStream instr =MainMidlet.getResourceAsStream(filename);
			if (instr == null) {
				System.out.println(filename + "找不到文件，文件为空。。。。。。。。。。。。。。。。。");
				return;
			}
			String line;
			String[] fields = new String[14];
			while ((line = readlineByType(1, instr)) != null) {
				int i = 0;
				System.out.println("line="+line);
				Vector tokens = tokenizeString(line, '-');
				for(int k=0;k<tokens.size();k++){
					fields[i++] = (String) tokens.elementAt(k);
				}
//				for (Enumeration e = tokens.elements(); e.hasMoreElements();) {
//					fields[i++] = (String) e.nextElement();
//				}

				int type = 0;
				if (!fields[0].startsWith("//")) {
					type = Integer.parseInt(fields[0]);
				}
				if (type == 1) {
					System.out.println(fields[1] + ":" + fields[2]);
					addFileKeyAndName(fields[1], fields[2]);
				} else if (type == 2) {
					System.out.println(fields[1] + ":" + fields[2]);
					addAnuKeyAndPngKey(fields[1], fields[2]);
				} else if (type == 3) {
					addPngInfo(fields[1], fields[2], fields[3]);
				}
				lnr++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String readlineByType(int type, InputStream instr) {
		if (type == 0) {
			return readline1(instr);
		} else {
			return readline2(instr);
		}
	}

	public static String readline1(InputStream instr) {
		InputStreamReader in = null;
		int ch;
		StringBuffer line = null;
		try {
			in = new InputStreamReader(instr);
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

	public static String readline2(InputStream in) {
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

	/**
	 * Replacement for stringTokenizer. str will be split into a Vector of
	 * tokens. Empty tokens are skipped. splitchar is a single character
	 * indicating a token boundary (multiple characters are not used here). The
	 * split characters are not included.
	 */
	public static Vector tokenizeString(String str, char splitchar) {
		Vector tok = new Vector(20, 50);
		int curidx = 0, nextidx;
		while ((nextidx = str.indexOf(splitchar, curidx)) >= 0) {
			if (nextidx > curidx)
				tok.addElement(str.substring(curidx, nextidx));
			curidx = nextidx + 1;
		}
		if (curidx < str.length())
			tok.addElement(str.substring(curidx));
		return tok;
	}

	public void addFileKeyAndName(String fileKey, String fileName) {
		MSDateManager.getInstance().addFileNameAndKey(fileKey, fileName);
	}

	public void addAnuKeyAndPngKey(String fileKey, String pngKeys) {
		String[] pngKey = PubToolKit.split(pngKeys, ",");
		MSDateManager.getInstance().addAnuKey(fileKey, pngKey);
	}

	public void addPngInfo(String fileKey, String fileName, String splits) {
		String[] splitIntToStr = PubToolKit.split(splits, ",");
		int[] splitInt = new int[splitIntToStr.length];
		for (int i = 0; i < splitInt.length; i++) {
			splitInt[i] = Integer.parseInt(splitIntToStr[i]);
		}
		addFileKeyAndName(fileKey, fileName);
		MSDateManager.getInstance().addPngSplitTable(fileKey, splitInt);
	}
}
