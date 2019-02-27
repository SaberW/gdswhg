package com.creatoo.hn.web.interceptor;

import com.creatoo.hn.controller.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 所有请求拦截
 * Created by wangxl on 2017/7/11.
 */
@Component
public class AllRequestInterceptor implements HandlerInterceptor {
    /**
     * 日志配置
     */
    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler) throws Exception {
        try {
            Object basePath = httpServletRequest.getServletContext().getAttribute("basePath");
            if (basePath == null) {
                //获取绝对路径
                basePath = httpServletRequest.getContextPath();//httpServletRequest.getScheme() + "://" + httpServletRequest.getServerName() + ":" + httpServletRequest.getServerPort() + httpServletRequest.getContextPath();
                //if (80 == httpServletRequest.getServerPort()) {
                //    basePath = httpServletRequest.getScheme() + "://" + httpServletRequest.getServerName() + httpServletRequest.getContextPath();
                //}
                httpServletRequest.getServletContext().setAttribute("basePath", basePath);
            }

            Object baseImgPath = httpServletRequest.getServletContext().getAttribute("baseImgPath");
            if (baseImgPath == null) {
                //获取绝对路径
                HandlerMethod handlerMethod = (HandlerMethod) handler;
                if (handlerMethod.getBean() instanceof BaseController) {

                    BaseController obj = (BaseController) handlerMethod.getBean();
                    baseImgPath = obj.getProperty("upload.local.server.addr");
                    httpServletRequest.getServletContext().setAttribute("baseImgPath", baseImgPath);
                }

            }
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }

        //System.out.println(">>>MyInterceptor2>>>>>>>在请求处理之前进行调用（Controller方法调用之前）");
        return true;// 只有返回true才会继续向下执行，返回false取消当前请求
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object o, ModelAndView modelAndView) throws Exception {
        //System.out.println(">>>MyInterceptor2>>>>>>>请求处理之后进行调用，但是在视图被渲染之前（Controller方法调用之后）");
        if(response.getStatus()==500){
            modelAndView.addObject("exception", new Exception("500"));
            modelAndView.setViewName("/error");
        }else if(response.getStatus()==404){
            modelAndView.addObject("exception", new Exception("404"));
            modelAndView.setViewName("/error");
        }else if(response.getStatus()==403){
            modelAndView.addObject("exception", new Exception("403"));
            modelAndView.setViewName("/error");
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        //System.out.println(">>>MyInterceptor2>>>>>>>在整个请求结束之后被调用，也就是在DispatcherServlet 渲染了对应的视图之后执行（主要是用于进行资源清理工作）");
    }
}
