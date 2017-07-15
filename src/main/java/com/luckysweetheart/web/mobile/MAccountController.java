package com.luckysweetheart.web.mobile;

import com.luckysweetheart.dto.UserDTO;
import com.luckysweetheart.service.UserService;
import com.luckysweetheart.utils.ResultInfo;
import com.luckysweetheart.web.BaseController;
import com.luckysweetheart.web.pc.AccountController;
import com.luckysweetheart.web.utils.SessionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * Created by yangxin on 2017/7/15.
 */
@RequestMapping("/m/account")
@Controller
public class MAccountController extends BaseController{

    @Resource
    private UserService userService;

    @Resource
    private AccountController accountController;

    @RequestMapping("/loginPage")
    public String loginPage(){
        return "/mobile/account/loginPage";
    }

    @RequestMapping("/doLogin")
    @ResponseBody
    public Object doLogin(String mobile, String password){
        try {
            ResultInfo<UserDTO> resultInfo = userService.login(mobile, password);
            if(resultInfo.isSuccess()){
                SessionUtils.login(request,resultInfo.getData());
            }
            return resultInfo;
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return ResultInfo.createFail(e.getMessage());
        }
    }



}
