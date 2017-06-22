package com.luckysweetheart.dal.dao;

import com.luckysweetheart.dal.entity.UnLoginUrl;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by yangxin on 2017/6/22.
 */
@Repository
public class UnLoginUrlDao extends ParameterizedBaseDAO<UnLoginUrl, Long> {

    public List<String> queryUnLoginUrl() {
        String hql = "select a.url from UnLoginUrl a";
        return this.getSession().createQuery(hql).list();
    }

}
