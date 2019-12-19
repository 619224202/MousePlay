package com.model.mainServer;

public class ConstantSW {
	/**
	 * 机顶盒ID账号
	 */
	public static String stbId = "";
	/**
	 * bussinessId
	 */
	public static String businessId = "";
	/**
	 * userId:接口返回的userId
	 */
	public static String userId = "";
	/**
	 * userToken
	 */
	public static String userToken = "";
	/**
	 * gameCode
	 */
	public static String gameCode = "";
	/**
	 * 接口地址
	 */
	public static String serverIp = "http://192.168.1.102";

	/**
	 * 项目地址
	 */
	public static String serverProject = "IptvWeb";
	/**
	 * 退出时返回的url
	 */
	public static String returnUrl = "";

	public static String browser = "";

	public static String productCode = "0001";


    
	/**
	 * 用户是否已经包月
	 */
	public static boolean isUserOrderMonth;

	public static String getURL() {
		StringBuffer buf = new StringBuffer();
		buf.append(serverIp).append("/" + serverProject).append("/gameData!");
		return buf.toString();
	}

}
