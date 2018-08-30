package com.xinchen.syn_sonar.sync.service;

import java.math.BigInteger;
import java.util.List;

import com.xinchen.syn_sonar.SynSonarApplicationTests;
import com.xinchen.syn_sonar.sonar.SonarSyncComponent;
import com.xinchen.syn_sonar.sync.entity.SonarSyncResult;
import com.xinchen.syn_sonar.sync.model.RulePage;

import org.junit.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

/**
 * @author dmj1161859184@126.com 2018-08-29 21:16
 * @version 1.0
 * @since 1.0
 */
public class SonarSyncResultServiceImplTest extends SynSonarApplicationTests {
    @Autowired
    private SonarSyncResultService sonarSyncResultService;
    @Autowired
    private SonarSyncComponent sonarSyncComponent;

    @Test
    public void test(){
        sonarSyncResultService.changeLocalSeverity("AWWBDjE14dKO5VYIM2kw","squid:S2204","CRITICAL");
    }

    @Test
    public void test_delete(){
        sonarSyncResultService.deleteSonarSyncResult("squid:S3751");
    }

    @Test
    public void test_getByLanguage(){
        List<SonarSyncResult> result = sonarSyncResultService.findAllByLanguage("java");
        System.out.println(result.size());
        System.out.println(result);
    }

    @Test
    public void testGetRecentVersion(){
        Integer recentVersion = sonarSyncResultService.getRecentVersion();
        System.out.println(recentVersion);
    }

    @Test
    public void test_getRule(){
        String ruleKey="squid:S00119";
        String url = sonarSyncComponent.getLocalHttpURL() + "/api/rules/search?rule_key="+ruleKey;

        RulePage rulePage = new RestTemplate().getForObject(url, RulePage.class);
        System.out.println(rulePage);
    }
}