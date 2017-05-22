package com.luckysweetheart.utils;

import org.springframework.util.DigestUtils;

import java.io.UnsupportedEncodingException;
import java.util.List;


public class Md5Util {

	public static enum MD5_LENGTH{
		MD5_16,MD5_32
	}
	
	/**
	 * 获取getMd5
	 * @param parameters 将参数里的值拼接起来进行md5加密
	 * @param md5_length 设置获取16的AppKey与32们的AppKey
	 * @return
	 */
	public static String getMd5(List<String> parameters,MD5_LENGTH md5_length){
		if (parameters.size()<=0) {
			return null;
		}
		String re = null;
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < parameters.size(); i++) {
			sb.append(parameters.get(i));
		}
		String par = sb.toString();
		try {
			if (MD5_LENGTH.MD5_16.equals(md5_length)) {
				re = DigestUtils.md5DigestAsHex(par.getBytes("UTF-8")).substring(8, 24);
			}else if (MD5_LENGTH.MD5_32.equals(md5_length)) {
				re = DigestUtils.md5DigestAsHex(par.getBytes("UTF-8"));
			}
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return re;
	}
}
