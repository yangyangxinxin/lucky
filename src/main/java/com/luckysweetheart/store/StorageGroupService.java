package com.luckysweetheart.store;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * bucketName管理，后续考虑加到数据库中，在管理后台管理。
 * Created by yangxin on 2017/5/26.
 */
@Service
public class StorageGroupService {

    @Value("${storage.strategy}")
    private String storageStrategy;

    @Value("${cos.defaultGroupName}")
    private String cosDefaultGroupName;

    @Value("${cos.userGroupName}")
    private String cosUserGroupName;

    @Value("${cos.photoGroupName}")
    private String cosPhotoGroupName;

    @Value("${oos.defaultGroupName}")
    private String oosDefaultGroupName;

    @Value("${oos.userGroupName}")
    private String oosUserGroupName;

    @Value("${oos.photoGroupName}")
    private String oosPhotoGroupName;


    /**
     * 测试的bucketName
     *
     * @return
     */
    public String getDefaultGroupName() {
        if (storageStrategy.equalsIgnoreCase(StoreServiceFactory.COS)) {
            return cosDefaultGroupName;
        }
        return oosDefaultGroupName;
    }

    /**
     * 用于存储用户的头像信息
     *
     * @return
     */
    public String getUserGroupName() {
        if (storageStrategy.equalsIgnoreCase(StoreServiceFactory.COS)) {
            return cosUserGroupName;
        }
        return oosUserGroupName;
    }

    /**
     * 用于用户相片存储
     *
     * @return
     */
    public String getPhotoGroupName() {
        if (storageStrategy.equalsIgnoreCase(StoreServiceFactory.COS)) {
            return cosPhotoGroupName;
        }
        return oosPhotoGroupName;
    }

}
