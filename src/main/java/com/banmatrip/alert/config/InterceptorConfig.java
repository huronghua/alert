package com.banmatrip.alert.config;

import com.banmatrip.alert.interceptor.SessionInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @author Eric-hu
 * @Description:
 * @create 2018-01-10 15:19
 * @Copyright: 2018 www.banmatrip.com All rights reserved.
 **/
@Configuration
public class InterceptorConfig extends WebMvcConfigurerAdapter {
    @Bean
    public SessionInterceptor sessionInterceptor(){
        return new SessionInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(sessionInterceptor()).addPathPatterns("/**")
                .excludePathPatterns("/order/**")
                .excludePathPatterns("/alertMessageCount/**")
                .excludePathPatterns("/alertMessage/**")
                .excludePathPatterns("/getAlertMessageDetail/**")
                .excludePathPatterns("/rest/**");
    }
}