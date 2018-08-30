/**
 * Copyright(c): 2018 com.mjduan All rights reserved.
 * 项目名：syn_sonar
 * 注意：未经作者允许，不得外传
 */
package com.xinchen.syn_sonar.sonar;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

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
public class SonarSyncCompareProcessor {
    private static final Logger LOGGER = LoggerFactory.getLogger(SonarSyncComponent.class);
    private ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    @Autowired
    private RestTemplateComponent restTemplateComponent;

    @Autowired
    private SonarSyncComponent sonarSyncComponent;
    @Autowired
    private SonarSyncResultService sonarSyncResultService;

    Profile getRemoteProfileWithLanaguageName(List<Profile> remoteProfiles, String languageName) {
        for (Profile profile : remoteProfiles) {
            if (profile.getLanguageName().equalsIgnoreCase(languageName)) {
                return profile;
            }
        }
        throw new RuntimeException("远程没有该语言的规则");
    }

    Profile getLocalProfileWithLanguageName(List<Profile> localProfiles, String languageName) {
        for (Profile profile : localProfiles) {
            if (profile.getLanguageName().equalsIgnoreCase(languageName)) {
                return profile;
            }
        }
        return null;
    }

    /**
     * 同步远程服务器和本地服务器上的规则
     */
    public void compare() {
        threadLocal.set(sonarSyncResultService.getRecentVersion().longValue() + 1);
        long startTime = System.currentTimeMillis();
        LOGGER.info("同步任务开始执行");
        List<Profile> remoteProfiles = getRemoteAllProfilesActions().getProfiles();
        List<Profile> localProfiles = getLocalAllProfilesActions().getProfiles();
        LOGGER.info("远程sonar上获得的profile个数是:{}", remoteProfiles.size());
        for (Profile remoteProfile : remoteProfiles) {
            Profile localProfile = getLocalProfile(remoteProfile.getLanguageName(), localProfiles);
            if (localProfile == null) {
                //本地没有该语言的规则，规则全部记录到数据库
                LinkedList<Rule> allRemoteRulePage = getAllActiveRemoteRulePage(remoteProfile.getKey());
                for (Rule rule : allRemoteRulePage) {
                    processAbsence(remoteProfile, null, rule);
                }
            } else {
                try {
                    //取出远程所有的active的规则
                    LinkedList<Rule> allRemoteActiveRulePage = getAllActiveRemoteRulePage(remoteProfile.getKey());
                    //取出本地所有的规则，只包括Active
                    LinkedList<Rule> allLocalActiveRulePage = getAllActiveLocalRulePage(localProfile.getKey());
                    //取出本地所有的规则，只包括inactive的
                    LinkedList<Rule> allLocalInactiveRulePage = getAllLocalInactiveRulePage(localProfile.getKey());
                    for (Rule remoteRule : allRemoteActiveRulePage) {
                        boolean localRuleExistAndActive = false;
                        Rule localRule = null;
                        for (int i = 0; i < allLocalActiveRulePage.size(); i++) {
                            localRule = allLocalActiveRulePage.get(i);
                            if (remoteRule.getKey().equalsIgnoreCase(localRule.getKey())) {
                                localRuleExistAndActive = true;
                                break;
                            }
                        }
                        if (localRuleExistAndActive) {
                            //本地有该规则，且处于active，但是有可能本地的规则与远程的一样，或者是不一样，要逐个属性比较
                            processDifference(remoteProfile.getKey(), localProfile.getKey(), remoteRule, localRule);
                        } else {
                            //本地也许有规则，但本地的该规则是inactive的，也许本地没有该规则
                            Rule localInactiveRule = isLocalInactiveRule(allLocalInactiveRulePage, remoteRule);
                            if (null != localInactiveRule) {
                                //本地有该规则，但是出于inactive状态
                                processLocalInactiveRule(remoteProfile, localProfile, remoteRule, localInactiveRule);
                            } else {
                                //本地没有该规则，缺失了当前这条规则，记录数据库
                                processAbsence(remoteProfile, localProfile, remoteRule);
                            }
                        }
                    }
                } catch (Exception e) {
                    LOGGER.error(e.getMessage(), e);
                }
            }
        }
        LOGGER.info("同步任务结束执行，用时{}毫秒", (System.currentTimeMillis() - startTime));
        threadLocal.remove();
    }

