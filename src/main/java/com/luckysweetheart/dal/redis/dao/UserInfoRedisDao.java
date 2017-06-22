package com.luckysweetheart.dal.redis.dao;

import com.luckysweetheart.dal.redis.entity.UserInfo;
import org.springframework.stereotype.Repository;


/**
 * Created by yangxin on 2017/6/21.
 */
@Repository
public class UserInfoRedisDao extends ParameterizedRedisBaseDao<UserInfo, Long> {

    public Long save(UserInfo userInfo) {
        hset(userInfo.getUserId(), userInfo);
        return userInfo.getUserId();
    }

    public UserInfo findById(Long userId){
        return hget(userId);
    }
}
