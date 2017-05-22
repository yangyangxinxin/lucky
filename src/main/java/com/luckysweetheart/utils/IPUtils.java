package com.luckysweetheart.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * IP 工具类
 * @author luopeng
 *         Created on 2014/6/18.
 */
public class IPUtils {

	public static String getLocalHostName() {
		String hostName;
		try {
			InetAddress addr = InetAddress.getLocalHost();
			hostName = addr.getHostName();
		} catch (Exception ex) {
			hostName = "";
		}
		return hostName;
	}

	public static String[] getAllLocalHostIP() {
		String[] ret = null;
		try {
			String hostName = getLocalHostName();
			if (hostName.length() > 0) {
				InetAddress[] addrs = InetAddress.getAllByName(hostName);
				if (addrs.length > 0) {
					ret = new String[addrs.length];
					for (int i = 0; i < addrs.length; i++) {
						ret[i] = addrs[i].getHostAddress();
					}
				}
			}

		} catch (Exception ex) {
			ret = null;
		}
		return ret;
	}

	public static String getHostInfo(){
		StringBuilder sb = new StringBuilder();
		try {
			String hostName = getLocalHostName();
			sb.append(hostName).append("/");
			InetAddress ip = null;
			if (hostName.length() > 0) {
				Enumeration<NetworkInterface> addrs = NetworkInterface.getNetworkInterfaces();
				while (addrs.hasMoreElements()) {
					NetworkInterface networkInterface = addrs.nextElement();
					Enumeration<InetAddress> inetAddressEnumeration = networkInterface.getInetAddresses();
					while(inetAddressEnumeration.hasMoreElements()){
						ip=(InetAddress) inetAddressEnumeration.nextElement();
						if(!ip.isLoopbackAddress() && ip.getHostAddress().indexOf(":")==-1){
							sb.append(ip.getHostAddress());
							sb.append("/");
						}
					}
				}
			}
			sb.deleteCharAt(sb.length()-1);
		} catch (Exception ex) {
			return "";
		}
		return sb.toString();
	}
}
