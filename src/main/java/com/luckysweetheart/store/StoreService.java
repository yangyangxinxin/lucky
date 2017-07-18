package com.luckysweetheart.store;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.luckysweetheart.common.Const;
import com.luckysweetheart.common.IdWorker;
import com.luckysweetheart.dal.dao.StoreDataDao;
import com.luckysweetheart.dal.entity.StoreData;
import com.luckysweetheart.dto.StoreDataDTO;
import com.luckysweetheart.exception.BusinessException;
import com.luckysweetheart.exception.StorageException;
import com.luckysweetheart.service.ParameterizedBaseService;
import com.luckysweetheart.utils.BeanCopierUtils;
import com.luckysweetheart.utils.DateUtil;
import com.luckysweetheart.utils.ResultInfo;
import com.luckysweetheart.utils.StoreResultUtil;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.request.DelFileRequest;
import com.qcloud.cos.request.GetFileInputStreamRequest;
import com.qcloud.cos.request.GetFileLocalRequest;
import com.qcloud.cos.request.UploadFileRequest;
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
public class StoreService extends ParameterizedBaseService<StoreData,Long> {

    private static final int BUFFER_SIZE = 4096;

    @Resource
    private COSClient cosClient;

    @Resource
    private StoreDataDao storeDataApi;

    @Resource
    private IdWorker idWorker;

    @Resource
    private StorageGroupService storageGroupService;


    /**
     * 获取全局唯一的cosPath
     *
     * @param suffix 文件后缀
     * @return
     */
    public final String getCosPath(String suffix) {
        return "/" + idWorker.nextId() + suffix;
    }

    public ResultInfo<StoreDataDTO> uploadFile(String filePath, String fileName) throws BusinessException {
        String cosPath = "/" + fileName;
        UploadFileRequest uploadFileRequest = new UploadFileRequest(storageGroupService.getDefaultGroupName(), cosPath, filePath);
        return uploadFile(uploadFileRequest, cosPath);
    }

    public ResultInfo<StoreDataDTO> uploadFile(byte[] bytes, String suffix) throws BusinessException {
        String cosPath = getCosPath(suffix);
        UploadFileRequest uploadFileRequest = new UploadFileRequest(storageGroupService.getDefaultGroupName(), cosPath, bytes);
        return uploadFile(uploadFileRequest, cosPath);
    }

    public ResultInfo<StoreDataDTO> uploadFile(byte[] bytes, String suffix, String bucketName) throws BusinessException {
        String cosPath = getCosPath(suffix);
        UploadFileRequest uploadFileRequest = new UploadFileRequest(bucketName, cosPath, bytes);
        return uploadFile(uploadFileRequest, cosPath);
    }

    private String getFileName(String cosPath) {
        return cosPath.substring(1, cosPath.length());
    }

    public ResultInfo<StoreDataDTO> uploadFile(UploadFileRequest uploadFileRequest, String cosPath) throws BusinessException {
        logger.info("调用存储上传文件，bucketName：" + uploadFileRequest.getBucketName() + "，cosPath：" + cosPath + " at " + DateUtil.formatDate(new Date()));
        String uploadFileRet = cosClient.uploadFile(uploadFileRequest);
        logger.info(uploadFileRet);
        ResultInfo<StoreDataDTO> resultInfo = StoreResultUtil.getResult(uploadFileRet);
        if (resultInfo.isSuccess() && resultInfo.getData() != null) {
            StoreDataDTO storeDataDTO = resultInfo.getData();
            storeDataDTO.setCosPath(cosPath);
            storeDataDTO.setFileName(getFileName(cosPath));
            storeDataDTO.setBucketName(uploadFileRequest.getBucketName());
            saveStoreData(storeDataDTO);
            logger.info(cosPath + "文件上传成功。" + " at " + DateUtil.formatDate(new Date()));
        } else {
            logger.info(cosPath + "文件上传失败，失败原因：" + resultInfo.getMsg() + " at " + DateUtil.formatNow());
        }
        return resultInfo;
    }

    public ResultInfo<Void> deleteFile(String resourcePath,String bucketName) throws BusinessException {
        logger.info("调用存储删除文件...");
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
                logger.info("文件删除成功...");
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
    public byte[] download(String cosFilePath, String bucketName) throws Exception {
        logger.info("调用存储下载文件，要下载的文件为：" + cosFilePath + "，bucketName ：" + bucketName + " at " + DateUtil.formatDate(new Date()));
        GetFileInputStreamRequest request = new GetFileInputStreamRequest(bucketName, cosFilePath);
        request.setUseCDN(false);
        InputStream inputStream = cosClient.getFileInputStream(request);
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] data = new byte[BUFFER_SIZE];
        int count;
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
        logger.info("文件下载完成：" + cosFilePath + " at " + DateUtil.formatDate(new Date()));
        return outStream.toByteArray();
    }

    /**
     * 下载文件，默认bucketName为bubu
     *
     * @param cosFilePath
     * @return
     * @throws Exception
     */
    public byte[] download(String cosFilePath) throws Exception {
        return download(cosFilePath, storageGroupService.getDefaultGroupName());
    }


    /**
     * 下载文件到本地
     *
     * @param cosFilePath
     * @param localPathDown
     * @return
     */
    public String downloadLocal(String cosFilePath, String localPathDown) {
        GetFileLocalRequest getFileLocalRequest = new GetFileLocalRequest(storageGroupService.getDefaultGroupName(), cosFilePath, localPathDown);
        getFileLocalRequest.setUseCDN(false);
        getFileLocalRequest.setReferer("http://www.luckysweetheart.com");
        return cosClient.getFileLocal(getFileLocalRequest);
    }

    public String getHttpUrlByResourcePath(String resourcePath) {
        StoreData storeData = storeDataApi.findByResourcePath(resourcePath);
        if (storeData != null) {
            return storeData.getSourceUrl();
        }
        return "";
    }

    public String getHttpUrlByCosPath(String cosPath) {
        StoreData storeData = storeDataApi.findByCosPath(cosPath);
        if (storeData != null) {
            return storeData.getSourceUrl();
        }
        return "";
    }

    public StoreDataDTO getByCosPath(String cosPath) {
        StoreData data = storeDataApi.findByCosPath(cosPath);
        if (data != null) {
            StoreDataDTO storeDataDTO = new StoreDataDTO();
            BeanCopierUtils.copy(data, storeDataDTO);
            return storeDataDTO;
        }
        return null;
    }

    public StoreDataDTO getByResourcePath(String resourcePath) {
        StoreData data = storeDataApi.findByResourcePath(resourcePath);
        if (data != null) {
            StoreDataDTO storeDataDTO = new StoreDataDTO();
            BeanCopierUtils.copy(data, storeDataDTO);
            return storeDataDTO;
        }
        return null;
    }

    /**
     * 数据库记录存储
     *
     * @param storeDataDTO
     * @return
     */
    private StoreData saveStoreData(StoreDataDTO storeDataDTO) throws BusinessException {
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
