/**
 * Copyright(c): 2018 com.mjduan All rights reserved.
 * 项目名：syn_sonar
 * 注意：未经作者允许，不得外传
 */
package com.xinchen.syn_sonar.sync.entity;

import lombok.Data;

/**
 * @author dmj1161859184@126.com 2018-08-26 18:26
 * @version 1.0
 * @since 1.0
 */
@Data
public class ResultAnalyse {
    private String chongQinSonarRuleId;
    private String beiJinSonarRuleId;
    private String analyseResult;
}
