package com.luckysweetheart.dal.entity;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;

/**
 * 邮件快照，用来记录每次发送邮件的情况
 * Created by yangxin on 2017/6/16.
 */
@Entity
@DynamicInsert
@DynamicUpdate
public class EmailSnapshoot {

    public static final Integer SEND_SUCCESS = 0;

    public static final Integer SEND_ING = 1;

    public static final Integer SEND_FAIL = 2;

    @Id
    @GeneratedValue
    private Long emailId;

    private String sendTo;

    private String sendFrom;

    private String subject;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(columnDefinition = "LONGTEXT")
    private String content;

    private Date createTime;

    private Integer status;

    private Date successDate;

    private Date failDate;

    private Long tryTimes;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Long getEmailId() {
        return emailId;
    }

    public void setEmailId(Long emailId) {
        this.emailId = emailId;
    }

    public String getSendTo() {
        return sendTo;
    }

    public void setSendTo(String sendTo) {
        this.sendTo = sendTo;
    }

    public String getSendFrom() {
        return sendFrom;
    }

    public void setSendFrom(String sendFrom) {
        this.sendFrom = sendFrom;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getSuccessDate() {
        return successDate;
    }

    public void setSuccessDate(Date successDate) {
        this.successDate = successDate;
    }

    public Date getFailDate() {
        return failDate;
    }

    public void setFailDate(Date failDate) {
        this.failDate = failDate;
    }

    public Long getTryTimes() {
        return tryTimes;
    }

    public void setTryTimes(Long tryTimes) {
        this.tryTimes = tryTimes;
    }

    public String[] getSendToArray() {
        if (StringUtils.isBlank(this.sendTo)) {
            return null;
        }
        try {
            String sendTo = this.sendTo;
            return sendTo.split(";");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
