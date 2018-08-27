/**
 * Copyright(c): 2018 com.mjduan All rights reserved.
 * 项目名：syn_sonar
 * 注意：未经作者允许，不得外传
 */
package com.xinchen.syn_sonar.core.common;

import lombok.Data;

/**
 * @author dmj1161859184@126.com 2018-08-26 18:39
 * @version 1.0
 * @since 1.0
 */
@Data
public class PageInfo {
    private Integer totalSize;
    /** 查询第几页数据 */
    private Integer page;
    /** 每页显示多少条数据 */
    private Integer size;
}
