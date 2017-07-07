package com.luckysweetheart.scan;

import com.luckysweetheart.dal.dao.EmailDao;
import com.luckysweetheart.dal.entity.EmailSnapshoot;
import com.luckysweetheart.service.EmailService;
import com.luckysweetheart.service.UserInfoService;
import com.luckysweetheart.utils.DateUtil;
import com.luckysweetheart.utils.EmailSender;
import com.luckysweetheart.utils.EmailTemplate;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by yangxin on 2017/6/17.
 */
@Configuration
@EnableScheduling
public class EmailScan extends BaseScan{

    @Resource
    private EmailDao emailDao;

    @Resource
    private EmailService emailService;

    @Resource
    private UserInfoService userInfoService;

    //@Scheduled(cron = "0/30 * * * * ?") // 每30秒执行一次
    public void doSend() {
        logger.info("开始扫描发送失败的邮件 at {}" , DateUtil.formatNow());
        List<EmailSnapshoot> list = emailDao.findUnsuccess();
        if (list != null && list.size() > 0) {
            for (EmailSnapshoot emailSnapshoot : list) {
                emailService.repeater(emailSnapshoot);
            }
        }
        logger.info("此阶段暂时没有要处理的数据 at {}" , DateUtil.formatNow());
    }

    private boolean flag = false;

    //@Scheduled(cron = "0/30 * * * * ?")
    public void test(){
        Long count = userInfoService.getTotalCount();
        if(!flag) {
            logger.info("当前项目数：{} at {}",count,DateUtil.formatNow() );
            if (count >= 100000 - 1) {
                this.flag = true;
                EmailSender.init().to("981987024@qq.com").emailTemplate(EmailTemplate.TEST).subject("测试").param("time", DateUtil.formatNow()).param("count",count).send();
            }
        }
    }
}
