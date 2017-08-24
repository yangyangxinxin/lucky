package com.luckysweetheart.ocr;

import java.io.Serializable;
import java.util.Date;

/**
 * 行驶证信息
 * Created by yangxin on 2017/8/24.
 */
public class VehicleLicenseInfo implements Serializable {

    /**
     * 车辆品牌
     */
    private String brand;

    /**
     * 发证日期
     */
    private Date issueTime;

    /**
     * 使用性质
     */
    private String useFunction;

    /**
     * 车牌号
     */
    private String plateNumber;

    /**
     * 发动机号码
     */
    private String engineNo;

    /**
     * 所有人
     */
    private String owner;

    /**
     * 住址
     */
    private String address;

    /**
     * 注册日期
     */
    private Date registerDate;

    /**
     * 车辆识别码，车架号
     */
    private String vin;

    /**
     * 车辆类型
     */
    private String carType;

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public Date getIssueTime() {
        return issueTime;
    }

    public void setIssueTime(Date issueTime) {
        this.issueTime = issueTime;
    }

    public String getUseFunction() {
        return useFunction;
    }

    public void setUseFunction(String useFunction) {
        this.useFunction = useFunction;
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

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(Date registerDate) {
        this.registerDate = registerDate;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }
}
