package hn.creatoo.com.web.interceptor;

import hn.creatoo.com.web.mode.WhUser;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
