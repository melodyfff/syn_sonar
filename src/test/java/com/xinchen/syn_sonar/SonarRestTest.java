/**
 * Copyright(c): 2018 com.mjduan All rights reserved.
 * 项目名：syn_sonar
 * 注意：未经作者允许，不得外传
 */
package com.xinchen.syn_sonar;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xinchen.syn_sonar.sync.model.RulePage;
import com.xinchen.syn_sonar.sync.model.ProfilesActions;
import com.xinchen.syn_sonar.sync.model.RuleActives;

import org.springframework.web.client.RestTemplate;

/**
 * @author dmj1161859184@126.com 2018-08-26 18:00
 * @version 1.0
 * @since 1.0
 */
public class SonarRestTest extends SynSonarApplicationTests {
    private static final Logger LOGGER = LoggerFactory.getLogger(SonarRestTest.class);
    private RestTemplate restTemplate = new RestTemplate();

    //http://localhost:9000/api/qualityprofiles/search?defaults=true
    //查询得到的结果是各种按编程语言类型来分的
    @Test
    public void test1(){
        String url="http://localhost:9000/api/qualityprofiles/search?defaults=true";
        ProfilesActions profilesActions = restTemplate.getForObject(url, ProfilesActions.class);
        LOGGER.info(profilesActions.toString());
        LOGGER.info("profile size:{}",profilesActions.getProfiles().size());
    }

    //查询特定类型中包含的规则，如java的规则有哪些
    //涉及分页查询
    @Test
    public void test2(){
        String url="http://localhost:9000/api/rules/search?qprofile=AWV1AVEh6AnXIfQjv3ES&activation=true&p=1&ps=1&facets=types";
        RulePage profilesActions = restTemplate.getForObject(url, RulePage.class);
        LOGGER.info(profilesActions.toString());
    }

    @Test
    public void test3(){
        String url="http://localhost:9000/api/rules/search?qprofile=AWV1AVEh6AnXIfQjv3ES&activation=true&p=1&ps=1&facets=types";
        RulePage profilesActions = restTemplate.getForObject(url, RulePage.class);
        LOGGER.info(profilesActions.toString());
    }

    //根据规则ID查询特定的规则
    @Test
    public void test4(){
        String url="http://localhost:9000/api/rules/show?key=csharpsquid:S2225&actives=true";
        RuleActives ruleActives = restTemplate.getForObject(url, RuleActives.class);
        LOGGER.info(ruleActives.toString());
    }
}
