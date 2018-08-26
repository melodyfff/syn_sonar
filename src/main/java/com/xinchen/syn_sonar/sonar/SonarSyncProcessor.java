/**
 * Copyright(c): 2018 com.mjduan All rights reserved.
 * 项目名：syn_sonar
 * 注意：未经作者允许，不得外传
 */
package com.xinchen.syn_sonar.sonar;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

import com.xinchen.syn_sonar.sonar.thread.CustomizedThreadFactory;
import com.xinchen.syn_sonar.sonar.thread.CustomizedThreadPoolExecutor;
import com.xinchen.syn_sonar.sonar.thread.RulePullRunnable;
import com.xinchen.syn_sonar.sync.model.Profile;
import com.xinchen.syn_sonar.sync.model.ProfilesActions;
import com.xinchen.syn_sonar.sync.model.Rule;
import com.xinchen.syn_sonar.sync.model.RuleActives;
import com.xinchen.syn_sonar.sync.model.RulePage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * @author dmj1161859184@126.com 2018-08-27 00:31
 * @version 1.0
 * @since 1.0
 */
@Component
public class SonarSyncProcessor {
    private static final Logger LOGGER = LoggerFactory.getLogger(SonarSyncComponent.class);
    /** 有意的设置为2 */
    private static final ThreadPoolExecutor poolExecutor = CustomizedThreadPoolExecutor.customizedThreadPoolExecutor(2, 2, CustomizedThreadFactory.customizedThreadFactory());
    private static RestTemplate restTemplate;

    static {
        //设置超时时间
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        //设置为15秒
        requestFactory.setConnectTimeout(15000);
        requestFactory.setReadTimeout(15000);

        restTemplate = new RestTemplate(requestFactory);
    }

    @Autowired
    private SonarSyncComponent sonarSyncComponent;

    public void process() throws ExecutionException, InterruptedException {
        List<Profile> profiles = getRemoteAllProfilesActions().getProfiles();
        LOGGER.info("远程sonar上获得的profile个数是:{}", profiles.size());
        for (Profile profile : profiles) {
            processProfileRules(profile);
        }
    }

    private void processProfileRules(Profile profile) throws ExecutionException, InterruptedException {
        LOGGER.info("开始处理profile:{}", profile);
        int page = 1;
        int pageSize = 100;
        RulePage rulePage = getRemoteRulePage(profile.getKey(), page, pageSize);
        LOGGER.info("从远程sonar上获得的,{}对应的rule总数是{}", rulePage.getTotal());
        LOGGER.info("从远程sonar上获得的,{}对应的rule个数是{}", profile.getKey(), rulePage.getRules().size());
        while (rulePage.getP() * rulePage.getPs() < rulePage.getTotal()) {
            List<Rule> rules = rulePage.getRules();
            for (Rule rule : rules) {
                Future<RuleActives> remoteRuleFuture = poolExecutor.submit(new RulePullRunnable(restTemplate, sonarSyncComponent, rule.getKey(), true));
                Future<RuleActives> localRuleFuture = poolExecutor.submit(new RulePullRunnable(restTemplate, sonarSyncComponent, rule.getKey(), false));

                RuleActives remoteRule = remoteRuleFuture.get();
                RuleActives localRule = localRuleFuture.get();
                doProcess(remoteRule,localRule);
            }
            rulePage = getRemoteRulePage(profile.getKey(), page + 1, pageSize);
            LOGGER.info("从远程sonar上获得的,{}对应的rule个数是{}", profile.getKey(), rulePage.getRules().size());
        }
    }

    private void doProcess(RuleActives remoteRule, RuleActives localRule) {

    }

    public ProfilesActions getRemoteAllProfilesActions() {
        String url = "http://%s:%d/api/qualityprofiles/search?defaults=true";
        url = String.format(url, sonarSyncComponent.getRemotehost(), sonarSyncComponent.getRemoteport());
        return restTemplate.getForObject(url, ProfilesActions.class);
    }

    public RulePage getRemoteRulePage(String profileKey, int page, int pageSize) {
        String url = "http://%s:%d/api/rules/search?qprofile=%s&activation=true&p=%d&ps=%d&facets=types";
        url = String.format(url, sonarSyncComponent.getRemotehost(), sonarSyncComponent.getRemoteport(), profileKey, page, pageSize);
        return restTemplate.getForObject(url, RulePage.class);
    }

    public RuleActives getRemoteRuleActives(String key) {
        return getRuleActives(key, sonarSyncComponent.getRemotehost());
    }

    public RuleActives getLocalRuleActives(String key) {
        return getRuleActives(key, sonarSyncComponent.getLocalhost());
    }

    public RuleActives getRuleActives(String key, String host) {
        String url = "http://%s:%d/api/rules/show?key=%s&actives=true";
        url = String.format(url, sonarSyncComponent.getRemotehost(), sonarSyncComponent.getRemoteport(), key);
        return restTemplate.getForObject(url, RuleActives.class);
    }
}
