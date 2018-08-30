/**
 * Copyright(c): 2018 com.mjduan All rights reserved.
 * 项目名：syn_sonar
 * 注意：未经作者允许，不得外传
 */
package com.xinchen.syn_sonar.sync.service;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.xinchen.syn_sonar.sonar.RestTemplateComponent;
import com.xinchen.syn_sonar.sonar.SonarSyncCompareProcessor;
import com.xinchen.syn_sonar.sonar.SonarSyncComponent;
import com.xinchen.syn_sonar.sonar.SonarSyncProcessor;
import com.xinchen.syn_sonar.sync.entity.SonarSyncResult;
import com.xinchen.syn_sonar.sync.repository.SonarSyncResultRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 * @author dmj1161859184@126.com 2018-08-26 18:46
 * @version 1.0
 * @since 1.0
 */
@Service("sonarSyncResultService")
public class SonarSyncResultServiceImpl implements SonarSyncResultService {
    @Autowired
    private SonarSyncResultRepository sonarSyncResultRepository;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private RestTemplateComponent restTemplateComponent;
    @Autowired
    private SonarSyncCompareProcessor sonarSyncCompareProcessor;
    @Autowired
    private SonarSyncComponent sonarSyncComponent;
    @Autowired
    private SonarSyncProcessor sonarSyncProcessor;

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

    @Override
    public List<SonarSyncResult> findAllByLanguage(final String language) {
        String languageName = language;
        Sort orders = new Sort(Sort.Direction.DESC, "createdTime");
        return sonarSyncResultRepository.findAll(new Specification<SonarSyncResult>() {
            @Override
            public Predicate toPredicate(Root<SonarSyncResult> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Predicate languagePredicate = criteriaBuilder.equal(root.get("language").as(String.class), languageName.toLowerCase());
                return criteriaBuilder.and(languagePredicate);
            }
        }, orders);
    }

    @Override
    public void sync(String... languages) {
        sonarSyncProcessor.sync(languages);
    }

    @Override
    public void compare() {
        sonarSyncCompareProcessor.compare();
    }

    @Override
    public Integer getRecentVersion() {
        String sql = "select max(version) as maxVersion from sonar_sync_result";
        Query query = entityManager.createNativeQuery(sql);
        Object maxVersion = query.getSingleResult();
        maxVersion = maxVersion == null ? 0 : maxVersion;
        if (maxVersion instanceof BigInteger) {
            return ((BigInteger) maxVersion).intValue();
        }
        return (Integer) maxVersion;
    }


    @Override
    public void activeLocalRule(String profileKey, String ruleKey) {
        String url = sonarSyncComponent.getLocalHttpURL() + "/api/qualityprofiles/activate_rule";
        url = String.format(url, profileKey, ruleKey);
        Map<String, String> map = new HashMap<>();
        map.put("key", profileKey);
        map.put("rule", ruleKey);
        restTemplateComponent.getRestTemplateLocal().postForLocation(url, map);
    }

    @Override
    public void deactiveLocalRule(String profileKey, String ruleKey) {
        String url = "/api/qualityprofiles/deactivate_rule?key=%s&rule=%s";
        url = String.format(url, profileKey, ruleKey);
        restTemplateComponent.getRestTemplateLocal().getForObject(url, Void.class);
    }

    @Override
    public void changeLocalSeverity(String ruleKey, String Serverity) {

    }

    @Override
    public void changeLocalStatus(String profile, String ruleKey, String status) {

    }

    @Override
    public void changeLocalSeverity(String profile, String ruleKey, String severity) {
        String url = sonarSyncComponent.getLocalHttpURL() + "/api/qualityprofiles/activate_rule";
        url = String.format(url, ruleKey, severity);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        String user = sonarSyncComponent.getLocalUsername();
        String password = sonarSyncComponent.getLocalPassword();
        String userMsg = user + ":" + password;
        String base64UserMsg = Base64.getEncoder().encodeToString(userMsg.getBytes());
        //headers.add("User-Agent", "curl/7.58.0");
        headers.add("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36");
        headers.add("Authorization", "Basic " + base64UserMsg);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        map.put("profile_key", Arrays.asList(profile));
        map.put("rule_key", Arrays.asList(ruleKey));
        map.put("severity", Arrays.asList(severity));
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);

        ResponseEntity<Void> response = restTemplateComponent.getRestTemplateLocal().postForEntity(url, request, Void.class);
    }

    public void changeLocalStatus(String ruleKey, String status) {
        //TODO
    }

    @Override
    public void saveSonarSyncResult(SonarSyncResult sonarSyncResult) {
        sonarSyncResultRepository.save(sonarSyncResult);
    }

    @Override
    public void deleteSonarSyncResult(String ruleKey) {
        SonarSyncResult sonarSyncResult = new SonarSyncResult();
        sonarSyncResult.setRuleKey(ruleKey);
        //先查出，后根据ID删除，心累
        List<SonarSyncResult> sonarSyncResults = sonarSyncResultRepository.findAll(new Specification<SonarSyncResult>() {
            @Override
            public Predicate toPredicate(Root<SonarSyncResult> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Predicate predicate = criteriaBuilder.equal(root.get("ruleKey").as(String.class), ruleKey);
                return criteriaBuilder.and(predicate);
            }
        });
        System.out.println(sonarSyncResults);
        for (SonarSyncResult sonarSyncResult1 : sonarSyncResults) {
            sonarSyncResultRepository.deleteById(sonarSyncResult1.getId());
        }
    }
}
