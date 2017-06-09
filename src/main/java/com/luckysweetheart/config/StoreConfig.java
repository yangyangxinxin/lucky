package com.luckysweetheart.config;

import com.luckysweetheart.utils.DateUtil;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.sign.Credentials;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Date;

/**
 * Created by yangxin on 2017/5/22.
 */
@Configuration
public class StoreConfig {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 初始化秘钥信息
     *
     * @return
     */
    @Bean
    public Credentials getCredentials() {
        logger.info("初始化COS存储私钥信息 at {} ", DateUtil.formatDate(new Date()));
        Long appId = 1253770331L;
        String secretId = "AKIDdFhz62SVzHQN9RaA4sX6YxGOVTGrjsQa";
        String secretKey = "GTTPAmEWdeVBfx169jKp8GD3h9QVPLIG";
        return new Credentials(appId, secretId, secretKey);
    }

    /**
     * 初始化客户端配置(如设置园区)
     */
    @Bean
    public ClientConfig getClient() {
        logger.info("初始化COS存储客户端配置，bucket所在区域：华北园区(tj) at {} ", DateUtil.formatDate(new Date()));
        // 初始化客户端配置
        ClientConfig clientConfig = new ClientConfig();
        // 设置bucket所在的区域，比如华南园区：gz； 华北园区：tj；华东园区：sh ；
        clientConfig.setRegion("tj");
        return clientConfig;
    }

    /**
     * 生成客户端
     */
    @Bean
    public COSClient getCosClient(ClientConfig clientConfig, Credentials cred) {
        logger.info("初始化COS存储客户端 at {} ", DateUtil.formatDate(new Date()));
        logger.info("存储初始化成功 at {} ", DateUtil.formatDate(new Date()));
        return new COSClient(clientConfig, cred);
    }

}
