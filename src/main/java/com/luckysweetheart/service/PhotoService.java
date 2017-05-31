package com.luckysweetheart.service;

import com.luckysweetheart.common.Const;
import com.luckysweetheart.dal.dao.PhotoDao;
import com.luckysweetheart.dal.entity.Photo;
import com.luckysweetheart.dto.PhotoDTO;
import com.luckysweetheart.exception.BusinessException;
import com.luckysweetheart.store.StorageGroupService;
import com.luckysweetheart.store.StoreService;
import com.luckysweetheart.utils.BeanCopierUtils;
import com.luckysweetheart.utils.ResultInfo;
import com.luckysweetheart.dto.StoreDataDTO;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;

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

}
