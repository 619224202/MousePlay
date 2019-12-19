package com.model.mainServer;

import java.util.Hashtable;
import java.util.Vector;

import com.model.mainServer.config.StringTools;

public class SwServer implements MainServerInterface {
	private ServerHttp serverHttp;
	private String[] userRank = new String[3];
	private long time;

	/**
	 * 购买道具使用包月接口
	 */
	public boolean buyProp(String propCode, String price, String propName,
			String passWord) {
		serverHttp = null;
		StringBuffer strBuf = new StringBuffer();
		strBuf.append(ConstantSW.serverIp).append(
				"/" + ConstantSW.serverProject).append(
				"/order!orderProp.action?").append("appGameCode=").append(
				ConstantSW.gameCode).append("&propPriceCode=").append(propCode)
				.append("&userID=").append(ConstantSW.userId).append(
						"&password=").append(passWord).append("&propPrice=")
				.append(price).append("&desc=").append(
						StringTools.encode(propName));
		if (ConstantSW.productCode != null
				&& !"".equals(ConstantSW.productCode)) {
			strBuf.append("&productCode=").append(ConstantSW.productCode);
		}
		time = System.currentTimeMillis();
		sendUrl(strBuf.toString());
		return false;
	}

	/**
	 * 购买道具使用包月接口
	 */
	public boolean orderMonth(String passWord) {
		StringBuffer strBuf = new StringBuffer();
		strBuf.append(ConstantSW.serverIp).append(
				"/" + ConstantSW.serverProject).append(
				"/order!orderMonth.action?").append("appCode=").append(
				ConstantSW.gameCode).append("&productCode=").append(
				ConstantSW.productCode).append("&businessID=").append(
				ConstantSW.businessId).append("&stbID=").append(
				ConstantSW.stbId).append("&userToken=").append(
				ConstantSW.userToken);
		sendUrl(strBuf.toString());
		return false;
	}

	public String getData(int index) {
		StringBuffer strBuf = new StringBuffer();
		strBuf.append(ConstantSW.getURL()).append("getData.action?gameCode=")
				.append(ConstantSW.gameCode).append("&userID=").append(
						ConstantSW.userId);
		sendUrl(strBuf.toString());
		return getGameData();
	}

	public String getData() {
		return getData(0);
	}

	public Vector getRank(int type) {
		// TODO Auto-generated method stub
		StringBuffer strBuf = new StringBuffer();
		strBuf.append(ConstantSW.getURL()).append("getRank.action?gameCode=")
				.append(ConstantSW.gameCode).append("&userID=").append(
						ConstantSW.userId).append("&rankColumn=").append(type)
				.append("&orderBy=desc&currPage=0&pageSize=10");
		sendUrl(strBuf.toString());
		return getGameRank();
	}

	public int getScore(int type) {
		// TODO Auto-generated method stub
		StringBuffer url = new StringBuffer();
		url.append(ConstantSW.getURL()).append(
				"getUserRankValue.action?gameCode=")
				.append(ConstantSW.gameCode).append("&userID=").append(
						ConstantSW.userId).append("&rankColumn=").append(type);
		sendUrl(url.toString());
		return getGameScore();
	}

	public byte[] getServerData() {
		// TODO Auto-generated method stub
		return serverHttp.getData();
	}

	public String[] getUserRank(int type) {
		// TODO Auto-generated method stub
		return userRank;
	}

	public int getVirtualCoin() {
		StringBuffer strBuf = new StringBuffer();
		strBuf.append(ConstantSW.serverIp).append(
				"/" + ConstantSW.serverProject).append(
				"/order!userMoney.action?").append("gameCode=").append(
				ConstantSW.gameCode).append("&userID=").append(
				ConstantSW.userId);
		sendUrl(strBuf.toString());
		return getGameVirtualCoin();
	}

	public boolean login() {
		// TODO Auto-generated method stub
		StringBuffer buf = new StringBuffer();
		buf.append(ConstantSW.getURL()).append("login.action?gameCode=")
				.append(ConstantSW.gameCode).append("&stbID=").append(
						ConstantSW.stbId).append("&businessID=").append(
						ConstantSW.businessId).append("&userToken=").append(
						ConstantSW.userToken);
		sendUrl(buf.toString());
		return getLoginState();
	}

	public void saveData(int index, String data) {
		// TODO Auto-generated method stub
		StringBuffer strBuf = new StringBuffer();
		strBuf.append(ConstantSW.getURL())
				.append("updateData.action?gameCode=").append(
						ConstantSW.gameCode).append("&userID=").append(
						ConstantSW.userId).append("&data=").append(data);
		time = System.currentTimeMillis();
		sendUrl(strBuf.toString());
//		new Thread() {
//			public void run() {
		getHttpStateTimeOut();
//			}
//		}.start();
	}

	public void saveData(String data) {
		// TODO Auto-generated method stub
		saveData(0, data);
	}

