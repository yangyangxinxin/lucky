package com.luckysweetheart.dal.dao;

import com.luckysweetheart.dal.entity.UserInfo;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Created by yangxin on 2017/6/23.
 */
@Repository
public class UserInfoDao extends ParameterizedBaseDAO<UserInfo,Long> {

    public void updateA(){
       // String hql = "update UserInfo set deleteStatus = 1 where user_id in ( select b.userId from ( select a.userId from UserInfo a where deleteStatus != 1) b )";
        String hql = "select a.userId from UserInfo a where name like :name";
        List<Long> list = this.getSession().createQuery(hql).setString("name", "%1%").list();
        String sql = "update UserInfo set deleteStatus = 1 where userId in (:list)";
        this.getSession().createQuery(sql).setParameterList("list",list).executeUpdate();
    }

    public void updateB(){
        String hql = "select a.userId from UserInfo a where name like :name";
        List<Long> list = this.getSession().createQuery(hql).setString("name", "%2%").list();
        String hql2 = "update UserInfo set createTime = :createTime , updateTime = :updateTime where userId in(:list)";
        Date data = new Date(new Date().getTime() + 1000 * 60 * 60 * 300);
        int i = this.getSession().createQuery(hql2).setDate("createTime",data).setDate("updateTime", new Date()).setParameterList("list",list).executeUpdate();
        System.out.println(i);
    }
}
