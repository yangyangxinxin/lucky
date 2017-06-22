package com.luckysweetheart.dal.redis.entity;

import com.alibaba.fastjson.JSONObject;
import com.luckysweetheart.dal.DatabaseNamingStrategy;

import java.io.Serializable;

/**
 * Created by yangxin on 2017/6/21.
 */
public abstract class BaseRedisEntity implements Serializable {

    public String toString() {
        return JSONObject.toJSONString(this);
    }

    /**
     * 返回本实体对应的redis中的key值
     */
    public String myKey() {
        return DatabaseNamingStrategy.addUnderscores(this.getClass().getSimpleName());
    }
}
