package com.luckysweetheart.service;

import com.luckysweetheart.dal.entity.EmailSnapshoot;
import com.luckysweetheart.dto.EmailSnapshootDTO;
import com.luckysweetheart.exception.BusinessException;
import com.luckysweetheart.utils.DateUtil;
import com.luckysweetheart.utils.EmailSender;
import freemarker.template.Template;
import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Map;

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

    @Resource
    private FreeMarkerConfigurer freeMarkerConfigurer;

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


    public synchronized void sendEmailTemplate(EmailSender emailSender) {
        long start = System.currentTimeMillis();
        logger.info("开始发送邮件 at {}", DateUtil.formatNow());
        MimeMessage message = null;
        EmailSnapshoot emailSnapshoot = null;
        try {

            message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(from);

            helper.setTo(emailSender.getSendToArray());
            helper.setSubject(emailSender.getSubject());

            Map<String, Object> model = emailSender.getParam();

            Template template = freeMarkerConfigurer.getConfiguration().getTemplate(emailSender.getEmailTemplate().getPath());
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
            helper.setText(html, true);

            emailSnapshoot = new EmailSnapshoot();

            emailSnapshoot.setContent(html);
            emailSnapshoot.setCreateTime(new Date());
            emailSnapshoot.setSendFrom(from);
            emailSnapshoot.setStatus(EmailSnapshoot.SEND_ING);
            emailSnapshoot.setSendTo(emailSender.getSendToList());
            emailSnapshoot.setTryTimes(0L);

            super.save(emailSnapshoot);
            javaMailSender.send(message);
            emailSnapshoot.setSuccessDate(new Date());
            emailSnapshoot.setStatus(EmailSnapshoot.SEND_SUCCESS);
            super.update(emailSnapshoot);

            long end = System.currentTimeMillis();
            logger.info("邮件发送成功 ，耗时 {} s", (end - start) / 1000);
        } catch (Exception e) {
            if (emailSnapshoot != null && emailSnapshoot.getEmailId() != null) {
                emailSnapshoot.setStatus(EmailSnapshoot.SEND_FAIL);
                emailSnapshoot.setFailDate(new Date());
                emailSnapshoot.setTryTimes(0L);
                super.update(emailSnapshoot);
                logger.error(e.getMessage(), e);
            } else {
                logger.info("邮件快照未保存成功，此错误信息可能是您传入的参数不正确，请检查传入的参数，例如Subject、content、收件人等信息是否存在。");
                logger.error(e.getMessage(), e);
            }

        }
    }


    /**
     * 重发
     */
    public synchronized void repeater(EmailSnapshoot emailSnapshoot) {
        try {
            logger.info("开始重发邮件... at {}", DateUtil.formatNow());
            long begin = System.currentTimeMillis();

            if (emailSnapshoot.getStatus().equals(EmailSnapshoot.SEND_SUCCESS)) {
                throw new BusinessException("该邮件已经成功发送！");
            }

            if (emailSnapshoot.getStatus().equals(EmailSnapshoot.SEND_ING)) {
                throw new BusinessException("该邮件正在发送中！");
            }

            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(from);
            helper.setTo(emailSnapshoot.getSendToArray());
            helper.setSubject(emailSnapshoot.getSubject());
            helper.setText(emailSnapshoot.getContent(), true);
            javaMailSender.send(message);

            emailSnapshoot.setSuccessDate(new Date());
            Long tryTimes = emailSnapshoot.getTryTimes() == null ? 0L : emailSnapshoot.getTryTimes();
            emailSnapshoot.setTryTimes(++tryTimes);
            emailSnapshoot.setStatus(EmailSnapshoot.SEND_SUCCESS);
            super.update(emailSnapshoot);

            long end = System.currentTimeMillis();
            logger.info("邮件重发成功... at {} , 耗时 {}", DateUtil.formatNow(), (end - begin) / 1000);
        } catch (BusinessException e) {
            logger.error(e.getMessage(), e);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            emailSnapshoot.setFailDate(new Date());
            Long tryTimes = emailSnapshoot.getTryTimes() == null ? 0L : emailSnapshoot.getTryTimes();
            emailSnapshoot.setTryTimes(++tryTimes);
            emailSnapshoot.setStatus(EmailSnapshoot.SEND_FAIL);
            super.update(emailSnapshoot);
        }
    }

}
