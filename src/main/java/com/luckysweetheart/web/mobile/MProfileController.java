package com.luckysweetheart.web.mobile;

import com.luckysweetheart.dto.UserDTO;
import com.luckysweetheart.service.UserService;
import com.luckysweetheart.web.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

/**
 * Created by yangxin on 2017/7/19.
 */
@Controller
@RequestMapping("/m/profile")
public class MProfileController extends BaseController {

    @Resource
    private UserService userService;

    @RequestMapping("/index")
    public String index() {
        Long userId = getLoginUserId();
        UserDTO userDTO = userService.findById(userId);
        setAttribute("user", userDTO);
        return "/mobile/profile/index";
    }

}
