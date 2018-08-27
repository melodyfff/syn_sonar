/**
 * Copyright(c): 2018 com.mjduan All rights reserved.
 * 项目名：syn_sonar
 * 注意：未经作者允许，不得外传
 */
package com.xinchen.syn_sonar.sonar.thread;

import com.xinchen.syn_sonar.sonar.SonarSyncComponent;
import com.xinchen.syn_sonar.sync.model.RuleActives;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.Callable;

/**
 * @author dmj1161859184@126.com 2018-08-27 00:54
 * @version 1.0
 * @since 1.0
 */
public class RulePullCallable implements Callable<RuleActives> {
    private static final Logger LOGGER = LoggerFactory.getLogger(RulePullCallable.class);
    private RestTemplate restTemplate;
    private SonarSyncComponent sonarSyncComponent;
    private String key;
    private boolean remote;

    public RulePullCallable(RestTemplate restTemplate, SonarSyncComponent sonarSyncComponent, String key, boolean remote) {
        this.restTemplate = restTemplate;
        this.sonarSyncComponent = sonarSyncComponent;
        this.key = key;
        this.remote = remote;
    }

    @Override
    public RuleActives call() throws Exception {
        return getRuleActives(key);
    }

    private RuleActives getRuleActives(String key) {
        String url = "http://%s:%d/api/rules/show?key=%s&actives=true";
        url = String.format(url, getParameters(key, remote));
        try {
            return restTemplate.getForObject(url, RuleActives.class);
        } catch (Exception e) {
            LOGGER.error("sonar服务器连接失败或者，sonar上规则不存在，那么sonar会返回400错误，RestTemplate会抛出异常", e);
        }
        return null;
    }

    private Object[] getParameters(String key, boolean isRemote) {
        return isRemote ? new Object[]{sonarSyncComponent.getRemotehost(), sonarSyncComponent.getRemoteport(), key} :
                new Object[]{sonarSyncComponent.getLocalhost(), sonarSyncComponent.getLocalport(), key};
    }
}
