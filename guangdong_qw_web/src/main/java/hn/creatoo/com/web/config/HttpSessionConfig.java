package hn.creatoo.com.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.config.annotation.web.http.SpringHttpSessionConfiguration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.http.CookieHttpSessionStrategy;

/**
 * 通过session配置，解决不同应用的跨域问题
 */
@Configuration
@EnableRedisHttpSession
public class HttpSessionConfig {
    @Bean
    public CookieHttpSessionStrategy cookieHttpSessionStrategy() {
        CookieHttpSessionStrategy strategy = new CookieHttpSessionStrategy();
        strategy.setCookieSerializer(new CustomerCookiesSerializer());
        return strategy;
    }

    @Bean
    public SpringHttpSessionConfiguration springHttpSessionConfiguration(){
        SpringHttpSessionConfiguration configuration = new SpringHttpSessionConfiguration();
        configuration.setCookieSerializer(new CustomerCookiesSerializer());
        return configuration;
    }
}
