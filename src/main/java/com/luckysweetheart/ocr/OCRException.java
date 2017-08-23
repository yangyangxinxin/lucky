package com.luckysweetheart.ocr;

/**
 * OCR文字识别异常，主要是对调用接口后返回的错误信息进行封装。具体错误信息请参考{@link OCRUtil#getErrorMsg(Integer)}
 * Created by yangxin on 2017/8/11.
 */
public class OCRException extends Exception {

    private String code;

    private String msg;

    public OCRException() {
        super();
    }

    public OCRException(String msg) {
        super(msg);
        this.msg = msg;
    }

    public OCRException(String code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
