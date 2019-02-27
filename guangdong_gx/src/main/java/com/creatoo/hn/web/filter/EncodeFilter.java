package com.creatoo.hn.web.filter;

import org.springframework.web.filter.CharacterEncodingFilter;

import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;

/**
 * 编码过滤器
 * Created by wangxl on 2017/7/13.
 */
@WebFilter(filterName = "CharacterEncodingFilter", urlPatterns = "/*",
        initParams = {
                @WebInitParam(name = "encoding", value = "UTF-8"),
                @WebInitParam(name = "forceEncoding", value = "true")
        })
public class EncodeFilter extends CharacterEncodingFilter {
}
