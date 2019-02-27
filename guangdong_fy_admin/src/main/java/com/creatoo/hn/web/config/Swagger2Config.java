package com.creatoo.hn.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger2配置
 * 访问地址：http://localhost:8080/doc.html
 * Created by wangxl on 2017/10/11.
 */
@Configuration
@EnableSwagger2
public class Swagger2Config {
    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor(){
        return new MethodValidationPostProcessor();
    }

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.creatoo.hn"))
                .paths(PathSelectors.any())
                //.paths(PathSelectors.regex("/api/.*"))
                .build().apiInfo(apiInfo()).useDefaultResponseMessages(false);
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("广东省文化联盟非遗平台")
                .description("接口说明文档")
                .contact(new Contact("wangxl", "http://hn.creatoo.cn", "36762196@qq.com"))
                .version("1.0")
                .termsOfServiceUrl("http://localhost:8080/")
                .build();
    }
}
