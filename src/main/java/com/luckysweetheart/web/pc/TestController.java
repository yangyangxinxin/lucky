package com.luckysweetheart.web.pc;

import com.luckysweetheart.service.EmailService;
import com.luckysweetheart.utils.EmailSender;
import com.luckysweetheart.utils.EmailTemplate;
import com.luckysweetheart.web.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * Created by yangxin on 2017/6/17.
 */
@Controller
@RequestMapping("/test")
public class TestController extends BaseController{

    @Resource
    private EmailService emailService;

    @RequestMapping("/sendEmail")
    @ResponseBody
    public Object sendEmail(){
        final EmailSender emailSender = EmailSender.init().emailTemplate(EmailTemplate.FORGET_PASSWORD).to("848135512@qq.com").to("354394024@qq.com").param("basePath","http://www.luckysweetheart.dev").subject("忘记密码");
        new Thread(new Runnable() {
            @Override
            public void run() {
                emailService.sendEmailTemplate(emailSender);
            }
        }).start();
        System.out.println("执行完毕");
        return "发送成功";
    }

    public Object testJsonp(){
        return null;
    }

}
