package com.xinchen.syn_sonar.core.service.impl;

import com.xinchen.syn_sonar.SynSonarApplicationTests;
import com.xinchen.syn_sonar.core.service.EmailService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;

import javax.annotation.Resource;

import static org.junit.Assert.*;

/**
 * @author Xin Chen (xinchenmelody@gmail.com)
 * @version 1.0
 * @date Created In 2018/8/30 20:01
 */
public class EmailServiceImplTest extends SynSonarApplicationTests {

    @Autowired
    private EmailService emailService;

    @Test
    public void sendSimpleEmail() {
        emailService.sendSimpleEmail("307208327@qq.com","test","test");
    }

    @Test
    public void sendHtmlEmail() {
        emailService.sendHtmlEmail(new String[]{"307208327@qq.com"},null,"你好中国","mail.ftl");
    }
}