/**
 * Copyright(c): 2018 com.mjduan All rights reserved.
 * 项目名：syn_sonar
 * 注意：未经作者允许，不得外传
 */
package com.xinchen.syn_sonar.model;

import lombok.Data;

/**
 * @author dmj1161859184@126.com 2018-08-26 15:23
 * @version 1.0
 * @since 1.0
 */
@Data
public class Actions {
    private Boolean edit;
    private Boolean setAsDefault;
    private Boolean copy;

    private Boolean created;
}
