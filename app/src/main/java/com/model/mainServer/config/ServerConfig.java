package com.model.mainServer.config;

public class ServerConfig {
	/**
	 * 机顶盒ID
	 */
	private String stbId;
	/**
	 * businessId:用来和stbId共同生成userId
	 * 在圣万的服务器上根据stbId，bussinessId及userToken生成userId
	 */
	private String bussinessId;
	/**
	 * userToken:用户令牌
	 * 在江苏有线，华数，掌牛及湖南平台上用来生成userId的一个标示
	 */
	private String userToken;
	/**
	 * userId:用户的标示符：
	 */
	private String userId;
	/**
	 * gameId:游戏的编号,（只用于江苏有线，华数，掌牛及湖南平台上）
	 */
	private String gameCode;
	/**
	 * 充值,平台提供的唯一的游戏标示：用来对游戏进行一般操作
	 */
	private String gameId;
	/**
	 * 服务器ip和端口
	 */
	private String ip;
	private String port;
	/**
	 * 服务器地址
	 */
	private String serverIp;
	/**
	 * 资源服务器
	 */
	private String resoureIp;
	/**
	 * byte数组
	 */
	private byte[] data;
	/**
	 * buyServer..新疆专用
	 */
	private String buyServer;
	
	
	public String getStbId() {
		return stbId;
	}
	public void setStbId(String stbId) {
		this.stbId = stbId;
	}
	public String getBussinessId() {
		return bussinessId;
	}
	public void setBussinessId(String bussinessId) {
		this.bussinessId = bussinessId;
	}
	public String getUserToken() {
		return userToken;
	}
	public void setUserToken(String userToken) {
		this.userToken = userToken;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getGameCode() {
		return gameCode;
	}
	public void setGameCode(String gameCode) {
		this.gameCode = gameCode;
	}
	public String getGameId() {
		return gameId;
	}
	public void setGameId(String gameId) {
		this.gameId = gameId;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public String getServerIp() {
		return serverIp;
	}
	public void setServerIp(String serverIp) {
		this.serverIp = serverIp;
	}
	public String getResoureIp() {
		return resoureIp;
	}
	public void setResoureIp(String resoureIp) {
		this.resoureIp = resoureIp;
	}
	public byte[] getData() {
		return data;
	}
	public void setData(byte[] data) {
		this.data = data;
	}
	public String getBuyServer() {
		return buyServer;
	}
	public void setBuyServer(String buyServer) {
		this.buyServer = buyServer;
	}

}
