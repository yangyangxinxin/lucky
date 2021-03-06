package com.luckysweetheart.utils;

import com.luckysweetheart.common.Const;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.util.Assert;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


public class AesUtil {

    private static final String AES = "AES/CBC/PKCS5Padding";
    private static final String CRYPT_KEY = "www.ebaoquan.com";
    private static final String IV = "www.ebaoquan.com";
    private static final String SALT = Const.SALT;

    /**
     * 加密
     *
     * @param src
     * @param key
     * @return byte[]    返回类型
     * @throws Exception
     */
    public static byte[] encrypt(byte[] src, String key) throws Exception {
        Cipher cipher = Cipher.getInstance(AES);
        SecretKeySpec securekey = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
        IvParameterSpec ivspec = new IvParameterSpec(IV.getBytes("UTF-8"));
        cipher.init(Cipher.ENCRYPT_MODE, securekey, ivspec);
        return cipher.doFinal(src);
    }

    /**
     * 解密
     *
     * @param src
     * @param key
     * @return byte[]    返回类型
     * @throws Exception
     */
    public static byte[] decrypt(byte[] src, String key) throws Exception {
        Cipher cipher = Cipher.getInstance(AES);
        SecretKeySpec securekey = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
        IvParameterSpec ivspec = new IvParameterSpec(IV.getBytes("UTF-8"));
        cipher.init(Cipher.DECRYPT_MODE, securekey, ivspec);
        return cipher.doFinal(src);
    }

    /**
     * 二行制转十六进制字符串
     *
     * @param b
     * @return String    返回类型
     */
    private static String byte2hex(byte[] b) {
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = (Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1) {
                hs = hs + "0" + stmp;
            } else {
                hs = hs + stmp;
            }
        }
        return hs.toUpperCase();
    }

    private static byte[] hex2byte(byte[] b) {
        if ((b.length % 2) != 0) {
            throw new IllegalArgumentException("长度不是偶数");
        }
        byte[] b2 = new byte[b.length / 2];
        for (int n = 0; n < b.length; n += 2) {
            String item = new String(b, n, 2);
            b2[n / 2] = (byte) Integer.parseInt(item, 16);
        }
        return b2;
    }

    /**
     * 解密(供外部调用)
     *
     * @param data
     * @return String    返回类型
     */
    public final static String decrypt(String data) {
        try {
            return new String(decrypt(hex2byte(data.getBytes("UTF-8")), CRYPT_KEY));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public final static String decrypt(String data, String key) {
        try {
            return new String(decrypt(hex2byte(data.getBytes("UTF-8")), key));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 加密(供外部调用)
     *
     * @param data
     * @return String    返回类型
     */
    public final static String encrypt(String data) {
        try {
            return byte2hex(encrypt(data.getBytes("UTF-8"), CRYPT_KEY));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public final static String encrypt(String data, String key) {
        try {
            return byte2hex(encrypt(data.getBytes("UTF-8"), key));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String sha512Hex(String str) {
        try {
            Assert.notNull(str, "要加密的字符串不能为空");
            return DigestUtils.sha512Hex(SALT + str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }



}
