package com.creatoo.hn.web.filter;

import com.creatoo.hn.web.servlet.XssHttpServletRequestWrapper;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by LENUVN on 2017/9/1.
 */
@WebFilter(filterName = "myfilter", urlPatterns = "/api/*",
        initParams = {
                @WebInitParam(name = "SplitChar", value = "@"),
                @WebInitParam(name = "ReplaceChar", value = "；@＆@＜@‘@“@＼@＃@％@ｓｅｌｅｃｔ @ｄｅｌｅｔｅ @ｕｐｄａｔｅ @ｉｎｓｅｒｔ @ｏｒ@ａｎｄ@＞"),
                @WebInitParam(name = "FilterChar", value = ";@&amp;@&lt;@'@\"@\\@#@%@select @delete @update @insert @or @and @>")
        })
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
