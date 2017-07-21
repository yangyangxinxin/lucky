package com.luckysweetheart.web.mobile;

import com.luckysweetheart.common.Const;
import com.luckysweetheart.dto.UserDTO;
import com.luckysweetheart.service.UserService;
import com.luckysweetheart.utils.ResultInfo;
import com.luckysweetheart.web.BaseController;
import com.luckysweetheart.web.pc.AccountController;
import com.luckysweetheart.web.utils.LoginUtils;
import com.luckysweetheart.web.utils.SessionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * Created by yangxin on 2017/7/15.
 */
@RequestMapping("/m/account")
@Controller
public class MAccountController extends BaseController {

    @Resource
    private UserService userService;

    @RequestMapping("/loginPage")
    public String loginPage() {
        return "/mobile/account/loginPage";
    }

    @RequestMapping("/doLogin")
    @ResponseBody
    public Object doLogin(String mobile, String password) {
        try {
            ResultInfo<UserDTO> resultInfo = userService.login(mobile, password);
            if (resultInfo.isSuccess()) {
                SessionUtils.login(request, resultInfo.getData());
                LoginUtils.writeJWT(resultInfo.getData().getUserId(), response);
            }
            return resultInfo;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return ResultInfo.createFail(e.getMessage());
        }
    }

    @RequestMapping("/registerPage")
    public String registerPage() {
        return "/mobile/account/registerPage";
    }

    @RequestMapping("/doRegister")
    @ResponseBody
    public Object doRegister(String username, String email, String mobilePhone, String password, String password2) {
        try {
            Assert.isTrue(StringUtils.equals(password, password2), "两次密码输入不一致");
            UserDTO userDTO = new UserDTO();
            userDTO.setMobilePhone(mobilePhone);
            userDTO.setActiveStatus(Const.ACTIVE_STATUS_NO);
            userDTO.setEmail(email);
            userDTO.setPassword(password);
            userDTO.setUsername(username);
            return userService.registerUser(userDTO);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return ResultInfo.createFail(e.getMessage());
        }
    }


}
