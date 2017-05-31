package com.luckysweetheart.web.pc;

import com.luckysweetheart.web.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * Created by yangxin on 2017/5/22.
 */
@Controller
@RequestMapping("/")
public class IndexController extends BaseController{

    @RequestMapping({"/index","/"})
    public String index(){
        return "index";
    }

}
