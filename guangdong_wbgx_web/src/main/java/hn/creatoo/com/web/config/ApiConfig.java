package hn.creatoo.com.web.config;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * API配置
 * Created by wangxl on 2017/6/6.
 */
@Configuration
@ConfigurationProperties(prefix="api")
public class ApiConfig {
    /**
     * API调用URI根路径
     */
    private String root;

    private String staticPath;

    private String yuMing;

    public String getRoot() {
        return root;
    }

    public String getYuMing() {
        return yuMing;
    }

    public void setYuMing(String yuMing) {
        this.yuMing = yuMing;
    }

    public String getStaticPath() {
        return staticPath;
    }

    public void setRoot(String root) {
        this.root = root;
    }

    public void setStaticPath(String staticPath) {
        this.staticPath = staticPath;
    }
}
