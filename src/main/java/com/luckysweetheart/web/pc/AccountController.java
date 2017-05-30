package com.luckysweetheart.web.pc;

import com.luckysweetheart.dto.UserDTO;
import com.luckysweetheart.service.UserService;
import com.luckysweetheart.utils.ResultInfo;
import com.luckysweetheart.web.BaseController;
import com.luckysweetheart.web.utils.SessionUtils;
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

    /**
     * 登录页
     * @return
     */
    @RequestMapping("/loginPage")
    public String loginPage() {
        //throw new RuntimeException("error!");
        return "/account/loginPage";
    }

    /**
     * 登录动作
     * @param mobile
     * @param password
     * @return
     */
    @RequestMapping("/doLogin")
    @ResponseBody
    public ResultInfo<UserDTO> doLogin(String mobile, String password) {
        ResultInfo<UserDTO> resultInfo = new ResultInfo<>();
        try {
            resultInfo = userService.login(mobile, password);
            if (resultInfo.isSuccess()) {
                // 在session中写key
                SessionUtils.login(request, resultInfo.getData());
            }
            return resultInfo;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return resultInfo.fail(e.getMessage());
        }
    }

    /**
     * 注册页面
     * @return
     */
    @RequestMapping("/registerPage")
    public String registerPage() {
        return "/account/registerPage";
    }

    /**
     * 注册用户
     * @param userDTO
     * @return
     */
    @RequestMapping("/doRegister")
    @ResponseBody
    public ResultInfo<UserDTO> doRegister(UserDTO userDTO) {
        try {
            return userService.registerUser(userDTO);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResultInfo<UserDTO>().fail(e.getMessage());
        }
    }

    /**
     * 登出操作
     * @return
     */
    @RequestMapping("/logout")
    public String logout() {
        SessionUtils.logout(request);
        return "/account/loginPage";
    }

}
