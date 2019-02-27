package hn.creatoo.com.web.interceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * 重写拦截器配置
 * Created by wangxl on 2017/6/1.
 */
@Configuration
public class ServletContextConfig extends WebMvcConfigurationSupport {
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/assets/**").addResourceLocations("classpath:/static/assets/");
        super.addResourceHandlers(registry);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns(//添加拦截规则
                        //"/doLogin",
                        "/*/usercenter/*",
                        "/*/xxpx/step",
                        "/*/zxkc/step",
                        "/*/zxkc/live"
                ).excludePathPatterns(//排除拦截规则

                );
        registry.addInterceptor(new CommInterceptor()).addPathPatterns("/**");

//        registry.addInterceptor(new SysLoginInterceptor())
//                .addPathPatterns(//添加拦截规则
//                    "/res-*/**"
//                ).excludePathPatterns(//排除拦截规则
//                    "/res-gx/login",
//                    "/res-gx/doLogin",
//                    "/res-gx/doLogout"
//                );

        super.addInterceptors(registry);
    }
}