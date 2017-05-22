package com.luckysweetheart.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by yangxin on 2017/5/22.
 */
@Transactional(rollbackFor = Exception.class)
@Service
public class BaseService {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

}
