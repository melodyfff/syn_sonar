/**
 * Copyright(c): 2018 com.mjduan All rights reserved.
 * 项目名：syn_sonar
 * 注意：未经作者允许，不得外传
 */
package com.xinchen.syn_sonar.sync.service;

import com.xinchen.syn_sonar.sync.entity.SonarSyncResult;

import org.springframework.data.domain.Page;

/**
 * @author dmj1161859184@126.com 2018-08-26 18:46
 * @version 1.0
 * @since 1.0
 */
public interface SonarSyncResultService {

    /**
     * 根据编程语言，来分页查询，结果按创建时间降序排序
     *
     * @param page 第几页数据
     * @param size 每页显示几条
     * @param sonarSync
     * @return 结果
     */
    Page<SonarSyncResult> findByLanguage(Integer page, Integer size, SonarSyncResult sonarSync);

    /**
     * 保存SonarSyncResult
     *
     * @param sonarSyncResult
     */
    void saveSonarSyncResult(SonarSyncResult sonarSyncResult);

    /**
     * 按规则key，进行删除
     *
     * @param ruleKey
     */
    void deleteSonarSyncResult(String ruleKey);

    /**
     * 激活规则
     *
     * @param profileKey
     * @param ruleKey
     */
    void activeLocalRule(String profileKey,String ruleKey);

    /**
     * 失效某个规则
     *
     * @param profileKey
     * @param ruleKey
     */
    void deactiveLocalRule(String profileKey, String ruleKey);

    /**
     * 修改规则的severity
     *
     * @param ruleKey
     * @param Serverity
     */
    void changeServerity(String ruleKey, String Serverity);
}
