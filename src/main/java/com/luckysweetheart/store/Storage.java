package com.luckysweetheart.store;

import org.springframework.beans.factory.annotation.Value;

/**
 * 存储门面
 * Created by yangxin on 2017/8/10.
 */
public class Storage {

    @Value("${storage.strategy}")
    private String storageStrategy;

    private StorageApi storageApi;

    /**
     * 初始化存储服务，使用腾讯COS还是阿里OSS
     */
    private synchronized void initStoreService() {
        if (null != storageApi) {
            return;
        }
        // 实例化存储服务
        storageApi = StoreServiceFactory.createStorageService(storageStrategy);
    }

    public StorageApi getStorageService() {
        if (null != storageApi) {
            return storageApi;
        } else {
            initStoreService();
        }
        return storageApi;
    }

}
