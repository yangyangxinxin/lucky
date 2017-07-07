package com.luckysweetheart.service;

import com.luckysweetheart.common.RedisKey;
import com.luckysweetheart.dal.dao.UnLoginUrlDao;
import com.luckysweetheart.dal.entity.UnLoginUrl;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by yangxin on 2017/6/22.
 */
@Service
public class UnLoginUrlService extends ParameterizedBaseService<UnLoginUrl, Long> {

    @Resource
    private UnLoginUrlDao unLoginUrlDao;

    @Resource
    private ListOperations<String, String> listOperations;

    private static final String key = RedisKey.REDIS_UNLOGIN_URL_KEY;

    public void setUnLoginUrl(String url){
        UnLoginUrl unLoginUrl = new UnLoginUrl();
        unLoginUrl.setUrl(url);
        unLoginUrl.setCreateTime(new Date());
        unLoginUrl.setUpdateTime(new Date());
        save(unLoginUrl);
        listOperations.leftPush(key, url);
        // 5分钟有效期
        listOperations.getOperations().expire(key, 5, TimeUnit.MINUTES);
    }

    /**
     * <p>获取不需要登陆的URL，优先从Redis中取，取不到从mysql中取，然后再放到redis中，并设置该集合的过期时间为5分钟。</p>
     * <p>redis list集合的最大长度为2<sup>31</sup>-1.即Integer.MAX_VALUE</p>
     * <p><code>ttl yourKey</code>来获取键到期的剩余时间(秒)。返回值：<p>
     * -1, key没有到期超时。
     * -2, key不存在。
     * </p>
     * <p>
     */
    public List<String> queryUnLoginUrl() {
        List<String> list = listOperations.range(key, 0, Integer.MAX_VALUE);
        if (list == null || list.size() == 0) {
            list = unLoginUrlDao.queryUnLoginUrl();
            try {
                for (String aList : list) {
                    listOperations.leftPush(key, aList);
                }
                // 5分钟有效期
                listOperations.getOperations().expire(key, 5, TimeUnit.MINUTES);
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }
        return list;
    }

}
