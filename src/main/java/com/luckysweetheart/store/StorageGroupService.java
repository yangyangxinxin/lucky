package com.luckysweetheart.store;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * bucketName管理，后续考虑加到数据库中，在管理后台管理。
 * Created by yangxin on 2017/5/26.
 */
@Service
public class StorageGroupService{

    @Value("${defaultGroupName}")
    private String defaultGroupName;

    @Value("${userGroupName}")
    private String userGroupName;

    @Value("${photoGroupName}")
    private String photoGroupName;

    /**
     * 测试的bucketName
     * @return
     */
    public String getDefaultGroupName() {
        return defaultGroupName;
    }

    /**
     * 用于存储用户的头像信息
     * @return
     */
    public String getUserGroupName() {
        return userGroupName;
    }

    /**
     * 用于用户相片存储
     * @return
     */
    public String getPhotoGroupName() {
        return photoGroupName;
    }

}
