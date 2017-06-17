package com.luckysweetheart.dal.dao;

import com.luckysweetheart.dal.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by yangxin on 2017/5/22.
 */
@Repository
public class UserDao extends ParameterizedBaseDAO<User, Long>{

    public User findByMobilePhoneOrEmail(String mobileOrEmail){
        String hql = "from User where mobilePhone = :mobileOrEmail or email = :mobileOrEmail";
        List<User> list =  this.getSession().createQuery(hql).setString("mobileOrEmail",mobileOrEmail).setString("mobileOrEmail",mobileOrEmail).list();
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
