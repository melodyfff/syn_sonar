/**
 * Copyright(c): 2018 com.mjduan All rights reserved.
 * 项目名：syn_sonar
 * 注意：未经作者允许，不得外传
 */
package com.xinchen.syn_sonar.sync.service;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.xinchen.syn_sonar.sonar.RestTemplateComponent;
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
    private RestTemplateComponent restTemplateComponent;
    @Autowired
    private SonarSyncProcessor sonarSyncProcessor;
    @Autowired
    private SonarSyncComponent sonarSyncComponent;

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
    public void compare(){
        sonarSyncProcessor.process();
    }

    @Override
    public void activeLocalRule(String profileKey, String ruleKey) {
        String url = sonarSyncComponent.getLocalHttpURL()+"/api/qualityprofiles/activate_rule";
        url = String.format(url, profileKey, ruleKey);
        Map<String,String> map=new HashMap<>();
        map.put("key",profileKey);
        map.put("rule",ruleKey);
        restTemplateComponent.getRestTemplateLocal().postForLocation(url,map);
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
    public void changeLocalSeverity(String profile,String ruleKey, String severity){
        String url=sonarSyncComponent.getLocalHttpURL()+ "/api/qualityprofiles/activate_rule";
        url = String.format(url,ruleKey,severity);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        String user = sonarSyncComponent.getLocalUsername();
        String password = sonarSyncComponent.getLocalPassword();
        String userMsg = user + ":" + password;
        String base64UserMsg = Base64.getEncoder().encodeToString(userMsg.getBytes());
        headers.add("Authorization ", "Basic "+base64UserMsg);
        MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
        map.put("key", Arrays.asList(profile));
        map.put("rule",Arrays.asList(ruleKey));

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);

        ResponseEntity<Void> response = restTemplateComponent.getRestTemplateLocal().postForEntity( url, request,Void.class);
    }

    public void changeLocalStatus(String ruleKey,String status){
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
        sonarSyncResultRepository.delete(sonarSyncResult);
    }
}