	public void updateScore(int type, int score) {
		// TODO Auto-generated method stub
		StringBuffer strBuf = new StringBuffer();
		strBuf.append(ConstantSW.getURL())
				.append("updateRank.action?gameCode=").append(
						ConstantSW.gameCode).append("&userID=").append(
						ConstantSW.userId).append("&rankColumn=").append(type)
				.append("&value=").append(score);
		time = System.currentTimeMillis();
		sendUrl(strBuf.toString());
//		new Thread() {
//			public void run() {
			getHttpStateTimeOut();
//			}
//		}.start();
	}

	public void updateScore(int score) {
		// TODO Auto-generated method stub
		updateScore(0, score);
	}

	public boolean saveURL(String url) {
		StringBuffer strBuf = new StringBuffer();
		strBuf.append(ConstantSW.serverIp).append(
				"/" + ConstantSW.serverProject).append(
				"/jartoWeb!recordUrl.action?").append("gameCode=").append(
				ConstantSW.gameCode).append("&userID=").append(
				ConstantSW.userId).append("&url=").append(url);
		sendUrl(strBuf.toString());
		return getGameNormal();
	}

	public boolean getLoginState() {
		getHttpState();
		return ParseSW.parseLogin(serverHttp.getData());
	}

	public void getHttpState() {
		while (serverHttp.getData() == null) {
			try {
				Thread.sleep(15);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void getHttpStateTimeOut() {
		while (serverHttp.getData() == null) {
			if (isBuyTimeOut()) {
				break;
			}
			try {
				Thread.sleep(30);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void sendUrl(String url) {
		if (serverHttp == null) {
			serverHttp = new ServerHttp();
		}
		serverHttp.openHttp(url, null);
	}

	public String getGameData() {
		getHttpState();
		return ParseSW.parseData(serverHttp.getData());
	}

	private Vector getGameRank() {
		getHttpState();
		Vector v = ParseSW.parseRank(serverHttp.getData());
		userRank = (String[]) v.elementAt(0);
		Vector rankVector = new Vector();
		for (int i = 1; i < v.size(); i++) {
			rankVector.addElement(v.elementAt(i));
		}
		return rankVector;
	}

	private int getGameScore() {
		getHttpState();
		return ParseSW.parseScore(serverHttp.getData());
	}

	private boolean getGameNormal() {
		getHttpState();
		return ParseSW.parserNormal(serverHttp.getData());
	}

	private int getGameVirtualCoin() {
		getHttpState();
		return ParseSW.parseUserMoney(serverHttp.getData());
	}

	public boolean saveGamesStbLogs(String loginSeq, int state, long memory,
			String otherPrams) {
		StringBuffer strBuf = new StringBuffer();
		strBuf.append(ConstantSW.getURL()).append(
				"saveGamesStbLogs.action?gameCode=")
				.append(ConstantSW.gameCode).append("&userID=").append(
						ConstantSW.userId).append("&loginSeq=")
				.append(loginSeq).append("&state=").append(state).append(
						"&memory=").append(memory).append("&otherPrams=")
				.append(otherPrams);
		sendUrl(strBuf.toString());
		getHttpState();
		return true;
	}

	public Hashtable getGamePropPrice() {
		StringBuffer strBuf = new StringBuffer();
		strBuf.append(ConstantSW.serverIp).append(
				"/" + ConstantSW.serverProject).append(
				"/order!getGamePropPrice.action?appGameCode=").append(
				ConstantSW.gameCode).append("&userID=").append(
				ConstantSW.userId);
		sendUrl(strBuf.toString());
		getHttpState();
		return ParseSW.parsePropPrice(serverHttp.getData());
	}
	
	public void addViricalCoin(int coinNum){
		StringBuffer strBuf = new StringBuffer();
		strBuf.append(ConstantSW.serverIp).append(
				"/" + ConstantSW.serverProject).append(
				"gameData!virvary.action?userID=").append(
				ConstantSW.userId).append("&num=").append(
				coinNum);
		sendUrl(strBuf.toString());
		getHttpState();
	}

	public void playMedia(String soundUrl, int count, int isreplce) {
		// TODO Auto-generated method stub
		StringBuffer strBuf = new StringBuffer();
		strBuf.append(ConstantSW.serverIp).append(
				"/" + ConstantSW.serverProject).append(
				"/playMedia!palySound.action?").append("userID=").append(
				ConstantSW.userId).append("&soundUrl=").append(soundUrl)
				.append("&count=").append(count).append("&isreplce=").append(
						isreplce);
		sendUrl(strBuf.toString());
	}

	public String getSysData() {
		// TODO Auto-generated method stub
		StringBuffer buf = new StringBuffer();
		buf.append(ConstantSW.getURL()).append("getSysData.action");
		sendUrl(buf.toString());
		getHttpState();
		return ParseSW.parseSysData(serverHttp.getData());
	}

	public boolean isBuyTimeOut() {
		boolean timeOut = time != 0
				&& System.currentTimeMillis() - time >= 5 * 1000;
		if (timeOut) {
			this.close();
		}
		return timeOut;
	}

	public void close() {
		time = 0;
		serverHttp.closeHttp0();
		serverHttp = null;
	}
}
