/**
 * Copyright(c): 2018 com.mjduan All rights reserved.
 * 项目名：syn_sonar
 * 注意：未经作者允许，不得外传
 */
package com.xinchen.syn_sonar.sync.model;

import java.util.List;

import lombok.Data;

/**
 * @author dmj1161859184@126.com 2018-08-26 15:08
 * @version 1.0
 * @since 1.0
 */
@Data
public class Rule {
    /** 可以理解为rule id */
    private String key;
    private String repo;
    private String name;
    private String createdAt;
    private String htmlDesc;
    private String mdDesc;
    private String severity;
    private String status;
    private String internalKey;
    private boolean isTemplate;
    private List<String> tags;
    private List<String> sysTags;
    private String lang;
    private String langName;
    private Object params;
    private String type;
    //sonar返回了很多字段，由于没有使用到，所以不添加了
}
