package com.model.mainServer;

import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

public class ParseSW {
	/**
	 * 登陆
	 * 
	 * @param data
	 */
	public static boolean parseLogin(byte[] data) {
		try {
			String s = new String(data, "utf-8");
			JSONObject json = new JSONObject(s);
			if (json.getInt("reslut") == 0) {
				ConstantSW.userId = json.getString("userID");
				return true;
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * score
	 * 
	 * @param data
	 * @return
	 */
	public static int parseScore(byte[] data) {
		int score = 0;
		try {
			String s = new String(data, "utf-8");
			JSONObject json = new JSONObject(s);
			if (json.getInt("reslut") == 0) {
				score = json.getInt("value");
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return score;
	}

	/**
	 * data
	 */
	public static String parseData(byte[] data) {
		String reData = "";
		try {
			if (data != null && data.length > 0) {
				String dataStr = new String(data, "utf-8");
				JSONObject jobj = new JSONObject(dataStr);
				if (jobj.getInt("reslut") == 0) {
					reData = jobj.getString("data");
				}
			}else{
				System.out.println("kong");
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return reData;
	}

	public static Vector parseRank(byte[] data) {
		Vector v = new Vector();
		try {
			String[] rankInfo = null;
			String str = new String(data, "UTF-8");
			JSONObject jobj = new JSONObject(str);
			if (jobj.getInt("reslut") == 0) {
				JSONObject json = new JSONObject(jobj.getString("myRank"));

				rankInfo = new String[3];
				rankInfo[1] = (json.getString("businessID"));
				rankInfo[0] = (json.getInt("rank") + "");
				rankInfo[2] = (json.getInt("value") + "");
				if (rankInfo[1].length() > 13) {
					rankInfo[1] = rankInfo[1].substring(0, 5) + "***"
							+ rankInfo[1].substring(rankInfo[1].length() - 5);
				}
				v.addElement(rankInfo);
				JSONArray jarray = new JSONArray(jobj.getString("userRank"));
				for (int i = 0; i < jarray.length(); i++) {
					json = jarray.getJSONObject(i);
					if (!json.getString("stbID").trim().equals("HZSW0000")) {
						rankInfo = new String[3];
						rankInfo[1] = (json.getString("businessID"));
						rankInfo[0] = (json.getInt("rank") + "");
						rankInfo[2] = (json.getInt("value") + "");
						if (MainConfig.regionType == MainConfig.Type_TJLT) {
							StringBuffer userIdBuf = new StringBuffer();
							userIdBuf.append(rankInfo[1].substring(0, 4));
							for (int j = 0; j < rankInfo[1].length() - 8; j++) {
								userIdBuf.append("*");
							}
							userIdBuf.append(rankInfo[1].substring(rankInfo[1]
									.length() - 4, rankInfo[1].length()));
							rankInfo[1] = userIdBuf.toString();
						} else if (rankInfo[1].length() > 13) {
							rankInfo[1] = rankInfo[1].substring(0, 5)
									+ "***"
									+ rankInfo[1].substring(rankInfo[1]
											.length() - 5);
						}
						v.addElement(rankInfo);
					}
				}
			}
			return v;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return v;
	}

	public static boolean parserNormal(byte[] data) {
		try {
			String dataStr = new String(data, "utf-8");
			JSONObject jobj = new JSONObject(dataStr);
			if (jobj.getInt("reslut") == 0) {
				return true;
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	public static int parseUserMoney(byte[] data) {
		int price = 0;
		try {
			if (data != null && data.length > 0) {
				String str = new String(data);
				JSONObject json = new JSONObject(str);
				if (json.getInt("reslut") == 0) {
					price = json.getInt("num");
				} else {
					price = 0;
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			MainServer.getInstance().httpServer.close();
			e.printStackTrace();
		}
		return price;
	}

	public static int parserBuy(byte[] data, boolean isTest) {
		String str = "";
		int result = -1;
		if (isTest) {
			return MainConfig.BUY_SUCCESS;
		}
		try {
			if (data != null && data.length > 0) {
				str = new String(data);
				System.out.println("收到的信息为 " + str);
				JSONObject json = new JSONObject(str);
				result = json.getInt("reslut");
				if (result == 0) {
					return MainConfig.BUY_SUCCESS;
				} else if (result == 9103) {
					return MainConfig.BUY_CHILDLOCK;
				} else if (result == -500) {
					return MainConfig.BUY_NOTBUY;
				} else if (result == -4) {
					return MainConfig.BUY_MONTHFAIL;
				} else {
					return MainConfig.BUY_FAILED;
				}
			} else {
				MainServer.getInstance().httpServer.close();
				return MainConfig.BUY_CONNERR;
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			MainServer.getInstance().httpServer.close();
			e.printStackTrace();
		}
		return MainConfig.BUY_CONNERR;
	}

	public static int parserMonthBuy(byte[] data, boolean isTest) {
		String str = "";
		int result = -1;
		if (isTest) {
			return MainConfig.BUY_SUCCESS;
		}
		try {
			str = new String(data, "utf-8");
			JSONObject json = new JSONObject(str);
			result = json.getInt("reslut");
			if (result == 0) {
				return MainConfig.ORDERMONTH_SUCCESS;
			} else if (result == 9103) {
				return MainConfig.BUY_CHILDLOCK;
			} else {
				return MainConfig.ORDERMONTH_FAILED;
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return MainConfig.ORDERMONTH_FAILED;
	}

	public static Hashtable parsePropPrice(byte[] data) {
		Hashtable tabPropPrice = new Hashtable();
		try {
			String[] rankInfo = null;
			String str = new String(data, "UTF-8");
			JSONObject jobj = new JSONObject(str);
			if ("0".equals(jobj.getString("reslut"))) {
				JSONObject jsonObj = new JSONObject(jobj.getString("props"));
				Enumeration keys = jsonObj.keys();
				while (keys.hasMoreElements()) {
					String key = keys.nextElement().toString();
					tabPropPrice.put(key, jsonObj.get(key));
				}
			}
			return tabPropPrice;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return tabPropPrice;
	}

	/**
	 * 登陆
	 * 
	 * @param data
	 */
	public static String parseSysData(byte[] data) {
		try {
			String s = new String(data, "utf-8");
			JSONObject json = new JSONObject(s);
			if (json.getInt("reslut") == 0) {
				return json.getString("nowdate");
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
