package com.luckysweetheart.web.pc;

import com.luckysweetheart.store.StoreService;
import com.luckysweetheart.utils.FileUtil;
import com.luckysweetheart.utils.ResultInfo;
import com.luckysweetheart.web.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * Created by yangxin on 2017/5/25.
 */
@RequestMapping("/photo")
@Controller
public class PhotoController extends BaseController {

    @Resource
    private StoreService storeService;

    @RequestMapping("/uploadPage")
    public String uploadPage(){
        return "/photo/uploadPage";
    }

    @RequestMapping(value = "/doUpload",method = RequestMethod.POST)
    @ResponseBody
    public Object upload(MultipartFile file) {
        try {
            byte[] bytes = file.getBytes();
            String suffix = FileUtil.getExtension(file.getOriginalFilename());
            return storeService.uploadFile(bytes, suffix);
        } catch (IOException e) {
            logger.error(e.getMessage());
            return new ResultInfo<>().fail(e.getMessage());
        }
    }

}
