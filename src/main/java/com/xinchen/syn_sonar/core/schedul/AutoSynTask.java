package com.xinchen.syn_sonar.core.schedul;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 自动检测定时任务
 * @author Xin Chen (xinchenmelody@gmail.com)
 * @version 1.0
 * @date Created In 2018/8/26 16:00
 */
@Component
public class AutoSynTask {

    private final static Logger LOGGER = LoggerFactory.getLogger(AutoSynTask.class);

    @Scheduled(cron="0/5 * * * * ? ")
    public void test(){
        LOGGER.info("---------------------");
        LOGGER.info("自动检测定时任务启动");
        LOGGER.info("---------------------");
    }
}
