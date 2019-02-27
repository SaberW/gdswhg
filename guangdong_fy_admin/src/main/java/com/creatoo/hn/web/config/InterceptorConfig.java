package com.creatoo.hn.web.config;

import com.creatoo.hn.web.interceptor.AdminSessionInterceptor;
import com.creatoo.hn.web.interceptor.AllRequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * 拦截器配置
 * Created by wangxl on 2017/7/11.
 */
@Configuration
public class InterceptorConfig extends WebMvcConfigurerAdapter {
    @Bean
    AllRequestInterceptor allRequestInterceptor() {
        return new AllRequestInterceptor();
    }
    @Bean
    AdminSessionInterceptor adminSessionInterceptor() {
        return new AdminSessionInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(allRequestInterceptor()).addPathPatterns("/**");
        registry.addInterceptor(adminSessionInterceptor()).addPathPatterns("","/","/admin**","/admin/**").excludePathPatterns("/admin/login", "/admin/doLogin");
        super.addInterceptors(registry);
    }
}
