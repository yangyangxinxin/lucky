package com.luckysweetheart.dto;

import java.util.Date;

/**
 * Created by yangxin on 2017/5/22.
 */
public class UserDTO implements DTO {

    public static final Integer ACTIVE_STATUS_NO = 0;

    public static final Integer ACTIVE_STATUS_YES = 1;

    private Long userId;

    private String username;

    private String password;

    private String imgPath;

    private String mobilePhone;

    private String httpUrl;

    private String email;

    /**
     * 激活状态
     */
    private Integer activeStatus;

    /**
     * 激活码
     */
    private String activeCode;

    /**
     * 激活时间
     */
    private Date activeDate;

    public Integer getActiveStatus() {
        return activeStatus;
    }

    public void setActiveStatus(Integer activeStatus) {
        this.activeStatus = activeStatus;
    }

    public String getActiveCode() {
        return activeCode;
    }

    public void setActiveCode(String activeCode) {
        this.activeCode = activeCode;
    }

    public Date getActiveDate() {
        return activeDate;
    }

    public void setActiveDate(Date activeDate) {
        this.activeDate = activeDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHttpUrl() {
        return httpUrl;
    }

    public void setHttpUrl(String httpUrl) {
        this.httpUrl = httpUrl;
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
