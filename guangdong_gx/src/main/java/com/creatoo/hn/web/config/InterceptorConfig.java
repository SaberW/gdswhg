package com.creatoo.hn.web.config;

import com.creatoo.hn.web.interceptor.AdminSessionInterceptor;
import com.creatoo.hn.web.interceptor.AllRequestInterceptor;
import com.creatoo.hn.web.interceptor.FrontSessionInterceptor;
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
    @Bean
    FrontSessionInterceptor frontSessionInterceptor() { return new FrontSessionInterceptor(); }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(allRequestInterceptor()).addPathPatterns("/**");
        registry.addInterceptor(adminSessionInterceptor()).addPathPatterns("/admin**","/admin/**").excludePathPatterns("/admin/mass/libres/notify", "/admin/login", "/admin/doLogin", "/admin/yunwei/area/findProvinceCityArea", "/admin/yunwei/area/findCodeByName");
        registry.addInterceptor(frontSessionInterceptor()).addPathPatterns("/api/mass/**");
        super.addInterceptors(registry);
    }
}
