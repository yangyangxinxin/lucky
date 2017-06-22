package com.luckysweetheart.dal.redis.dao;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author yfx
 * @Description: 处理redis的dao基类
 * @date 2015年9月16日 上午10:39:13
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class BaseRedisDao {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());
    /**
     * redisTemplate的list集合操作类
     */
    @Resource
    protected ListOperations<String, String> listOperations;

    /**
     * redisTemplate的hash集合操作类
     */
    @Resource
    protected HashOperations<String, String, String> hashOperations;

    /**
     * 字符操作类
     */
    @Resource
    protected StringRedisTemplate stringRedisTemplate;

    public BaseRedisDao() {
    }

    /**
     * redis.hset命令|有覆盖的功能
     *
     * @param key
     * @param filed
     * @param val
     */
    public void hset(String key, String filed, String val) {
        logger.debug("hset:key|filed|val->" + key + "|" + filed + "|" + val);
        hashOperations.put(key, filed, val);
    }

    /**
     * redis.hget命令
     *
     * @param key
     * @param filed
     * @param cls
     * @return
     */
    public <T> T hget(String key, String filed, Class<T> cls) {
        logger.debug("hget:key|filed|cls->" + key + "|" + filed + "|" + cls.getCanonicalName());
        String result = hashOperations.get(key, filed);
        logger.debug("hget:key|filed|result->" + key + "|" + filed + "|" + result);
        return JSONObject.parseObject(result, cls);
    }

    /**
     * redis.hmget命令
     *
     * @param key
     * @param fileds
     * @return
     */
    public List<String> hmget(String key, Collection<String> fileds) {
        logger.debug("hget:key|filed|cls->" + key + "|" + fileds);
        return hashOperations.multiGet(key, fileds);
    }

    /**
     * redis.hkeys命令
     *
     * @param key
     * @return
     */
    public Set<String> hkeys(String key) {
        logger.debug("hget:key->" + key);
        return hashOperations.keys(key);
    }

    /**
     * redis.hvals命令
     *
     * @return
     */
    public List<String> hvals(String key) {
        logger.debug("hvals:key->" + key);
        return hashOperations.values(key);
    }

    /**
     * redis.hexists 查看是否存在某键
     *
     * @param key
     * @param filed
     * @return
     */
    public boolean hexists(String key, Object filed) {//HEXISTS
        logger.debug("hexists:key|filed->" + key + "|" + filed);
        return hashOperations.hasKey(key, filed);
    }

    /**
     * redis.hdel通过键删除多个值的命令
     *
     * @param key
     */
    protected void hdel(String key, Object... hashKeys) {
        logger.debug("hget:key|hashKeys->" + key + "|" + JSONObject.toJSONString(hashKeys));
        hashOperations.delete(key, hashKeys);
    }

    /**
     * redis.hexists 查看是否存在某键
     *
     * @param key
     * @return
     */
    public boolean exists(String key) {//HEXISTS
        logger.debug("exists:key->" + key);
        return stringRedisTemplate.hasKey(key);
    }

    /**
     * redis.del命令|跟据key删除其记录
     *
     * @param key
     */
    public void del(String key) {
        logger.debug("del:key->" + key);
        stringRedisTemplate.delete(key);
    }

    /**
     * redis.keys命令|其中pt为*时表示所有，一般不要使用
     *
     * @param pt 查询条件
     * @return
     */
    public Set<String> keys(String pt) {
        logger.debug("keys:pattern->" + pt);
        return stringRedisTemplate.keys(pt);
    }

    /**
     * redis.get命令
     *
     * @param key
     * @return value
     */
    public String get(String key) {
        logger.debug("key->" + key);
        return stringRedisTemplate.opsForValue().get(key);
    }

    /**
     * redis.set命令，超时时间为毫秒
     *
     * @param key
     * @return value
     */
    public void set(String key, String value, long timeout) {
        logger.debug("key|value->" + key + "|" + value);
        stringRedisTemplate.opsForValue().set(key, value, timeout, TimeUnit.MILLISECONDS);
    }

    /**
     * 此设置true后，会强制设置当前线程的stringRedisTemplate为一个,并开始事务,throw异常后自动回滚
     */
    public void setEnableTransactionSupport(boolean flag) {
        stringRedisTemplate.setEnableTransactionSupport(flag);
    }
}
