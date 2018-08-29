/**
 * Copyright(c): 2018 com.mjduan All rights reserved.
 * 项目名：syn_sonar
 * 注意：未经作者允许，不得外传
 */
package com.xinchen.syn_sonar.sonar;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * @author dmj1161859184@126.com 2018-08-28 21:40
 * @version 1.0
 * @since 1.0
 */
@Component
public class RestTemplateComponent implements InitializingBean {
    private RestTemplate restTemplateRemote;
    private RestTemplate restTemplateLocal;

    @Autowired
    private SonarSyncComponent sonarSyncComponent;

    public RestTemplate getRestTemplateLocal(){
        return restTemplateLocal;
    }

    public RestTemplate getRestTemplateRemote(){
        return restTemplateRemote;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        //设置超时时间
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        //设置为15秒，JDK8上可以用下划线隔开
        requestFactory.setConnectTimeout(15_000);
        requestFactory.setReadTimeout(15_000);
        restTemplateLocal = new RestTemplate(requestFactory);
        restTemplateRemote = new RestTemplate(requestFactory);
    }

    public HttpHeaders getHttpHeadersAuthInfo(){
        String user = sonarSyncComponent.getLocalUsername();
        String password = sonarSyncComponent.getLocalPassword();
        String userMsg = user + ":" + password;
        String base64UserMsg = Base64.getEncoder().encodeToString(userMsg.getBytes(StandardCharsets.UTF_8));
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization ", "Basic "+base64UserMsg);
        headers.add("Content-Type", "form-data");
        return  headers;
    }
}
