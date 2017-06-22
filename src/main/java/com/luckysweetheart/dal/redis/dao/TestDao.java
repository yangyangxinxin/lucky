package com.luckysweetheart.dal.redis.dao;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * Created by yangxin on 2017/6/20.
 */
@Repository
public class TestDao extends ParameterizedRedisBaseDao<String,Long>{

    public void set(String key,String value){
        super.set(key,value,10000000);
    }

}
