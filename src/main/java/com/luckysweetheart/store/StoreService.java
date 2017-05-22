package com.luckysweetheart.store;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.luckysweetheart.dal.dao.StoreDataDao;
import com.luckysweetheart.dal.entity.StoreData;
import com.luckysweetheart.exception.BusinessException;
import com.luckysweetheart.service.BaseService;
import com.luckysweetheart.utils.BeanCopierUtils;
import com.luckysweetheart.utils.ResultInfo;
import com.luckysweetheart.utils.StoreResultUtil;
import com.luckysweetheart.vo.StoreDataDTO;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.request.DelFileRequest;
import com.qcloud.cos.request.UploadFileRequest;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * Created by yangxin on 2017/5/22.
 */
@Service
public class StoreService extends BaseService {

    @Resource
    private COSClient cosClient;

    @Resource
    private StoreDataDao storeDataApi;

    private final String bucketName = "bubu";

    public ResultInfo<StoreDataDTO> uploadFile(String filePath, String fileName) {
        UploadFileRequest uploadFileRequest = new UploadFileRequest(bucketName, "/" + fileName, filePath);
        String uploadFileRet = cosClient.uploadFile(uploadFileRequest);
        logger.info(uploadFileRet);
        ResultInfo<StoreDataDTO> resultInfo = StoreResultUtil.getResult(uploadFileRet);
        if (resultInfo.isSuccess() && resultInfo.getData() != null) {
            StoreDataDTO storeDataDTO = resultInfo.getData();
            saveStoreData(storeDataDTO);
        }
        return resultInfo;
    }

    public ResultInfo<Void> deleteFile(String resourcePath) {
        ResultInfo<Void> resultInfo = new ResultInfo<>();
        try {
            StoreData storeData = storeDataApi.findByResourcePath(resourcePath);
            if (storeData == null) {
                throw new BusinessException("该数据不存在!");
            }
            DelFileRequest delFileRequest = new DelFileRequest(bucketName, resourcePath);
            String result = cosClient.delFile(delFileRequest);
            logger.info(result);
            JSONObject jsonObject = JSON.parseObject(result);
            Integer code = jsonObject.getInteger("code");
            String message = jsonObject.getString("message");
            if (code == 0) {
                storeData.setDeleteStatus(StoreData.DELETE_STATUS_YES);
                storeData.setUpdateTime(new Date());
                storeDataApi.update(storeData);
                return resultInfo.success();
            }
            return resultInfo.fail(message).setResultCode(code + "");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage());
        }
    }

    /**
     * 数据库记录存储
     *
     * @param storeDataDTO
     * @return
     */
    private StoreData saveStoreData(StoreDataDTO storeDataDTO) {
        if (storeDataDTO == null) {
            throw new BusinessException("存储对象不能为空!");
        }
        StoreData storeData = new StoreData();
        BeanCopierUtils.copy(storeDataDTO, storeData);
        storeData.setCreateTime(new Date());
        storeData.setDeleteStatus(StoreData.DELETE_STATUS_NO);
        storeDataApi.save(storeData);
        return storeData;
    }

}
