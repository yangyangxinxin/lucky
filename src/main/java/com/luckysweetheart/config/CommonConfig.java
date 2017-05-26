package com.luckysweetheart.config;

import com.luckysweetheart.common.IdWorker;
import com.luckysweetheart.web.interceptor.AuthInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yangxin on 2017/5/22.
 */
@Configuration
public class CommonConfig {

    private final Long workerId = 13L;
    private final Long datacenterId = 24L;

    @Bean
    public IdWorker getIdWorker() {
        return new IdWorker(workerId, datacenterId);
    }

}
