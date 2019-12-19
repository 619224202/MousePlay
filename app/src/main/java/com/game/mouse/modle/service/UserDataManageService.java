package com.game.mouse.modle.service;

import java.util.Vector;

import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.game.mouse.modle.Pass;
import com.game.mouse.modle.ScenePass;
import com.game.mouse.modle.UserMouse;
import com.model.mainServer.MainServer;

public class UserDataManageService {
	private boolean isFirstLogin;

	private JSONObject jsonData;

	private String dataString = "{\"scenes\":["
			+ "{\"code\":0,\"isopen\":1,\"pass\":[{\"code\":0,\"isopen\":1,\"starnum\":0,\"isfrist\":1},{\"code\":1,\"isopen\":0,\"starnum\":0,\"isfrist\":1},{\"code\":2,\"isopen\":0,\"starnum\":0,\"isfrist\":1},{\"code\":3,\"isopen\":0,\"starnum\":0,\"isfrist\":1},{\"code\":4,\"isopen\":0,\"starnum\":0,\"isfrist\":1},{\"code\":5,\"isopen\":0,\"starnum\":0,\"isfrist\":1}]},"
			+ "{\"code\":1,\"isopen\":0,\"pass\":[{\"code\":0,\"isopen\":0,\"starnum\":0,\"isfrist\":1},{\"code\":1,\"isopen\":0,\"starnum\":0,\"isfrist\":1},{\"code\":2,\"isopen\":0,\"starnum\":0,\"isfrist\":1},{\"code\":3,\"isopen\":0,\"starnum\":0,\"isfrist\":1},{\"code\":4,\"isopen\":0,\"starnum\":0,\"isfrist\":1},{\"code\":5,\"isopen\":0,\"starnum\":0,\"isfrist\":1}]},"
			+ "{\"code\":2,\"isopen\":0,\"pass\":[{\"code\":0,\"isopen\":0,\"starnum\":0,\"isfrist\":1},{\"code\":1,\"isopen\":0,\"starnum\":0,\"isfrist\":1},{\"code\":2,\"isopen\":0,\"starnum\":0,\"isfrist\":1},{\"code\":3,\"isopen\":0,\"starnum\":0,\"isfrist\":1},{\"code\":4,\"isopen\":0,\"starnum\":0,\"isfrist\":1},{\"code\":5,\"isopen\":0,\"starnum\":0,\"isfrist\":1}]},"
			+ "{\"code\":3,\"isopen\":0,\"pass\":[{\"code\":0,\"isopen\":0,\"starnum\":0,\"isfrist\":1},{\"code\":1,\"isopen\":0,\"starnum\":0,\"isfrist\":1},{\"code\":2,\"isopen\":0,\"starnum\":0,\"isfrist\":1},{\"code\":3,\"isopen\":0,\"starnum\":0,\"isfrist\":1},{\"code\":4,\"isopen\":0,\"starnum\":0,\"isfrist\":1},{\"code\":5,\"isopen\":0,\"starnum\":0,\"isfrist\":1}]}"
			+ "],\"mouses\":[{\"code\":2,\"isoutpk\":1}],\"props\":[{\"code\":0,\"num\":0},{\"code\":1,\"num\":0},{\"code\":1,\"num\":0},{\"code\":2,\"num\":0},{\"code\":3,\"num\":0},{\"code\":5,\"num\":0}],\"winnum\":0}";

	private static UserDataManageService insatnce;

	private static boolean iserror;

	public String getDataString() {
		return dataString;
	}

	public void setDataString(String dataString) {
		this.dataString = dataString;
	}

	//---- 我写的逻辑 ----
	public void myLoadGameData(){
		MyGameData.getInstance().loadData();
	}

	//---- 我写的逻辑 ----
	public void mySaveGameData(){
		MyGameData.getInstance().saveData(jsonData.toString());
		System.out.println("jsonData.toString = " + jsonData.toString());
	}

