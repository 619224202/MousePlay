package com.game.mouse.modle.service;

import java.util.Hashtable;
import java.util.Vector;

import org.json.me.JSONArray;
import org.json.me.JSONObject;

import com.game.mouse.modle.ResPass;
import com.game.mouse.modle.UserMouse;
import com.model.tool.PubToolKit;

public class DataManageService {
	private static DataManageService insatnce;

	public static DataManageService getInsatnce() {
		if (insatnce == null) {
			insatnce = new DataManageService();
		}
		return insatnce;
	}

	public int[][][] getMapsMessage(int scene, int pass) {
		String passmsg = PubToolKit.getTxtData("/data/pass/sc" + scene
				+ "map" + pass + ".txt", "utf-8");
		String mapdraw = passmsg.substring(10, passmsg.indexOf("}maps=") - 1);
		String[] mapDrawArr = PubToolKit.split(mapdraw, "},{");
		int[][] retDrawmaps = new int[mapDrawArr.length][];
		for (int i = 0; i < mapDrawArr.length; i++) {
			String[] retDraw = PubToolKit.split(mapDrawArr[i], ",");
			retDrawmaps[i] = new int[retDraw.length];
			for (int j = 0; j < retDraw.length; j++) {
				retDrawmaps[i][j] = Integer.parseInt(retDraw[j].trim());
			}
		}

		String maps = passmsg.substring(passmsg.indexOf("}maps=") + 8, passmsg
				.indexOf("}datas=") - 1);
		String[] mapArr = PubToolKit.split(maps, "},{");
		int[][] retmaps = new int[mapArr.length][];
		for (int i = 0; i < mapArr.length; i++) {
			String[] map = PubToolKit.split(mapArr[i], ",");
			retmaps[i] = new int[map.length];
			for (int j = 0; j < map.length; j++) {
				retmaps[i][j] = Integer.parseInt(map[j].trim());
			}
		}

		String datas = passmsg.substring(passmsg.indexOf("}datas=") + 9,
				passmsg.length() - 2);
		String[] datasArr = PubToolKit.split(datas, "},{");
		int[][] retdatas = new int[datasArr.length][];
		for (int i = 0; i < datasArr.length; i++) {
			String[] data = PubToolKit.split(datasArr[i], ",");
			retdatas[i] = new int[data.length];
			for (int j = 0; j < data.length; j++) {
				retdatas[i][j] = Integer.parseInt(data[j].trim());
			}
		}
		int[][][] mapsmes = { retDrawmaps, retmaps, retdatas };
		return mapsmes;
	}

	/**
	 * 获得角色信息
	 * 
	 * @return
	 */
	public UserMouse[] getAllUserMouse() {
		UserMouse[] userMouse = new UserMouse[3];
		for (int i = 0; i < 3; i++) {
			userMouse[i] = new UserMouse(i + "");
		}
		return userMouse;
	}

	/**
	 * 获得角色信息
	 * 
	 * @return
	 */
	public UserMouse getUserMouseByCode(String code) {
		UserMouse userMouse = new UserMouse(code);
		return userMouse;
	}

	/**
	 * 
	 * 得到每关的配置信息
	 */
	public ResPass getScenePassMsg(int sceneCode, int pass) {
		ResPass resPass = new ResPass();
		Vector cats = new Vector();
		String scenemsg = PubToolKit.getTxtData("/data/pass/sc" + sceneCode
				+ "pass.txt", "GB2312");
		JSONArray jsonArrData = null;
		JSONObject passObj = null;
		try {
			jsonArrData = new JSONArray(scenemsg);
			Vector scenePass = new Vector();
			passObj = jsonArrData.getJSONObject(pass);
			resPass.setCode(passObj.getInt("code"));
			JSONArray catsArr = passObj.getJSONArray("cats");
			for (int j = 0; j < catsArr.length(); j++) {
				JSONObject catsObj = catsArr.getJSONObject(j);
				Hashtable catsmsg = new Hashtable();
				catsmsg.put("code", new Integer(catsObj.getInt("code")));
				catsmsg.put("hp", new Integer(catsObj.getInt("hp")));
				catsmsg.put("att", new Integer(catsObj.getInt("att")));
				catsmsg.put("lv", new Integer(catsObj.getInt("lv")));
				cats.addElement(catsmsg);
			}
			resPass.setCats(cats);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resPass;
	}

	/**
	 * 根据猫的编号得到猫的配置信息
	 * 
	 * @param catCode
	 * @return
	 */
	public Hashtable getCatMsgByCode(int catCode) {
		Hashtable catTable = new Hashtable();
		String cats = PubToolKit.getTxtData("/data/cats.txt", "GB2312");
		JSONArray jsonCatsData = null;
		try {
			jsonCatsData = new JSONArray(cats);
			for (int j = 0; j < jsonCatsData.length(); j++) {
				JSONObject catsObj = jsonCatsData.getJSONObject(j);
				if (catsObj.getInt("code") == catCode) {
					catTable.put("code", new Integer(catsObj.getInt("code")));
					catTable.put("moveType", new Integer(catsObj
							.getInt("moveType")));
					catTable.put("speed", new Integer(catsObj.getInt("speed")));
					catTable.put("jump", new Integer(catsObj.getInt("jump")));
					catTable.put("eatCheese", new Integer(catsObj
							.getInt("eatCheese")));
					catTable.put("carryCheese", new Integer(catsObj
							.getInt("carryCheese")));
					JSONArray organsArr = catsObj.getJSONArray("organs");
					int[] organs = new int[organsArr.length()];
					for (int i = 0; i < organsArr.length(); i++) {
						organs[i] = Integer.parseInt(organsArr.getString(i));
					}
					catTable.put("organs", organs);
					break;
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return catTable;
	}
}
