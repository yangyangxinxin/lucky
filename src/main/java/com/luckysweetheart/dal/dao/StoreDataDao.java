package com.luckysweetheart.dal.dao;

import com.luckysweetheart.dal.entity.StoreData;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by yangxin on 2017/5/22.
 */
@Repository
public interface StoreDataDao extends CrudRepository<StoreData,Long> {

    /**
     * 根据resourcePath 获取httpUrl
     * @param resourcePath
     * @return
     */
    //@Query(value = "select s.resourcePath from StoreData where resourcePath=:resourcePath and deleteStatus = DELETE_STATUS_NO")
    //String getHttpUrl(String resourcePath);

    StoreData findByResourcePath(String resourcePath);

    //StoreData update(StoreData storeData);

}
