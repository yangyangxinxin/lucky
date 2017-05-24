package com.luckysweetheart.web.pc;

import com.luckysweetheart.dto.UserDTO;
import com.luckysweetheart.service.UserService;
import com.luckysweetheart.utils.ResultInfo;
import com.luckysweetheart.web.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * Created by yangxin on 2017/5/23.
 */
@Controller
@RequestMapping("/account")
public class AccountController extends BaseController {

    @Resource
    private UserService userService;

    @RequestMapping("/loginPage")
    public String loginPage() {
        throw new RuntimeException("error!");
        //return "loginPage";
    }

    @RequestMapping("/doLogin")
    @ResponseBody
    public ResultInfo<Void> doLogin(String mobile, String password) {
        ResultInfo<Void> resultInfo = new ResultInfo<>();
        try {
            resultInfo = userService.login(mobile, password);
            return resultInfo;
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return resultInfo.fail(e.getMessage());
        }
    }

    @RequestMapping("/registerPage")
    public String registerPage(){
        return "/account/registerPage";
    }

    @RequestMapping("/doRegister")
    @ResponseBody
    public ResultInfo<UserDTO> doRegister(UserDTO userDTO){
        try {
            return userService.registerUser(userDTO);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return new ResultInfo<UserDTO>().fail(e.getMessage());
        }
    }

}
