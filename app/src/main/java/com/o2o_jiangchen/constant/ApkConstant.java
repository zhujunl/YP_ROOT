package com.o2o_jiangchen.constant;

public class ApkConstant {

	public static final boolean DEBUG = false;
	public static String SERVER_API_URL_MID = "hmsh.hm2019.com";
//	public static String SERVER_API_URL_MID = "hm2.hm2019.com";
//	public static String SERVER_API_URL_MID = "hm.hm2019.com";
	// public static String SERVER_API_URL_MID = "dzo2o.o2o.fanwe.net";
	// public static String SERVER_API_URL_MID = "fw.yewugou.com";
	// www.hm2019.com
	public static final String SERVER_API_URL_PRE = "http://";
	public static final String SERVER_API_URL_END = "/mapi/index.php";
	public static final String URL_PART_WAP = "/wap/index.php";
	public static final String KEY_AES = "FANWE5LMUQC436IM";

	private static final String SERVER_API_URL = SERVER_API_URL_PRE
			+ SERVER_API_URL_MID + SERVER_API_URL_END;

	public static String getWapUrl() {
		return SERVER_API_URL_PRE + SERVER_API_URL_MID + URL_PART_WAP;
	}

	public static String getServerApiUrl() {
		if (DEBUG) {
			return SERVER_API_URL_PRE + SERVER_API_URL_MID + SERVER_API_URL_END;
		} else {
			return SERVER_API_URL;
		}
	}

}

