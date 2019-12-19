package com.game.mouse.gameinfo;

import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.game.mouse.context.Config;
import com.model.tool.PubToolKit;

public class Props {

	/**
	 * 内部道具编码与扣费编码价格对应
	 */
	private String[][] priceProp;
	/**
	 * 与人民币单位(分)比率
	 */
	private int rate = 1;

	/**
	 * 虚拟币类型
	 */
	private int type = 0;

	/**
	 * 单位名称
	 */
	private String unitName = "分";

	private static Props intance;

	public synchronized static Props getIntance() {
		if (intance == null)
			intance = new Props();
		return intance;
	}

	private Props() {
		this.getAllPriceProp();
	}

	/**
	 * 读取配置文件
	 */
	public void getAllPriceProp() {
		String propstr = "/data/prop/props-" + Config.region.toLowerCase();
		if (Config.poperator != null && !"".equals(Config.poperator)) {
			propstr += ("-" + Config.poperator.toLowerCase());
		}
		propstr = propstr + ".txt";
		String jsonStr = PubToolKit.getTxtData(propstr, "GB2312");
		try {
			JSONArray jsonArr = new JSONArray(jsonStr);
			priceProp = new String[jsonArr.length()][6];
			JSONObject obj;
			for (int i = 0; i < jsonArr.length(); i++) {
				obj = jsonArr.getJSONObject(i);
				priceProp[i][0] = obj.getString("code");
				priceProp[i][1] = obj.getString("priceProp");
				priceProp[i][2] = obj.getString("price");
				priceProp[i][3] = obj.getString("buyNum");
				priceProp[i][4] = obj.getString("des");
				priceProp[i][5] = obj.getString("desc");
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String[] getPricePropByCode(String code) {
		String[] prop = { "", "", "", "", "", "" };
		for (int i = 0; i < priceProp.length; i++) {
			if (priceProp[i][0].toLowerCase().equals(code.toLowerCase())) {
				prop[0] = priceProp[i][0];
				prop[1] = priceProp[i][1];
				prop[2] = priceProp[i][2];
				prop[3] = priceProp[i][3];
				String des = priceProp[i][4];
				if (des.indexOf("{price}") > 0)
					des = des.substring(0, des.indexOf("{price}"))
							+ Integer.parseInt(priceProp[i][2]) / rate
							+ des.substring(des.indexOf("{price}") + 7);

				if (des.indexOf("{unit}") > 0)
					des = des.substring(0, des.indexOf("{unit}")) + unitName
							+ des.substring(des.indexOf("{unit}") + 6);
				if (des.indexOf("{num}") > 0)
					des = des.substring(0, des.indexOf("{num}"))
							+ priceProp[i][3]
							+ des.substring(des.indexOf("{num}") + 5);
				prop[4] = des;
				prop[5] = priceProp[i][5];
				return prop;
			}
		}
		return prop;
	}

	public int getRate() {
		return rate;
	}

	public void setRate(int rate) {
		this.rate = rate;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public String[][] getPriceProp() {
		return priceProp;
	}

	public void setPriceProp(String[][] priceProp) {
		this.priceProp = priceProp;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
}
