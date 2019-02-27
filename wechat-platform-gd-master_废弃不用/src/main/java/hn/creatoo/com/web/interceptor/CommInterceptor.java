package hn.creatoo.com.web.interceptor;

import hn.creatoo.com.web.config.ApiUrlsConfig;
import hn.creatoo.com.web.util.ConfigUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 会话拦截
 * Created by wangxl on 2017/6/1.
 */
@PropertySource("classpath:application.properties")
public class CommInterceptor extends HandlerInterceptorAdapter {
    public final static String sessionUserKey = "sessUserKey";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String staticUrl = ConfigUtils.getApiConfig().getStaticPath();
        String rootUrl = ConfigUtils.getApiConfig().getRoot();
        //获取绝对路径
        String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();
        if(80 == request.getServerPort()){
            basePath = request.getScheme()+"://"+request.getServerName()+request.getContextPath();
        }
        request.setAttribute( "basePath", basePath);
        request.setAttribute( "imgPath",staticUrl);
        //设置接口API
        Map<String, Object> jsonObject = new HashMap<String, Object>();


        Map<String, String> urls = ConfigUtils.getApiUrls();

        for (Map.Entry<String, String> ent : urls.entrySet()) {
            jsonObject.put(ent.getKey(), rootUrl + ent.getValue());
        }
        jsonObject.put("province","广东省");


        request.setAttribute("apiPath",jsonObject);
        return super.preHandle(request, response, handler);
    }
}
