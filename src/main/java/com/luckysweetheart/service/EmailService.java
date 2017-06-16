package com.luckysweetheart.service;

import com.luckysweetheart.dal.entity.EmailSnapshoot;
import com.luckysweetheart.dto.EmailSnapshootDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 邮件服务，可用来找回密码、发送验证码。
 * Created by yangxin on 2017/6/16.
 */
@Service
public class EmailService extends ParameterizedBaseService<EmailSnapshoot, Long> {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String from;

    /**
     * 发送纯文本的简单邮件
     *
     * @param to
     * @param subject
     * @param content
     */
    public void sendSimpleMail(String to, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(content);

        EmailSnapshoot emailSnapshoot = new EmailSnapshoot();
        emailSnapshoot.setContent(content);
        emailSnapshoot.setCreateTime(new Date());
        emailSnapshoot.setSendFrom(from);
        emailSnapshoot.setStatus(EmailSnapshoot.SEND_ING);
        emailSnapshoot.setSendTo(to);
        emailSnapshoot.setTryTimes(0L);

        super.save(emailSnapshoot);

        try {
            javaMailSender.send(message);
            logger.info("简单邮件已经发送。");
            emailSnapshoot.setSuccessDate(new Date());
            emailSnapshoot.setStatus(EmailSnapshoot.SEND_SUCCESS);
            super.update(emailSnapshoot);
        } catch (Exception e) {
            emailSnapshoot.setStatus(EmailSnapshoot.SEND_FAIL);
            emailSnapshoot.setFailDate(new Date());
            super.update(emailSnapshoot);
            logger.error("发送简单邮件时发生异常！", e);
        }
    }

    public void sendSimleMail(EmailSnapshootDTO emailSnapshootDTO) {

    }

}
