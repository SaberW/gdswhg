package com.creatoo.hn.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by LENUVN on 2017/9/1.
 */
public class XSSFilter implements Filter {

    FilterConfig filterConfig = null;
    private String filterChar;//需要过滤的字符串
    private String replaceChar;//替代需要过滤的字符串对应的字符串
    private String splitChar;//分隔符
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterChar=filterConfig.getInitParameter("FilterChar");
        this.replaceChar=filterConfig.getInitParameter("ReplaceChar");
        this.splitChar=filterConfig.getInitParameter("SplitChar");
        this.filterConfig = filterConfig ;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        filterChain.doFilter(new XssHttpServletRequestWrapper(
                (HttpServletRequest) request,filterChar,replaceChar,splitChar), response);
    }

    @Override
    public void destroy() {
        this.filterConfig = null;
    }

}
