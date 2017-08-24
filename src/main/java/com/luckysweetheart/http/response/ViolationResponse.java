package com.luckysweetheart.http.response;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by yangxin on 2017/8/23.
 */
public class ViolationResponse implements Serializable {

    private String token;

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
     * 违章的具体信息，有多条。
     */
    private List<Violations> violations;

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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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

    public List<Violations> getViolations() {
        return violations;
    }

    public void setViolations(List<Violations> violations) {
        this.violations = violations;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    /**
     * 违章的具体信息
     */
    public static class Violations{

        /**
         * 违章编码,唯一，非违章条例码
         */
        private String code;

        /**
         * 违章时间
         */
        private Date time;

        /**
         * 罚款金额
         */
        private Double fine;

        /**
         * 违章地址
         */
        private String address;

        /**
         * 违章处理原因
         */
        private String reason;

        /**
         * 违章扣分
         */
        private Integer point;

        /**
         * 违章发生城市，可能为空
         */
        private String violationCity;

        /**
         * 省份
         */
        private String province;

        /**
         * 城市
         */
        private String city;

        /**
         * 服务费
         */
        private Double serviceFee;

        /**
         * 代扣分费用
         */
        private Double markFee;

        /**
         * 能否勾选办理：0不可勾选, 1可勾选。
         */
        private Integer canSelect;

        /**
         * 违章处理状态：1：未处理，2：处理中，3：已处理，4：不支持
         */
        private Integer processStatus;

        /**
         * 违章官方编码
         */
        private String violationNum;

        /**
         * 违章缴费状态 不返回表示无法获取该信息，1-未缴费 2-已缴
         */
        private Integer paymentStatus;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public Date getTime() {
            return time;
        }

        public void setTime(Date time) {
            this.time = time;
        }

        public Double getFine() {
            return fine;
        }

        public void setFine(Double fine) {
            this.fine = fine;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }

        public Integer getPoint() {
            return point;
        }

        public void setPoint(Integer point) {
            this.point = point;
        }

        public String getViolationCity() {
            return violationCity;
        }

        public void setViolationCity(String violationCity) {
            this.violationCity = violationCity;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public Double getServiceFee() {
            return serviceFee;
        }

        public void setServiceFee(Double serviceFee) {
            this.serviceFee = serviceFee;
        }

        public Double getMarkFee() {
            return markFee;
        }

        public void setMarkFee(Double markFee) {
            this.markFee = markFee;
        }

        public Integer getCanSelect() {
            return canSelect;
        }

        public void setCanSelect(Integer canSelect) {
            this.canSelect = canSelect;
        }

        public Integer getProcessStatus() {
            return processStatus;
        }

        public void setProcessStatus(Integer processStatus) {
            this.processStatus = processStatus;
        }

        public String getViolationNum() {
            return violationNum;
        }

        public void setViolationNum(String violationNum) {
            this.violationNum = violationNum;
        }

        public Integer getPaymentStatus() {
            return paymentStatus;
        }

        public void setPaymentStatus(Integer paymentStatus) {
            this.paymentStatus = paymentStatus;
        }

        @Override
        public String toString() {
            return JSON.toJSONString(this);
        }
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
