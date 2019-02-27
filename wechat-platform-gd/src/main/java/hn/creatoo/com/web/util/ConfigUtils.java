package hn.creatoo.com.web.util;

import hn.creatoo.com.web.config.ApiConfig;
import hn.creatoo.com.web.config.ApiUrlsConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 配置文件
 * Created by wangxl on 2017/6/6.
 */
@Component
public class ConfigUtils {
    @Resource
    private ApiConfig apiConfigAutowired;

    @Autowired
    private ApiUrlsConfig apiUrlsConfig;

    /** Api配置 */
    private static ApiConfig apiConfig;

    private static Map<String, String> apiUrls = new HashMap<>();

    @PostConstruct
    public void init() {
        apiConfig = this.apiConfigAutowired;
        apiUrls = this.apiUrlsConfig.getUrls();
    }

    public static ApiConfig getApiConfig(){
        return apiConfig;
    }

    public static Map<String, String> getApiUrls(){
        return apiUrls;
    }
}
