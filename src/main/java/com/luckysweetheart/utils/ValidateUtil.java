package com.luckysweetheart.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

/**
 * 校验工具类
 * @author yfx
 *
 */
public class ValidateUtil {
	public static Pattern email=Pattern.compile("^(\\w)+(\\.\\w+)*@[\\w\\-_]+((\\.\\w+)+)$");
	public static Pattern pwdVal=Pattern.compile("^(?!\\d{6,16}$)[\\S]{6,16}$");
	public static Pattern specialVal=Pattern.compile("^[^-`=\\\\\\[\\];',.\\/~!@#$￥！……%^&*()_+|{}:\"<>?]+$");
	public static Pattern mobileVal=Pattern.compile("^1[3|4|5|7|8][0-9]\\d{8}$");
	public static Pattern telVal=Pattern.compile("^(([0\\+]\\d{2,3}-)?(0\\d{2,3})-)?(\\d{7,8})(-(\\d{3,}))?$");
	public static Pattern businessLicenseVal=Pattern.compile("^\\d{1,}$");
	public static Pattern organizationCodeVal=Pattern.compile("^[a-zA-Z0-9]{8}-[a-zA-Z0-9]$");
	public static Pattern url=Pattern.compile("^(http|www|ftp|)?(://)?"
			+ "(\\w+(-\\w+)*)(\\.(\\w+(-\\w+)*))*((:\\d+)?)"
			+ "(/(\\w+(-\\w+)*))*(\\.?(\\w)*)(\\?)?(((\\w*%)*(\\w*\\?)*(\\w*:)*(\\w*\\+)*(\\w*\\.)"
			+ "*(\\w*&)*(\\w*-)*(\\w*=)*(\\w*%)*(\\w*\\?)*(\\w*:)*(\\w*\\+)*(\\w*\\.)*(\\w*&)*(\\w*-)*(\\w*=)*)*(\\w*)*)$");
	public static Pattern registerFundVal=Pattern.compile("^[1-9]\\d*$");
	/**
	 * 不能为空
	 * @param c
	 * @return
	 */
	public static boolean required(String s){
		return StringUtils.isNotBlank(s);
	}
	
	/**
	 * 是否为邮箱
	 * @param s
	 * @return
	 */
	public static boolean email(String s){
		return required(s)&&email.matcher(s).matches();
	}
	
	/**
	 * 最大长度
	 * @param s
	 * @param maxl
	 * @return
	 */
	public static boolean maxlength(String s,int maxl){
		return required(s)&&s.length()<maxl+1;
	}
	
	
	/**
	 * 最小长度
	 * @param s
	 * @param minl
	 * @return
	 */
	public static boolean minlength(String s,int minl){
		return required(s)&&s.length()>minl-1;
	}
	
	/**
	 * 是否符号password验证6-16位
	 * @param s
	 * @return
	 */
	public static boolean pwdVal(String s){
		return required(s)&&pwdVal.matcher(s).matches();
	}
	
	/**
	 * 是否相同
	 * @param srcStr
	 * @param toStr
	 * @return
	 */
	public static boolean confirm(String srcStr,String toStr){
		return srcStr.equals(toStr);
	}
	
	/**
	 * 特殊字符校验
	 * @param s
	 * @return
	 */
	public static boolean specialVal(String s){
		return required(s)&&specialVal.matcher(s).matches();
	}
	
	/**
	 * 手机号码校验
	 * @param s
	 * @return
	 */
	public static boolean mobileVal(String s){
		return required(s)&&mobileVal.matcher(s).matches();
	}
	
	/**
	 * 电话号码校验
	 * @param s
	 * @return
	 */
	public static boolean telVal(String s){
		return required(s)&&telVal.matcher(s).matches();
	}
	
	/**
	 * 营业执照校验
	 * @param s
	 * @return
	 */
	public static boolean businessLicenseVal(String s){
		return required(s)&&businessLicenseVal.matcher(s).matches();
	}
	
	/**
	 * 组织机构校验
	 * @param s
	 * @return
	 */
	public static boolean organizationCodeVal(String s){
		return required(s)&&organizationCodeVal.matcher(s).matches();
	}
	
	/**
	 * 注册资金校验
	 * @param s
	 * @return
	 */
	public static boolean registerFundVal(String s){
		return required(s)&&registerFundVal.matcher(s).matches();
	}
	
	
	/**
	 * url校验
	 * @param s
	 * @return
	 */
	public static boolean url(String s){
		return required(s)&&url.matcher(s).matches();
	}

}
