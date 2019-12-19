package com.model.mainServer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;

import com.model.mainServer.config.StringTools;

/**
 * Lib9引擎通过该类提供了对联网的支持，支持http方式联网(服务器端为动态网页)，采用post方式传递数据
 * 
 * @author not attributable
 * @version 1.0
 */
public class ServerHttp implements Runnable {
	public ServerHttp() {
		this.sendType = ServerHttp.HTTP_GET;
	}

	private Thread l9Thread = null;
	private boolean bInProgress = false;
	private boolean bCanceled;
	private boolean bError;
	private byte[] responseBin = null;
	private String httpAgent = "125.210.135.149:80";

	/**
	 * 返回从服务器获取的数据
	 * 
	 * @return byte[]
	 */
	public byte[] getData() {
		return responseBin;
	}

	/**
	 * 返回是否正在http连接中
	 * 
	 * @return boolean
	 */
	public boolean isInProcess() {
		return bInProgress;
	}

	/**
	 * 返回http连接过程是否有错误发生
	 * 
	 * @return boolean
	 */
	public boolean isError() {
		return bError;
	}

	private static boolean bFirstHttpConnection = true;
	private int HttpConnectionTimeOut = 15;

	/**
	 * 设置第一次判断http接入点的超时时间，默认为45秒，第一次http连接默认将会进行判断http接入点，如果timeout为-1则不判断联网接入点直接联网
	 * 有两种接入点联网Net和Wap,默认使用Net接入点
	 * 
	 * @param timeout
	 *            int
	 */
	public void setFirstJudgeHttpWay(int timeout) {
		if (timeout == -1) {
			bFirstHttpConnection = false;
		} else {
			HttpConnectionTimeOut = timeout;
			bFirstHttpConnection = true;
		}
	}

	/**
	 * 联网使用Wap接入点的时候要求设置该属性，默认为10.0.0.172:80
	 * 
	 * @param sAgent
	 *            String
	 */
	public void setAgent(String sAgent) {
		httpAgent = sAgent;
	}

	private HttpConnection httpCon = null;
	private OutputStream httpOut = null;
	private InputStream httpIn = null;

	/**
	 * 取消http连接
	 */
	public void cancelHttp() {
		bCanceled = true;
		closeHttp();
	}

	/**
	 * 关闭http连接并释放有关资源
	 */
	public void closeHttp() {
		if (httpCon != null) {
			try {
				httpCon.close();
			} catch (Exception e) {
			}
		}
		if (httpIn != null) {
			try {
				httpIn.close();
			} catch (Exception e) {
			}
		}
		if (httpOut != null) {
			try {
				httpOut.close();
			} catch (Exception e) {
			}
		}
		httpCon = null;
		httpIn = null;
		httpOut = null;
		l9Thread = null;
		bInProgress = false;
	}

	public void closeHttp0() {
		if (httpCon != null) {
			try {
				httpCon.close();
			} catch (Exception e) {
			}
		}
		if (httpIn != null) {
			try {
				httpIn.close();
			} catch (Exception e) {
			}
		}
		if (httpOut != null) {
			try {
				httpOut.close();
			} catch (Exception e) {
			}
		}
		httpIn = null;
		httpOut = null;
		l9Thread = null;
		bInProgress = false;
	}

	private byte[] postData;
	private String sHost = "";
	private final String K_Http = "http://";
	private String sQuery = "";

	// 分为get和post
	private int sendType;

	public static final int HTTP_POST = 2;
	public static final int HTTP_GET = 4;

	public void setSendType(int sendType) {
		this.sendType = sendType;
	}

