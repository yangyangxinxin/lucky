package com.luckysweetheart.service;

import com.luckysweetheart.store.StorageGroupService;
import com.luckysweetheart.store.StoreService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by yangxin on 2017/5/26.
 */
@Service
public class PhotoService extends BaseService{

    @Resource
    private StoreService storeService;

    @Resource
    private StorageGroupService storageGroupService;


}
