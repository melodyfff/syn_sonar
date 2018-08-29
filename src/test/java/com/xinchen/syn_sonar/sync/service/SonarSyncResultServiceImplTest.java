package com.xinchen.syn_sonar.sync.service;

import com.xinchen.syn_sonar.SynSonarApplicationTests;

import org.junit.Test;

import org.springframework.beans.factory.annotation.Autowired;
/**
 * @author dmj1161859184@126.com 2018-08-29 21:16
 * @version 1.0
 * @since 1.0
 */
public class SonarSyncResultServiceImplTest extends SynSonarApplicationTests {
    @Autowired
    private SonarSyncResultService sonarSyncResultService;

    @Test
    public void test(){
        sonarSyncResultService.changeLocalSeverity("AWWBDjE14dKO5VYIM2kw","squid:S2204","CRITICAL");
    }
}