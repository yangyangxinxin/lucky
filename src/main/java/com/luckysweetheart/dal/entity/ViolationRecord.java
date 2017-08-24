package com.luckysweetheart.dal.entity;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by yangxin on 2017/8/24.
 */
@Entity
@DynamicUpdate
@DynamicInsert
public class ViolationRecord {

    public static final int SUCCESS = 0;

    public static final int FAIL = 1;

    @Id
    @GeneratedValue
    private Long violationId;

    /**
     * 是否成功0：成功，1：失败
     */
    private Integer success;

    /**
     * 未处理违章总罚款
     */
    private Double totalFine;

    /**
     * 未处理违章总扣分
     */
    private Integer totalPoints;

    /**
     * 未处理违章条数
     */
    private Integer untreated;

    /**
     * 违章的具体信息，有多条。(JSON格式)
     */
    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(columnDefinition = "LONGTEXT")
    private String violations;

    /**
     * 违章总条数
     */
    private Integer amount;

    /**
     * 车牌号
     */
    private String plateNumber;

    /**
     * 发动机号
     */
    private String engineNo;

    /**
     * 车架号
     */
    private String vin;

    private Date createTime;

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getViolationId() {
        return violationId;
    }

    public void setViolationId(Long violationId) {
        this.violationId = violationId;
    }

    public Double getTotalFine() {
        return totalFine;
    }

    public void setTotalFine(Double totalFine) {
        this.totalFine = totalFine;
    }

    public Integer getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(Integer totalPoints) {
        this.totalPoints = totalPoints;
    }

    public Integer getUntreated() {
        return untreated;
    }

    public void setUntreated(Integer untreated) {
        this.untreated = untreated;
    }

    public String getViolations() {
        return violations;
    }

    public void setViolations(String violations) {
        this.violations = violations;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public String getEngineNo() {
        return engineNo;
    }

    public void setEngineNo(String engineNo) {
        this.engineNo = engineNo;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }
}
