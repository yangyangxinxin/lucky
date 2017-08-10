package com.luckysweetheart.config;

import com.aliyun.oss.ClientConfiguration;
import com.aliyun.oss.OSSClient;
import com.luckysweetheart.utils.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 阿里云OSS存储
 * Created by yangxin on 2017/8/10.
 */
@Configuration
public class OSSConfig {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${oss.endPoint}")
    private String endpoint;

    @Value("${oss.access.key.id}")
    private String accessKeyId;

    @Value("${oss.access.key.sercet}")
    private String accessKeySecret;

    /**
     * ClientConfiguration是OSSClient的配置类，可配置代理、连接超时、最大连接数等参数。
     * @return
     */
    @Bean
    public ClientConfiguration getClientConfiguration() {
        // 创建ClientConfiguration实例
        ClientConfiguration conf = new ClientConfiguration();

        // 设置OSSClient使用的最大连接数，默认1024
        conf.setMaxConnections(200);

        // 设置请求超时时间，默认50秒
        conf.setSocketTimeout(10000);

        // 设置失败请求重试次数，默认3次
        conf.setMaxErrorRetry(5);

        return conf;
    }

    /**
     * 初始化OSS
     * @param configuration
     * @return
     */
    @Bean
    public OSSClient getOSSClient(ClientConfiguration configuration){
        logger.info("初始化 阿里云 OSS 存储 at {}", DateUtil.formatNow());
        // 创建OSSClient实例
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret, configuration);
        logger.info("初始化 阿里云 OSS 存储 完成 at {}", DateUtil.formatNow());
        return ossClient;
    }

}
