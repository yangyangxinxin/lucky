package com.luckysweetheart.common;

/**
 * Redis Key 常量
 * Created by yangxin on 2017/6/22.
 */
public interface RedisKey {

    /**
     * 记录不需要登录的URL的redis key
     */
    String REDIS_UNLOGIN_URL_KEY = "lucky_unlogin_url_list";

    /**
     * 记录当前在线用户的redis key
     */
    String REDIS_ONLINE_USER_COUNT_KEY = "lucky_web_online_count";

}
