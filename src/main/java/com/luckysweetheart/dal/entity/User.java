package com.luckysweetheart.dal.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * Created by yangxin on 2017/5/22.
 */
@Entity
public class User {

    public static final Integer DELETE_STATUS_NO = 0;

    public static final Integer DELETE_STATUS_YES = 1;

    @Id
    @GeneratedValue
    private Long userId;

    private String mobilePhone;

    private String username;

    private String password;

    /**
     * 是存储的id，如果需要显示htpUrl，还需要转换，StoreDataDao.getHttpUrl()
     */
    private String imgPath;

    @Column(nullable = false)
    private Integer deleteStatus;

    private Date createTime;

    private Date updateTime;

    public Integer getDeleteStatus() {
        return deleteStatus;
    }

    public void setDeleteStatus(Integer deleteStatus) {
        this.deleteStatus = deleteStatus;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }
}
