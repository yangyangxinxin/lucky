package com.luckysweetheart.web.mobile;

import com.luckysweetheart.utils.ResultInfo;
import com.luckysweetheart.web.BaseController;
import com.luckysweetheart.web.utils.SessionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

/**
 * Created by yangxin on 2017/7/15.
 */
@Controller
@RequestMapping("/m/")
public class MIndexController extends BaseController {

    @RequestMapping({"/", "/index"})
    public String index() throws IOException {
        if (isLogin()) {
            return "/mobile/index";
        } else {
            response.sendRedirect("/m/account/loginPage");
            return null;
        }
    }

    // 0 : 白天 1: 夜间
    @RequestMapping("/setNightShift")
    @ResponseBody
    public Object setNightShift(Integer status) {
        if (status == 0) {
            if(SessionUtils.containsKey("night",request)){
                SessionUtils.removeAttribute(request,"night");
            }
        } else {
            setSessionAttribute("night", true);
        }
        return ResultInfo.create(Integer.class).success(status);
    }

}
