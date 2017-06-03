package com.luckysweetheart.web.pc;

import com.luckysweetheart.common.Const;
import com.luckysweetheart.dto.PhotoDTO;
import com.luckysweetheart.exception.BusinessException;
import com.luckysweetheart.service.PhotoService;
import com.luckysweetheart.store.StoreService;
import com.luckysweetheart.utils.FileUtil;
import com.luckysweetheart.utils.ResultInfo;
import com.luckysweetheart.web.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;

/**
 * Created by yangxin on 2017/5/25.
 */
@RequestMapping("/photo")
@Controller
public class PhotoController extends BaseController {

    @Resource
    private StoreService storeService;

    @Resource
    private PhotoService photoService;

    @RequestMapping("/uploadPage")
    public String uploadPage() {
        return "/photo/uploadPage";
    }

    @RequestMapping(value = "/doUpload", method = RequestMethod.POST)
    @ResponseBody
    public Object upload(MultipartFile file) {
        try {
            return photoService.create(file, getLoginUserId());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResultInfo<>().fail(e.getMessage());
        }
    }

    @RequestMapping(value = "/doUploadMultipart", method = RequestMethod.POST)
    @ResponseBody
    public Object upload() {
        List<Map<String, Object>> list = new ArrayList<>();
        try {
            List<MultipartFile> files = getMultipartFile("file");
            if (files != null && files.size() > 0) {
                for (MultipartFile file : files) {
                    Map<String, Object> map = new HashMap<>();
                    ResultInfo<Long> resultInfo = photoService.create(file, getLoginUserId());
                    if (resultInfo.isSuccess()) {
                        map.put("success", true);
                        map.put("data", resultInfo.getData());
                    } else {
                        map.put("success", false);
                        map.put("msg", resultInfo.getMsg());
                    }
                    list.add(map);
                }
            }
            return list;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResultInfo<>().fail(e.getMessage());
        }
    }

    @RequestMapping("/detail/{photoId}")
    public String detail(@PathVariable Long photoId) {
        try {
            ResultInfo<PhotoDTO> resultInfo = photoService.detail(photoId);
            if (resultInfo.isSuccess() && resultInfo.getData() != null) {
                setAttribute("photo", resultInfo.getData());
            } else {
                setAttribute("msg", resultInfo.getMsg());
            }
        } catch (BusinessException e) {
            logger.error(e.getMessage(), e);
            setAttribute("msg", e.getMessage());
        }
        return "/photo/detail";
    }

}
