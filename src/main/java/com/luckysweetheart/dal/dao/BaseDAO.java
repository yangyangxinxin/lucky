package com.luckysweetheart.dal.dao;


import com.luckysweetheart.common.Paged;
import com.luckysweetheart.common.PagedResult;
import com.luckysweetheart.dal.entity.Photo;
import com.luckysweetheart.dal.query.param.QueryConditionParam;
import com.luckysweetheart.dal.query.param.QueryOrderParam;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.*;
import org.hibernate.metadata.ClassMetadata;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * 基本DAO
 */
@Transactional(rollbackFor = Exception.class)
public abstract class BaseDAO {

    @Resource
    private SessionFactory sessionFactory;

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public Session getSession() {
        // 事务必须是开启的(Required)，否则获取不到
        return sessionFactory.getCurrentSession();
    }

    public <T> T get(Class<T> entityClass, Serializable pk) {
        Assert.notNull(pk, "pk is required");
        return (T) getSession().get(entityClass, pk);
    }

    public <T> T load(Class<T> entityClass, Serializable pk) {
        Assert.notNull(pk, "pk is required");
        return (T) getSession().load(entityClass, pk);
    }

    public <T> List<T> getAll(Class<T> entityClass) {
        ClassMetadata classMetadata = getSessionFactory().getClassMetadata(entityClass);
        String hql = "from " + classMetadata.getEntityName();
        return getSession().createQuery(hql).list();
    }

    public <T> Long getTotalCount(Class<T> entityClass) {
        ClassMetadata classMetadata = getSessionFactory().getClassMetadata(entityClass);
        final String hql = "select count(*) from " + classMetadata.getEntityName();
        return (Long) getSession().createQuery(hql).uniqueResult();
    }

    public <T> T save(Object entity) {
        Assert.notNull(entity, "entity is required");
        return (T) getSession().save(entity);
    }

    public void saveOrUpdate(Object entity) {
        Assert.notNull(entity, "entity is required");
        getSession().saveOrUpdate(entity);
    }

    public void update(Object entity) {
        Assert.notNull(entity, "entity is required");
        getSession().update(entity);
    }

    public void delete(Object entity) {
        Assert.notNull(entity, "entity is required");
        getSession().delete(entity);
    }

