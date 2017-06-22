package com.luckysweetheart.dal.redis.entity;

/**
 * Created by yangxin on 2017/6/21.
 */
public class UserInfo extends BaseRedisEntity {

    private Long userId;

    private String username;

    private String mobile;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
