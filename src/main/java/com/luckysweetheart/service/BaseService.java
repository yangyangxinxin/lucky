package com.luckysweetheart.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * Created by yangxin on 2017/5/22.
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class BaseService {

    /**
     * 处理复杂的sql
     */
    @Autowired
    protected JdbcTemplate jdbcTemplate;

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    protected void notNull(Object obj, String message) {
        Assert.notNull(obj, message);
    }

    protected void isTrue(boolean flag, String message) {
        Assert.isTrue(flag, message);
    }

}