    public <T> T findUnique(String hql, Object... params) {
        Query query = getSession().createQuery(hql);
        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                query.setParameter(i, params[i]);
            }
        }
        List<T> list = query.list();
        if (list.isEmpty()) {
            return null;
        }
        if (list.size() > 1) {
            throw new RuntimeException("result not unique!");
        }
        return list.get(0);
    }

    public <T> PagedResult<T> findByPaged(Class<T> entityClass, Paged paged) {
        if (paged == null) {
            paged = new Paged();
        }
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(entityClass);
        return findByPaged(paged, detachedCriteria, entityClass);
    }

    public <T> PagedResult<T> findByPaged(final Paged p, final DetachedCriteria detachedCriteria, final Class<T> tClass) {
        Paged paged = p;
        if (paged == null) {
            paged = new Paged();
        }

        Integer pageSize = paged.getPageSize();

        detachedCriteria.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
        //Criteria criteria = detachedCriteria.getExecutableCriteria(getSession());

        // 真TM是个坑！按照上面注释的写法，不知道为撒总是要报个空指针异常！！！！
        // WC这个坑是我自己填的。。。在PhotoQueryField中的getFiledName()方法中没有返回值，现在还是不知道为撒总是报
        // java.lang.ClassCastException: java.lang.Integer cannot be cast to java.lang.Long
        // 这个异常，不过如果按照下面那个写法的话 暂时没得问题，这是个神坑！！！

        Criteria criteria = getSession().createCriteria(tClass);
        Long totalCount = (Long) criteria.setProjection(
                Projections.rowCount()).uniqueResult();

        criteria.setProjection(null);
        criteria.setFirstResult(paged.getOffset());
        criteria.setMaxResults(pageSize);
        paged.setItems(totalCount.intValue());

        PagedResult<T> result = new PagedResult<T>();
        result.setPaged(paged);
        result.setResults(criteria.list());
        return result;
    }


    /**
     * 不建议使用，因为容易导致因HQL字段顺序变动引发的SQL错误
     *
     * @param hql
     * @param params
     * @param <T>
     * @return
     */
    @Deprecated
    public <T> List<T> findObjects(String hql, Object... params) {
        Query query = getSession().createQuery(hql);
        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                query.setParameter(i, params[i]);
            }
        }
        List<T> list = query.list();
        return list;
    }

    public void flush() {
        this.getSession().flush();
    }

    /**
     * 根据各种条件查询
     *
     * @param entityClass
     * @param paged
     * @param queryParams
     * @param queryOrderParams
     * @param entityClass
     * @return
     */
    @SuppressWarnings("rawtypes")
    public <T> PagedResult<T> query(Paged paged, List<QueryConditionParam> queryParams, List<QueryOrderParam> queryOrderParams, Class<T> entityClass) {
        DetachedCriteria detachedCriteria = DetachedCriteria.forClass(entityClass);
        detachedCriteria.setProjection(null);
        if (null != queryParams) {
            for (QueryConditionParam queryParam : queryParams) {
                String fieldName = queryParam.getQueryField();
                String operation = queryParam.getOperation();
                Object value = queryParam.getValue();
                if (operation.equalsIgnoreCase(QueryConditionParam.OPERATION_EQ)) {
                    detachedCriteria.add(Restrictions.eq(fieldName, value));
                } else if (operation.equalsIgnoreCase(QueryConditionParam.OPERATION_NE)) {
                    detachedCriteria.add(Restrictions.ne(fieldName, value));
                } else if (operation.equalsIgnoreCase(QueryConditionParam.OPERATION_GE)) {
                    detachedCriteria.add(Restrictions.ge(fieldName, value));
                } else if (operation.equalsIgnoreCase(QueryConditionParam.OPERATION_GT)) {
                    detachedCriteria.add(Restrictions.gt(fieldName, value));
                } else if (operation.equalsIgnoreCase(QueryConditionParam.OPERATION_LE)) {
                    detachedCriteria.add(Restrictions.le(fieldName, value));
                } else if (operation.equalsIgnoreCase(QueryConditionParam.OPERATION_LT)) {
                    detachedCriteria.add(Restrictions.lt(fieldName, value));
                } else if (operation.equalsIgnoreCase(QueryConditionParam.OPERATION_LIKE)) {
                    detachedCriteria.add(Restrictions.like(fieldName, value));
                } else if (operation.equalsIgnoreCase(QueryConditionParam.OPERATION_NOTLIKE)) {
                    detachedCriteria.add(Restrictions.not(Restrictions.like(fieldName, value)));
                } else if (operation.equalsIgnoreCase(QueryConditionParam.OPERATION_ISNULL)) {
                    detachedCriteria.add(Restrictions.isNull(fieldName));
                } else if (operation.equalsIgnoreCase(QueryConditionParam.OPERATION_ISNOTNULL)) {
                    detachedCriteria.add(Restrictions.isNotNull(fieldName));
                } else if (operation.equalsIgnoreCase(QueryConditionParam.OPERATION_IN)) {
                    detachedCriteria.add(Restrictions.in(fieldName, (Collection) value));
                }
            }
        }

        if (null != queryOrderParams) {
            for (QueryOrderParam queryOrderParam : queryOrderParams) {
                if (queryOrderParam.getOrderType().equalsIgnoreCase(QueryOrderParam.ORDER_TYPE_ASC)) {
                    detachedCriteria.addOrder(Order.asc(queryOrderParam.getOrderField()));
                } else if (queryOrderParam.getOrderType().equalsIgnoreCase(QueryOrderParam.ORDER_TYPE_DESC)) {
                    detachedCriteria.addOrder(Order.desc(queryOrderParam.getOrderField()));
                }
            }
        }

        return findByPaged(paged, detachedCriteria, entityClass);
    }
}