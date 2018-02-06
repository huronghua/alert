package com.banmatrip.alert.config;

import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @author Eric-hu
 * @Description:
 * @create 2017-11-07 19:14
 * @Copyright: 2017 www.banmatrip.com All rights reserved.
 **/

@Configuration
@EnableOAuth2Sso
public class SecurityConfigure extends WebSecurityConfigurerAdapter {



    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.antMatcher("/**")
                .authorizeRequests().antMatchers("/oauth/**").permitAll()
                .antMatchers("/img/**","/js/**","/css/**","/fonts/**","/static/**").permitAll()
                .antMatchers("/order/**").permitAll()
                .antMatchers("/alertMessageCount/**").permitAll()
                .antMatchers("/alertMessage/**").permitAll()
                .antMatchers("/getAlertMessageDetail/**").permitAll()
                .antMatchers("/rest/**").permitAll()
                    .anyRequest()
                        .authenticated();

    }
}
