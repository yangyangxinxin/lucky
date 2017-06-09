package com.luckysweetheart.dal.dao;

import com.luckysweetheart.dal.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by yangxin on 2017/5/22.
 */
@Repository
public class UserDao extends ParameterizedBaseDAO<User, Long>{

    public User findByMobilePhone(String mobilePhone){
        String hql = "from User where mobilePhone = :mobilePhone";
        List<User> list =  this.getSession().createQuery(hql).setString("mobilePhone",mobilePhone).list();
        if(list != null && list.size() > 0){
            return list.get(0);
        }
        return null;
    }

    // hibernate的占位符是从0下标开始
    public User login(String mobilePhone,String password){
        String hql = "from User where mobilePhone = ? and password = ?";
        List<User> list =  this.getSession().createQuery(hql).setString(0,mobilePhone).setString(1,password).list();
        if(list != null && list.size() > 0){
            return list.get(0);
        }
        return null;
    }

}
