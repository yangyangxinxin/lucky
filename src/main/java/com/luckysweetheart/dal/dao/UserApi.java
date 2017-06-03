package com.luckysweetheart.dal.dao;

import com.luckysweetheart.dal.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by yangxin on 2017/5/22.
 */
@Repository
public interface UserApi extends JpaRepository<User, Long>,JpaSpecificationExecutor<User> {

    @Query(value = "select u from User u where mobilePhone=:mobilePhone and password=:password")
    User login(@Param("mobilePhone") String mobilePhone, @Param("password") String password);

    User findByMobilePhone(String mobilePhone);

    // 修改操作其实可以在service层直接update，查询出来的实体类对象是持久态的，直接调用set方法即可直接update

    /**
     * 修改用户名
     * @param username
     * @param userId
     */
    @Modifying
    @Query("update from User set username = ?1 where userId = ?2")
    void updateUserName(String username,Long userId);

    /**
     * 停用用户
     * @param userId
     */
    @Modifying
    @Query("update from User set deleteStatus = 1 where userId = ?1")
    void deleteUser(Long userId);

    /**
     * 修改手机号码
     * @param mobilePhone
     * @param userId
     */
    @Modifying
    @Query("update from User set mobilePhone = ?1 where userId = ?2")
    void updateMobilePhone(String mobilePhone,Long userId);

    /**
     * 修改用户头像
     * @param imgPath
     * @param userId
     */
    @Modifying
    @Query("update from User set imgPath = ?1 where userId = ?2")
    void updateUserImg(String imgPath,Long userId);

}
