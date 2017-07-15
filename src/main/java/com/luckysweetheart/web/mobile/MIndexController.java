package com.luckysweetheart.web.mobile;

import com.luckysweetheart.web.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by yangxin on 2017/7/15.
 */
@Controller
@RequestMapping("/m/")
public class MIndexController extends BaseController {

    @RequestMapping({"/","/index"})
    public String index(){
        return "/mobile/index";
    }

}
