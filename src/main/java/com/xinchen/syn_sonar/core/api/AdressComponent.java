package com.xinchen.syn_sonar.core.api;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotEmpty;

/**
 * 调用地址
 * @author Xin Chen (xinchenmelody@gmail.com)
 * @version 1.0
 * @date Created In 2018/9/1 4:50
 */
@Data
@Component
public class AdressComponent {

    @NotEmpty
    @Value("${sonar.local.url}")
    private String localUrl;


    @NotEmpty
    @Value("${sonar.local.username}")
    private String localUsername;
    @NotEmpty
    @Value("${sonar.local.password}")
    private String localPassword;

    @NotEmpty
    @Value("${sonar.remote.url}")
    private String remoteUrl;
}
