/**
 * Copyright(c): 2018 com.mjduan All rights reserved.
 * 项目名：syn_sonar
 * 注意：未经作者允许，不得外传
 */
package com.xinchen.syn_sonar.model;

import java.util.List;

import lombok.Data;

/**
 * @author dmj1161859184@126.com 2018-08-26 15:11
 * @version 1.0
 * @since 1.0
 */
@Data
public class Active {
    private String qProfile;
    private String inherit;
    private String severity;
    private List<Object> params;
    private String crateAt;
}
