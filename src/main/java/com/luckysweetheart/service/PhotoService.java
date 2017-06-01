package com.luckysweetheart.service;

import com.luckysweetheart.common.Const;
import com.luckysweetheart.dal.dao.PhotoDao;
import com.luckysweetheart.dal.entity.Photo;
import com.luckysweetheart.dto.PhotoDTO;
import com.luckysweetheart.exception.BusinessException;
import com.luckysweetheart.store.StorageGroupService;
import com.luckysweetheart.store.StoreService;
import com.luckysweetheart.utils.BeanCopierUtils;
import com.luckysweetheart.utils.FileUtil;
import com.luckysweetheart.utils.ResultInfo;
import com.luckysweetheart.dto.StoreDataDTO;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Date;

/**
 * Created by yangxin on 2017/5/26.
 */
@Service
public class PhotoService extends BaseService {

    @Resource
    private StoreService storeService;

    @Resource
    private StorageGroupService storageGroupService;

    @Resource
    private PhotoDao photoDao;

    public ResultInfo<Long> create(PhotoDTO photoDTO) throws BusinessException {
        ResultInfo<Long> resultInfo = new ResultInfo<>();
        try {
            Assert.notNull(photoDTO, "相片对象不能为空");
            Photo photo = new Photo();
            BeanCopierUtils.copy(photoDTO, photo);
            Photo photo1 = photoDao.save(photo);
            if (photoDTO.getIsDirectory().equals(Const.NO_DIRECTORY)) { // 是目录直接保存，不是目录，存储图片
                ResultInfo<StoreDataDTO> result = storeService.uploadFile(photoDTO.getBytes(), photoDTO.getSuffix(), storageGroupService.getPhotoGroupName());
                if (result.isSuccess()) {
                    photo1.setResourcePath(result.getData().getResourcePath());
                } else {
                    throw new BusinessException("文件上传出现异常");
                }
            } else {
                // todo 将文件存储id设置为文件夹样式
            }
            return resultInfo.success(photo1.getPhotoId());
        } catch (Exception e) {
            throw new BusinessException("保存相册出现错误");
        }
    }

    public ResultInfo<Long> create(MultipartFile file,Long userId) throws IOException, BusinessException {
        byte[] bytes = file.getBytes();
        String suffix = FileUtil.getExtension(file.getOriginalFilename());

        PhotoDTO photoDTO = new PhotoDTO();
        photoDTO.setBytes(bytes);
        photoDTO.setSuffix(suffix);
        photoDTO.setCreateTime(new Date());
        photoDTO.setName(file.getName());
        photoDTO.setParentId(0L);
        photoDTO.setUserId(userId);
        photoDTO.setDeleteStatus(Const.DELETE_STATUS_NO);
        photoDTO.setIsDirectory(Const.NO_DIRECTORY);

        return create(photoDTO);
    }

    public ResultInfo<Void> delete(Long photoId, Long userId) throws BusinessException {
        ResultInfo<Void> resultInfo = new ResultInfo<>();
        try {
            Assert.notNull(photoId, "要删除的id不能为空");
            Assert.notNull(userId, "删除人不能为空");
            Photo photo = photoDao.findOne(photoId);
            if (photo != null) {
                if (photo.getUserId().equals(userId)) {
                    photo.setDeleteStatus(Const.DELETE_STATUS_YES);
                    ResultInfo<Void> result = storeService.deleteFile(photo.getResourcePath());
                    if (result.isSuccess()) {
                        return resultInfo.success();
                    } else {
                        throw new BusinessException(result.getMsg());
                    }
                } else {
                    throw new BusinessException("该相片不属于你");
                }
            }
            throw new BusinessException("该相片不存在！");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage());
        }
    }

    public ResultInfo<PhotoDTO> detail(Long photoId) throws BusinessException {
        ResultInfo<PhotoDTO> re = new ResultInfo<>();
        try {
            notNull(photoId, "id不能为空");
            Photo photo = photoDao.findOne(photoId);
            if (photo != null) {
                PhotoDTO photoDTO = new PhotoDTO();
                BeanCopierUtils.copy(photo, photoDTO);
                photoDTO.setHttpUrl(storeService.getHttpUrlByResourcePath(photoDTO.getResourcePath()));
                return re.success(photoDTO);
            }
            return re.fail("该相片不存在！");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage());
        }
    }

}
