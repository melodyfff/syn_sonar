package com.xinchen.syn_sonar.core.model;

import lombok.Data;

/**
 * @author Xin Chen (xinchenmelody@gmail.com)
 * @version 1.0
 * @date Created In 2018/9/1 2:52
 */
@Data
public class ProfilesModel {
    /** 在页面上的名字 **/
    private String name;

    /** 规则KEY值 **/
    private String key;

    /** 激活的规则总数 **/
    private int activeRuleCount;

    /** 语言名 **/
    private String languageName;
}
