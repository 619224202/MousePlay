package com.model.mainServer;

import java.util.Vector;

public interface MainServerInterface {
	/**
	 * 登陆方法
	 */
	public abstract boolean login();

	/**
	 * index为data的下标：一般为存储的index下标 index为第几条 获取人物保存的信息
	 */
	public abstract String getData(int index);

	// 默认index为0
	public abstract String getData();

	/**
	 * 保存人物的信息 index为第几条
	 */
	public abstract void saveData(int index, String data);

	// 默认index为0
	public abstract void saveData(String data);

	/**
	 * 获取人物虚拟货币
	 */
	public abstract int getVirtualCoin();

	/**
	 * 人物购买道具
	 */
	public abstract boolean buyProp(String propCode, String price,
			String propName, String passWord);
	
	/**
	 * 订购包月
	 * @param passWord
	 * @return
	 */
	public boolean orderMonth(String passWord);

	/**
	 * 获取人物的分数
	 */
	public int getScore(int type);

	/**
	 * 根据type获取人物自己的排行 若无type，则取0 第一个为userId,第二个为userName,第三个为排行，第四个为分数score
	 */
	public String[] getUserRank(int type);

	/**
	 * 获取排行榜信息 第一个为排行，第二个为userId，第三个为score
	 * 
	 * @param type
	 * @return
	 */
	public Vector getRank(int type);

	/**
	 * 更新排行榜信息
	 */
	public void updateScore(int type, int score);

	public void updateScore(int score);

	public byte[] getServerData();

	public String getSysData();
	
	public boolean isBuyTimeOut();

	public void close();
}