	public void initJsonData() {
//		String dataStringTmp = MainServer.getInstance().getData();
		MyGameData.getInstance().loadData();
		String dataStringTmp = MyGameData.getInstance().content;
		System.out.println("::::" + dataStringTmp);
		try {
			if (dataStringTmp != null && dataStringTmp.trim().length() > 0
					&& !dataStringTmp.startsWith("null")
					&& dataStringTmp.indexOf("scenes") > -1
					&& dataStringTmp.indexOf("mouses") > -1
					&& dataStringTmp.indexOf("props") > -1) {
				jsonData = new JSONObject(dataStringTmp);
			} else {
				jsonData = new JSONObject(dataString);
				isFirstLogin = true;
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			iserror = true;
			e.printStackTrace();
		}
	}

	private UserDataManageService() {
		initJsonData();
	}

	public static UserDataManageService getInsatnce() {
		if (insatnce == null) {
			insatnce = new UserDataManageService();
		}
		return insatnce;
	}

	public static boolean isIserror() {
		return iserror;
	}

	/**
	 * 得到所有场景的信息
	 * 
	 * @return
	 * @throws JSONException
	 */
	public Vector getAllSceneMsg() {
		Vector allScene = new Vector();
		try {
			JSONArray scenesJson = jsonData.getJSONArray("scenes");
			for (int i = 0; i < scenesJson.length(); i++) {
				ScenePass scenePass = new ScenePass();
				JSONObject sceneObj = scenesJson.getJSONObject(i);
				scenePass.setCode(sceneObj.getString("code"));
				scenePass.setIsOpen(sceneObj.getInt("isopen"));
				allScene.addElement(scenePass);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			iserror = true;
			e.printStackTrace();
		}
		return allScene;
	}

	/**
	 * 得到最后打开的场景
	 * 
	 * @return
	 * @throws JSONException
	 */
	public ScenePass getLastOpenScene() {
		ScenePass lastOpenScene = null;
		Vector allScenePass = this.getAllSceneMsg();
		for (int i = 0; i < allScenePass.size(); i++) {
			ScenePass scenePass = (ScenePass) allScenePass.elementAt(i);
			if (scenePass.getIsOpen() == 1) {
				lastOpenScene = this.getSceneAllPassBySceneCode(Integer
						.parseInt(scenePass.getCode()));
			} else {
				break;
			}
		}
		return lastOpenScene;
	}

	/**
	 * 获得场景中所有关卡信息
	 * 
	 * @param scene
	 * @return
	 */
	public ScenePass getSceneAllPassBySceneCode(int scene) {
		ScenePass scenePass = new ScenePass();
		try {
			JSONArray scenesJson = jsonData.getJSONArray("scenes");
			JSONObject sceneObj = scenesJson.getJSONObject(scene);
			scenePass.setCode(sceneObj.getString("code"));
			scenePass.setIsOpen(sceneObj.getInt("isopen"));
			JSONArray passJson = sceneObj.getJSONArray("pass");
			for (int j = 0; j < passJson.length(); j++) {
				JSONObject passObj = passJson.getJSONObject(j);
				Pass pass = new Pass(scene, passObj.getInt("code"));
				pass.setIsOpen(passObj.getInt("isopen"));
				pass.setStarNum(passObj.getInt("starnum"));
				if (passObj.has("isfrist"))
					pass.setIsfrist(passObj.getInt("isfrist") == 1);
				scenePass.addPass(pass);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return scenePass;
	}

	/**
	 * 得到场景中最后打开的关卡
	 * 
	 * @param scenePass
	 * @return
	 */
	public Pass getLastOpenPass(ScenePass scenePass) {
		Pass lastPass = null;
		for (int i = 0; scenePass != null && i < scenePass.getPass().size(); i++) {
			Pass pass = (Pass) scenePass.getPass().elementAt(i);
			if (pass.getIsOpen() == 1) {
				lastPass = pass;
			} else {
				break;
			}
		}
		return lastPass;
	}

	public JSONObject getUserMousesByCode(String code) {
		try {
			JSONArray mousesJson = jsonData.getJSONArray("mouses");
			for (int i = 0; i < mousesJson.length(); i++) {
				JSONObject tanObj = mousesJson.getJSONObject(i);
				if (code.equals(tanObj.getString("code"))) {
					return tanObj;
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 角色购买
	 * 
	 * @param mouse
	 * @throws JSONException
	 */
	public void buyMouses(UserMouse mouse) {
		try {
			JSONArray mousesJson = jsonData.getJSONArray("mouses");
			JSONObject mousesObj = new JSONObject();
			mousesObj.put("code", mouse.getCode());
			mousesObj.put("lv", mouse.getLv());
			mousesObj.put("hp", mouse.getMaxhp());
			mousesObj.put("mood", mouse.getMood());
			mousesObj.put("exp", mouse.getExp());
			mousesObj.put("blue", mouse.getFightBlue());
			mousesObj.put("isoutpk", 0);
			mousesJson.put(mousesJson.length(), mousesObj);
			jsonData.put("mouses", mousesJson);
			MainServer.getInstance().saveData(jsonData.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 更新宠物数据
	 * 
	 * @param mouse
	 * @throws JSONException
	 */
	public void updateMouse(UserMouse mouse) {
		try {
			JSONArray mousesJson = jsonData.getJSONArray("mouses");
			for (int i = 0; i < mousesJson.length(); i++) {
				JSONObject mousesObj = mousesJson.getJSONObject(i);
				if (mouse.getCode().equals(mousesObj.getString("code"))) {
					mousesObj.put("lv", mouse.getLv());
					mousesObj.put("hp", mouse.getHp());
					mousesObj.put("exp", mouse.getExp());
					mousesObj.put("att", mouse.getAtt());
					mousesObj.put("mood", mouse.getMood());
					mousesObj.put("upmoodtime", mouse.getUpMoodTime());
					mousesObj.put("weaponstage", mouse.getWeaponStage());
					mousesObj.put("weaponlv", mouse.getWeaponLv());
					mousesObj.put("blue", mouse.getFightBlue());
					mousesJson.put(i, mousesObj);
					jsonData.put("mouses", mousesJson);
					MainServer.getInstance().saveData(jsonData.toString());
					break;
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 获得出战的老鼠Code
	 * 
	 * @return
	 */
	public String getOutPkUserMouse() {
		try {
			JSONArray mousesJson = jsonData.getJSONArray("mouses");
			for (int i = 0; i < mousesJson.length(); i++) {
				JSONObject tanObj = mousesJson.getJSONObject(i);
				if (tanObj.getInt("isoutpk") == 1) {
					return tanObj.getString("code");
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "0";
	}

	/**
	 * 设置出战
	 * 
	 *
	 */
	public void setUserMouseOutPk(UserMouse outPkmouse, UserMouse restMouse) {
		try {
			JSONArray mousesJson = jsonData.getJSONArray("mouses");
			for (int i = 0; i < mousesJson.length(); i++) {
				JSONObject mousesObj = mousesJson.getJSONObject(i);
				if (outPkmouse.getCode().equals(mousesObj.getString("code"))) {
					mousesObj.put("mood", outPkmouse.getMood());
					mousesObj.put("upmoodtime", outPkmouse.getUpMoodTime());
					mousesObj.put("isoutpk", 1);
					mousesJson.put(i, mousesObj);
					jsonData.put("mouses", mousesJson);
				} else if (restMouse.getCode().equals(
						mousesObj.getString("code"))) {
					mousesObj.put("mood", restMouse.getMood());
					mousesObj.put("upmoodtime", restMouse.getUpMoodTime());
					mousesObj.put("isoutpk", 0);
					mousesJson.put(i, mousesObj);
					jsonData.put("mouses", mousesJson);
				}
			}
			MainServer.getInstance().saveData(jsonData.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public int getPropNumByCode(String code) {
		try {
			JSONArray propsJson = jsonData.getJSONArray("props");
			for (int i = 0; i < propsJson.length(); i++) {
				JSONObject propObj = propsJson.getJSONObject(i);
				if (code.equals(propObj.getString("code"))) {
					return propObj.getInt("num");
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 
	 * 
	 * @throws JSONException
	 */
	public void updateMouseProp(String propCode, int propNum, boolean issave) {
		try {
			JSONArray propsJson = jsonData.getJSONArray("props");
			for (int i = 0; i < propsJson.length(); i++) {
				JSONObject propsObj = propsJson.getJSONObject(i);
				if (propCode.equals(propsObj.getString("code"))) {
					propsObj.put("num", propNum);
					propsJson.put(i, propsObj);
					jsonData.put("props", propsJson);
					if (issave) {
						MainServer.getInstance().saveData(jsonData.toString());
					}
					break;
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean isFirstLogin() {
		return isFirstLogin;
	}

	/**
	 * 更新用户关卡、星星
	 */
	public void updateEndGameUserData(boolean isPassed, int scene, int pass,
			int starNum) {
		try {
			if (isPassed) {
				JSONArray scenesJson = jsonData.getJSONArray("scenes");
				for (int i = 0; i < scenesJson.length(); i++) {
					JSONObject sceneObj = scenesJson.getJSONObject(i);
					if (i == scene) {
						JSONArray passJson = sceneObj.getJSONArray("pass");
						for (int j = 0; j < passJson.length(); j++) {
							JSONObject passObj = passJson.getJSONObject(j);
							if (passObj.getInt("code") == pass) {
								if (starNum > passObj.getInt("starnum")) {
									passObj.put("starnum", starNum);
								}
								passJson.put(j, passObj);
								if (pass < 5) {
									JSONObject nextPassObj = passJson
											.getJSONObject(j + 1);
									nextPassObj.put("isopen", 1);
									passJson.put(j + 1, nextPassObj);

								}
								sceneObj.put("pass", passJson);
								scenesJson.put(i, sceneObj);
								break;
							}
						}
						if (pass == 5 && i < scenesJson.length() - 1) {
							JSONObject nextSceneObj = scenesJson
									.getJSONObject(i + 1);
							nextSceneObj.put("isopen", 1);
							JSONArray passArray = nextSceneObj
									.getJSONArray("pass");
							JSONObject passObj = passArray.getJSONObject(0);
							passObj.put("isopen", 1);
							passArray.put(0, passObj);
							nextSceneObj.put("pass", passArray);
							scenesJson.put(i + 1, nextSceneObj);
						}
						jsonData.put("scenes", scenesJson);
						break;
					}
				}
				int winNum = jsonData.getInt("winnum") + 1;
				jsonData.put("winnum", winNum);
			}
			MainServer.getInstance().saveData(jsonData.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public int getWinNum() {
		int winNum = 0;
		try {
			winNum = jsonData.getInt("winnum");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return winNum;
	}
}
