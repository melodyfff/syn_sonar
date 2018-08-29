/**
 * Copyright(c): 2018 com.mjduan All rights reserved.
 * 项目名：syn_sonar
 * 注意：未经作者允许，不得外传
 */
package com.xinchen.syn_sonar.sync.repository;

import com.xinchen.syn_sonar.sync.entity.SonarSyncResult;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author dmj1161859184@126.com 2018-08-26 18:44
 * @version 1.0
 * @since 1.0
 */
@Repository("sonarSyncResultRepository")
public interface SonarSyncResultRepository extends CrudRepository<SonarSyncResult, Long>,
        JpaSpecificationExecutor<SonarSyncResult> {
}
