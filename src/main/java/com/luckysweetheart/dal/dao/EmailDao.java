package com.luckysweetheart.dal.dao;

import com.luckysweetheart.dal.entity.EmailSnapshoot;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by yangxin on 2017/6/17.
 */
@Repository
public class EmailDao extends ParameterizedBaseDAO<EmailSnapshoot, Long> {

    public List<EmailSnapshoot> findUnsuccess() {
        String hql = "from EmailSnapshoot where status = 2";
        return this.getSession().createQuery(hql).list();
    }

}
