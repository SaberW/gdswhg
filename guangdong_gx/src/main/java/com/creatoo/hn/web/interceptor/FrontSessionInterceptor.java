package com.creatoo.hn.web.interceptor;

import com.creatoo.hn.util.BaseUtils;
import com.creatoo.hn.util.MD5Util;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * WEB 前端会话拦截
 * Created by wangxl on 2017/7/13.
 */
public class FrontSessionInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        String timestamp = httpServletRequest.getParameter("timestamp");
        Object osign = httpServletRequest.getParameter("token");
        if (osign == null || StringUtils.isBlank(timestamp)) {
            httpServletResponse.setContentType("text/html;charset=UTF-8");
            JSONObject json = new JSONObject();
            json.put("code", "101");
            json.put("data", false);
            json.put("msg", "获取签名失败");
            httpServletResponse.getWriter().print(json);
            return false;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("dev-name","ZJ)6so!-");
        String str = BaseUtils.createLinkString(map);
        String sign = MD5Util.toMd5(str+timestamp).toUpperCase();
        if (!sign.equals(osign.toString().toUpperCase())) {
            httpServletResponse.setContentType("text/html;charset=UTF-8");
            JSONObject json = new JSONObject();
            json.put("code", "102");
            json.put("data", "签名验证失败");
            json.put("data", false);
            httpServletResponse.getWriter().print(json);
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
