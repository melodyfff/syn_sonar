/**
 * Copyright(c): 2018 com.mjduan All rights reserved.
 * 项目名：syn_sonar
 * 注意：未经作者允许，不得外传
 */
package com.xinchen.syn_sonar.sonar;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author dmj1161859184@126.com 2018-08-27 00:17
 * @version 1.0
 * @since 1.0
 */
@Component
public class SonarSyncComponent {
    @NotEmpty
    @Value("${sonarsync.local.host}")
    private String localHost;
    @NotNull
    @Value("${sonarsync.local.port}")
    private Integer localPort;
    @NotEmpty
    @Value("${sonarsync.local.username}")
    private String localUsername;
    @NotEmpty
    @Value("${sonarsync.local.password}")
    private String localPassword;
    @NotEmpty
    @Value("${sonarsync.remote.host}")
    private String remoteHost;
    @NotNull
    @Value("${sonarsync.remote.port}")
    private Integer remotePort;
    @NotEmpty
    @Value("${sonarsync.remote.username}")
    private String remoteUsername;
    @NotEmpty
    @Value("${sonarsync.remote.password}")
    private String remotePassword;

    public String getLocalHttpURL(){
        return "http://"+localHost+":"+localPort;
    }

    public String getRemoteHttpURL(){
        return "http://"+remoteHost+":"+remotePort;
    }

    public String getLocalHost() {
        return localHost;
    }

    public void setLocalHost(String localHost) {
        this.localHost = localHost;
    }

    public Integer getLocalPort() {
        return localPort;
    }

    public void setLocalPort(Integer localPort) {
        this.localPort = localPort;
    }

    public void setRemotePort(Integer remotePort) {
        this.remotePort = remotePort;
    }

    public String getLocalUsername() {
        return localUsername;
    }

    public void setLocalUsername(String localUsername) {
        this.localUsername = localUsername;
    }

    public String getLocalPassword() {
        return localPassword;
    }

    public void setLocalPassword(String localPassword) {
        this.localPassword = localPassword;
    }

    public String getRemoteHost() {
        return remoteHost;
    }

    public void setRemoteHost(String remoteHost) {
        this.remoteHost = remoteHost;
    }

    public String getRemoteUsername() {
        return remoteUsername;
    }

    public void setRemoteUsername(String remoteUsername) {
        this.remoteUsername = remoteUsername;
    }

    public String getRemotePassword() {
        return remotePassword;
    }

    public void setRemotePassword(String remotePassword) {
        this.remotePassword = remotePassword;
    }

    public Integer getRemotePort() {
        return remotePort;
    }
}
