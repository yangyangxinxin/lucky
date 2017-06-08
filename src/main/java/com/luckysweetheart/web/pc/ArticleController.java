package com.luckysweetheart.web.pc;

import com.luckysweetheart.web.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by yangxin on 2017/6/8.
 */
@Controller
@RequestMapping("/article")
public class ArticleController extends BaseController{

    @RequestMapping("/create")
    public String create(){
        return "/article/create";
    }

}