	/**
	 * 进行http连接，使用post方式来传递数据,head为向服务器传递的数据
	 * 
	 * @param url
	 *            String
	 * @param head
	 *            byte[]
	 */
	public void openHttp(String url, byte[] head) {
		// this.sendType = sendType;
		String bufUrl = url;
		System.out.println("链接地址为::: " + bufUrl);
		if (this.sendType == ServerHttp.HTTP_POST) {
			// head = StringTools.split(url, "?")[1].getBytes();
			url = StringTools.split(url, "?")[0];
			head = bufUrl.substring(url.length() + 1).getBytes();
			// System.out.println("头为 "+bufUrl.substring(url.length()));
		}
		responseBin = null;
		closeHttp0();
//		while (bInProgress) { // 当前正在处理
//			try {
//				synchronized (this) {
//					wait(80L);
//				}
//			} catch (Exception e) {
//			}
//		}
//		if (l9Thread != null) {
//			try {
//				l9Thread.join();
//			} catch (Exception e) {
//			}
//		}
		// url = url.toLowerCase();
		if (url.startsWith(K_Http)) {
			url = url.substring(K_Http.length());
		}
		int pos = url.indexOf("/");

		if (pos == -1) {
			sQuery = "";
			sHost = url;
		} else {
			sQuery = url.substring(pos);
			sHost = url.substring(0, pos);
		}

		postData = head;

		bInProgress = true;
		bCanceled = false;
		bError = false;

		l9Thread = new Thread(this);
		l9Thread.start();
		// run();
	}

	private static boolean bHttpNet = true; // 默认使用Net的方式联网

	/**
	 * 返回true表示联网使用net接入点，否则使用wap接入点
	 * 
	 * @return boolean
	 */
	public boolean getHttpWay() {
		return bHttpNet;
	}

	/**
	 * 设置Wap或者Net接入点，默认为Net接入点,如果指定了接入点那么第一次联网时将不会再进行接入点判断了
	 * 
	 * @param bNetOrWap
	 *            boolean
	 */
	public void setHttpWay(boolean bNetOrWap) {
		bHttpNet = bNetOrWap;
		setFirstJudgeHttpWay(-1);
	}

	private String getHttpUrl() {
		if (bHttpNet) {
			return K_Http + sHost + sQuery;
		}
		return K_Http + sHost + sQuery;
	}

