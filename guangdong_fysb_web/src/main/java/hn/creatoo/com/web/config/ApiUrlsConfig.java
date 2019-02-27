package hn.creatoo.com.web.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by rbg on 2017/10/9.
 */
@Component
@ConfigurationProperties(prefix = "api")
@PropertySource("classpath:apiurls.properties")
public class ApiUrlsConfig {
    private Map<String, String> urls = new HashMap<>();

    public Map<String, String> getUrls() {
        return urls;
    }

    public void setUrls(Map<String, String> urls) {
        this.urls = urls;
    }
}
