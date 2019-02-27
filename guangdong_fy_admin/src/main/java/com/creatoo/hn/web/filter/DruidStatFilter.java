package com.creatoo.hn.web.filter;

import com.alibaba.druid.support.http.WebStatFilter;

import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;

/**
 * Druid连接池的StatFilter过滤器
 * Created by wangxl on 2017/7/12.
 */
@WebFilter(filterName = "druidWebStatFilter", urlPatterns = "",
        initParams = {
                @WebInitParam(name = "exclusions", value = "*.js,*.gif,*.jpg,*.bmp,*.png,*.css,*.ico,/druid")// 忽略资源
        })
public class DruidStatFilter extends WebStatFilter {
}