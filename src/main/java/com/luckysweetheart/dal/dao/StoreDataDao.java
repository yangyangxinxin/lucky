package com.luckysweetheart.dal.dao;

import com.luckysweetheart.dal.entity.StoreData;
import org.springframework.stereotype.Repository;

/**
 * Created by yangxin on 2017/5/22.
 */
@Repository
public class StoreDataDao extends ParameterizedBaseDAO<StoreData, Long> {

    public StoreData findByResourcePath(String resourcePath) {
        String hql = "from StoreData where resourcePath = ?1";
        return (StoreData) this.getSession().createQuery(hql).setString(1, resourcePath).list().get(0);
    }

    public StoreData findByCosPath(String cosPath){
        String hql = "from StoreData where cosPath = ?1";
        return (StoreData) this.getSession().createQuery(hql).setString(1, cosPath).list().get(0);
    }

}
