/**
 * Copyright(c): 2018 com.mjduan All rights reserved.
 * 项目名：syn_sonar
 * 注意：未经作者允许，不得外传
 */
package com.xinchen.syn_sonar.sync.model;

import lombok.Data;

/**
 * @author dmj1161859184@126.com 2018-08-26 15:20
 * @version 1.0
 * @since 1.0
 */
@Data
public class Profile {
    private String key;
    private String name;
    private String language;
    private String languageName;
    private Boolean isInherited;
    private Boolean isDefault;
    private Integer activeRuleCount;
    private Integer activeDeprecatedRuleCount;
    private String rulesUpdatedAt;
    private String organization;
    private Boolean isBuiltIn;
    private Actions actions;
}
