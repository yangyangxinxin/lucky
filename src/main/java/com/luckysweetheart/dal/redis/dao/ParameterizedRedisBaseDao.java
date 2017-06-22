package com.luckysweetheart.dal.redis.dao;

import com.alibaba.fastjson.JSON;
import com.luckysweetheart.dal.DatabaseNamingStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

@Transactional(rollbackFor = Exception.class)
@Service
public class ParameterizedRedisBaseDao<T, PK extends Serializable> {
    public static Logger logger = LoggerFactory.getLogger(BaseRedisDao.class);

    @Resource
    BaseRedisDao baseRedisDao;

    @Resource
    private DatabaseNamingStrategy databaseNamingStrategy;


    public Class<T> entityClass;

    public ParameterizedRedisBaseDao() {
        initEntityClass(getClass());
    }

    private void initEntityClass(Class c) {
        Type type = c.getGenericSuperclass();
        if (type != null && type instanceof ParameterizedType) {
            Type[] parameterizedType = ((ParameterizedType) type)
                    .getActualTypeArguments();
            this.entityClass = (Class<T>) parameterizedType[0];
        } else {
            if (type != null) {
                initEntityClass((Class) type);
            }
        }
    }

    public void hset(PK pk, T t) {
        baseRedisDao.hset(getTableName(), pk.toString(), t.toString());
    }

    public T hget(PK pk) {
        return baseRedisDao.hget(getTableName(), pk + "", entityClass);
    }

    public boolean exists(String key) {//HEXISTS
        return baseRedisDao.exists(getTableName() + "_" + key);
    }


    public boolean hexists(PK pk) {//HEXISTS
        return baseRedisDao.hexists(getTableName(), pk);
    }

    /**
     * redis.hdel命令
     *
     * @return
     */
    public void hashdel(Object... hashKeys) {
        baseRedisDao.hdel(getTableName(), hashKeys);
    }

    /**
     * @param key
     * @return 后辍
     */
    public T get(String key) {
        return JSON.parseObject(baseRedisDao.get(getTableName() + "_" + key), entityClass);
    }

    /**
     * redis.set命令，超时时间为毫秒
     *
     * @param key
     * @return value
     */
    public void del(String key) {
        baseRedisDao.del(getTableName() + "_" + key);
    }

    /**
     * redis.set命令，超时时间为毫秒
     *
     * @param key
     * @return value
     */
    public void set(String key, T value, long timeout) {
        baseRedisDao.set(getTableName() + "_" + key, value.toString(), timeout);
    }


    public String getTableName() {
        return getTableName(entityClass);
    }

    /**
     * 此设置true后，会强制设置当前线程的stringRedisTemplate为一个,并开始事务,throw异常后自动回滚
     */
    public void setEnableTransactionSupport(boolean flag) {
        baseRedisDao.setEnableTransactionSupport(flag);
    }

    /**
     * 返回转换后的表名,同系统对mysql的表名转换|由于redis只有key value结构没有column所以没有对列名做转换
     *
     * @param c
     * @return
     */
    @SuppressWarnings("rawtypes")
    public String getTableName(Class c) {
        return databaseNamingStrategy.classToTableName(c.getSimpleName());
    }
}
