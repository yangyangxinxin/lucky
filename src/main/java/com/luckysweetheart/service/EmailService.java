package com.luckysweetheart.service;

import com.luckysweetheart.dal.entity.EmailSnapshoot;
import com.luckysweetheart.dto.EmailSnapshootDTO;
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

    public void sendSimleMail(EmailSnapshootDTO emailSnapshootDTO) {

    }

    public void sendEmailTemplate(EmailSender emailSender) {
        long start = System.currentTimeMillis();
        logger.info("开始发送邮件 at {}", DateUtil.formatNow());
        MimeMessage message = null;
        try {
            message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(from);
            String[] tos = new String[emailSender.getSendTo().size()];
            for (int i = 0; i < emailSender.getSendTo().size(); i++) {
                tos[i] = emailSender.getSendTo().get(i);
                logger.info("发送给 {}", tos[i]);
            }
            helper.setTo(tos);
            helper.setSubject(emailSender.getSubject());

            Map<String, Object> model = emailSender.getParam();

            Template template = freeMarkerConfigurer.getConfiguration().getTemplate(emailSender.getEmailTemplate().getPath());
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
            helper.setText(html, true);
            javaMailSender.send(message);
            long end = System.currentTimeMillis();
            logger.info("邮件发送成功 ，耗时 {} s", (end - start) / 1000);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    public void sendTemplete() {
        MimeMessage message = null;
        try {
            message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom("981987024@qq.com");
            helper.setTo("848135512@qq.com");
            helper.setSubject("主题：模板邮件");

            Map<String, Object> model = new HashedMap();
            model.put("username", "zggdczfr");

            //修改 application.properties 文件中的读取路径
//            FreeMarkerConfigurer configurer = new FreeMarkerConfigurer();
//            configurer.setTemplateLoaderPath("classpath:templates");
            //读取 html 模板
            Template template = freeMarkerConfigurer.getConfiguration().getTemplate("/email/test.ftl");
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
            helper.setText(html, true);
            javaMailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
