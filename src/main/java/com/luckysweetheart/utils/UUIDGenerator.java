package com.luckysweetheart.utils;

/**
 * UUID生成器，去掉了UUID中多余的中划线
 * @author luopeng
 *         Created on 2014/4/17.
 */
public class UUIDGenerator {

	public static String gen(){
		return java.util.UUID.randomUUID().toString().toUpperCase().replaceAll("-", "");
	}


}