    private void processLocalInactiveRule(Profile remoteProfile, Profile localProfile, Rule remoteRule, Rule localInactiveRule) {
        SonarSyncResult sonarSyncResult = getSonarSyncResult(remoteProfile.getKey(), localProfile.getKey(), localInactiveRule);
        processSeverityDifference(sonarSyncResult, remoteRule, localInactiveRule);
        processStatusDifference(sonarSyncResult, remoteRule, localInactiveRule);
        processTypeDifference(sonarSyncResult, remoteRule, localInactiveRule);
        sonarSyncResult.setNeedLocalActive(true);
        sonarSyncResult.setRuleName(localInactiveRule.getName());
        saveOrUpdateSonarSyncResult(sonarSyncResult);
    }

    private Rule isLocalInactiveRule(LinkedList<Rule> allLocalInactiveRulePage, Rule remoteRule) {
        for (Rule rule : allLocalInactiveRulePage) {
            if (rule.getKey().equalsIgnoreCase(remoteRule.getKey())) {
                //本地有该规则，但是出于inactive状态
                return rule;
            }
        }
        return null;
    }

    Profile getLocalProfile(String remoteLanguageName, List<Profile> localProfiless) {
        for (Profile profile : localProfiless) {
            if (remoteLanguageName.equalsIgnoreCase(profile.getLanguageName())) {
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
        processSeverityDifference(sonarSyncResult, remoteRule, localRule);
        if (!remoteRule.getSeverity().equalsIgnoreCase(localRule.getSeverity())) {
            //severity不同
            flag = true;
        }
        processStatusDifference(sonarSyncResult, remoteRule, localRule);
        if (!remoteRule.getStatus().equalsIgnoreCase(localRule.getStatus())) {
            //status不同
            flag = true;
        }
        processTypeDifference(sonarSyncResult, remoteRule, localRule);
        if (!remoteRule.getType().equalsIgnoreCase(localRule.getType())) {
            //type不同
            flag = true;
        }
        if (flag) {
            //规则不一样，写入数据库
            sonarSyncResult.setRuleName(remoteRule.getName());
            saveOrUpdateSonarSyncResult(sonarSyncResult);
        }
    }

    private void processTypeDifference(SonarSyncResult sonarSyncResult, Rule remoteRule, Rule localRule) {
        LOGGER.info("规则的type不一样，规则ID:{}，远程type:{}, 本地type:{}", remoteRule.getKey(), remoteRule.getType(), localRule.getType());
        sonarSyncResult.setRemoteType(remoteRule.getType());
        sonarSyncResult.setLocalType(localRule.getType());
    }

    private void processAbsence(Profile remoteProfile, Profile localProfile, Rule remoteRule) {
        SonarSyncResult sonarSyncResult = getSonarSyncResult(remoteProfile.getKey(), localProfile == null ? null : localProfile.getKey(), remoteRule);
        //本地没有该规则
        sonarSyncResult.setAbsence(true);
        sonarSyncResult.setRuleName(remoteRule.getName());
        saveOrUpdateSonarSyncResult(sonarSyncResult);
        LOGGER.info("规则缺失，远程sonar的规则ID:{}，severity:{}，status:{}，type:{}，本地sonar上没有", remoteRule.getKey(), remoteRule.getSeverity(), remoteRule.getStatus(), remoteRule.getType());
    }

    private SonarSyncResult getSonarSyncResult(String remoteProfileKey, String localProfileKey, Rule rule) {
        SonarSyncResult sonarSyncResult = new SonarSyncResult();
        sonarSyncResult.setLocalProfileKey(localProfileKey);
        sonarSyncResult.setRemoteProfileKey(remoteProfileKey);
        sonarSyncResult.setRuleKey(rule.getKey());
        sonarSyncResult.setLanguage(rule.getLangName());
        sonarSyncResult.setCreatedTime(new Date());
        sonarSyncResult.setUpdatedTime(new Date());
        return sonarSyncResult;
    }

    private void processStatusDifference(SonarSyncResult sonarSyncResult, Rule remoteRule, Rule localRule) {
        LOGGER.info("规则的status不一样，规则ID:{}，远程status:{},本地status:{}", remoteRule.getKey(), remoteRule.getStatus(), localRule.getStatus());
        sonarSyncResult.setRemoteStatus(remoteRule.getStatus());
        sonarSyncResult.setLocalStatus(localRule.getStatus());
    }

    private void processSeverityDifference(SonarSyncResult sonarSyncResult, Rule remoteRule, Rule localRule) {
        LOGGER.info("规则的severity不一样，规则ID:{}，远程severity:{},本地severity:{}", remoteRule.getKey(), remoteRule.getSeverity(), localRule.getSeverity());
        sonarSyncResult.setLocalSeverity(localRule.getSeverity());
        sonarSyncResult.setRemoteSeverity(remoteRule.getSeverity());
    }

    private void saveOrUpdateSonarSyncResult(SonarSyncResult sonarSyncResult) {
        //一定要删除后再添加，不然很有可能会堆积
        //sonarSyncResultService.deleteSonarSyncResult(sonarSyncResult.getRuleKey());
        sonarSyncResult.setLanguage(sonarSyncResult.getLanguage().toLowerCase());
        sonarSyncResult.setVersion(threadLocal.get());
        sonarSyncResultService.saveSonarSyncResult(sonarSyncResult);
    }

    ProfilesActions getRemoteAllProfilesActions() {
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

    ProfilesActions getLocalAllProfilesActions() {
        String url = "http://%s:%d/api/qualityprofiles/search?defaults=true";
        url = String.format(url, sonarSyncComponent.getLocalHost(), sonarSyncComponent.getLocalPort());
        return restTemplateComponent.getRestTemplateLocal().getForObject(url, ProfilesActions.class);
    }

//    private RulePage getRemoteActiveRulePage(String languageName, int page, int pageSize) {
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

    private RulePage getRemoteActiveRulePage(String profileKey, int page, int pageSize) {
        String url = "http://%s:%d/api/rules/search?qprofile=%s&activation=true&p=%d&ps=%d&facets=types";
        url = String.format(url, sonarSyncComponent.getRemoteHost(), sonarSyncComponent.getRemotePort(), profileKey, page, pageSize);
        return restTemplateComponent.getRestTemplateRemote().getForObject(url, RulePage.class);
    }

    private RulePage getActiveLocalRulePage(String profileKey, int page, int pageSize) {
        String url = "http://%s:%d/api/rules/search?qprofile=%s&activation=true&p=%d&ps=%d&facets=types";
        url = String.format(url, sonarSyncComponent.getLocalHost(), sonarSyncComponent.getLocalPort(), profileKey, page, pageSize);
        return restTemplateComponent.getRestTemplateRemote().getForObject(url, RulePage.class);
    }

    private RulePage getAllLocalInactiveRulePage(String profileKey, int page, int pageSize) {
        String url = "http://%s:%d/api/rules/search?qprofile=%s&activation=false&p=%d&ps=%d&facets=types";
        url = String.format(url, sonarSyncComponent.getLocalHost(), sonarSyncComponent.getLocalPort(), profileKey, page, pageSize);
        return restTemplateComponent.getRestTemplateRemote().getForObject(url, RulePage.class);
    }

    LinkedList<Rule> getAllActiveRemoteRulePage(String languageName) {
        LinkedList<Rule> rules = new LinkedList<>();
        int page = 1;
        int pageSize = 200;
        RulePage rulePage = getRemoteActiveRulePage(languageName, page, pageSize);
        while (rulePage.getP() <= getTotalPageNum(pageSize, rulePage.getTotal())) {
            rules.addAll(rulePage.getRules());
            rulePage = getRemoteActiveRulePage(languageName, rulePage.getP() + 1, pageSize);
        }
        return rules;
    }

    LinkedList<Rule> getAllActiveLocalRulePage(String lanaguageName) {
        LinkedList<Rule> rules = new LinkedList<>();
        int page = 1;
        int pageSize = 200;
        RulePage rulePage = getActiveLocalRulePage(lanaguageName, page, pageSize);
        while (rulePage.getP() <= getTotalPageNum(pageSize, rulePage.getTotal())) {
            rules.addAll(rulePage.getRules());
            rulePage = getActiveLocalRulePage(lanaguageName, rulePage.getP() + 1, pageSize);
        }
        return rules;
    }

    LinkedList<Rule> getAllLocalInactiveRulePage(String lanaguageName) {
        LinkedList<Rule> rules = new LinkedList<>();
        int page = 1;
        int pageSize = 200;
        RulePage rulePage = getAllLocalInactiveRulePage(lanaguageName, page, pageSize);
        while (rulePage.getP() <= getTotalPageNum(pageSize, rulePage.getTotal())) {
            rules.addAll(rulePage.getRules());
            rulePage = getAllLocalInactiveRulePage(lanaguageName, rulePage.getP() + 1, pageSize);
        }
        return rules;
    }
}
