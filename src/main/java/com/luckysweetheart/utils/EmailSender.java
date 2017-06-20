package com.luckysweetheart.utils;

import com.luckysweetheart.service.EmailService;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.*;

/**
 * Created by yangxin on 2017/6/16.
 */
public class EmailSender implements Serializable {

    private EmailTemplate emailTemplate;

    private String subject;

    private List<String> sendTo;

    private Map<String, Object> param;

    private EmailService emailService;

    // 为了高大上
    private EmailSender() {

    }

    public static EmailSender init() {
        return new EmailSender();
    }

    public EmailSender to(List<String> to) {
        this.sendTo = to;
        return this;
    }

    public EmailSender to(String to) {
        if (sendTo == null) {
            sendTo = new ArrayList<>();
        }
        sendTo.add(to);
        return this;
    }

    public EmailSender to(String... to) {
        if (sendTo == null) {
            sendTo = new ArrayList<>();
        }
        sendTo.addAll(Arrays.asList(to));
        return this;
    }

    public EmailSender subject(String subject) {
        this.subject = subject;
        return this;
    }

    public EmailSender param(Map<String, Object> param) {
        this.param = param;
        return this;
    }

    public EmailSender param(String key, Object value) {
        if (this.param == null) {
            this.param = new HashMap<>();
        }
        param.put(key, value);
        return this;
    }

    public EmailSender emailTemplate(EmailTemplate emailTemplate) {
        this.emailTemplate = emailTemplate;
        return this;
    }

    public EmailSender emailService(EmailService emailService){
        this.emailService = emailService;
        return this;
    }


    public EmailTemplate getEmailTemplate() {
        return emailTemplate;
    }

    public String getSubject() {
        return subject;
    }

    public List<String> getSendTo() {
        return sendTo;
    }

    public Map<String, Object> getParam() {
        return param;
    }

    public String getSendToList() {
        if (this.sendTo == null || this.sendTo.size() == 0) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (String aSendTo : this.sendTo) {
            sb.append(aSendTo).append(";");
        }
        return sb.toString();
    }

    public String[] getSendToArray() {
        if (this.sendTo == null || this.sendTo.size() == 0) {
            return null;
        }
        try {
            String[] arr = new String[this.sendTo.size()];
            for (int i = 0, length = this.sendTo.size(); i < length; i++) {
                arr[i] = this.getSendTo().get(i);
            }
            return arr;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public synchronized EmailSender send(final EmailService emailService) {
        final EmailSender sender = this;
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                emailService.sendEmailTemplate(sender);
            }
        });
        t.setName("邮件发送线程");
        t.start();
        try {
            Thread.sleep(60000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return this;
    }

    private synchronized void initService() {
        if (this.emailService == null) {
            this.emailService = SpringUtil.getBean(EmailService.class);
        }
    }

    public synchronized EmailSender send() {
        initService();
        return this.send(emailService);
    }
}
