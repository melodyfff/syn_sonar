/**
 * Copyright(c): 2018 com.mjduan All rights reserved.
 * 项目名：syn_sonar
 * 注意：未经作者允许，不得外传
 */
package com.xinchen.syn_sonar.sync.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

import org.hibernate.annotations.Proxy;

/**
 * 与数据库实体对应
 *
 * @author dmj1161859184@126.com 2018-08-26 18:21
 * @version 1.0
 * @since 1.0
 */
@Data
@Entity(name = "sonar_sync_result")
@Proxy(lazy = false)
public class SonarSyncResult implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "language")
    private String language;
    @Column(name = "mark")
    private String mark;
    @Column(name = "updated_time")
    private Date updatedTime;
    @Column(name = "created_time")
    private Date createdTime;
    @Column(name = "result_type")
    private ResultType resultType;
    @Column(name = "exrta")
    private String extra;

    public enum ResultType {
        ADD, MODIFY
    }
}
