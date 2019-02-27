package hn.creatoo.com.web.interceptor;

import com.google.gson.JsonObject;
import hn.creatoo.com.web.mode.WhUser;
import hn.creatoo.com.web.util.JsonUtils;
import hn.creatoo.com.web.util.RestUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 会话拦截
 * Created by wangxl on 2017/6/1.
 */
public class LoginInterceptor extends HandlerInterceptorAdapter {
    public final static String sessionUserKey = "wbgx_sessUserKey";
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        boolean isOK = true;
        WhUser user = (WhUser) request.getSession().getAttribute(sessionUserKey);
        String visitType = request.getParameter("visitType");// 外网访问 1
        if(visitType!=null&&user==null) {
            String phoneNum = request.getParameter("phoneNum");
            String password = request.getParameter("password");
            if(phoneNum!=null) {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userName", phoneNum);
                params.put("password", password);
                params.put("visitType", visitType);
                RestTemplate restTemplate = new RestTemplate();
                String a = RestUtils.post(restTemplate, "api/user/doLogin", params);
                JsonObject json = JsonUtils.toJsonObj(a);
                 user = JsonUtils.toJavaObj(json.get("data").toString(), WhUser.class);
                if (user != null)
                    request.getSession().setAttribute(CommInterceptor.sessionUserKey, user);
            }
        }
        String cultsite = "/default";
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie != null && cookie.getName() != null && "cultsite".equals(cookie.getName())) {
                    cultsite = "/" + cookie.getValue();
                }
            }
        }
        if(user == null){
            isOK = false;
            if(request.getServerPort() == 80){
                response.sendRedirect(request.getScheme() + "://" + request.getServerName() + request.getContextPath()+cultsite+"/login" );
            }else{
                response.sendRedirect(request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath()+cultsite+"/login" );
            }
        }
        return isOK;
        //return super.preHandle(request, response, handler);
    }
}
