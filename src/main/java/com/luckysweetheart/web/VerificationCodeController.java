package com.luckysweetheart.web;

import com.luckysweetheart.dal.entity.VerificationCode;
import com.luckysweetheart.service.VerificationCodeService;
import com.luckysweetheart.utils.EmailSender;
import com.luckysweetheart.utils.RandomValidateCode;
import com.luckysweetheart.utils.ResultInfo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Date;

/**
 * Created by yangxin on 2017/7/19.
 */
@RequestMapping("/verification")
@Controller
public class VerificationCodeController extends BaseController {

    @Resource
    private VerificationCodeService verificationCodeService;

    @RequestMapping("/sendCode")
    @ResponseBody
    public ResultInfo<String> sendCode(String email, Integer type) {
        ResultInfo<String> resultInfo = new ResultInfo<>();
        try {
            if (!RandomCodeType.contains(type)) {
                return resultInfo.fail("类型错误");
            }
            String code = RandomValidateCode.getRandomString();
            VerificationCode verificationCode = new VerificationCode();
            verificationCode.setCode(code);
            verificationCode.setMobileOrEmail(email);
            verificationCode.setCreateTime(new Date());
            verificationCode.setExpire(VerificationCode.expireTime);
            Long pk = verificationCodeService.save(verificationCode);
            if (pk != null) {
                EmailSender.init().param("code", code).to(email).emailTemplate(RandomCodeType.fromType(type).getEmailTemplate()).subject("【邮箱验证】").send();
                return resultInfo.success(code);
            }
            return resultInfo.fail("发送失败");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return resultInfo.fail(e.getMessage());
        }
    }

}
