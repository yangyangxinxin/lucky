package com.luckysweetheart.web.utils;

import com.alibaba.fastjson.JSONObject;
import com.luckysweetheart.exception.ResultInfoException;
import org.slf4j.LoggerFactory;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.codec.Codecs;
import org.springframework.security.jwt.crypto.sign.RsaSigner;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;

import java.util.Map;

/**
 * Created by yangxin on 2017/07/21.
 */
public class JwtUtils {

    private static org.slf4j.Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    private static Object obj = new Object();

    /**
     * jzq.sso的私钥
     */
    private static String ssoAppPrivateKey = "-----BEGIN RSA PRIVATE KEY-----\n" +
            "MIICXQIBAAKBgQDI3tnCo7gw5IHuu0vLK9ptGrYfoxPQaCxLNb9qwIuwbhER5wIJ\n" +
            "FXjYX2IXt3LrhKQZSlZ4Md23jSz4xTMetZ761Bws2ddJ0BFx8xKBVN8jDM4eQ0JB\n" +
            "uStWAyUt5imRXy88wKqVkeUPo1T+JGGF+zpdkL4UOmP9xoVmWEBhWT1ZEwIDAQAB\n" +
            "AoGBAJJBYMDNwEEpQEn46bjnF0U1m8HxVW37S7T/T36E77Dok1k7oEnI7pJs8Nlf\n" +
            "4zcE/w7DB67AcDVDf6kqvIvxpeyl5nP4rw/XIcCqz5tYMyk0D+5IiOiP365p9ABV\n" +
            "vaAwRQjPxiGQAsB4PiP6/57qqwbEDHK1SdEltSwEVnLKDAdxAkEA40KZ18NKfwOu\n" +
            "dMctQipFBhg8v3GtEQzmeKmMEX35m7rivXhm5oqmFrAp2LciWZ94QdrYgSpmYZiA\n" +
            "MNCaQHbxDQJBAOJF5utFnpDa0fF3Jc7AqZVmEk+j+26udX92GBWNkk15j3lpLPOr\n" +
            "cMEhW2Xb4sFYBUFk1VTVxUbppkAlB0+3qp8CQGmWOwYNHSKQIlDtbXCtVO50+lx0\n" +
            "Q1WFqdrr40EJR5x8Ivam5yA70Nj/tiGTTKlZNzVZqXexrGmxwfVsJg7HdE0CQAOu\n" +
            "M2VcSon8pTGSSJEmXRvK/z1AY5SXTd3248ll9h1Afu7woQSNo1Xg3wW3H+cZMWyw\n" +
            "4S9yErVmk4ezknzfde0CQQDgxoxn2kUpOEkKW2PfckIq0vLDpAuDBuf/rpukRgOP\n" +
            "r2cURV0uRdMcLoCmOPVhZ76m+mlaqOVyv9imZEcowDAm\n" +
            "-----END RSA PRIVATE KEY-----\n";

    /**
     * lucky.sso的公钥
     */
    private static String ssoAppPublicKey = "-----BEGIN PUBLIC KEY-----\n" +
            "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDI3tnCo7gw5IHuu0vLK9ptGrYf\n" +
            "oxPQaCxLNb9qwIuwbhER5wIJFXjYX2IXt3LrhKQZSlZ4Md23jSz4xTMetZ761Bws\n" +
            "2ddJ0BFx8xKBVN8jDM4eQ0JBuStWAyUt5imRXy88wKqVkeUPo1T+JGGF+zpdkL4U\n" +
            "OmP9xoVmWEBhWT1ZEwIDAQAB\n" +
            "-----END PUBLIC KEY-----\n";

    /**
     * 签名工具
     */
    private static RsaSigner signer;

    private static RsaVerifier verifier;

    static {
        signer = new RsaSigner(ssoAppPrivateKey);
        verifier = new RsaVerifier(ssoAppPublicKey);
    }

    public byte[] enSign(String data) {
        byte[] content = Codecs.utf8Encode(data);
        return signer.sign(content);
    }


    /**
     * 加密token
     *
     * @param json 要加密的json
     * @return
     */
    public static String encode(JSONObject json) {
        Jwt jwt = JwtHelper.encode(json.toJSONString(), signer);
        return jwt.getEncoded();
    }

    /**
     * @param json    要加密的json
     * @param headers 共享数据，这个可以JwtHelper.headers取得map得取得：{headers.key=headers.val, alg=HS256, typ=JWT}
     * @return
     */
    public static String encode(JSONObject json, Map<String, String> headers) {
        Jwt jwt = JwtHelper.encode(json.toJSONString(), signer, headers);
        return jwt.getEncoded();
    }

    /**
     * 解密token|token自带了解密串，使用了公私对串再进行一次校验，确认是lucky发出
     *
     * @return
     */
    public static JSONObject decode(String token) {
        try {
            Jwt jwt = JwtHelper.decodeAndVerify(token, verifier);
            return JSONObject.parseObject(jwt.getClaims());
        } catch (RuntimeException e) {
            logger.error(e.getMessage(),e);
            throw new ResultInfoException("jwtError", "校验失败");
        }
    }


}
