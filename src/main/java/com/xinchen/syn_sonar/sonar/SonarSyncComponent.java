/**
 * Copyright(c): 2018 com.mjduan All rights reserved.
 * 项目名：syn_sonar
 * 注意：未经作者允许，不得外传
 */
package com.xinchen.syn_sonar.sonar;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

/**
 * @author dmj1161859184@126.com 2018-08-27 00:17
 * @version 1.0
 * @since 1.0
 */
@Component
//@ConfigurationProperties(prefix = "sonarsync")
@Validated
public class SonarSyncComponent {
    @NotEmpty
    private String localhost;
    @NotNull
    private Integer localport;

    @NotEmpty
    private String remotehost;
    @NotNull
    private Integer remoteport;

    public SonarSyncComponent() {
        localhost = "localhost";
        localport = 9000;

        remotehost = localhost;
        remoteport = localport;
    }

    public String getLocalhost() {
        return localhost;
    }

    public void setLocalhost(String localhost) {
        this.localhost = localhost;
    }

    public Integer getLocalport() {
        return localport;
    }

    public void setLocalport(Integer localport) {
        this.localport = localport;
    }

    public String getRemotehost() {
        return remotehost;
    }

    public void setRemotehost(String remotehost) {
        this.remotehost = remotehost;
    }

    public Integer getRemoteport() {
        return remoteport;
    }

    public void setRemoteport(Integer remoteport) {
        this.remoteport = remoteport;
    }
}
