package com.luckysweetheart.scan;

import com.luckysweetheart.service.ViolationRecordService;
import com.luckysweetheart.utils.DateUtil;
import com.luckysweetheart.utils.EmailSender;
import com.luckysweetheart.utils.EmailTemplate;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;

/**
 * Created by yangxin on 2017/8/24.
 */
@Configuration
@EnableScheduling
public class ViolationCountScan {

    @Resource
    private ViolationRecordService violationRecordService;

    /**
     * 每周6凌晨1点统计次数
     */
    @Scheduled(cron = "0 0 1 ? * SAT")
    public void doCount() {
        Long totalCount = violationRecordService.getTotalCount();
        int rest = (int) (120 - totalCount);
        EmailSender.init().subject("违章识别剩余次数提醒").to("981987024@qq.com").emailTemplate(EmailTemplate.VIOLATION_COUNT).param("count", totalCount).param("rest", rest).param("time", DateUtil.formatNow()).send();
    }

}
