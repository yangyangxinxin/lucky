package com.luckysweetheart.web.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Created by yangxin on 2017/5/31.
 */
@Service
public class DomainService {

    @Value("${index.url}")
    private String indexUrl;

    public String getIndexUrl() {
        return indexUrl;
    }

    public void setIndexUrl(String indexUrl) {
        this.indexUrl = indexUrl;
    }
}
