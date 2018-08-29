/**
 * Copyright(c): 2018 com.mjduan All rights reserved.
 * 项目名：syn_sonar
 * 注意：未经作者允许，不得外传
 */
package com.xinchen.syn_sonar.sonar;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

import com.xinchen.syn_sonar.sonar.thread.CustomizedThreadFactory;
import com.xinchen.syn_sonar.sonar.thread.CustomizedThreadPoolExecutor;
import com.xinchen.syn_sonar.sync.entity.SonarSyncResult;
import com.xinchen.syn_sonar.sync.model.Profile;
import com.xinchen.syn_sonar.sync.model.ProfilesActions;
import com.xinchen.syn_sonar.sync.model.Rule;
import com.xinchen.syn_sonar.sync.model.RulePage;
import com.xinchen.syn_sonar.sync.service.SonarSyncResultService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
        List<Profile> remoteProfiles = getRemoteAllProfilesActions().getProfiles();
        List<Profile> localProfiless = getLocalAllProfilesActions().getProfiles();
        LOGGER.info("远程sonar上获得的profile个数是:{}", remoteProfiles.size());
        for (Profile remoteProfile : remoteProfiles) {
            Profile localProfile = getLocalProfile(remoteProfile, localProfiless);
            if (localProfile == null) {
                LinkedList<Rule> allRemoteRulePage = getAllRemoteRulePage(remoteProfile.getKey());
                for (Rule rule : allRemoteRulePage) {
                    processAbsence(remoteProfile, localProfile, rule);
                }
            } else {
                try {
                    LinkedList<Rule> allRemoteRulePage = getAllRemoteRulePage(remoteProfile.getKey());
                    LinkedList<Rule> allLocalRulePage = getAllLocalRulePage(localProfile.getKey());
                    for (Rule remoteRule : allRemoteRulePage) {
                        boolean flag = false;
                        Rule localRule = null;
                        for (int i = 0; i < allLocalRulePage.size(); i++) {
                            localRule = allLocalRulePage.get(i);
                            if (remoteRule.getKey().equalsIgnoreCase(localRule.getKey())) {
                                flag = true;
                                break;
                            }
                        }
                        if (flag) {
                            processDifference(remoteProfile.getKey(), localProfile.getKey(), remoteRule, localRule);
                        } else {
                            processAbsence(remoteProfile, localProfile, remoteRule);
                        }
                    }
                } catch (Exception e) {
                    LOGGER.error(e.getMessage(), e);
                }
            }
        }
        LOGGER.info("同步任务结束执行，用时{}毫秒", (System.currentTimeMillis() - startTime));
    }

    private Profile getLocalProfile(Profile remoteProfile, List<Profile> localProfiless) {
        for (Profile profile : localProfiless) {
            if (remoteProfile.getLanguage().equalsIgnoreCase(profile.getLanguage())) {
                return profile;
            }
        }
        return null;
    }

    private int getTotalPageNum(int pageSize, int total) {
        int tmp = total % pageSize;
        return tmp == 0 ? total / pageSize : total / pageSize + 1;
    }

    private void processDifference(String profileKey, String localProfileKey, Rule remoteRule, Rule localRule) {
        SonarSyncResult sonarSyncResult = getSonarSyncResult(profileKey, localProfileKey, localRule);
        boolean flag = false;
        if (!remoteRule.getSeverity().equalsIgnoreCase(localRule.getSeverity())) {
            //severity不同
            processSeverityDifference(sonarSyncResult, remoteRule, localRule);
            flag = true;
        }
        if (!remoteRule.getStatus().equalsIgnoreCase(localRule.getStatus())) {
            //status不同
            processStatusDifference(sonarSyncResult, remoteRule, localRule);
            flag = true;
        }
        if (flag) {
            saveOrUpdateSonarSyncResult(sonarSyncResult);
        }
    }

    private void processAbsence(Profile remoteProfile, Profile localProfile, Rule remoteRule) {
        SonarSyncResult sonarSyncResult = getSonarSyncResult(remoteProfile.getKey(), localProfile.getKey(), remoteRule);
        //本地没有该规则
        sonarSyncResult.setAbsence(true);
        saveOrUpdateSonarSyncResult(sonarSyncResult);
        LOGGER.info("规则缺失，远程sonar的规则ID:{}，severity:{}，status:{}，type:{}，本地sonar上没有", remoteRule.getKey(), remoteRule.getSeverity(), remoteRule.getStatus(),remoteRule.getType());
    }

    private SonarSyncResult getSonarSyncResult(String remoteProfileKey, String localProfileKey, Rule remoteRule) {
        SonarSyncResult sonarSyncResult = new SonarSyncResult();
        sonarSyncResult.setLocalProfileKey(localProfileKey);
        sonarSyncResult.setRemoteProfileKey(remoteProfileKey);
        sonarSyncResult.setRuleKey(remoteRule.getKey());
        sonarSyncResult.setLanguage(remoteRule.getLangName());
        sonarSyncResult.setCreatedTime(new Date());
        sonarSyncResult.setUpdatedTime(new Date());
        return sonarSyncResult;
    }

    private void processStatusDifference(SonarSyncResult sonarSyncResult, Rule remoteRule, Rule localRule) {
        LOGGER.info("规则的status不一样，规则ID:{}，远程status:{},本地status:{}",remoteRule.getKey(), remoteRule.getStatus(), localRule.getStatus());
        sonarSyncResult.setRemoteStatus(remoteRule.getStatus());
        sonarSyncResult.setLocalStatus(localRule.getStatus());
    }

    private void processSeverityDifference(SonarSyncResult sonarSyncResult, Rule remoteRule, Rule localRule) {
        LOGGER.info("规则的severity不一样，规则ID:{}，远程severity:{},本地severity:{}",remoteRule.getKey(), remoteRule.getSeverity(), localRule.getSeverity());
        sonarSyncResult.setLocalSeverity(localRule.getSeverity());
        sonarSyncResult.setRemoteSeverity(remoteRule.getSeverity());
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

    private Rule getAllRemoteRule(String languageName) {
        String url = "http://%s:%d/api/rules/search?activation=true&languages=?";
        url = String.format(url, sonarSyncComponent.getRemoteHost(), sonarSyncComponent.getRemotePort());
        return null;//restTemplateComponent.getRestTemplateRemote().getForObject(url,ProfilesActions.class);
    }

    private ProfilesActions getAllRemoteProfileRule(String languageName) {
        String url = "http://%s:%d/api/qualityprofiles/search?language=%s";
        url = String.format(url, sonarSyncComponent.getRemoteHost(), sonarSyncComponent.getRemotePort());
        return restTemplateComponent.getRestTemplateRemote().getForObject(url, ProfilesActions.class);
    }

    private ProfilesActions getAllLocalProfileRule(String languageName) {
        String url = "http://%s:%d/api/qualityprofiles/search?language=%s";
        url = String.format(url, sonarSyncComponent.getLocalHost(), sonarSyncComponent.getLocalPort());
        return restTemplateComponent.getRestTemplateRemote().getForObject(url, ProfilesActions.class);
    }

    private ProfilesActions getLocalAllProfilesActions() {
        String url = "http://%s:%d/api/qualityprofiles/search?defaults=true";
        url = String.format(url, sonarSyncComponent.getLocalHost(), sonarSyncComponent.getLocalPort());
        return restTemplateComponent.getRestTemplateLocal().getForObject(url, ProfilesActions.class);
    }

//    private RulePage getRemoteRulePage(String languageName, int page, int pageSize) {
//        String url = "http://%s:%d/api/rules/search?activation=true&languages=%s&p=%d&ps=%d&facets=types";
//        url = String.format(url, sonarSyncComponent.getRemoteHost(), sonarSyncComponent.getRemotePort(), languageName, page, pageSize);
//        return restTemplateComponent.getRestTemplateRemote().getForObject(url, RulePage.class);
//    }
//
//    private RulePage getLocalRulePage(String profileKey, int page, int pageSize) {
//        String url = "http://%s:%d/api/rules/search?activation=true&languages=%s&p=%d&ps=%d&facets=types";
//        url = String.format(url, sonarSyncComponent.getLocalHost(), sonarSyncComponent.getLocalPort(), profileKey, page, pageSize);
//        return restTemplateComponent.getRestTemplateLocal().getForObject(url, RulePage.class);
//    }

    private RulePage getRemoteRulePage(String profileKey, int page, int pageSize) {
        String url = "http://%s:%d/api/rules/search?qprofile=%s&activation=true&p=%d&ps=%d&facets=types";
        url = String.format(url, sonarSyncComponent.getRemoteHost(), sonarSyncComponent.getRemotePort(), profileKey, page, pageSize);
        return restTemplateComponent.getRestTemplateRemote().getForObject(url, RulePage.class);
    }

    private RulePage getLocalRulePage(String profileKey, int page, int pageSize) {
        String url = "http://%s:%d/api/rules/search?qprofile=%s&activation=true&p=%d&ps=%d&facets=types";
        url = String.format(url, sonarSyncComponent.getLocalHost(), sonarSyncComponent.getLocalPort(), profileKey, page, pageSize);
        return restTemplateComponent.getRestTemplateRemote().getForObject(url, RulePage.class);
    }

    private LinkedList<Rule> getAllRemoteRulePage(String languageName) {
        LinkedList<Rule> rules = new LinkedList<>();
        int page = 1;
        int pageSize = 200;
        RulePage rulePage = getRemoteRulePage(languageName, page, pageSize);
        while (rulePage.getP() <= getTotalPageNum(pageSize, rulePage.getTotal())) {
            rules.addAll(rulePage.getRules());
            rulePage = getRemoteRulePage(languageName, rulePage.getP() + 1, pageSize);
        }
        return rules;
    }

    private LinkedList<Rule> getAllLocalRulePage(String lanaguageName) {
        LinkedList<Rule> rules = new LinkedList<>();
        int page = 1;
        int pageSize = 200;
        RulePage rulePage = getLocalRulePage(lanaguageName, page, pageSize);
        while (rulePage.getP() <= getTotalPageNum(pageSize, rulePage.getTotal())) {
            rules.addAll(rulePage.getRules());
            rulePage = getLocalRulePage(lanaguageName, rulePage.getP() + 1, pageSize);
        }
        return rules;
    }
}
