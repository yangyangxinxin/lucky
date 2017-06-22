package com.luckysweetheart;

import com.luckysweetheart.dal.redis.dao.UserInfoRedisDao;
import com.luckysweetheart.dal.redis.entity.UserInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.annotation.Resource;

/**
 * Created by yangxin on 2017/6/21.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = LuckyWebApplication.class)
@WebAppConfiguration
public class RedisTest {

    @Resource
    private UserInfoRedisDao userInfoRedisDao;

    @Test
    public void test1(){
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(1L);
        userInfo.setUsername("yangxin");
        userInfo.setMobile("18580090476");
        userInfoRedisDao.save(userInfo);
    }

    @Test
    public void test2(){
        System.out.println(userInfoRedisDao.findById(1L));
    }
}
