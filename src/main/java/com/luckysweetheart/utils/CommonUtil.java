package com.luckysweetheart.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import java.text.NumberFormat;
import java.util.UUID;


/**
 * 基本工具类
 * @author yfx
 *
 */
public class CommonUtil {

	public static String[] chars = new String[] { "a", "b", "c", "d", "e", "f",
			"g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s",
			"t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5",
			"6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I",
			"J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
			"W", "X", "Y", "Z" };

	/**
	 * 去空，null->''
	 * 
	 * @param str
	 * @return
	 */
	public static String trim(String str, String def) {
		if (str == null || "null".equals(str.trim()) || "".equals(str.trim())) {
			return def;
		} else {
			return str.trim();
		}
	}

	/**
	 * 去空，null->''
	 * 
	 * @param str
	 * @return
	 */
	public static String trim(String str) {
		if (str == null || "null".equals(str.trim())) {
			return "";
		} else {
			return str.trim();
		}
	}

	/**
	 * 去空,支持sql（发现为空时返回null）
	 * 
	 * @param str
	 * @return
	 */
	public static String trimForSql(String str) {
		if (str == null || "".equals(str.trim()) || "null".equals(str.trim())) {
			return null;
		} else {
			return str.trim();
		}
	}

	/**
	 * 默认转换
	 * 
	 * @param obj
	 * @param cla
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T parVal(Object obj, Class<T> cla) {
		if (cla.equals(String.class)) {
			if (obj == null || "".equals(obj + "") || "null".equals(obj)) {
				return (T) "";
			} else {
				return (T) (obj + "");
			}
		} else {
			try {
				if (obj == null)
					return null;
				if (cla.equals(Integer.class))
					return (T) Integer.valueOf(obj + "");
				if (cla.equals(Long.class))
					return (T) Long.valueOf(obj + "");
				if (cla.equals(Float.class))
					return (T) Float.valueOf(obj + "");
				if (cla.equals(Double.class))
					return (T) Double.valueOf(obj + "");
				if (cla.equals(Boolean.class))
					return (T) Boolean.valueOf(obj + "");
				return (T) obj;
			} catch (Exception e) {
				throw new RuntimeException("CommonUtil.parVal error|obj:" + obj+ ",class:" + cla);
			}
		}
	}

	/**
	 * 不返回错误的转换
	 * 
	 * @param obj
	 * @param cla
	 * @return
	 */
	public static <T> T parValNoErr(Object obj, Class<T> cla) {
		try {
			return parVal(obj, cla);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 不返回错误的转换，为空或异常时返回默认值
	 * @param obj
	 * @param cla
	 * @param t
	 * @return
	 */
	public static <T> T parValNoErrDef(Object obj, Class<T> cla, T t) {
		try {
			T t1 = parVal(obj, cla);
			if (t1 == null || (cla.equals(String.class) && "".equals(t1))) {
				return t;
			}
			return t1;
		} catch (Exception e) {
			return t;
		}
	}

	/**
	 * 补位数据 ，前补0
	 * 
	 * @param
	 * @param length
	 * @return
	 */
	public static String conver0(Object src, int length) {
		NumberFormat formatter = NumberFormat.getNumberInstance();
		formatter.setMinimumIntegerDigits(length);
		formatter.setGroupingUsed(false);
		return formatter.format(src);
	}
	
	
	/**
	 * 邮箱遮挡
	 * @param email
	 * @return
	 */
	public static String privacyEmail(String email){
		if(StringUtils.isBlank(email)){return "";}
		if(email.indexOf("@")-4>0){
			email=email.substring(0,email.indexOf("@")-4)+"****"+email.substring(email.indexOf("@"),email.length());
		}else{
			email=email.substring(0,1)+"****"+email.substring(email.indexOf("@"),email.length());
		}
		return email;
	}
	
	/**
	 * 手机遮挡
	 * @param mobile
	 * @return
	 */
	public static String privacyMobile(String mobile){
		if(StringUtils.isBlank(mobile)){return "";}
		mobile=mobile.substring(0, 4)+"****"+mobile.substring(8);
		return mobile;
	}
	
	/**
	 * 电话遮挡
	 * @param
	 * @return
	 */
	public static String privacyPhone(String phone){
		if(StringUtils.isBlank(phone)){return "";}
		String pre="";
		String fix="";
		if(phone.indexOf("-")>0){
			pre=phone.substring(0,phone.lastIndexOf("-"));
			fix=phone.substring(phone.lastIndexOf("-")+1,phone.length());
			System.out.println(pre);
			System.out.println(fix);
			return pre+"-"+maskStr(fix, 3);
		}else{
			return maskStr(phone, 3);
		}
	}
	
	public static void main(String[] args) {
		//System.out.println(privacyPhone("400-808-9187"));
		System.out.println(generateShortUuid());
	}
	
	/**
	 * 查找字符组串中是否包含特定的字符串
	 * @param srcStr 原字符串
	 * @param divide 分隔符
	 * @param tagStr 要查找的字符串
	 * @return
	 */
	public static boolean contain(String srcStr,String divide,String tagStr){
		Assert.isTrue(StringUtils.isNotBlank(srcStr),"srcStr is null");
		Assert.isTrue(StringUtils.isNotBlank(divide),"divide is null");
		Assert.isTrue(StringUtils.isNotBlank(tagStr),"tagStr is null");
		if((divide+srcStr+divide).indexOf(divide+tagStr+divide)>-1){
			return true;
		}else{
			return false;
		}
	}
	
    public static Boolean isMobile(String userAgent){
    	if(StringUtils.isNotBlank(userAgent)){
    		userAgent=userAgent.toLowerCase();
    	}else{
    		return false;
    	}
        boolean flag = false;  
        String[] keywords = { "android", "iphone", "ipod", "ipad", "windows phone", "mqqbrowser","nokia","mobile" };
        //排除 Windows 桌面系统  
        if (!userAgent.contains("windows nt") || (userAgent.contains("windows nt") && userAgent.contains("compatible; msie 9.0;"))){
            //排除 苹果桌面系统  
            if (!userAgent.contains("windows nt") && !userAgent.contains("macintosh"))  
                {  
                    for (String item : keywords)  
                    {  
                        if (userAgent.contains(item))  
                        {  
                            flag = true;  
                            break;  
                        }  
                    }  
                }  
        }  
        return flag;  
    }  
    
	/**
	 * 生成遮档str
	 * @param str 原字符串
	 * @param skipInt 忽略长度
	 */
	public static String maskStr(String str,Integer skipInt){
		if(StringUtils.isBlank(str)){return "";}
		if(str!=null&&skipInt>0){
			if(str.length()==1){
				str="*";
			}else if(str.length()==2){
				str=str.substring(0,1)+"*";
			}else{
				if(!(str.length()>skipInt*2)){
					return maskStr(str,1);
				}
				String strPre=str.substring(0,skipInt);
				String strfix=str.substring(str.length()-skipInt);
				StringBuffer strMask=new StringBuffer();
				for(Integer i=0;i<str.length()-2*skipInt;i++){
					strMask.append("*");
				}
				return strPre+strMask+strfix;
			}
		}
		return str;
	}

	/*
	生成短8位UUID
	 */
	public static String generateShortUuid() {
		StringBuffer shortBuffer = new StringBuffer();
		String uuid = UUID.randomUUID().toString().replace("-", "");
		for (int i = 0; i < 8; i++) {
			String str = uuid.substring(i * 4, i * 4 + 4);
			int x = Integer.parseInt(str, 16);
			shortBuffer.append(chars[x % 0x3E]);
		}
		return shortBuffer.toString();
	}

}
