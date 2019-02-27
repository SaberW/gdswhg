package hn.creatoo.com.web.interceptor;

import com.google.gson.JsonObject;
import hn.creatoo.com.web.config.ApiUrlsConfig;
import hn.creatoo.com.web.mode.WhUser;
import hn.creatoo.com.web.util.ConfigUtils;
import hn.creatoo.com.web.util.JsonUtils;
import hn.creatoo.com.web.util.RestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.client.RestTemplate;
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
        String cultsite=request.getParameter("cultsite");
        if(cultsite!=null){
            request.setAttribute("cultsite",cultsite);
        }
        Map<String, String> urls = ConfigUtils.getApiUrls();

        for (Map.Entry<String, String> ent : urls.entrySet()) {
            jsonObject.put(ent.getKey(), rootUrl + ent.getValue());
        }
        jsonObject.put("province","广东省");
        WhUser user = (WhUser) request.getSession().getAttribute(sessionUserKey);
        if(user!=null) {
            RestTemplate restTemplate = new RestTemplate();
            String a = "";
            Map<String, String> params = new HashMap<String, String>();
            params.put("userId", user.getUserId());
            a = RestUtils.post(restTemplate, "api/user/detail", params);
            JsonObject json = JsonUtils.toJsonObj(a);
            String code = json.get("code").getAsString();
            if ("0".equals(code)) {
                WhUser users = JsonUtils.toJavaObj(json.get("data").toString(), WhUser.class);
                request.getSession().setAttribute(LoginInterceptor.sessionUserKey, users);
            }
        }
        request.setAttribute("apiPath",jsonObject);
        return super.preHandle(request, response, handler);
    }
}
