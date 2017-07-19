package com.luckysweetheart.web;

import com.luckysweetheart.utils.EmailTemplate;
import com.luckysweetheart.web.utils.SessionKeys;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yangxin on 2017/7/19.
 */
public enum RandomCodeType {

    RANDOM_CODE_FORGET(1, SessionKeys.RANDOM_CODE_FORGET,EmailTemplate.FORGET_PASSWORD)
    ;

    private Integer type;

    private String code;

    private EmailTemplate emailTemplate;

    private static Map<Integer ,RandomCodeType> map = new HashMap<>();

    static {
        RandomCodeType[] values = RandomCodeType.values();

        for (RandomCodeType type : values) {
            map.put(type.getType(),type);
        }
    }

    RandomCodeType(Integer type, String code, EmailTemplate emailTemplate){
        this.type = type;
        this.code = code;
        this.emailTemplate = emailTemplate;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public EmailTemplate getEmailTemplate() {
        return emailTemplate;
    }

    public void setEmailTemplate(EmailTemplate emailTemplate) {
        this.emailTemplate = emailTemplate;
    }

    public static RandomCodeType fromType(Integer type){
        return map.get(type);
    }

    public static boolean contains(Integer type){
        return map.containsKey(type);
    }
}
