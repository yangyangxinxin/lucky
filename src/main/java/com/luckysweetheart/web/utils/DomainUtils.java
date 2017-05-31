package com.luckysweetheart.web.utils;

import com.luckysweetheart.utils.SpringUtil;

/**
 * Created by yangxin on 2017/5/31.
 */
public class DomainUtils {

    private static DomainService domainService;

    protected static synchronized void init() {
        if (domainService == null) {
            domainService = SpringUtil.getBean(DomainService.class);
        }
    }

    public static String getIndexUrl() {
        if (domainService == null) {
            init();
        }
        return domainService.getIndexUrl();
    }

}
