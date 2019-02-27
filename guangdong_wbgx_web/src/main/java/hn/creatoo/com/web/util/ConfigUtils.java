package hn.creatoo.com.web.util;

import hn.creatoo.com.web.config.ApiConfig;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * 配置文件
 * Created by wangxl on 2017/6/6.
 */
@Component
public class ConfigUtils {
    @Resource
    private ApiConfig apiConfigAutowired;

    /** Api配置 */
    private static ApiConfig apiConfig;

    @PostConstruct
    public void init() {
        apiConfig = this.apiConfigAutowired;
    }

    public static ApiConfig getApiConfig(){
        return apiConfig;
    }
}
