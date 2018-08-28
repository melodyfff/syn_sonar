/**
 * Copyright(c): 2018 com.mjduan All rights reserved.
 * 项目名：syn_sonar
 * 注意：未经作者允许，不得外传
 */
package com.xinchen.syn_sonar.sonar;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

import com.xinchen.syn_sonar.sonar.thread.CustomizedThreadFactory;
import com.xinchen.syn_sonar.sonar.thread.CustomizedThreadPoolExecutor;
import com.xinchen.syn_sonar.sonar.thread.RulePullCallable;
import com.xinchen.syn_sonar.sync.entity.SonarSyncResult;
import com.xinchen.syn_sonar.sync.model.Profile;
import com.xinchen.syn_sonar.sync.model.ProfilesActions;
import com.xinchen.syn_sonar.sync.model.Rule;
import com.xinchen.syn_sonar.sync.model.RuleActives;
import com.xinchen.syn_sonar.sync.model.RulePage;
import com.xinchen.syn_sonar.sync.service.SonarSyncResultService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
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
    /**
     * 有意的设置为2
     */
    private static final ThreadPoolExecutor poolExecutor = CustomizedThreadPoolExecutor.customizedThreadPoolExecutor(2, 2, CustomizedThreadFactory.customizedThreadFactory());

    @Autowired
    private RestTemplateComponent restTemplateComponent;

    @Autowired
    private SonarSyncComponent sonarSyncComponent;
    @Autowired
    private SonarSyncResultService sonarSyncResultService;

    /**
     * 同步远程服务器和本地服务器上的规则
     */
    public void process() {
        long startTime = System.currentTimeMillis();
        LOGGER.info("同步任务开始执行");
        List<Profile> profiles = getRemoteAllProfilesActions().getProfiles();
        LOGGER.info("远程sonar上获得的profile个数是:{}", profiles.size());
        for (Profile profile : profiles) {
            try {
                processProfileRules(profile);
            } catch (ExecutionException e) {
                //有意的不抛出异常
                LOGGER.error(e.getMessage(), e);
            }
        }
        LOGGER.info("同步任务结束执行，用时{}毫秒", (System.currentTimeMillis() - startTime));
    }

    private void processProfileRules(Profile profile) throws ExecutionException {
        LOGGER.info("开始处理profile:{}", profile);
        int page = 1;
        int pageSize = 200;
        RulePage rulePage = getRemoteRulePage(profile.getKey(), page, pageSize);
        LOGGER.info("从远程sonar上获得的,{}对应的rule总数是{}", rulePage.getTotal());
        LOGGER.info("从远程sonar上获得的,{}对应的rule个数是{}", profile.getKey(), rulePage.getRules().size());
        while (rulePage.getP() * rulePage.getPs() < rulePage.getTotal()) {
            List<Rule> rules = rulePage.getRules();
            for (Rule rule : rules) {
                Future<RuleActives> remoteRuleFuture = poolExecutor.submit(new RulePullCallable(restTemplateComponent.getRestTemplateRemote(), sonarSyncComponent, rule.getKey(), true));
                Future<RuleActives> localRuleFuture = poolExecutor.submit(new RulePullCallable(restTemplateComponent.getRestTemplateLocal(), sonarSyncComponent, rule.getKey(), false));
                try {
                    RuleActives remoteRule = remoteRuleFuture.get();
                    RuleActives localRule = localRuleFuture.get();
                    doProcess(profile.getKey(), remoteRule, localRule);
                } catch (InterruptedException e) {
                    //有意的不抛出异常
                    LOGGER.error(e.getMessage(), e);
                }
            }
            rulePage = getRemoteRulePage(profile.getKey(), page + 1, pageSize);
            LOGGER.info("从远程sonar上获得的,{}对应的rule个数是{}", profile.getKey(), rulePage.getRules().size());
        }
    }

    private void doProcess(String profileKey, RuleActives remoteRule, RuleActives localRule) {
        SonarSyncResult sonarSyncResult = new SonarSyncResult();
        Rule rule = remoteRule.getRule();
        sonarSyncResult.setProfileKey(profileKey);
        sonarSyncResult.setRuleKey(rule.getKey());
        sonarSyncResult.setLanguage(rule.getLangName());
        sonarSyncResult.setCreatedTime(new Date());
        sonarSyncResult.setUpdatedTime(new Date());

        boolean flag=false;
        if (localRule == null) {
            //本地没有该规则
            sonarSyncResult.setAbsence(true);
            flag=true;
            LOGGER.info("规则缺失，远程sonar的规则{}，本地sonar上没有", remoteRule);
        } else {
            if (!remoteRule.getRule().getSeverity().equalsIgnoreCase(localRule.getRule().getSeverity())) {
                //severity不同
                processSeverityDifference(sonarSyncResult, remoteRule, localRule);
                flag=true;
            }
            if (!remoteRule.getRule().getStatus().equalsIgnoreCase(localRule.getRule().getStatus())) {
                //status不同
                processStatusDifference(sonarSyncResult, remoteRule, localRule);
                flag=true;
            }
        }
        if (flag) {
            saveOrUpdateSonarSyncResult(sonarSyncResult);
        }
    }

    private void processStatusDifference(SonarSyncResult sonarSyncResult, RuleActives remoteRule, RuleActives localRule) {
        LOGGER.info("远程规则的status和本地规则status不一样，远程:{},本地:{}", remoteRule, localRule);
        sonarSyncResult.setRemoteStatus(remoteRule.getRule().getStatus());
        sonarSyncResult.setLocalStatus(localRule.getRule().getStatus());
    }

    private void processSeverityDifference(SonarSyncResult sonarSyncResult, RuleActives remoteRule, RuleActives localRule) {
        LOGGER.info("远程规则的severity和本地规则severity不一样，远程:{},本地:{}", remoteRule, localRule);
        sonarSyncResult.setLocalSeverity(localRule.getRule().getSeverity());
        sonarSyncResult.setRemoteSeverity(remoteRule.getRule().getSeverity());
    }

    private void saveOrUpdateSonarSyncResult(SonarSyncResult sonarSyncResult) {
        //一定要删除后再添加，不然很有可能会堆积
        sonarSyncResultService.deleteSonarSyncResult(sonarSyncResult.getRuleKey());
        sonarSyncResultService.saveSonarSyncResult(sonarSyncResult);
    }

    private ProfilesActions getRemoteAllProfilesActions() {
        String url = "http://%s:%d/api/qualityprofiles/search?defaults=true";
        url = String.format(url, sonarSyncComponent.getRemoteHost(), sonarSyncComponent.getRemotePort());
        return restTemplateComponent.getRestTemplateRemote().getForObject(url, ProfilesActions.class);
    }

    private RulePage getRemoteRulePage(String profileKey, int page, int pageSize) {
        String url = "http://%s:%d/api/rules/search?qprofile=%s&activation=true&p=%d&ps=%d&facets=types";
        url = String.format(url, sonarSyncComponent.getRemoteHost(), sonarSyncComponent.getRemotePort(), profileKey, page, pageSize);
        return restTemplateComponent.getRestTemplateRemote().getForObject(url, RulePage.class);
    }
}
