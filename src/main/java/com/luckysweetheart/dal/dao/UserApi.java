package com.luckysweetheart.dal.dao;

import com.luckysweetheart.dal.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by yangxin on 2017/5/22.
 */
@Repository
public interface UserApi extends CrudRepository<User, Long> {

    @Query(value = "select u from User u where mobilePhone=:mobilePhone and password=:password")
    User login(@Param("mobilePhone") String mobilePhone, @Param("password") String password);

    User findByMobilePhone(String mobilePhone);
}
