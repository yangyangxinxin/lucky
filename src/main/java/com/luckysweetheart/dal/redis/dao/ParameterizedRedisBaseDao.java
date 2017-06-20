package com.luckysweetheart.dal.redis.dao;

import com.alibaba.fastjson.JSON;
import com.luckysweetheart.dal.DatabaseNamingStrategy;
import com.luckysweetheart.utils.SpringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

@Transactional(rollbackFor = Exception.class)
public class ParameterizedRedisBaseDao<T, PK extends Serializable> {
    public static Logger logger = LoggerFactory.getLogger(BaseRedisDao.class);

    BaseRedisDao baseRedisDao;

    private DatabaseNamingStrategy databaseNamingStrategy;

    protected void init() {
        try {
            if (baseRedisDao == null) {
                baseRedisDao = SpringUtil.getBean("baseRedisDao", BaseRedisDao.class);
                baseRedisDao.init();
            }
            if (databaseNamingStrategy == null) {
                databaseNamingStrategy = SpringUtil.getBean("databaseNamingStrategyRedis", DatabaseNamingStrategy.class);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }


    public Class<T> entityClass;

    public ParameterizedRedisBaseDao() {
        initEntityClass(getClass());
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private void initEntityClass(Class c) {
        Type type = c.getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            Type[] parameterizedType = ((ParameterizedType) type)
                    .getActualTypeArguments();
            this.entityClass = (Class<T>) parameterizedType[0];
        } else {
            initEntityClass((Class) type);
        }
    }

    public void hset(PK pk, T t) {
        init();
        baseRedisDao.hset(getTableName(), pk.toString(), t.toString());
    }

    public T hget(PK pk) {
        init();
        return baseRedisDao.hget(getTableName(), pk + "", entityClass);
    }

    public boolean exists(String key) {//HEXISTS
        init();
        return baseRedisDao.exists(getTableName() + "_" + key);
    }


    public boolean hexists(PK pk) {//HEXISTS
        init();
        return baseRedisDao.hexists(getTableName(), pk);
    }

    /**
     * redis.hdel命令
     *
     * @return
     */
    public void hashdel(Object... hashKeys) {
        init();
        baseRedisDao.hdel(getTableName(), hashKeys);
    }

    /**
     * @param key
     * @return 后辍
     */
    public T get(String key) {
        init();
        return JSON.parseObject(baseRedisDao.get(getTableName() + "_" + key), entityClass);
    }

    /**
     * redis.set命令，超时时间为毫秒
     *
     * @param key
     * @return value
     */
    public void del(String key) {
        init();
        baseRedisDao.del(getTableName() + "_" + key);
    }

    /**
     * redis.set命令，超时时间为毫秒
     *
     * @param key
     * @return value
     */
    public void set(String key, T value, long timeout) {
        init();
        baseRedisDao.set(getTableName() + "_" + key, value.toString(), timeout);
    }


    public String getTableName() {
        return getTableName(entityClass);
    }

    /**
     * 此设置true后，会强制设置当前线程的stringRedisTemplate为一个,并开始事务,throw异常后自动回滚
     */
    public void setEnableTransactionSupport(boolean flag) {
        init();
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
        if (databaseNamingStrategy == null) {
            databaseNamingStrategy = new DatabaseNamingStrategy();
            databaseNamingStrategy.setTablePrefix("themis_");
            databaseNamingStrategy.setMaxLength(64);
            databaseNamingStrategy.setIsAddUnderscores(true);
        }
        return databaseNamingStrategy.classToTableName(c.getSimpleName());
    }
}
