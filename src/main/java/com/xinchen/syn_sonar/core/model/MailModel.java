package com.xinchen.syn_sonar.core.model;

import lombok.Data;

import java.io.Serializable;

/**
 * 邮件返回参数
 * @author Xin Chen (xinchenmelody@gmail.com)
 * @version 1.0
 * @date Created In 2018/8/30 23:07
 */
@Data
public class MailModel implements Serializable {
    /** 规则名 **/
    private String ruleName;
    /** 对比结果(一致|不一致) **/
    private String result;
    /** 差异数 **/
    private int number;
}
