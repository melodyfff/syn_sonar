package com.xinchen.syn_sonar.core.service.impl;

import com.google.common.primitives.Booleans;
import com.xinchen.syn_sonar.core.entity.BaseProfile;
import com.xinchen.syn_sonar.core.model.MailModel;
import com.xinchen.syn_sonar.core.repository.BaseProfileRepository;
import com.xinchen.syn_sonar.core.service.EmailService;
import com.xinchen.syn_sonar.sync.entity.SonarSyncResult;
import com.xinchen.syn_sonar.sync.service.SonarSyncResultService;
import freemarker.core.ParseException;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;
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
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 发送邮件接口
 *
 * @author Xin Chen (xinchenmelody@gmail.com)
 * @version 1.0
 * @date Created In 2018/8/30 19:27
 */
@Service
public class EmailServiceImpl implements EmailService {

    /**
     * 查询差异结果接口
     **/
    @Resource
    private SonarSyncResultService sonarSyncResultService;

    /**
     * 需要进行同步的规则名称
     **/
    @Resource
    private BaseProfileRepository baseProfileRepository;

    /**
     * 日志
     */
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Resource
    private JavaMailSender mailSender;

    @Resource
    private FreeMarkerConfigurer configurer;

    @Value("${mail.fromMail.addr}")
    private String from;

    @Override
    public void sendSimpleEmail(String to, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        // 设置发送人
        message.setFrom(from);
        // 设置收件人
        message.setTo(to);
        //同时发送给多人
        //String[] adds = {"xxx@qq.com","yyy@qq.com"};
        //message.setTo(adds);

        // 设置主题
        message.setSubject(subject);
        //设置内容
        message.setText(content);
        try {
            // 执行发送邮件
            mailSender.send(message);
            LOGGER.info("邮件已经发送到: [{}] , 主题为: [{}]", to, subject);
        } catch (Exception e) {
            LOGGER.error("发送邮件: [{}] , 主题为: [{}] 时发生异常！- error: {}", to, subject, e);
        }
    }

    @Override
    public void sendHtmlEmail(String[] to, String[] cc, String subject, String templateName) {
        //创建一个MINE消息
        MimeMessage message = mailSender.createMimeMessage();
        //true表示需要创建一个multipart message
        MimeMessageHelper helper;
        try {
            helper = new MimeMessageHelper(message, true);
            helper.setFrom(from);
            // 发送给
            helper.setTo(to);
            // 抄送
            if (null != cc){helper.setCc(cc);}
            // 主题
            helper.setSubject(subject);

            // 构建返回对象
            Map<String, Object> model = new HashMap<>(16);
            List<MailModel> list = new ArrayList(10);
            List<BaseProfile> baseProfiles = baseProfileRepository.findAll();
            for (BaseProfile baseProfile : baseProfiles) {
                final List<SonarSyncResult> results = sonarSyncResultService.findAllByLanguage(baseProfile.getProfiles());
                MailModel mailModel = new MailModel();
                mailModel.setRuleName(baseProfile.getProfiles());
                mailModel.setResult(results.isEmpty() ? "一致" : "不一致");
                mailModel.setNumber(results.size());
                list.add(mailModel);
            }
            model.put("mailModel", list);

            Template template = configurer.getConfiguration().getTemplate(templateName);
            String content = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
            helper.setText(content, true);
            mailSender.send(message);
            LOGGER.info("邮件已经发送到: [{}] , 主题为: [{}]", to, subject);
        } catch (MessagingException | IOException | TemplateException e) {
            LOGGER.error("发送邮件: [{}] , 主题为: [{}] 时发生异常！- error: {}", to, subject, e);
        }


    }
}
