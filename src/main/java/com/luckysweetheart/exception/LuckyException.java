package com.luckysweetheart.exception;

/**
 * Created by yangxin on 2017/5/22.
 */
public abstract class LuckyException extends Exception {

    private String errcode;

    public LuckyException(){

    }

    public LuckyException(String message, String errcode) {
        super(message);
        this.errcode = errcode;
    }

    public LuckyException(Throwable cause) {
        super(cause);
    }

    public LuckyException(String message) {
        super(message);
    }

    public LuckyException(String message, Throwable cause) {
        super(message, cause);
    }

    public String getErrcode() {
        return this.errcode;
    }

    public void setErrcode(String errcode) {
        this.errcode = errcode;
    }

}
