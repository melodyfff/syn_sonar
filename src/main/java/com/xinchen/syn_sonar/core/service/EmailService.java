package com.xinchen.syn_sonar.core.service;

/**
 * @author Xin Chen (xinchenmelody@gmail.com)
 * @version 1.0
 * @date Created In 2018/8/30 19:26
 */
public interface EmailService {
    /**
     * 发送简单邮件
     *
     * @param to 发送给
     * @param subject 主题
     * @param content 内容
     */
    void sendSimpleEmail(String to, String subject, String content);

    /**
     * 发送html格式邮件
     *
     * @param to 发送给
     * @param cc 抄送
     * @param subject 主题
     * @param templateName 使用的模板名
     */
    void sendHtmlEmail(String[] to, String[] cc, String subject, String templateName);
}
