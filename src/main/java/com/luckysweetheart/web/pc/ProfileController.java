package com.luckysweetheart.web.pc;

import com.luckysweetheart.dto.UserDTO;
import com.luckysweetheart.service.UserService;
import com.luckysweetheart.storage.StorageApi;
import com.luckysweetheart.storage.StorageGroupService;
import com.luckysweetheart.web.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

/**
 * 用户账户设置相关
 * Created by yangxin on 2017/5/31.
 */
@Controller
@RequestMapping("/profile")
public class ProfileController extends BaseController {

    @Resource
    private UserService userService;

    @Resource
    private StorageApi storageApi;

    @Resource
    private StorageGroupService storageGroupService;

    /**
     * 账户设置首页
     *
     * @return
     */
    @RequestMapping("/index")
    public String profileIndex() {
        UserDTO userDTO = userService.findById(getLoginUserId());
        try {
            if (userDTO != null) {
                userDTO.setHttpUrl(storageApi.getHttpUrl(userDTO.getImgPath()));
                setAttribute("user", userDTO);
            } else {
                setAttribute("msg", "用户不存在。");
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return "/profile/index";
    }


}
