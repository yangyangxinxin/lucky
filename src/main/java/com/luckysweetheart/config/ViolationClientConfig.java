package com.luckysweetheart.config;

import com.luckysweetheart.http.ViolationClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by yangxin on 2017/8/23.
 */
@Configuration
public class ViolationClientConfig {

    @Value("${ali.violation.appcode}")
    private String appCode;

    @Bean
    public ViolationClient violationClient(){
        return new ViolationClient(appCode);
    }

}
