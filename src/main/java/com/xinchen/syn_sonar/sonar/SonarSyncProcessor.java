/**
 * Copyright(c): 2018 com.mjduan All rights reserved.
 * 项目名：syn_sonar
 * 注意：未经作者允许，不得外传
 */
package com.xinchen.syn_sonar.sonar;

import com.xinchen.syn_sonar.sonar.thread.CustomizedThreadFactory;
import com.xinchen.syn_sonar.sonar.thread.CustomizedThreadPoolExecutor;
import com.xinchen.syn_sonar.sonar.thread.RulePullCallable;
import com.xinchen.syn_sonar.sync.entity.SonarSyncResult;
import com.xinchen.syn_sonar.sync.model.*;
import com.xinchen.syn_sonar.sync.service.SonarSyncResultService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author dmj1161859184@126.com 2018-08-27 00:31
 * @version 1.0
 * @since 1.0
 */
@Component
public class SonarSyncProcessor {
    private static final Logger LOGGER = LoggerFactory.getLogger(SonarSyncComponent.class);
    /**
     * 有意的设置为2
     */
    private static final ThreadPoolExecutor poolExecutor = CustomizedThreadPoolExecutor.customizedThreadPoolExecutor(2, 2, CustomizedThreadFactory.customizedThreadFactory());
    private static RestTemplate restTemplate;

    static {
        //设置超时时间
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        //设置为15秒，JDK8上可以用下划线隔开
        requestFactory.setConnectTimeout(15_000);
        requestFactory.setReadTimeout(15_000);

        restTemplate = new RestTemplate(requestFactory);
    }

    @Autowired
    private SonarSyncComponent sonarSyncComponent;
    @Autowired
    private SonarSyncResultService sonarSyncResultService;

    /**
     * 同步远程服务器和本地服务器上的规则
     *
     * @throws ExecutionException
     * @throws InterruptedException
     */
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
                Future<RuleActives> remoteRuleFuture = poolExecutor.submit(new RulePullCallable(restTemplate, sonarSyncComponent, rule.getKey(), true));
                Future<RuleActives> localRuleFuture = poolExecutor.submit(new RulePullCallable(restTemplate, sonarSyncComponent, rule.getKey(), false));

                RuleActives remoteRule = remoteRuleFuture.get();
                RuleActives localRule = localRuleFuture.get();
                doProcess(remoteRule, localRule);
            }
            rulePage = getRemoteRulePage(profile.getKey(), page + 1, pageSize);
            LOGGER.info("从远程sonar上获得的,{}对应的rule个数是{}", profile.getKey(), rulePage.getRules().size());
        }
    }

    private void doProcess(RuleActives remoteRule, RuleActives localRule) {
        if (localRule == null) {
            processLocalRuleIsNull(remoteRule);
        } else if (remoteRule.getRule().getSeverity().equalsIgnoreCase(localRule.getRule().getSeverity())) {
            processLocalRuleSeverityDifference(remoteRule, localRule);
        }
    }

    private void processLocalRuleSeverityDifference(RuleActives remoteRule, RuleActives localRule) {
        LOGGER.info("对ID为{}的规则，远程规则的severity和本地规则severity不一样，远程:{},本地:{}", remoteRule, localRule);
        SonarSyncResult sonarSyncResult = new SonarSyncResult();
        Rule rule = remoteRule.getRule();
        sonarSyncResult.setRuleKey(rule.getKey());
        sonarSyncResult.setResultType(SonarSyncResult.ResultType.SEVERITY_DIFFERENCE);
        //TODO rule.getLangName()返回的是语言名称吗
        sonarSyncResult.setLanguage(rule.getLangName());
        sonarSyncResult.setMark("规则的severity不一样");
        sonarSyncResult.setUpdatedTime(new Date());
        sonarSyncResult.setCreatedTime(new Date());
        saveOrUpdateSonarSyncResult(sonarSyncResult);
    }

    private void processLocalRuleIsNull(RuleActives remoteRule) {
        LOGGER.info("远程sonar的规则{}，本地sonar上没有", remoteRule);
        Rule rule = remoteRule.getRule();
        SonarSyncResult sonarSyncResult = new SonarSyncResult();
        sonarSyncResult.setLanguage(rule.getLangName());
        sonarSyncResult.setResultType(SonarSyncResult.ResultType.ABSENCE);
        sonarSyncResult.setRuleKey(rule.getKey());
        sonarSyncResult.setMark("本地sonar缺失规则");
        sonarSyncResult.setCreatedTime(new Date());
        sonarSyncResult.setUpdatedTime(new Date());
        saveOrUpdateSonarSyncResult(sonarSyncResult);
    }

    private void saveOrUpdateSonarSyncResult(SonarSyncResult sonarSyncResult) {
        //一定要删除，不然会堆积
        sonarSyncResultService.deleteSonarSyncResult(sonarSyncResult.getRuleKey());
        sonarSyncResultService.saveSonarSyncResult(sonarSyncResult);
    }

    private ProfilesActions getRemoteAllProfilesActions() {
        String url = "http://%s:%d/api/qualityprofiles/search?defaults=true";
        url = String.format(url, sonarSyncComponent.getRemotehost(), sonarSyncComponent.getRemoteport());
        return restTemplate.getForObject(url, ProfilesActions.class);
    }

    private RulePage getRemoteRulePage(String profileKey, int page, int pageSize) {
        String url = "http://%s:%d/api/rules/search?qprofile=%s&activation=true&p=%d&ps=%d&facets=types";
        url = String.format(url, sonarSyncComponent.getRemotehost(), sonarSyncComponent.getRemoteport(), profileKey, page, pageSize);
        return restTemplate.getForObject(url, RulePage.class);
    }
}
