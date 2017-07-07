package com.luckysweetheart.dal.entity;

import com.alibaba.fastjson.JSON;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * Created by yangxin on 2017/6/23.
 */
@Entity
public class UserInfo {

    @Id
    @GeneratedValue
    private Long userId;

    private String name;

    private Integer deleteStatus;

    private Date createTime;

    private Date updateTime;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
