/**
 * Copyright(c): 2018 com.mjduan All rights reserved.
 * 项目名：syn_sonar
 * 注意：未经作者允许，不得外传
 */
package com.xinchen.syn_sonar.sync.model;

import java.util.List;

import lombok.Data;

/**
 * @author dmj1161859184@126.com 2018-08-26 16:00
 * @version 1.0
 * @since 1.0
 */
@Data
public class Page {
    private Integer total;
    private Integer p;
    private Integer ps;

    private List<Rule> rules;
}
