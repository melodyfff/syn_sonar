package com.xinchen.syn_sonar.sonar;

import com.xinchen.syn_sonar.SynSonarApplicationTests;

import org.junit.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

/**
 * @author dmj1161859184@126.com 2018-08-28 22:04
 * @version 1.0
 * @since 1.0
 */
public class SonarSyncProcessorTest extends SynSonarApplicationTests {
    @Autowired
    private SonarSyncProcessor sonarSyncProcessor;

    @Test
    public void test_process(){
        sonarSyncProcessor.process();
    }

    @Test
    public void test2(){
        String url="http://localhost:9002/api/rules/search?rule_key=S3751&activation=true";
        String forObject = new RestTemplate().getForObject(url, String.class);
        System.out.println(forObject);
    }

    @Test
    public void test(){
        String profile="AWWBDzIIsOba1zB1O1Ea";
        String key="squid:S2757";
        String url="http://localhost:9000/api/rules/update";
    }
}