package com.luckysweetheart.web.mobile;

import com.luckysweetheart.dto.PhotoDTO;
import com.luckysweetheart.service.PhotoService;
import com.luckysweetheart.utils.ResultInfo;
import com.luckysweetheart.web.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

/**
 * Created by yangxin on 2017/7/19.
 */
@RequestMapping("/m/photo")
@Controller
public class MPhotoController extends BaseController {

    @Resource
    private PhotoService photoService;

    @RequestMapping("/list")
    public String list() {
        return "/mobile/photo/list";
    }

    // AJAX 动态获取数据接口、上传照片、删除照片调用电脑版的接口

    @RequestMapping("/create")
    public String create() {
        return "/mobile/photo/create";
    }

    @RequestMapping("/detail/{photoId}")
    public String detail(@PathVariable("photoId") Long photoId) {
        ResultInfo<PhotoDTO> detail = photoService.detail(photoId);
        if (detail.isSuccess() && detail.getData() != null) {
            setAttribute("photo", detail.getData());
        }
        return "/mobile/photo/detail";
    }
}
