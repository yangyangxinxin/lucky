package com.luckysweetheart.dal.dao;

import com.luckysweetheart.dal.entity.User;
import org.springframework.stereotype.Repository;

/**
 * Created by yangxin on 2017/5/22.
 */
@Repository
public class UserDao extends ParameterizedBaseDAO<User, Long>{

    public User findByMobilePhone(String mobilePhone){
        String hql = "from User where mobilePhone = :mobilePhone";
        return (User) this.getSession().createQuery(hql).setString("mobilePhone",mobilePhone).list().get(0);
    }

    public User login(String mobilePhone,String password){
        String hql = "from User where mobilePhone = ?1 and password = ?2";
        return (User) this.getSession().createQuery(hql).setString(1,mobilePhone).setString(2,password).list().get(0);
    }

}
