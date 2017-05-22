package com.luckysweetheart.config;

import com.luckysweetheart.common.IdWorker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
