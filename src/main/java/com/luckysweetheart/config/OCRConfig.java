package com.luckysweetheart.config;

import com.baidu.aip.ocr.AipOcr;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by yangxin on 2017/8/14.
 */
@Configuration
public class OCRConfig {

    @Value("${baidu.ocr.appkey}")
    private String appkey;

    @Value("${baidu.ocr.appsecret}")
    private String appsecret;

    @Value("${baidu.ocr.appid}")
    private String appid;

    @Value("${baidu.ocr.connetion.timeout}")
    private Integer timeout;

    @Bean
    public AipOcr getAipOcr() {
        AipOcr aipOcr = new AipOcr(appid, appkey, appsecret);
        aipOcr.setConnectionTimeoutInMillis(timeout);
        return aipOcr;
    }


}
