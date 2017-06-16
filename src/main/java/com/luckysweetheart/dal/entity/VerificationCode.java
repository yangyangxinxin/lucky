package com.luckysweetheart.dal.entity;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * Created by yangxin on 2017/6/16.
 */
@Entity
@DynamicUpdate
@DynamicInsert
public class VerificationCode {

    /**
     * 过期时间30分钟
     */
    public static final Long expireTime = 30 * 60 * 1000L;

    @Id
    @GeneratedValue
    private Long codeId;

    private String mobileOrEmail;

    private String code;

    /**
     * 验证码类型，例如注册、登录、激活等，之后写在常量里面
     */
    private String type;

    /**
     * 过期时间
     */
    @Column(nullable = false)
    private Long expire;

    private Date createTime;

    public Long getCodeId() {
        return codeId;
    }

    public void setCodeId(Long codeId) {
        this.codeId = codeId;
    }

    public String getMobileOrEmail() {
        return mobileOrEmail;
    }

    public void setMobileOrEmail(String mobileOrEmail) {
        this.mobileOrEmail = mobileOrEmail;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getExpire() {
        return expire;
    }

    public void setExpire(Long expire) {
        this.expire = expire;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
