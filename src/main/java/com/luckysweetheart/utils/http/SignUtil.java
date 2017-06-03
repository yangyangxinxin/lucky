package com.luckysweetheart.utils.http;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * Created by wlinguo on 2015/8/25.
 */
public class SignUtil {

    public static String sign(String appSecret, String paramsStr, String headStr, String extStr){
        return DigestUtils.sha1Hex(appSecret + paramsStr + headStr + extStr);
    }
}
