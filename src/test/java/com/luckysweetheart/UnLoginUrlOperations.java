package com.luckysweetheart;

import com.luckysweetheart.service.UnLoginUrlService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.annotation.Resource;

/**
 * Created by yangxin on 2017/6/26.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = LuckyWebApplication.class)
@WebAppConfiguration
public class UnLoginUrlOperations {

    @Resource
    private UnLoginUrlService unLoginUrlService;

    @Test
    public void save(){
        unLoginUrlService.setUnLoginUrl("/article/getDetail");
    }

}