	public void run() {
		responseBin = null;
		bError = false;
		bInProgress = true;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		String Content_Type = "";
		// System.out.println("发送的链接为 "+getHttpUrl());
		if (bCanceled) {
			closeHttp();
			return;
		}
		try {
			// if (bFirstHttpConnection) { // 第一次需要判断网络连接,确定用户是使用Wap还是Net接入点
			// long Net_Time = 0;
			// long Net_BeginTime = System.currentTimeMillis();
			// new Thread() {
			// public void run() {
			// try {
			// // System.out.println("发送协议。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。");
			// httpCon = (HttpConnection) Connector.open(
			// getHttpUrl(), Connector.READ_WRITE, true);
			// // System.out.println("httpCon = "+httpCon);
			// } catch (Exception ex) {
			// // System.out.println("wwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwww");
			// ex.printStackTrace();
			// httpCon = null;
			// }
			// }
			// }.start();
			// boolean bRun = true;
			// // System.out.println("httpCon = "+httpCon);
			// while (bRun) {
			//
			// Net_Time += (System.currentTimeMillis() - Net_BeginTime);
			// Net_BeginTime = System.currentTimeMillis();
			// // System.out.println("花费时间为 "+Net_Time);
			// try {
			// Thread.sleep(100);
			// Thread.yield();
			// } catch (Exception e) {
			// }
			// if (httpCon != null) {
			// bRun = false;
			// } else if (Net_Time > HttpConnectionTimeOut * 1000) { //
			// 联网时间大于超时时间就切换接入点
			// bHttpNet = !bHttpNet;
			// bRun = false;
			// }
			// }
			//
			// bFirstHttpConnection = false;
			// }
			// System.out.println("发送的信息 = " + getHttpUrl());
			System.out.println("aaaa ---- 1");
			httpCon = (HttpConnection) Connector.open(getHttpUrl().trim(),
					Connector.READ_WRITE, true);
			System.out.println("aaaa ---- 2");
			if (bCanceled) {
				closeHttp();
			}
			System.out.println("aaaa ---- 3");
			// httpCon.setRequestProperty("X-Online-Host",
			// L9Str.splitStr(sHost, "/")[0]);
			// Set the request method and headers
			if (sendType == ServerHttp.HTTP_POST) {
				System.out.println("aaaa ---- 4");
				httpCon.setRequestMethod(HttpConnection.POST);
				Content_Type = "application/x-www-form-urlencoded";
			} else if (sendType == ServerHttp.HTTP_GET) {
				System.out.println("aaaa ---- 5");
				httpCon.setRequestMethod(HttpConnection.GET);
			}
			// 不要设置User-Agent这个字段，否则有的手机会出现bad request(invalid header name)错误
			// c.setRequestProperty("User-Agent",
			// "Profile/MIDP-2.0 Configuration/CLDC-1.0");
			System.out.println("aaaa ---- 6");
			httpCon.setRequestProperty("Content-Type", Content_Type);
			// c.setRequestProperty("Connection", "Keep-Alive");
			// httpCon.setRequestProperty("Accept", "*/*");
			// c.setRequestProperty("Content-Length",""+my_head_info.length);
			if (postData != null) {
				System.out.println("aaaa ---- 7");
				httpCon.setRequestProperty("Content-Length", ""
						+ postData.length);
				httpOut = httpCon.openOutputStream();
				httpOut.write(postData);
				// flush函数可能造成HTTP_INTERNAL_ERROR
				// out.flush();
				httpOut.close();
			}
			System.out.println("aaaa ---- 8");
			int status = httpCon.getResponseCode();
			System.out.println("aaaa ---- 9");
			if (status != HttpConnection.HTTP_OK) { // 注册或者找回信息判断连接网络 连接超时
				closeHttp();
				bError = true;
				throw new IOException("status = " + status);
			} else {
				// 处理移动资费页面
//				String s = httpCon.getHeaderField("Content-Type");
//				// System.out.println("s = "+s);
//				if (s != null) {
//					s = s.toLowerCase();
//					if (!s.startsWith(Content_Type)) { // 如果返回的结果不是设置的类型则重新连接
//						openHttp(getHttpUrl().trim(), postData);
//						return;
//					}
//				}
				System.out.println("httpCon = " + httpCon);
				httpIn = httpCon.openInputStream();
				int ch = 0;
				while ((ch = httpIn.read()) != -1) {
					baos.write(ch);
					// strBuf.append((char)ch);
				}
				responseBin = baos.toByteArray();
				// responseBin = "".getBytes();
				// responseBin = strBuf.toString().getBytes();
				// System.out.println("lenth = "+responseBin.length);
				// String st = new String(responseBin, "UTF-8");
				// System.out.println("接收到的信息为 = " + st);
			}
		} catch (SecurityException se) {
			bError = true;
			// se.printStackTrace();
			bError = true;
			responseBin = "".getBytes();
			System.out.println("ddddddddddd");
			// MainServer.getInstance().toError("网络连接异常，请稍后再试>>"+se.getMessage()+">>>错误地址为："+getHttpUrl());
		} catch (IOException e) {
			bError = true;
			bError = true;
			responseBin = "".getBytes();
			System.out.println("bbbbbb");
			// e.printStackTrace();
			// System.out.println("忘了。。。。。。。。。。。。"+e.getMessage());
			// MainServer.getInstance().toError("输入输出流异常，请稍后再试>>"+e.getMessage()+">>>错误地址为："+getHttpUrl());
		} finally {
			try {
				baos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			baos = null;
			cancelHttp();
			l9Thread = null;
			bInProgress = false;
		}
	}

	public void setResponseBin(byte[] data) {
		this.responseBin = data;
		bInProgress = false;
		cancelHttp();
	}
}