package com.lvetianzhiyi.route.utils;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

public class GetIP
{
	private static String IP = "";
	
	public static String GetLocalIP( Activity in )
	{
		IP = getWifiIP( in );
		return IP;
	}

	/**
	 * 
	 * getWifiIP(得到当前wifi连接的ip地址)
	 * 
	 * @return String
	 * @exception
	 * @since 1.0.0
	 */
	public static String getWifiIP( Activity pActivity )
	{
		WifiManager wifiManager = (WifiManager) pActivity
				.getSystemService( Context.WIFI_SERVICE );

		WifiInfo wifiInfo = wifiManager.getConnectionInfo();

		// 得到wifi ip地址
		int ipAddress = wifiInfo.getIpAddress();
		String ip = intToIp( ipAddress );

		return ip;
	}

	/**
	 * 
	 * intToIp(int转成正确的ip格式)
	 * 
	 * @param ipAddress
	 * @return String
	 * @exception
	 * @since 1.0.0
	 */
	private static String intToIp( int ipAddress )
	{
		// return ((ipAddress ) & 0xFF) + "."+ ((ipAddress >> 8) & 0xFF) + "." +
		// ((ipAddress >> 16) & 0xFF) + "." + (ipAddress>> 24 & 0xFF);
		return ( ( ipAddress ) & 0xFF ) + "." + ( ( ipAddress >> 8 ) & 0xFF )
				+ "." + ( ( ipAddress >> 16 ) & 0xFF ) + "." + 1;
	}
};