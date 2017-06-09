package com.luckysweetheart.dal.dao;

import com.luckysweetheart.dal.entity.StoreData;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by yangxin on 2017/5/22.
 */
@Repository
public class StoreDataDao extends ParameterizedBaseDAO<StoreData, Long> {

    public StoreData findByResourcePath(String resourcePath) {
        String hql = "from StoreData where resourcePath = ?";
        List<StoreData> list = this.getSession().createQuery(hql).setString(0, resourcePath).list();
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    public StoreData findByCosPath(String cosPath) {
        String hql = "from StoreData where cosPath = ?";
        List<StoreData> list = this.getSession().createQuery(hql).setString(0, cosPath).list();
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

}
