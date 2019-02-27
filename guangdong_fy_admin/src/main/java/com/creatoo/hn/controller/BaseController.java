package com.creatoo.hn.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

/**
 * 基础控制器
 * Created by wangxl on 2017/7/3.
 */
public class BaseController {
    /**
     * 日志配置
     */
    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * 当前环境的配置信息:application[-][test|dev].properties
     * 如：env.getProperty("spring.datasource.password")
     */
    @Autowired
    protected Environment env;

    /**
     * 获取环境变量属性
     * @param name
     * @return
     */
    public String getProperty(String name){
        return this.env.getProperty(name);
    }
}
