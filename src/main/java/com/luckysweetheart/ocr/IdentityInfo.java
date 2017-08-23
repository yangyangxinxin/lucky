package com.luckysweetheart.ocr;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.mapu.common.DateUtil;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by yangxin on 2017/8/11.
 */
public class IdentityInfo implements Serializable {

    /**
     * 姓名
     */
    private String name;

    /**
     * 性别
     */
    private String gender;

    /**
     * 民族
     */
    private String nation;

    /**
     * 出生日期(yyyy-MM-dd)
     */
    private String birthday;

    /**
     * 住址
     */
    private String address;

    /**
     * 身份证号码
     */
    private String idCard;

    /***********************身份证反面信息************************/

    /**
     * 身份证失效时间(yyyy-MM-dd)
     */
    private String overdueTime;

    /**
     * 该身份证是否已经过期
     */
    private Boolean isOverdue;

    /**
     * 签发机关
     */
    private String issuingAuthority;

    /**
     * 签发日期
     */
    private String issuingTime;

    /*************************风险检测***************************/

    /**
     * 身份证风险类型，是否为正常身份证、复印件、临时身份证、翻拍和其他未知的情况
     */
    private String riskType;

    /**
     * 如果身份证被编辑过，则返回该编辑工具如:Adobe Photoshop CC 2014 (Macintosh),如果没有被编辑过则返回值无此参数
     */
    private String editTool;

    public String getRiskType() {
        return riskType;
    }

    public void setRiskType(String riskType) {
        this.riskType = riskType;
    }

    public String getEditTool() {
        return editTool;
    }

    public void setEditTool(String editTool) {
        this.editTool = editTool;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getOverdueTime() {
        return overdueTime;
    }

    public void setOverdueTime(String overdueTime) {
        this.overdueTime = overdueTime;
    }

    public String getIssuingAuthority() {
        return issuingAuthority;
    }

    public void setIssuingAuthority(String issuingAuthority) {
        this.issuingAuthority = issuingAuthority;
    }

    public String getIssuingTime() {
        return issuingTime;
    }

    public void setIssuingTime(String issuingTime) {
        this.issuingTime = issuingTime;
    }

    /**
     * 验证该身份证是否已过有效期。
     *
     * @return 过期返回true, 没过期返回false
     */
    public Boolean getOverdue() {
        if (StringUtils.isNotBlank(overdueTime)) {
            long now = new Date().getTime();
            Date date = DateUtil.getDate(this.overdueTime, "yyyyMMdd");
            if (date != null) {
                long overdueTime = date.getTime();
                return now > overdueTime;
            }
        }
        return Boolean.FALSE;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

    public static void main(String[] args) {
        IdentityInfo info = new IdentityInfo();
        info.setOverdueTime("20170814");
        System.out.println(info.getOverdue());
    }
}
