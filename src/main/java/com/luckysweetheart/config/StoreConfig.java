package com.luckysweetheart.config;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.sign.Credentials;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by yangxin on 2017/5/22.
 */
@Configuration
public class StoreConfig {

    @Value("${store.appId}")
    private Long appId;

    @Value("${store.SecretId}")
    private String secretId;

    @Value("${store.SecretKey}")
    private String secretKey;

    /**
     * 初始化秘钥信息
     * @return
     */
    @Bean
    public Credentials getCredentials(){
        return new Credentials(appId,secretId,secretKey);
    }

    /**
     * 初始化客户端配置(如设置园区)
     */
    @Bean
    public ClientConfig getClient(){
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
    public COSClient getCosClient(ClientConfig clientConfig,Credentials cred){
        return new COSClient(clientConfig, cred);
    }

}
