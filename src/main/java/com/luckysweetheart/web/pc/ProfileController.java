package com.luckysweetheart.web.pc;

import com.luckysweetheart.dto.UserDTO;
import com.luckysweetheart.service.EmailService;
import com.luckysweetheart.service.UserService;
import com.luckysweetheart.store.StoreService;
import com.luckysweetheart.utils.*;
import com.luckysweetheart.web.BaseController;
import com.luckysweetheart.web.utils.DomainUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
    private StoreService storeService;

    @Resource
    private EmailService emailService;

    /**
     * 账户设置首页
     *
     * @return
     */
    @RequestMapping("/index")
    public String profileIndex() {
        UserDTO userDTO = userService.findById(getLoginUserId());
        if (userDTO != null) {
            userDTO.setHttpUrl(storeService.getHttpUrlByResourcePath(userDTO.getImgPath()));
            setAttribute("user", userDTO);
        } else {
            setAttribute("msg", "用户不存在。");
        }
        return "/profile/index";
    }

    @RequestMapping("/forgetPasswordPage")
    public String forgetPasswordPage() {
        return "/profile/forget_password";
    }

    @RequestMapping("/forgetPwdSendEmail")
    @ResponseBody
    public ResultInfo<String> forgetPwdSendEmail(String email) {
        ResultInfo<String> resultInfo = new ResultInfo<>();
        try {
            Assert.isTrue(ValidateUtil.email(email), "请输入合法的电子邮件");
            UserDTO userDTO = userService.findByMobilePhoneOrEmail(email);
            if (userDTO != null) {
                String url = DomainUtils.getIndexUrl() + "/profile/resetPassword?param=" + AesUtil.encrypt(email);
                final EmailSender emailSender = EmailSender.init().subject("忘记密码_重置密码").param("basePath", url).to(email).emailTemplate(EmailTemplate.FORGET_PASSWORD);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        emailService.sendEmailTemplate(emailSender);
                    }
                }).start();
                return resultInfo.success(email);
            }
            return resultInfo.fail("该邮件还没有注册，请检查无误再试。");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return resultInfo.fail(e.getMessage());
        }
    }

    @RequestMapping("/resetPasswordPage")
    public String resetPassword(String param) {
        setAttribute("param", param);
        setSeesionAttribute("forgetEmail", AesUtil.decrypt(param));
        return "/profile/resetPassword";
    }

    @RequestMapping("/resetPassword")
    @ResponseBody
    public ResultInfo<UserDTO> resetPassword(String password, String secPwd) {
        ResultInfo<UserDTO> resultInfo = new ResultInfo<>();
        try {
            if (StringUtils.isNotBlank(password) && StringUtils.isNotBlank(secPwd)) {
                if (password.equals(secPwd)) {
                    Object forgetEmail = getSessionAttribute("forgetEmail");
                    if (forgetEmail != null) {
                        String email = forgetEmail.toString();
                        UserDTO userDTO = userService.findByMobilePhoneOrEmail(email);
                        if (userDTO != null) {
                            return userService.resetPwd(userDTO.getUserId(), password);
                        }
                        return resultInfo.fail("该用户不存在");
                    }
                    return resultInfo.fail("验证失败");
                }
                return resultInfo.fail("两次输入的密码不一致");
            }
            return resultInfo.fail();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return resultInfo.fail(e.getMessage());
        }
    }


}
