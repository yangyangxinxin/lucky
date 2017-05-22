package com.luckysweetheart.exception;

/**
 * Created by yangxin on 2017/5/22.
 */
public class BusinessException extends LuckyException {

    public BusinessException(String message) {
        super(message, "50001");
    }

}
