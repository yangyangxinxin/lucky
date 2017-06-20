package com.luckysweetheart.dal.redis.dao;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * Created by yangxin on 2017/6/20.
 */
@Repository
public class TestDao {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

}
