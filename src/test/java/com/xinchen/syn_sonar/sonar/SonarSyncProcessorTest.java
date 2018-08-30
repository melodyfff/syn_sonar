package com.xinchen.syn_sonar.sonar;

import java.io.IOException;

import com.xinchen.syn_sonar.SynSonarApplicationTests;
import com.xinchen.syn_sonar.sync.model.Rule;

import org.apache.http.auth.AuthenticationException;
import org.junit.Test;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author dmj1161859184@126.com 2018-08-31 01:28
 * @version 1.0
 * @since 1.0
 */
public class SonarSyncProcessorTest extends SynSonarApplicationTests {
    @Autowired
    private SonarSyncProcessor sonarSyncProcessor;

    @Test
    public void test1() throws IOException, AuthenticationException {
        sonarSyncProcessor.activeLocalRule("AWWBDzIIsOba1zB1O1Ea", "squid:S2975");
    }

    @Test
    public void test_sync() {
        sonarSyncProcessor.sync("java");
    }

    @Test
    public void test_updateRule() throws IOException, AuthenticationException {
        Rule rule = new Rule();
        rule.setKey("squid:S00119");
        rule.setSeverity("MAJOR");
        rule.setStatus("READY");
        sonarSyncProcessor.updateRule(rule);
    }

    @Test
    public void test_createRule() throws IOException, AuthenticationException {
        Rule rule = new Rule();
        rule.setType("CODE_SMELL");
        rule.setStatus("READY");
        rule.setSeverity("INFO");
        rule.setKey("squid:S88888");
        rule.setHtmlDesc("xxxxx");
        rule.setLang("java");
        rule.setLangName("Java");
        rule.setName("mjduan");
        rule.setRepo("squid");
        sonarSyncProcessor.createRule(rule);
    }
}