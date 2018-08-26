/**
 * Copyright(c): 2018 com.mjduan All rights reserved.
 * 项目名：syn_sonar
 * 注意：未经作者允许，不得外传
 */
package com.xinchen.syn_sonar.sync.service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.xinchen.syn_sonar.sync.entity.SonarSyncResult;
import com.xinchen.syn_sonar.sync.repository.SonarSyncResultRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

/**
 * @author dmj1161859184@126.com 2018-08-26 18:46
 * @version 1.0
 * @since 1.0
 */
@Service("sonarSyncResultService")
public class SonarSyncResultServiceImpl implements SonarSyncResultService {
    @Autowired
    private SonarSyncResultRepository sonarSyncResultRepository;

    @Override
    public Page<SonarSyncResult> findByLanguage(Integer page, Integer size, SonarSyncResult sonarSync) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.Direction.DESC, "created_time");
        return sonarSyncResultRepository.findAll(new Specification<SonarSyncResult>() {
            @Override
            public Predicate toPredicate(Root<SonarSyncResult> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Predicate language = criteriaBuilder.equal(root.get("language").as(String.class), sonarSync.getLanguage());
                return criteriaBuilder.and(language);
            }
        }, pageRequest);
    }
}
