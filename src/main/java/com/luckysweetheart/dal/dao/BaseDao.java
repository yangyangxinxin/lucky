package com.luckysweetheart.dal.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

/**
 * Created by yangxin on 2017/5/22.
 */
@Repository
public interface BaseDao<T,PK extends Serializable> extends CrudRepository<T,PK> {


}
