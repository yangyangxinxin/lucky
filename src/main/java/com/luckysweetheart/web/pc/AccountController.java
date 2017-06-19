package com.luckysweetheart.web.pc;

import com.luckysweetheart.dto.UserDTO;
import com.luckysweetheart.service.EmailService;
import com.luckysweetheart.service.UserService;
import com.luckysweetheart.utils.*;
import com.luckysweetheart.web.BaseController;
import com.luckysweetheart.web.utils.DomainUtils;
import com.luckysweetheart.web.utils.SessionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
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

    @Resource
    private EmailService emailService;

    /**
     * 登录页
     *
     * @return
     */
    @RequestMapping("/loginPage")
    public String loginPage() {
        if (isLogin()) {
            return "redirect:/index";
        }
        return "/account/loginPage";
    }

    /**
     * 登录动作
     *
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
     *
     * @return
     */
    @RequestMapping("/registerPage")
    public String registerPage() {
        return "/account/registerPage";
    }

    /**
     * 注册用户
     *
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
     *
     * @return
     */
    @RequestMapping("/logout")
    public String logout() {
        SessionUtils.logout(request);
        return "/account/loginPage";
    }

    @RequestMapping("/activeUser")
    public String active(String email, String activeCode, Long timestamp) {
        try {
            ResultInfo<Void> resultInfo = userService.activeUser(email, activeCode, timestamp);
            if (resultInfo.isSuccess()) {
                setAttribute("msg", "已激活成功");
                setAttribute("code", 0);
            } else {
                setAttribute("msg", resultInfo.getMsg());
                setAttribute("code", 1);
            }
        } catch (Exception e) {
            setAttribute("msg", e.getMessage());
            setAttribute("code", 2);
        }
        return "/account/activeUser";
    }

    @RequestMapping("/forgetPasswordPage")
    public String forgetPasswordPage() {
        return "/account/forget_password";
    }

    @RequestMapping("/forgetPwdSendEmail")
    @ResponseBody
    public ResultInfo<String> forgetPwdSendEmail(String email) {
        ResultInfo<String> resultInfo = new ResultInfo<>();
        try {
            Assert.isTrue(ValidateUtil.email(email), "请输入合法的电子邮件");
            UserDTO userDTO = userService.findByMobilePhoneOrEmail(email);
            if (userDTO != null) {
                String url = DomainUtils.getIndexUrl() + "/profile/resetPasswordPage?param=" + AesUtil.encrypt(email);
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
        setSessionAttribute("forgetEmail", AesUtil.decrypt(param));
        return "/account/resetPassword";
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
