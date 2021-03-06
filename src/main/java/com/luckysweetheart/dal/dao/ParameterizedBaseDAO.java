package com.luckysweetheart.dal.dao;

import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * 带泛型的DAO
 *
 */
@Transactional(rollbackFor = Exception.class)
public abstract class ParameterizedBaseDAO<T, PK extends Serializable> extends BaseDAO {

    protected Class<T> entityClass;

    public ParameterizedBaseDAO() {
        initEntityClass(getClass());
    }

    public T get(PK pk) {
        return this.get(entityClass, pk);
    }

    public T load(PK pk) {
        return load(entityClass, pk);
    }

    public List<T> getAll() {
        return getAll(entityClass);
    }

    public Long getTotalCount() {
        return getTotalCount(entityClass);
    }

    /**
     * 递归处理,使此类可以被多层extends
     *
     * @param c
     */
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
}