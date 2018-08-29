/**
 * Copyright(c): 2018 com.mjduan All rights reserved.
 * 项目名：syn_sonar
 * 注意：未经作者允许，不得外传
 */
package com.xinchen.syn_sonar.sonar;

import java.io.File;

import sun.net.www.http.HttpClient;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.aspectj.weaver.ast.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author dmj1161859184@126.com 2018-08-29 22:49
 * @version 1.0
 * @since 1.0
 */
@Component
public class HttpClientUtil {
    @Autowired
    private SonarSyncComponent sonarSyncComponent;


}
