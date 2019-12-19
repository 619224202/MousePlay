package com.game.mouse.modle;

import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.game.mouse.context.GameConfigInfo;
import com.game.mouse.modle.service.UserDataManageService;

public class UserMouse extends Mouse {
	private int lv;

	private int maxLv = 10;

	private int att;

	private int mood;

	private int exp;

	private boolean isbuy;

	/**
	 * 是否出战
	 */
	private boolean isOutPk;

	/**
	 * 上次保存心情时间
	 */
	private long upMoodTime;

	/**
	 * 武器进化阶段
	 */
	private int weaponStage = 0;

	/**
	 * 武器等级
	 */
	private int weaponLv;

	/**
	 * 初始化时角色的攻击力
	 */
	private int initAtt;

	private int[] lvaddhp = { 30, 40, 36 };

	private int[] initAttMsg = { 30, 30, 35 };

	public UserMouse(String code) {
		super(code, "2".equals(code) ? 1 : 2);
		initAttribute();
	}

	public UserMouse(String code, int maxhp) {
		super(code, maxhp);
		initAttribute();
	}

	public void initAttribute() {
		JSONObject mouseObj = UserDataManageService.getInsatnce()
				.getUserMousesByCode(this.code);
		try {
			if (mouseObj != null) {
				isbuy = true;
				lv = mouseObj.has("lv") ? mouseObj.getInt("lv") : 1;
				exp = mouseObj.has("exp") ? mouseObj.getInt("exp") : 0;
				mood = mouseObj.has("mood") ? mouseObj.getInt("mood") : 100;
				att = mouseObj.has("att") ? mouseObj.getInt("att")
						: initAttMsg[Integer.parseInt(this.code)];
				weaponStage = mouseObj.has("weaponstage") ? mouseObj
						.getInt("weaponstage") : 0;
				weaponLv = mouseObj.has("weaponlv") ? mouseObj
						.getInt("weaponlv") : 0;
				upMoodTime = mouseObj.has("upmoodtime") ? mouseObj
						.getLong("upmoodtime") : System.currentTimeMillis();
				isOutPk = mouseObj.has("isoutpk")
						&& mouseObj.getInt("isoutpk") == 1;
				fightHp = fightMaxHp = 100
						+ lvaddhp[Integer.parseInt(this.code)] * (lv - 1);
				this.fightBlue = mouseObj.has("blue") ? mouseObj.getInt("blue")
						: this.fightMaxBlue;
			} else {
				this.lv = 1;
				this.att = initAttMsg[Integer.parseInt(this.code)];
				this.mood = 100;
				this.fightHp = this.fightMaxHp = 100;
				this.fightBlue = this.fightMaxBlue = 100;
			}
			initAtt = this.getNowAttByMood();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 根据时间获得当前的心情值
	 * 
	 * @return
	 */
	public int getNowMoodByTime() {
		if (isbuy) {
			if (isOutPk) {
				long nowLoseMood = ((System.currentTimeMillis() - upMoodTime) / 1000 / 60) * 5;
				return (mood - nowLoseMood) > 30 ? (int) (mood - nowLoseMood)
						: 30;
			} else {
				long nowLoseMood = ((System.currentTimeMillis() - upMoodTime) / 1000 / 60) * 2;
				return (mood + nowLoseMood) < 100 ? (int) (mood + nowLoseMood > 30 ? mood
						+ nowLoseMood
						: 30)
						: 100;
			}
		} else {
			return 100;
		}
	}

	/**
	 * 根据心情获得攻击
	 * 
	 * @return
	 */
	public int getNowAttByMood() {
		int nowMood = getNowMoodByTime();
		if (nowMood >= 80) {
			return this.att / 10 + this.att;
		} else if (nowMood < 80 & nowMood >= 50) {
			return this.att;
		} else {
			return this.att * 7 / 10;
		}
	}

	public int isMouseState() {
		int nowMood = getNowMoodByTime();
		if (nowMood >= 80) {
			return 2;
		} else if (nowMood < 80 & nowMood >= 50) {
			return 1;
		} else {
			return 0;
		}
	}

	public void addExp(int exp) {
		if (this.lv < this.maxLv) {
			if (this.exp + exp >= 100) {
				this.exp = this.exp + exp - 100;
				this.lv = this.lv + 1 > this.maxLv ? this.maxLv : this.lv + 1;
			} else {
				this.exp = this.exp + exp;
			}
		}
	}

	/**
	 * 持久增加攻击力
	 * 
	 * @param att
	 */
	public void addAtt(int addatt) {
		if (this.lv < this.maxLv) {
			this.att = this.att + addatt;
		}
	}

	/**
	 * 恢复当前心情百分比
	 * 
	 * @param addmood
	 */
	public void addNowMood(int addmood) {
		if (addmood > 0) {
			addmood = addmood * mood / 100;
			int nowMoodTmp = this.getNowMoodByTime();
			mood = nowMoodTmp + addmood > 100 ? 100 : nowMoodTmp + addmood;
			upMoodTime = System.currentTimeMillis();
		}
	}

	public boolean isOutPk() {
		return isOutPk;
	}

	public void setOutPk(boolean isOutPk) {
		this.isOutPk = isOutPk;
	}

	public long getUpMoodTime() {
		return upMoodTime;
	}

	public void setUpMoodTime(long upMoodTime) {
		this.upMoodTime = upMoodTime;
	}

	public int getMaxLv() {
		return maxLv;
	}

	public void setMaxLv(int maxLv) {
		this.maxLv = maxLv;
	}

	public boolean isIsbuy() {
		return isbuy;
	}

	public void setIsbuy(boolean isbuy) {
		this.isbuy = isbuy;
	}

	public int getLv() {
		return lv;
	}

	public void setLv(int lv) {
		this.lv = lv;
	}

	public void setMood(int mood) {
		this.mood = mood;
	}

	/**
	 * 得到最大攻击力，好心情的时候攻击力加成10%
	 * 
	 * @param lvAddAtt
	 * @return
	 */
	public int getMaxAtt(int lvAddAtt) {
		int mouseatt = (this.maxLv - 1) * lvAddAtt
				+ initAttMsg[Integer.parseInt(this.code)];
		return mouseatt / 10 + mouseatt;
	}

	public int getAtt() {
		return att;
	}

	public int getMood() {
		return mood;
	}

	public void setAtt(int att) {
		this.att = att;
	}

	public int getExp() {
		return exp;
	}

	public void setExp(int exp) {
		this.exp = exp;
	}

	public int getWeaponStage() {
		return weaponStage;
	}

	public void setWeaponStage(int weaponStage) {
		this.weaponStage = weaponStage;
	}

	public int getWeaponLv() {
		return weaponLv;
	}

	public void setWeaponLv(int weaponLv) {
		this.weaponLv = weaponLv;
	}

	/**
	 * 得到保护罩使用时间
	 * 
	 * @return
	 */
	public int getUseProtectProTime() {
		return this.lv / 3 + ("0".equals(code) ? 5 : "1".equals(code) ? 6 : 7);
	}

	public int getInitAtt() {
		return initAtt;
	}

	/**
	 * 得到对战时的攻击力
	 * 
	 * @return
	 */
	public int getFightAllAtt() {
		int weaponAtt = (GameConfigInfo.weapon[Integer.parseInt(code)][2] + GameConfigInfo.weapon[Integer
				.parseInt(code)][1] * 20)
				* (GameConfigInfo.weapon[Integer.parseInt(code)][0] + 1);
		return this.initAtt + weaponAtt;
	}
}
