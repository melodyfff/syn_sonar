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
    //规则名称
    @Column(name = "rule_name")
    private String ruleName;
    @Column(name = "mark")
    private String mark;
    @Column(name = "updated_time")
    private Date updatedTime;
    @Column(name = "created_time")
    private Date createdTime;
    //表示本地没有该规则
    @Column(name = "absence")
    private Boolean absence;
    //表示远程规则与本地规则不一样
    @Column(name = "difference")
    private Boolean difference;
    @Column(name = "local_profile_key")
    private String localProfileKey;
    @Column(name = "remote_profile_key")
    private String remoteProfileKey;
    @Column(name = "rule_key")
    private String ruleKey;
    @Column(name = "local_severity")
    private String localSeverity;
    @Column(name = "remote_severity")
    private String remoteSeverity;
    @Column(name = "local_status")
    private String localStatus;
    @Column(name = "remote_status")
    private String remoteStatus;

    //本地规则的type
    @Column(name = "local_type")
    private String localType;
    //远程规则的type
    @Column(name = "remote_type")
    private String remoteType;
    //该规则在否是需要激活
    @Column(name = "need_local_active")
    private Boolean needLocalActive = false;

    //入库版本号，执行批次号
    @Column(name = "version")
    private Long version;
}
