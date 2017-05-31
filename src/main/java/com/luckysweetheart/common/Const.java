package com.luckysweetheart.common;

/**
 * 常量，避免在编写过程中使用未经声明的变量
 * Created by yangxin on 2017/5/31.
 */
public interface Const {

    /**
     * 统一删除状态 0 ：未删除
     */
    int DELETE_STATUS_NO = 0;

    /**
     * 统一删除状态 1 ： 已删除
     */
    int DELETE_STATUS_YES = 1;

    /**
     * 是照片的目录
     */
    int IS_DIRECTORY = 0;

    /**
     * 是普通的照片
     */
    int NO_DIRECTORY = 1;

    /**
     * 默认的bucketName（如果不传）
     */
    String DEFAULT_BUCKET_NAME = "bubu";

    /**
     * 用户默认头像COS地址
     */
    String DEFAULT_USER_IMG = "/1253770331/bubu/defaultUserImg.png";

    /**
     * 用户密码加密盐
     */
    String SALT = "www.luckysweetheart.com";

}
