package com.luckysweetheart.http.request;

import java.io.Serializable;

/**
 * Created by yangxin on 2017/8/23.
 */
public class ViolationRequest implements Serializable {

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
}
