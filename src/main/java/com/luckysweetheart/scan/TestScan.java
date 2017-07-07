package com.luckysweetheart.scan;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * SpringBoot的定时任务
 * Created by yangxin on 2017/6/16.
 */
@Configuration
@EnableScheduling
public class TestScan {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    //@Scheduled(cron = "0/20 * * * * ?") // 每20秒执行一次
    public void scheduler() {
        logger.info(">>>>>>>>>>>>> scheduled ... ");
    }


}
