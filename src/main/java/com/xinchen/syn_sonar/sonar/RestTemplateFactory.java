/**
 * Copyright(c): 2018 com.mjduan All rights reserved.
 * 项目名：syn_sonar
 * 注意：未经作者允许，不得外传
 */
package com.xinchen.syn_sonar.sonar;

import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * @author dmj1161859184@126.com 2018-08-28 21:40
 * @version 1.0
 * @since 1.0
 */
public class RestTemplateFactory {

    private static RestTemplate restTemplate;

    static {
        //设置超时时间
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        //设置为15秒，JDK8上可以用下划线隔开
        requestFactory.setConnectTimeout(15_000);
        requestFactory.setReadTimeout(15_000);

        restTemplate = new RestTemplate(requestFactory);
    }

    public static RestTemplate getRestTemplate(){
        return restTemplate;
    }
}
