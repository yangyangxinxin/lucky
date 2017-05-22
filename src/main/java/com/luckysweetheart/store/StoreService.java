package com.luckysweetheart.store;

import com.luckysweetheart.utils.ResultInfo;
import com.luckysweetheart.vo.StoreDataDTO;
import com.qcloud.cos.COSClient;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by yangxin on 2017/5/22.
 */
@Service
public class StoreService {

    @Resource
    private COSClient cosClient;

    public ResultInfo<StoreDataDTO> uploadFile(String filePath){
        return null;
    }

}
