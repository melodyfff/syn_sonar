/**
 * Copyright(c): 2018 com.mjduan All rights reserved.
 * 项目名：syn_sonar
 * 注意：未经作者允许，不得外传
 */
package com.xinchen.syn_sonar.sonar;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.xinchen.syn_sonar.sync.model.Profile;
import com.xinchen.syn_sonar.sync.model.Rule;

import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author dmj1161859184@126.com 2018-08-31 01:00
 * @version 1.0
 * @since 1.0
 */
@Component
public class SonarSyncProcessor {
    private static final Logger LOGGER = LoggerFactory.getLogger(SonarSyncComponent.class);
    @Autowired
    private SonarSyncComponent sonarSyncComponent;
    @Autowired
    private SonarSyncCompareProcessor sonarSyncCompareProcessor;

    //根据语言来同步
    public void sync(String... languageNames) {
        LOGGER.info("同步任务开始执行");
        List<Profile> remoteProfiles = sonarSyncCompareProcessor.getRemoteAllProfilesActions().getProfiles();
        List<Profile> localProfiles = sonarSyncCompareProcessor.getLocalAllProfilesActions().getProfiles();
        for (String languageName : languageNames) {
            doSync(remoteProfiles, localProfiles, languageName);
        }
    }

    private void doSync(List<Profile> remoteProfiles, List<Profile> localProfiles, String languageName) {
        long startTime = System.currentTimeMillis();
        LOGGER.info("同步任务开始执行");
        LOGGER.info("远程sonar上获得的profile个数是:{}", remoteProfiles.size());
        for (Profile remoteProfile : remoteProfiles) {
            if (!remoteProfile.getLanguageName().equalsIgnoreCase(languageName)) {
                continue;
            }
            Profile localProfile = sonarSyncCompareProcessor.getLocalProfile(remoteProfile.getLanguageName(), localProfiles);
            if (localProfile == null) {
                //本地没有该语言的规则，规则全部创建
                LinkedList<Rule> allRemoteRulePage = sonarSyncCompareProcessor.getAllActiveRemoteRulePage(remoteProfile.getKey());
                for (Rule rule : allRemoteRulePage) {
                    try {
                        createRule(rule);
                    } catch (UnsupportedEncodingException e) {
                        LOGGER.error(e.getMessage(), e);
                    } catch (AuthenticationException e) {
                        LOGGER.error(e.getMessage(), e);
                    } catch (IOException e) {
                        LOGGER.error(e.getMessage(), e);
                    }
                }
            } else {
                try {
                    //取出远程所有的active的规则
                    LinkedList<Rule> allRemoteActiveRulePage = sonarSyncCompareProcessor.getAllActiveRemoteRulePage(remoteProfile.getKey());
                    //取出本地所有的规则，只包括Active
                    LinkedList<Rule> allLocalActiveRulePage = sonarSyncCompareProcessor.getAllActiveLocalRulePage(localProfile.getKey());
                    //取出本地所有的规则，只包括inactive的
                    LinkedList<Rule> allLocalInactiveRulePage = sonarSyncCompareProcessor.getAllLocalInactiveRulePage(localProfile.getKey());
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
                            processDifference(remoteRule, localRule);
                            updateRule(localRule);
                        } else {
                            //本地也许有规则，但本地的该规则是inactive的，也许本地没有该规则
                            Rule localInactiveRule = isLocalInactiveRule(allLocalInactiveRulePage, remoteRule);
                            if (null != localInactiveRule) {
                                //本地有该规则，但是处于inactive状态
                                processDifference(remoteRule, localRule);
                                updateRule(localRule);
                                activeLocalRule(localProfile.getKey(), localRule.getKey());
                            } else {
                                //本地没有该规则，缺失了当前这条规则，在本地创建该规则
                                createRule(localRule);
                            }
                        }
                    }
                } catch (Exception e) {
                    LOGGER.error(e.getMessage(), e);
                }
            }
        }
        LOGGER.info("同步任务结束执行，用时{}毫秒", (System.currentTimeMillis() - startTime));
    }

    private void processDifference(Rule remoteRule, Rule localRule) {
        localRule.setSeverity(remoteRule.getSeverity());
        localRule.setStatus(remoteRule.getStatus());
        localRule.setType(remoteRule.getType());
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

    public void activeLocalRule(String profileRule, String ruleKey) throws IOException, AuthenticationException {
        String url = "http://%s:%d/api/qualityprofiles/activate_rule";
        url = String.format(url, sonarSyncComponent.getLocalHost(), sonarSyncComponent.getLocalPort());

        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);

        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair("profile_key", profileRule));
        nameValuePairs.add(new BasicNameValuePair("rule_key", ruleKey));
        nameValuePairs.add(new BasicNameValuePair("severity", "MAJOR"));
        httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

        UsernamePasswordCredentials usernamePasswordCredentials = new UsernamePasswordCredentials(sonarSyncComponent.getLocalUsername(), sonarSyncComponent.getLocalPassword());
        Header authenticate = new BasicScheme().authenticate(usernamePasswordCredentials, httpPost, null);
        httpPost.addHeader(authenticate);

        CloseableHttpResponse response = client.execute(httpPost);
        LOGGER.info("激活规则，规则ID是{}，状态码是code:{}", ruleKey, response.getStatusLine().getStatusCode());
    }

    //http 500
    public void updateRule(Rule rule) throws IOException, AuthenticationException {
        String url = "http://%s:%d/api/rules/update";
        url = String.format(url, sonarSyncComponent.getLocalHost(), sonarSyncComponent.getLocalPort());
        //post请求
        //admin权限
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);

        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair("key", rule.getKey()));
        nameValuePairs.add(new BasicNameValuePair("severity", rule.getSeverity()));
        nameValuePairs.add(new BasicNameValuePair("tags", "backbone"));
        //nameValuePairs.add(new BasicNameValuePair("status",rule.getStatus()));
        //nameValuePairs.add(new BasicNameValuePair("type",rule.getType()));
        httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

        UsernamePasswordCredentials usernamePasswordCredentials = new UsernamePasswordCredentials(sonarSyncComponent.getLocalUsername(), sonarSyncComponent.getLocalPassword());
        Header authenticate = new BasicScheme().authenticate(usernamePasswordCredentials, httpPost, null);
        httpPost.addHeader(authenticate);

        CloseableHttpResponse response = client.execute(httpPost);
        LOGGER.info("更新规则，规则ID是{}，状态码是code:{}", rule.getKey(), response.getStatusLine().getStatusCode());
    }

    //创建规则 http 400
    public void createRule(Rule rule) throws IOException, AuthenticationException {
        String url = "http://%s:%d/api/rules/create";
        url = String.format(url, sonarSyncComponent.getLocalHost(), sonarSyncComponent.getLocalPort());
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);

        ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair("custom_key", rule.getKey()));
        nameValuePairs.add(new BasicNameValuePair("name", rule.getName()));
        nameValuePairs.add(new BasicNameValuePair("params", rule.getParams() != null ? rule.getParams().toString() : null));
        nameValuePairs.add(new BasicNameValuePair("severity", rule.getSeverity()));
        nameValuePairs.add(new BasicNameValuePair("status", rule.getStatus()));
        nameValuePairs.add(new BasicNameValuePair("type", rule.getType()));
        httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

        UsernamePasswordCredentials usernamePasswordCredentials = new UsernamePasswordCredentials(sonarSyncComponent.getLocalUsername(), sonarSyncComponent.getLocalPassword());
        Header authenticate = new BasicScheme().authenticate(usernamePasswordCredentials, httpPost, null);
        httpPost.addHeader(authenticate);
        CloseableHttpResponse response = client.execute(httpPost);
        LOGGER.info("创建规则，规则ID是{}，状态码是code:{}", rule.getKey(), response.getStatusLine().getStatusCode());
    }
}
