package com.xinchen.syn_sonar.core.schedul;

import com.xinchen.syn_sonar.core.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 自动检测定时任务
 * @author Xin Chen (xinchenmelody@gmail.com)
 * @version 1.0
 * @date Created In 2018/8/26 16:00
 */
@Component
public class AutoSynTask {
    @Resource
    private EmailService emailService;

    private final static Logger LOGGER = LoggerFactory.getLogger(AutoSynTask.class);

//    @Scheduled(cron="0/30 * * * * ? ")
    // 周五 8:15 执行
    @Scheduled(cron="0 15 8 ? * FRI")
    public void test(){
        LOGGER.info("---------------------");
        LOGGER.info("自动检测定时任务启动");
        LOGGER.info("---------------------");

        // 发送检测邮件
        emailService.sendHtmlEmail(new String[]{"307208327@qq.com"},null,"Sonar规则自动检测","mail.ftl");


        LOGGER.info("---------------------");
        LOGGER.info("自动检测定时任务结束");
        LOGGER.info("---------------------");
    }
}
