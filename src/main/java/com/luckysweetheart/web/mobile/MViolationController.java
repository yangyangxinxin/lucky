package com.luckysweetheart.web.mobile;

import com.luckysweetheart.http.ViolationService;
import com.luckysweetheart.http.response.ViolationResponse;
import com.luckysweetheart.utils.ResultInfo;
import com.luckysweetheart.web.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * Created by yangxin on 2017/8/24.
 */
@RequestMapping("/m/violation")
@Controller
public class MViolationController extends BaseController{

    @Resource
    private ViolationService violationService;

    @RequestMapping({"/","/index"})
    public String index(){
        return "/mobile/violation/index";
    }

    @RequestMapping("/query")
    @ResponseBody
    public Object query(MultipartFile file){
        ResultInfo<ViolationResponse> resultInfo = new ResultInfo<>();
        try{
            if(file == null){
                return resultInfo.fail("请先上传文件！");
            }
            ViolationResponse violation = violationService.getViolation(file.getBytes());
            return resultInfo.success(violation);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return resultInfo.fail(e.getMessage());
        }
    }

}
