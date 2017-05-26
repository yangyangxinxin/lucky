package com.luckysweetheart.store;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.luckysweetheart.common.IdWorker;
import com.luckysweetheart.dal.dao.StoreDataDao;
import com.luckysweetheart.dal.entity.StoreData;
import com.luckysweetheart.exception.BusinessException;
import com.luckysweetheart.exception.StorageException;
import com.luckysweetheart.service.BaseService;
import com.luckysweetheart.utils.BeanCopierUtils;
import com.luckysweetheart.utils.ResultInfo;
import com.luckysweetheart.utils.StoreResultUtil;
import com.luckysweetheart.vo.StoreDataDTO;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.exception.AbstractCosException;
import com.qcloud.cos.request.DelFileRequest;
import com.qcloud.cos.request.GetFileInputStreamRequest;
import com.qcloud.cos.request.GetFileLocalRequest;
import com.qcloud.cos.request.UploadFileRequest;
import com.qcloud.cos.sign.Credentials;
import com.qcloud.cos.sign.Sign;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

/**
 * Created by yangxin on 2017/5/22.
 */
@Service
public class StoreService extends BaseService {

    private static final int BUFFER_SIZE = 4096;

    @Resource
    private COSClient cosClient;

    @Resource
    private Credentials cred;

    @Resource
    private StoreDataDao storeDataApi;

    @Resource
    private IdWorker idWorker;

    @Resource
    private StorageGroupService storageGroupService;

    private final String bucketName = "bubu";

    /**
     * 获取全局唯一的cosPath
     *
     * @param suffix 文件后缀
     * @return
     */
    public final String getCosPath(String suffix) {
        return "/" + idWorker.nextId() + suffix;
    }

    public ResultInfo<StoreDataDTO> uploadFile(String filePath, String fileName) {
        String cosPath = "/" + fileName;
        UploadFileRequest uploadFileRequest = new UploadFileRequest(bucketName, cosPath, filePath);
        return uploadFile(uploadFileRequest, cosPath);
    }

    public ResultInfo<StoreDataDTO> uploadFile(byte[] bytes, String suffix) {
        String cosPath = getCosPath(suffix);
        UploadFileRequest uploadFileRequest = new UploadFileRequest(bucketName, cosPath, bytes);
        return uploadFile(uploadFileRequest, cosPath);
    }


    public ResultInfo<StoreDataDTO> uploadFile(UploadFileRequest uploadFileRequest, String cosPath) {
        String uploadFileRet = cosClient.uploadFile(uploadFileRequest);
        logger.info(uploadFileRet);
        ResultInfo<StoreDataDTO> resultInfo = StoreResultUtil.getResult(uploadFileRet);
        if (resultInfo.isSuccess() && resultInfo.getData() != null) {
            StoreDataDTO storeDataDTO = resultInfo.getData();
            storeDataDTO.setCosPath(cosPath);
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
            DelFileRequest delFileRequest = new DelFileRequest(bucketName, storeData.getCosPath());
            String result = cosClient.delFile(delFileRequest);
            logger.info(result);
            JSONObject jsonObject = JSON.parseObject(result);
            Integer code = jsonObject.getInteger("code");
            String message = jsonObject.getString("message");
            if (code == 0) {
                storeData.setDeleteStatus(StoreData.DELETE_STATUS_YES);
                storeData.setUpdateTime(new Date());
                //storeDataApi.update(storeData);
                return resultInfo.success();
            }
            return resultInfo.fail(message).setResultCode(code + "");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage());
        }
    }

    /**
     * 下载文件，返回文件二进制数组
     *
     * @param cosFilePath
     * @return
     * @throws Exception
     */
    public byte[] download(String cosFilePath) throws Exception {
        GetFileInputStreamRequest request = new GetFileInputStreamRequest(bucketName, cosFilePath);
        request.setUseCDN(false);
        request.setReferer("http://www.luckysweetheart.com");
        InputStream inputStream = cosClient.getFileInputStream(request);
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] data = new byte[BUFFER_SIZE];
        int count = -1;
        try {
            while ((count = inputStream.read(data, 0, BUFFER_SIZE)) != -1) {
                outStream.write(data, 0, count);
            }
        } catch (IOException e) {
            logger.error("通过COS下载文件出现IO异常", e);
            throw new StorageException(e);
        } finally {
            outStream.close();
            inputStream.close();
        }
        return outStream.toByteArray();
    }

    /**
     * 下载文件到本地
     *
     * @param cosFilePath
     * @param localPathDown
     * @return
     */
    public String download(String cosFilePath, String localPathDown) {
        GetFileLocalRequest getFileLocalRequest = new GetFileLocalRequest(bucketName, cosFilePath, localPathDown);
        getFileLocalRequest.setUseCDN(false);
        getFileLocalRequest.setReferer("http://www.luckysweetheart.com");
        String getFileResult = cosClient.getFileLocal(getFileLocalRequest);
        return getFileResult;
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
