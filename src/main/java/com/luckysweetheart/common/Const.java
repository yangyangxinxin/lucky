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

    /**
     * 上传文件大小不能超过50MB
     */
    Integer MAX_UPLOAD_FILE_SIZE = 50 * 1024 * 1024;

    /**
     * 上传文件大小不能超过50MB
     */
    String MAX_UPLOAD_FILE_SIZE_FORMAT = MAX_UPLOAD_FILE_SIZE / 1024 / 1024 + "MB";

    public static final Integer ACTIVE_STATUS_NO = 0;

    public static final Integer ACTIVE_STATUS_YES = 1;

}
