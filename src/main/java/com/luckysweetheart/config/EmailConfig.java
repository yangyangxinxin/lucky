package com.luckysweetheart.config;

import com.luckysweetheart.utils.DateUtil;
import com.luckysweetheart.utils.EnvironmentUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import javax.mail.Authenticator;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import java.util.Properties;

/**
 * Created by yangxin on 2017/6/20.
 */
@Configuration
public class EmailConfig {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${spring.mail.username}")
    private String account;//登录用户名

    @Value("${spring.mail.password}")
    private String pass;        //登录密码

    @Value("${spring.mail.username}")
    private String from;        //发件地址

    @Value("${spring.mail.host}")
    private String host;        //服务器地址

    @Value("${spring.mail.port}")
    private int port;        //端口

    @Value("${spring.mail.protocol}")
    private String protocol; //协议

    @Value("${spring.profiles.active}")
    private String active;

    @Bean
    public JavaMailSender getSender() {
        logger.info("初始化邮件配置 at {}", DateUtil.formatNow());
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setUsername(from);
        javaMailSender.setHost(host);
        javaMailSender.setPort(port);//①
        javaMailSender.setDefaultEncoding("UTF-8");
        Properties props = new Properties();//②
        props.setProperty("mail.smtp.host", host);
        props.setProperty("mail.smtp.auth", "true");
        javax.mail.Session session = javax.mail.Session.getDefaultInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, pass);
            }
        });
        javaMailSender.setSession(session);//③
        if (!StringUtils.equals("dev", active)) {
            try {
                logger.info("测试邮件连接 at {} ", DateUtil.formatNow());
                javaMailSender.testConnection();
                logger.info("邮件连接正常 at {} ", DateUtil.formatNow());

            } catch (MessagingException e) {
                logger.error(e.getMessage(), e);
            }
        }
        logger.info("邮件配置初始化完成 at {}", DateUtil.formatNow());
        return javaMailSender;
    }

}
