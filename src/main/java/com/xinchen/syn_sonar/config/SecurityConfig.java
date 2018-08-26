package com.xinchen.syn_sonar.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.AccessDeniedHandler;

/**
 * @author Xin Chen (xinchenmelody@gmail.com)
 * @version 1.0
 * @date Created In 2018/8/25 2:48
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private AccessDeniedHandler accessDeniedHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
//                .loginPage("/login").permitAll().defaultSuccessUrl("/",true).failureForwardUrl("/login?erorr").and()
//                .logout().permitAll().and()

                .authorizeRequests()
                .antMatchers("/autoSyn*")
//                .anyRequest()
                .authenticated().and().exceptionHandling().accessDeniedHandler(accessDeniedHandler);
    }
}
